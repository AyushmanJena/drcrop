package com.example.dr_crop.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SoilRequest {
    private float n;
    private float p;
    private float k;
    private float temp;
    private float humidity;

    @JsonProperty("pH")
    private float pH;
    private float rainfall;

    // Default constructor
    public SoilRequest() {}

    // Parameterized constructor
    public SoilRequest(float n, float p, float k, float temp, float humidity, float pH, float rainfall) {
        this.n = n;
        this.p = p;
        this.k = k;
        this.temp = temp;
        this.humidity = humidity;
        this.pH = pH;
        this.rainfall = rainfall;
    }

    // Getters and Setters
    public float getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public float getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public float getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getPH() {
        return pH;
    }

    public void setPH(int pH) {
        this.pH = pH;
    }

    public float getRainfall() {
        return rainfall;
    }

    public void setRainfall(int rainfall) {
        this.rainfall = rainfall;
    }

    @Override
    public String toString() {
        return "NutrientRequest{" +
                "n=" + n +
                ", p=" + p +
                ", k=" + k +
                ", temp=" + temp +
                ", humidity=" + humidity +
                ", pH=" + pH +
                ", rainfall=" + rainfall +
                '}';
    }
}
