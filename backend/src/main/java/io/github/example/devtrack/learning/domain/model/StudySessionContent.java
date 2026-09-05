package io.github.example.devtrack.learning.domain.model;

public record StudySessionContent(String content, String note) {
    public StudySessionContent {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("study content must not be blank");
        }
        content = content.strip();
        note = note == null ? "" : note.strip();
    }
}

