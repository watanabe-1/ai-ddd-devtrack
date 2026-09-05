package io.github.example.devtrack.certification.application;

import java.time.LocalDate;

public record ScheduleExamCommand(LocalDate plannedExamDate) {}
