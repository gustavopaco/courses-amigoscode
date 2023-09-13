package com.pacoprojects.springbootSecurityV5.student;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "management/api/v1/students")
public class StudentManagementController {

    private final StudentConfig studentConfig;

    public StudentManagementController(StudentConfig studentConfig) {
        this.studentConfig = studentConfig;
    }

    /* Pode usar valores como: hasRole('ROLE_') | hasAnyRole('ROLE_') | hasAuthority('permission') | hasAnyAuthority('permission') */
    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_TRAINEE')")
    public List<Student> getAllStudents() {
        return studentConfig.getAllStudents();
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('student:write')")
    public ResponseEntity<?> registerNewStudent(@RequestBody Student student) {

        /* Percorre a lista de Students verificando se ID ou Username ja existem, se sim lanca exception se nao registra na lista*/
        Optional<Student> studentOptional = studentConfig.getAllStudents().stream()
                .filter(student1 -> (student1.getStudentID().equals(student.getStudentID())) || student1.getStudentName().equals(student.getStudentName()))
                .findFirst();
        if (studentOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist");
        } else {
            studentConfig.getAllStudents().add(student);
        }
        System.out.println(student.toString());
        return ResponseEntity.ok("New user registred");
    }

    @PutMapping
    @PreAuthorize(value = "hasAuthority('student:write')")
    public void updateStudent(@RequestBody Student student) {

        /* Passa pela lista de Students, filtrando Student onde id == getStudentID(), se achar retorna o Student*/
        Optional<Student> studentOptional = studentConfig.getAllStudents().stream()
                .filter(student1 -> student1.getStudentID().equals(student.getStudentID()))
                .findFirst();

        /* Se Student existe,
        entao removemos Student da lista no Index = getStudentFiltradoID
        e adicionamos novo Student vindo de parametro a lista,
        se nao lança exception*/
        studentOptional.ifPresentOrElse(student1 -> {
                    studentConfig.getAllStudents().remove(studentOptional.get());
                    studentConfig.getAllStudents().add(student);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student cant be updated because he does not exist");
                }
        );
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize(value = "hasAuthority('student:write')")
    public void deleteStudent(@PathVariable(name = "id") Integer id) {

        /* Passa pela lista e filtrando Student onde id == getStudentID(), se achar retorna o Student*/
        Optional<Student> studentOptional = studentConfig.getAllStudents().stream()
                .filter(student -> student.getStudentID().equals(id))
                .findFirst();

        /* Se estudante existe na lista deleta, se nao lança exception*/
        studentOptional.ifPresentOrElse(
                studentConfig.getAllStudents()::remove,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student does not exist");
                }
        );
    }
}
