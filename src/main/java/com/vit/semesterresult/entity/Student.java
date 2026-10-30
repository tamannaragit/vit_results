package com.vit.semesterresult.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_name", nullable = false, length = 100)
    private String studentName;
    @Column(nullable = false, unique = true, length = 40)
    private String prn;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist void setCreatedAt() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getPrn() { return prn; }
    public void setPrn(String prn) { this.prn = prn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
