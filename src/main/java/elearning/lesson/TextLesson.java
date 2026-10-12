package elearning.lesson;

public class TextLesson extends Lesson {
    public TextLesson(String id, String title) {
        super(id, title);
    }

    @Override
    public boolean isComplete(CompletionEvidence evidence) {
        if (!(evidence instanceof TextEvidence)) {
            return false;
        }

        TextEvidence textEvidence = (TextEvidence) evidence;
        return textEvidence.isCompletedReading();
    }
}
