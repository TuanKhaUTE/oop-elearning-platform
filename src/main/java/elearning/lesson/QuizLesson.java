package elearning.lesson;

public class QuizLesson extends Lesson {
    private final double minimumScore;

    public QuizLesson(String id, String title, double minimumScore) {
        super(id, title);

        PercentageValidator.requireValid(
                minimumScore,
                "Minimum score");

        this.minimumScore = minimumScore;
    }

    public double getMinimumScore() {
        return minimumScore;
    }

    @Override
    public boolean isComplete(CompletionEvidence evidence) {
        if (!(evidence instanceof QuizEvidence)) {
            return false;
        }

        QuizEvidence quizEvidence = (QuizEvidence) evidence;
        return quizEvidence.getScore() >= minimumScore;
    }
}