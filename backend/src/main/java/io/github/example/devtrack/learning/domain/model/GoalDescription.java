package io.github.example.devtrack.learning.domain.model;

public record GoalDescription(String value) {
  public GoalDescription {
    value = value == null ? "" : value.strip();
  }
}
