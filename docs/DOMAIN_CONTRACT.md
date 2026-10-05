# Domain Contract

This document defines the shared boundary between the Course/Lesson domain
and the Enrollment/Progress domain.

Changes to these contracts must be discussed by both team members before implementation.

## Lesson

Every lesson has:

- id
- title
- optional prerequisite

Required methods:

`boolean isComplete(CompletionEvidence evidence)`

`Lesson getPrerequisite()`

`void setPrerequisite(Lesson prerequisite)`

Lesson subclasses are responsible for deciding whether their own completion evidence satisfies the completion rule.

Progress must not inspect concrete Lesson types.

## Course

Course contains an ordered list of Lesson objects.

Required methods:

`void addLesson(Lesson lesson)`

`List<Lesson> getLessons()`

The lesson list exposed outside Course must not allow external modification.

## Progress

Progress belongs to one Course context.

Constructor:

`Progress(Course course)`

Required methods:

`boolean isLessonUnlocked(Lesson lesson)`

Returns true when the lesson has no prerequisite or its prerequisite has already been completed.

`boolean updateLessonProgress(Lesson lesson, CompletionEvidence evidence)`

Behavior:

1. Verify that the lesson belongs to the Course.
2. Verify that the lesson is unlocked.
3. Call `lesson.isComplete(evidence)`.
4. If the lesson is complete, record it in completed lessons.
5. Do not allow external code to modify completed lessons directly.
6. Return true when completion is successfully recorded.
7. Return false when the attempt is valid but does not satisfy the lesson completion rule.

Locked lessons or lessons outside the Course must be rejected.

`double getOverallPercentage()`

Percentage is calculated from completed lessons divided by total lessons.

`boolean isCertificateEligible()`

Returns true only when all Course lessons are completed.

## Integration Rule

The Course/Lesson side must not depend on Progress.

Progress may depend on Course, Lesson and CompletionEvidence.

This dependency direction must be preserved to reduce coupling and merge conflicts.