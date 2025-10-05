package co.za.cput.service.auth;

import co.za.cput.domain.users.Administrator;
import co.za.cput.domain.users.Landlord;
import co.za.cput.domain.users.Student;
import co.za.cput.dto.auth.LoginRequest;
import co.za.cput.dto.auth.LoginResponse;
import co.za.cput.dto.auth.UserRole;
import co.za.cput.repository.users.AdministratorRepository;
import co.za.cput.repository.users.LandLordRepository;
import co.za.cput.repository.users.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final LandLordRepository landLordRepository;
    private final AdministratorRepository administratorRepository;

    public AuthenticationService(StudentRepository studentRepository,
                                 LandLordRepository landLordRepository,
                                 AdministratorRepository administratorRepository) {
        this.studentRepository = studentRepository;
        this.landLordRepository = landLordRepository;
        this.administratorRepository = administratorRepository;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
            return new LoginResponse.Builder()
                    .setSuccess(false)
                    .setMessage("Email and password are required")
                    .build();
        }

        UserRole role = request.getRole();
        return switch (role) {
            case STUDENT -> authenticateStudent(request);
            case LANDLORD -> authenticateLandlord(request);
            case ADMIN -> authenticateAdministrator(request);
        };
    }

    private LoginResponse authenticateStudent(LoginRequest request) {
        return studentRepository.findByContactEmail(request.getEmail())
                .map(student -> buildStudentResponse(student, request.getPassword()))
                .orElseGet(() -> failureResponse("Student account not found"));
    }

    private LoginResponse buildStudentResponse(Student student, String rawPassword) {
        if (!student.getPassword().equals(rawPassword)) {
            return failureResponse("Invalid credentials supplied");
        }

        return new LoginResponse.Builder()
                .setSuccess(true)
                .setMessage("Login successful")
                .setRole(UserRole.STUDENT)
                .setUserId(student.getStudentID())
                .setFullName(student.getStudentName() + " " + student.getStudentSurname())
                .setEmail(student.getContact() != null ? student.getContact().getEmail() : null)
                .setRequiresVerification(!student.getIsStudentVerified())
                .build();
    }

    private LoginResponse authenticateLandlord(LoginRequest request) {
        return landLordRepository.findByContactEmail(request.getEmail())
                .map(landlord -> buildLandlordResponse(landlord, request.getPassword()))
                .orElseGet(() -> failureResponse("Landlord account not found"));
    }

    private LoginResponse buildLandlordResponse(Landlord landlord, String rawPassword) {
        if (!landlord.getPassword().equals(rawPassword)) {
            return failureResponse("Invalid credentials supplied");
        }

        return new LoginResponse.Builder()
                .setSuccess(true)
                .setMessage("Login successful")
                .setRole(UserRole.LANDLORD)
                .setUserId(landlord.getLandlordID())
                .setFullName(landlord.getLandlordFirstName() + " " + landlord.getLandlordLastName())
                .setEmail(landlord.getContact() != null ? landlord.getContact().getEmail() : null)
                .setRequiresVerification(!landlord.isVerified())
                .build();
    }

    private LoginResponse authenticateAdministrator(LoginRequest request) {
        return administratorRepository.findByContactEmail(request.getEmail())
                .map(admin -> buildAdministratorResponse(admin, request.getPassword()))
                .orElseGet(() -> failureResponse("Administrator account not found"));
    }

    private LoginResponse buildAdministratorResponse(Administrator administrator, String rawPassword) {
        if (!administrator.getAdminPassword().equals(rawPassword)) {
            return failureResponse("Invalid credentials supplied");
        }

        boolean isActive = administrator.getAdminRoleStatus() == Administrator.AdminRoleStatus.ACTIVE;

        return new LoginResponse.Builder()
                .setSuccess(true)
                .setMessage(isActive ? "Login successful" : "Administrator account is not active")
                .setRole(UserRole.ADMIN)
                .setUserId(administrator.getAdminID())
                .setFullName(administrator.getAdminName() + " " + administrator.getAdminSurname())
                .setEmail(administrator.getContact() != null ? administrator.getContact().getEmail() : null)
                .setRequiresVerification(!isActive)
                .build();
    }

    private LoginResponse failureResponse(String message) {
        return new LoginResponse.Builder()
                .setSuccess(false)
                .setMessage(message)
                .build();
    }
}