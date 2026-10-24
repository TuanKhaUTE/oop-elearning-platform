package elearning.integration;

import elearning.course.Course;
import elearning.enrollment.Enrollment;
import elearning.lesson.*;
import elearning.progress.Progress;
import elearning.user.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentProgressIntegrationTest {
    private Course course;
    private Lesson video;
    private Lesson text;
    private Lesson assignment;
    private Lesson quiz;

    @BeforeEach
    void setUp() {
        course = new Course("C1", "Java OOP");
        video = new VideoLesson("L1", "Introduction Video", 80.0);
        text = new TextLesson("L2", "OOP Reading");
        assignment = new AssignmentLesson("L3", "OOP Reflection", 500);
        quiz = new QuizLesson("L4", "OOP Quiz", 70.0);

        text.setPrerequisite(video);
        assignment.setPrerequisite(text);
        quiz.setPrerequisite(assignment);

        course.addLesson(video);
        course.addLesson(text);
        course.addLesson(assignment);
        course.addLesson(quiz);
    }

    @Test
    void lockedAssignmentRejectsCompletionWithoutChangingProgress() {
        Progress progress = new Progress(course);
        assertTrue(progress.updateLessonProgress(video, new VideoEvidence(85.0)));
        assertEquals(25.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isLessonUnlocked(assignment));

        assertThrows(IllegalStateException.class,
                () -> progress.updateLessonProgress(assignment, new AssignmentEvidence(500)));

        assertTrue(progress.isLessonCompleted(video));
        assertFalse(progress.isLessonCompleted(text));
        assertFalse(progress.isLessonCompleted(assignment));
        assertFalse(progress.isLessonCompleted(quiz));
        assertEquals(25.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isLessonUnlocked(assignment));
        assertFalse(progress.isLessonUnlocked(quiz));
    }

    @Test
    void insufficientAssignmentEvidencePreservesProgressAndPassingRetryUnlocksQuiz() {
        Progress progress = new Progress(course);
        assertTrue(progress.updateLessonProgress(video, new VideoEvidence(85.0)));
        assertTrue(progress.updateLessonProgress(text, new TextEvidence(true)));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertTrue(progress.isLessonUnlocked(assignment));

        assertFalse(progress.updateLessonProgress(assignment, new AssignmentEvidence(499)));

        assertTrue(progress.isLessonCompleted(video));
        assertTrue(progress.isLessonCompleted(text));
        assertFalse(progress.isLessonCompleted(assignment));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isLessonUnlocked(quiz));
        assertFalse(progress.isCertificateEligible());

        assertTrue(progress.updateLessonProgress(assignment, new AssignmentEvidence(500)));

        assertTrue(progress.isLessonCompleted(assignment));
        assertEquals(75.0, progress.getOverallPercentage(), 0.001);
        assertTrue(progress.isLessonUnlocked(quiz));
        assertFalse(progress.isLessonCompleted(quiz));
        assertFalse(progress.isCertificateEligible());
    }

    @Test
    void mixedCourseRequiresAssignmentAndEveryOtherLessonForCertificate() {
        Student student = new Student("S1", "Trang");
        Enrollment enrollment = student.enroll(course);
        Progress progress = enrollment.getProgress();
        CompletionEvidence videoEvidence = new VideoEvidence(85.0);
        CompletionEvidence textEvidence = new TextEvidence(true);
        CompletionEvidence assignmentEvidence = new AssignmentEvidence(500);
        CompletionEvidence quizEvidence = new QuizEvidence(80.0);

        assertEquals(0.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());

        assertTrue(progress.updateLessonProgress(video, videoEvidence));
        assertTrue(progress.isLessonCompleted(video));
        assertEquals(25.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());

        assertTrue(progress.updateLessonProgress(text, textEvidence));
        assertTrue(progress.isLessonCompleted(text));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());

        assertTrue(progress.updateLessonProgress(assignment, assignmentEvidence));
        assertTrue(progress.isLessonCompleted(assignment));
        assertEquals(75.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());

        assertTrue(progress.updateLessonProgress(quiz, quizEvidence));
        assertTrue(progress.isLessonCompleted(quiz));
        assertEquals(100.0, progress.getOverallPercentage(), 0.001);
        assertTrue(progress.isCertificateEligible());
    }
}
