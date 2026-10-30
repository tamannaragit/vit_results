CREATE DATABASE IF NOT EXISTS vit_semester_result;
USE vit_semester_result;

CREATE TABLE students (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_name VARCHAR(100) NOT NULL,
    prn VARCHAR(40) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_students_prn UNIQUE (prn)
);

CREATE TABLE semester_results (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    semester VARCHAR(30) NOT NULL,
    total_final_marks DECIMAL(8,2) NOT NULL,
    overall_average DECIMAL(5,2) NOT NULL,
    calculated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_semester_results_student_semester UNIQUE (student_id, semester),
    CONSTRAINT fk_semester_results_student
        FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE result_subjects (
    id BIGINT NOT NULL AUTO_INCREMENT,
    semester_result_id BIGINT NOT NULL,
    -- Java byte maps to MariaDB TINYINT; this is restricted to positions 1 through 4 below.
    subject_order TINYINT NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    mse_marks DECIMAL(5,2) NOT NULL,
    ese_marks DECIMAL(5,2) NOT NULL,
    final_marks DECIMAL(5,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_result_subject_order UNIQUE (semester_result_id, subject_order),
    CONSTRAINT ck_result_subject_order CHECK (subject_order BETWEEN 1 AND 4),
    CONSTRAINT ck_mse_marks CHECK (mse_marks BETWEEN 0 AND 100),
    CONSTRAINT ck_ese_marks CHECK (ese_marks BETWEEN 0 AND 100),
    CONSTRAINT fk_result_subjects_result
        FOREIGN KEY (semester_result_id) REFERENCES semester_results(id)
        ON DELETE CASCADE
);
