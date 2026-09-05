package io.github.example.devtrack.certification.application;

import java.util.UUID;

public record RegisterCertificationCommand(String qualificationName, UUID relatedLearningGoalId) {}

