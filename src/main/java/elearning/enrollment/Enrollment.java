package elearning.enrollment;

import elearning.course.Course;
import elearning.progress.Progress;
import elearning.user.Student;

public class Enrollment {
    private Student student;
    private Course course;
    private Progress progress;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.progress = new Progress(course);
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Progress getProgress() {
        return progress;
    }
}
