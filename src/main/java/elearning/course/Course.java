package elearning.course;

import elearning.lesson.Lesson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Course {
    private final String id;
    private final String title;
    private final List<Lesson> lessons;

    public Course(String id, String title) {
        this.id = id;
        this.title = title;
        this.lessons = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void addLesson(Lesson lesson) {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson must not be null.");
        }

        lessons.add(lesson);
    }

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }
}
