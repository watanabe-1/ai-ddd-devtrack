package io.github.example.devtrack.learning.application;

import java.time.LocalDate;
import java.util.UUID;

public record RecordStudySessionCommand(
        UUID learningGoalId,
        LocalDate studyDate,
        int durationMinutes,
        String content,
        String note) {}

