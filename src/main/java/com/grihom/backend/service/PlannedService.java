package com.grihom.backend.service;

import com.grihom.backend.dto.PlannedDTO;
import com.grihom.backend.dto.PlannedRequest;
import com.grihom.backend.exception.ResourceNotFoundException;
import com.grihom.backend.model.PlannedImprovement;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.PlannedImprovementRepository;
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
public class PlannedService {

    private final PlannedImprovementRepository plannedRepo;
    private final UserRepository userRepository;

    public List<PlannedDTO> getPlanned(String email) {
        User user = findUser(email);
        return plannedRepo.findByUser(user)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public PlannedDTO addPlanned(PlannedRequest req, String email) {
        User user = findUser(email);
        PlannedImprovement planned = PlannedImprovement.builder()
                .improvementId(req.getImprovementId())
                .user(user)
                .build();
        PlannedImprovement saved = plannedRepo.save(planned);
        log.info("Planned improvement added: improvementId={} by {}", req.getImprovementId(), email);
        return toDTO(saved);
    }

    @Transactional
    public void removePlanned(String improvementId, String email) {
        User user = findUser(email);
        plannedRepo.deleteByUserAndImprovementId(user, improvementId);
        log.info("Planned improvement removed: improvementId={} by {}", improvementId, email);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    private PlannedDTO toDTO(PlannedImprovement p) {
        return PlannedDTO.builder()
                .id(p.getId())
                .improvementId(p.getImprovementId())
                .addedAt(p.getAddedAt())
                .build();
    }
}
