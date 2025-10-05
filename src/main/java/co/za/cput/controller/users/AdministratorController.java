package co.za.cput.controller.users;

import co.za.cput.domain.business.Verification;
import co.za.cput.domain.users.Administrator;
import co.za.cput.domain.users.Landlord;
import co.za.cput.dto.admin.AdminDashboardSummary;
import co.za.cput.dto.admin.LandlordVerificationRequest;
import co.za.cput.dto.admin.VerificationDecisionRequest;
import co.za.cput.service.users.IAdministratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Administrator")
public class AdministratorController {

    private final IAdministratorService administratorService;

    @Autowired
    public AdministratorController(IAdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping("/create")
    public ResponseEntity<Administrator> create(@RequestBody Administrator administrator,
                                                @RequestParam(value = "requestingAdminId", required = false) Long requestingAdminId) {
        Administrator created;
        if (requestingAdminId != null) {
            created = administratorService.createWithAuthorization(administrator, requestingAdminId);
        } else {
            created = administratorService.create(administrator);
        }
        return ResponseEntity.ok(created);
    }

    @GetMapping("/read/{Id}")
    public ResponseEntity<Administrator> read(@PathVariable Long Id) {
        Administrator admin = administratorService.read(Id);
        if (admin == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/update")
    public ResponseEntity<Administrator> update(@RequestBody Administrator administrator) {
        if (administrator.getAdminID() == null) {
            return ResponseEntity.badRequest().build();
        }
        Administrator updated = administratorService.update(administrator);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/getAllAdministrators")
    public ResponseEntity<List<Administrator>> getAllAdministrators() {
        List<Administrator> admins = administratorService.getAllAdministrators();
        if (admins == null || admins.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(admins);
    }

    @DeleteMapping("/delete/{Id}")
    public void delete(@PathVariable Long Id) {
        administratorService.delete(Id);
    }
    @GetMapping("/pending-landlords")
    public ResponseEntity<List<Landlord>> getPendingLandlords() {
        List<Landlord> landlords = administratorService.getPendingLandlords();
        if (landlords == null || landlords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(landlords);
    }

    @PutMapping("/landlords/{landlordId}/verification")
    public ResponseEntity<Landlord> verifyLandlord(@PathVariable Long landlordId,
                                                   @Valid @RequestBody LandlordVerificationRequest request) {
        Landlord landlord = administratorService.verifyLandlord(landlordId, request);
        return ResponseEntity.ok(landlord);
    }

    @GetMapping("/pending-verifications")
    public ResponseEntity<List<Verification>> getPendingVerifications() {
        List<Verification> verifications = administratorService.getPendingVerifications();
        if (verifications == null || verifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(verifications);
    }

    @PutMapping("/verifications/{verificationId}")
    public ResponseEntity<Verification> decideOnVerification(@PathVariable Long verificationId,
                                                             @Valid @RequestBody VerificationDecisionRequest request) {
        Verification verification = administratorService.decideOnVerification(verificationId, request);
        return ResponseEntity.ok(verification);
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<AdminDashboardSummary> getDashboardSummary() {
        AdminDashboardSummary summary = administratorService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }
}
