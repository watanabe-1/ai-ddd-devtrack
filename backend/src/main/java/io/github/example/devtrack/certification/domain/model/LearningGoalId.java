package io.github.example.devtrack.certification.domain.model;

import java.util.Objects;
import java.util.UUID;

public record LearningGoalId(UUID value) {
    public LearningGoalId {
        Objects.requireNonNull(value, "related learning goal id is required");
    }
}

