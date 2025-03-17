package com.example.dr_crop.Model;

public class SoilRequest {
    private float nitrogen;
    private float phosphorous;
    private float oxygen;

    public SoilRequest() {
    }

    public SoilRequest(float nitrogen, float phosphorous, float oxygen) {
        this.nitrogen = nitrogen;
        this.phosphorous = phosphorous;
        this.oxygen = oxygen;
    }

    public float getNitrogen() {
        return nitrogen;
    }

    public void setNitrogen(float nitrogen) {
        this.nitrogen = nitrogen;
    }

    public float getPhosphorous() {
        return phosphorous;
    }

    public void setPhosphorous(float phosphorous) {
        this.phosphorous = phosphorous;
    }

    public float getOxygen() {
        return oxygen;
    }

    public void setOxygen(float oxygen) {
        this.oxygen = oxygen;
    }
}