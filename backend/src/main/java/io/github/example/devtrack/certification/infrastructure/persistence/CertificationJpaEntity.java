package io.github.example.devtrack.certification.infrastructure.persistence;

import io.github.example.devtrack.certification.domain.model.CertificationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "certifications")
class CertificationJpaEntity {
    @Id
    UUID id;
    String qualificationName;
    UUID relatedLearningGoalId;
    LocalDate plannedExamDate;

    @Enumerated(EnumType.STRING)
    CertificationStatus status;

    Instant createdAt;
    Instant updatedAt;

    @OneToMany(mappedBy = "certification", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ExamAttemptJpaEntity> attempts = new ArrayList<>();
}

