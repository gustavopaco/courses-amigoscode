package br.com.curso.webmvnspringbootjpacourse.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student gustavo = new Student("Gustavo",
                    "gustavopaco@gmail.com",
                    LocalDate.of(1989,9,24));

            Student augusto = new Student("Augusto",
                    "augustopaco@gmail.com",
                    LocalDate.of(1991,7,16));

            Student vagaba = new Student("Vagaba",
                    "vagaba@gmail.com",
                    LocalDate.of(1984,2,10));

            Student raquel = new Student("Raquel",
                    "raquel@gmail.com",
                    LocalDate.of(1964,4,23));

            Student joaquim = new Student("Joaquim",
                    "joaquim@gmail.com",
                    LocalDate.of(1960,4,28));

            Student alex = new Student("Alex",
                    "alex@gmail.com",
                    LocalDate.of(2000,1,10));

        studentRepository.saveAll(List.of(gustavo, augusto, vagaba, raquel, joaquim, alex));
        };
    }
}
