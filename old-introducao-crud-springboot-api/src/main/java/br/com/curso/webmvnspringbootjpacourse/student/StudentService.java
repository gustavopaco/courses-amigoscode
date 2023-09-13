package br.com.curso.webmvnspringbootjpacourse.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new NoSuchElementException("Email already taken");
        }
        studentRepository.save(student);
    }

    public void removeStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if (!exists) {
            throw new NoSuchElementException("No student found");
        }
        studentRepository.deleteById(id);

    }

    @Transactional
    public void updateStudent(Long studentID, String name, String email) {
        Optional<Student> studentOptional = studentRepository.findById(studentID);
        if (studentOptional.isEmpty()) {
            throw new IllegalStateException("Student not found for update, contact the admin");
        }

        if (name != null && name.length() > 0 && !Objects.equals(studentOptional.get().getName(), name)) {
            studentOptional.get().setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(studentOptional.get().getEmail(), email)) {
            boolean exists = studentRepository.findByEmail(email).isPresent();
            if (exists) {
                throw new IllegalStateException("Email already taken");
            }
            studentOptional.get().setEmail(email);
        }
    }
}
