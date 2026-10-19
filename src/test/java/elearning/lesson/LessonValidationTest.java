package elearning.lesson;

import elearning.course.Course;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LessonValidationTest {

    @Test
    void videoEvidenceRejectsInvalidWatchedPercent() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new VideoEvidence(-0.1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new VideoEvidence(100.1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new VideoEvidence(Double.NaN)
        );
    }

    @Test
    void quizEvidenceRejectsInvalidScore() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new QuizEvidence(-0.1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new QuizEvidence(100.1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new QuizEvidence(Double.NaN)
        );
    }

    @Test
    void lessonThresholdsRejectInvalidPercentages() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new VideoLesson("L1", "Video", -0.1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new QuizLesson("L2", "Quiz", 100.1)
        );
    }

    @Test
    void percentageBoundaryValuesAreAccepted() {
        assertDoesNotThrow(() -> new VideoEvidence(0.0));
        assertDoesNotThrow(() -> new VideoEvidence(100.0));

        assertDoesNotThrow(() -> new QuizEvidence(0.0));
        assertDoesNotThrow(() -> new QuizEvidence(100.0));

        assertDoesNotThrow(
                () -> new VideoLesson("L1", "Video", 0.0)
        );

        assertDoesNotThrow(
                () -> new QuizLesson("L2", "Quiz", 100.0)
        );
    }

    @Test
    void courseRejectsNullLesson() {
        Course course = new Course("C1", "Java OOP");

        assertThrows(
                IllegalArgumentException.class,
                () -> course.addLesson(null)
        );
    }
}