package elearning.lesson;

public class AssignmentLesson extends Lesson {
    private final int minimumWordCount;

    public AssignmentLesson(
            String id,
            String title,
            int minimumWordCount
    ) {
        super(id, title);

        if (minimumWordCount <= 0) {
            throw new IllegalArgumentException(
                    "Minimum word count must be greater than zero."
            );
        }

        this.minimumWordCount = minimumWordCount;
    }

    public int getMinimumWordCount() {
        return minimumWordCount;
    }

    @Override
    public boolean isComplete(CompletionEvidence evidence) {
        if (!(evidence instanceof AssignmentEvidence)) {
            return false;
        }

        AssignmentEvidence assignmentEvidence =
                (AssignmentEvidence) evidence;

        return assignmentEvidence.getSubmittedWordCount()
                >= minimumWordCount;
    }
}