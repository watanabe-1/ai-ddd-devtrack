package io.github.example.devtrack.certification.application;

import io.github.example.devtrack.certification.domain.model.ExamOutcome;
import java.time.LocalDate;

public record RecordExamResultCommand(
    LocalDate examDate, LocalDate resultDate, ExamOutcome outcome, String note) {}
