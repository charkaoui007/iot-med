package repository;

import com.safalifter.userservice.model.MedicalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalDataRepository extends JpaRepository<MedicalData, Long> {
}