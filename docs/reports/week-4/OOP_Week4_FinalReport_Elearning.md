# WEEK 4 FINAL REPORT

**Course:** Object-Oriented Programming  
**Project:** E-Learning Platform (Courses, Enrollment, Progress Tracking)  
**Lecturer:** Huỳnh Xuân Phụng  
**Members:** Trần Tuấn Kha - 23110028; Mai Trần Thùy Trang - 23110065

## 1. Week 4 goals and final status

Week 4 focused on extension/finalization: prove Open/Closed Principle with a new Lesson variant, integrate that variant with Progress and persistence, freeze the final test suite, update UML/README, complete AI disclosure, prepare 10 defense Q&A and write individual reflections.

Final status:

- AssignmentLesson added as a new Lesson subtype without modifying existing core production classes.
- AssignmentEvidence added as the dedicated CompletionEvidence subtype.
- Invalid-input and polymorphic extensibility tests added.
- Assignment integrated with prerequisite locking, Progress, certificate eligibility and persistence/restore.
- Final main independently re-tested by Kha: **102 tests, 0 failures, 0 errors, 0 skipped - BUILD SUCCESS**.
- Final UML, README, AI Usage Log, defense Q&A and reflections completed.

## 2. Ownership

### Trần Tuấn Kha - 23110028

- AssignmentLesson and AssignmentEvidence.
- AssignmentLessonExtensibilityTest.
- Invalid-input tests and direct Open/Closed diff evidence.
- Review of Trang's Week 4 branch before merge.
- Final UML, README, Kha Q&A/reflection and final documentation review.

Main evidence: feature commit 3a64498, PR #10, merge a6dcace, 96-test evidence.

### Mai Trần Thùy Trang - 23110065

- AssignmentProgressIntegrationTest: 3 tests.
- AssignmentEnrollmentFileStoreIntegrationTest: 3 tests.
- Progress/prerequisite/certificate integration.
- Save/load and restore onto newly created Student/Course/Lesson objects.
- Final Maven evidence and Trang Q&A/reflection.

Main evidence: test commit cdbd666, PR #11, merge 1ff7840, 102-test Maven evidence.

## 3. Extensibility demo - Open/Closed Principle

Kha added AssignmentLesson, AssignmentEvidence and AssignmentLessonExtensibilityTest on baseline main 8e0d120.

Direct diff before test-output evidence:

    A src/main/java/elearning/lesson/AssignmentEvidence.java
    A src/main/java/elearning/lesson/AssignmentLesson.java
    A src/test/java/elearning/lesson/AssignmentLessonExtensibilityTest.java

There was no M entry for Lesson, Course, Progress, Enrollment, EnrollmentFileStore, or existing Lesson subtypes. The new variant therefore extends the system instead of modifying existing core logic.

Assignment rules:

- minimumWordCount must be greater than zero.
- submittedWordCount must not be negative.
- 499 words is incomplete for a 500-word assignment.
- 500 or more words is complete.
- wrong CompletionEvidence subtype returns false.
- test declares the new object through Lesson abstraction to demonstrate polymorphism.

Kha branch after extension: 96 tests, 0 failures, 0 errors, BUILD SUCCESS.

## 4. Integration with Progress and persistence

Trang used a mixed course: Video -> Text -> Assignment -> Quiz.

Progress integration verifies:

- after Video: 25%.
- Assignment remains locked until Text prerequisite is complete.
- after Video + Text: 50%.
- 499 words does not complete Assignment.
- 500 words completes Assignment: 75%, Quiz unlocks.
- after Quiz: 100%, certificate eligible = true.

Persistence integration verifies snapshots at 50%, 75% and 100%. EnrollmentFileStore stores IDs, not concrete Lesson types. Progress.restore() maps IDs to the Lesson objects of the supplied Course and validates prerequisite consistency. Therefore Assignment restores without subtype-specific persistence logic.

Known scope limit documented by Trang: the snapshot does not store historical AssignmentEvidence/word count, is not an anti-tampering mechanism, and the tests do not claim to simulate a true JVM restart. A sandbox AccessDeniedException was treated as an environment limitation; production code was not changed to hide it.

## 5. Final tests

Final Maven evidence from Trang:

    Tests run: 102
    Failures: 0
    Errors: 0
    Skipped: 0
    BUILD SUCCESS

The Maven compiler log uses release 17. Two targeted JUnit screenshots were produced on OpenJDK 26.0.1, so the report does not claim that every test was executed on a Java 17 runtime.

After PR #11 merged, Kha pulled final main and independently reran mvn clean test; all 102 tests passed again.

## 6. Final UML

Final UML: [Week 4 final class diagram](../../diagrams/week4_final_class_diagram.md)

The diagram matches all 17 production classes: Course; Student; Instructor; Enrollment; Progress; EnrollmentFileStore; Lesson and four subtypes; CompletionEvidence and four evidence subtypes; PercentageValidator. It also shows inheritance, interface implementation, Course-Lesson composition, Enrollment-Progress composition, prerequisite self-association and persistence dependencies.

## 7. README and run steps

README now documents the final four Lesson variants, Progress/restore, persistence, OOP mapping, structure and Maven commands.

Primary command:

    mvn clean test

Final verified suite: 102/102 passed.

## 8. OOP principles mapped to code

| Principle | Class / method | Evidence |
|---|---|---|
| Abstraction | Lesson; CompletionEvidence | Common behavior/input contracts |
| Inheritance | Video/Text/Quiz/AssignmentLesson extends Lesson | Each concrete type is-a Lesson |
| Polymorphism | Progress.updateLessonProgress -> lesson.isComplete(evidence) | Progress does not branch on concrete Lesson type |
| Encapsulation | Progress.completedLessons private | State changes only through validated update/restore paths |
| Composition | Course -> List<Lesson>; Enrollment -> Progress | Domain objects are built from contained parts |
| Open/Closed | AssignmentLesson + AssignmentEvidence | Variant added without editing existing core production classes |
| SRP / separation | Lesson rules vs Progress state vs EnrollmentFileStore I/O | Responsibilities remain separated |

## 9. AI usage and human verification

AI was used to review the rubric, propose test scenarios, review code and organize documentation. AI output was never treated as evidence by itself.

Kha verified the Week 4 extension against actual APIs, Git diff and Maven output. Trang limited her integration changes to new test classes and did not change production code to make tests pass. During review, an accidental rewrite of historical AI model names in the branch was corrected so Week 1-3 approved history remained unchanged and only Week 4 entries were appended.

Full disclosure: [AI_USAGE_LOG.md](../../../AI_USAGE_LOG.md)

## 10. Defense Q&A and reflections

- [10 final defense questions and answers](../../evidence/week-4/defense_qna.md)
- [Individual reflections - Kha and Trang](../../evidence/week-4/reflections.md)

The Q&A contains five questions owned by Kha and five by Trang and includes a direct mapping of OOP principles to classes/methods.

## 11. Repository/process evidence

- Repository: https://github.com/TuanKhaUTE/oop-elearning-platform
- Kha feature commit: https://github.com/TuanKhaUTE/oop-elearning-platform/commit/3a64498
- Kha PR #10: https://github.com/TuanKhaUTE/oop-elearning-platform/pull/10
- Kha merge: https://github.com/TuanKhaUTE/oop-elearning-platform/commit/a6dcace2dafa96e572ae7b034c40f2ec3bce0ed7
- Trang integration commit: https://github.com/TuanKhaUTE/oop-elearning-platform/commit/cdbd666fef6f3068c9a371cc335f6229ead46b63
- Trang PR #11: https://github.com/TuanKhaUTE/oop-elearning-platform/pull/11
- Final integration merge: https://github.com/TuanKhaUTE/oop-elearning-platform/commit/1ff78407db0d719817a3e4bd790f5146ca04c709
- Kha extensibility evidence: ../../evidence/week-4/kha/kha_week4_extensibility_test_output.txt
- Open/Closed diff: ../../evidence/week-4/kha/week4_extensibility_diff.txt
- Trang final Maven evidence: ../../evidence/week-4/trang/W4_Full_Maven_Test_Output.txt

## 12. Week 4 rubric check

| Rubric item | Evidence | Status |
|---|---|---|
| Extensibility demo | Assignment variant + add-only diff / PR #10 | ĐẠT |
| Dedicated evidence subtype + invalid input | AssignmentEvidence + four tests | ĐẠT |
| Integration | Six Progress/persistence tests / PR #11 | ĐẠT |
| Final UML matching code | 17 production classes | ĐẠT |
| Final report | This report | ĐẠT |
| Complete AI log | AI_USAGE_LOG.md Week 1-4 | ĐẠT |
| Defense Q&A | 10 questions + answers | ĐẠT |
| Individual reflection | Kha + Trang | ĐẠT |
| Final tests | 102/102 pass, BUILD SUCCESS | ĐẠT |

## 13. Conclusion

Week 4 completes the extension/finalization goal. AssignmentLesson proves the Lesson hierarchy can be extended without rewriting existing core classes. Trang's integration tests prove the new subtype continues to work with prerequisite locking, Progress, certificate eligibility and persistence. Final source, UML, README, AI log, Q&A/reflection and test evidence are synchronized for the portal submission and oral defense.
