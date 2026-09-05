package io.github.example.devtrack.learning.domain.model;

import java.util.Objects;
import java.util.UUID;

public record LearningGoalId(UUID value) {
    public LearningGoalId {
        Objects.requireNonNull(value, "learning goal id is required");
    }

    public static LearningGoalId newId() {
        return new LearningGoalId(UUID.randomUUID());
    }
}

