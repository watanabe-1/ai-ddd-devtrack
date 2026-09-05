package io.github.example.devtrack.learning.application;

import java.time.LocalDate;

public record ChangeLearningGoalCommand(
        String title,
        String description,
        LocalDate startDate,
        LocalDate targetDate) {}

