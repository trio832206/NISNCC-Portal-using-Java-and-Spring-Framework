-- ============================================================
-- NCC Cadet Management Portal — Sample Data (data.sql)
-- Auto-loaded by Spring Boot on startup
-- Passwords are BCrypt hashed:
--   admin123   -> $2a$10$...
--   officer123 -> $2a$10$...
--   cadet123   -> $2a$10$...
-- ============================================================

-- Use INSERT IGNORE to avoid errors on repeated restarts
-- (Hibernate handles DDL; this file handles initial data)

-- ============================================================
-- USERS (BCrypt hashed passwords)
-- ============================================================
INSERT IGNORE INTO users (id, username, password, role, is_active, last_login) VALUES
(1, 'admin',    '$2a$10$Jj3NS2jrm9p35P8xOjHWzewN.g2lpZU6nNrFNM4O9E3RVvxu9HHxm', 'ADMIN',   true, NULL),
(2, 'officer1', '$2a$10$dPjhPekle6AsE4si6k5/V.vQdze7ZMIzVRFGHyADr880kqrTkx5RK', 'OFFICER', true, NULL),
(3, 'cadet1',   '$2a$10$n96Ov.eWrm9O4n7qJPj5BupnqStPzFwNTi7/krU4emqoQhbsF19s6', 'CADET',   true, NULL),
(4, 'cadet2',   '$2a$10$n96Ov.eWrm9O4n7qJPj5BupnqStPzFwNTi7/krU4emqoQhbsF19s6', 'CADET',   true, NULL),
(5, 'cadet3',   '$2a$10$n96Ov.eWrm9O4n7qJPj5BupnqStPzFwNTi7/krU4emqoQhbsF19s6', 'CADET',   true, NULL);

-- ============================================================
-- UNITS
-- ============================================================
INSERT IGNORE INTO units (id, unit_name, battalion, state, district, description, established_date) VALUES
(1, '1 TN Bn NCC',    '1 TN Bn',    'Tamil Nadu', 'Chennai',    'First Tamil Nadu Battalion',              '2000-01-15'),
(2, '2 TN Bn NCC',    '2 TN Bn',    'Tamil Nadu', 'Coimbatore', 'Second Tamil Nadu Battalion',             '2001-03-10'),
(3, 'KR NCC Unit',    '1 KR Bn',    'Karnataka',  'Bengaluru',  'Karnataka NCC Battalion',                 '1999-06-20');

-- ============================================================
-- OFFICERS
-- ============================================================
INSERT IGNORE INTO officers (id, name, designation, email, phone, unit_id, user_id) VALUES
(1, 'Maj Ravi Kumar',   'Major / ANO',   'ravi.kumar@ncc.gov.in',   '9876543210', 1, 2);

-- ============================================================
-- CADETS
-- ============================================================
INSERT IGNORE INTO cadets (id, cadet_id, name, dob, gender, father_name, email, phone,
    address, college, unit_id, cadet_rank, enrollment_date, blood_group, aadhaar_masked, user_id) VALUES
(1, 'NCC/2024/001', 'Arjun Sharma',     '2005-04-15', 'MALE',   'Ramesh Sharma',     'arjun.sharma@email.com',     '9876543201',
    '12 Gandhi Nagar, Chennai', 'Loyola College, Chennai', 1, 'CADET', '2024-01-10', 'B+', 'XXXX-XXXX-1234', 3),

(2, 'NCC/2024/002', 'Priya Devi',       '2006-07-22', 'FEMALE', 'Suresh Devi',       'priya.devi@email.com',       '9876543202',
    '45 Anna Salai, Chennai',   'MCC, Chennai',            1, 'LANCE_CORPORAL', '2024-01-10', 'A+', 'XXXX-XXXX-5678', 4),

(3, 'NCC/2024/003', 'Mohammed Aslam',   '2004-11-30', 'MALE',   'Akbar Ali',         'aslam.ali@email.com',        '9876543203',
    '78 Mount Road, Chennai',   'SRM College, Chennai',    1, 'CORPORAL', '2023-06-01', 'O+', 'XXXX-XXXX-9012', 5);

-- ============================================================
-- CAMPS
-- ============================================================
INSERT IGNORE INTO camps (id, camp_name, type, start_date, end_date, location, description, max_cadets) VALUES
(1, 'Annual Training Camp 2024',     'ATC',      '2024-12-01', '2024-12-10', 'NCC Camp, Aundh, Pune',     'Annual Training Camp for all cadets',     50),
(2, 'Republic Day Camp 2025',        'RDC',      '2025-01-05', '2025-01-28', 'Delhi Parade Ground, Delhi', 'Republic Day Parade Camp, Delhi',         25),
(3, 'Trekking Expedition 2024',      'TREKKING', '2024-11-15', '2024-11-20', 'Kodaikanal, Tamil Nadu',     'Nature trekking camp for adventure cadets', 30);

-- ============================================================
-- CAMP REGISTRATIONS
-- ============================================================
INSERT IGNORE INTO camp_registrations (id, camp_id, cadet_id, attendance, performance, remarks) VALUES
(1, 1, 1, 'ATTENDED',   'EXCELLENT', 'Outstanding performance'),
(2, 1, 2, 'ATTENDED',   'GOOD',      'Good participation'),
(3, 3, 3, 'REGISTERED', 'NOT_RATED', 'Upcoming camp');

-- ============================================================
-- ATTENDANCE
-- ============================================================
INSERT IGNORE INTO attendance (id, cadet_id, date, status, type, remarks) VALUES
(1, 1, '2024-10-01', 'PRESENT', 'PARADE', NULL),
(2, 1, '2024-10-08', 'PRESENT', 'PARADE', NULL),
(3, 1, '2024-10-15', 'ABSENT',  'PARADE', 'Medical leave'),
(4, 2, '2024-10-01', 'PRESENT', 'PARADE', NULL),
(5, 2, '2024-10-08', 'PRESENT', 'PARADE', NULL),
(6, 3, '2024-10-01', 'ABSENT',  'PARADE', NULL),
(7, 3, '2024-10-08', 'PRESENT', 'PARADE', NULL);

-- ============================================================
-- CERTIFICATES
-- ============================================================
INSERT IGNORE INTO certificates (id, cadet_id, type, result, exam_date, remarks) VALUES
(1, 1, 'A', 'PASSED',    '2023-11-15', 'Cleared A certificate with distinction'),
(2, 2, 'A', 'PASSED',    '2023-11-15', 'Passed A certificate'),
(3, 1, 'B', 'ELIGIBLE',  NULL,         'Eligible for B certificate exam'),
(4, 3, 'A', 'PENDING',   NULL,         'Appearing in upcoming exam');

-- ============================================================
-- NOTICES
-- ============================================================
INSERT IGNORE INTO notices (id, title, message, posted_by, posted_date, target_role) VALUES
(1, 'Welcome to NCC Portal',
    'Welcome to the NCC Cadet Management Portal. All cadets are requested to update their profiles.',
    'admin', '2024-10-01', 'ALL'),
(2, 'Annual Training Camp Registration Open',
    'Annual Training Camp 2024 registrations are now open. Eligible cadets must report to their ANO by 20 November 2024.',
    'admin', '2024-10-15', 'CADET'),
(3, 'Attendance Update',
    'All officers are requested to update attendance records for October 2024 by 31 October.',
    'admin', '2024-10-20', 'OFFICER'),
(4, 'Republic Day Camp Selection',
    'Selection tests for Republic Day Camp 2025 will be held on 15 November 2024. All eligible cadets must attend.',
    'officer1', '2024-10-25', 'CADET');
