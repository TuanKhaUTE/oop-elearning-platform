package elearning.lesson;

public abstract class Lesson {
    private final String id;
    private final String title;
    private Lesson prerequisite;

    public Lesson(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Lesson getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(Lesson prerequisite) {
        this.prerequisite = prerequisite;
    }

    public abstract boolean isComplete(CompletionEvidence evidence);
}
