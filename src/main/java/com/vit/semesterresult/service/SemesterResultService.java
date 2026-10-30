package com.vit.semesterresult.service;

import com.vit.semesterresult.entity.ResultSubject;
import com.vit.semesterresult.entity.SemesterResult;
import com.vit.semesterresult.entity.Student;
import com.vit.semesterresult.form.SemesterResultForm;
import com.vit.semesterresult.form.SubjectMarksForm;
import com.vit.semesterresult.repository.SemesterResultRepository;
import com.vit.semesterresult.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterResultService {

    private static final BigDecimal MSE_WEIGHT = new BigDecimal("0.30");
    private static final BigDecimal ESE_WEIGHT = new BigDecimal("0.70");

    private final StudentRepository studentRepository;
    private final SemesterResultRepository semesterResultRepository;

    public SemesterResultService(
            StudentRepository studentRepository,
            SemesterResultRepository semesterResultRepository) {
        this.studentRepository = studentRepository;
        this.semesterResultRepository = semesterResultRepository;
    }

    @Transactional
    public SemesterResult prepareResult(SemesterResultForm form) {

        normalize(form);
        validateForProcessing(form);

        if (semesterResultRepository.existsByStudent_PrnAndSemester(
                form.getPrn(),
                form.getSemester())) {
            throw new DuplicateSemesterResultException();
        }

        Student student = studentRepository
                .findByPrn(form.getPrn().trim())
                .orElseGet(Student::new);

        student.setStudentName(form.getStudentName().trim());
        student.setPrn(form.getPrn().trim());

        student = studentRepository.save(student);

        SemesterResult result = new SemesterResult();
        result.setStudent(student);
        result.setSemester(form.getSemester().trim());

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < form.getSubjects().size(); i++) {

            SubjectMarksForm input = form.getSubjects().get(i);

            BigDecimal finalMarks = calculateFinalMarks(
                    input.getMseMarks(),
                    input.getEseMarks()
            );

            ResultSubject subject = new ResultSubject();

            subject.setSubjectOrder((byte) (i + 1));
            subject.setSubjectName(input.getSubjectName().trim());
            subject.setMseMarks(input.getMseMarks());
            subject.setEseMarks(input.getEseMarks());
            subject.setFinalMarks(finalMarks);

            result.addSubject(subject);

            total = total.add(finalMarks);
        }

        result.setTotalFinalMarks(
                total.setScale(2, RoundingMode.HALF_UP)
        );

        result.setOverallAverage(
                total.divide(
                        BigDecimal.valueOf(4),
                        2,
                        RoundingMode.HALF_UP
                )
        );

        return semesterResultRepository.save(result);
    }

    public void normalize(SemesterResultForm form) {

        if (form.getStudentName() != null) {
            form.setStudentName(form.getStudentName().trim());
        }

        if (form.getPrn() != null) {
            form.setPrn(form.getPrn().trim());
        }

        if (form.getSemester() != null) {
            form.setSemester(form.getSemester().trim());
        }

        if (form.getSubjects() != null) {
            form.getSubjects().forEach(subject -> {
                if (subject != null && subject.getSubjectName() != null) {
                    subject.setSubjectName(
                            subject.getSubjectName().trim()
                    );
                }
            });
        }
    }

    public List<Integer> duplicateSubjectIndexes(
            SemesterResultForm form) {

        List<Integer> duplicates = new ArrayList<>();

        if (form.getSubjects() == null) {
            return duplicates;
        }

        for (int i = 0; i < form.getSubjects().size(); i++) {

            SubjectMarksForm current = form.getSubjects().get(i);

            if (current == null
                    || current.getSubjectName() == null
                    || current.getSubjectName().isBlank()) {
                continue;
            }

            for (int j = 0; j < i; j++) {

                SubjectMarksForm previous = form.getSubjects().get(j);

                if (previous != null
                        && previous.getSubjectName() != null
                        && current.getSubjectName()
                        .trim()
                        .equalsIgnoreCase(
                                previous.getSubjectName().trim())) {

                    duplicates.add(i);
                    break;
                }
            }
        }

        return duplicates;
    }

    private void validateForProcessing(
            SemesterResultForm form) {

        // PRN: exactly 8 digits
        if (form.getPrn() == null
                || !form.getPrn().matches("\\d{8}")) {

            throw new IllegalArgumentException(
                    "PRN must contain exactly 8 digits."
            );
        }

        // Semester: exactly one integer from 1 to 8
        if (form.getSemester() == null
                || !form.getSemester().matches("[1-8]")) {

            throw new IllegalArgumentException(
                    "Semester must be a single number from 1 to 8."
            );
        }

        // Exactly four subjects
        if (form.getSubjects() == null
                || form.getSubjects().size() != 4) {

            throw new IllegalArgumentException(
                    "Exactly four subjects are required."
            );
        }

        // Subject names and marks
        for (SubjectMarksForm subject : form.getSubjects()) {

            if (subject == null
                    || subject.getSubjectName() == null
                    || subject.getSubjectName().isBlank()
                    || !validMark(subject.getMseMarks())
                    || !validMark(subject.getEseMarks())) {

                throw new IllegalArgumentException(
                        "Please provide four subjects and marks from 0 to 100."
                );
            }
        }

        // No duplicate subjects
        if (!duplicateSubjectIndexes(form).isEmpty()) {
            throw new IllegalArgumentException(
                    "Subject names must be unique."
            );
        }
    }

    private boolean validMark(BigDecimal mark) {

        return mark != null
                && mark.compareTo(BigDecimal.ZERO) >= 0
                && mark.compareTo(BigDecimal.valueOf(100)) <= 0;
    }

    public BigDecimal calculateFinalMarks(
            BigDecimal mseMarks,
            BigDecimal eseMarks) {

        return mseMarks
                .multiply(MSE_WEIGHT)
                .add(eseMarks.multiply(ESE_WEIGHT))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public SemesterResult getResult(Long id) {

        return semesterResultRepository
                .findDetailedById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Result not found."
                        )
                );
    }
}