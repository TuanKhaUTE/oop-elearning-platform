package elearning.enrollment;

import elearning.course.Course;
import elearning.lesson.TextEvidence;
import elearning.lesson.TextLesson;
import elearning.user.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentRestoreTest {
    @Test
    void restoresEnrollmentWithSuppliedObjectsAndValidatedProgress() {
        Student student = new Student("S1", "Trang");
        Course course = new Course("C1", "Java OOP");
        TextLesson lesson = new TextLesson("L1", "Reading");
        course.addLesson(lesson);

        Enrollment restored = Enrollment.restore(student, course, List.of("L1"));

        assertSame(student, restored.getStudent());
        assertSame(course, restored.getCourse());
        assertTrue(restored.getProgress().isLessonCompleted(lesson));
        assertEquals(100.0, restored.getProgress().getOverallPercentage(), 0.001);
        assertTrue(restored.getProgress().isCertificateEligible());
    }

    @Test
    void rejectsNullArguments() {
        Student student = new Student("S1", "Trang");
        Course course = new Course("C1", "Java OOP");
        assertThrows(IllegalArgumentException.class,
                () -> Enrollment.restore(null, course, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> Enrollment.restore(student, null, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> Enrollment.restore(student, course, null));
    }

    @Test
    void failedRestorationDoesNotChangeExistingEnrollment() {
        Student student = new Student("S1", "Trang");
        Course course = new Course("C1", "Java OOP");
        TextLesson lesson = new TextLesson("L1", "Reading");
        course.addLesson(lesson);
        Enrollment existing = student.enroll(course);
        existing.getProgress().updateLessonProgress(lesson, new TextEvidence(true));

        assertThrows(IllegalArgumentException.class,
                () -> Enrollment.restore(student, course, List.of("L1", "unknown")));

        assertTrue(existing.getProgress().isLessonCompleted(lesson));
        assertTrue(existing.getProgress().isCertificateEligible());
    }
}
