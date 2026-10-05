# E-Learning Platform

Final project for the Object-Oriented Programming course.

## Team

Trần Tuấn Kha - 23110028  
Mai Trần Thùy Trang - 23110065

Lecturer: Huỳnh Xuân Phụng

## Project Overview

This project is a Java-based E-Learning Platform that models courses, lesson enrollment, student progress, lesson prerequisites, and course completion.

The main focus of the project is applying Object-Oriented Programming principles clearly through the system design and implementation.

## Core Requirements

The system supports courses containing ordered lessons.

Lesson is the base type with three required subtypes:

- VideoLesson
- TextLesson
- QuizLesson

Students can enroll in courses and their progress is tracked for each enrollment.

Each lesson type has its own completion rule through polymorphic `isComplete(...)` behavior.

Lessons may have prerequisites and can only be completed when their prerequisite requirements are satisfied.

A student becomes eligible for a course completion certificate when all lessons in the course are completed.

## OOP Principles

### Polymorphism

`Lesson.isComplete(...)` is implemented differently by `VideoLesson`, `TextLesson`, and `QuizLesson`.

### Encapsulation

`Progress` controls how lesson completion is recorded and prevents external code from directly modifying completion state.

### Composition

`Course` contains an ordered collection of `Lesson` objects.

## Technology

Java 17

Initial version: Java console application

Testing will be added for the required happy path, validation/error scenario, and polymorphism scenario.

## Project Structure

`course` - Course domain logic

`lesson` - Lesson hierarchy and polymorphic completion rules

`user` - Student and Instructor models

`enrollment` - Student-Course enrollment

`progress` - Completion tracking, prerequisite checking, percentage calculation and certificate eligibility

`app` - Application entry point

`docs` - Diagrams, reports and test evidence

## Git Workflow

The `main` branch contains reviewed and working code.

Feature development is performed on separate branches.

Kha:

`feature/course-lesson`

Trang:

`feature/enrollment-progress`

Before starting work, pull the latest version of `main`.

Do not push unfinished feature work directly to `main`.

Keep commits small and focused on one meaningful change.

The other member should review changes before a feature is merged.

Both members must understand the complete system for the final oral defense.

## Current Status

Week 1 completed:

Requirements analyzed.

Main system flow defined.

Initial class responsibilities defined.

Preliminary class diagram created.

Test and evidence plan prepared.

Week 2 will focus on implementing the shared domain foundation and core OOP logic.