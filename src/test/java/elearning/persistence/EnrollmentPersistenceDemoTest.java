package elearning.persistence;

import elearning.course.Course;
import elearning.enrollment.Enrollment;
import elearning.lesson.*;
import elearning.user.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentPersistenceDemoTest {
    @TempDir
    Path directory;

    @Test
    void demonstratesRecoveryWithNewObjectsFromSavedFile() throws IOException {
        Student originalStudent = new Student("S1", "Trang");
        Course originalCourse = createCourse();
        Enrollment original = originalStudent.enroll(originalCourse);
        List<Lesson> originalLessons = originalCourse.getLessons();
        assertTrue(original.getProgress().updateLessonProgress(
                originalLessons.get(0), new VideoEvidence(85.0)));
        assertTrue(original.getProgress().updateLessonProgress(
                originalLessons.get(1), new TextEvidence(true)));

        System.out.println("=== BEFORE SAVE ===");
        System.out.println("Student ID: " + original.getStudent().getStudentId());
        System.out.println("Course ID: " + original.getCourse().getId());
        System.out.println("Completed lesson IDs: " + originalLessons.stream()
                .filter(original.getProgress()::isLessonCompleted)
                .map(Lesson::getId)
                .toList());
        System.out.printf(Locale.ROOT, "Overall progress: %.2f%%%n",
                original.getProgress().getOverallPercentage());
        System.out.println("certificateEligible=" + original.getProgress().isCertificateEligible());

        Path file = directory.resolve("enrollment.txt");
        EnrollmentFileStore store = new EnrollmentFileStore();
        store.save(file, original);
        String saved = Files.readString(file, StandardCharsets.UTF_8);
        assertEquals("version=1\nstudent=S1\ncourse=C1\ncompleted=2\nlesson=L1\nlesson=L2\n", saved);
        System.out.println("=== SAVED FILE CONTENT (UTF-8, exact contents) ===");
        System.out.print(saved);
        System.out.println("End of saved file.");

        Student newStudent = new Student("S1", "Trang");
        Course newCourse = createCourse();
        assertNotSame(originalStudent, newStudent);
        assertNotSame(originalCourse, newCourse);
        for (int i = 0; i < originalLessons.size(); i++) {
            assertNotSame(originalLessons.get(i), newCourse.getLessons().get(i));
        }
        Enrollment restored = store.load(file, newStudent, newCourse);
        assertNotSame(original, restored);
        assertNotSame(original.getProgress(), restored.getProgress());
        assertSame(newStudent, restored.getStudent());
        assertSame(newCourse, restored.getCourse());

        List<String> completedIds = newCourse.getLessons().stream()
                .filter(restored.getProgress()::isLessonCompleted)
                .map(Lesson::getId)
                .toList();
        assertEquals(List.of("L1", "L2"), completedIds);
        for (Lesson oldLesson : originalLessons) {
            assertFalse(restored.getProgress().isLessonCompleted(oldLesson));
        }
        assertTrue(restored.getProgress().isLessonUnlocked(newCourse.getLessons().get(2)));
        assertEquals(200.0 / 3.0, restored.getProgress().getOverallPercentage(), 0.001);
        assertFalse(restored.getProgress().isCertificateEligible());

        System.out.println("=== AFTER LOAD ===");
        System.out.println("Restored completed lesson IDs: " + completedIds);
        System.out.printf(Locale.ROOT, "Restored progress: %.2f%%%n",
                restored.getProgress().getOverallPercentage());
        System.out.println("certificateEligible=" + restored.getProgress().isCertificateEligible());
        System.out.println("Verified: new Student, Course, Lessons, Enrollment and Progress objects.");
        System.out.println("Verified: completion belongs to new lesson instances, not original instances.");
        System.out.println("SUCCESS: save/load round trip verified; all assertions passed.");
    }

    private Course createCourse() {
        Course course = new Course("C1", "Java OOP");
        VideoLesson video = new VideoLesson("L1", "Introduction", 80.0);
        TextLesson text = new TextLesson("L2", "Reading");
        QuizLesson quiz = new QuizLesson("L3", "Quiz", 70.0);
        text.setPrerequisite(video);
        quiz.setPrerequisite(text);
        course.addLesson(video);
        course.addLesson(text);
        course.addLesson(quiz);
        return course;
    }
}
