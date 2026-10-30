package com.vit.semesterresult.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class SemesterResultForm {

    @NotBlank(message = "Student name is required.")
    @Size(max = 100, message = "Student name must not exceed 100 characters.")
    private String studentName;

    @NotBlank(message = "PRN / Roll Number is required.")
    @Pattern(
            regexp = "^[0-9]{8}$",
            message = "PRN must contain exactly 8 digits."
    )
    private String prn;

    @NotBlank(message = "Semester is required.")
    @Pattern(
            regexp = "^[1-8]$",
            message = "Semester must be a single number from 1 to 8."
    )
    private String semester;

    @Valid
    @Size(
            min = 4,
            max = 4,
            message = "Exactly four subjects are required."
    )
    private List<SubjectMarksForm> subjects = new ArrayList<>();

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<SubjectMarksForm> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectMarksForm> subjects) {
        this.subjects = subjects;
    }
}