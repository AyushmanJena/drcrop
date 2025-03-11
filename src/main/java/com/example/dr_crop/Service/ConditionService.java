package com.example.dr_crop.Service;

import com.example.dr_crop.Model.ConditionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class ConditionService {
    private final RestTemplate restTemplate = new RestTemplate();

    final String UPLOADED_FOLDER = "D:/DR-CROP/dr-crop/input-storage/";
    public String generateId(){
        return "abcde";
    }

    public boolean uploadImage(MultipartFile file, RedirectAttributes redirectAttributes){
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return false;
        }
        try{
            byte[] bytes = file.getBytes();
            String generatedId = generateId();
            Path path = Paths.get(UPLOADED_FOLDER + generatedId + ".png");
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message", "you successfully uploaded '"+ generatedId+ "'");
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Image Upload successful!!!");
        return true;
    }

    public ConditionResult sendImageToMLLayer(String id){
        try {
            String url = "http://localhost:9091/api/process"; // String processing app URL

            String text = "Hello, this is a test message!";

            // Prepare request body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("message", text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send request to Application B
            ResponseEntity<ConditionResult> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ConditionResult.class);

            ConditionResult conditionResult = response.getBody();

            return conditionResult;
        } catch (Exception e) {
            return null;
        }
    }
}