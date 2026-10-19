package elearning.lesson;

public class VideoEvidence implements CompletionEvidence {
    private final double watchedPercent;

    public VideoEvidence(double watchedPercent) {
        PercentageValidator.requireValid(
                watchedPercent,
                "Watched percent");

        this.watchedPercent = watchedPercent;
    }

    public double getWatchedPercent() {
        return watchedPercent;
    }
}