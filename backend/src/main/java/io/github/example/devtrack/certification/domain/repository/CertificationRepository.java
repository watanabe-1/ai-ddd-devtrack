package io.github.example.devtrack.certification.domain.repository;

import io.github.example.devtrack.certification.domain.model.Certification;
import io.github.example.devtrack.certification.domain.model.CertificationId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CertificationRepository {
    Certification save(Certification certification);

    Optional<Certification> findById(CertificationId id);

    List<Certification> findAll();

    List<Certification> findUpcomingExams(LocalDate referenceDate);
}

