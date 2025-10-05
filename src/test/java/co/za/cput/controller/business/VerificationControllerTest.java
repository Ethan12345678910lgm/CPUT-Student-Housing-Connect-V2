package co.za.cput.controller.business;

import co.za.cput.domain.business.Verification;
import co.za.cput.factory.business.VerificationFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class VerificationControllerTest {

    private Verification verification1 = VerificationFactory.createVerification(
            LocalDate.of(2025, 5, 15),
            "All documents verified. Inspection successful.",
            LocalDateTime.of(2025, 5, 16, 9, 19, 54, 0),
            LocalDateTime.of(2025, 6, 1, 9, 15, 47, 0),
            Verification.VerificationStatus.APPROVED,
            null,
            null
    );

    private static Verification verification_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/HouseConnect/Verification";
    }

    @Test
    void a_create() {
        String url = baseUrl() + "/create";
        ResponseEntity<Verification> createVerification = this.restTemplate.postForEntity(url, verification1, Verification.class);
        assertNotNull(createVerification.getBody());
        System.out.println("Created verification: " + createVerification.getBody());
        verification_with_Id = createVerification.getBody();
        assertNotNull(verification_with_Id);
        System.out.println("Created verification: " + verification_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(verification_with_Id);
        String url = baseUrl() + "/read/" + verification_with_Id.getVerificationID();
        ResponseEntity<Verification> readVerification = this.restTemplate.getForEntity(url, Verification.class);
        assertNotNull(readVerification.getBody());
        System.out.println("Read verification: " + readVerification.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(verification_with_Id);
        String url = baseUrl() + "/update";
        Verification updatedVerification = new Verification.Builder()
                .copy(verification_with_Id)
                .setVerificationDate(LocalDate.of(2025, 7, 15))
                .setUpdateAt(LocalDateTime.of(2025, 7, 15, 9, 19, 54, 0))
                .setVerificationStatus(Verification.VerificationStatus.REJECTED)
                .setNotes("Investigation was conducted the Accommodation is not safe for students." +
                        " Furniture is not in good conditions")
                .build();
        this.restTemplate.put(url, updatedVerification);

        //verify if the object is updated.
        ResponseEntity<Verification> readVerification =
                this.restTemplate.getForEntity(baseUrl() + "/read/" + updatedVerification.getVerificationID(),
                        Verification.class);
        System.out.println("Read verification immediately after updated: " + readVerification.getBody());

        assertEquals("Investigation was conducted the Accommodation is not safe for students. " +
                "Furniture is not in good conditions", updatedVerification.getNotes());
        assertEquals(Verification.VerificationStatus.REJECTED, updatedVerification.getVerificationStatus());
        assertEquals(LocalDateTime.of(2025, 7, 15, 9, 19, 54, 0),
                updatedVerification.getUpdateAt());
        assertEquals(LocalDate.of(2025, 7, 15), updatedVerification.getVerificationDate());
        System.out.println("Updated verification: " + updatedVerification);
    }

    @Test
    void d_getAllVerification() {
        assertNotNull(verification_with_Id);
        String url = baseUrl() + "/getAllVerification";
        ResponseEntity<Verification[]> verificationList = this.restTemplate.getForEntity(url, Verification[].class);
        assertNotNull(verificationList.getBody());
        System.out.println("Get all verifications: ");
        for (Verification verification : verificationList.getBody()) {
            System.out.println(verification);
        }
    }

    @Test
    void e_delete() {
        assertNotNull(verification_with_Id);
        String url = baseUrl() + "/delete/" + verification_with_Id.getVerificationID();
        this.restTemplate.delete(url);

        ResponseEntity<Verification> readVerification =
                this.restTemplate.getForEntity(baseUrl() + "/read/" + verification_with_Id.getVerificationID(),
                        Verification.class);
        assertEquals(HttpStatus.NOT_FOUND, readVerification.getStatusCode());
        System.out.println("After deletion the status code for readContact is: " + readVerification.getStatusCode());
    }
}
