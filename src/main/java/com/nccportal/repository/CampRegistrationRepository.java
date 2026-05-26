package com.nccportal.repository;

import com.nccportal.entity.Cadet;
import com.nccportal.entity.Camp;
import com.nccportal.entity.CampRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for CampRegistration — tracks which cadets joined which camps.
 */
@Repository
public interface CampRegistrationRepository extends JpaRepository<CampRegistration, Long> {

    List<CampRegistration> findByCamp(Camp camp);

    List<CampRegistration> findByCadet(Cadet cadet);

    Optional<CampRegistration> findByCampAndCadet(Camp camp, Cadet cadet);

    boolean existsByCampAndCadet(Camp camp, Cadet cadet);

    long countByCamp(Camp camp);
}
