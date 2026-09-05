# AGENTS.md

This repository treats PlantUML as the source of truth for design.

AI coding agents working in this repository must follow these rules:

1. Read the related PlantUML files before changing code.
2. Treat PlantUML as the source of truth for design.
3. If a design change is required, update PlantUML before changing Java code.
4. Do not implement behavior that ignores aggregate boundaries.
5. Do not validate domain invariants only in the application layer or controller layer.
6. Do not introduce Spring, JPA, HTTP, or database-specific dependencies into the domain layer.
7. Do not create unnecessary repositories, domain services, or interfaces.
8. After Java implementation, add or update unit tests and integration tests.
9. Update Markdown design documentation at the end of implementation work.
10. If PlantUML, Java, and Markdown conflict, use PlantUML as the baseline and report the conflict to the user.
11. Do not fill unclear design decisions with large unreviewed changes.
12. Always assume this repository may be public. Do not commit personal information, real study history, real exam results, passwords, API keys, tokens, `.env`, AWS credentials, or database credentials.

# Windows shell encoding

When running commands in PowerShell on Windows, always switch the console code page to UTF-8 before executing commands:

chcp 65001

When reading Japanese text files in PowerShell, explicitly pass UTF-8 to avoid mojibake:

Get-Content <path> -Encoding UTF8

If a Japanese file still looks garbled, inspect the file encoding before editing it. Try `-Encoding Default` only for legacy Shift_JIS files.
