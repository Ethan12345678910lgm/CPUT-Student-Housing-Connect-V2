package co.za.cput.service.users.implementation;

import co.za.cput.domain.users.Student;
import co.za.cput.repository.users.StudentRepository;
import co.za.cput.service.users.IStudentService;
import co.za.cput.util.LinkingEntitiesHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student create(Student student) {
        Student studentWithLinkedBookings = LinkingEntitiesHelper.setStudentInBookings(student);
        return studentRepository.save(studentWithLinkedBookings);
    }

    @Override
    public Student read(Long Id) {
        return studentRepository.findById(Id).orElse(null);
    }

    @Override
    public Student update(Student student) {
        Student existingStudent = studentRepository.findById(student.getStudentID()).orElse(null);
        if (existingStudent == null) {
            return null;
        }

        Student studentWithLinkedBookings = LinkingEntitiesHelper.setStudentInBookings(student);

        Student updatedStudent = new Student.Builder()
                .copy(existingStudent)
                .setStudentName(studentWithLinkedBookings.getStudentName())
                .setStudentSurname(studentWithLinkedBookings.getStudentSurname())
                .setDateOfBirth(studentWithLinkedBookings.getDateOfBirth())
                .setGender(studentWithLinkedBookings.getGender())
                .setPassword(studentWithLinkedBookings.getPassword())
                .setRegistrationDate(studentWithLinkedBookings.getRegistrationDate())
                .setIsStudentVerified(studentWithLinkedBookings.getIsStudentVerified())
                .setFundingStatus(studentWithLinkedBookings.getFundingStatus())
                .setContact(studentWithLinkedBookings.getContact() != null
                        ? studentWithLinkedBookings.getContact()
                        : existingStudent.getContact())
                .setBookings(studentWithLinkedBookings.getBookings() != null
                        ? studentWithLinkedBookings.getBookings()
                        : existingStudent.getBookings())
                .build();

        return studentRepository.save(updatedStudent);    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void delete(Long Id) {
        studentRepository.deleteById(Id);
    }
}
