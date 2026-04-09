-- Run this on an existing barangay_database to align schema
-- with official approval + resident update request features.

ALTER TABLE users
    ADD COLUMN official_approved BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE blotter_reports
    ADD COLUMN resident_update_request TEXT NULL;

-- Backfill: existing non-official accounts should be treated as approved.
UPDATE users
SET official_approved = TRUE
WHERE role <> 'OFFICIAL';

-- Optional backfill: existing official accounts can be marked approved.
-- Keep this ON if you want current officials to retain access immediately.
UPDATE users
SET official_approved = TRUE
WHERE role = 'OFFICIAL';

-- The application seeder will auto-create default approved official
-- (configured in application.properties) if none exists.
