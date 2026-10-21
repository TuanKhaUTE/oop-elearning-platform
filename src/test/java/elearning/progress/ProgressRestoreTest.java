package elearning.progress;

import elearning.course.Course;
import elearning.lesson.TextLesson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProgressRestoreTest {
    private final Course course = new Course("C1", "Java OOP");
    private final TextLesson first = new TextLesson("L1", "First");
    private final TextLesson second = new TextLesson("L2", "Second");

    ProgressRestoreTest() {
        course.addLesson(first);
        course.addLesson(second);
        second.setPrerequisite(first);
    }

    @Test
    void restoresEmptyCompletionState() {
        Progress progress = Progress.restore(course, List.of());
        assertFalse(progress.isLessonCompleted(first));
        assertFalse(progress.isLessonCompleted(second));
        assertEquals(0.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
        Progress emptyCourse = Progress.restore(new Course("C2", "Empty"), List.of());
        assertEquals(0.0, emptyCourse.getOverallPercentage(), 0.001);
        assertFalse(emptyCourse.isCertificateEligible());
    }

    @Test
    void restoresPartialCompletion() {
        Progress progress = Progress.restore(course, List.of("L1"));
        assertTrue(progress.isLessonCompleted(first));
        assertFalse(progress.isLessonCompleted(second));
        assertTrue(progress.isLessonUnlocked(second));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.isCertificateEligible());
    }

    @Test
    void restoresFullCompletion() {
        Progress progress = Progress.restore(course, List.of("L1", "L2"));
        assertTrue(progress.isLessonCompleted(first));
        assertTrue(progress.isLessonCompleted(second));
        assertEquals(100.0, progress.getOverallPercentage(), 0.001);
        assertTrue(progress.isCertificateEligible());
    }

    @Test
    void rejectsUnknownId() {
        assertThrows(IllegalArgumentException.class,
                () -> Progress.restore(course, List.of("L1", "unknown")));
    }

    @Test
    void rejectsDuplicateCompletedId() {
        assertThrows(IllegalArgumentException.class,
                () -> Progress.restore(course, List.of("L1", "L1")));
    }

    @Test
    void rejectsMissingPrerequisite() {
        assertThrows(IllegalArgumentException.class,
                () -> Progress.restore(course, List.of("L2")));
    }

    @Test
    void acceptsReversedCompletionAndCourseOrder() {
        Course reversed = new Course("C2", "Reversed");
        reversed.addLesson(second);
        reversed.addLesson(first);
        Progress progress = Progress.restore(reversed, List.of("L2", "L1"));
        assertTrue(progress.isCertificateEligible());
        assertEquals(100.0, progress.getOverallPercentage(), 0.001);
    }

    @Test
    void rejectsCompletedCycle() {
        first.setPrerequisite(second);
        assertThrows(IllegalArgumentException.class,
                () -> Progress.restore(course, List.of("L1", "L2")));
    }

    @Test
    void usesSuppliedCourseInstances() {
        TextLesson oldInstance = new TextLesson("L1", "Old first");
        Progress progress = Progress.restore(course, List.of(oldInstance.getId()));
        assertTrue(progress.isLessonCompleted(first));
        assertFalse(progress.isLessonCompleted(oldInstance));
        assertTrue(progress.isLessonUnlocked(second));
    }

    @Test
    void doesNotRetainMutableInputOrShareCompletionState() {
        List<String> ids = new ArrayList<>(List.of("L1"));
        Progress restored = Progress.restore(course, ids);
        Progress independent = Progress.restore(course, List.of());
        ids.clear();
        ids.add("L2");
        assertTrue(restored.isLessonCompleted(first));
        assertFalse(restored.isLessonCompleted(second));
        assertEquals(50.0, restored.getOverallPercentage(), 0.001);
        assertFalse(independent.isLessonCompleted(first));
    }

    @Test
    void rejectsNullArgumentsAndInvalidCompletedIds() {
        assertThrows(IllegalArgumentException.class, () -> Progress.restore(null, List.of()));
        assertThrows(IllegalArgumentException.class, () -> Progress.restore(course, null));
        for (String id : Arrays.asList(null, "", " ", " L1", "L1 ", "L\n1", "L\r1")) {
            assertThrows(IllegalArgumentException.class,
                    () -> Progress.restore(course, Arrays.asList(id)));
        }
    }

    @Test
    void rejectsDuplicateCourseIdsEvenWithNoCompletions() {
        course.addLesson(new TextLesson("L1", "Duplicate"));
        assertThrows(IllegalArgumentException.class, () -> Progress.restore(course, List.of()));
    }

    @Test
    void rejectsInvalidCourseIdsEvenWithNoCompletions() {
        for (String id : Arrays.asList(null, "", " ", " L3", "L3 ", "L\n3", "L\r3")) {
            Course invalid = new Course("C2", "Invalid");
            invalid.addLesson(new TextLesson(id, "Invalid ID"));
            assertThrows(IllegalArgumentException.class, () -> Progress.restore(invalid, List.of()));
        }
    }

    @Test
    void rejectsSelfPrerequisite() {
        first.setPrerequisite(first);
        assertThrows(IllegalArgumentException.class,
                () -> Progress.restore(course, List.of("L1")));
    }

    @Test
    void rejectsExternalPrerequisiteEvenWithMatchingId() {
        second.setPrerequisite(new TextLesson("L1", "External"));
        assertThrows(IllegalArgumentException.class,
                () -> Progress.restore(course, List.of("L1", "L2")));
    }
}
