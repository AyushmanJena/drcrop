package com.example.dr_crop.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "medicines")
public class DiseaseMedicine {

    @Id
    private String id;
    private String diseaseName;
    private ArrayList<Medicine> medicineList;

    public static class Medicine{
        String imageUrl;
        String medicineName;
        String buyLink;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getBuyLink() {
            return buyLink;
        }

        public void setBuyLink(String buyLink) {
            this.buyLink = buyLink;
        }
    }



    public DiseaseMedicine() {}

    public DiseaseMedicine(String id, String diseaseName, ArrayList<Medicine> medicinesList) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.medicineList = medicinesList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public ArrayList<Medicine> getMedicinesList() {
        return medicineList;
    }

    public void setMedicinesList(ArrayList<Medicine> medicinesList) {
        this.medicineList = medicinesList;
    }


}
