package com.nccportal.service;

import com.nccportal.dto.CadetDTO;
import com.nccportal.entity.Cadet;
import com.nccportal.entity.Unit;
import com.nccportal.entity.User;
import com.nccportal.exception.DuplicateRecordException;
import com.nccportal.exception.ResourceNotFoundException;
import com.nccportal.repository.CadetRepository;
import com.nccportal.repository.UnitRepository;
import com.nccportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Cadet management.
 * Handles CRUD, search, filter, pagination, and Aadhaar masking.
 */
@Service
@Transactional
public class CadetService {

    @Autowired
    private CadetRepository cadetRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get all cadets with pagination and sorting.
     */
    public Page<Cadet> getAllCadets(int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return cadetRepository.findAll(pageable);
    }

    /**
     * Search cadets by name/email/cadetId with pagination.
     */
    public Page<Cadet> searchCadets(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return cadetRepository.searchCadets(query, pageable);
    }

    /**
     * Get cadets by unit.
     */
    public Page<Cadet> getCadetsByUnit(Long unitId, int page, int size) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", unitId));
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return cadetRepository.findByUnit(unit, pageable);
    }

    /**
     * Get cadet by ID.
     */
    public Cadet getCadetById(Long id) {
        return cadetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", id));
    }

    /**
     * Get cadet by linked user ID.
     */
    public Optional<Cadet> getCadetByUserId(Long userId) {
        return cadetRepository.findByUserId(userId);
    }

    /**
     * Add a new cadet — creates a linked user account as well.
     */
    public Cadet addCadet(CadetDTO dto) {
        // Check for duplicate email
        if (cadetRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateRecordException("A cadet with email " + dto.getEmail() + " already exists.");
        }

        // Create linked user account for the cadet
        User user = User.builder()
                .username(generateUsername(dto.getName()))
                .password(passwordEncoder.encode("cadet123")) // default password
                .role(User.Role.CADET)
                .active(true)
                .build();
        userRepository.save(user);

        // Fetch unit if provided
        Unit unit = dto.getUnitId() != null
                ? unitRepository.findById(dto.getUnitId()).orElse(null)
                : null;

        // Generate cadet ID
        String cadetId = generateCadetId();

        // Build and save cadet
        Cadet cadet = Cadet.builder()
                .cadetId(cadetId)
                .name(dto.getName())
                .dob(dto.getDob())
                .gender(dto.getGender())
                .fatherName(dto.getFatherName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .college(dto.getCollege())
                .unit(unit)
                .rank(dto.getRank() != null ? dto.getRank() : Cadet.Rank.CADET)
                .enrollmentDate(dto.getEnrollmentDate() != null ? dto.getEnrollmentDate() : LocalDate.now())
                .bloodGroup(dto.getBloodGroup())
                .aadhaarMasked(maskAadhaar(dto.getAadhaar()))
                .user(user)
                .build();

        return cadetRepository.save(cadet);
    }

    /**
     * Update existing cadet.
     */
    public Cadet updateCadet(Long id, CadetDTO dto) {
        Cadet cadet = getCadetById(id);

        // Check duplicate email (but allow same cadet's own email)
        if (!cadet.getEmail().equals(dto.getEmail()) && cadetRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateRecordException("Email " + dto.getEmail() + " is already in use.");
        }

        Unit unit = dto.getUnitId() != null
                ? unitRepository.findById(dto.getUnitId()).orElse(null)
                : null;

        cadet.setName(dto.getName());
        cadet.setDob(dto.getDob());
        cadet.setGender(dto.getGender());
        cadet.setFatherName(dto.getFatherName());
        cadet.setEmail(dto.getEmail());
        cadet.setPhone(dto.getPhone());
        cadet.setAddress(dto.getAddress());
        cadet.setCollege(dto.getCollege());
        cadet.setUnit(unit);
        cadet.setRank(dto.getRank());
        cadet.setBloodGroup(dto.getBloodGroup());
        if (dto.getAadhaar() != null && !dto.getAadhaar().isBlank()) {
            cadet.setAadhaarMasked(maskAadhaar(dto.getAadhaar()));
        }

        return cadetRepository.save(cadet);
    }

    /**
     * Delete cadet by ID.
     */
    public void deleteCadet(Long id) {
        Cadet cadet = getCadetById(id);
        cadetRepository.delete(cadet);
    }

    /**
     * List all cadets (no pagination — for CSV export).
     */
    public List<Cadet> getAllCadets() {
        return cadetRepository.findAll(Sort.by("name").ascending());
    }

    // ---- Helper Methods ----

    /**
     * Mask Aadhaar: show only last 4 digits.
     * Input: 123456789012 → Output: XXXX-XXXX-9012
     */
    private String maskAadhaar(String aadhaar) {
        if (aadhaar == null || aadhaar.length() != 12)
            return null;
        return "XXXX-XXXX-" + aadhaar.substring(8);
    }

    /**
     * Generate a username from cadet name (lowercase, no spaces, + random suffix).
     */
    private String generateUsername(String name) {
        String base = name.toLowerCase().replaceAll("\\s+", "");
        String suffix = String.valueOf(System.currentTimeMillis()).substring(9);
        String username = base + suffix;
        // Ensure uniqueness
        while (userRepository.existsByUsername(username)) {
            username = base + System.currentTimeMillis();
        }
        return username;
    }

    /**
     * Generate sequential cadet ID like NCC/2024/001.
     */
    private String generateCadetId() {
        long count = cadetRepository.count() + 1;
        return String.format("NCC/%s/%03d", LocalDate.now().getYear(), count);
    }
}
