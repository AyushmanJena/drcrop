package com.example.dr_crop.Controller;

import com.example.dr_crop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.authenticate(username, password);
        if (token != null) {
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
