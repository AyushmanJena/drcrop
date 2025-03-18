//package com.example.dr_crop.Controller;
//
//import com.example.dr_crop.Model.ConditionResult;
//import com.example.dr_crop.Service.ConditionService;
//import com.example.dr_crop.Service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@RestController
//public class ConditionController {
//
//    @Autowired
//    private ConditionService conditionService;
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,@RequestHeader("Authorization") String token, RedirectAttributes redirectAttributes){
//
//        String userId = userService.getUserIdFromToken(token);
//
//        if (userId == null) {
//            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token");
//        }
//
//        String fileName = conditionService.uploadImage(file, redirectAttributes, userId); // rename the file to the new name
//
//        if (fileName != null) {
//            String inputStoragePath = "D:/DR-CROP/dr-crop/input-storage/";
//            // Convert image to Base64 (might add multi part file method later)
//            String base64img = conditionService.convertImageToBase64(inputStoragePath + fileName+".png");
//
//            ConditionResult conditionResult = conditionService.sendImageToMLLayer(base64img);
//
//            if (conditionResult != null) {
//                System.out.println("Plant name: " + conditionResult.plantName);
//                System.out.println("Condition name: " + conditionResult.conditionName);
//                conditionService.convertConditionResultToPdf(conditionResult, fileName);
//                // convert the received data into a pdf file and allow for download
//            }
//            return ResponseEntity.ok(conditionResult);
//        }
//        return ResponseEntity.status(404).body("File upload failed");
//    }
//
//    @GetMapping("/downloadfile") // to download the pdf report
//    public ResponseEntity<InputStreamResource> downloadPdfFile(@RequestHeader("Authorization") String token) throws IOException {
//
//        String userId = userService.getUserIdFromToken(token);
//
//        String id = userId + "img.pdf";
//
//        String directory = "D:/DR-CROP/dr-crop/output-storage/";
//        File file = new File(directory + id);
//
//        // file.renameTo(new File("D:/DR-CROP/dr-crop/output-storage/dr-crop-condition-report.pdf"));
//
//        if (!file.exists()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        MediaType mediaType = MediaType.parseMediaType("application/pdf");
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
//                .contentType(mediaType)
//                .contentLength(file.length())
//                .body(resource);
//    }
//}
