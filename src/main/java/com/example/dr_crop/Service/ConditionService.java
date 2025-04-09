package com.example.dr_crop.Service;

import com.example.dr_crop.Model.ConditionRequest;
import com.example.dr_crop.Model.ConditionResult;
import com.example.dr_crop.Model.DiseaseMedicine;
import com.example.dr_crop.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class ConditionService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MedicineRepository medicineRepository;

    final String UPLOADED_FOLDER = "D:/DR-CROP/dr-crop/input-storage/";

    public String uploadImage(MultipartFile file, RedirectAttributes redirectAttributes, String userId){
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return null;
        }
        try{
            byte[] bytes = file.getBytes();
            String generatedId = userId + "img";
            Path path = Paths.get(UPLOADED_FOLDER + generatedId + ".jpg");
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message", "you successfully uploaded '"+ generatedId+ "'");
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Image Upload successful!!!");
        return userId + "img";
    }

    public ConditionResult sendImageToMLLayer(String id, String cropName) {
        try {
            String url = "http://localhost:5000/leaf"; // ML service URL

            // Creating JSON request body
            ConditionRequest request = new ConditionRequest(id, cropName);

            // Setting headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // Sending JSON

            // Creating HttpEntity with request body and headers
            HttpEntity<ConditionRequest> requestEntity = new HttpEntity<>(request, headers);

            // Sending POST request
            ResponseEntity<ConditionResult> response = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, ConditionResult.class
            );

            // Extracting response
            ConditionResult conditionResult = response.getBody();

            System.out.println("test : 5");
            System.out.println("Plant Name: " + conditionResult.getPlantName());

            return conditionResult;
        } catch (Exception e) {
            e.printStackTrace(); // Print error if exception occurs
            return null;
        }
    }


    public String convertImageToBase64(String fileName) {
        String encoded = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(fileName);
            byte[] bytes = new byte[(int)fileName.length()];
            fileInputStream.read(bytes);
            encoded = new String(Base64.getEncoder().encodeToString(bytes));
        }catch (Exception e){
            e.printStackTrace();
        }
        return encoded;

    }

    public void convertConditionResultToPdf(ConditionResult conditionResult, String filetemp) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try{
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph titlePara = new Paragraph("Dr. Crop Report", titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            document.add(titlePara);

            Font paraFont = FontFactory.getFont(FontFactory.HELVETICA, 16);
            Paragraph paragraph1 = new Paragraph("\nPlant Name : " + conditionResult.plantName, paraFont);
            Paragraph paragraph2 = new Paragraph("\nCondition : " + conditionResult.conditionName, paraFont);
            Paragraph paragraph3 = new Paragraph("\nAccuracy : " + conditionResult.accuracy, paraFont);

            // to add content during runtime
            // paragraph1.add(new Chunk("\nPlant Name : " + paragraph1));
            document.add(paragraph1);
            //paragraph2.add(new Chunk("\nCondition : " + paragraph2));
            document.add(paragraph2);
            document.add(paragraph3);

            document.close();

            // to save the pdf
            String fileName = filetemp+".pdf";
            String directoryPath = "D:/DR-CROP/dr-crop/output-storage/";
            File file = new File(directoryPath + fileName);

            file.getParentFile().mkdirs();
            System.out.println("Successfuly saved pdf file to local repo!!!");

            try(FileOutputStream fileOut = new FileOutputStream(file)){
                fileOut.write(out.toByteArray());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Medicine Store page methods
    public List<DiseaseMedicine.Medicine> getAllMedicines(){
        List<DiseaseMedicine> diseases = medicineRepository.findAll();
        ArrayList<DiseaseMedicine.Medicine> medicines = new ArrayList<>();
        for(DiseaseMedicine d:  diseases){
            medicines.addAll(d.getMedicinesList());
        }
        return medicines;
    }

    public List<DiseaseMedicine.Medicine> getMedicinesFromDiseaseName(String diseaseName){
        Optional<DiseaseMedicine> disease = medicineRepository.findByDiseaseName(diseaseName);
        List<DiseaseMedicine.Medicine> medicines = disease.get().getMedicinesList();
        return medicines;
    }
}