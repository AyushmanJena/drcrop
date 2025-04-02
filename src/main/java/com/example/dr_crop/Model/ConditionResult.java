package com.example.dr_crop.Model;

public class ConditionResult {
    public String plantName;
    public String conditionName;

    public float accuracy;

    public ConditionResult() {
    }

    public ConditionResult(String plantName, String conditionName, float accuracy) {
        this.plantName = plantName;
        this.conditionName = conditionName;
        this.accuracy = accuracy;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
