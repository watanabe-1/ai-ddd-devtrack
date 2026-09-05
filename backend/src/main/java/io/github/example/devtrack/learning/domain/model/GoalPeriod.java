package io.github.example.devtrack.learning.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public record GoalPeriod(LocalDate startDate, LocalDate targetDate) {
    public GoalPeriod {
        Objects.requireNonNull(startDate, "start date is required");
        if (targetDate != null && targetDate.isBefore(startDate)) {
            throw new IllegalArgumentException("target date must not be before start date");
        }
    }

    public Optional<LocalDate> targetDateOptional() {
        return Optional.ofNullable(targetDate);
    }
}

