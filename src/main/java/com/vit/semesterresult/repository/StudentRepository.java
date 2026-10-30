package com.vit.semesterresult.repository;

import com.vit.semesterresult.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByPrn(String prn);
}
