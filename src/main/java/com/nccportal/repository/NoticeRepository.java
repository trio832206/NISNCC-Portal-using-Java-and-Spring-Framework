package com.nccportal.repository;

import com.nccportal.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Notice entity — supports role-based filtering.
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // Get notices visible to a particular role (ALL + role-specific)
    @Query("SELECT n FROM Notice n WHERE n.targetRole = 'ALL' OR n.targetRole = :role ORDER BY n.postedDate DESC")
    List<Notice> findByTargetRole(@org.springframework.data.repository.query.Param("role") Notice.TargetRole role);

    // All notices sorted by date descending
    List<Notice> findAllByOrderByPostedDateDesc();
}
