package com.example.dr_crop.WebController;

import com.example.dr_crop.Model.User;
import com.example.dr_crop.Service.ConditionService;
import com.example.dr_crop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/new-user-form")
    public String showUserRegistrationForm(){
        return "accounts/new-user-form";
    }

    @PostMapping("/new-user-form")
    public String getNewUserDetails(@RequestParam String email,
                                    @RequestParam String fname,
                                    @RequestParam String mname,
                                    @RequestParam String lname,
                                    @RequestParam String phone,
                                    Model model){

        if(userService.containsUser(email)){
            model.addAttribute("message", "Account already exists"); // user already exists ask the user to login instead
            return "index";
        }

        String id = userService.createNewUser(email, fname, mname, lname, phone);

        model.addAttribute("message", "account created Successfully");

        return "index";
    }

    @GetMapping("/account-user")
    public String redirectToUserAccount(HttpSession session){
        String token = (String)session.getAttribute("token");
        String userId = userService.getUserIdFromToken(token);

        return "redirect:/accounts/account/"+userId;
    }

    @GetMapping("/account/{id}")
    public String showUserAccount(@PathVariable String id, Model model){
        User user = userService.getUserById(id);
        if(user == null){
            return "accounts/account";
        }
        String middleName;
        if(user.getMname() == null){
            middleName = "";
        }else{
            middleName = user.getMname()+ " ";
        }
        String fullName = user.getFname() + " " + middleName + user.getLname();
        System.out.println(fullName);
        model.addAttribute("name", fullName);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("id", user.getId());
        model.addAttribute("phone", user.getPhone());
        return "accounts/account";
    }

    @GetMapping("/log-out")
    public String returnToIndex(Model model){
        model.addAttribute("message", "Logged Out Successfully");
        return "index";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@RequestParam String email, Model model){
        boolean isDeleted = userService.deleteUser(email);
        if(!isDeleted){
            model.addAttribute("message", "Failed to Delete Account, Contact Support!");
        }else{
            model.addAttribute("message", "Account Deleted Successfully");
        }
        return "index";
    }
}
