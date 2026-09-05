package io.github.example.devtrack.learning.domain.model;

public record GoalTitle(String value) {
  public GoalTitle {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("goal title must not be blank");
    }
    if (value.length() > 200) {
      throw new IllegalArgumentException("goal title must be 200 characters or less");
    }
  }
}
