# Database Schematic (Java System)

This schematic is based on the Java JPA entities in `src/main/java/com/barangay/bims/domain`.

## ER Diagram

```mermaid
erDiagram
    USERS ||--o{ AYUDA_REQUESTS : creates
    USERS ||--o{ BLOTTER_REPORTS : reports
    AYUDA_REQUESTS ||--o| SCHEDULES : has_schedule

    USERS {
        BIGINT id PK
        VARCHAR username UNIQUE
        VARCHAR password
        VARCHAR full_name
        VARCHAR resident_number
        VARCHAR contact_number
        VARCHAR address
        VARCHAR id_document_path
        BOOLEAN is_verified
        VARCHAR role
        BOOLEAN official_approved
        TIMESTAMP created_at
    }

    AYUDA_REQUESTS {
        BIGINT id PK
        VARCHAR request_type
        VARCHAR urgency_level
        TEXT description
        DATE preferred_date
        VARCHAR status
        BIGINT created_by FK
        TIMESTAMP created_at
    }

    SCHEDULES {
        BIGINT id PK
        BIGINT ayuda_request_id FK
        DATE scheduled_date
        TIMESTAMP created_at
    }

    BLOTTER_REPORTS {
        BIGINT id PK
        DATE incident_date
        VARCHAR type
        VARCHAR location
        TEXT details
        TEXT resident_update_request
        VARCHAR status
        BIGINT reported_by FK
        TIMESTAMP created_at
    }
```

## Relationship Summary

- `users (1) -> (many) ayuda_requests` via `ayuda_requests.created_by`
- `users (1) -> (many) blotter_reports` via `blotter_reports.reported_by`
- `ayuda_requests (1) -> (0..1) schedules` via `schedules.ayuda_request_id`

## Notes

- `role` is stored as string enum values: `RESIDENT`, `OFFICIAL`, `ADMIN`.
- `official_approved` controls if an official account can login and manage approvals/workflows.
- `urgency_level` uses enum values: `LOW`, `MEDIUM`, `HIGH`.
- `ayuda_requests.status` uses values: `PENDING`, `APPROVED`.
- New resident accounts auto-generate `resident_number` format like `RES-001`.
- `resident_update_request` lets residents request a follow-up update on their own blotter reports.

