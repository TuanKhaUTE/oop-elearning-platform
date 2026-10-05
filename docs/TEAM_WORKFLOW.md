# Team Workflow

## Branches

Stable branch:

`main`

Kha:

`feature/course-lesson`

Trang:

`feature/enrollment-progress`

## Ownership

Kha initially owns Course, Lesson hierarchy, completion evidence,
Instructor and prerequisite definition.

Trang initially owns Student, Enrollment, Progress, progress percentage,
prerequisite enforcement and certificate eligibility.

Integration code and end-to-end tests are reviewed by both members.

## Shared Contract Rule

Do not change public method signatures listed in
`docs/DOMAIN_CONTRACT.md` without discussing the change first.

If one side needs a contract change:

1. message the other member;
2. agree on the new signature;
3. update DOMAIN_CONTRACT.md;
4. then modify code.

## Git Rules

Before starting new work:

`git checkout main`

`git pull origin main`

Then switch to the feature branch.

Keep commits small and meaningful.

Do not create empty commits for contribution counts.

Do not push unfinished implementation directly to main.

Before opening a pull request:

- compile the code;
- run relevant tests;
- check that shared contracts are unchanged;
- review changed files.

The other team member reviews the pull request before merge.

## AI/Codex

AI should be used for one small task at a time.

The agent must follow AGENTS.md and DOMAIN_CONTRACT.md.

Generated code must be reviewed and understood before commit.

Substantial AI assistance must be recorded in AI_USAGE_LOG.md.