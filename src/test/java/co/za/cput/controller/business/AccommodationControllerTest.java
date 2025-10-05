package co.za.cput.controller.business;

import co.za.cput.domain.business.Accommodation;
import co.za.cput.domain.generic.Address;
import co.za.cput.factory.business.AccommodationFactory;
import co.za.cput.factory.generic.AddressFactory;
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
class AccommodationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/HouseConnect/Accommodation";
    }

    private Address address = AddressFactory.createAddress(
            "42", "Main Street", "Observatory", "Cape Town", 7925
    );

    private Accommodation accommodation1 = AccommodationFactory.createAccommodation(
            4200.00,
            true,
            true,
            1.2,
            true,
            Accommodation.RoomType.SINGLE,
            Accommodation.BathroomType.PRIVATE,
            Accommodation.AccommodationStatus.AVAILABLE,
            address,
            null,
            null
    );

    private static Accommodation accommodation_with_Id;

    @Test
    void a_create() {
        String url = baseUrl() + "/create";
        ResponseEntity<Accommodation> response = this.restTemplate.postForEntity(url, accommodation1, Accommodation.class);
        assertNotNull(response.getBody());
        accommodation_with_Id = response.getBody();
        System.out.println("Created accommodation: " + accommodation_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(accommodation_with_Id);
        String url = baseUrl() + "/read/" + accommodation_with_Id.getAccommodationID();
        ResponseEntity<Accommodation> response = this.restTemplate.getForEntity(url, Accommodation.class);
        assertNotNull(response.getBody());
        System.out.println("Read accommodation: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(accommodation_with_Id);
        String url = baseUrl() + "/update";
        Accommodation updatedAccommodation = new Accommodation.Builder()
                .copy(accommodation_with_Id)
                .setRent(4500.00)
                .setWifiAvailable(false)
                .build();
        this.restTemplate.put(url, updatedAccommodation);

        ResponseEntity<Accommodation> response = this.restTemplate.getForEntity(
                baseUrl() + "/read/" + updatedAccommodation.getAccommodationID(), Accommodation.class);

        assertNotNull(response.getBody());
        assertEquals(4500.00, response.getBody().getRent());
        assertFalse(response.getBody().getIsWifiAvailable());
        System.out.println("Updated accommodation: " + response.getBody());
    }

    @Test
    void d_getAllAccommodations() {//THIS TEST IS FAILING BECAUSE OF THE BI-DIRECTIONAL RELATIONSHIP
        String url = baseUrl() + "/getAllAccommodation";
        ResponseEntity<Accommodation[]> response = this.restTemplate.getForEntity(url, Accommodation[].class);
        assertNotNull(response.getBody());
        System.out.println("All accommodations:");
        for (Accommodation accommodation : response.getBody()) {
            System.out.println(accommodation);
        }
    }

    @Test
    void e_delete() {
        assertNotNull(accommodation_with_Id);
        String url = baseUrl() + "/delete/" + accommodation_with_Id.getAccommodationID();
        this.restTemplate.delete(url);

        ResponseEntity<Accommodation> response = this.restTemplate.getForEntity(
                baseUrl() + "/read/" + accommodation_with_Id.getAccommodationID(), Accommodation.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        System.out.println("Deleted accommodation with ID: " + accommodation_with_Id.getAccommodationID());
    }
}
