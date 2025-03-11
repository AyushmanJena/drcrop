package com.example.dr_crop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/") // needs a new controller
    public String showLoginPage(){
        return "login-page";
    }
}
