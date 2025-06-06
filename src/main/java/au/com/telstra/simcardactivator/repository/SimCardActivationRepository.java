package au.com.telstra.simcardactivator.repository;

import au.com.telstra.simcardactivator.entity.SimCardActivation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimCardActivationRepository extends JpaRepository<SimCardActivation, Long> {
}
