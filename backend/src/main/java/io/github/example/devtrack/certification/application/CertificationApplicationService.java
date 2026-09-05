package io.github.example.devtrack.certification.application;

import io.github.example.devtrack.certification.domain.model.Certification;
import io.github.example.devtrack.certification.domain.model.CertificationId;
import io.github.example.devtrack.certification.domain.model.ExamAttempt;
import io.github.example.devtrack.certification.domain.model.ExamOutcome;
import io.github.example.devtrack.certification.domain.model.ExamPlan;
import io.github.example.devtrack.certification.domain.model.ExamResult;
import io.github.example.devtrack.certification.domain.model.LearningGoalId;
import io.github.example.devtrack.certification.domain.model.QualificationName;
import io.github.example.devtrack.certification.domain.repository.CertificationRepository;
import io.github.example.devtrack.shared.application.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CertificationApplicationService {
    private final CertificationRepository certificationRepository;

    public CertificationApplicationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    public Certification registerCertification(RegisterCertificationCommand command) {
        Certification certification = Certification.register(new QualificationName(command.qualificationName()));
        if (command.relatedLearningGoalId() != null) {
            certification.relateToLearningGoal(new LearningGoalId(command.relatedLearningGoalId()));
        }
        return certificationRepository.save(certification);
    }

    public Certification scheduleExam(UUID id, ScheduleExamCommand command) {
        Certification certification = getCertification(id);
        certification.scheduleExam(new ExamPlan(command.plannedExamDate()));
        return certificationRepository.save(certification);
    }

    public Certification recordExamResult(UUID id, RecordExamResultCommand command) {
        Certification certification = getCertification(id);
        ExamAttempt attempt = ExamAttempt.record(
                command.examDate(),
                new ExamResult(command.resultDate(), command.outcome(), command.note()));
        certification.recordExamResult(attempt);
        return certificationRepository.save(certification);
    }

    @Transactional(readOnly = true)
    public Certification getCertification(UUID id) {
        return certificationRepository
                .findById(new CertificationId(id))
                .orElseThrow(() -> new ResourceNotFoundException("certification not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Certification> findAll() {
        return certificationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Certification> findUpcomingExams(LocalDate referenceDate) {
        return certificationRepository.findUpcomingExams(referenceDate);
    }
}

