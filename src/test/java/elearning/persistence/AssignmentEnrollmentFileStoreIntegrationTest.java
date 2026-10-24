package elearning.persistence;

import elearning.course.Course;
import elearning.enrollment.Enrollment;
import elearning.lesson.*;
import elearning.progress.Progress;
import elearning.user.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentEnrollmentFileStoreIntegrationTest {
    @TempDir
    Path directory;

    private final EnrollmentFileStore store = new EnrollmentFileStore();

    @Test
    void unfinishedAssignmentRemainsIncompleteAfterReloadAndCanBeCompleted() throws IOException {
        Student student = new Student("S1", "Trang");
        Course course = createCourse();
        Enrollment original = student.enroll(course);
        Progress progress = original.getProgress();
        completeVideoAndText(original);
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);
        assertFalse(progress.updateLessonProgress(
                course.getLessons().get(2), new AssignmentEvidence(499)));
        assertEquals(50.0, progress.getOverallPercentage(), 0.001);

        Path file = directory.resolve("enrollment.txt");
        store.save(file, original);
        Student newStudent = new Student("S1", "Trang after restart");
        Course newCourse = createCourse();
        assertNewObjects(student, course, newStudent, newCourse);
        Enrollment loaded = store.load(file, newStudent, newCourse);
        assertNotSame(original, loaded);
        Progress restored = loaded.getProgress();
        Lesson newAssignment = newCourse.getLessons().get(2);
        Lesson newQuiz = newCourse.getLessons().get(3);

        assertTrue(restored.isLessonCompleted(newCourse.getLessons().get(0)));
        assertTrue(restored.isLessonCompleted(newCourse.getLessons().get(1)));
        assertFalse(restored.isLessonCompleted(newAssignment));
        assertTrue(restored.isLessonUnlocked(newAssignment));
        assertFalse(restored.isLessonCompleted(newQuiz));
        assertFalse(restored.isLessonUnlocked(newQuiz));
        assertEquals(50.0, restored.getOverallPercentage(), 0.001);
        assertFalse(restored.isCertificateEligible());

        assertTrue(restored.updateLessonProgress(newAssignment, new AssignmentEvidence(500)));
        assertTrue(restored.isLessonUnlocked(newQuiz));
        assertTrue(restored.updateLessonProgress(newQuiz, new QuizEvidence(80.0)));
        assertEquals(100.0, restored.getOverallPercentage(), 0.001);
        assertTrue(restored.isCertificateEligible());
    }

    @Test
    void completedAssignmentRestoresOntoNewObjectsAndUnlocksNextLesson() throws IOException {
        Student student = new Student("S1", "Trang");
        Course course = createCourse();
        Enrollment original = student.enroll(course);
        completeVideoAndText(original);
        Lesson originalAssignment = course.getLessons().get(2);
        assertTrue(original.getProgress().updateLessonProgress(
                originalAssignment, new AssignmentEvidence(500)));
        assertEquals(75.0, original.getProgress().getOverallPercentage(), 0.001);

        Path file = directory.resolve("enrollment.txt");
        store.save(file, original);
        Student newStudent = new Student("S1", "Trang after restart");
        Course newCourse = createCourse();
        assertNewObjects(student, course, newStudent, newCourse);
        Enrollment loaded = store.load(file, newStudent, newCourse);
        Progress restored = loaded.getProgress();
        Lesson newAssignment = newCourse.getLessons().get(2);
        Lesson newQuiz = newCourse.getLessons().get(3);

        assertNotSame(original, loaded);
        assertNotSame(original.getProgress(), restored);
        assertSame(newStudent, loaded.getStudent());
        assertSame(newCourse, loaded.getCourse());
        assertTrue(restored.isLessonCompleted(newAssignment));
        assertFalse(restored.isLessonCompleted(originalAssignment));
        assertFalse(restored.isLessonCompleted(newQuiz));
        assertTrue(restored.isLessonUnlocked(newQuiz));
        assertEquals(75.0, restored.getOverallPercentage(), 0.001);
        assertFalse(restored.isCertificateEligible());

        assertTrue(restored.updateLessonProgress(newQuiz, new QuizEvidence(80.0)));
        assertEquals(100.0, restored.getOverallPercentage(), 0.001);
        assertTrue(restored.isCertificateEligible());
        assertEquals(75.0, original.getProgress().getOverallPercentage(), 0.001);
        assertFalse(original.getProgress().isLessonCompleted(course.getLessons().get(3)));
        assertFalse(original.getProgress().isCertificateEligible());
    }

    @Test
    void fullyCompletedMixedCourseRetainsCertificateAfterReload() throws IOException {
        Student student = new Student("S1", "Trang");
        Course course = createCourse();
        Enrollment original = student.enroll(course);
        completeVideoAndText(original);
        assertTrue(original.getProgress().updateLessonProgress(
                course.getLessons().get(2), new AssignmentEvidence(500)));
        assertTrue(original.getProgress().updateLessonProgress(
                course.getLessons().get(3), new QuizEvidence(80.0)));
        assertEquals(100.0, original.getProgress().getOverallPercentage(), 0.001);
        assertTrue(original.getProgress().isCertificateEligible());

        Path file = directory.resolve("enrollment.txt");
        store.save(file, original);
        // The complete snapshot contains IDs only, including Assignment's L3.
        assertEquals("version=1\nstudent=S1\ncourse=C1\ncompleted=4\n"
                        + "lesson=L1\nlesson=L2\nlesson=L3\nlesson=L4\n",
                Files.readString(file, StandardCharsets.UTF_8));

        Student newStudent = new Student("S1", "Trang after restart");
        Course newCourse = createCourse();
        assertNewObjects(student, course, newStudent, newCourse);
        Enrollment loaded = store.load(file, newStudent, newCourse);
        assertNotSame(original, loaded);
        assertNotSame(original.getProgress(), loaded.getProgress());
        assertSame(newStudent, loaded.getStudent());
        assertSame(newCourse, loaded.getCourse());
        for (Lesson lesson : newCourse.getLessons()) {
            assertTrue(loaded.getProgress().isLessonCompleted(lesson));
        }
        assertEquals(100.0, loaded.getProgress().getOverallPercentage(), 0.001);
        assertTrue(loaded.getProgress().isCertificateEligible());
    }

    private Course createCourse() {
        Course course = new Course("C1", "Java OOP");
        Lesson video = new VideoLesson("L1", "Introduction Video", 80.0);
        Lesson text = new TextLesson("L2", "OOP Reading");
        Lesson assignment = new AssignmentLesson("L3", "OOP Reflection", 500);
        Lesson quiz = new QuizLesson("L4", "OOP Quiz", 70.0);
        text.setPrerequisite(video);
        assignment.setPrerequisite(text);
        quiz.setPrerequisite(assignment);
        course.addLesson(video);
        course.addLesson(text);
        course.addLesson(assignment);
        course.addLesson(quiz);
        return course;
    }

    private void completeVideoAndText(Enrollment enrollment) {
        List<Lesson> lessons = enrollment.getCourse().getLessons();
        assertTrue(enrollment.getProgress().updateLessonProgress(lessons.get(0), new VideoEvidence(85.0)));
        assertTrue(enrollment.getProgress().updateLessonProgress(lessons.get(1), new TextEvidence(true)));
    }

    private void assertNewObjects(Student originalStudent, Course originalCourse,
                                  Student newStudent, Course newCourse) {
        assertNotSame(originalStudent, newStudent);
        assertNotSame(originalCourse, newCourse);
        for (int i = 0; i < originalCourse.getLessons().size(); i++) {
            assertNotSame(originalCourse.getLessons().get(i), newCourse.getLessons().get(i));
        }
    }
}
