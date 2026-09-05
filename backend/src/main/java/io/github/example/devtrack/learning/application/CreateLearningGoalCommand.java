package io.github.example.devtrack.learning.application;

import java.time.LocalDate;

public record CreateLearningGoalCommand(
    String title, String description, LocalDate startDate, LocalDate targetDate) {}
