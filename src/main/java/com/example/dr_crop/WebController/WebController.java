package com.example.dr_crop.WebController;

import com.example.dr_crop.Model.ConditionResult;
import com.example.dr_crop.Model.DiseaseMedicine;
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
import java.util.ArrayList;
import java.util.List;

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
    public String showLoginPage(Model model) { // initially asks for email only
        model.addAttribute("otpSent", false);
        return "login";
    }

    @PostMapping("/get-email")
    public String getEmail(@RequestParam String email, Model model, HttpSession session) {
        if (!userService.containsUser(email)) {
            model.addAttribute("error", "Account does not exist");
            model.addAttribute("otpSent", false);
            return "login";
        }

        String generatedOTP = userService.generateOTP();
        userService.sendOtpToMail(email, generatedOTP);  // Ensure OTP is actually sent

        // String generatedOTP = "abc"; // testing otp

        session.setAttribute("generatedOtp", generatedOTP);
        session.setAttribute("email", email);

        model.addAttribute("otpSent", true);
        model.addAttribute("mailId", email);
        return "login";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String otp, HttpSession session, Model model){
        System.out.println(1);
        String generatedOtp = (String) session.getAttribute("generatedOtp");
        String email = (String) session.getAttribute("email");
        System.out.println(2);
        if (generatedOtp == null) {
            model.addAttribute("error", "Session expired. Please try again.");
            model.addAttribute("otpSent", false);
            return "login";
        }
        System.out.println(3);
        System.out.println(email);

        String token = userService.authenticate(email, generatedOtp, otp);
        System.out.println(4);
        System.out.println(token);

        if (token == null) {
            model.addAttribute("error", "Invalid OTP");
            model.addAttribute("otpSent", false);
            return "login";
        }

        session.setAttribute("token", token);
        session.removeAttribute("generatedOtp");  // Remove OTP from session after successful login
        return "homepage";
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
    public String uploadImage(@RequestParam("file") MultipartFile file, @RequestParam String crop, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
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

        System.out.println("file : " + file.getOriginalFilename());
        System.out.println("crop : " + crop);

        String fileName = conditionService.uploadImage(file, redirectAttributes, userId);
        if (fileName != null) {
             System.out.println("upload : " + 1);
            String inputStoragePath = "D:/DR-CROP/dr-crop/input-storage/";
            // String base64img = conditionService.convertImageToBase64(inputStoragePath + fileName + ".png");
            ConditionResult conditionResult = conditionService.sendImageToMLLayer(fileName, crop);
            System.out.println("upload : " + 2);

            if (conditionResult != null) {
                String diseaseName = conditionService.replaceSpaces(conditionResult.conditionName+"-"+capCropName(conditionResult.plantName));
                String conditionUrl = "/web/store/" + diseaseName;//+conditionResult.conditionName;
                model.addAttribute("diseaseMedicine", conditionUrl);
                conditionService.convertConditionResultToPdf(conditionResult, fileName, conditionResult.conditionName+"-"+capCropName(conditionResult.plantName));
                model.addAttribute("conditionResult", conditionResult);
                //model.addAttribute("imageUrl", inputStoragePath+"/"+fileName+ ".jpg");
                return "result";
            }
        }
        model.addAttribute("error", "File upload failed");
        return "upload";
    }

    public String capCropName(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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
                                        @RequestParam float phosphorus,
                                        @RequestParam float potassium,
                                        @RequestParam float temperature,
                                        @RequestParam float humidity,
                                        @RequestParam float pH,
                                        @RequestParam float rainfall,
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
        SoilRequest soilRequest = new SoilRequest(nitrogen, phosphorus, potassium, temperature,humidity, pH, rainfall);

        SoilResult soilResult = soilService.getSoilResult(soilRequest);

        if (soilResult != null) {
            model.addAttribute("crop", soilResult.getCrop());
            model.addAttribute("suggestion", soilResult.getSuggestion());
            return "soil-result.html";
        }

        model.addAttribute("error", "Failed to get recommendation");
        return "crop-recommendation-form";
    }

    // Store Page
    @GetMapping("/store/{diseaseName}")
    public String getStorePage(@PathVariable String diseaseName, Model model){
        List<DiseaseMedicine.Medicine> medicines = new ArrayList<>();
        if(diseaseName.compareTo("all") == 0){
            medicines= conditionService.getAllMedicines();

        }else{
            medicines =conditionService.getMedicinesFromDiseaseName(diseaseName);
        }
        model.addAttribute("medicines", medicines);
        return "store";
    }

    // About us
    @GetMapping("/aboutUs")
    public String aboutUs(){
        return "about-us";
    }
}
