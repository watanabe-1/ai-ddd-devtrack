package io.github.example.devtrack.certification.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public class ExamAttempt {
    private final ExamAttemptId id;
    private final LocalDate examDate;
    private final ExamResult result;

    private ExamAttempt(ExamAttemptId id, LocalDate examDate, ExamResult result) {
        this.id = Objects.requireNonNull(id, "exam attempt id is required");
        this.examDate = Objects.requireNonNull(examDate, "exam date is required");
        this.result = Objects.requireNonNull(result, "exam result is required");
    }

    public static ExamAttempt record(LocalDate examDate, ExamResult result) {
        return new ExamAttempt(ExamAttemptId.newId(), examDate, result);
    }

    public static ExamAttempt reconstruct(ExamAttemptId id, LocalDate examDate, ExamResult result) {
        return new ExamAttempt(id, examDate, result);
    }

    public ExamAttemptId id() {
        return id;
    }

    public LocalDate examDate() {
        return examDate;
    }

    public ExamResult result() {
        return result;
    }
}

