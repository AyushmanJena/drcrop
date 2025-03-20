package com.example.dr_crop.Controller;

import com.example.dr_crop.Model.ConditionResult;
import com.example.dr_crop.Service.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DrCropController { // Might delete this

    @Autowired
    private ConditionService conditionService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/condition")
    public String conditionForm(){
        return "upload-image";
    }

    @PostMapping("/uploadweb")
    public String uploadImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){

        String fileName = "abcde";// conditionService.uploadImage(file, redirectAttributes, userId);

        if (fileName != null){
            // if the image is valid send it to the python app

            String base64img = "abcde";

            ConditionResult conditionResult = conditionService.sendImageToMLLayer(base64img);

            if(conditionResult != null){
                System.out.println("Plant name : " +conditionResult.plantName);
                System.out.println("Condition name : " +conditionResult.conditionName);

                // save the condition results to a pdf of the same name
            }
            return "redirect:download"; // might need changes if output is valid or invalid
        }
        return "redirect:download";
    }

    @GetMapping("/download") // display the page where you can download the file
    public String downloadPage(){
        return "condition-results";
    }

    @GetMapping("/downloadfileweb") // to download the pdf report
    public ResponseEntity<InputStreamResource> downloadPdfFile() throws IOException {

        String id = "abcde.pdf";

        String directory = "D:/DR-CROP/dr-crop/output-storage/";
        File file = new File(directory + id);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        MediaType mediaType = MediaType.parseMediaType("application/pdf");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }
}
