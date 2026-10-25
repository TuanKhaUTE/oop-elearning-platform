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
- GPT-5.6 Sol

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
- GPT-5.6 Sol

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

---
## Week 3 - Features, Exceptions, Persistence and Testing

### Entry 1 - Mai Trần Thùy Trang

Date: 20/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Review Week 3 requirements and implement edge-case tests for Progress, prerequisites, and certificate eligibility.

AI Output Summary:

- Helped identify edge cases involving incomplete prerequisites, duplicate completion, invalid completion evidence, and certificate eligibility.
- Generated `ProgressEdgeCaseTest.java` with 7 JUnit tests.
- The tests detected a bug where an empty Course incorrectly returned `isCertificateEligible() = true`.

Verification and Modification:

- Trang ran the tests using IntelliJ IDEA and Maven.
- The initial result was 6 passed and 1 failed.
- Trang verified the bug and added an empty-course guard in `Progress.isCertificateEligible()`.
- Tests were rerun and before/after screenshots were saved as evidence.

Result:

- Before Fix: 7 tests, 1 failure.
- After Fix: 7 tests, 0 failures.

---

### Entry 2 - Mai Trần Thùy Trang

Date: 20/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Design and implement validated restoration for Enrollment and Progress while preserving encapsulation and prerequisite rules.

AI Output Summary:

- Proposed `Progress.restore()` and `Enrollment.restore()` to reconstruct completion state from saved lesson IDs.
- Suggested validating unknown or duplicate lesson IDs, missing prerequisites, and invalid completion states.
- Generated restoration methods and corresponding JUnit tests.

Verification and Modification:

- Trang reviewed the design before implementation and selected a separate historical restoration mechanism instead of using fabricated `CompletionEvidence`.
- `completedLessons` remained private, without adding a public setter.
- Trang verified that restored lessons belong to the supplied Course and that normal completion logic remained unchanged.
- Relevant tests and the full Maven suite were executed successfully.

Result:

- Added 15 `ProgressRestoreTest` tests and 3 `EnrollmentRestoreTest` tests.
- Full Maven suite at this stage: 43 tests, 0 failures.

---

### Entry 3 - Mai Trần Thùy Trang

Date: 20/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Implement file-based persistence for Enrollment/Progress and prepare JUnit tests for save/load, exceptions, and invalid data.

AI Output Summary:

- Proposed and generated `EnrollmentFileStore.java` using Java File I/O.
- Implemented `save()` and `load()` with a UTF-8 file containing Student ID, Course ID, and completed lesson IDs.
- Suggested validating malformed files, mismatched IDs, invalid prerequisites, and incomplete data.
- Generated `EnrollmentFileStoreTest.java` covering 22 testing scenarios.

Verification and Modification:

- Trang reviewed the persistence design and kept File I/O separate from the domain classes.
- The implementation reused `Enrollment.restore()` to validate loaded completion state.
- Trang verified file saving and loading, exception handling, and atomic file replacement.
- JUnit tests were run using IntelliJ's bundled Maven.

Result:

- `EnrollmentFileStoreTest`: 48 tests, 0 failures.
- Full Maven suite at this stage: 91 tests, 0 failures.

---

### Entry 4 - Mai Trần Thùy Trang

Date: 20/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Prepare and verify the persistence save/load demonstration, full test evidence, and Week 3 documentation.

AI Output Summary:

- Helped review and improve `EnrollmentPersistenceDemoTest.java`.
- Suggested displaying progress before saving, saved file contents, and restored progress.
- Assisted with organizing test evidence and documenting the Week 3 implementation.

Verification and Modification:

- Trang reviewed the existing demo test and decided not to create another Console Demo class because it would duplicate functionality.
- The existing demo was updated to print the before-save state while preserving its assertions.
- Trang ran the demo and verified that newly created Student, Course, Lesson, Enrollment, and Progress objects restored the correct state.
- Trang reran the full Maven suite and saved the test output and screenshots.

Result:

- Before Save: 2/3 lessons completed, 66.67%, certificate eligibility `false`.
- After Load: 2/3 lessons completed, 66.67%, certificate eligibility `false`.
- Full Maven suite: 92 tests, 0 failures, 0 errors.
- BUILD SUCCESS.

---

### Entry 5 - Trần Tuấn Kha

Date: 19/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Review the Week 3 rubric against the current implementation.
- Identify the smallest remaining end-to-end, validation, edge-case, and refactoring work required for Kha's ownership.

AI Output Summary:

- Suggested one end-to-end course completion test.
- Suggested lesson percentage-validation edge cases and null-lesson validation.
- Identified duplicated 0..100 validation across VideoLesson, QuizLesson, VideoEvidence, and QuizEvidence as a genuine refactoring candidate.

Verification and Modification:

- Kha reviewed the actual source before changing code.
- Kha added ELearningEndToEndTest and LessonValidationTest first and ran Maven successfully.
- Refactoring was performed only after the tests locked the existing behavior.

Result:

- 18 tests passed before Trang's Week 3 branch was integrated.

---

### Entry 6 - Trần Tuấn Kha

Date: 19/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Refactor duplicated percentage validation without changing external behavior and prepare before/after evidence.

AI Output Summary:

- Suggested extracting package-private PercentageValidator.requireValid(...).
- Suggested keeping behavior and exception semantics unchanged.
- Suggested storing the refactor diff and Maven output as Week 3 evidence.

Verification and Modification:

- Kha updated VideoLesson, QuizLesson, VideoEvidence, and QuizEvidence to call PercentageValidator.
- Kha reran the full suite and confirmed all 18 tests still passed.
- The refactoring and test evidence were committed separately to preserve before/after history.

Result:

- Refactor commit: ce7b5c2.
- Evidence commit: f818295.

---
## Week 4 - Extensibility and Final Integration

### Entry 1 - Mai Trần Thùy Trang

Date: 25/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Review the Week 4 Assignment extension and plan integration tests for Progress, Enrollment, and persistence.

AI Output Summary:

- Reviewed the existing `AssignmentLesson` and `AssignmentEvidence` APIs.
- Suggested integration scenarios for prerequisite locking, assignment completion, progress calculation, certificate eligibility, and persistence.
- Proposed tests demonstrating Open/Closed Principle without modifying existing production classes.

Verification and Modification:

- Trang checked the actual constructors and completion rules before implementing tests.
- The test plan was limited to Trang's integration responsibilities to avoid duplicating Kha's Assignment validation and extensibility tests.
- Trang selected two integration test classes, keeping `Progress`, `Enrollment`, and `EnrollmentFileStore` unchanged.

---

### Entry 2 - Mai Trần Thùy Trang

Date: 25/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Implement and verify Assignment integration tests for Progress and Enrollment.

AI Output Summary:

- Helped implement `AssignmentProgressIntegrationTest.java` with three JUnit tests.
- Covered prerequisite locking, unsuccessful and successful Assignment completion, progress percentage, and certificate eligibility.
- Used `Lesson` and `CompletionEvidence` references to verify polymorphic behavior.

Verification and Modification:

- Trang reviewed the assertions and checked that the tests used the actual Assignment API.
- Verified that submitting 499 words did not complete the Assignment, while submitting 500 words completed it and unlocked Quiz.
- Confirmed progress transitions of 0%, 25%, 50%, 75%, and 100%.
- Ran the targeted tests and full Maven suite without modifying production code.

Result:

- Assignment Progress integration: 3 tests, 0 failures, 0 errors.
- BUILD SUCCESS.

---

### Entry 3 - Mai Trần Thùy Trang

Date: 25/10/2026

Tool / Model:

- ChatGPT
- GPT-5.6 Sol

Prompt / Task:

- Implement Assignment persistence integration tests, verify the full Maven suite, and review Week 4 evidence.

AI Output Summary:

- Helped implement `AssignmentEnrollmentFileStoreIntegrationTest.java` with three JUnit tests using `@TempDir`.
- Suggested save/load scenarios for incomplete, completed, and fully completed Assignments.
- Helped review the integration tests, Git changes, and evidence files.

Verification and Modification:

- Trang verified restoration using new Student, Course, Lesson, Enrollment, and Progress objects.
- Confirmed that Assignment completion and prerequisite states were restored correctly.
- Persistence tests initially encountered `AccessDeniedException` in the sandbox; Trang reran them outside the restricted environment, where they passed.
- Trang reviewed the Git changes and confirmed that no production classes or existing tests were modified.
- Saved two JUnit screenshots and the full Maven test output as evidence.

Result:

- Assignment Persistence integration: 3 tests, 0 failures, 0 errors.
- Total new Week 4 integration tests: 6/6 passed.
- Full Maven suite: 102 tests, 0 failures, 0 errors, 0 skipped.
- BUILD SUCCESS.
