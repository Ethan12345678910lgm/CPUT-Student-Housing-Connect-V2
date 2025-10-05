package co.za.cput.service.business.implementation;
//Firstname:        Sinhle Xiluva
//LastName:         Mthethwa
//Student Number:   221802797.

import co.za.cput.domain.business.Accommodation;
import co.za.cput.repository.business.AccommodationRepository;
import co.za.cput.repository.users.LandLordRepository;
import co.za.cput.service.business.IAccommodationService;
import co.za.cput.util.LinkingEntitiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccommodationServiceImpl implements IAccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final LandLordRepository landLordRepository;

    @Autowired
    public AccommodationServiceImpl(AccommodationRepository accommodationRepository,
                                    LandLordRepository landLordRepository) {
        this.accommodationRepository = accommodationRepository;
        this.landLordRepository = landLordRepository;
    }

    @Override
    public Accommodation create(Accommodation accommodation) {
        Accommodation linkedAccommodation = LinkingEntitiesHelper.linkLandlord(accommodation, landLordRepository);
        return accommodationRepository.save(linkedAccommodation);
    }

    @Override
    public Accommodation read(Long Id) {
        return accommodationRepository.findById(Id).orElse(null);
    }

    @Override
    public Accommodation update(Accommodation accommodation) {
        Accommodation linkedAccommodation = LinkingEntitiesHelper.linkLandlord(accommodation, landLordRepository);

        Accommodation existingAccommodation = accommodationRepository
                .findById(accommodation.getAccommodationID())
                .orElse(null);

        if (existingAccommodation == null) {            return null;
        }

        Accommodation updatedAccommodation = new Accommodation.Builder()
                .copy(existingAccommodation)
                .setRent(linkedAccommodation.getRent())
                .setWifiAvailable(linkedAccommodation.getIsWifiAvailable())
                .setFurnished(linkedAccommodation.getIsFurnished())
                .setDistanceFromCampus(linkedAccommodation.getDistanceFromCampus())
                .setUtilitiesIncluded(linkedAccommodation.getIsUtilitiesIncluded())
                .setRoomType(linkedAccommodation.getRoomType())
                .setBathroomType(linkedAccommodation.getBathroomType())
                .setAccommodationStatus(linkedAccommodation.getAccommodationStatus())
                .setAddress(linkedAccommodation.getAddress())
                .setLandlord(linkedAccommodation.getLandlord())
                .setBookings(linkedAccommodation.getBookings() != null
                        ? linkedAccommodation.getBookings()
                        : existingAccommodation.getBookings())
                .build();

        return accommodationRepository.save(updatedAccommodation);
    }

    @Override
    public List<Accommodation> getAllAccommodations() {
        return accommodationRepository.findAll();
    }

    @Override
    public void delete(Long Id) {
        accommodationRepository.deleteById(Id);
    }

}

