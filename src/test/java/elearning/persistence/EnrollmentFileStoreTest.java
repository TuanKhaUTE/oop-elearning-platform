package elearning.persistence;

import elearning.course.Course;
import elearning.enrollment.Enrollment;
import elearning.lesson.*;
import elearning.progress.Progress;
import elearning.user.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentFileStoreTest {
    @TempDir
    Path directory;

    private final EnrollmentFileStore store = new EnrollmentFileStore();
    private final Student student = new Student("S1", "Trang");
    private final Course course = new Course("C1", "Java OOP");
    private final VideoLesson video = new VideoLesson("L1", "Video", 80.0);
    private final TextLesson text = new TextLesson("L2", "Reading");
    private final QuizLesson quiz = new QuizLesson("L3", "Quiz", 70.0);
    private final Enrollment enrollment;

    EnrollmentFileStoreTest() {
        text.setPrerequisite(video);
        quiz.setPrerequisite(text);
        course.addLesson(video);
        course.addLesson(text);
        course.addLesson(quiz);
        enrollment = student.enroll(course);
    }

    private Path file() {
        return directory.resolve("enrollment.txt");
    }

    private void save(Enrollment value) throws IOException {
        try {
            store.save(file(), value);
        } catch (AtomicMoveNotSupportedException exception) {
            fail("Environment limitation: @TempDir filesystem does not support required atomic moves.", exception);
        }
    }

    private void completeTwoLessons() {
        assertTrue(enrollment.getProgress().updateLessonProgress(video, new VideoEvidence(80.0)));
        assertTrue(enrollment.getProgress().updateLessonProgress(text, new TextEvidence(true)));
    }

    private void write(String content) throws IOException {
        Files.writeString(file(), content, StandardCharsets.UTF_8);
    }

    private String snapshot(String entries, int count) {
        return "version=1\nstudent=S1\ncourse=C1\ncompleted=" + count + "\n" + entries;
    }

    @Test
    void roundTripWithNoCompletions() throws IOException {
        save(enrollment);
        Enrollment loaded = store.load(file(), student, course);
        assertNotSame(enrollment, loaded);
        assertNotSame(enrollment.getProgress(), loaded.getProgress());
        assertEquals(0.0, loaded.getProgress().getOverallPercentage(), 0.001);
        assertFalse(loaded.getProgress().isCertificateEligible());
        for (Lesson lesson : course.getLessons()) {
            assertFalse(loaded.getProgress().isLessonCompleted(lesson));
        }
    }

    @Test
    void roundTripWithTwoOfThreeCompleted() throws IOException {
        completeTwoLessons();
        save(enrollment);
        Progress loaded = store.load(file(), student, course).getProgress();
        assertTrue(loaded.isLessonCompleted(video));
        assertTrue(loaded.isLessonCompleted(text));
        assertFalse(loaded.isLessonCompleted(quiz));
        assertEquals(200.0 / 3.0, loaded.getOverallPercentage(), 0.001);
        assertFalse(loaded.isCertificateEligible());
    }

    @Test
    void roundTripWithAllLessonsCompleted() throws IOException {
        completeTwoLessons();
        assertTrue(enrollment.getProgress().updateLessonProgress(quiz, new QuizEvidence(70.0)));
        save(enrollment);
        Progress loaded = store.load(file(), student, course).getProgress();
        for (Lesson lesson : course.getLessons()) {
            assertTrue(loaded.isLessonCompleted(lesson));
        }
        assertEquals(100.0, loaded.getOverallPercentage(), 0.001);
        assertTrue(loaded.isCertificateEligible());
    }

    @Test
    void restartUsesNewStudentCourseAndLessonInstances() throws IOException {
        completeTwoLessons();
        save(enrollment);
        Student newStudent = new Student("S1", "Trang after restart");
        Course newCourse = new Course("C1", "Reloaded course");
        VideoLesson newVideo = new VideoLesson("L1", "Video", 80.0);
        TextLesson newText = new TextLesson("L2", "Reading");
        QuizLesson newQuiz = new QuizLesson("L3", "Quiz", 70.0);
        newText.setPrerequisite(newVideo);
        newQuiz.setPrerequisite(newText);
        newCourse.addLesson(newVideo);
        newCourse.addLesson(newText);
        newCourse.addLesson(newQuiz);

        Enrollment loaded = store.load(file(), newStudent, newCourse);

        assertSame(newStudent, loaded.getStudent());
        assertSame(newCourse, loaded.getCourse());
        assertTrue(loaded.getProgress().isLessonCompleted(newVideo));
        assertTrue(loaded.getProgress().isLessonCompleted(newText));
        assertFalse(loaded.getProgress().isLessonCompleted(newQuiz));
        assertFalse(loaded.getProgress().isLessonCompleted(video));
        assertFalse(loaded.getProgress().isLessonCompleted(text));
        assertTrue(loaded.getProgress().isLessonUnlocked(newQuiz));
        assertEquals(200.0 / 3.0, loaded.getProgress().getOverallPercentage(), 0.001);
        assertFalse(loaded.getProgress().isCertificateEligible());
    }

    @Test
    void savesOnlyDocumentedFieldsInCourseOrderAsUtf8() throws IOException {
        Student unicodeStudent = new Student("SV-Trang-ệ", "Trang");
        Course unicodeCourse = new Course("KH-học", "Course title must not be saved");
        TextLesson first = new TextLesson("Bài-2", "First");
        TextLesson second = new TextLesson("Bài-1", "Second");
        unicodeCourse.addLesson(first);
        unicodeCourse.addLesson(second);
        Enrollment value = unicodeStudent.enroll(unicodeCourse);
        value.getProgress().updateLessonProgress(second, new TextEvidence(true));
        value.getProgress().updateLessonProgress(first, new TextEvidence(true));
        save(value);
        String expected = "version=1\nstudent=SV-Trang-ệ\ncourse=KH-học\ncompleted=2\n"
                + "lesson=Bài-2\nlesson=Bài-1\n";
        assertArrayEquals(expected.getBytes(StandardCharsets.UTF_8), Files.readAllBytes(file()));
        assertTrue(store.load(file(), unicodeStudent, unicodeCourse).getProgress().isCertificateEligible());
    }

    @Test
    void duplicateCompletionSavesOnlyOneEntry() throws IOException {
        enrollment.getProgress().updateLessonProgress(video, new VideoEvidence(80.0));
        enrollment.getProgress().updateLessonProgress(video, new VideoEvidence(90.0));
        save(enrollment);
        assertEquals(snapshot("lesson=L1\n", 1), Files.readString(file(), StandardCharsets.UTF_8));
    }

    @Test
    void rejectsWrongStudentId() throws IOException {
        save(enrollment);
        assertThrows(IOException.class, () -> store.load(file(), new Student("S2", "Other"), course));
    }

    @Test
    void rejectsWrongCourseId() throws IOException {
        save(enrollment);
        assertThrows(IOException.class, () -> store.load(file(), student, new Course("C2", "Other")));
    }

    @Test
    void rejectsUnknownLessonId() throws IOException {
        write(snapshot("lesson=unknown\n", 1));
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @Test
    void rejectsDuplicateSavedLessonId() throws IOException {
        write(snapshot("lesson=L1\nlesson=L1\n", 2));
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @Test
    void rejectsCompletedDependentWithoutPrerequisite() throws IOException {
        write(snapshot("lesson=L2\n", 1));
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @Test
    void acceptsReversedPrerequisiteOrder() throws IOException {
        write(snapshot("lesson=L3\nlesson=L2\nlesson=L1\n", 3));
        Progress loaded = store.load(file(), student, course).getProgress();
        assertTrue(loaded.isLessonCompleted(video));
        assertTrue(loaded.isLessonCompleted(text));
        assertTrue(loaded.isLessonCompleted(quiz));
        assertTrue(loaded.isCertificateEligible());
    }

    @Test
    void rejectsUnsupportedVersion() throws IOException {
        write("version=2\nstudent=S1\ncourse=C1\ncompleted=0\n");
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", "version=1\n", "student=S1\ncourse=C1\ncompleted=0\n",
            "version:1\nstudent=S1\ncourse=C1\ncompleted=0\n",
            "version=1\ncourse=C1\nstudent=S1\ncompleted=0\n",
            "version=1\nstudent=S1\nstudent=S1\ncompleted=0\n",
            "version=1\nstudent=S1\ncourse=C1\n",
            "version=1\nstudent=\ncourse=C1\ncompleted=0\n",
            "version=1\nstudent=S1\ncourse= C1\ncompleted=0\n"
    })
    void rejectsMissingOrMalformedHeaders(String content) throws IOException {
        write(content);
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "+1", "1.5", "abc", "", " 1", "2147483648", "4"})
    void rejectsInvalidCount(String count) throws IOException {
        write("version=1\nstudent=S1\ncourse=C1\ncompleted=" + count + "\n");
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "completed=2\nlesson=L1\n", "completed=1\n",
            "completed=0\nlesson=L1\n", "completed=1\nlesson=L1\nextra=value\n",
            "completed=0\n\n", "completed=1\nlesson:L1\n",
            "completed=1\nlesson=\n", "completed=1\nlesson= L1\n"
    })
    void rejectsTruncationCountMismatchAndUnexpectedEntries(String body) throws IOException {
        write("version=1\nstudent=S1\ncourse=C1\n" + body);
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\n", "\r\n"})
    void acceptsLfAndCrlf(String newline) throws IOException {
        write(snapshot("lesson=L1\nlesson=L2\n", 2).replace("\n", newline));
        Progress loaded = store.load(file(), student, course).getProgress();
        assertTrue(loaded.isLessonCompleted(video));
        assertTrue(loaded.isLessonCompleted(text));
        assertEquals(200.0 / 3.0, loaded.getOverallPercentage(), 0.001);
    }

    @Test
    void rejectsMissingFile() {
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }

    @Test
    void invalidSaveInputPreservesDestination() throws IOException {
        byte[] original = "existing destination".getBytes(StandardCharsets.UTF_8);
        Files.write(file(), original);
        assertThrows(IllegalArgumentException.class, () -> store.save(file(), null));
        assertArrayEquals(original, Files.readAllBytes(file()));
        Enrollment invalid = new Student(" bad", "Invalid ID").enroll(course);
        assertThrows(IllegalArgumentException.class, () -> store.save(file(), invalid));
        assertArrayEquals(original, Files.readAllBytes(file()));
        course.addLesson(new TextLesson("L1", "Duplicate"));
        assertThrows(IllegalArgumentException.class, () -> store.save(file(), enrollment));
        assertArrayEquals(original, Files.readAllBytes(file()));
    }

    @Test
    void inconsistentSnapshotPreservesDestination() throws IOException {
        completeTwoLessons();
        byte[] original = "previous save".getBytes(StandardCharsets.UTF_8);
        Files.write(file(), original);
        text.setPrerequisite(quiz);
        assertThrows(IllegalArgumentException.class, () -> store.save(file(), enrollment));
        assertArrayEquals(original, Files.readAllBytes(file()));
    }

    @Test
    void failedLoadDoesNotMutateOriginalEnrollmentOrProgress() throws IOException {
        completeTwoLessons();
        Progress original = enrollment.getProgress();
        write(snapshot("lesson=L1\nlesson=unknown\n", 2));
        assertThrows(IOException.class, () -> store.load(file(), student, course));
        assertSame(original, enrollment.getProgress());
        assertSame(student, enrollment.getStudent());
        assertSame(course, enrollment.getCourse());
        assertTrue(original.isLessonCompleted(video));
        assertTrue(original.isLessonCompleted(text));
        assertFalse(original.isLessonCompleted(quiz));
        assertEquals(200.0 / 3.0, original.getOverallPercentage(), 0.001);
        assertFalse(original.isCertificateEligible());
    }

    @Test
    void repeatedSaveIsDeterministicAndReplacesExistingFile() throws IOException {
        completeTwoLessons();
        save(enrollment);
        byte[] first = Files.readAllBytes(file());
        save(enrollment);
        assertArrayEquals(first, Files.readAllBytes(file()));
    }

    @Test
    void successfulSavesLeaveNoTemporaryFiles() throws IOException {
        save(enrollment);
        completeTwoLessons();
        save(enrollment);
        try (var files = Files.list(directory)) {
            assertEquals(List.of(file()), files.toList());
        }
        assertEquals(200.0 / 3.0,
                store.load(file(), student, course).getProgress().getOverallPercentage(), 0.001);
    }

    @Test
    void invalidCallerArgumentsAreNotReportedAsCorruptFiles() {
        assertThrows(IllegalArgumentException.class, () -> store.save(null, enrollment));
        assertThrows(IllegalArgumentException.class, () -> store.load(null, student, course));
        assertThrows(IllegalArgumentException.class, () -> store.load(file(), null, course));
        assertThrows(IllegalArgumentException.class, () -> store.load(file(), student, null));
        course.addLesson(new TextLesson("L1", "Duplicate"));
        assertThrows(IllegalArgumentException.class, () -> store.load(file(), student, course));
    }

    @Test
    void rejectsMalformedUtf8() throws IOException {
        Files.write(file(), new byte[] {(byte) 0xC3, (byte) 0x28});
        assertThrows(IOException.class, () -> store.load(file(), student, course));
    }
}
