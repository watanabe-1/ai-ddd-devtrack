package io.github.example.devtrack.learning.presentation;

import io.github.example.devtrack.learning.application.ChangeLearningGoalCommand;
import io.github.example.devtrack.learning.application.CreateLearningGoalCommand;
import io.github.example.devtrack.learning.application.LearningApplicationService;
import io.github.example.devtrack.learning.domain.model.LearningGoal;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learning-goals")
class LearningGoalController {
    private final LearningApplicationService service;

    LearningGoalController(LearningApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    LearningGoalResponse create(@Valid @RequestBody LearningGoalRequest request) {
        return LearningGoalResponse.from(service.createGoal(
                new CreateLearningGoalCommand(request.title(), request.description(), request.startDate(), request.targetDate())));
    }

    @PutMapping("/{id}")
    LearningGoalResponse change(@PathVariable UUID id, @Valid @RequestBody LearningGoalRequest request) {
        return LearningGoalResponse.from(service.changeGoal(
                id,
                new ChangeLearningGoalCommand(request.title(), request.description(), request.startDate(), request.targetDate())));
    }

    @PostMapping("/{id}/complete")
    LearningGoalResponse complete(@PathVariable UUID id) {
        return LearningGoalResponse.from(service.completeGoal(id));
    }

    @GetMapping("/{id}")
    LearningGoalResponse get(@PathVariable UUID id) {
        return LearningGoalResponse.from(service.getGoal(id));
    }

    @GetMapping("/active")
    List<LearningGoalResponse> active() {
        return service.findActiveGoals().stream().map(LearningGoalResponse::from).toList();
    }

    record LearningGoalRequest(
            @NotBlank String title,
            String description,
            @NotNull LocalDate startDate,
            LocalDate targetDate) {}

    record LearningGoalResponse(
            UUID id,
            String title,
            String description,
            LocalDate startDate,
            LocalDate targetDate,
            String status) {
        static LearningGoalResponse from(LearningGoal goal) {
            return new LearningGoalResponse(
                    goal.id().value(),
                    goal.title().value(),
                    goal.description().value(),
                    goal.period().startDate(),
                    goal.period().targetDate(),
                    goal.status().name());
        }
    }
}

