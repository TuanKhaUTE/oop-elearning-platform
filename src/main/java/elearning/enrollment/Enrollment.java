package elearning.enrollment;

import elearning.course.Course;
import elearning.progress.Progress;
import elearning.user.Student;
import java.util.Collection;

public class Enrollment {
    private Student student;
    private Course course;
    private Progress progress;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.progress = new Progress(course);
    }

    private Enrollment(Student student, Course course, Progress progress) {
        this.student = student;
        this.course = course;
        this.progress = progress;
    }

    /** Creates a new enrollment only after historical progress is validated. */
    public static Enrollment restore(Student student, Course course,
                                     Collection<String> completedLessonIds) {
        if (student == null) {
            throw new IllegalArgumentException("Student must not be null.");
        }
        Progress progress = Progress.restore(course, completedLessonIds);
        return new Enrollment(student, course, progress);
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
