package com.grihom.backend.service;

import com.grihom.backend.dto.ReportDTO;
import com.grihom.backend.dto.ReportRequest;
import com.grihom.backend.exception.ResourceNotFoundException;
import com.grihom.backend.exception.UnauthorizedException;
import com.grihom.backend.model.Report;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.ReportRepository;
import com.grihom.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public List<ReportDTO> getReportsForUser(String email) {
        User user = findUser(email);
        return reportRepository.findByUserOrderByCreatedAtDesc(user)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ReportDTO createReport(ReportRequest req, String email) {
        User user = findUser(email);
        Report report = Report.builder()
                .title(req.getTitle())
                .valorScore(req.getValorScore())
                .propertyData(req.getPropertyData())
                .recommendations(req.getRecommendations())
                .user(user)
                .build();
        Report saved = reportRepository.save(report);
        log.info("Report saved: id={} for user={}", saved.getId(), email);
        return toDTO(saved);
    }

    @Transactional
    public void deleteReport(Long id, String email) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report", id));
        if (!report.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only delete your own reports.");
        }
        reportRepository.delete(report);
        log.info("Report deleted: id={} by {}", id, email);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    private ReportDTO toDTO(Report r) {
        return ReportDTO.builder()
                .id(r.getId())
                .title(r.getTitle())
                .valorScore(r.getValorScore())
                .propertyData(r.getPropertyData())
                .recommendations(r.getRecommendations())
                .createdAt(r.getCreatedAt())
                .timestamp(r.getCreatedAt() != null ? r.getCreatedAt().toString() : null)
                .build();
    }
}
