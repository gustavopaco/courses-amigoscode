package com.pacoprojects.webmvnsbamigoscodespringsecurity.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Gustavo"),
            new Student(2,"Augusto"),
            new Student(3,"Thiago")
    );

    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable(value = "studentId") Integer studentId) {

        /* Metodo de stream que varre a lista, filtra por ID e compara se ID da lista eh == a ID vindo da URL,
            se encontrado retorna o primeiro ObjEstudante encontrado
            se nao lanca excecao
        */
        return STUDENTS.stream()
                .filter(student -> student.getStudentID().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student " + studentId + " does not exist"));


        /* Funcao varre a lista de estudantes e verifica se existe algum com ID vindo da URL GET se nao lanca excecao */
//        Student studentObj;
//        for (Student s: STUDENTS
//        ) {
//            if (s.getStudentID().equals(studentId)) {
//                studentObj = s;
//                return studentObj;
//            } else {
//                throw new IllegalArgumentException("Student " + studentId + "does not exist");
//            }
//        }
    }

}
