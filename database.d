-- ============================================================
--  NCC CADET MANAGEMENT PORTAL — DATABASE DEFINITION FILE
--  File      : database.d
--  Database  : MySQL 8.x
--  App       : NISNCC — NCC Integrated Software Portal (Spring Boot)
--  Version   : 1.0.0
--  Created   : 2026-05-02
-- ============================================================
--
--  TABLES (in dependency order):
--    1. users
--    2. units
--    3. officers
--    4. cadets
--    5. camps
--    6. camp_registrations
--    7. attendance
--    8. certificates
--    9. notices
--
--  ENUM VALUES are stored as VARCHAR(EnumType.STRING) by Hibernate.
--  DDL is intentionally MySQL-compatible; Hibernate handles auto-DDL
--  (spring.jpa.hibernate.ddl-auto=update) so this file is the
--  *authoritative reference* schema, useful for:
--    - Recreating the DB from scratch
--    - Documentation / code review
--    - Migration scripts
-- ============================================================

-- ============================================================
-- 0. DATABASE SETUP
-- ============================================================
CREATE DATABASE IF NOT EXISTS ncc_portal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ncc_portal;

-- ============================================================
-- 1. USERS
--    Authentication table — stores login credentials and role.
--    Passwords are BCrypt-hashed (Spring Security).
--    Linked 1-to-1 with either an Officer or a Cadet record.
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT        NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50)   NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,           -- BCrypt hash
    role       VARCHAR(20)   NOT NULL,            -- ADMIN | OFFICER | CADET
    is_active  TINYINT(1)    NOT NULL DEFAULT 1,
    last_login DATETIME      NULL,

    PRIMARY KEY (id),
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'OFFICER', 'CADET'))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='User authentication and role management';

-- ============================================================
-- 2. UNITS
--    Represents NCC battalion units (e.g., 1 TN Bn NCC).
--    One unit can have multiple Officers and many Cadets.
-- ============================================================
CREATE TABLE IF NOT EXISTS units (
    id               BIGINT        NOT NULL AUTO_INCREMENT,
    unit_name        VARCHAR(100)  NOT NULL,
    battalion        VARCHAR(100)  NOT NULL,
    state            VARCHAR(50)   NOT NULL,
    district         VARCHAR(50)   NOT NULL,
    description      VARCHAR(200)  NULL,
    established_date DATE          NULL,

    PRIMARY KEY (id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='NCC battalion units';

-- ============================================================
-- 3. OFFICERS
--    ANO (Associate NCC Officers) who manage a unit.
--    Each officer is linked to one Unit and one User account.
-- ============================================================
CREATE TABLE IF NOT EXISTS officers (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)  NOT NULL,
    designation VARCHAR(100)  NULL,              -- e.g., Major, Captain, ANO
    email       VARCHAR(100)  NULL UNIQUE,
    phone       VARCHAR(10)   NULL,
    unit_id     BIGINT        NULL,
    user_id     BIGINT        NULL UNIQUE,

    PRIMARY KEY (id),
    CONSTRAINT fk_officers_unit FOREIGN KEY (unit_id)
        REFERENCES units (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_officers_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='NCC Officers (ANO) managing units';

-- ============================================================
-- 4. CADETS
--    Core entity representing enrolled NCC cadets.
--    Linked to a Unit (ManyToOne) and a User account (OneToOne).
--    cadet_id follows format: NCC/YYYY/NNN (application-generated).
--
--    ENUM — cadet_rank:
--      CADET | LANCE_CORPORAL | CORPORAL | SERGEANT
--      | UNDER_OFFICER | SENIOR_UNDER_OFFICER
--    ENUM — gender:
--      MALE | FEMALE | OTHER
-- ============================================================
CREATE TABLE IF NOT EXISTS cadets (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    cadet_id        VARCHAR(20)   NULL UNIQUE,   -- NCC/YYYY/NNN
    name            VARCHAR(100)  NOT NULL,
    dob             DATE          NOT NULL,
    gender          VARCHAR(10)   NOT NULL,       -- MALE | FEMALE | OTHER
    father_name     VARCHAR(100)  NULL,
    email           VARCHAR(100)  NULL UNIQUE,
    phone           VARCHAR(10)   NULL,
    address         VARCHAR(300)  NULL,
    college         VARCHAR(150)  NULL,
    unit_id         BIGINT        NULL,
    cadet_rank      VARCHAR(30)   NULL,           -- Rank enum value
    enrollment_date DATE          NULL,
    blood_group     VARCHAR(5)    NULL,           -- A+, B-, O+, AB+, etc.
    aadhaar_masked  VARCHAR(20)   NULL,           -- XXXX-XXXX-1234 (last 4 only)
    user_id         BIGINT        NULL UNIQUE,

    PRIMARY KEY (id),
    CONSTRAINT fk_cadets_unit FOREIGN KEY (unit_id)
        REFERENCES units (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_cadets_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT chk_cadets_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT chk_cadets_rank CHECK (cadet_rank IN (
        'CADET', 'LANCE_CORPORAL', 'CORPORAL',
        'SERGEANT', 'UNDER_OFFICER', 'SENIOR_UNDER_OFFICER'
    ))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='NCC cadets — core enrolment table';

-- ============================================================
-- 5. CAMPS
--    NCC camps of various types (ATC, RDC, Trekking, etc.).
--    max_cadets: maximum enrollment capacity.
--
--    ENUM — type:
--      ATC | RDC | TSC | NIC | TREKKING | OTHER
-- ============================================================
CREATE TABLE IF NOT EXISTS camps (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    camp_name   VARCHAR(150)  NOT NULL,
    type        VARCHAR(20)   NOT NULL,           -- CampType enum
    start_date  DATE          NOT NULL,
    end_date    DATE          NOT NULL,
    location    VARCHAR(200)  NULL,
    description VARCHAR(500)  NULL,
    max_cadets  INT           NULL,

    PRIMARY KEY (id),
    CONSTRAINT chk_camps_type CHECK (type IN ('ATC', 'RDC', 'TSC', 'NIC', 'TREKKING', 'OTHER')),
    CONSTRAINT chk_camps_dates CHECK (end_date >= start_date)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='NCC camps — ATC, RDC, Trekking, etc.';

-- ============================================================
-- 6. CAMP REGISTRATIONS
--    Junction table: tracks which cadet is registered for which camp.
--    Unique constraint prevents duplicate registrations per camp.
--
--    ENUM — attendance: REGISTERED | ATTENDED | ABSENT | WITHDRAWN
--    ENUM — performance: EXCELLENT | GOOD | AVERAGE | POOR | NOT_RATED
-- ============================================================
CREATE TABLE IF NOT EXISTS camp_registrations (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    camp_id     BIGINT        NOT NULL,
    cadet_id    BIGINT        NOT NULL,
    attendance  VARCHAR(20)   NULL DEFAULT 'REGISTERED',
    performance VARCHAR(20)   NULL DEFAULT 'NOT_RATED',
    remarks     VARCHAR(200)  NULL,

    PRIMARY KEY (id),
    CONSTRAINT uq_camp_cadet UNIQUE (camp_id, cadet_id),
    CONSTRAINT fk_campreg_camp FOREIGN KEY (camp_id)
        REFERENCES camps (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_campreg_cadet FOREIGN KEY (cadet_id)
        REFERENCES cadets (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_campreg_attendance CHECK (attendance IN (
        'REGISTERED', 'ATTENDED', 'ABSENT', 'WITHDRAWN'
    )),
    CONSTRAINT chk_campreg_performance CHECK (performance IN (
        'EXCELLENT', 'GOOD', 'AVERAGE', 'POOR', 'NOT_RATED'
    ))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Camp registration and attendance records per cadet';

-- ============================================================
-- 7. ATTENDANCE
--    Parade / training session attendance per cadet.
--    Unique on (cadet_id, date, type) — one record per session.
--
--    ENUM — status: PRESENT | ABSENT | LEAVE
--    ENUM — type:   PARADE  | CAMP
-- ============================================================
CREATE TABLE IF NOT EXISTS attendance (
    id        BIGINT        NOT NULL AUTO_INCREMENT,
    cadet_id  BIGINT        NOT NULL,
    date      DATE          NOT NULL,
    status    VARCHAR(10)   NOT NULL,             -- PRESENT | ABSENT | LEAVE
    type      VARCHAR(10)   NOT NULL,             -- PARADE | CAMP
    remarks   VARCHAR(200)  NULL,

    PRIMARY KEY (id),
    CONSTRAINT uq_attendance UNIQUE (cadet_id, date, type),
    CONSTRAINT fk_attendance_cadet FOREIGN KEY (cadet_id)
        REFERENCES cadets (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_attendance_status CHECK (status IN ('PRESENT', 'ABSENT', 'LEAVE')),
    CONSTRAINT chk_attendance_type   CHECK (type   IN ('PARADE', 'CAMP'))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Cadet parade and camp attendance log';

-- ============================================================
-- 8. CERTIFICATES
--    NCC A / B / C certificate exam tracking per cadet.
--    Unique on (cadet_id, type) — one record per cert level.
--
--    ENUM — type:   A | B | C
--    ENUM — result: ELIGIBLE | NOT_ELIGIBLE | PASSED | FAILED | PENDING
-- ============================================================
CREATE TABLE IF NOT EXISTS certificates (
    id        BIGINT        NOT NULL AUTO_INCREMENT,
    cadet_id  BIGINT        NOT NULL,
    type      VARCHAR(5)    NOT NULL,             -- A | B | C
    result    VARCHAR(20)   NOT NULL,
    exam_date DATE          NULL,
    remarks   VARCHAR(200)  NULL,

    PRIMARY KEY (id),
    CONSTRAINT uq_cert_cadet_type UNIQUE (cadet_id, type),
    CONSTRAINT fk_cert_cadet FOREIGN KEY (cadet_id)
        REFERENCES cadets (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_cert_type   CHECK (type   IN ('A', 'B', 'C')),
    CONSTRAINT chk_cert_result CHECK (result IN (
        'ELIGIBLE', 'NOT_ELIGIBLE', 'PASSED', 'FAILED', 'PENDING'
    ))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='NCC A/B/C certificate exam results per cadet';

-- ============================================================
-- 9. NOTICES
--    Bulletin board notices posted by Admin or Officers.
--    target_role controls visibility: ALL | CADET | OFFICER | ADMIN
-- ============================================================
CREATE TABLE IF NOT EXISTS notices (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    title       VARCHAR(200)  NOT NULL,
    message     TEXT          NOT NULL,
    posted_by   VARCHAR(100)  NULL,               -- Username of poster
    posted_date DATE          NOT NULL,
    target_role VARCHAR(20)   NULL DEFAULT 'ALL', -- ALL | CADET | OFFICER | ADMIN

    PRIMARY KEY (id),
    CONSTRAINT chk_notices_role CHECK (target_role IN ('ALL', 'CADET', 'OFFICER', 'ADMIN'))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Portal bulletin board notices';

-- ============================================================
-- INDEXES (performance)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_cadets_unit       ON cadets           (unit_id);
CREATE INDEX IF NOT EXISTS idx_cadets_rank        ON cadets           (cadet_rank);
CREATE INDEX IF NOT EXISTS idx_officers_unit      ON officers         (unit_id);
CREATE INDEX IF NOT EXISTS idx_campreg_camp       ON camp_registrations(camp_id);
CREATE INDEX IF NOT EXISTS idx_campreg_cadet      ON camp_registrations(cadet_id);
CREATE INDEX IF NOT EXISTS idx_attendance_cadet   ON attendance       (cadet_id);
CREATE INDEX IF NOT EXISTS idx_attendance_date    ON attendance       (date);
CREATE INDEX IF NOT EXISTS idx_cert_cadet         ON certificates     (cadet_id);
CREATE INDEX IF NOT EXISTS idx_notices_role       ON notices          (target_role);
CREATE INDEX IF NOT EXISTS idx_notices_date       ON notices          (posted_date DESC);

-- ============================================================
-- USEFUL VIEWS
-- ============================================================

-- v_cadet_summary: full cadet profile with unit name and username
CREATE OR REPLACE VIEW v_cadet_summary AS
SELECT
    c.id,
    c.cadet_id,
    c.name                  AS cadet_name,
    c.dob,
    c.gender,
    c.cadet_rank            AS cadet_rank,
    c.blood_group,
    c.college,
    c.email,
    c.phone,
    c.enrollment_date,
    u.unit_name,
    u.battalion,
    u.state,
    usr.username,
    usr.role
FROM cadets c
LEFT JOIN units   u   ON c.unit_id = u.id
LEFT JOIN users   usr ON c.user_id  = usr.id;

-- v_attendance_stats: attendance percentage per cadet
CREATE OR REPLACE VIEW v_attendance_stats AS
SELECT
    c.id                                                          AS cadet_id,
    c.name                                                        AS cadet_name,
    COUNT(*)                                                      AS total_sessions,
    SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END)        AS present_count,
    SUM(CASE WHEN a.status = 'ABSENT'  THEN 1 ELSE 0 END)        AS absent_count,
    ROUND(
        SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END)
        / COUNT(*) * 100, 2
    )                                                             AS attendance_pct
FROM attendance a
JOIN cadets c ON a.cadet_id = c.id
GROUP BY c.id, c.name;

-- v_camp_enrollment: camp enrollment count vs capacity
CREATE OR REPLACE VIEW v_camp_enrollment AS
SELECT
    cm.id,
    cm.camp_name,
    cm.type,
    cm.start_date,
    cm.end_date,
    cm.location,
    cm.max_cadets,
    COUNT(cr.id)                              AS enrolled_count,
    cm.max_cadets - COUNT(cr.id)              AS seats_remaining
FROM camps cm
LEFT JOIN camp_registrations cr ON cm.id = cr.camp_id
GROUP BY cm.id, cm.camp_name, cm.type, cm.start_date, cm.end_date,
         cm.location, cm.max_cadets;

-- ============================================================
-- END OF SCHEMA
-- ============================================================
