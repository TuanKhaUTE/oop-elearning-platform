package elearning.progress;

import elearning.course.Course;
import elearning.lesson.QuizEvidence;
import elearning.lesson.QuizLesson;
import elearning.lesson.TextEvidence;
import elearning.lesson.TextLesson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgressEdgeCaseTest {
    @Test
    void emptyCourseHasZeroProgressAndNoCertificate() {
        Progress progress = new Progress(new Course("C1", "Empty course"));

        assertEquals(0.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
    }

    @Test
    void failedQuizPreservesPartialProgressAndPassingRetryEarnsCertificate() {
        Course course = new Course("C1", "Java OOP");
        TextLesson reading = new TextLesson("L1", "Reading");
        QuizLesson quiz = new QuizLesson("L2", "Quiz", 70.0);
        course.addLesson(reading);
        course.addLesson(quiz);
        Progress progress = new Progress(course);
        assertTrue(progress.updateLessonProgress(reading, new TextEvidence(true)));

        assertFalse(progress.updateLessonProgress(quiz, new QuizEvidence(69.0)));

        assertTrue(progress.isLessonCompleted(reading));
        assertFalse(progress.isLessonCompleted(quiz));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());

        assertTrue(progress.updateLessonProgress(quiz, new QuizEvidence(70.0)));
        assertTrue(progress.isLessonCompleted(quiz));
        assertEquals(100.0, progress.getOverallPercentage(), 0.001);
        assertTrue(progress.isCertificateEligible());
    }

    @Test
    void lockedLessonRejectionPreservesExistingCompletion() {
        Course course = new Course("C1", "Java OOP");
        TextLesson completed = new TextLesson("L1", "Completed reading");
        TextLesson prerequisite = new TextLesson("L2", "Prerequisite");
        TextLesson locked = new TextLesson("L3", "Locked reading");
        locked.setPrerequisite(prerequisite);
        course.addLesson(completed);
        course.addLesson(prerequisite);
        course.addLesson(locked);
        Progress progress = new Progress(course);
        assertTrue(progress.updateLessonProgress(completed, new TextEvidence(true)));

        assertThrows(IllegalStateException.class,
                () -> progress.updateLessonProgress(locked, new TextEvidence(true)));

        assertTrue(progress.isLessonCompleted(completed));
        assertFalse(progress.isLessonCompleted(prerequisite));
        assertFalse(progress.isLessonCompleted(locked));
        assertFalse(progress.isLessonUnlocked(locked));
        assertEquals(100.0 / 3.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
    }

    @Test
    void outsideLessonRejectionPreservesPartialProgress() {
        Course course = new Course("C1", "Java OOP");
        TextLesson completed = new TextLesson("L1", "Completed reading");
        TextLesson remaining = new TextLesson("L2", "Remaining reading");
        TextLesson outside = new TextLesson("L3", "Outside course");
        course.addLesson(completed);
        course.addLesson(remaining);
        Progress progress = new Progress(course);
        assertTrue(progress.updateLessonProgress(completed, new TextEvidence(true)));

        assertThrows(IllegalArgumentException.class,
                () -> progress.updateLessonProgress(outside, new TextEvidence(true)));

        assertTrue(progress.isLessonCompleted(completed));
        assertFalse(progress.isLessonCompleted(remaining));
        assertFalse(progress.isLessonCompleted(outside));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
    }

    @Test
    void duplicateCompletionDoesNotInflateProgressOrEarnCertificateEarly() {
        Course course = new Course("C1", "Java OOP");
        TextLesson first = new TextLesson("L1", "First reading");
        TextLesson second = new TextLesson("L2", "Second reading");
        course.addLesson(first);
        course.addLesson(second);
        Progress progress = new Progress(course);
        assertTrue(progress.updateLessonProgress(first, new TextEvidence(true)));

        // The duplicate call's return value is not specified by the shared contract.
        progress.updateLessonProgress(first, new TextEvidence(true));

        assertTrue(progress.isLessonCompleted(first));
        assertFalse(progress.isLessonCompleted(second));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
    }

    @Test
    void wrongEvidencePreservesPartialProgress() {
        Course course = new Course("C1", "Java OOP");
        TextLesson reading = new TextLesson("L1", "Reading");
        QuizLesson quiz = new QuizLesson("L2", "Quiz", 70.0);
        course.addLesson(reading);
        course.addLesson(quiz);
        Progress progress = new Progress(course);
        assertTrue(progress.updateLessonProgress(reading, new TextEvidence(true)));

        assertFalse(progress.updateLessonProgress(quiz, new TextEvidence(true)));

        assertTrue(progress.isLessonCompleted(reading));
        assertFalse(progress.isLessonCompleted(quiz));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
    }

    @Test
    void failedPrerequisiteCompletionDoesNotUnlockDependentLesson() {
        Course course = new Course("C1", "Java OOP");
        QuizLesson prerequisite = new QuizLesson("L1", "Prerequisite quiz", 70.0);
        TextLesson dependent = new TextLesson("L2", "Dependent reading");
        dependent.setPrerequisite(prerequisite);
        course.addLesson(prerequisite);
        course.addLesson(dependent);
        Progress progress = new Progress(course);

        assertFalse(progress.updateLessonProgress(prerequisite, new QuizEvidence(69.0)));

        assertFalse(progress.isLessonCompleted(prerequisite));
        assertFalse(progress.isLessonUnlocked(dependent));
        assertThrows(IllegalStateException.class,
                () -> progress.updateLessonProgress(dependent, new TextEvidence(true)));
        assertFalse(progress.isLessonCompleted(dependent));
        assertEquals(0.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
    }
}
