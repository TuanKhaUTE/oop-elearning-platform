package elearning.lesson;

public class QuizEvidence implements CompletionEvidence {
    private final double score;

    public QuizEvidence(double score) {
        if (Double.isNaN(score) || score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        }

        this.score = score;
    }

    public double getScore() {
        return score;
    }
}
