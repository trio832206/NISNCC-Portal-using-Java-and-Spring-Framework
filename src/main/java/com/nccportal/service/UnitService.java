package com.nccportal.service;

import com.nccportal.dto.UnitDTO;
import com.nccportal.entity.Unit;
import com.nccportal.exception.DuplicateRecordException;
import com.nccportal.exception.ResourceNotFoundException;
import com.nccportal.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service layer for Unit management.
 */
@Service
@Transactional
public class UnitService {

    @Autowired private UnitRepository unitRepository;

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Unit getUnitById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", id));
    }

    public Unit addUnit(UnitDTO dto) {
        if (unitRepository.existsByUnitName(dto.getUnitName())) {
            throw new DuplicateRecordException("Unit '" + dto.getUnitName() + "' already exists.");
        }
        Unit unit = Unit.builder()
                .unitName(dto.getUnitName())
                .battalion(dto.getBattalion())
                .state(dto.getState())
                .district(dto.getDistrict())
                .description(dto.getDescription())
                .build();
        return unitRepository.save(unit);
    }

    public Unit updateUnit(Long id, UnitDTO dto) {
        Unit unit = getUnitById(id);
        unit.setUnitName(dto.getUnitName());
        unit.setBattalion(dto.getBattalion());
        unit.setState(dto.getState());
        unit.setDistrict(dto.getDistrict());
        unit.setDescription(dto.getDescription());
        return unitRepository.save(unit);
    }

    public void deleteUnit(Long id) {
        Unit unit = getUnitById(id);
        unitRepository.delete(unit);
    }
}
