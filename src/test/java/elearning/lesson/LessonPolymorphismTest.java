package elearning.lesson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonPolymorphismTest {

    @Test
    void differentLessonTypesUseTheirOwnCompletionRules() {
        Lesson videoLesson =
                new VideoLesson("L1", "Introduction Video", 80.0);

        Lesson textLesson =
                new TextLesson("L2", "OOP Reading");

        Lesson quizLesson =
                new QuizLesson("L3", "OOP Quiz", 70.0);

        assertTrue(
                videoLesson.isComplete(new VideoEvidence(85.0))
        );

        assertTrue(
                textLesson.isComplete(new TextEvidence(true))
        );

        assertTrue(
                quizLesson.isComplete(new QuizEvidence(80.0))
        );
    }
}