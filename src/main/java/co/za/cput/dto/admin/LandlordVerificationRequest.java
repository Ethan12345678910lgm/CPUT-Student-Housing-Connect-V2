package co.za.cput.dto.admin;

import jakarta.validation.constraints.NotNull;

public class LandlordVerificationRequest {

    @NotNull(message = "Admin identifier is required")
    private Long adminId;

    @NotNull(message = "Approval decision is required")
    private Boolean approve;

    public LandlordVerificationRequest() {
    }

    public LandlordVerificationRequest(Long adminId, Boolean approve) {
        this.adminId = adminId;
        this.approve = approve;
    }

    public Long getAdminId() {
        return adminId;
    }

    public Boolean getApprove() {
        return approve;
    }
}