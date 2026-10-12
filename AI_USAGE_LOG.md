# AI Usage Log

This file records substantial AI assistance used during the development of the project.

## Entry 1

Date: 04/10/2026

Purpose: Week 1 requirement analysis and planning.

AI assistance:
- Helped organize the project requirements.
- Suggested an initial system flow.
- Suggested an initial mapping between requirements and OOP principles.
- Suggested test and evidence categories for discussion.

Human review:
- The team checked the suggestions against the original project requirements.
- Kha and Trang revised the system flow and class responsibilities.
- Trang revised the preliminary class diagram.
- The final project scope and design decisions were reviewed and agreed on by the team.

## Entry 2

Date: 11/10/2026

Tool:
ChatGPT / Codex

Purpose:
Implement Trang's Week 2 classes: Student, Enrollment and Progress.

AI assistance:
- Helped plan and draft the three classes.
- Suggested additional prerequisite validations such as cycle, self-prerequisite and cross-course prerequisite.

Verification / human review:
- Trang checked the code against the class diagram and DOMAIN_CONTRACT.md.
- The complex prerequisite validations were rejected because the project only requires simple prerequisite locking.
- The classes were compiled successfully before committing.

## Entry 3

Date: 12/10/2026

Tool:
ChatGPT / Codex

Purpose:
Prepare and verify Week 2 unit tests.

AI assistance:
- Helped set up Maven with JUnit 5.
- Initially suggested a 30-test suite including prerequisite, polymorphism, validation and end-to-end scenarios.

Verification / human review:
- Trang reviewed the Week 2 scope and reduced the suite to 9 basic unit tests:
  3 Student tests, 3 Enrollment tests and 3 Progress tests.
- Tests were rerun after integrating Kha's code.

Result:
- Tests run: 9
- Failures: 0
- Errors: 0
- BUILD SUCCESS