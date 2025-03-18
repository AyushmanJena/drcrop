package com.example.dr_crop.Service;

import com.example.dr_crop.Model.User;
import com.example.dr_crop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();

    public String createNewUser(String email, String fname, String mname, String lname, String phone){
        User user = new User(email, fname, mname, lname, phone);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public User getUserById(String id){
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean deleteUser(String email) {
         User user = userRepository.findByEmail(email).orElse(null);
         if(user != null) {
             String id = user.getId();
             if (userRepository.existsById(id)) {
                 userRepository.deleteById(id);
                 return true;
             }
         }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean containsUser(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            System.out.println(email+ "does not exist");
            return false;
        }
        return true;
    }

    public String authenticate(String email, String generatedOTP, String userOTP) { // needs major changes for taking two strings ans return token if valid or null if invalid
        Optional<User> userOpt = userRepository.findByEmail(email);
        // generatedOTP = "abc123"; // manually setting the generated otp temporarily
        if (userOpt.isPresent() && generatedOTP.equals(userOTP)) {
            String token = UUID.randomUUID().toString(); // Generate a random token
            tokenStore.put(token, userOpt.get().getId()); // Store token with userId
            System.out.println("token : " + token);
            return token;
        }
        return null;
    }

    public String getUserIdFromToken(String token) {
        return tokenStore.get(token);
    }

    public String generateOTP(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int size = 6;
        for(int i = 0; i<size; i++){
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public void sendOtpToMail(String email, String generatedOTP) {
        try{
            String subject = "Your One-Time Password for Dr. Crop Login";
            String body = "Your OTP for Dr. Crop login is "+ generatedOTP + ". If you are still unable to login, please reply to this mail.";
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // may need another method get user from token
}
