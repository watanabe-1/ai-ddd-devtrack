package io.github.example.devtrack.learning.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "study_sessions")
class StudySessionJpaEntity {
  @Id UUID id;
  UUID learningGoalId;
  LocalDate studyDate;
  int durationMinutes;
  String content;
  String note;
  Instant createdAt;
  Instant updatedAt;
}
