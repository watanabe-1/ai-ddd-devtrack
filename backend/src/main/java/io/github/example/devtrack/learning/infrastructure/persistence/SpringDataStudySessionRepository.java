package io.github.example.devtrack.learning.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface SpringDataStudySessionRepository extends JpaRepository<StudySessionJpaEntity, UUID> {
  List<StudySessionJpaEntity> findByLearningGoalIdOrderByStudyDateDesc(UUID learningGoalId);

  List<StudySessionJpaEntity> findByOrderByStudyDateDesc(Pageable pageable);

  @Query(
      "select coalesce(sum(s.durationMinutes), 0) from StudySessionJpaEntity s where s.learningGoalId = :learningGoalId")
  long sumDurationByLearningGoalId(@Param("learningGoalId") UUID learningGoalId);

  @Query(
      "select coalesce(sum(s.durationMinutes), 0) from StudySessionJpaEntity s where s.studyDate between :startDate and :endDate")
  long sumDurationBetween(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
