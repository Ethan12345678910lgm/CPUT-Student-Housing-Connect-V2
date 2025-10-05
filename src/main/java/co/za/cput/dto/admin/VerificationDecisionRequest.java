package co.za.cput.dto.admin;

import co.za.cput.domain.business.Verification;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VerificationDecisionRequest {

    @NotNull(message = "Admin identifier is required")
    private Long adminId;

    @NotNull(message = "Verification status is required")
    private Verification.VerificationStatus status;

    private String notes;
    private LocalDate verificationDate;

    public VerificationDecisionRequest() {
    }

    public VerificationDecisionRequest(Long adminId,
                                       Verification.VerificationStatus status,
                                       String notes,
                                       LocalDate verificationDate) {
        this.adminId = adminId;
        this.status = status;
        this.notes = notes;
        this.verificationDate = verificationDate;
    }

    public Long getAdminId() {
        return adminId;
    }

    public Verification.VerificationStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getVerificationDate() {
        return verificationDate;
    }
}