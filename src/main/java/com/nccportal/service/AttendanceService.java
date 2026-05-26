package com.nccportal.service;

import com.nccportal.dto.AttendanceDTO;
import com.nccportal.entity.Attendance;
import com.nccportal.entity.Cadet;
import com.nccportal.exception.DuplicateRecordException;
import com.nccportal.exception.ResourceNotFoundException;
import com.nccportal.repository.AttendanceRepository;
import com.nccportal.repository.CadetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Attendance management.
 */
@Service
@Transactional
public class AttendanceService {

    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private CadetRepository cadetRepository;

    /**
     * Mark attendance for a cadet on a given date.
     */
    public Attendance markAttendance(AttendanceDTO dto) {
        Cadet cadet = cadetRepository.findById(dto.getCadetId())
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", dto.getCadetId()));

        // Prevent duplicate entry for same cadet, date, type
        if (attendanceRepository.existsByCadetAndDateAndType(cadet, dto.getDate(), dto.getType())) {
            throw new DuplicateRecordException("Attendance already marked for this cadet on " + dto.getDate());
        }

        Attendance attendance = Attendance.builder()
                .cadet(cadet)
                .date(dto.getDate())
                .status(dto.getStatus())
                .type(dto.getType())
                .remarks(dto.getRemarks())
                .build();

        return attendanceRepository.save(attendance);
    }

    /**
     * Get all attendance records for a cadet.
     */
    public List<Attendance> getAttendanceByCadet(Long cadetId) {
        Cadet cadet = cadetRepository.findById(cadetId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", cadetId));
        return attendanceRepository.findByCadet(cadet);
    }

    /**
     * Get attendance records for a date and type (e.g., all cadets on parade day).
     */
    public List<Attendance> getAttendanceByDate(LocalDate date, Attendance.AttendanceType type) {
        return attendanceRepository.findByDateAndType(date, type);
    }

    /**
     * Calculate attendance percentage for a cadet in a date range.
     */
    public double calculateAttendancePercentage(Long cadetId, LocalDate from, LocalDate to) {
        Cadet cadet = cadetRepository.findById(cadetId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", cadetId));

        long total = attendanceRepository.countByCadetAndDateBetween(cadet, from, to);
        if (total == 0) return 0.0;

        long present = attendanceRepository.countPresentByDateRange(cadet, from, to);
        return Math.round((present * 100.0 / total) * 10.0) / 10.0; // Round to 1 decimal
    }

    /**
     * Monthly attendance report for a unit.
     */
    public List<Attendance> getMonthlyReportForUnit(Long unitId, int month, int year) {
        return attendanceRepository.findByUnitAndMonth(unitId, month, year);
    }

    /**
     * Get all attendance records.
     */
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    /**
     * Delete attendance record.
     */
    public void deleteAttendance(Long id) {
        Attendance att = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));
        attendanceRepository.delete(att);
    }
}
