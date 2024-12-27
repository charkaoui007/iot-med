package com.safalifter.jobservice.service;

import com.safalifter.jobservice.client.UserServiceClient;
import com.safalifter.jobservice.dto.MedicalDataRequest;
import com.safalifter.jobservice.dto.UserDto;
import com.safalifter.jobservice.model.MedicalAlert;
import com.safalifter.jobservice.model.MedicalData;
import com.safalifter.jobservice.repository.MedicalAlertRepository;
import com.safalifter.jobservice.repository.MedicalDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalDataService {

    private final MedicalDataRepository medicalDataRepository;
    private final MedicalAlertRepository medicalAlertRepository;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;

    @Value("${medical.threshold.temperature}")
    private double highTemperatureThreshold;

    @Value("${medical.threshold.heartRate}")
    private int highHeartRateThreshold;

    public MedicalData processAndSaveData(MedicalDataRequest request) {
        MedicalData medicalData = MedicalData.builder()
                .userId(request.getUserId())
                .temperature(request.getTemperature())
                .heartRate(request.getHeartRate())
                .motionDetected(request.isMotionDetected())
                .timestamp(LocalDateTime.now())
                .build();

        checkAndRaiseAlerts(medicalData);
        return medicalDataRepository.save(medicalData);
    }

    private void checkAndRaiseAlerts(MedicalData data) {
        if (data.getTemperature() > highTemperatureThreshold) {
            String message = String.format("High temperature detected for user %s: %.2f",
                    data.getUserId(), data.getTemperature());
            raiseAlert(data.getUserId(), message);
        }

        if (data.getHeartRate() > highHeartRateThreshold) {
            String message = String.format("High heart rate detected for user %s: %d",
                    data.getUserId(), data.getHeartRate());
            raiseAlert(data.getUserId(), message);
        }
    }

    private void raiseAlert(String userId, String message) {
        MedicalAlert alert = MedicalAlert.builder()
                .userId(userId)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        medicalAlertRepository.save(alert);

        // Get all users from userService using Feign
        List<UserDto> allUsers = userServiceClient.getAllUsers();
        System.out.println("DEBUG: Retrieved all users: " + allUsers.size());

        // Send email to each user
        allUsers.forEach(user -> {
            System.out.println("DEBUG: Sending email to user: " + user.getEmail());
            emailService.sendSimpleMessage(
                    user.getEmail(),
                    "Medical Anomaly Detected",
                    String.format("Medical anomaly detected for user %s:\n\n%s", userId, message)
            );
        });

        System.out.println("ALERT: " + message + " for user: " + userId + " - Notified " + allUsers.size() + " users.");
    }
}