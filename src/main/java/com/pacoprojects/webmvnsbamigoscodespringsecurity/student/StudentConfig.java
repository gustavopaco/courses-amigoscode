package com.pacoprojects.webmvnsbamigoscodespringsecurity.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class StudentConfig {

    private List<Student> studentList;


    @Bean
    CommandLineRunner commandLineRunner() {

        return args -> {
            studentList = new ArrayList<>();
            studentList.add(new Student(1, "Gustavo"));
            studentList.add(new Student(2, "Augusto"));
            studentList.add(new Student(3, "Thiago"));
        };
    }

    public List<Student> getAllStudents() {
        return studentList;
    }
}
