package io.github.example.devtrack.certification.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Certification {
    private final CertificationId id;
    private QualificationName qualificationName;
    private LearningGoalId relatedLearningGoalId;
    private ExamPlan examPlan;
    private CertificationStatus status;
    private final List<ExamAttempt> attempts;

    private Certification(
            CertificationId id,
            QualificationName qualificationName,
            LearningGoalId relatedLearningGoalId,
            ExamPlan examPlan,
            CertificationStatus status,
            List<ExamAttempt> attempts) {
        this.id = Objects.requireNonNull(id, "certification id is required");
        this.qualificationName = Objects.requireNonNull(qualificationName, "qualification name is required");
        this.relatedLearningGoalId = relatedLearningGoalId;
        this.examPlan = examPlan;
        this.status = Objects.requireNonNull(status, "certification status is required");
        this.attempts = new ArrayList<>(Objects.requireNonNull(attempts, "attempts are required"));
    }

    public static Certification register(QualificationName qualificationName) {
        return new Certification(
                CertificationId.newId(),
                qualificationName,
                null,
                null,
                CertificationStatus.PREPARING,
                List.of());
    }

    public static Certification reconstruct(
            CertificationId id,
            QualificationName qualificationName,
            LearningGoalId relatedLearningGoalId,
            ExamPlan examPlan,
            CertificationStatus status,
            List<ExamAttempt> attempts) {
        return new Certification(id, qualificationName, relatedLearningGoalId, examPlan, status, attempts);
    }

    public void relateToLearningGoal(LearningGoalId learningGoalId) {
        this.relatedLearningGoalId = Objects.requireNonNull(learningGoalId, "learning goal id is required");
    }

    public void scheduleExam(ExamPlan examPlan) {
        if (status == CertificationStatus.PASSED || status == CertificationStatus.RETIRED) {
            throw new IllegalStateException("passed or retired certification cannot be scheduled");
        }
        this.examPlan = Objects.requireNonNull(examPlan, "exam plan is required");
        this.status = CertificationStatus.SCHEDULED;
    }

    public void recordExamResult(ExamAttempt attempt) {
        attempts.add(Objects.requireNonNull(attempt, "exam attempt is required"));
        status = switch (attempt.result().outcome()) {
            case PASSED -> CertificationStatus.PASSED;
            case FAILED, ABSENT -> CertificationStatus.FAILED;
        };
        examPlan = null;
    }

    public boolean isPassed() {
        return status == CertificationStatus.PASSED;
    }

    public CertificationId id() {
        return id;
    }

    public QualificationName qualificationName() {
        return qualificationName;
    }

    public Optional<LearningGoalId> relatedLearningGoalId() {
        return Optional.ofNullable(relatedLearningGoalId);
    }

    public Optional<ExamPlan> examPlan() {
        return Optional.ofNullable(examPlan);
    }

    public CertificationStatus status() {
        return status;
    }

    public List<ExamAttempt> attempts() {
        return List.copyOf(attempts);
    }
}

