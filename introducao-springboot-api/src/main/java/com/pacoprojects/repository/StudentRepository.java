package com.pacoprojects.repository;

import com.pacoprojects.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Boolean existsStudentByEmailContainingIgnoreCase(String email);

    @Query(value = "select exists(select s from Student s where s.email = ?1 and s.id != ?2)")
    Boolean existsStudentButNewEmailAlreadyTaken(String email, Long id);

}
