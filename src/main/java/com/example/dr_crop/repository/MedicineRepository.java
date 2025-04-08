package com.example.dr_crop.repository;

import com.example.dr_crop.Model.DiseaseMedicine;
import com.example.dr_crop.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MedicineRepository extends MongoRepository<DiseaseMedicine, String> {
    Optional<DiseaseMedicine> findByDiseaseName(String diseaseName);
}
