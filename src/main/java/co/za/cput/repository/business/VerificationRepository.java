package co.za.cput.repository.business;

import co.za.cput.domain.business.Verification;
import co.za.cput.domain.business.Verification.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    List<Verification> findByVerificationStatus(VerificationStatus status);
    long countByVerificationStatus(VerificationStatus status);
}
