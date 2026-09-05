package io.github.example.devtrack.dashboard.presentation;

import io.github.example.devtrack.certification.application.CertificationApplicationService;
import io.github.example.devtrack.learning.application.LearningApplicationService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
class DashboardController {
    private final LearningApplicationService learningService;
    private final CertificationApplicationService certificationService;

    DashboardController(
            LearningApplicationService learningService,
            CertificationApplicationService certificationService) {
        this.learningService = learningService;
        this.certificationService = certificationService;
    }

    @GetMapping
    DashboardResponse dashboard() {
        LocalDate today = LocalDate.now();
        return new DashboardResponse(
                learningService.findActiveGoals().stream()
                        .map(goal -> new ActiveGoalItem(goal.id().value(), goal.title().value()))
                        .toList(),
                learningService.calculateThisWeekStudyTime(today).minutes(),
                learningService.findRecentStudySessions(5).stream()
                        .map(session -> new RecentStudySessionItem(
                                session.id().value(),
                                session.learningGoalId().value(),
                                session.studyDate(),
                                session.duration().minutes(),
                                session.content().content()))
                        .toList(),
                certificationService.findUpcomingExams(today).stream()
                        .map(certification -> new UpcomingExamItem(
                                certification.id().value(),
                                certification.qualificationName().value(),
                                certification.examPlan().map(plan -> plan.plannedExamDate()).orElse(null)))
                        .toList());
    }

    record DashboardResponse(
            List<ActiveGoalItem> activeGoals,
            int thisWeekStudyMinutes,
            List<RecentStudySessionItem> recentStudySessions,
            List<UpcomingExamItem> upcomingExams) {}

    record ActiveGoalItem(UUID id, String title) {}

    record RecentStudySessionItem(
            UUID id,
            UUID learningGoalId,
            LocalDate studyDate,
            int durationMinutes,
            String content) {}

    record UpcomingExamItem(UUID id, String qualificationName, LocalDate plannedExamDate) {}
}

