package io.github.example.devtrack.learning.domain.repository;

import io.github.example.devtrack.learning.domain.model.LearningGoal;
import io.github.example.devtrack.learning.domain.model.LearningGoalId;
import java.util.List;
import java.util.Optional;

public interface LearningGoalRepository {
    LearningGoal save(LearningGoal goal);

    Optional<LearningGoal> findById(LearningGoalId id);

    List<LearningGoal> findActive();
}

