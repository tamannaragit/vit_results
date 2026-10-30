package com.vit.semesterresult.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "result_subjects")
public class ResultSubject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_result_id", nullable = false)
    private SemesterResult semesterResult;
    @Column(name = "subject_order", nullable = false) private byte subjectOrder;
    @Column(name = "subject_name", nullable = false, length = 100) private String subjectName;
    @Column(name = "mse_marks", nullable = false, precision = 5, scale = 2) private BigDecimal mseMarks;
    @Column(name = "ese_marks", nullable = false, precision = 5, scale = 2) private BigDecimal eseMarks;
    @Column(name = "final_marks", nullable = false, precision = 5, scale = 2) private BigDecimal finalMarks;
    public Long getId() { return id; }
    public SemesterResult getSemesterResult() { return semesterResult; }
    public void setSemesterResult(SemesterResult semesterResult) { this.semesterResult = semesterResult; }
    public byte getSubjectOrder() { return subjectOrder; }
    public void setSubjectOrder(byte subjectOrder) { this.subjectOrder = subjectOrder; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public BigDecimal getMseMarks() { return mseMarks; }
    public void setMseMarks(BigDecimal mseMarks) { this.mseMarks = mseMarks; }
    public BigDecimal getEseMarks() { return eseMarks; }
    public void setEseMarks(BigDecimal eseMarks) { this.eseMarks = eseMarks; }
    public BigDecimal getFinalMarks() { return finalMarks; }
    public void setFinalMarks(BigDecimal finalMarks) { this.finalMarks = finalMarks; }
}
