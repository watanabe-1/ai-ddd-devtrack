package io.github.example.devtrack.learning.infrastructure.persistence;

import io.github.example.devtrack.learning.domain.model.GoalDescription;
import io.github.example.devtrack.learning.domain.model.GoalPeriod;
import io.github.example.devtrack.learning.domain.model.GoalTitle;
import io.github.example.devtrack.learning.domain.model.LearningGoal;
import io.github.example.devtrack.learning.domain.model.LearningGoalId;
import java.time.Instant;

class LearningGoalMapper {
    LearningGoal toDomain(LearningGoalJpaEntity entity) {
        return LearningGoal.reconstruct(
                new LearningGoalId(entity.id),
                new GoalTitle(entity.title),
                new GoalDescription(entity.description),
                new GoalPeriod(entity.startDate, entity.targetDate),
                entity.status);
    }

    LearningGoalJpaEntity toEntity(LearningGoal goal, LearningGoalJpaEntity existing) {
        LearningGoalJpaEntity entity = existing == null ? new LearningGoalJpaEntity() : existing;
        Instant now = Instant.now();
        entity.id = goal.id().value();
        entity.title = goal.title().value();
        entity.description = goal.description().value();
        entity.startDate = goal.period().startDate();
        entity.targetDate = goal.period().targetDate();
        entity.status = goal.status();
        entity.createdAt = entity.createdAt == null ? now : entity.createdAt;
        entity.updatedAt = now;
        return entity;
    }
}

