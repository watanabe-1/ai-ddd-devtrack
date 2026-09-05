package io.github.example.devtrack.certification.presentation;

import io.github.example.devtrack.certification.application.CertificationApplicationService;
import io.github.example.devtrack.certification.application.RecordExamResultCommand;
import io.github.example.devtrack.certification.application.RegisterCertificationCommand;
import io.github.example.devtrack.certification.application.ScheduleExamCommand;
import io.github.example.devtrack.certification.domain.model.Certification;
import io.github.example.devtrack.certification.domain.model.ExamAttempt;
import io.github.example.devtrack.certification.domain.model.ExamOutcome;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certifications")
class CertificationController {
    private final CertificationApplicationService service;

    CertificationController(CertificationApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CertificationResponse register(@Valid @RequestBody RegisterCertificationRequest request) {
        return CertificationResponse.from(service.registerCertification(
                new RegisterCertificationCommand(request.qualificationName(), request.relatedLearningGoalId())));
    }

    @GetMapping
    List<CertificationResponse> all() {
        return service.findAll().stream().map(CertificationResponse::from).toList();
    }

    @GetMapping("/{id}")
    CertificationResponse get(@PathVariable UUID id) {
        return CertificationResponse.from(service.getCertification(id));
    }

    @GetMapping("/upcoming")
    List<CertificationResponse> upcoming() {
        return service.findUpcomingExams(LocalDate.now()).stream().map(CertificationResponse::from).toList();
    }

    @PostMapping("/{id}/exam-plan")
    CertificationResponse schedule(@PathVariable UUID id, @Valid @RequestBody ScheduleExamRequest request) {
        return CertificationResponse.from(service.scheduleExam(id, new ScheduleExamCommand(request.plannedExamDate())));
    }

    @PostMapping("/{id}/exam-results")
    CertificationResponse recordResult(@PathVariable UUID id, @Valid @RequestBody RecordExamResultRequest request) {
        return CertificationResponse.from(service.recordExamResult(
                id,
                new RecordExamResultCommand(
                        request.examDate(), request.resultDate(), request.outcome(), request.note())));
    }

    record RegisterCertificationRequest(@NotBlank String qualificationName, UUID relatedLearningGoalId) {}

    record ScheduleExamRequest(@NotNull LocalDate plannedExamDate) {}

    record RecordExamResultRequest(
            @NotNull LocalDate examDate,
            @NotNull LocalDate resultDate,
            @NotNull ExamOutcome outcome,
            String note) {}

    record CertificationResponse(
            UUID id,
            String qualificationName,
            UUID relatedLearningGoalId,
            LocalDate plannedExamDate,
            String status,
            List<ExamAttemptResponse> attempts) {
        static CertificationResponse from(Certification certification) {
            return new CertificationResponse(
                    certification.id().value(),
                    certification.qualificationName().value(),
                    certification.relatedLearningGoalId().map(id -> id.value()).orElse(null),
                    certification.examPlan().map(plan -> plan.plannedExamDate()).orElse(null),
                    certification.status().name(),
                    certification.attempts().stream().map(ExamAttemptResponse::from).toList());
        }
    }

    record ExamAttemptResponse(UUID id, LocalDate examDate, LocalDate resultDate, String outcome, String note) {
        static ExamAttemptResponse from(ExamAttempt attempt) {
            return new ExamAttemptResponse(
                    attempt.id().value(),
                    attempt.examDate(),
                    attempt.result().resultDate(),
                    attempt.result().outcome().name(),
                    attempt.result().note());
        }
    }
}

