package io.github.example.devtrack.learning.domain.model;

import java.util.Objects;
import java.util.UUID;

public record StudySessionId(UUID value) {
    public StudySessionId {
        Objects.requireNonNull(value, "study session id is required");
    }

    public static StudySessionId newId() {
        return new StudySessionId(UUID.randomUUID());
    }
}

