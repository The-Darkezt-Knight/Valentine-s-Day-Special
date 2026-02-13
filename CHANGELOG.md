# Changelog

All notable changes to the **Valentine's Day Special** project are documented in this file.

---
## [v1.2.1] — 2026-02-13

### Data Persistence (`application.properties`)
- **REVERTED** H2 database from file-based (`jdbc:h2:file:./data/valentine_db`) back to **in-memory** (`jdbc:h2:mem:valentine_db`).

---
## [v1.1.0] — 2026-02-13

### Security & Access Control
- **NEW** `SessionAuthenticationFilter.java` — A `OncePerRequestFilter` that blocks direct URL access to `/view/main-panel.html`. Only users with a valid session flag (`authenticated = true`) may view the letters page; all others are redirected to `/view/onboarding-page.html`.
- **CHANGED** `Security.java` — Registered `SessionAuthenticationFilter` in the Spring Security filter chain (before `UsernamePasswordAuthenticationFilter`).
- **CHANGED** `AuthenticationController.java` — Injected `HttpSession`; sets `session.setAttribute("authenticated", true)` upon successful answer validation.

### Authentication Logic (`AuthenticationService.java`)
- **FIX** `isAnswerCorrect` — Now guards against duplicate secret questions. If `findBySecretQuestion()` returns more than one result, an `IllegalStateException` is thrown instead of silently picking the first match.
- **FIX** `isAnswerCorrect` — Answer comparison is now **case-insensitive** and **whitespace-trimmed** (both stored and user answers go through `.trim().toLowerCase()` before comparison).
- **FIX** `isAnswerCorrect` — Return type changed from `String` to `boolean` to follow single-responsibility; the success message is now built in the controller.
- **IMPROVED** `authenticateEntrance` — Returns `List.of()` instead of `null` when the category is invalid, preventing downstream `NullPointerException`s.
- **IMPROVED** Added `Objects.requireNonNull()` defensive checks on all method parameters.

### Data Persistence (`application.properties`)
- **CHANGED** H2 database from in-memory (`mem`) to **file-based** (`jdbc:h2:file:./data/valentine_db`) so registered people and letters survive server restarts.
- **ADDED** `spring.jpa.hibernate.ddl-auto=update` for automatic schema management.
- **ADDED** H2 console enabled at `/h2-console` for development debugging.

### Frontend (`mainpanel.js`)
- **FIX** `paper.innerHTML = ""` referenced an undefined variable; corrected to `container.innerHTML = ""`.
- **FIX** Added `res.ok` check on the fetch response to properly handle HTTP errors.
- **IMPROVED** Added a guard that redirects to `/view/onboarding-page.html` if `category` is missing from `localStorage`.
- **IMPROVED** Consolidated `paper.classList.add("letter")` and `paper.classList.add("paper")` into a single `classList.add("letter", "paper")` call.

---

## [v1.0.0] — Initial Release

- Core Spring Boot project with H2 in-memory database.
- `Person` entity with category-based grouping (`PARENTS`, `SPECIAL_SOMEONE`, `PEERS`).
- `Letter` entity with `@ManyToOne` relationship to `Person`.
- `RegisterService` for CRUD operations on people.
- `LetterService` for dedicating and displaying letters by category.
- `AuthenticationService` with secret question/answer flow.
- Static frontend: onboarding page with modals, admin panel, and main letter display panel.
- Spring Security configured with all requests permitted (no login).
