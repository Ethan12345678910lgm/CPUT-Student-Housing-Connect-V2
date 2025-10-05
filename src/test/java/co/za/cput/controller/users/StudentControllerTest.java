package co.za.cput.controller.users;

import co.za.cput.domain.generic.Contact;
import co.za.cput.domain.users.Student;
import co.za.cput.factory.generic.ContactFactory;
import co.za.cput.factory.user.StudentFactory;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/HouseConnect/Student";
    }

    private static final Contact contact = ContactFactory.createContact(
            "Abigail@yahoo.com", "0712345678", "0723456789", true, true,
            Contact.PreferredContactMethod.EMAIL
    );

    private static final Student student = StudentFactory.createStudent(
            "Kwanda", "Twalo", LocalDate.of(2000, 5, 15),
            "Male", "student123", LocalDateTime.now(),
            true, Student.FundingStatus.FUNDED, contact, null
    );

    private static Student studentWithId;

    @Test
    void a_create() {
        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl() + "/create", student, Student.class);
        assertNotNull(response.getBody());
        studentWithId = response.getBody();
        System.out.println("Created student: " + studentWithId);
    }

    @Test
    void b_read() {
        String url = baseUrl() + "/read/" + studentWithId.getStudentID();
        ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class);
        assertNotNull(response.getBody());
        System.out.println("Read student: " + response.getBody());
    }

    @Test
    void c_update() {
        Student updated = new Student.Builder().copy(studentWithId).setFundingStatus(Student.FundingStatus.SELF_FUNDED).build();
        restTemplate.put(baseUrl() + "/update", updated);
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl() + "/read/" + updated.getStudentID(), Student.class);
        assertEquals(Student.FundingStatus.SELF_FUNDED, response.getBody().getFundingStatus());
        System.out.println("Updated student: " + response.getBody());
    }

    @Test
    void d_getAllStudents() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(baseUrl() + "/getAllStudents", Student[].class);
        assertNotNull(response.getBody());
        System.out.println("All students: ");
        for (Student s : response.getBody()) {
            System.out.println(s);
        }
    }

    @Test
    void e_delete() {
        restTemplate.delete(baseUrl() + "/delete/" + studentWithId.getStudentID());
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl() + "/read/" + studentWithId.getStudentID(), Student.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
