package com.nccportal.service;

import com.nccportal.dto.OfficerDTO;
import com.nccportal.entity.Officer;
import com.nccportal.entity.Unit;
import com.nccportal.entity.User;
import com.nccportal.exception.DuplicateRecordException;
import com.nccportal.exception.ResourceNotFoundException;
import com.nccportal.repository.OfficerRepository;
import com.nccportal.repository.UnitRepository;
import com.nccportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Officer management.
 */
@Service
@Transactional
public class OfficerService {

    @Autowired private OfficerRepository officerRepository;
    @Autowired private UnitRepository unitRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Officer> getAllOfficers() {
        return officerRepository.findAll();
    }

    public Officer getOfficerById(Long id) {
        return officerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Officer", "id", id));
    }

    public Optional<Officer> getOfficerByUserId(Long userId) {
        return officerRepository.findByUserId(userId);
    }

    public Officer addOfficer(OfficerDTO dto) {
        if (officerRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateRecordException("Officer with email " + dto.getEmail() + " already exists.");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateRecordException("Username '" + dto.getUsername() + "' already exists.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(
                        dto.getPassword() != null ? dto.getPassword() : "officer123"))
                .role(User.Role.OFFICER)
                .active(true)
                .build();
        userRepository.save(user);

        Unit unit = dto.getUnitId() != null
                ? unitRepository.findById(dto.getUnitId()).orElse(null) : null;

        Officer officer = Officer.builder()
                .name(dto.getName())
                .designation(dto.getDesignation())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .unit(unit)
                .user(user)
                .build();
        return officerRepository.save(officer);
    }

    public Officer updateOfficer(Long id, OfficerDTO dto) {
        Officer officer = getOfficerById(id);
        Unit unit = dto.getUnitId() != null
                ? unitRepository.findById(dto.getUnitId()).orElse(null) : null;
        officer.setName(dto.getName());
        officer.setDesignation(dto.getDesignation());
        officer.setEmail(dto.getEmail());
        officer.setPhone(dto.getPhone());
        officer.setUnit(unit);
        return officerRepository.save(officer);
    }

    public void deleteOfficer(Long id) {
        Officer officer = getOfficerById(id);
        officerRepository.delete(officer);
    }
}
