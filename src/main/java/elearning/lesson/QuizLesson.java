package elearning.lesson;

public class QuizLesson extends Lesson {
    private final double minimumScore;

    public QuizLesson(String id, String title, double minimumScore) {
        super(id, title);

        if (Double.isNaN(minimumScore) || minimumScore < 0 || minimumScore > 100) {
            throw new IllegalArgumentException("Minimum score must be between 0 and 100.");
        }

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
