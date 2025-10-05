package co.za.cput.service.users.implementation;

import co.za.cput.domain.business.Accommodation;
import co.za.cput.domain.business.Verification;
import co.za.cput.domain.users.Administrator;
import co.za.cput.domain.users.Landlord;
import co.za.cput.dto.admin.AdminDashboardSummary;
import co.za.cput.dto.admin.LandlordVerificationRequest;
import co.za.cput.dto.admin.VerificationDecisionRequest;
import co.za.cput.repository.business.AccommodationRepository;
import co.za.cput.repository.business.VerificationRepository;
import co.za.cput.repository.users.AdministratorRepository;
import co.za.cput.repository.users.LandLordRepository;
import co.za.cput.repository.users.StudentRepository;
import co.za.cput.service.users.IAdministratorService;
import co.za.cput.util.LinkingEntitiesHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements IAdministratorService {

    private final AdministratorRepository administratorRepository;
    private final LandLordRepository landLordRepository;
    private final AccommodationRepository accommodationRepository;
    private final StudentRepository studentRepository;
    private final VerificationRepository verificationRepository;

    @Autowired
    public AdministratorServiceImpl(AdministratorRepository administratorRepository,
                                    LandLordRepository landLordRepository,
                                    AccommodationRepository accommodationRepository,
                                    StudentRepository studentRepository,
                                    VerificationRepository verificationRepository) {        this.administratorRepository = administratorRepository;
        this.landLordRepository = landLordRepository;
        this.accommodationRepository = accommodationRepository;
        this.studentRepository = studentRepository;
        this.verificationRepository = verificationRepository;
    }

    @Override
    public Administrator create(Administrator administrator) {
        Administrator preparedAdmin = LinkingEntitiesHelper.prepareAdministratorForSave(
                administrator,
                accommodationRepository,
                landLordRepository
        );

        Administrator adminWithoutVerifications = new Administrator.Builder()
                .copy(preparedAdmin)
                .setVerifications(null)
                .build();

        Administrator savedAdmin = administratorRepository.save(adminWithoutVerifications);

        List<Verification> verifications = preparedAdmin.getVerifications();
        if (verifications == null || verifications.isEmpty()) {
            return savedAdmin;
        }

        Administrator finalSavedAdmin = savedAdmin;
        List<Verification> verificationsWithAdmin = verifications.stream()
                .map(verification -> new Verification.Builder()
                        .copy(verification)
                        .setAdministrator(finalSavedAdmin)
                        .build())
                .collect(Collectors.toList());

        savedAdmin = new Administrator.Builder()
                .copy(savedAdmin)
                .setVerifications(verificationsWithAdmin)
                .build();

        return administratorRepository.save(savedAdmin);

        @Override
        public Administrator createWithAuthorization(Administrator administrator, Long requestingAdminId) {
            if (administratorRepository.count() > 0) {
                requireActiveAdministrator(requestingAdminId);
            }
            return create(administrator);
        }
    }

    @Override
    public Administrator read(Long Id) {
        return administratorRepository.findById(Id).orElse(null);
    }

    @Override
    public Administrator update(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    @Override
    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    @Override
    public void delete(Long Id) {
        administratorRepository.deleteById(Id);
    }

    @Override
    public List<Landlord> getPendingLandlords() {
        return landLordRepository.findByIsVerified(false);
    }

    @Override
    public Landlord verifyLandlord(Long landlordId, LandlordVerificationRequest request) {
        requireActiveAdministrator(request.getAdminId());
        Landlord landlord = landLordRepository.findById(landlordId)
                .orElseThrow(() -> new IllegalArgumentException("Landlord not found"));

        Landlord updated = new Landlord.Builder()
                .copy(landlord)
                .setVerified(Boolean.TRUE.equals(request.getApprove()))
                .build();

        // The administrator entity is fetched to ensure the requester exists and is active.
        // Future auditing can attach the administrator to a verification log if required.
        return landLordRepository.save(updated);
    }

    @Override
    public List<Verification> getPendingVerifications() {
        return verificationRepository.findByVerificationStatus(Verification.VerificationStatus.PENDING);
    }

    @Override
    public Verification decideOnVerification(Long verificationId, VerificationDecisionRequest request) {
        Administrator administrator = requireActiveAdministrator(request.getAdminId());
        Verification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new IllegalArgumentException("Verification not found"));

        Verification.Builder builder = new Verification.Builder()
                .copy(verification)
                .setAdministrator(administrator)
                .setVerificationStatus(request.getStatus())
                .setNotes(request.getNotes())
                .setUpdateAt(LocalDateTime.now());

        LocalDate decisionDate = request.getVerificationDate();
        if (decisionDate == null) {
            decisionDate = LocalDate.now();
        }
        builder.setVerificationDate(decisionDate);

        return verificationRepository.save(builder.build());
    }

    @Override
    public AdminDashboardSummary getDashboardSummary() {
        long totalStudents = studentRepository.count();
        long verifiedStudents = studentRepository.countByIsStudentVerifiedTrue();
        long totalLandlords = landLordRepository.count();
        long verifiedLandlords = landLordRepository.countByIsVerified(true);
        long totalAccommodations = accommodationRepository.count();
        long availableAccommodations = accommodationRepository.countByAccommodationStatus(Accommodation.AccommodationStatus.AVAILABLE);
        long pendingVerifications = verificationRepository.countByVerificationStatus(Verification.VerificationStatus.PENDING);
        long approvedVerifications = verificationRepository.countByVerificationStatus(Verification.VerificationStatus.APPROVED);
        long rejectedVerifications = verificationRepository.countByVerificationStatus(Verification.VerificationStatus.REJECTED);

        return new AdminDashboardSummary.Builder()
                .setTotalStudents(totalStudents)
                .setVerifiedStudents(verifiedStudents)
                .setTotalLandlords(totalLandlords)
                .setVerifiedLandlords(verifiedLandlords)
                .setTotalAccommodations(totalAccommodations)
                .setAvailableAccommodations(availableAccommodations)
                .setPendingVerifications(pendingVerifications)
                .setApprovedVerifications(approvedVerifications)
                .setRejectedVerifications(rejectedVerifications)
                .build();
    }

    private Administrator requireActiveAdministrator(Long adminId) {
        if (adminId == null) {
            throw new IllegalArgumentException("Administrator identifier is required");
        }

        Administrator administrator = administratorRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Administrator not found"));

        if (administrator.getAdminRoleStatus() != Administrator.AdminRoleStatus.ACTIVE) {
            throw new IllegalStateException("Administrator is not active");
        }
        return administrator;
    }
}
