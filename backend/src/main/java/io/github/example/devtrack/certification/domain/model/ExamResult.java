package io.github.example.devtrack.certification.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public record ExamResult(LocalDate resultDate, ExamOutcome outcome, String note) {
  public ExamResult {
    Objects.requireNonNull(resultDate, "result date is required");
    Objects.requireNonNull(outcome, "exam outcome is required");
    note = note == null ? "" : note.strip();
  }
}
