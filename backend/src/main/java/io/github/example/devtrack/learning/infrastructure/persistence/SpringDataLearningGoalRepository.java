package io.github.example.devtrack.learning.infrastructure.persistence;

import io.github.example.devtrack.learning.domain.model.GoalStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataLearningGoalRepository extends JpaRepository<LearningGoalJpaEntity, UUID> {
  List<LearningGoalJpaEntity> findByStatus(GoalStatus status);
}
