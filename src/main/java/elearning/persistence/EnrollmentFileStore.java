package elearning.persistence;

import elearning.course.Course;
import elearning.enrollment.Enrollment;
import elearning.lesson.Lesson;
import elearning.user.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Stores one enrollment as a versioned UTF-8 snapshot. */
public class EnrollmentFileStore {
    public void save(Path path, Enrollment enrollment) throws IOException {
        if (path == null || enrollment == null) {
            throw new IllegalArgumentException("Path and enrollment must not be null.");
        }
        Student student = enrollment.getStudent();
        Course course = enrollment.getCourse();
        validateCaller(student, course);
        if (enrollment.getProgress() == null) {
            throw new IllegalArgumentException("Enrollment progress must not be null.");
        }

        List<String> completedIds = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            if (enrollment.getProgress().isLessonCompleted(lesson)) {
                completedIds.add(lesson.getId());
            }
        }
        // Validate the entire snapshot before creating or replacing any file.
        Enrollment.restore(student, course, completedIds);
        StringBuilder content = new StringBuilder("version=1\n");
        content.append("student=").append(student.getStudentId()).append('\n');
        content.append("course=").append(course.getId()).append('\n');
        content.append("completed=").append(completedIds.size()).append('\n');
        for (String id : completedIds) {
            content.append("lesson=").append(id).append('\n');
        }

        Path target = path.toAbsolutePath();
        Path temporary = Files.createTempFile(target.getParent(), "enrollment-", ".tmp");
        try {
            Files.writeString(temporary, content, StandardCharsets.UTF_8);
            Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | RuntimeException failure) {
            try {
                Files.deleteIfExists(temporary);
            } catch (IOException | RuntimeException cleanupFailure) {
                failure.addSuppressed(cleanupFailure);
            }
            throw failure;
        }
    }

    public Enrollment load(Path path, Student student, Course course) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        validateCaller(student, course);
        List<String> completedIds = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String version = readField(reader, "version");
            if (!version.equals("1")) {
                throw new IOException("Unsupported enrollment format version: " + version);
            }
            String studentId = readIdentifier(reader, "student");
            String courseId = readIdentifier(reader, "course");
            if (!studentId.equals(student.getStudentId())) {
                throw new IOException("Saved student ID does not match supplied student.");
            }
            if (!courseId.equals(course.getId())) {
                throw new IOException("Saved course ID does not match supplied course.");
            }
            String countText = readField(reader, "completed");
            if (!countText.matches("[0-9]+")) {
                throw new IOException("Completed count must be a nonnegative decimal integer.");
            }
            int count;
            try {
                count = Integer.parseInt(countText);
            } catch (NumberFormatException exception) {
                throw new IOException("Completed count exceeds the supported integer range.", exception);
            }
            if (count > course.getLessons().size()) {
                throw new IOException("Completed count exceeds the number of course lessons.");
            }
            for (int i = 0; i < count; i++) {
                completedIds.add(readIdentifier(reader, "lesson"));
            }
            if (reader.readLine() != null) {
                throw new IOException("Unexpected content after the declared lesson entries.");
            }
        }
        try {
            return Enrollment.restore(student, course, completedIds);
        } catch (IllegalArgumentException exception) {
            throw new IOException("Invalid saved completion state: " + exception.getMessage(), exception);
        }
    }

    private static void validateCaller(Student student, Course course) {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course must not be null.");
        }
        requireIdentifier(student.getStudentId(), "Student ID");
        requireIdentifier(course.getId(), "Course ID");
        Set<String> lessonIds = new HashSet<>();
        for (Lesson lesson : course.getLessons()) {
            requireIdentifier(lesson.getId(), "Lesson ID");
            if (!lessonIds.add(lesson.getId())) {
                throw new IllegalArgumentException("Duplicate course lesson ID: " + lesson.getId());
            }
        }
    }

    private static void requireIdentifier(String id, String field) {
        if (id == null || id.isBlank() || !id.equals(id.strip())
                || id.contains("\r") || id.contains("\n")) {
            throw new IllegalArgumentException(field
                    + " must be nonblank without surrounding whitespace or line breaks.");
        }
    }

    private static String readField(BufferedReader reader, String field) throws IOException {
        String line = reader.readLine();
        String prefix = field + "=";
        if (line == null || !line.startsWith(prefix)) {
            throw new IOException("Expected " + field + "= entry.");
        }
        return line.substring(prefix.length());
    }

    private static String readIdentifier(BufferedReader reader, String field) throws IOException {
        String id = readField(reader, field);
        try {
            requireIdentifier(id, field);
        } catch (IllegalArgumentException exception) {
            throw new IOException("Invalid saved identifier: " + exception.getMessage(), exception);
        }
        return id;
    }
}
