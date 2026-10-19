package elearning.lesson;

public class VideoLesson extends Lesson {
    private final double requiredWatchPercent;

    public VideoLesson(String id, String title, double requiredWatchPercent) {
        super(id, title);

        PercentageValidator.requireValid(
                requiredWatchPercent,
                "Required watch percent");

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