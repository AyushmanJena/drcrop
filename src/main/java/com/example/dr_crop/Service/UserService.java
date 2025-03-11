package com.example.dr_crop.Service;

import com.example.dr_crop.Model.User;
import com.example.dr_crop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createNewUser(String username, String password){
        User user = new User(username, password);
        return userRepository.save(user);
    }

    public User getUser(String id){
        return userRepository.findById(id).orElse(null);
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
