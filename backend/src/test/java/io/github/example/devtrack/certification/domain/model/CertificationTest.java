package io.github.example.devtrack.certification.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CertificationTest {
  @Test
  void scheduleExamChangesStatusToScheduled() {
    Certification certification =
        Certification.register(new QualificationName("AWS Certified Developer"));

    certification.scheduleExam(new ExamPlan(LocalDate.of(2026, 10, 1)));

    assertEquals(CertificationStatus.SCHEDULED, certification.status());
  }

  @Test
  void passingAttemptChangesStatusToPassedAndClearsPlan() {
    Certification certification =
        Certification.register(new QualificationName("AWS Certified Developer"));
    certification.scheduleExam(new ExamPlan(LocalDate.of(2026, 10, 1)));

    certification.recordExamResult(
        ExamAttempt.record(
            LocalDate.of(2026, 10, 1),
            new ExamResult(LocalDate.of(2026, 10, 2), ExamOutcome.PASSED, "")));

    assertEquals(CertificationStatus.PASSED, certification.status());
    assertFalse(certification.examPlan().isPresent());
  }

  @Test
  void passedCertificationCannotBeScheduledAgain() {
    Certification certification =
        Certification.register(new QualificationName("AWS Certified Developer"));
    certification.recordExamResult(
        ExamAttempt.record(
            LocalDate.of(2026, 10, 1),
            new ExamResult(LocalDate.of(2026, 10, 2), ExamOutcome.PASSED, "")));

    assertThrows(
        IllegalStateException.class,
        () -> certification.scheduleExam(new ExamPlan(LocalDate.of(2026, 11, 1))));
  }
}
