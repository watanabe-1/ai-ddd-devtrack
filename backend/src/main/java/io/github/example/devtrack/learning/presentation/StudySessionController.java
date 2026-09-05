package io.github.example.devtrack.learning.presentation;

import io.github.example.devtrack.learning.application.LearningApplicationService;
import io.github.example.devtrack.learning.application.RecordStudySessionCommand;
import io.github.example.devtrack.learning.application.ReviseStudySessionCommand;
import io.github.example.devtrack.learning.domain.model.StudySession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study-sessions")
class StudySessionController {
    private final LearningApplicationService service;

    StudySessionController(LearningApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    StudySessionResponse record(@Valid @RequestBody RecordStudySessionRequest request) {
        return StudySessionResponse.from(service.recordStudySession(new RecordStudySessionCommand(
                request.learningGoalId(),
                request.studyDate(),
                request.durationMinutes(),
                request.content(),
                request.note())));
    }

    @PutMapping("/{id}")
    StudySessionResponse revise(@PathVariable UUID id, @Valid @RequestBody ReviseStudySessionRequest request) {
        return StudySessionResponse.from(service.reviseStudySession(
                id,
                new ReviseStudySessionCommand(
                        request.studyDate(), request.durationMinutes(), request.content(), request.note())));
    }

    @GetMapping("/learning-goals/{learningGoalId}")
    List<StudySessionResponse> history(@PathVariable UUID learningGoalId) {
        return service.findStudyHistory(learningGoalId).stream().map(StudySessionResponse::from).toList();
    }

    @GetMapping("/recent")
    List<StudySessionResponse> recent() {
        return service.findRecentStudySessions(5).stream().map(StudySessionResponse::from).toList();
    }

    @GetMapping("/learning-goals/{learningGoalId}/total-minutes")
    StudyTimeResponse total(@PathVariable UUID learningGoalId) {
        return new StudyTimeResponse(service.calculateTotalStudyTime(learningGoalId).minutes());
    }

    record RecordStudySessionRequest(
            @NotNull UUID learningGoalId,
            @NotNull LocalDate studyDate,
            @Min(1) int durationMinutes,
            @NotBlank String content,
            String note) {}

    record ReviseStudySessionRequest(
            @NotNull LocalDate studyDate,
            @Min(1) int durationMinutes,
            @NotBlank String content,
            String note) {}

    record StudySessionResponse(
            UUID id,
            UUID learningGoalId,
            LocalDate studyDate,
            int durationMinutes,
            String content,
            String note) {
        static StudySessionResponse from(StudySession session) {
            return new StudySessionResponse(
                    session.id().value(),
                    session.learningGoalId().value(),
                    session.studyDate(),
                    session.duration().minutes(),
                    session.content().content(),
                    session.content().note());
        }
    }

    record StudyTimeResponse(int minutes) {}
}

