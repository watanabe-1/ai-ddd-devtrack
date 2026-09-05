package io.github.example.devtrack.certification.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public record ExamPlan(LocalDate plannedExamDate) {
  public ExamPlan {
    Objects.requireNonNull(plannedExamDate, "planned exam date is required");
  }
}
