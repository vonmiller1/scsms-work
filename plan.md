# SCSMS delivery plan

## Purpose

Guide the backend from its current feature implementation to a secure, supportable release. Priorities below are based on the Spring Boot source in this repository; they are proposed work, not claims that the items are complete.

## Current snapshot

- The repository contains a Java 21 / Spring Boot REST backend with MySQL persistence.
- User and BMC account controllers expose registration, sign-in, and session lookup routes.
- Complaint handling supports submission, retrieval, status updates, coordinates, and either a local photo upload or photo URL.
- Session-based authentication, CORS configuration, local upload storage, and Hibernate `ddl-auto=update` are present.
- A Docker Compose file is present, but its artifact path and port configuration should be reconciled with the current Maven and application settings before relying on it.
- A separate client application is not included in this repository.

## Priorities

### 1. Establish a safe release baseline

- Move every database and service credential into environment-specific configuration or a secret manager; replace committed development credentials and rotate any real credential that was exposed.
- Define and verify the authentication and authorization policy for user versus BMC operations, including complaint creation, listing, and status changes.
- Review password storage, account registration controls, session lifecycle, CSRF behavior, and CORS origins; document the intended browser/session model.
- Standardize request validation and API errors so invalid input, missing records, and unauthenticated requests return predictable HTTP status codes without exposing internals.

**Exit criteria:** no production secret is committed; account and complaint permissions are explicitly specified and exercised; API input and failure behavior are documented.

### 2. Make data and uploads production-ready

- Replace automatic schema mutation with versioned database migrations and define backup/restore procedures.
- Validate image type and size, generate collision-resistant storage names, and avoid treating client-provided media URLs as trusted content.
- Move uploads from a local-only directory to durable storage, or explicitly persist and back up the volume in every deployment environment.
- Define complaint status values and allowed transitions; add timestamps and audit history where operational workflows require them.

**Exit criteria:** schema changes are repeatable, complaint transitions are constrained, and uploaded media survives restart and deployment replacement.

### 3. Align development and deployment

- Reconcile `docker-compose.yml`, the Maven output artifact, application port, and health check; keep local-only credentials out of deployable configuration.
- Define one supported local setup path and document required environment variables, CORS values, database initialization, and persistent storage.
- Make deployment health, logs, and shutdown behavior observable; avoid logging credentials, session contents, or sensitive report data.
- Confirm the Java runtime and packaging expectations for each target environment before release.

**Exit criteria:** a clean environment can start the API using documented steps, and its service and database configuration agree.

### 4. Verify API and operational quality

- Add focused automated coverage for account flows, authorization boundaries, complaint creation and retrieval, status transitions, uploads, and error responses.
- Add API schema/documentation that reflects actual request formats and authentication requirements.
- Establish dependency and configuration review, repeatable checks in continuous integration, and a release checklist.
- Agree on the separate frontend/API contract, including session cookies, allowed origins, request fields, and status values.

**Exit criteria:** critical behavior is covered by repeatable checks and client integration uses a versioned, agreed API contract.

## Suggested sequence

1. Complete release-baseline decisions and credential cleanup.
2. Lock down and test the identity, roles, and complaint workflow.
3. Introduce schema migration and durable media storage.
4. Reconcile local/container/deployment configuration and improve observability.
5. Publish the API contract and add continuous verification.

## Out of scope for this plan

A frontend application, AI image validation, analytics dashboards, push notifications, and city-wide reporting are not present in this repository. Treat them as separate product initiatives after the backend release baseline is established.