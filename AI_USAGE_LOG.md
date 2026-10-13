# AI Usage Log

This file records substantial AI assistance used during the development of the project.

## Week 1 - Analysis and Design

### Entry 1 - Trần Tuấn Kha

Date: 04/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Analyze the E-Learning Platform project requirements.
- Identify candidate classes, required OOP principles, the main system flow, and a Week 1 test plan.

AI Output Summary:

- Suggested the main domain classes and Lesson hierarchy.
- Suggested the progress flow and initial test scenarios.
- Suggested a structure for the Week 1 report.

Verification and Modification:

- The suggestions were checked against the original project requirements.
- The team kept the Java console and domain-core scope.
- Web, database, and other unnecessary features were rejected because they were outside the required project scope.
- The verified ideas were reflected in the Week 1 system flow, class diagram, and report draft.

---

### Entry 2 - Trần Tuấn Kha

Date: 11/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Compare the supervisor feedback and assessment rubric with the Week 1 report and repository.
- Identify missing evidence.

AI Output Summary:

- Suggested adding clearer repository evidence, syllabus mapping, individual teach-back sections, hand-drawn traces, and a more detailed AI usage log.

Verification and Modification:

- The suggestions were checked directly against the rubric and repository history.
- The team added hand-drawn evidence and clearer evidence paths.
- Week 2 implementation was not presented as Week 1 progress.

---

### Entry 3 - Mai Trần Thùy Trang

Date: 04/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Analyze the E-Learning Platform requirements and suggest candidate classes.

AI Output Summary:

- Suggested candidate classes related to Course, Lesson, users, Enrollment, Progress, completion, and supporting responsibilities.

Verification and Modification:

- Trang checked each proposed class against the project requirements.
- Classes without a clear responsibility were removed or combined.
- The design was kept small and focused on the required project scope.

---

### Entry 4 - Mai Trần Thùy Trang

Date: 04/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Analyze candidate classes and OOP relationships for the E-Learning Platform.

AI Output Summary:

- Suggested relationships for the Lesson hierarchy, Course-Lesson, Student-Enrollment-Course, Enrollment-Progress, and completion evidence.

Verification and Modification:

- Trang reviewed whether each relationship matched the responsibility of the classes.
- The UML was adjusted to keep only relationships that could be clearly explained from the project requirements.

---

## AI Design Weakness / Rejected Suggestion

One early AI suggestion was to create skeleton implementations for Student, Enrollment, and Progress while Kha was working on the Course/Lesson side.

The team rejected this suggestion because it would reduce the clarity of Trang's ownership and contribution.

Instead:

- only the shared package structure and domain contract were prepared;
- Kha implemented the Course/Lesson domain on `feature/course-lesson`;
- Trang implemented Student/Enrollment/Progress on `feature/enrollment-progress`.

---

## Week 2 - Core Classes and Invariants

### Entry 1 - Mai Trần Thùy Trang

Date: 11/10/2026

Tool / Model:

- ChatGPT
- Codex

Prompt / Task:

- Implement Trang's Week 2 classes: Student, Enrollment, and Progress.

AI Output Summary:

- Helped plan and draft the three classes.
- Suggested additional prerequisite validations such as cycle detection, self-prerequisite, and cross-course prerequisite.

Verification and Modification:

- Trang checked the code against the class diagram and `DOMAIN_CONTRACT.md`.
- The complex prerequisite validations were rejected because the project only requires simple prerequisite locking.
- The classes were compiled successfully before committing.

---

### Entry 2 - Mai Trần Thùy Trang

Date: 12/10/2026

Tool / Model:

- ChatGPT
- Codex

Prompt / Task:

- Prepare and verify Week 2 unit tests.

AI Output Summary:

- Helped set up Maven with JUnit 5.
- Initially suggested a larger test suite covering prerequisite, polymorphism, validation, and end-to-end scenarios.

Verification and Modification:

- Trang reviewed the Week 2 scope and first kept 9 basic unit tests:
  - 3 Student tests;
  - 3 Enrollment tests;
  - 3 Progress tests.
- After reviewing the Week 2 rubric, 2 additional invariant-protection tests were added:
  - reject a lesson that does not belong to the Course;
  - reject a locked lesson when its prerequisite is incomplete.
- The final Week 2 suite contains 11 unit tests.

Result:

- Tests run: 11
- Failures: 0
- Errors: 0
- BUILD SUCCESS
### Entry 3 - Trần Tuấn Kha

Date: 14/10/2026

Tool / Model:
- ChatGPT
- GPT-5.6 Sol

Prompt / Task:
- Review the current Week 2 implementation and tests against the assessment rubric.
- Identify missing evidence and prepare the Week 2 integration/report structure.

AI Output Summary:
- Identified that the existing Progress tests needed clearer invariant-protection evidence.
- Suggested verifying the two Progress invariants and adding a Lesson polymorphism test.
- Suggested a rubric-aligned structure for the Week 2 report.

Verification and Modification:
- Kha reviewed the actual branch diff and existing tests before making changes.
- The invariant tests were verified from Trang's branch.
- Kha added only LessonPolymorphismTest and final Maven test-output evidence instead of adding unnecessary features.
- The full Maven suite was rerun on Java 17 before the Week 2 report was finalized.

Result:
- Tests run: 12
- Failures: 0
- Errors: 0
- BUILD SUCCESS