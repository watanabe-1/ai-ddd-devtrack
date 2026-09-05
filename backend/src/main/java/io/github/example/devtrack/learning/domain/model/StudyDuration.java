package io.github.example.devtrack.learning.domain.model;

public record StudyDuration(int minutes) {
  public StudyDuration {
    if (minutes < 0) {
      throw new IllegalArgumentException("study duration must not be negative");
    }
  }

  public boolean isPositive() {
    return minutes > 0;
  }
}
