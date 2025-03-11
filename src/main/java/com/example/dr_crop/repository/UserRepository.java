package com.example.dr_crop.repository;

import com.example.dr_crop.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
