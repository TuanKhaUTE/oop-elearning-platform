package elearning.lesson;

public class VideoEvidence implements CompletionEvidence {
    private final double watchedPercent;

    public VideoEvidence(double watchedPercent) {
        if (Double.isNaN(watchedPercent) || watchedPercent < 0 || watchedPercent > 100) {
            throw new IllegalArgumentException("Watched percent must be between 0 and 100.");
        }

        this.watchedPercent = watchedPercent;
    }

    public double getWatchedPercent() {
        return watchedPercent;
    }
}
