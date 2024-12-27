package repository;

import com.safalifter.medicaldataservice.model.MedicalAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalAlertRepository extends JpaRepository<MedicalAlert, Long> {
}