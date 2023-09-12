package com.pacoprojects.service;

import com.pacoprojects.model.Student;
import com.pacoprojects.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getStudents() {

        return studentRepository.findAll();

/*        return List.of(new Student(
                1L,
                "Gustavo Paco",
                "gustavopaco@gmail.com",
                LocalDate.now().minusYears(1989).getYear(),
                LocalDate.of(1989, Month.SEPTEMBER,24)
        ));*/
    }

    public void registerNewStudent(Student student) {

        boolean exists = studentRepository.existsStudentByEmailContainingIgnoreCase(student.getEmail());

        if (exists) {
            throw new IllegalArgumentException("Email already taken!");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {

        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            throw new IllegalArgumentException("User does not exist");
        }

        studentRepository.deleteById(id);
    }

    @Transactional
    public void updateStudent(Student student) {

        Optional<Student> studentOptional = studentRepository.findById(student.getId());

        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("Student does not exist on DB");
        }

        if (student.getName() != null && !student.getName().isBlank() && !Objects.equals(studentOptional.get().getName(), student.getName())) {
            studentOptional.get().setName(student.getName());
        }

        if (student.getEmail() != null && !student.getEmail().isBlank()) {

            boolean emailTaken = studentRepository.existsStudentButNewEmailAlreadyTaken(student.getEmail(), student.getId());

            if (emailTaken) {
                throw new IllegalArgumentException("New Email already taken");
            }
            studentOptional.get().setEmail(student.getEmail());
        }
    }

    @Transactional
    public void updateStudentAmigosCode(Long id, String name, String email) {

        Student student = studentRepository.findById(id).orElseThrow(() ->
        {
            throw new IllegalArgumentException("Student does not exist on DB");
        });

        if (name != null && !name.isBlank() && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null && !email.isBlank() && !Objects.equals(student.getEmail(), email)) {
            boolean emailTaken = studentRepository.existsStudentByEmailContainingIgnoreCase(email);

            if (emailTaken) {
                throw new IllegalArgumentException("New Email already taken");
            }
            student.setEmail(email);
        }
    }
}
