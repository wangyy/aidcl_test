# User CRUD Demo Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Build a Vue + Spring Boot demo for user CRUD management.

**Architecture:** The frontend is a Vue 3 + Vite single page app that calls REST APIs through `/api/users`. The backend is a Spring Boot 3 application exposing CRUD endpoints backed by MySQL. Database credentials are externalized so the Volcengine public cloud MySQL address can be added later.

**Tech Stack:** Vue 3, Vite, Spring Boot 3, Spring Web, Spring Data JPA, MySQL Connector/J, Java 17.

---

### Task 1: Backend CRUD API

**Files:**
- Create: `backend/pom.xml`
- Create: `backend/src/main/java/com/example/userdemo/UserDemoApplication.java`
- Create: `backend/src/main/java/com/example/userdemo/user/*`
- Create: `backend/src/main/resources/application.yml`

**Steps:**
1. Define the Maven Spring Boot project with Web, JPA, Validation, MySQL and Test dependencies.
2. Create `User` entity mapped to `sys_user` with `userId`, `username`, and `organization` fields.
3. Create DTOs for request/response to avoid exposing persistence objects directly.
4. Create repository, service, controller, exception handler and CORS config.
5. Add service tests covering create, list, get, update and delete behavior.

### Task 2: Frontend CRUD UI

**Files:**
- Create: `frontend/package.json`
- Create: `frontend/vite.config.js`
- Create: `frontend/src/App.vue`
- Create: `frontend/src/api/users.js`
- Create: `frontend/src/styles.css`

**Steps:**
1. Create a Vue 3 + Vite app.
2. Add an API client that calls backend `/api/users` endpoints.
3. Build one page with table, create/edit form, delete action, loading state and error message.
4. Configure Vite dev proxy to forward `/api` to Spring Boot on port `8080`.

### Task 3: Documentation And Verification

**Files:**
- Create: `README.md`

**Steps:**
1. Document backend and frontend startup commands.
2. Document MySQL environment variables for the future Volcengine MySQL connection.
3. Verify generated project structure and check diagnostics.
