package elearning.lesson;

public class QuizEvidence implements CompletionEvidence {
    private final double score;

    public QuizEvidence(double score) {
        PercentageValidator.requireValid(
                score,
                "Score");

        this.score = score;
    }

    public double getScore() {
        return score;
    }
}