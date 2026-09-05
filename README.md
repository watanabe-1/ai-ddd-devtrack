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
- Node.js 22 or later

## Generate Diff Prompt

Generate a prompt from the whole Git repository diff:

```bash
npm run diff2prompt
```

The output is written to `generated-prompt.txt`. The command includes staged,
unstaged, and untracked files by default, excluding build artifacts and local
tool output.

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

Run the frontend:

```bash
cd frontend
npm install
npm run dev
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
