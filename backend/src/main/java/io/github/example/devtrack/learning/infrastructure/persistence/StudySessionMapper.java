package io.github.example.devtrack.learning.infrastructure.persistence;

import io.github.example.devtrack.learning.domain.model.LearningGoalId;
import io.github.example.devtrack.learning.domain.model.StudyDuration;
import io.github.example.devtrack.learning.domain.model.StudySession;
import io.github.example.devtrack.learning.domain.model.StudySessionContent;
import io.github.example.devtrack.learning.domain.model.StudySessionId;
import java.time.Instant;

class StudySessionMapper {
    StudySession toDomain(StudySessionJpaEntity entity) {
        return StudySession.reconstruct(
                new StudySessionId(entity.id),
                new LearningGoalId(entity.learningGoalId),
                entity.studyDate,
                new StudyDuration(entity.durationMinutes),
                new StudySessionContent(entity.content, entity.note));
    }

    StudySessionJpaEntity toEntity(StudySession session, StudySessionJpaEntity existing) {
        StudySessionJpaEntity entity = existing == null ? new StudySessionJpaEntity() : existing;
        Instant now = Instant.now();
        entity.id = session.id().value();
        entity.learningGoalId = session.learningGoalId().value();
        entity.studyDate = session.studyDate();
        entity.durationMinutes = session.duration().minutes();
        entity.content = session.content().content();
        entity.note = session.content().note();
        entity.createdAt = entity.createdAt == null ? now : entity.createdAt;
        entity.updatedAt = now;
        return entity;
    }
}

