package io.github.example.devtrack.certification.infrastructure.persistence;

import io.github.example.devtrack.certification.domain.model.Certification;
import io.github.example.devtrack.certification.domain.model.CertificationId;
import io.github.example.devtrack.certification.domain.repository.CertificationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaCertificationRepositoryAdapter implements CertificationRepository {
    private final SpringDataCertificationRepository repository;
    private final CertificationMapper mapper = new CertificationMapper();

    JpaCertificationRepositoryAdapter(SpringDataCertificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Certification save(Certification certification) {
        CertificationJpaEntity existing = repository.findById(certification.id().value()).orElse(null);
        return mapper.toDomain(repository.save(mapper.toEntity(certification, existing)));
    }

    @Override
    public Optional<Certification> findById(CertificationId id) {
        return repository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Certification> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Certification> findUpcomingExams(LocalDate referenceDate) {
        return repository.findByPlannedExamDateGreaterThanEqualOrderByPlannedExamDateAsc(referenceDate).stream()
                .map(mapper::toDomain)
                .toList();
    }
}

