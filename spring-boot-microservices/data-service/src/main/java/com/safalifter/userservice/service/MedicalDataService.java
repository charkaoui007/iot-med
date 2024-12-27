package com.safalifter.userservice.service;

import com.safalifter.medicaldataservice.dto.UserDto;
import com.safalifter.medicaldataservice.model.MedicalAlert;

import com.safalifter.userservice.client.UserServiceClient;
import com.safalifter.userservice.dto.MedicalDataRequest;
import com.safalifter.userservice.model.MedicalData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repository.MedicalAlertRepository;
import repository.MedicalDataRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        // Store the anomaly
        MedicalAlert alert = MedicalAlert.builder()
                .userId(userId)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        medicalAlertRepository.save(alert);

        // Get all users from userService using Feign
        List<UserDto> allUsers = userServiceClient.getAllUsers();

        // Filter users with CAREGIVER role
        List<UserDto> caregivers = allUsers.stream()
                .filter(user -> user.getRole() != null && user.getRole().equals(com.safalifter.userservice.enums.Role.CAREGIVER))
                .collect(Collectors.toList());

        // Send email to each caregiver
        caregivers.forEach(caregiver -> {
            emailService.sendSimpleMessage(
                    caregiver.getEmail(),
                    "Medical Anomaly Detected",
                    String.format("Medical anomaly detected for user %s:\n\n%s", userId, message)
            );
        });

        System.out.println("ALERT: " + message + " for user: " + userId + " - Notified " + caregivers.size() + " caregivers.");
    }
}