package io.github.example.devtrack.learning.infrastructure.persistence;

import io.github.example.devtrack.learning.domain.model.GoalStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "learning_goals")
class LearningGoalJpaEntity {
  @Id UUID id;
  String title;
  String description;
  LocalDate startDate;
  LocalDate targetDate;

  @Enumerated(EnumType.STRING)
  GoalStatus status;

  Instant createdAt;
  Instant updatedAt;
}
