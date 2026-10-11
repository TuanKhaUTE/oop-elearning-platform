package elearning.lesson;

public class VideoLesson extends Lesson {
    private final double requiredWatchPercent;

    public VideoLesson(String id, String title, double requiredWatchPercent) {
        super(id, title);

        if (Double.isNaN(requiredWatchPercent)
                || requiredWatchPercent < 0
                || requiredWatchPercent > 100) {
            throw new IllegalArgumentException("Required watch percent must be between 0 and 100.");
        }

        this.requiredWatchPercent = requiredWatchPercent;
    }

    public double getRequiredWatchPercent() {
        return requiredWatchPercent;
    }

    @Override
    public boolean isComplete(CompletionEvidence evidence) {
        if (!(evidence instanceof VideoEvidence)) {
            return false;
        }

        VideoEvidence videoEvidence = (VideoEvidence) evidence;
        return videoEvidence.getWatchedPercent() >= requiredWatchPercent;
    }
}
