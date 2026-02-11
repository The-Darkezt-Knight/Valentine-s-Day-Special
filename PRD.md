
# PRD: Valentine's Special Access & Categorization System

## 1. Product Overview

The system will allow the administrator to categorize recipients into three distinct groups: Parents, Special Someone, and Peers. Access to personalized letters for high-privacy categories (Parents/Special Someone) will be gated by a "Challenge-Response" security question, while Peers will have immediate access to general content.

## 2. User Personas

* Admin (Raphael): Creates and manages people, assigns categories, and sets security questions/answers.
* Restricted Recipients (Parents/Special Someone): Individuals who must verify their identity via a personal question to view their letters.
* General Recipients (Peers): Friends or acquaintances who can view group-wide letters without verification.

## 3. Functional Requirements

### 3.1 Data Model Extensions

* Category Assignment: Every `Person` entity must be assigned a `Category` (Enum: `PARENTS`, `SPECIAL_SOMEONE`, `PEERS`).
* Challenge-Response Storage: The `Person` entity must store a `secretQuestion` and a `secretAnswer` for the `PARENTS` and `SPECIAL_SOMEONE` categories.
* Service Layer Validation: The `RegisterService` must enforce that `secretQuestion` and `secretAnswer` are provided if the category is not `PEERS`.

### 3.2 Frontend Access Workflow (Landing Page)

The `index.html` (or a new guest landing page) will present three primary entry points (Cards):

1. Peers Card: * Behavior: On click, redirects directly to the peer letter gallery.
* Requirement: No identification prompt.


2. Parents & Special Someone Cards:
* Behavior: On click, opens a "Who are you?" selection interface.
* Identification Flow:
* User selects their name from a filtered list.
* System retrieves and displays the specific `secretQuestion` for that person.
* User enters an answer.
* Access is granted only if the answer matches the database record (case-insensitive).





### 3.3 Security & Validation

* Defensive Checks: Use `Objects.requireNonNull()` for category and security fields during registration to prevent null pointer exceptions.
* Fail-Fast Verification: The system must immediately reject incorrect answers and provide a clear error message without revealing sensitive data.

## 4. Technical Specifications

### 4.1 Backend Changes

* Entity Update (`Person.java`): Add `@Enumerated` Category and String fields for the challenge.
* DTO Update (`RegisterRequest.java`): Include category and security fields to support the new admin "Add Person" modal.
* Controller (`UserController.java`): Create an endpoint `api/admin/list/challenge/{id}` to fetch the question and `api/admin/list/verify` to validate the response.

### 4.2 Frontend Changes

* UI Components: Replace or augment the main admin panel with a guest-facing view containing three Bootstrap cards.
* JavaScript (`UserTable.js` extension): Implement a `verifyAccess()` function using the Fetch API to handle the challenge-response handshake.

## 5. Non-Functional Requirements

* Usability: The "Peers" flow must be frictionless (one-click access).
* Privacy: Security questions should be specific enough that only the intended recipient can answer, but simple enough for them to remember.

## 6. Future Scope

* MySQL Transition: Ensure the `secretAnswer` column is properly indexed for performance.
* Encryption: Implement hashing for the `secretAnswer` to ensure that even if the database is compromised, the answers remain secure.

To help Copilot or any AI assistant refactor your existing code to match the new requirements, you can use the following prompt. It focuses strictly on the files you have already created—`Person.java`, `RegisterService.java`, `UserController.java`, and your DTOs—without adding new features like the Letter system yet.

---
