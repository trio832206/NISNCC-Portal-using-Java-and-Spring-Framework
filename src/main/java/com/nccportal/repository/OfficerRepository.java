package com.nccportal.repository;

import com.nccportal.entity.Officer;
import com.nccportal.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Officer entity.
 */
@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {

    Optional<Officer> findByUserId(Long userId);

    List<Officer> findByUnit(Unit unit);

    boolean existsByEmail(String email);
}
