package com.nccportal.repository;

import com.nccportal.entity.Attendance;
import com.nccportal.entity.Cadet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Attendance entity — supports monthly reports and percentage calculation.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByCadet(Cadet cadet);

    List<Attendance> findByCadetAndDateBetween(Cadet cadet, LocalDate from, LocalDate to);

    List<Attendance> findByDateAndType(LocalDate date, Attendance.AttendanceType type);

    // Count how many times a cadet was present in a date range
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.cadet = :cadet " +
           "AND a.status = 'PRESENT' AND a.date BETWEEN :from AND :to")
    long countPresentByDateRange(@Param("cadet") Cadet cadet,
                                  @Param("from") LocalDate from,
                                  @Param("to") LocalDate to);

    // Count total records for percentage calculation
    long countByCadetAndDateBetween(Cadet cadet, LocalDate from, LocalDate to);

    // Monthly attendance for a unit
    @Query("SELECT a FROM Attendance a JOIN a.cadet c WHERE c.unit.id = :unitId " +
           "AND MONTH(a.date) = :month AND YEAR(a.date) = :year")
    List<Attendance> findByUnitAndMonth(@Param("unitId") Long unitId,
                                         @Param("month") int month,
                                         @Param("year") int year);

    boolean existsByCadetAndDateAndType(Cadet cadet, LocalDate date, Attendance.AttendanceType type);
}
