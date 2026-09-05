package io.github.example.devtrack.learning.application;

import java.time.LocalDate;

public record ReviseStudySessionCommand(
        LocalDate studyDate,
        int durationMinutes,
        String content,
        String note) {}

