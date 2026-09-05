package io.github.example.devtrack.learning.domain.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class StudySessionTest {
    @Test
    void recordRejectsZeroDuration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> StudySession.record(
                        LearningGoalId.newId(),
                        LocalDate.of(2026, 9, 1),
                        new StudyDuration(0),
                        new StudySessionContent("Read docs", "")));
    }

    @Test
    void recordRejectsBlankContent() {
        assertThrows(
                IllegalArgumentException.class,
                () -> StudySession.record(
                        LearningGoalId.newId(),
                        LocalDate.of(2026, 9, 1),
                        new StudyDuration(30),
                        new StudySessionContent(" ", "")));
    }
}

