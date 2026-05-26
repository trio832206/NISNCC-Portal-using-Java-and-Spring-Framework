package com.nccportal.service;

import com.nccportal.entity.Cadet;
import com.nccportal.entity.Attendance;
import com.nccportal.repository.AttendanceRepository;
import com.nccportal.repository.CadetRepository;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Writer;
import java.util.List;

/**
 * Service for generating reports and CSV exports.
 */
@Service
public class ReportService {

    @Autowired private CadetRepository cadetRepository;
    @Autowired private AttendanceRepository attendanceRepository;

    /**
     * Write all cadets as CSV to the given writer.
     * Used by the ReportController to stream the CSV download.
     */
    public void exportCadetsToCSV(Writer writer) {
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Header row
            String[] header = {"Cadet ID", "Name", "DOB", "Gender", "Email",
                               "Phone", "College", "Unit", "Rank", "Blood Group", "Enrollment Date"};
            csvWriter.writeNext(header);

            // Data rows
            List<Cadet> cadets = cadetRepository.findAll();
            for (Cadet c : cadets) {
                csvWriter.writeNext(new String[]{
                        c.getCadetId(),
                        c.getName(),
                        c.getDob() != null ? c.getDob().toString() : "",
                        c.getGender() != null ? c.getGender().name() : "",
                        c.getEmail(),
                        c.getPhone(),
                        c.getCollege(),
                        c.getUnit() != null ? c.getUnit().getUnitName() : "",
                        c.getRank() != null ? c.getRank().name() : "",
                        c.getBloodGroup(),
                        c.getEnrollmentDate() != null ? c.getEnrollmentDate().toString() : ""
                });
            }
        } catch (Exception e) {
            throw new com.nccportal.exception.DatabaseException("Failed to generate CSV report", e);
        }
    }

    /**
     * Write all attendance records as CSV.
     */
    public void exportAttendanceToCSV(Writer writer) {
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            String[] header = {"Cadet ID", "Cadet Name", "Date", "Status", "Type", "Remarks"};
            csvWriter.writeNext(header);

            List<Attendance> records = attendanceRepository.findAll();
            for (Attendance a : records) {
                csvWriter.writeNext(new String[]{
                        a.getCadet().getCadetId(),
                        a.getCadet().getName(),
                        a.getDate().toString(),
                        a.getStatus().name(),
                        a.getType().name(),
                        a.getRemarks() != null ? a.getRemarks() : ""
                });
            }
        } catch (Exception e) {
            throw new com.nccportal.exception.DatabaseException("Failed to generate attendance CSV", e);
        }
    }
}
