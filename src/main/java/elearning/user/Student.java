package elearning.user;

import elearning.course.Course;
import elearning.enrollment.Enrollment;

public class Student {
    private String studentId;
    private String name;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public Enrollment enroll(Course course) {
        return new Enrollment(this, course);
    }
}
