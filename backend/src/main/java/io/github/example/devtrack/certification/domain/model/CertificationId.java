package io.github.example.devtrack.certification.domain.model;

import java.util.Objects;
import java.util.UUID;

public record CertificationId(UUID value) {
    public CertificationId {
        Objects.requireNonNull(value, "certification id is required");
    }

    public static CertificationId newId() {
        return new CertificationId(UUID.randomUUID());
    }
}

