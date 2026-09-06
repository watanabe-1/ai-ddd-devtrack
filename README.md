# ai-ddd-devtrack

AI coding agent and DDD sample project for managing technical learning goals, study sessions, and certification progress.

This repository contains a small Spring Boot backend and a simple React frontend.

## Design Source Of Truth

PlantUML files under `docs/plantuml/` are the source of truth.

Before implementing backend, database, API, or frontend code, review the relevant PlantUML files first.

## Current Scope

Implemented:

- Initial DDD design documentation and PlantUML diagrams
- Spring Boot REST API for Learning Goals, Study Sessions, Certifications, and Dashboard
- PostgreSQL schema managed by Flyway
- JPA adapters separated from pure Java domain models
- Focused domain unit tests
- Simple React/Vite UI for operating the API

Not implemented yet:

- Generated TypeScript API client from OpenAPI
- Full integration test coverage with Testcontainers
- Authentication or multi-user support

## Requirements

- Java 21 or later
- Docker
- aqua 2.62.3 or later
- Bun and Node.js managed by `aqua/aqua.yaml`

Install aqua-managed tools:

```bash
aqua install
bun install
```

If aqua warns about `aqua/aqua-policy.yaml`, review it and allow it locally:

```bash
aqua policy allow aqua/aqua-policy.yaml
```

## Generate Diff Prompt

Generate a prompt from the whole Git repository diff:

```bash
bun run diff2prompt
```

The output is written to `generated-prompt.txt`. The command includes staged,
unstaged, and untracked files by default, excluding build artifacts and local
tool output.

## Quality Checks

Run all configured formatter, linter, and type checks from the repository root:

```bash
bun run check
```

Apply supported formatting and autofixes:

```bash
bun run check:fix
```

TypeScript and frontend files are checked with `oxfmt` and `oxlint`. Java files
are formatted with Spotless using google-java-format and linted with Checkstyle.
Backend tests remain available separately:

```bash
bun run test
```

## GitHub Actions

Configured workflows:

- `CI`: frontend Bun checks and backend Gradle build/test on Ubuntu and Windows
- `autofix.ci`: applies supported formatter/autofix changes on pull requests
- `GHA static checks`: runs actionlint, zizmor, and ghalint against GitHub Actions
- `Label PRs`: applies labels from `.github/labeler.yml`
- `Auto Approve`: approves non-draft pull requests opened by the repository owner
- `Renovate`: updates aqua-managed tools

## Dependency Updates

Dependabot is configured in `.github/dependabot.yml` for:

- Root Bun workspace dependencies
- Frontend npm lockfile dependencies
- Backend Gradle dependencies and Gradle wrapper
- Docker Compose images
- GitHub Actions

Dependabot runs as a GitHub-native service and does not require a repository
secret for public dependencies.

Renovate is configured in `.github/renovate.json` only for aqua-managed tools,
matching the existing aqua comments in `aqua/aqua.yaml`.

## Run Locally

Start PostgreSQL:

```bash
docker compose up -d
```

Run the backend:

```bash
cd backend
./gradlew bootRun
```

On Windows PowerShell:

```powershell
cd backend
.\gradlew.bat bootRun
```

Run the frontend:

```bash
cd frontend
bun run dev
```

Open:

- Frontend: http://localhost:5173
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Swagger UI: http://localhost:8080/swagger-ui.html

## Backend Package

The current sample package is:

```text
io.github.example.devtrack
```

Replace `example` with the target GitHub user or organization before publishing if desired.
