package co.za.cput.controller.generic;

import co.za.cput.domain.generic.Address;
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
class AddressControllerTest {

    private static Address address1 = AddressFactory.createAddress(
            "789B",
            "Kingsway Avenue",
            "Auckland Park",
            "Johannesburg",
            2092
    );
    private static Address address_with_ID;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/HouseConnect/Address";
    }

    @Test
    void a_create() {
        String url = baseUrl() + "/create";
        ResponseEntity<Address> postResponse = restTemplate.postForEntity(url, address1, Address.class);
        assertNotNull(postResponse.getBody());
        System.out.println("postResponse: " + postResponse.getBody());
        address_with_ID = postResponse.getBody();
        assertNotNull(address_with_ID);
        System.out.println("address_with_ID: " + address_with_ID);
    }

    @Test
    void b_read() {
        assertNotNull(address_with_ID, "Address is null");
        String url = baseUrl() + "/read/" + address_with_ID.getAddressID();
        ResponseEntity<Address> response = restTemplate.getForEntity(url, Address.class);
        assertNotNull(response.getBody());
        System.out.println("response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(address_with_ID, "Address is null");
        String url = baseUrl() + "/update";
        Address updatedAddress = new Address.Builder()
                .copy(address_with_ID)
                .setStreetName("Mandela Avenue")
                .build();
        this.restTemplate.put(url, updatedAddress);

        //verify if the object is updated using the read method.
        ResponseEntity<Address> readAddress =
                this.restTemplate.getForEntity(baseUrl() + "/read/" + updatedAddress.getAddressID(), Address.class);
        assertEquals(HttpStatus.OK, readAddress.getStatusCode());

        Address newAddress = readAddress.getBody();
        assertEquals("Mandela Avenue", newAddress.getStreetName());
        System.out.println("newAddress: " + newAddress);
    }

    @Test
    void d_getAllAddress() {
        String url = baseUrl() + "/getAllAddress";
        ResponseEntity<Address[]> response = restTemplate.getForEntity(url, Address[].class);
        assertNotNull(response.getBody());
        System.out.println("response: ");
        for (Address address : response.getBody()) {
            System.out.println(address);
        }
    }

    @Test
    void e_delete() {
        String url = baseUrl() + "/delete/" + address_with_ID.getAddressID();
        System.out.println("Deleted address: " + address_with_ID);
        this.restTemplate.delete(url);

        //Verify if the object is deleted.
        ResponseEntity<Address> readAddress =
                this.restTemplate.getForEntity(baseUrl() + "/read/" + address_with_ID.getAddressID(), Address.class);

        // Expect 404 after deletion
        assertEquals(HttpStatus.NOT_FOUND, readAddress.getStatusCode());
        System.out.println("After deletion, the readAddress status code: " + readAddress.getStatusCode());
    }
}
