package co.za.cput.repository.users;

import co.za.cput.domain.users.Landlord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LandLordRepository extends JpaRepository<Landlord, Long> {
    Optional<Landlord> findByContactEmail(String email);
    List<Landlord> findByIsVerified(boolean isVerified);
    long countByIsVerified(boolean isVerified);
}
