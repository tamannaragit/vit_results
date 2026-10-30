package com.vit.semesterresult.repository;

import com.vit.semesterresult.entity.SemesterResult;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SemesterResultRepository extends JpaRepository<SemesterResult, Long> {
    @EntityGraph(attributePaths = {"student", "subjects"})
    Optional<SemesterResult> findDetailedById(Long id);
    boolean existsByStudent_PrnAndSemester(String prn, String semester);
}
