package com.example.dr_crop.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SoilResult {
    private String crop;

    @JsonProperty("Suggestion")
    private String suggestion;

    public SoilResult() {
    }

    public SoilResult(String crop, String suggestion) {
        this.crop = crop;
        this.suggestion = suggestion;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
