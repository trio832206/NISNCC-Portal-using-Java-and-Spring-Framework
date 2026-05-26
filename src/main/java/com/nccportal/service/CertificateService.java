package com.nccportal.service;

import com.nccportal.dto.CertificateDTO;
import com.nccportal.entity.Cadet;
import com.nccportal.entity.Certificate;
import com.nccportal.exception.ResourceNotFoundException;
import com.nccportal.repository.CadetRepository;
import com.nccportal.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service layer for Certificate management.
 */
@Service
@Transactional
public class CertificateService {

    @Autowired private CertificateRepository certificateRepository;
    @Autowired private CadetRepository cadetRepository;

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public List<Certificate> getCertificatesByCadet(Long cadetId) {
        Cadet cadet = cadetRepository.findById(cadetId)
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", cadetId));
        return certificateRepository.findByCadet(cadet);
    }

    public Certificate addCertificate(CertificateDTO dto) {
        Cadet cadet = cadetRepository.findById(dto.getCadetId())
                .orElseThrow(() -> new ResourceNotFoundException("Cadet", "id", dto.getCadetId()));

        Certificate certificate = Certificate.builder()
                .cadet(cadet)
                .type(dto.getType())
                .result(dto.getResult())
                .examDate(dto.getExamDate())
                .remarks(dto.getRemarks())
                .build();

        return certificateRepository.save(certificate);
    }

    public Certificate updateCertificate(Long id, CertificateDTO dto) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));
        cert.setResult(dto.getResult());
        cert.setExamDate(dto.getExamDate());
        cert.setRemarks(dto.getRemarks());
        return certificateRepository.save(cert);
    }

    public void deleteCertificate(Long id) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));
        certificateRepository.delete(cert);
    }

    public Certificate getCertificateById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));
    }
}
