package io.github.example.devtrack.learning.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public class StudySession {
  private final StudySessionId id;
  private final LearningGoalId learningGoalId;
  private LocalDate studyDate;
  private StudyDuration duration;
  private StudySessionContent content;

  private StudySession(
      StudySessionId id,
      LearningGoalId learningGoalId,
      LocalDate studyDate,
      StudyDuration duration,
      StudySessionContent content) {
    this.id = Objects.requireNonNull(id, "id is required");
    this.learningGoalId = Objects.requireNonNull(learningGoalId, "learning goal id is required");
    this.studyDate = Objects.requireNonNull(studyDate, "study date is required");
    this.duration = Objects.requireNonNull(duration, "duration is required");
    if (!duration.isPositive()) {
      throw new IllegalArgumentException("study session duration must be positive");
    }
    this.content = Objects.requireNonNull(content, "content is required");
  }

  public static StudySession record(
      LearningGoalId learningGoalId,
      LocalDate studyDate,
      StudyDuration duration,
      StudySessionContent content) {
    return new StudySession(StudySessionId.newId(), learningGoalId, studyDate, duration, content);
  }

  public static StudySession reconstruct(
      StudySessionId id,
      LearningGoalId learningGoalId,
      LocalDate studyDate,
      StudyDuration duration,
      StudySessionContent content) {
    return new StudySession(id, learningGoalId, studyDate, duration, content);
  }

  public void revise(LocalDate studyDate, StudyDuration duration, StudySessionContent content) {
    this.studyDate = Objects.requireNonNull(studyDate, "study date is required");
    this.duration = Objects.requireNonNull(duration, "duration is required");
    if (!duration.isPositive()) {
      throw new IllegalArgumentException("study session duration must be positive");
    }
    this.content = Objects.requireNonNull(content, "content is required");
  }

  public StudySessionId id() {
    return id;
  }

  public LearningGoalId learningGoalId() {
    return learningGoalId;
  }

  public LocalDate studyDate() {
    return studyDate;
  }

  public StudyDuration duration() {
    return duration;
  }

  public StudySessionContent content() {
    return content;
  }
}
