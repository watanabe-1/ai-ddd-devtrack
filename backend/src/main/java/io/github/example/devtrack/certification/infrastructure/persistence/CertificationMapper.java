package io.github.example.devtrack.certification.infrastructure.persistence;

import io.github.example.devtrack.certification.domain.model.Certification;
import io.github.example.devtrack.certification.domain.model.CertificationId;
import io.github.example.devtrack.certification.domain.model.ExamAttempt;
import io.github.example.devtrack.certification.domain.model.ExamAttemptId;
import io.github.example.devtrack.certification.domain.model.ExamPlan;
import io.github.example.devtrack.certification.domain.model.ExamResult;
import io.github.example.devtrack.certification.domain.model.LearningGoalId;
import io.github.example.devtrack.certification.domain.model.QualificationName;
import java.time.Instant;

class CertificationMapper {
  Certification toDomain(CertificationJpaEntity entity) {
    return Certification.reconstruct(
        new CertificationId(entity.id),
        new QualificationName(entity.qualificationName),
        entity.relatedLearningGoalId == null
            ? null
            : new LearningGoalId(entity.relatedLearningGoalId),
        entity.plannedExamDate == null ? null : new ExamPlan(entity.plannedExamDate),
        entity.status,
        entity.attempts.stream().map(this::toDomain).toList());
  }

  CertificationJpaEntity toEntity(Certification certification, CertificationJpaEntity existing) {
    CertificationJpaEntity entity = existing == null ? new CertificationJpaEntity() : existing;
    Instant now = Instant.now();
    entity.id = certification.id().value();
    entity.qualificationName = certification.qualificationName().value();
    entity.relatedLearningGoalId =
        certification.relatedLearningGoalId().map(LearningGoalId::value).orElse(null);
    entity.plannedExamDate = certification.examPlan().map(ExamPlan::plannedExamDate).orElse(null);
    entity.status = certification.status();
    entity.createdAt = entity.createdAt == null ? now : entity.createdAt;
    entity.updatedAt = now;
    entity.attempts.clear();
    certification.attempts().forEach(attempt -> entity.attempts.add(toEntity(attempt, entity)));
    return entity;
  }

  private ExamAttempt toDomain(ExamAttemptJpaEntity entity) {
    return ExamAttempt.reconstruct(
        new ExamAttemptId(entity.id),
        entity.examDate,
        new ExamResult(entity.resultDate, entity.outcome, entity.note));
  }

  private ExamAttemptJpaEntity toEntity(ExamAttempt attempt, CertificationJpaEntity certification) {
    ExamAttemptJpaEntity entity = new ExamAttemptJpaEntity();
    entity.id = attempt.id().value();
    entity.certification = certification;
    entity.examDate = attempt.examDate();
    entity.resultDate = attempt.result().resultDate();
    entity.outcome = attempt.result().outcome();
    entity.note = attempt.result().note();
    return entity;
  }
}
