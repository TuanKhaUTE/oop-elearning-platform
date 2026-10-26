# E-Learning Platform

Final project for the **Object-Oriented Programming** course.

## Team

- Trần Tuấn Kha - 23110028
- Mai Trần Thùy Trang - 23110065

Lecturer: **Huỳnh Xuân Phụng**

## Project Overview

This project is a Java 17 E-Learning domain model for courses, enrollment, lesson completion, prerequisites, progress tracking, certificate eligibility, and file-based enrollment progress persistence.

The project focuses on applying OOP principles through a small domain model with explicit responsibilities and automated tests.

## Core Domain

### Lesson hierarchy

`Lesson` is the abstract base type. The final project contains four concrete variants:

- `VideoLesson` - completed when watched percentage reaches the configured threshold.
- `TextLesson` - completed when reading is confirmed.
- `QuizLesson` - completed when score reaches the configured minimum.
- `AssignmentLesson` - Week 4 extension; completed when submitted word count reaches the configured minimum.

Each variant receives its own `CompletionEvidence` subtype:

- `VideoEvidence`
- `TextEvidence`
- `QuizEvidence`
- `AssignmentEvidence`

### Enrollment and progress

A `Student` enrolls in a `Course` through `Enrollment`. Each Enrollment owns a `Progress` object.

`Progress`:

- checks that a lesson belongs to the Course;
- enforces prerequisite locking;
- delegates completion checks to `Lesson.isComplete(...)` polymorphically;
- tracks completed lessons;
- calculates overall percentage;
- determines certificate eligibility;
- restores validated historical completion from lesson IDs.

### Persistence

`EnrollmentFileStore` saves and loads a versioned UTF-8 snapshot containing:

- Student ID;
- Course ID;
- completed lesson count;
- completed lesson IDs.

Loaded state is validated through `Enrollment.restore(...)` and `Progress.restore(...)`. Percentage and certificate eligibility are recalculated instead of stored directly.

## OOP Principles

### Abstraction and Polymorphism

`Lesson.isComplete(CompletionEvidence)` defines the common completion contract. Each Lesson subtype overrides it with its own rule. `Progress` works through the `Lesson` abstraction and does not contain subtype-specific switch/if logic.

### Encapsulation

`Progress.completedLessons` is private. Completion state can only be changed through validated progress methods or the validated restoration path.

### Composition

`Course` contains an ordered `List<Lesson>`. `Enrollment` contains its `Progress`.

### Open/Closed Principle

Week 4 adds `AssignmentLesson` and `AssignmentEvidence` without modifying existing core production classes. The Assignment variant works with the existing `Progress`, `Enrollment`, and `EnrollmentFileStore` APIs.

## Technology

- Java 17 compilation target
- Maven
- JUnit 5
- Java NIO file I/O

## Project Structure

- `src/main/java/elearning/course` - Course domain
- `src/main/java/elearning/lesson` - Lesson hierarchy, evidence types, validation helper
- `src/main/java/elearning/user` - Student and Instructor
- `src/main/java/elearning/enrollment` - Enrollment and restore entry point
- `src/main/java/elearning/progress` - Progress, prerequisites, certificate rules, restoration
- `src/main/java/elearning/persistence` - Enrollment file persistence
- `src/test/java` - unit, edge-case, E2E, extensibility and persistence integration tests
- `docs/diagrams` - UML/class diagrams
- `docs/evidence` - weekly test/refactor/extensibility evidence
- `docs/reports` - weekly/final reports and portal content

## Build and Test

Requirements:

- JDK 17 or newer installed
- Maven available on `PATH`

From the repository root:

```bash
mvn clean test
```

Final verified suite:

```text
Tests run: 102
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

### Run the main E2E test

```bash
mvn -Dtest=elearning.integration.ELearningEndToEndTest test
```

### Run the Week 4 Assignment integration tests

```bash
mvn -Dtest=elearning.integration.AssignmentProgressIntegrationTest test
mvn -Dtest=elearning.persistence.AssignmentEnrollmentFileStoreIntegrationTest test
```

### Run the persistence demonstration

```bash
mvn -Dtest=elearning.persistence.EnrollmentPersistenceDemoTest test
```

## Final Evidence

- Final UML: `docs/diagrams/week4_final_class_diagram.png`
- Week 4 Kha extensibility test output: `docs/evidence/week-4/kha/kha_week4_extensibility_test_output.txt`
- Week 4 Open/Closed diff: `docs/evidence/week-4/kha/week4_extensibility_diff.txt`
- Week 4 Trang full Maven output: `docs/evidence/week-4/trang/W4_Full_Maven_Test_Output.txt`
- Final defense Q&A: `docs/evidence/week-4/defense_qna.md`
- Individual reflections: `docs/evidence/week-4/reflections.md`
- AI usage history: `AI_USAGE_LOG.md`

## Git Workflow and Ownership

Feature work was developed on separate branches and merged after review. Major Week 4 integration points:

- Kha: `feature/week4-assignment-lesson-kha` - Assignment variant, dedicated evidence type, invalid-input/extensibility tests.
- Trang: `feature/week4-extension-trang` - Progress and persistence integration tests plus final Maven evidence.

Both members are expected to understand the complete system for the final oral defense.
