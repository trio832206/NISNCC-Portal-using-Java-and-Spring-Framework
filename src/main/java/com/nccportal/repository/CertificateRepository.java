package com.nccportal.repository;

import com.nccportal.entity.Cadet;
import com.nccportal.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Certificate entity.
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByCadet(Cadet cadet);

    Optional<Certificate> findByCadetAndType(Cadet cadet, Certificate.CertificateType type);

    List<Certificate> findByType(Certificate.CertificateType type);

    long countByResult(Certificate.CertificateResult result);
}
