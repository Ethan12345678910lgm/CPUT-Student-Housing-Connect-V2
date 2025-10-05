package co.za.cput.controller.business;


import co.za.cput.domain.business.Accommodation;
import co.za.cput.service.business.implementation.AccommodationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Accommodation")
public class AccommodationController {

    private final AccommodationServiceImpl accommodationService;

    public AccommodationController(AccommodationServiceImpl accommodationService) {
        this.accommodationService = accommodationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Accommodation> create(@RequestBody Accommodation accommodation) {
        Accommodation created = accommodationService.create(accommodation);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/read/{accommodationID}")
    public ResponseEntity<Accommodation> read(@PathVariable Long accommodationID) {
        Accommodation readAccommodation = accommodationService.read(accommodationID);
        if (readAccommodation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(readAccommodation);
    }

    @PutMapping("/update")
    public ResponseEntity<Accommodation> update(@RequestBody Accommodation accommodation) {
        if (accommodation.getAccommodationID() == null) {
            return ResponseEntity.badRequest().build();
        }
        Accommodation updated = accommodationService.update(accommodation);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @GetMapping({"/getAllAccommodations", "/getAllAccommodation"})
    public ResponseEntity<List<Accommodation>> getAllAccommodations() {
        List<Accommodation> all = accommodationService.getAllAccommodations();
        if (all == null || all.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/delete/{accommodationID}")
    public void delete(@PathVariable Long accommodationID) {
        accommodationService.delete(accommodationID);
    }
}
