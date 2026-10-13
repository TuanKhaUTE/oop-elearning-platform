package elearning.enrollment;

import elearning.course.Course;
import elearning.user.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class EnrollmentTest {
    @Test
    void storesStudent() {
        Student student = new Student("S1", "Trang");
        Course course = new Course("C1", "Java OOP");

        Enrollment enrollment = new Enrollment(student, course);

        assertSame(student, enrollment.getStudent());
    }

    @Test
    void storesCourse() {
        Student student = new Student("S1", "Trang");
        Course course = new Course("C1", "Java OOP");

        Enrollment enrollment = new Enrollment(student, course);

        assertSame(course, enrollment.getCourse());
    }

    @Test
    void createsProgress() {
        Student student = new Student("S1", "Trang");
        Course course = new Course("C1", "Java OOP");

        Enrollment enrollment = new Enrollment(student, course);

        assertNotNull(enrollment.getProgress());
    }
}
