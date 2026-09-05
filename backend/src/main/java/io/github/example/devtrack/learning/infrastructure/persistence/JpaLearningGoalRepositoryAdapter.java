package io.github.example.devtrack.learning.infrastructure.persistence;

import io.github.example.devtrack.learning.domain.model.GoalStatus;
import io.github.example.devtrack.learning.domain.model.LearningGoal;
import io.github.example.devtrack.learning.domain.model.LearningGoalId;
import io.github.example.devtrack.learning.domain.repository.LearningGoalRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaLearningGoalRepositoryAdapter implements LearningGoalRepository {
  private final SpringDataLearningGoalRepository repository;
  private final LearningGoalMapper mapper = new LearningGoalMapper();

  JpaLearningGoalRepositoryAdapter(SpringDataLearningGoalRepository repository) {
    this.repository = repository;
  }

  @Override
  public LearningGoal save(LearningGoal goal) {
    LearningGoalJpaEntity existing = repository.findById(goal.id().value()).orElse(null);
    return mapper.toDomain(repository.save(mapper.toEntity(goal, existing)));
  }

  @Override
  public Optional<LearningGoal> findById(LearningGoalId id) {
    return repository.findById(id.value()).map(mapper::toDomain);
  }

  @Override
  public List<LearningGoal> findActive() {
    return repository.findByStatus(GoalStatus.ACTIVE).stream().map(mapper::toDomain).toList();
  }
}
