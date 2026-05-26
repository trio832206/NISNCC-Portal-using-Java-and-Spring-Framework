package com.nccportal.service;

import com.nccportal.dto.NoticeDTO;
import com.nccportal.entity.Notice;
import com.nccportal.exception.ResourceNotFoundException;
import com.nccportal.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Notice (bulletin board) management.
 */
@Service
@Transactional
public class NoticeService {

    @Autowired private NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAllByOrderByPostedDateDesc();
    }

    /**
     * Get notices visible to a specific role (includes ALL + role-specific).
     */
    public List<Notice> getNoticesForRole(Notice.TargetRole role) {
        return noticeRepository.findByTargetRole(role);
    }

    public Notice addNotice(NoticeDTO dto, String postedBy) {
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .postedBy(postedBy)
                .postedDate(LocalDate.now())
                .targetRole(dto.getTargetRole())
                .build();
        return noticeRepository.save(notice);
    }

    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice", "id", id));
    }

    public void deleteNotice(Long id) {
        Notice notice = getNoticeById(id);
        noticeRepository.delete(notice);
    }
}
