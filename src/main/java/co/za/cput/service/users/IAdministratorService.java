package co.za.cput.service.users;

import co.za.cput.domain.business.Verification;
import co.za.cput.domain.users.Administrator;
import co.za.cput.domain.users.Landlord;
import co.za.cput.dto.admin.AdminDashboardSummary;
import co.za.cput.dto.admin.LandlordVerificationRequest;
import co.za.cput.dto.admin.VerificationDecisionRequest;
import co.za.cput.service.IService;

import java.util.List;

public interface IAdministratorService extends IService<Administrator, Long> {
    List<Administrator> getAllAdministrators();

    Administrator createWithAuthorization(Administrator administrator, Long requestingAdminId);

    List<Landlord> getPendingLandlords();

    Landlord verifyLandlord(Long landlordId, LandlordVerificationRequest request);

    List<Verification> getPendingVerifications();

    Verification decideOnVerification(Long verificationId, VerificationDecisionRequest request);

    AdminDashboardSummary getDashboardSummary();
}
