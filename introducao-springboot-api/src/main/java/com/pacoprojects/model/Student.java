package com.pacoprojects.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Table
@Entity(name = "Student")
public class Student implements Serializable {

    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq", allocationSize = 1)
    @GeneratedValue(generator = "student_seq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    private String name;

    private String email;

    @Transient
    private Integer age;

    private LocalDate dob;

    public Student(String name, String email, LocalDate dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public Integer getAge() {
        return LocalDate.now().compareTo(dob);
    }
}
