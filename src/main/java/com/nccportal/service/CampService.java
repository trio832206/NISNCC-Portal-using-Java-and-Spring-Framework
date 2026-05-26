package com.nccportal.service;

import com.nccportal.dto.CampDTO;
import com.nccportal.entity.Camp;
import com.nccportal.entity.Cadet;
import com.nccportal.entity.CampRegistration;
import com.nccportal.exception.DuplicateRecordException;
import com.nccportal.exception.InvalidInputException;
import com.nccportal.exception.ResourceNotFoundException;
import com.nccportal.repository.CadetRepository;
import com.nccportal.repository.CampRegistrationRepository;
import com.nccportal.repository.CampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Camp management and cadet registration.
 */
@Service
@Transactional
public class CampService {

    @Autowired private CampRepository campRepository;
    @Autowired private CampRegistrationRepository registrationRepository;
    @Autowired private CadetRepository cadetRepository;

    public List<Camp> getAllCamps() {
        return campRepository.findAll();
    }

    public Camp getCampById(Long id) {
        return campRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camp", "id", id));
    }

    public Camp addCamp(CampDTO dto) {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new InvalidInputException("End date cannot be before start date.");
        }
        Camp camp = Camp.builder()
                .campName(dto.getCampName())
                .type(dto.getType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .maxCadets(dto.getMaxCadets())
                .build();
        return campRepository.save(camp);
    }

    public Camp updateCamp(Long id, CampDTO dto) {
        Camp camp = getCampById(id);
        camp.setCampName(dto.getCampName());
        camp.setType(dto.getType());
        camp.setStartDate(dto.getStartDate());
        camp.setEndDate(dto.getEndDate());
        camp.setLocation(dto.getLocation());
        camp.setDescription(dto.getDescription());
        camp.setMaxCadets(dto.getMaxCadets());
        return campRepository.save(camp);
    }

    public void deleteCamp(Long id) {
        Camp camp = getCampById(id);
        campRepository.delete(camp);
    }

    /**
     * Register a cadet for a camp.
     */
    public CampRegistration registerCadet(Long campId, Long cadetId) {
        Camp camp = getCampById(campId);
        Cadet cadet = cadetRepository.findById(cadetId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", cadetId));

        if (registrationRepository.existsByCampAndCadet(camp, cadet)) {
            throw new DuplicateRecordException("Cadet is already registered for this camp.");
        }

        CampRegistration reg = CampRegistration.builder()
                .camp(camp)
                .cadet(cadet)
                .attendance(CampRegistration.AttendanceStatus.REGISTERED)
                .performance(CampRegistration.Performance.NOT_RATED)
                .build();

        return registrationRepository.save(reg);
    }

    public List<CampRegistration> getRegistrationsByCamp(Long campId) {
        Camp camp = getCampById(campId);
        return registrationRepository.findByCamp(camp);
    }

    public List<CampRegistration> getRegistrationsByCadet(Long cadetId) {
        Cadet cadet = cadetRepository.findById(cadetId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", cadetId));
        return registrationRepository.findByCadet(cadet);
    }

    public List<Camp> getUpcomingCamps() {
        return campRepository.findByStartDateAfterOrderByStartDateAsc(LocalDate.now());
    }
}
