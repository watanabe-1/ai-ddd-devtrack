package io.github.example.devtrack.certification.infrastructure.persistence;

import io.github.example.devtrack.certification.domain.model.ExamOutcome;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "exam_attempts")
class ExamAttemptJpaEntity {
  @Id UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "certification_id", nullable = false)
  CertificationJpaEntity certification;

  LocalDate examDate;
  LocalDate resultDate;

  @Enumerated(EnumType.STRING)
  ExamOutcome outcome;

  String note;
}
