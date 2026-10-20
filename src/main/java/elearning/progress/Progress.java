package elearning.progress;

import elearning.course.Course;
import elearning.lesson.CompletionEvidence;
import elearning.lesson.Lesson;

import java.util.HashSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class Progress {
    private Course course;
    private Set<Lesson> completedLessons;

    public Progress(Course course) {
        this.course = course;
        this.completedLessons = new HashSet<>();
    }

    /** Restores validated historical completion, without inventing evidence. */
    public static Progress restore(Course course, Collection<String> completedLessonIds) {
        if (course == null || completedLessonIds == null) {
            throw new IllegalArgumentException("Course and completed lesson IDs must not be null.");
        }
        Map<String, Lesson> lessonsById = new HashMap<>();
        for (Lesson lesson : course.getLessons()) {
            requireValidLessonId(lesson.getId());
            if (lessonsById.putIfAbsent(lesson.getId(), lesson) != null) {
                throw new IllegalArgumentException("Duplicate course lesson ID: " + lesson.getId());
            }
        }

        Set<String> seenIds = new HashSet<>();
        Set<Lesson> pending = new HashSet<>();
        for (String id : completedLessonIds) {
            requireValidLessonId(id);
            if (!seenIds.add(id)) {
                throw new IllegalArgumentException("Duplicate completed lesson ID: " + id);
            }
            Lesson lesson = lessonsById.get(id);
            if (lesson == null) {
                throw new IllegalArgumentException("Unknown completed lesson ID: " + id);
            }
            pending.add(lesson);
        }

        Set<Lesson> validated = new HashSet<>();
        while (!pending.isEmpty()) {
            boolean advanced = false;
            Iterator<Lesson> iterator = pending.iterator();
            while (iterator.hasNext()) {
                Lesson lesson = iterator.next();
                Lesson prerequisite = lesson.getPrerequisite();
                if (prerequisite == null || validated.contains(prerequisite)) {
                    validated.add(lesson);
                    iterator.remove();
                    advanced = true;
                }
            }
            if (!advanced) {
                throw new IllegalArgumentException("Completed lessons have missing or invalid prerequisites.");
            }
        }

        Progress progress = new Progress(course);
        progress.completedLessons.addAll(validated);
        return progress;
    }

    private static void requireValidLessonId(String id) {
        if (id == null || id.isBlank() || !id.equals(id.strip())
                || id.contains("\r") || id.contains("\n")) {
            throw new IllegalArgumentException("Lesson ID must be nonblank without surrounding whitespace or line breaks.");
        }
    }

    public boolean isLessonCompleted(Lesson lesson) {
        return completedLessons.contains(lesson);
    }

    public boolean isLessonUnlocked(Lesson lesson) {
        Lesson prerequisite = lesson.getPrerequisite();
        return prerequisite == null || completedLessons.contains(prerequisite);
    }

    public boolean updateLessonProgress(Lesson lesson, CompletionEvidence evidence) {
        if (!course.getLessons().contains(lesson)) {
            throw new IllegalArgumentException("Lesson does not belong to this course.");
        }

        if (!isLessonUnlocked(lesson)) {
            throw new IllegalStateException("Lesson prerequisite has not been completed.");
        }

        if (!lesson.isComplete(evidence)) {
            return false;
        }

        completedLessons.add(lesson);
        return true;
    }

    public double getOverallPercentage() {
        int totalLessons = course.getLessons().size();
        if (totalLessons == 0) {
            return 0.0;
        }

        int completedCount = 0;
        for (Lesson lesson : course.getLessons()) {
            if (completedLessons.contains(lesson)) {
                completedCount++;
            }
        }

        return completedCount * 100.0 / totalLessons;
    }

    public boolean isCertificateEligible() {
        if (course.getLessons().isEmpty()) {
            return false;
        }

        for (Lesson lesson : course.getLessons()) {
            if (!completedLessons.contains(lesson)) {
                return false;
            }
        }

        return true;
    }
}
