package io.github.example.devtrack.certification.domain.model;

public record QualificationName(String value) {
  public QualificationName {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("qualification name must not be blank");
    }
    if (value.length() > 200) {
      throw new IllegalArgumentException("qualification name must be 200 characters or less");
    }
    value = value.strip();
  }
}
