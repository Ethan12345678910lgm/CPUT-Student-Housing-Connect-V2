package co.za.cput.repository.business;

import co.za.cput.domain.business.Accommodation;
import co.za.cput.domain.business.Accommodation.AccommodationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    long countByAccommodationStatus(AccommodationStatus accommodationStatus);

}
