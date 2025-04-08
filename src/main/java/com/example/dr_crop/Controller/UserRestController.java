package com.example.dr_crop.Controller;

import com.example.dr_crop.Model.User;
import com.example.dr_crop.Service.ConditionService;
import com.example.dr_crop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/newuser")
    public ResponseEntity<?> newUser(){
        String id = userService.createNewUser("ayushmanjena24@gmail.com", "Ayushman", " ", "jena", "7077126295");
        return ResponseEntity.ok(id);
    }

    @PostMapping("/newuser")
    public ResponseEntity<?> createNewUser(@RequestBody User user){
        String id = userService.createNewUser(user.getEmail(), user.getFname(), user.getMname(), user.getLname(), user.getPhone());
        return ResponseEntity.ok(id);
    }

    @GetMapping("/all") // get all users
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/{email}") // get user from id
    public ResponseEntity<?> getUser(@PathVariable String email){
        User user = userService.getUserByEmail(email);
        if(user != null){
            System.out.println(user.getEmail());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

//    @DeleteMapping("/{id}") // delete user from id
//    public ResponseEntity<?> deleteUser(@PathVariable String id) {
//        boolean deleted = userService.deleteUser(id);
//        return deleted ? ResponseEntity.ok("User deleted successfully")
//                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//    }

}
