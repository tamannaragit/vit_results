package com.vit.semesterresult.form;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class SubjectMarksForm {
    @NotBlank(message = "Subject name is required.")
    @Size(max = 100)
    private String subjectName;
    @NotNull(message = "MSE marks are required.") @DecimalMin(value = "0.00", message = "MSE marks must be between 0 and 100.") @DecimalMax(value = "100.00", message = "MSE marks must be between 0 and 100.") @Digits(integer = 3, fraction = 2, message = "Use at most two decimal places.")
    private BigDecimal mseMarks;
    @NotNull(message = "ESE marks are required.") @DecimalMin(value = "0.00", message = "ESE marks must be between 0 and 100.") @DecimalMax(value = "100.00", message = "ESE marks must be between 0 and 100.") @Digits(integer = 3, fraction = 2, message = "Use at most two decimal places.")
    private BigDecimal eseMarks;
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public BigDecimal getMseMarks() { return mseMarks; }
    public void setMseMarks(BigDecimal mseMarks) { this.mseMarks = mseMarks; }
    public BigDecimal getEseMarks() { return eseMarks; }
    public void setEseMarks(BigDecimal eseMarks) { this.eseMarks = eseMarks; }
}
