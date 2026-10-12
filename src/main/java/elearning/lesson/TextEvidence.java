package elearning.lesson;

public class TextEvidence implements CompletionEvidence {
    private final boolean completedReading;

    public TextEvidence(boolean completedReading) {
        this.completedReading = completedReading;
    }

    public boolean isCompletedReading() {
        return completedReading;
    }
}
