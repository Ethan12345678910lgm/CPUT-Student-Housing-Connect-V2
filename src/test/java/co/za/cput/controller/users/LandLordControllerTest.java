package co.za.cput.controller.users;
//Firstname:        Sinhle Xiluva
//LastName:         Mthethwa
//Student Number:   221802797.

import co.za.cput.domain.generic.Contact;
import co.za.cput.domain.users.Landlord;
import co.za.cput.factory.generic.ContactFactory;
import co.za.cput.factory.user.LandlordFactory;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class LandLordControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/HouseConnect/Landlord";
    }

    private static final Contact contact = ContactFactory.createContact(
            "landlord@example.com", "0721234567", "0731234567", true, false,
            Contact.PreferredContactMethod.PHONE
    );

    private static final Landlord landlord = LandlordFactory.createLandlord(
            "Kwanda", "Twalo", true,
            LocalDate.of(2024, 12, 1), "ValidPass123", contact, null
    );

    private static Landlord landlordWithId;

    @Test
    void a_create() {
        ResponseEntity<Landlord> response = restTemplate.postForEntity(baseUrl() + "/create", landlord, Landlord.class);
        assertNotNull(response.getBody());
        landlordWithId = response.getBody();
        System.out.println("Created landlord: " + landlordWithId);
    }

    @Test
    void b_read() {
        String url = baseUrl() + "/read/" + landlordWithId.getLandlordID();
        ResponseEntity<Landlord> response = restTemplate.getForEntity(url, Landlord.class);
        assertNotNull(response.getBody());
        System.out.println("Read landlord: " + response.getBody());
    }

    @Test
    void c_update() {
        Landlord updated = new Landlord.Builder().copy(landlordWithId).setVerified(false).build();
        restTemplate.put(baseUrl() + "/update", updated);
        ResponseEntity<Landlord> response = restTemplate.getForEntity(baseUrl() + "/read/" + updated.getLandlordID(), Landlord.class);
        assertFalse(response.getBody().isVerified());
        System.out.println("Updated landlord: " + response.getBody());
    }

    @Test
    void d_getAllLandlords() {
        ResponseEntity<Landlord[]> response = restTemplate.getForEntity(baseUrl() + "/getAllLandlords", Landlord[].class);
        assertNotNull(response.getBody());
        System.out.println("All landlords: ");
        for (Landlord l : response.getBody()) {
            System.out.println(l);
        }
    }

    @Test
    void e_delete() {
        restTemplate.delete(baseUrl() + "/delete/" + landlordWithId.getLandlordID());
        ResponseEntity<Landlord> response = restTemplate.getForEntity(baseUrl() + "/read/" + landlordWithId.getLandlordID(), Landlord.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
