package com.safalifter.jobservice.repository;


import com.safalifter.jobservice.model.MedicalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalDataRepository extends JpaRepository<MedicalData, Long> {
}