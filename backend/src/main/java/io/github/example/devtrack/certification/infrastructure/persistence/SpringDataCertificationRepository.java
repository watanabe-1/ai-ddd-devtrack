package io.github.example.devtrack.certification.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCertificationRepository extends JpaRepository<CertificationJpaEntity, UUID> {
  List<CertificationJpaEntity> findByPlannedExamDateGreaterThanEqualOrderByPlannedExamDateAsc(
      LocalDate referenceDate);
}
