package elearning.lesson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentLessonExtensibilityTest {

    @Test
    void assignmentLessonWorksThroughLessonAbstraction() {
        Lesson lesson = new AssignmentLesson(
                "A1",
                "OOP Reflection Assignment",
                500);

        assertFalse(
                lesson.isComplete(
                        new AssignmentEvidence(499)));

        assertTrue(
                lesson.isComplete(
                        new AssignmentEvidence(500)));

        assertTrue(
                lesson.isComplete(
                        new AssignmentEvidence(750)));
    }

    @Test
    void assignmentLessonRejectsWrongEvidenceType() {
        Lesson lesson = new AssignmentLesson(
                "A1",
                "OOP Reflection Assignment",
                500);

        assertFalse(
                lesson.isComplete(
                        new TextEvidence(true)));
    }

    @Test
    void assignmentLessonRejectsInvalidMinimumWordCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AssignmentLesson(
                        "A1",
                        "Invalid Assignment",
                        0));

        assertThrows(
                IllegalArgumentException.class,
                () -> new AssignmentLesson(
                        "A2",
                        "Invalid Assignment",
                        -100));
    }

    @Test
    void assignmentEvidenceRejectsNegativeWordCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AssignmentEvidence(-1));
    }
}