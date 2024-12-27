package com.safalifter.jobservice.dto;

import lombok.Data;

@Data
public class MedicalDataRequest {
    private String userId;
    private double temperature;
    private int heartRate;
    private boolean motionDetected;
}