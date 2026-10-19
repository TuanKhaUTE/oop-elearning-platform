package elearning.integration;

import elearning.course.Course;
import elearning.enrollment.Enrollment;
import elearning.lesson.*;
import elearning.progress.Progress;
import elearning.user.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ELearningEndToEndTest {

    @Test
    void studentCanCompleteCourseAndBecomeCertificateEligible() {
        Course course = new Course("C1", "Java OOP");

        VideoLesson video =
                new VideoLesson("L1", "Introduction Video", 80.0);

        TextLesson text =
                new TextLesson("L2", "OOP Reading");

        QuizLesson quiz =
                new QuizLesson("L3", "OOP Quiz", 70.0);

        text.setPrerequisite(video);
        quiz.setPrerequisite(text);

        course.addLesson(video);
        course.addLesson(text);
        course.addLesson(quiz);

        Student student = new Student("S1", "Kha");
        Enrollment enrollment = student.enroll(course);
        Progress progress = enrollment.getProgress();

        assertEquals(0.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());

        assertTrue(
                progress.updateLessonProgress(
                        video,
                        new VideoEvidence(85.0)
                )
        );

        assertTrue(progress.isLessonUnlocked(text));
        assertEquals(
                100.0 / 3.0,
                progress.getOverallPercentage(),
                0.001
        );

        assertTrue(
                progress.updateLessonProgress(
                        text,
                        new TextEvidence(true)
                )
        );

        assertTrue(progress.isLessonUnlocked(quiz));

        assertTrue(
                progress.updateLessonProgress(
                        quiz,
                        new QuizEvidence(80.0)
                )
        );

        assertEquals(
                100.0,
                progress.getOverallPercentage(),
                0.001
        );

        assertTrue(progress.isCertificateEligible());
    }
}