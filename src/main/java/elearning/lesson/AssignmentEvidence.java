package elearning.lesson;

public class AssignmentEvidence implements CompletionEvidence {
    private final int submittedWordCount;

    public AssignmentEvidence(int submittedWordCount) {
        if (submittedWordCount < 0) {
            throw new IllegalArgumentException(
                    "Submitted word count must not be negative."
            );
        }

        this.submittedWordCount = submittedWordCount;
    }

    public int getSubmittedWordCount() {
        return submittedWordCount;
    }
}