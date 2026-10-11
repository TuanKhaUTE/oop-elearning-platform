package elearning.progress;

import elearning.course.Course;
import elearning.lesson.CompletionEvidence;
import elearning.lesson.Lesson;

import java.util.HashSet;
import java.util.Set;

public class Progress {
    private Course course;
    private Set<Lesson> completedLessons;

    public Progress(Course course) {
        this.course = course;
        this.completedLessons = new HashSet<>();
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
        for (Lesson lesson : course.getLessons()) {
            if (!completedLessons.contains(lesson)) {
                return false;
            }
        }

        return true;
    }
}
