package com.example.dr_crop.WebController;

import com.example.dr_crop.Model.ConditionResult;
import com.example.dr_crop.Model.SoilRequest;
import com.example.dr_crop.Model.SoilResult;
import com.example.dr_crop.Service.ConditionService;
import com.example.dr_crop.Service.SoilService;
import com.example.dr_crop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private SoilService soilService;

    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Renders login.html
    }

    @GetMapping("/homepage")
    public String showHomePage(HttpSession session, Model model){
        String token = (String) session.getAttribute("token");
        System.out.println("token1 : " + token);
        if (token == null) {
            return "redirect:/web/login";
        }
        model.addAttribute("token", token);
        return "homepage";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        String token = userService.authenticate(username, password);
        if (token != null) {
            session.setAttribute("token", token);
            return "redirect:/web/homepage";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    // Condition Detection Endpoints

    @GetMapping("/upload")
    public String showUploadPage(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        System.out.println("token1 : " + token);
        if (token == null) {
            return "redirect:/web/login";
        }
        model.addAttribute("token", token);
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        // String token = (String)model.getAttribute("token");
        String token = (String) session.getAttribute("token");
        System.out.println("token2 : " + token);
        if (token == null) {
            return "redirect:/web/login";
        }

        String userId = userService.getUserIdFromToken(token);
        if (userId == null) {
            return "redirect:/web/login";
        }

        String fileName = conditionService.uploadImage(file, redirectAttributes, userId);
        if (fileName != null) {
            // System.out.println(1);
            String inputStoragePath = "D:/DR-CROP/dr-crop/input-storage/";
            String base64img = conditionService.convertImageToBase64(inputStoragePath + fileName + ".png");
            ConditionResult conditionResult = conditionService.sendImageToMLLayer(base64img);
            // System.out.println(2);

            if (conditionResult != null) {
                // System.out.println(3);
                conditionService.convertConditionResultToPdf(conditionResult, fileName);
                model.addAttribute("conditionResult", conditionResult);
                System.out.println(4);
                return "result";
            }
        }
        model.addAttribute("error", "File upload failed");
        return "upload";
    }

    @GetMapping("/downloadfile")
    public ResponseEntity<InputStreamResource> downloadPdfFile(HttpSession session) throws IOException {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userId = userService.getUserIdFromToken(token);
        String filePath = "D:/DR-CROP/dr-crop/output-storage/" + userId + "img.pdf";
        File file = new File(filePath);

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

    // Crop Recommendation Endpoints
    @GetMapping("/soil-recommendation")
    public String showCropRecommendationForm(){
        return "crop-recommendation-form";
    }

    @PostMapping("/soil-recommendation")
    public String getSoilRecommendation(@RequestParam float nitrogen,
                                        @RequestParam float oxygen,
                                        @RequestParam float phosphorus,
                                        HttpSession session, Model model) {

        String token = (String) session.getAttribute("token");
        System.out.println("token : " + token);
        if (token == null) {
            return "redirect:/web/login";
        }

        String userId = userService.getUserIdFromToken(token);
        if (userId == null) {
            return "redirect:/web/login";
        }
        SoilRequest soilRequest = new SoilRequest(nitrogen, oxygen, phosphorus);

        SoilResult soilResult = soilService.getSoilResult(soilRequest);

        if (soilResult != null) {
            model.addAttribute("crop", soilResult.getCrop());
            return "soil-result.html";
        }

        model.addAttribute("error", "Failed to get recommendation");
        return "crop-recommendation-form";
    }
}
