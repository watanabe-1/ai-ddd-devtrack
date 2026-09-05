# Overview

`ai-ddd-devtrack` manages technical learning and certification preparation.

The application has two goals:

- Be useful as a personal learning and certification tracker.
- Be publishable as a public sample of AI-assisted development with DDD and Spring Boot.

PlantUML is the design source of truth. This Markdown document explains the current design decisions for human review and must be kept consistent with the PlantUML files.

# Requirements

The initial functional scope is:

- Create, update, complete, and query active Learning Goals.
- Record and revise Study Sessions.
- Browse study history.
- Aggregate study time.
- Register Certifications.
- Set exam schedules.
- Record exam results.
- Check certification status.

The initial non-functional and architectural constraints are:

- Backend uses Java 21 or later, Spring Boot, Gradle, PostgreSQL, Spring Data JPA, Flyway, Bean Validation, JUnit 5, Testcontainers, and OpenAPI.
- Frontend uses React, TypeScript, Vite, React Router, and TanStack Query.
- Domain models should remain pure Java.
- JPA entities must be separated from domain models.
- Repository interfaces belong to the domain side; implementations belong to infrastructure.
- Controllers must not contain business logic.
- Public repository safety must be maintained.

# Ubiquitous Language

| Term                 | Meaning                                                                                                |
| -------------------- | ------------------------------------------------------------------------------------------------------ |
| Learning Goal        | A technical learning objective such as learning Spring Boot, DDD, AWS, or GitHub Actions.              |
| Goal Status          | The lifecycle state of a Learning Goal: planned, active, completed, or archived.                       |
| Study Session        | A record of actual learning activity performed for a Learning Goal.                                    |
| Study Date           | The date on which a Study Session was performed.                                                       |
| Study Duration       | The amount of time spent in a Study Session.                                                           |
| Study Content        | The concrete topic or activity studied during a Study Session.                                         |
| Certification        | A managed qualification or certification target.                                                       |
| Exam Plan            | A planned exam date for a Certification.                                                               |
| Exam Attempt         | A single exam-taking event for a Certification.                                                        |
| Exam Result          | The outcome of an Exam Attempt, such as passed, failed, or absent.                                     |
| Certification Status | The lifecycle state of a Certification: considering, preparing, scheduled, passed, failed, or retired. |

# Bounded Context

The initial design uses two bounded contexts:

- `learning`: Manages Learning Goals and Study Sessions.
- `certification`: Manages Certifications, exam schedules, attempts, and results.

Learning and certification are closely related in the product experience, but they have different lifecycle rules. A Learning Goal tracks study progress and completion. A Certification tracks exam intent, scheduled exams, attempts, and results. Keeping them as separate contexts avoids forcing exam lifecycle rules into the learning model.

# Domain Model

The learning context contains:

- `LearningGoal` as an Aggregate Root.
- `StudySession` as an independent Aggregate Root.
- Value Objects such as `LearningGoalId`, `StudySessionId`, `GoalPeriod`, `StudyDuration`, and `StudySessionContent`.

The certification context contains:

- `Certification` as an Aggregate Root.
- `ExamAttempt` as an Entity inside `Certification`.
- Value Objects such as `CertificationId`, `QualificationName`, `ExamPlan`, and `ExamResult`.

# Aggregate

## LearningGoal

`LearningGoal` owns goal identity, title, description, target period, and lifecycle status.

It does not own `StudySession` records. A goal may have many sessions over time, and loading or changing the goal should not require loading the entire study history.

Repository save unit:

- `LearningGoal`

## StudySession

`StudySession` is an independent Aggregate Root linked to a `LearningGoalId`.

It records actual study work. It may be created and corrected independently from the goal lifecycle, while still enforcing local rules such as positive duration and non-empty content.

Repository save unit:

- `StudySession`

## Certification

`Certification` owns qualification name, certification lifecycle status, planned exam date, and exam attempts.

Exam attempts are modeled inside `Certification` because attempts are meaningful only within a certification target and usually have a small bounded history.

Repository save unit:

- `Certification`, including its `ExamAttempt` children.

# Entities

| Entity        | Aggregate     | Role                                             |
| ------------- | ------------- | ------------------------------------------------ |
| LearningGoal  | LearningGoal  | Tracks the lifecycle of a learning objective.    |
| StudySession  | StudySession  | Records one completed study activity.            |
| Certification | Certification | Tracks a certification target and exam progress. |
| ExamAttempt   | Certification | Records one exam-taking event and result.        |

# Value Objects

| Value Object        | Purpose                                    |
| ------------------- | ------------------------------------------ |
| LearningGoalId      | Identity for LearningGoal.                 |
| StudySessionId      | Identity for StudySession.                 |
| CertificationId     | Identity for Certification.                |
| ExamAttemptId       | Identity for ExamAttempt.                  |
| GoalTitle           | Non-empty Learning Goal title.             |
| GoalPeriod          | Start date and optional target date.       |
| StudyDuration       | Positive study duration.                   |
| StudySessionContent | Non-empty study content and optional note. |
| QualificationName   | Non-empty certification name.              |
| ExamPlan            | Optional planned exam date.                |
| ExamResult          | Exam outcome and result date.              |

# Invariants

- Learning Goal title must not be blank.
- Learning Goal target date must not be before start date.
- Completed Learning Goals cannot be returned to active without an explicit future design change.
- Study Session must reference a Learning Goal by `LearningGoalId`.
- Study Session duration must be positive.
- Calculated study time may be zero when there are no matching Study Sessions.
- Study Session date must be present.
- Study Session content must not be blank.
- Certification qualification name must not be blank.
- Certification may have at most one current planned exam date.
- Exam Attempt result date must be present when recording a result.
- Exam Attempt belongs to exactly one Certification.
- Passing an Exam Attempt changes Certification status to passed.

# Aggregate Relations

`StudySession` references `LearningGoal` by identity only. This keeps the aggregate boundary small and avoids accidental modification of a goal while editing study history.

`Certification` may optionally reference a `LearningGoalId` when the certification is supported by a learning goal. The relationship is intentionally loose because not every certification needs a dedicated learning goal, and one learning goal may support multiple certifications.

# Main Use Cases

Learning use cases:

- Create Learning Goal.
- Change Learning Goal.
- Complete Learning Goal.
- Get active Learning Goals.
- Record Study Session.
- Revise Study Session.
- Browse study history.
- Calculate study time.

Certification use cases:

- Register Certification.
- Set exam schedule.
- Record exam result.
- Check Certification status.

Application services should be organized around coherent workflows, not one service class per use case.

# Persistence

Domain repositories:

- `LearningGoalRepository`
- `StudySessionRepository`
- `CertificationRepository`

Repository interfaces are placed in the domain package because they describe domain persistence needs. Spring Data JPA adapters are placed in infrastructure packages.

Domain models and JPA entities are separate. Infrastructure mappers convert between them.

# Backend Package

Current sample package:

```text
io.github.example.devtrack
```

Replace `example` with the target GitHub user or organization before publishing if desired.

Package structure:

```text
io.github.example.devtrack
+-- learning
|   +-- domain
|   |   +-- model
|   |   +-- repository
|   +-- application
|   +-- presentation
|   +-- infrastructure
|       +-- persistence
+-- certification
    +-- domain
    |   +-- model
    |   +-- repository
    +-- application
    +-- presentation
    +-- infrastructure
        +-- persistence
```

# Repository Directory

```text
ai-ddd-devtrack/
+-- backend/
|   +-- src/
|   |   +-- main/
|   |   +-- test/
|   +-- build.gradle
|   +-- settings.gradle
+-- frontend/
|   +-- src/
|   +-- package.json
|   +-- vite.config.ts
+-- docs/
|   +-- plantuml/
|   |   +-- architecture/
|   |   +-- domain/
|   |   +-- sequence/
|   +-- design/
+-- .github/
|   +-- workflows/
+-- AGENTS.md
+-- README.md
+-- compose.yaml
+-- .gitignore
+-- .env.example
```

The implementation keeps backend and frontend separate so the DDD backend can stay focused while the frontend remains a thin API client.

# PlantUML File Structure

```text
docs/plantuml/
+-- architecture/
|   +-- system-architecture.puml
+-- domain/
|   +-- learning-domain.puml
|   +-- certification-domain.puml
+-- sequence/
    +-- record-study-session.puml
    +-- record-exam-result.puml
```

# Design Decisions

## LearningGoal and StudySession are not the same Aggregate

`LearningGoal` and `StudySession` should not be the same aggregate.

Reason:

- A Learning Goal is changed infrequently.
- Study Sessions are added often.
- Study history can grow without a natural small upper bound.
- Loading a Learning Goal should not require loading every Study Session.
- A Study Session can enforce its own invariants without modifying the Learning Goal.

## StudySession is an independent Aggregate

`StudySession` is an independent aggregate because it has its own identity, lifecycle, correction use case, and persistence needs.

It references `LearningGoal` by `LearningGoalId`. Cross-aggregate rules, such as rejecting sessions for completed goals, should be handled by an application service after loading the goal and before saving the session.

## Certification is a separate Bounded Context

`Certification` belongs to the certification context, not the learning context.

Reason:

- Exam scheduling and exam attempts have a different language and lifecycle from daily learning.
- Certifications can exist without a Learning Goal.
- A Learning Goal can support more than one Certification.
- Keeping a loose reference avoids a large aggregate that mixes study and exam concerns.

## Study Time is calculated from StudySession

The initial design calculates total study time from `StudySession` records instead of storing it on `LearningGoal`.

Reason:

- Study Session is the source event-like record for actual study.
- Derived totals can be recalculated consistently.
- Avoids synchronization bugs between session edits and stored totals.

If performance becomes a problem later, a read model or cached summary can be introduced outside the core aggregate.

## ExamAttempt is an Entity inside Certification

`ExamAttempt` is an Entity inside `Certification`, not a separate Aggregate.

Reason:

- Exam attempts are meaningful only for one Certification.
- Attempt history is expected to be small.
- Recording a passing result must update Certification status in the same consistency boundary.
- There is no current use case for modifying an attempt independently from the Certification.

# Alternatives Considered

## Store total study time on LearningGoal

Rejected for the initial design because it duplicates information already represented by Study Sessions and creates update consistency concerns when sessions are edited.

## Put LearningGoal, StudySession, and Certification in one context

Rejected because certification exam lifecycle rules would pollute the learning model. A loose identity reference gives enough connection without coupling the models.

## Model every use case as a separate service

Rejected because it would produce mechanical application services. The application layer should group coherent workflows and delegate business rules to domain models.

## Add Domain Events now

Deferred because no current requirement needs asynchronous domain reactions. Domain events can be added when there is a concrete use case, such as generating notifications or updating read models.
