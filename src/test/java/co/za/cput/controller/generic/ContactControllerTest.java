package co.za.cput.controller.generic;

import co.za.cput.domain.generic.Contact;
import co.za.cput.factory.generic.ContactFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ContactControllerTest {

    private static Contact contact1 = ContactFactory.createContact(
            "zanele.ndaba@outlook.com",     // valid email
            "+27761234567",                   // valid phone number
            "+27789876543",                   // valid alternate phone number
            false,                           // email verified
            false,                          // phone not verified
            Contact.PreferredContactMethod.ALTERNATE_PHONE
    );
    private static Contact contact_with_ID;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/HouseConnect/Contact";
    }

    @Test
    void a_create() {
        String url = baseUrl() + "/create";
        ResponseEntity<Contact> createContact = restTemplate.postForEntity(url, contact1, Contact.class);
        assertNotNull(createContact.getBody());
        System.out.println("Created contact: " + createContact.getBody());
        contact_with_ID = createContact.getBody();
        assertNotNull(contact_with_ID);
        System.out.println("Created contact: " + contact_with_ID);
    }

    @Test
    void a_read() {
        assertNotNull(contact_with_ID);
        String url = baseUrl() + "/read/" + contact_with_ID.getContactID();
        ResponseEntity<Contact> readContact = restTemplate.getForEntity(url, Contact.class);
        assertNotNull(readContact.getBody());
        System.out.println("Read contact: " + readContact.getBody());
    }

    @Test
    void c_update() {
        /*assertNotNull(contact_with_ID);
        String url = baseUrl() + "/update";
        Contact updateContact = new Contact.Builder()
                .copy(contact_with_ID)
                .setIsPhoneVerified(true)
                .setPhoneNumber("+27647721990")
                .build();
        this.restTemplate.put(url, updateContact);

        //verify if the object is updated.
        ResponseEntity<Contact> readContact =
                this.restTemplate.getForEntity(BASE_URL + "/read/" + contact_with_ID.getContactID(), Contact.class);
        assertEquals(HttpStatus.OK, readContact.getStatusCode());

        Contact newContact = readContact.getBody();
        assertNotNull(newContact);
        assertEquals("+27647721990", newContact.getPhoneNumber());
        //assertEquals(true, newContact.isPhoneVerified());
        System.out.println("Updated contact: " + newContact);*/

        assertNotNull(contact_with_ID);
        String url = baseUrl() + "/update";

        Contact updateContact = new Contact.Builder()
                .copy(contact_with_ID)
                .setIsPhoneVerified(true)
                .setIsEmailVerified(true) // Add this too
                .setPhoneNumber("+27647721990")
                .build();
        System.out.println("Update contact before put method: " + updateContact);

        this.restTemplate.put(url, updateContact);

        System.out.println("Update contact after put method: " + updateContact);

        ResponseEntity<Contact> readContact =
                restTemplate.getForEntity(baseUrl() + "/read/" + updateContact.getContactID(), Contact.class);

        System.out.println("Read contact after we have read updated contact: " + readContact.getBody());

        Contact newContact = readContact.getBody();
        assertNotNull(newContact);
        assertEquals("+27647721990", newContact.getPhoneNumber());
        assertTrue(newContact.isPhoneVerified(), "Phone verification should be true");
        assertTrue(newContact.isEmailVerified(), "Email verification should be true");

        System.out.println("Updated contact: " + newContact);
    }

    @Test
    void d_getAllContacts() {
        String url = baseUrl() + "/getAllContacts";
        ResponseEntity<Contact[]> response = restTemplate.getForEntity(url, Contact[].class);
        assertNotNull(response.getBody());
        System.out.println("Get all contacts: ");
        for (Contact contact : response.getBody()) {
            System.out.println(contact);
        }
    }

    @Test
    void e_delete() {
        String url = baseUrl() + "/delete/" + contact_with_ID.getContactID();
        this.restTemplate.delete(url);

        //verify if the object is deleted.
        ResponseEntity<Contact> readContact =
                this.restTemplate.getForEntity(baseUrl() + "/read/" + contact_with_ID.getContactID(), Contact.class);

        assertEquals(HttpStatus.NOT_FOUND, readContact.getStatusCode());
        System.out.println("After deletion the status code for readContact is: " + readContact.getStatusCode());
    }
}
