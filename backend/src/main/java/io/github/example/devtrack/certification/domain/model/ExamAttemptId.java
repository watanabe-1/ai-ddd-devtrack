package io.github.example.devtrack.certification.domain.model;

import java.util.Objects;
import java.util.UUID;

public record ExamAttemptId(UUID value) {
  public ExamAttemptId {
    Objects.requireNonNull(value, "exam attempt id is required");
  }

  public static ExamAttemptId newId() {
    return new ExamAttemptId(UUID.randomUUID());
  }
}
