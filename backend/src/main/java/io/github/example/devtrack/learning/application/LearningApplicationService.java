package io.github.example.devtrack.learning.application;

import io.github.example.devtrack.learning.domain.model.GoalDescription;
import io.github.example.devtrack.learning.domain.model.GoalPeriod;
import io.github.example.devtrack.learning.domain.model.GoalTitle;
import io.github.example.devtrack.learning.domain.model.LearningGoal;
import io.github.example.devtrack.learning.domain.model.LearningGoalId;
import io.github.example.devtrack.learning.domain.model.StudyDuration;
import io.github.example.devtrack.learning.domain.model.StudySession;
import io.github.example.devtrack.learning.domain.model.StudySessionContent;
import io.github.example.devtrack.learning.domain.model.StudySessionId;
import io.github.example.devtrack.learning.domain.repository.LearningGoalRepository;
import io.github.example.devtrack.learning.domain.repository.StudySessionRepository;
import io.github.example.devtrack.shared.application.ResourceNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LearningApplicationService {
  private final LearningGoalRepository goalRepository;
  private final StudySessionRepository sessionRepository;

  public LearningApplicationService(
      LearningGoalRepository goalRepository, StudySessionRepository sessionRepository) {
    this.goalRepository = goalRepository;
    this.sessionRepository = sessionRepository;
  }

  public LearningGoal createGoal(CreateLearningGoalCommand command) {
    LearningGoal goal =
        LearningGoal.create(
            new GoalTitle(command.title()),
            new GoalDescription(command.description()),
            new GoalPeriod(command.startDate(), command.targetDate()));
    return goalRepository.save(goal);
  }

  public LearningGoal changeGoal(UUID id, ChangeLearningGoalCommand command) {
    LearningGoal goal = getGoal(id);
    goal.changeTitle(new GoalTitle(command.title()));
    goal.changeDescription(new GoalDescription(command.description()));
    goal.changePeriod(new GoalPeriod(command.startDate(), command.targetDate()));
    return goalRepository.save(goal);
  }

  public LearningGoal completeGoal(UUID id) {
    LearningGoal goal = getGoal(id);
    goal.complete();
    return goalRepository.save(goal);
  }

  @Transactional(readOnly = true)
  public LearningGoal getGoal(UUID id) {
    return goalRepository
        .findById(new LearningGoalId(id))
        .orElseThrow(() -> new ResourceNotFoundException("learning goal not found: " + id));
  }

  @Transactional(readOnly = true)
  public List<LearningGoal> findActiveGoals() {
    return goalRepository.findActive();
  }

  public StudySession recordStudySession(RecordStudySessionCommand command) {
    LearningGoal goal = getGoal(command.learningGoalId());
    if (!goal.isActive()) {
      throw new IllegalStateException(
          "study session can be recorded only for active learning goals");
    }
    StudySession session =
        StudySession.record(
            goal.id(),
            command.studyDate(),
            new StudyDuration(command.durationMinutes()),
            new StudySessionContent(command.content(), command.note()));
    return sessionRepository.save(session);
  }

  public StudySession reviseStudySession(UUID id, ReviseStudySessionCommand command) {
    StudySession session =
        sessionRepository
            .findById(new StudySessionId(id))
            .orElseThrow(() -> new ResourceNotFoundException("study session not found: " + id));
    session.revise(
        command.studyDate(),
        new StudyDuration(command.durationMinutes()),
        new StudySessionContent(command.content(), command.note()));
    return sessionRepository.save(session);
  }

  @Transactional(readOnly = true)
  public List<StudySession> findStudyHistory(UUID learningGoalId) {
    return sessionRepository.findByLearningGoalId(new LearningGoalId(learningGoalId));
  }

  @Transactional(readOnly = true)
  public List<StudySession> findRecentStudySessions(int limit) {
    return sessionRepository.findRecent(limit);
  }

  @Transactional(readOnly = true)
  public StudyDuration calculateTotalStudyTime(UUID learningGoalId) {
    return sessionRepository.sumDurationByLearningGoalId(new LearningGoalId(learningGoalId));
  }

  @Transactional(readOnly = true)
  public StudyDuration calculateThisWeekStudyTime(LocalDate today) {
    LocalDate start = today.with(DayOfWeek.MONDAY);
    return sessionRepository.sumDurationBetween(start, today);
  }
}
