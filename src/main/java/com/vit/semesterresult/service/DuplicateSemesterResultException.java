package com.vit.semesterresult.service;

public class DuplicateSemesterResultException extends RuntimeException {
    public DuplicateSemesterResultException() {
        super("A result already exists for this PRN and semester.");
    }
}
