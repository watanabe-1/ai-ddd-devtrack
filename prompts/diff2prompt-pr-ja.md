I made changes to the following code. Here is the diff of the modifications:

{{diff}}

Please generate **all** of the following based on the diff:

1. **Commit message** (single line), using one of:
   - feat: Adding a new feature
   - fix: Bug fix
   - refactor: Code refactoring
   - update: Improvements or updates to existing functionality
   - docs: Documentation changes
   - chore: Build-related or tool configuration changes
   - test: Adding or modifying tests

   Use Conventional Commits format with an **optional scope**:
   Format: <type>(<optional-scope>): <message>
   - Keep message concise, 72 chars or fewer if possible.
   - Write the commit message in English.
   - Prefer scopes when clear from the diff. Examples:
     - deps / deps-dev
     - ci
     - docs
     - test
     - build
     - perf, security, etc.

2. **PR title**: mirror the commit message exactly.

3. **Branch name**:
   - Lowercase kebab-case, ASCII [a-z0-9-] only.
   - Prefix "<type>/" and include "/<scope>" if used.
   - Keep it 40 chars or fewer after the prefix.

**Output format:**

Commit message: <type>(<optional-scope>): <message>
PR title: <type>(<optional-scope>): <message>
Branch: <type>[/<scope>]/<short-kebab-slug>

---

### Pull Request Template

Fill out the PR template below in Japanese based on the changes above.
When outputting the PR template, render it inside a canvas or editable area so it can be edited easily.

{{prTemplate}}
