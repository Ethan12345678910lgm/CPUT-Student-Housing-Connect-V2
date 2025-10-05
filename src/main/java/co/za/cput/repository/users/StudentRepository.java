package co.za.cput.repository.users;

import co.za.cput.domain.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByContactEmail(String email);
    long countByIsStudentVerifiedTrue();
    long countByIsStudentVerifiedFalse();
    List<Student> findByIsStudentVerified(boolean isStudentVerified);
}
