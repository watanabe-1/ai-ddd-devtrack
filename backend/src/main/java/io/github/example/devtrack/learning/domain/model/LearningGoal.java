package io.github.example.devtrack.learning.domain.model;

import java.util.Objects;

public class LearningGoal {
  private final LearningGoalId id;
  private GoalTitle title;
  private GoalDescription description;
  private GoalPeriod period;
  private GoalStatus status;

  private LearningGoal(
      LearningGoalId id,
      GoalTitle title,
      GoalDescription description,
      GoalPeriod period,
      GoalStatus status) {
    this.id = Objects.requireNonNull(id, "id is required");
    this.title = Objects.requireNonNull(title, "title is required");
    this.description = Objects.requireNonNull(description, "description is required");
    this.period = Objects.requireNonNull(period, "period is required");
    this.status = Objects.requireNonNull(status, "status is required");
  }

  public static LearningGoal create(
      GoalTitle title, GoalDescription description, GoalPeriod period) {
    return new LearningGoal(LearningGoalId.newId(), title, description, period, GoalStatus.ACTIVE);
  }

  public static LearningGoal reconstruct(
      LearningGoalId id,
      GoalTitle title,
      GoalDescription description,
      GoalPeriod period,
      GoalStatus status) {
    return new LearningGoal(id, title, description, period, status);
  }

  public void changeTitle(GoalTitle title) {
    ensureNotCompleted();
    this.title = Objects.requireNonNull(title, "title is required");
  }

  public void changeDescription(GoalDescription description) {
    ensureNotCompleted();
    this.description = Objects.requireNonNull(description, "description is required");
  }

  public void changePeriod(GoalPeriod period) {
    ensureNotCompleted();
    this.period = Objects.requireNonNull(period, "period is required");
  }

  public void complete() {
    if (status == GoalStatus.ARCHIVED) {
      throw new IllegalStateException("archived goal cannot be completed");
    }
    status = GoalStatus.COMPLETED;
  }

  public boolean isActive() {
    return status == GoalStatus.ACTIVE;
  }

  private void ensureNotCompleted() {
    if (status == GoalStatus.COMPLETED) {
      throw new IllegalStateException("completed goal cannot be changed");
    }
  }

  public LearningGoalId id() {
    return id;
  }

  public GoalTitle title() {
    return title;
  }

  public GoalDescription description() {
    return description;
  }

  public GoalPeriod period() {
    return period;
  }

  public GoalStatus status() {
    return status;
  }
}
