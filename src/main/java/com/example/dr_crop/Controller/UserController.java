package com.example.dr_crop.Controller;

import com.example.dr_crop.Model.User;
import com.example.dr_crop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/testnewusers")
    public ResponseEntity<?> newUser(){
        userService.createNewUser("ayush", "ayush");
        userService.createNewUser("subh", "subh");
        User last = userService.createNewUser("sai", "sai");
        int count = 3;
        return ResponseEntity.ok(3);
    }

    @PostMapping("/newuser")
    public ResponseEntity<User> createNewUser(@RequestBody User user){
        User save = userService.createNewUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(save);
    }

    @GetMapping("/all") // get all users
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/{id}") // get user from id
    public ResponseEntity<?> getUser(@PathVariable String id){
        User user = userService.getUser(id);
        if(user != null){
            System.out.println(user.getId());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}") // delete user from id
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok("User deleted successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
