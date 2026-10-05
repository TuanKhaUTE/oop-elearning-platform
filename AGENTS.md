# AGENTS.md

## Project

This repository is an Object-Oriented Programming final project implementing a Java console-based E-Learning Platform.

The primary goal is to demonstrate clear and justified OOP design rather than building a large web application.

Use Java 17.

Do not introduce Spring Boot, databases, frontend frameworks, external APIs, or unnecessary architecture unless explicitly requested.

## Required OOP Design

The required core domain includes:

- Course
- abstract Lesson
- VideoLesson
- TextLesson
- QuizLesson
- Student
- Instructor
- Enrollment
- Progress

Course contains an ordered collection of Lessons.

Lesson completion must use polymorphism through:

`Lesson.isComplete(CompletionEvidence evidence)`

Progress must call this method uniformly and must not contain type-switching logic for VideoLesson, TextLesson, or QuizLesson.

Progress owns and guards lesson completion state.

Lesson prerequisites must be checked before Progress records a lesson as completed.

Certificate eligibility is true only when every lesson in the enrolled Course is completed.

## Shared Contracts

Do not change these public method contracts without explicit team approval:

Lesson:
- isComplete(CompletionEvidence evidence)
- getPrerequisite()
- setPrerequisite(Lesson prerequisite)

Course:
- addLesson(Lesson lesson)
- getLessons()

Progress:
- Progress(Course course)
- isLessonUnlocked(Lesson lesson)
- updateLessonProgress(Lesson lesson, CompletionEvidence evidence)
- getOverallPercentage()
- isCertificateEligible()

Refer to `docs/DOMAIN_CONTRACT.md` for behavior details.

## Code Ownership

Kha owns initial implementation of:

- course/
- lesson/
- Instructor

Trang owns initial implementation of:

- Student
- enrollment/
- progress/

Do not modify files owned by the other member unless the task explicitly requests integration or review changes.

## Development Rules

Work on feature branches.

Do not implement the entire project in one task.

Keep each change small and focused.

Before editing code:
1. inspect the relevant existing files;
2. check `docs/DOMAIN_CONTRACT.md`;
3. preserve existing public contracts.

After editing:
1. compile the affected code;
2. run relevant tests when available;
3. report the files changed;
4. explain the purpose of each change;
5. do not commit or push unless explicitly requested.

Prefer simple readable Java suitable for an OOP course.

Avoid God classes and unnecessary abstractions.

Do not add functionality not required by the assignment.

All generated code must remain understandable enough for both students to explain during oral defense.