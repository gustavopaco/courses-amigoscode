package com.pacoprojects.config;

import com.pacoprojects.model.Student;
import com.pacoprojects.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student student1 = new Student(
                    "Gustavo Paco",
                    "gustavopaco@gmail.com",
                    LocalDate.of(1989, Month.SEPTEMBER, 24));

            Student student2 = new Student(
                    "Augusto Paco",
                    "augustopaco@gmail.com",
                    LocalDate.of(1991, Month.JULY, 16));

            /* Salvando no Banco uma lista de objetos estudantes */
            studentRepository.saveAll(List.of(student1,student2));
        };
    }
}
