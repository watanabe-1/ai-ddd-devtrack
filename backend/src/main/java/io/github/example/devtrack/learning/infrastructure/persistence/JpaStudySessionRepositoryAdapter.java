package io.github.example.devtrack.learning.infrastructure.persistence;

import io.github.example.devtrack.learning.domain.model.LearningGoalId;
import io.github.example.devtrack.learning.domain.model.StudyDuration;
import io.github.example.devtrack.learning.domain.model.StudySession;
import io.github.example.devtrack.learning.domain.model.StudySessionId;
import io.github.example.devtrack.learning.domain.repository.StudySessionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
class JpaStudySessionRepositoryAdapter implements StudySessionRepository {
    private final SpringDataStudySessionRepository repository;
    private final StudySessionMapper mapper = new StudySessionMapper();

    JpaStudySessionRepositoryAdapter(SpringDataStudySessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public StudySession save(StudySession session) {
        StudySessionJpaEntity existing = repository.findById(session.id().value()).orElse(null);
        return mapper.toDomain(repository.save(mapper.toEntity(session, existing)));
    }

    @Override
    public Optional<StudySession> findById(StudySessionId id) {
        return repository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<StudySession> findByLearningGoalId(LearningGoalId learningGoalId) {
        return repository.findByLearningGoalIdOrderByStudyDateDesc(learningGoalId.value()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<StudySession> findRecent(int limit) {
        return repository.findByOrderByStudyDateDesc(PageRequest.of(0, limit)).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public StudyDuration sumDurationByLearningGoalId(LearningGoalId learningGoalId) {
        return new StudyDuration(Math.toIntExact(repository.sumDurationByLearningGoalId(learningGoalId.value())));
    }

    @Override
    public StudyDuration sumDurationBetween(LocalDate startDate, LocalDate endDate) {
        return new StudyDuration(Math.toIntExact(repository.sumDurationBetween(startDate, endDate)));
    }
}
