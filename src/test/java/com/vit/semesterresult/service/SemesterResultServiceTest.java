package com.vit.semesterresult.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SemesterResultServiceTest {
    private final SemesterResultService service = new SemesterResultService(null, null);
    @Test void calculatesFullMarks() { assertEquals(new BigDecimal("100.00"), service.calculateFinalMarks(new BigDecimal("100"), new BigDecimal("100"))); }
    @Test void calculatesEqualMarks() { assertEquals(new BigDecimal("50.00"), service.calculateFinalMarks(new BigDecimal("50"), new BigDecimal("50"))); }
    @Test void calculatesEseOnlyMarks() { assertEquals(new BigDecimal("70.00"), service.calculateFinalMarks(BigDecimal.ZERO, new BigDecimal("100"))); }
    @Test void calculatesMseOnlyMarks() { assertEquals(new BigDecimal("30.00"), service.calculateFinalMarks(new BigDecimal("100"), BigDecimal.ZERO)); }
    @Test void calculatesZeroMarks() { assertEquals(new BigDecimal("0.00"), service.calculateFinalMarks(BigDecimal.ZERO, BigDecimal.ZERO)); }
    @Test void calculatesMixedMarks() {
        assertEquals(new BigDecimal("65.00"), service.calculateFinalMarks(new BigDecimal("100"), new BigDecimal("50")));
        assertEquals(new BigDecimal("38.00"), service.calculateFinalMarks(new BigDecimal("10"), new BigDecimal("50")));
    }
    @Test void totalsAndAveragesFourSubjects() {
        BigDecimal total = service.calculateFinalMarks(new BigDecimal("100"), new BigDecimal("100"))
                .add(service.calculateFinalMarks(new BigDecimal("50"), new BigDecimal("50")))
                .add(service.calculateFinalMarks(BigDecimal.ZERO, new BigDecimal("100")))
                .add(service.calculateFinalMarks(new BigDecimal("100"), BigDecimal.ZERO));
        assertEquals(new BigDecimal("250.00"), total);
        assertEquals(new BigDecimal("62.50"), total.divide(BigDecimal.valueOf(4)));
    }
}
