package com.safalifter.jobservice.repository;


import com.safalifter.jobservice.model.MedicalAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalAlertRepository extends JpaRepository<MedicalAlert, Long> {
}