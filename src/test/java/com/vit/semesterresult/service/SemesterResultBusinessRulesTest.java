package com.vit.semesterresult.service;

import com.vit.semesterresult.entity.SemesterResult;
import com.vit.semesterresult.entity.Student;
import com.vit.semesterresult.form.SemesterResultForm;
import com.vit.semesterresult.form.SubjectMarksForm;
import com.vit.semesterresult.repository.SemesterResultRepository;
import com.vit.semesterresult.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SemesterResultBusinessRulesTest {

    private final StudentRepository students = mock(StudentRepository.class);
    private final SemesterResultRepository results = mock(SemesterResultRepository.class);
    private final SemesterResultService service =
            new SemesterResultService(students, results);

    @Test
    void rejectsMarksBelowZeroOrAboveOneHundred() {
        SemesterResultForm below =
                validForm("12345678", "1");
        below.getSubjects().get(0)
                .setMseMarks(new BigDecimal("-1"));

        SemesterResultForm above =
                validForm("87654321", "1");
        above.getSubjects().get(0)
                .setEseMarks(new BigDecimal("101"));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.prepareResult(below)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.prepareResult(above)
        );

        verifyNoInteractions(students, results);
    }

    @Test
    void rejectsDuplicateSubjectNamesIgnoringCaseAndSpaces() {
        SemesterResultForm form =
                validForm("12345678", "1");

        form.getSubjects().get(1).setSubjectName("  dbms ");
        form.getSubjects().get(0).setSubjectName("DBMS");

        service.normalize(form);

        assertEquals(
                List.of(1),
                service.duplicateSubjectIndexes(form)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.prepareResult(form)
        );
    }

    @Test
    void requiresExactlyFourSubjects() {
        SemesterResultForm form =
                validForm("12345678", "1");

        form.getSubjects().remove(3);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.prepareResult(form)
        );
    }

    @Test
    void rejectsSamePrnAndSemester() {
        SemesterResultForm form =
                validForm("12345678", "1");

        when(results.existsByStudent_PrnAndSemester(
                "12345678",
                "1"
        )).thenReturn(true);

        assertThrows(
                DuplicateSemesterResultException.class,
                () -> service.prepareResult(form)
        );
    }

    @Test
    void allowsSamePrnDifferentSemesterAndDifferentPrnSameSemester() {

        when(results.existsByStudent_PrnAndSemester(any(), any()))
                .thenReturn(false);

        when(students.findByPrn(any()))
                .thenReturn(Optional.empty());

        when(students.save(any(Student.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(results.save(any(SemesterResult.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        assertDoesNotThrow(
                () -> service.prepareResult(
                        validForm("12345678", "1")
                )
        );

        assertDoesNotThrow(
                () -> service.prepareResult(
                        validForm("12345678", "2")
                )
        );

        assertDoesNotThrow(
                () -> service.prepareResult(
                        validForm("87654321", "1")
                )
        );
    }

    @Test
    void acceptsOnlySemesterNumbersOneToEight() {

        for (int semester = 1; semester <= 8; semester++) {

            SemesterResultForm form =
                    validForm("12345678", String.valueOf(semester));

            when(results.existsByStudent_PrnAndSemester(any(), any()))
                    .thenReturn(false);

            when(students.findByPrn(any()))
                    .thenReturn(Optional.empty());

            when(students.save(any(Student.class)))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0));

            when(results.save(any(SemesterResult.class)))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0));

            assertDoesNotThrow(
                    () -> service.prepareResult(form)
            );
        }
    }

    @Test
    void rejectsInvalidSemesterValues() {

        String[] invalidSemesters = {
                "0",
                "9",
                "10",
                "-1",
                "5.5",
                "7-5",
                "7-100",
                "Sem 5",
                "Semester 5",
                "5th",
                "abc",
                "",
                " "
        };

        for (String semester : invalidSemesters) {

            SemesterResultForm form =
                    validForm("12345678", semester);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.prepareResult(form)
            );
        }
    }

    @Test
    void rejectsInvalidPrnValues() {

        String[] invalidPrns = {
                "1234567",
                "123456789",
                "1234abcd",
                "abcd1234",
                "12-345678",
                "1234 5678",
                "",
                "1234567.0"
        };

        for (String prn : invalidPrns) {

            SemesterResultForm form =
                    validForm(prn, "1");

            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.prepareResult(form)
            );
        }
    }

    @Test
    void acceptsExactlyEightDigitPrn() {

        SemesterResultForm form =
                validForm("12345678", "1");

        when(results.existsByStudent_PrnAndSemester(any(), any()))
                .thenReturn(false);

        when(students.findByPrn(any()))
                .thenReturn(Optional.empty());

        when(students.save(any(Student.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(results.save(any(SemesterResult.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        assertDoesNotThrow(
                () -> service.prepareResult(form)
        );
    }

    private SemesterResultForm validForm(
            String prn,
            String semester) {

        SemesterResultForm form =
                new SemesterResultForm();

        form.setStudentName("Asha Patil");
        form.setPrn(prn);
        form.setSemester(semester);

        for (int i = 1; i <= 4; i++) {

            SubjectMarksForm subject =
                    new SubjectMarksForm();

            subject.setSubjectName("Subject " + i);
            subject.setMseMarks(new BigDecimal("50"));
            subject.setEseMarks(new BigDecimal("50"));

            form.getSubjects().add(subject);
        }

        return form;
    }
}