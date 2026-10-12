package elearning.user;

import elearning.course.Course;
import elearning.enrollment.Enrollment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class StudentTest {
    @Test
    void constructorStoresStudentId() {
        Student student = new Student("S1", "Trang");

        assertEquals("S1", student.getStudentId());
    }

    @Test
    void constructorStoresName() {
        Student student = new Student("S1", "Trang");

        assertEquals("Trang", student.getName());
    }

    @Test
    void enrollReturnsEnrollmentForStudentAndCourse() {
        Student student = new Student("S1", "Trang");
        Course course = new Course("C1", "Java OOP");

        Enrollment enrollment = student.enroll(course);

        assertNotNull(enrollment);
        assertSame(student, enrollment.getStudent());
        assertSame(course, enrollment.getCourse());
    }
}
