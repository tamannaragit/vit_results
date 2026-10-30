package com.vit.semesterresult.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "semester_results", uniqueConstraints =
        @UniqueConstraint(name = "uk_semester_results_student_semester", columnNames = {"student_id", "semester"}))
public class SemesterResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @Column(nullable = false, length = 30)
    private String semester;
    @Column(name = "total_final_marks", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalFinalMarks;
    @Column(name = "overall_average", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallAverage;
    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;
    @OneToMany(mappedBy = "semesterResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("subjectOrder ASC")
    private List<ResultSubject> subjects = new ArrayList<>();

    @PrePersist void setCalculatedAt() { calculatedAt = LocalDateTime.now(); }
    public void addSubject(ResultSubject subject) { subject.setSemesterResult(this); subjects.add(subject); }
    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public BigDecimal getTotalFinalMarks() { return totalFinalMarks; }
    public void setTotalFinalMarks(BigDecimal totalFinalMarks) { this.totalFinalMarks = totalFinalMarks; }
    public BigDecimal getOverallAverage() { return overallAverage; }
    public void setOverallAverage(BigDecimal overallAverage) { this.overallAverage = overallAverage; }
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public List<ResultSubject> getSubjects() { return subjects; }
}
