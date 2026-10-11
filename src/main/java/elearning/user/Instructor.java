package elearning.user;

import elearning.course.Course;

public class Instructor {
    private final String id;
    private final String name;

    public Instructor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Course createCourse(String courseId, String title) {
        return new Course(courseId, title);
    }
}
