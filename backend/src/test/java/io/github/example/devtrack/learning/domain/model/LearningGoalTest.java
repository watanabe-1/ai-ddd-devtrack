package io.github.example.devtrack.learning.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LearningGoalTest {
    @Test
    void createRejectsInvalidPeriod() {
        assertThrows(
                IllegalArgumentException.class,
                () -> LearningGoal.create(
                        new GoalTitle("DDD"),
                        new GoalDescription(""),
                        new GoalPeriod(LocalDate.of(2026, 9, 2), LocalDate.of(2026, 9, 1))));
    }

    @Test
    void completedGoalCannotBeChanged() {
        LearningGoal goal = LearningGoal.create(
                new GoalTitle("Spring Boot"),
                new GoalDescription("Learn basics"),
                new GoalPeriod(LocalDate.of(2026, 9, 1), null));

        goal.complete();

        assertEquals(GoalStatus.COMPLETED, goal.status());
        assertThrows(IllegalStateException.class, () -> goal.changeTitle(new GoalTitle("Spring Boot Advanced")));
    }
}

