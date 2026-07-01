$readme = @"
# NISNCC — NCC Cadet Management Portal

**NISNCC Replica** — A full-stack NCC Integrated Software Portal built with **Java 17**, **Spring Boot 3.2**, **Spring Security**, **Thymeleaf**, and **MySQL**.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?logo=mysql)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-green?logo=thymeleaf)](https://www.thymeleaf.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Default Credentials](#default-credentials)
- [Role-Based Access](#role-based-access)
- [URL Routes](#url-routes)
- [Configuration](#configuration)
- [Build and Run](#build-and-run)

---

## About

The **NISNCC Portal** is a web-based management system inspired by the National Integration Software for the National Cadet Corps (NCC) of India. It provides a centralized platform for **Administrators**, **Officers (ANOs)**, and **Cadets** to manage cadet records, attendance, camps, certificates, and notices — all secured with role-based access control.

---

## Features

### Admin
- Manage officers and user accounts
- View dashboard statistics (total cadets, officers, camps, notices)
- Post notices visible to all or specific roles
- Full access to all modules

### Officer (ANO)
- View and manage cadets in their unit
- Mark and update parade/camp attendance
- Register cadets for camps and update performance
- Issue and manage NCC A/B/C certificate records
- Post notices targeted to cadets

### Cadet
- View personal profile and rank
- Check attendance history and percentage
- View camp registrations and performance
- View assigned certificates (A/B/C)
- Read notices posted by admin/officers

### General
- Secure login with BCrypt password hashing
- Role-based routing and access control (Spring Security)
- Session timeout after 30 minutes of inactivity
- Custom 404 / 500 / Access Denied error pages
- CSV export for reports
- Responsive Thymeleaf-rendered UI

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Web MVC | Spring MVC + Embedded Tomcat |
| Security | Spring Security 6 |
| ORM | Spring Data JPA + Hibernate |
| Template Engine | Thymeleaf 3 + thymeleaf-extras-springsecurity6 |
| Database | MySQL 8.x |
| Validation | Jakarta Bean Validation |
| CSV Export | OpenCSV 5.9 |
| Build Tool | Apache Maven |
| Boilerplate Reduction | Lombok |
| Dev Tools | Spring Boot DevTools (hot reload) |

---

## Project Structure

    NISNCC/
    |-- src/main/java/com/nccportal/
    |   |-- NccPortalApplication.java
    |   |-- config/SecurityConfig.java
    |   |-- controller/
    |   |   |-- AdminController.java
    |   |   |-- AuthController.java
    |   |   |-- CadetController.java
    |   |   |-- CadetManagementController.java
    |   |   |-- CampController.java
    |   |   |-- CertificateController.java
    |   |   |-- AttendanceController.java
    |   |   |-- NoticeController.java
    |   |   |-- OfficerController.java
    |   |   |-- ReportController.java
    |   |   |-- UnitController.java
    |   |   \-- CustomErrorController.java
    |   |-- dto/
    |   |-- entity/
    |   |   |-- User.java, Officer.java, Cadet.java, Unit.java
    |   |   |-- Camp.java, CampRegistration.java
    |   |   |-- Attendance.java, Certificate.java, Notice.java
    |   |-- exception/
    |   |-- repository/
    |   \-- service/
    |-- src/main/resources/
    |   |-- application.properties
    |   |-- data.sql
    |   |-- static/css/style.css
    |   |-- static/js/main.js
    |   \-- templates/ (login, admin, cadet, camps, attendance, notices, reports, error)
    |-- database.d       <- Full MySQL schema reference
    |-- pom.xml
    |-- .gitignore
    \-- README.md

---

## Database Schema

The application uses a **MySQL 8.x** database named ``ncc_portal``. Hibernate auto-manages DDL (``ddl-auto=update``). The file [database.d](database.d) is the authoritative schema reference.

### Entity Relationship Overview

    users
      |__(1:1)__ officers __(N:1)__ units
      \__(1:1)__ cadets   __(N:1)__ units
                    |
                    |__(1:N)__ attendance
                    |__(1:N)__ certificates
                    \__(1:N)__ camp_registrations __(N:1)__ camps
    
    notices  (standalone, targeted by role)

### Tables

| Table | Description |
|-------|-------------|
| users | Authentication — BCrypt passwords, roles: ADMIN, OFFICER, CADET |
| units | NCC battalion units (name, state, district) |
| officers | ANOs linked to a unit and user account |
| cadets | Core cadet records (rank, DOB, college, blood group, Aadhaar masked) |
| camps | NCC camps — ATC, RDC, TSC, NIC, TREKKING, OTHER |
| camp_registrations | Cadet-Camp junction with attendance and performance rating |
| attendance | Parade / camp session attendance log per cadet |
| certificates | NCC A / B / C certificate exam tracking |
| notices | Bulletin board notices with role-based visibility |

### SQL Views (in database.d)

| View | Description |
|------|-------------|
| v_cadet_summary | Full cadet profile with unit name and username |
| v_attendance_stats | Attendance percentage per cadet |
| v_camp_enrollment | Camp enrollment count vs. capacity |

---

## Getting Started

### Prerequisites

- **Java 17+** — https://adoptium.net/
- **Maven 3.8+** — https://maven.apache.org/download.cgi
- **MySQL 8.x** — https://dev.mysql.com/downloads/mysql/

### 1. Clone the Repository

    git clone https://github.com/trio832206/NISNCC-Portal-using-Java-and-Spring-Framework.git
    cd NISNCC-Portal-using-Java-and-Spring-Framework

### 2. Create the Database

Run this in MySQL:

    CREATE DATABASE IF NOT EXISTS ncc_portal
        CHARACTER SET utf8mb4
        COLLATE utf8mb4_unicode_ci;

Or run the full schema (tables, indexes, views):

    mysql -u root -p < database.d

### 3. Configure the Application

Edit ``src/main/resources/application.properties`` with your MySQL credentials:

    spring.datasource.url=jdbc:mysql://localhost:3306/ncc_portal?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password

### 4. Build and Run

    mvn clean install
    mvn spring-boot:run

Open http://localhost:8080 in your browser.

> ``data.sql`` is auto-loaded on first startup — default users, units, cadets, camps, and notices are seeded automatically.

---

## Default Credentials

> WARNING: Change these passwords immediately in a production environment.

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Officer | officer1 | officer123 |
| Cadet | cadet1 | cadet123 |
| Cadet | cadet2 | cadet123 |
| Cadet | cadet3 | cadet123 |

---

## Role-Based Access

| Feature | Admin | Officer | Cadet |
|---------|:-----:|:-------:|:-----:|
| Dashboard | Yes | Yes | Yes |
| Manage Officers | Yes | No | No |
| Manage Cadets | Yes | Yes | No |
| Mark Attendance | Yes | Yes | No |
| View Own Attendance | Yes | Yes | Yes |
| Manage Camps | Yes | Yes | No |
| View Camp Registrations | Yes | Yes | Yes |
| Manage Certificates | Yes | Yes | No |
| View Own Certificates | Yes | Yes | Yes |
| Post Notices | Yes | Yes | No |
| View Notices | Yes | Yes | Yes |
| Reports / CSV Export | Yes | Yes | No |
| Manage Units | Yes | No | No |

---

## URL Routes

| URL | Description | Roles |
|-----|-------------|-------|
| /login | Login page | Public |
| /admin/dashboard | Admin dashboard | ADMIN |
| /admin/officers | Manage officers | ADMIN |
| /officer/dashboard | Officer dashboard | OFFICER |
| /cadet/dashboard | Cadet dashboard | CADET |
| /cadets | Cadet list | ADMIN, OFFICER |
| /cadets/{id} | Cadet detail / edit | ADMIN, OFFICER |
| /attendance/mark | Mark attendance | ADMIN, OFFICER |
| /attendance/report | Attendance report | ADMIN, OFFICER |
| /camps | Camp list | ALL |
| /camps/{id}/registrations | Camp registrations | ADMIN, OFFICER |
| /certificates | Certificate list | ALL |
| /notices | Notices board | ALL |
| /reports | Reports and export | ADMIN, OFFICER |
| /units | Unit management | ADMIN |

---

## Configuration

Key properties in ``src/main/resources/application.properties``:

    # Server port
    server.port=8080

    # Session timeout
    server.servlet.session.timeout=30m

    # JPA DDL (use 'validate' or 'none' in production)
    spring.jpa.hibernate.ddl-auto=update

    # Thymeleaf caching (set to true in production)
    spring.thymeleaf.cache=false

    # File upload limits
    spring.servlet.multipart.max-file-size=5MB
    spring.servlet.multipart.max-request-size=5MB

---

## Build and Run

    # Development
    mvn spring-boot:run

    # Production JAR
    mvn clean package -DskipTests
    java -jar target/ncc-cadet-portal-1.0.0.jar

    # Run tests
    mvn test

---

## License

This project is licensed under the MIT License.

---

Built with pride for the National Cadet Corps of India.
"@
[System.IO.File]::WriteAllText("$PWD\README.md", $readme, [System.Text.Encoding]::UTF8)
Write-Host "README.md written successfully. Lines: $($readme.Split("`n").Count)"
