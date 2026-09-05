package io.github.example.devtrack.learning.domain.repository;

import io.github.example.devtrack.learning.domain.model.LearningGoalId;
import io.github.example.devtrack.learning.domain.model.StudyDuration;
import io.github.example.devtrack.learning.domain.model.StudySession;
import io.github.example.devtrack.learning.domain.model.StudySessionId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudySessionRepository {
    StudySession save(StudySession session);

    Optional<StudySession> findById(StudySessionId id);

    List<StudySession> findByLearningGoalId(LearningGoalId learningGoalId);

    List<StudySession> findRecent(int limit);

    StudyDuration sumDurationByLearningGoalId(LearningGoalId learningGoalId);

    StudyDuration sumDurationBetween(LocalDate startDate, LocalDate endDate);
}

