package co.za.cput.controller.users;

import co.za.cput.domain.generic.Contact;
import co.za.cput.domain.users.Administrator;
import co.za.cput.factory.generic.ContactFactory;
import co.za.cput.factory.user.AdministratorFactory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AdministratorControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/HouseConnect/Administrator";
    }

    private static final Contact contact = ContactFactory.createContact(
            "admin@gmail.com", "0821234567", "0832345678", true, true,
            Contact.PreferredContactMethod.EMAIL
    );

    private static final Administrator administrator = AdministratorFactory.createAdministrator(
            "Kwanda", "Twalo", "StrongPass123", Administrator.AdminRoleStatus.ACTIVE, contact, null
    );

    private static Administrator adminWithId;

    @Test
    void a_create() {
        ResponseEntity<Administrator> response = this.restTemplate.postForEntity(baseUrl() + "/create", administrator, Administrator.class);
        assertNotNull(response.getBody());
        adminWithId = response.getBody();
        System.out.println("Created admin: " + adminWithId);

    }

    @Test
    void b_read() {
        String url = baseUrl() + "/read/" + adminWithId.getAdminID();
        ResponseEntity<Administrator> response = this.restTemplate.getForEntity(url, Administrator.class);
        assertNotNull(response.getBody());
        System.out.println("Read admin: " + response.getBody());
    }

    @Test
    void c_update() {
        Administrator updated = new Administrator.Builder()
                .copy(adminWithId)
                .setAdminRoleStatus(Administrator.AdminRoleStatus.SUSPENDED)
                .build();
        this.restTemplate.put(baseUrl() + "/update", updated);
        ResponseEntity<Administrator> response =
                this.restTemplate.getForEntity(baseUrl() + "/read/" + updated.getAdminID(), Administrator.class);
        assertEquals(Administrator.AdminRoleStatus.SUSPENDED, response.getBody().getAdminRoleStatus());
        System.out.println("Updated admin: " + response.getBody());
    }

    @Test
    void d_getAllAdministrators() {
        ResponseEntity<Administrator[]> response =
                this.restTemplate.getForEntity(baseUrl() + "/getAllAdministrators", Administrator[].class);
        assertNotNull(response.getBody());
        System.out.println("All admins: ");
        for (Administrator a : response.getBody()) {
            System.out.println(a);
        }
    }

    @Test
    void e_delete() {
        restTemplate.delete(baseUrl() + "/delete/" + adminWithId.getAdminID());
        ResponseEntity<Administrator> response =
                this.restTemplate.getForEntity(baseUrl() + "/read/" + adminWithId.getAdminID(), Administrator.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
