package com.pacoprojects.controller;

import com.pacoprojects.model.Student;
import com.pacoprojects.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        studentService.registerNewStudent(student);
    }

    @DeleteMapping(path = "{id}")
    public void deleteStudent(@PathVariable(name = "id") Long id)  {
        studentService.deleteStudent(id);
    }

    @PutMapping
    public void  updateStudent(@RequestBody Student student) {
        studentService.updateStudent(student);
    }

    @PutMapping(path = "{id}")
    public void  updateStudentAmigosCode(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email) {
        studentService.updateStudentAmigosCode(id,name,email);
    }
}
