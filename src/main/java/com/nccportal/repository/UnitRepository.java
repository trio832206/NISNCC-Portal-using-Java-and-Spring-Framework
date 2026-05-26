package com.nccportal.repository;

import com.nccportal.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Unit entity.
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    boolean existsByUnitName(String unitName);
}
