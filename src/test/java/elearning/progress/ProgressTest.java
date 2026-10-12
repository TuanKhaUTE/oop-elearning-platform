package elearning.progress;

import elearning.course.Course;
import elearning.lesson.TextEvidence;
import elearning.lesson.TextLesson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressTest {
    @Test
    void startsAtZeroPercentForNonEmptyCourse() {
        Course course = new Course("C1", "Java OOP");
        course.addLesson(new TextLesson("L1", "Introduction"));
        Progress progress = new Progress(course);

        assertEquals(0.0, progress.getOverallPercentage(), 0.001);
    }

    @Test
    void successfulCompletionIsRecorded() {
        Course course = new Course("C1", "Java OOP");
        TextLesson lesson = new TextLesson("L1", "Introduction");
        course.addLesson(lesson);
        Progress progress = new Progress(course);

        progress.updateLessonProgress(lesson, new TextEvidence(true));

        assertTrue(progress.isLessonCompleted(lesson));
    }

    @Test
    void certificateIsAvailableAfterAllLessonsAreComplete() {
        Course course = new Course("C1", "Java OOP");
        TextLesson first = new TextLesson("L1", "Introduction");
        TextLesson second = new TextLesson("L2", "Classes");
        course.addLesson(first);
        course.addLesson(second);
        Progress progress = new Progress(course);

        progress.updateLessonProgress(first, new TextEvidence(true));
        progress.updateLessonProgress(second, new TextEvidence(true));

        assertTrue(progress.isCertificateEligible());
    }
}
