package com.nccportal.repository;

import com.nccportal.entity.Camp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Camp entity.
 */
@Repository
public interface CampRepository extends JpaRepository<Camp, Long> {

    List<Camp> findByType(Camp.CampType type);

    // Upcoming camps (start date is in the future)
    List<Camp> findByStartDateAfterOrderByStartDateAsc(LocalDate date);
}
