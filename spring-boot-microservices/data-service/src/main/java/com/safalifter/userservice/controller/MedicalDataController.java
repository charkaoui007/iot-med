package com.safalifter.userservice.controller;

import com.safalifter.userservice.dto.MedicalDataRequest;
import com.safalifter.userservice.model.MedicalData;
import com.safalifter.userservice.service.MedicalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/medical-data")
@RequiredArgsConstructor
public class MedicalDataController {

    private final MedicalDataService medicalDataService;

    @PostMapping("/receive")
    public ResponseEntity<MedicalData> receiveMedicalData(@Valid @RequestBody MedicalDataRequest request) {
        return ResponseEntity.ok(medicalDataService.processAndSaveData(request));
    }
}