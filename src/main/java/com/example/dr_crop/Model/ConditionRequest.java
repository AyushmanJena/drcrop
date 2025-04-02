package com.example.dr_crop.Model;

public class ConditionRequest {
    private String file;
    private String crop;

    public ConditionRequest() {} // Default constructor for JSON deserialization

    public ConditionRequest(String file, String crop) {
        this.file = file;
        this.crop = crop;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }
}
