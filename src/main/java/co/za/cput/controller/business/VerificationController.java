package co.za.cput.controller.business;
//Firstname: Siyabonga
//Lastname: Jiyane
//Student Number: 222359676

import co.za.cput.domain.business.Verification;
import co.za.cput.service.business.implementation.VerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Verification")
public class VerificationController {

    private VerificationServiceImpl verificationService;
  
    @Autowired
    public VerificationController(VerificationServiceImpl verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Verification> create(@RequestBody Verification verification) {
        Verification createdVerification = verificationService.create(verification);
        return ResponseEntity.ok(createdVerification);
    }

    @GetMapping("/read/{verificationID}")
    public ResponseEntity<Verification> read(@PathVariable Long verificationID) {
        Verification readVerification = verificationService.read(verificationID);
        if (readVerification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(readVerification);
    }

    @PutMapping("/update")
    public ResponseEntity<Verification> update(@RequestBody Verification verification) {
        if(verification.getVerificationID() == null) {
            return ResponseEntity.badRequest().build();
        }
        Verification updatedVerification = verificationService.update(verification);
        if (updatedVerification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedVerification);
    }

    @GetMapping("/getAllVerification")
    public ResponseEntity<List<Verification>> getAllVerification() {
        List<Verification> verifications = verificationService.getAllVerifications();
        if (verifications == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(verifications);
    }

    @DeleteMapping("/delete/{verificationID}")
    public void delete(@PathVariable Long verificationID) {
        verificationService.delete(verificationID);
    }
}

