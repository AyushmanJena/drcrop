//package com.example.dr_crop.Controller;
//
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import java.util.Map;
//
//@RestController
//public class ImageHandlingController {
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @PostMapping("/send") // send image to the ml layer
//    public ResponseEntity<Map<String, Object>> sendString() {
//        try {
//            String url = "http://localhost:9091/api/process"; // String processing app URL
//
//            String text = "Hello, this is a test message!";
//
//            // Prepare request body
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("message", text);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//            // Send request to Application B
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
//
//            return ResponseEntity.ok(response.getBody());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Failed to send string to Application B"));
//        }
//    }
//}