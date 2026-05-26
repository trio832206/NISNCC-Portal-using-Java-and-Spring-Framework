package com.nccportal.repository;

import com.nccportal.entity.Cadet;
import com.nccportal.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Cadet entity — supports search, filter, pagination.
 */
@Repository
public interface CadetRepository extends JpaRepository<Cadet, Long> {

    // Find cadet by linked user account
    Optional<Cadet> findByUserId(Long userId);

    // Find by unit
    List<Cadet> findByUnit(Unit unit);

    Page<Cadet> findByUnit(Unit unit, Pageable pageable);

    // Find by rank
    Page<Cadet> findByRank(Cadet.Rank rank, Pageable pageable);

    // Search by name or email (case-insensitive)
    @Query("SELECT c FROM Cadet c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR c.cadetId LIKE CONCAT('%', :query, '%')")
    Page<Cadet> searchCadets(@Param("query") String query, Pageable pageable);

    // Count cadets in a unit
    long countByUnit(Unit unit);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Filter by unit and rank
    Page<Cadet> findByUnitAndRank(Unit unit, Cadet.Rank rank, Pageable pageable);
}
