package com.grihom.backend.service;

import com.grihom.backend.dto.ImprovementDTO;
import com.grihom.backend.dto.ImprovementRequest;
import com.grihom.backend.exception.ResourceNotFoundException;
import com.grihom.backend.exception.UnauthorizedException;
import com.grihom.backend.model.Improvement;
import com.grihom.backend.model.Role;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.ImprovementRepository;
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
public class ImprovementService {

    private final ImprovementRepository improvementRepository;
    private final UserRepository userRepository;

    public List<ImprovementDTO> getAll(String room, String cost, String effort) {
        log.debug("Fetching improvements — room={} cost={} effort={}", room, cost, effort);
        return improvementRepository.findWithFilters(room, cost, effort)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ImprovementDTO getById(Long id) {
        return toDTO(improvementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Improvement", id)));
    }

    @Transactional
    public ImprovementDTO create(ImprovementRequest req, String creatorEmail) {
        User creator = findUser(creatorEmail);
        Improvement improvement = Improvement.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .room(req.getRoom() != null ? req.getRoom() : "All")
                .cost(req.getCost() != null ? req.getCost() : "Low")
                .effort(req.getEffort() != null ? req.getEffort() : "Medium")
                .roi(req.getRoi() != null ? req.getRoi() : "High")
                .impact(req.getImpact() != null ? req.getImpact() : 0)
                .duration(req.getDuration())
                .budgetRange(req.getBudgetRange())
                .imageUrl(req.getImageUrl())
                .indianSpecific(req.isIndianSpecific())
                .source("admin")
                .createdByUser(creator)
                .build();
        Improvement saved = improvementRepository.save(improvement);
        log.info("Improvement created: '{}' by {}", saved.getTitle(), creatorEmail);
        return toDTO(saved);
    }

    @Transactional
    public ImprovementDTO update(Long id, ImprovementRequest req, String editorEmail) {
        Improvement existing = improvementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Improvement", id));
        User editor = findUser(editorEmail);

        // DECOR can only edit their own improvements
        if (editor.getRole() == Role.DECOR) {
            if (existing.getCreatedByUser() == null
                    || !existing.getCreatedByUser().getEmail().equals(editorEmail)) {
                throw new UnauthorizedException("You can only edit your own improvement suggestions.");
            }
        }

        if (req.getTitle() != null)       existing.setTitle(req.getTitle());
        if (req.getDescription() != null) existing.setDescription(req.getDescription());
        if (req.getRoom() != null)        existing.setRoom(req.getRoom());
        if (req.getCost() != null)        existing.setCost(req.getCost());
        if (req.getEffort() != null)      existing.setEffort(req.getEffort());
        if (req.getRoi() != null)         existing.setRoi(req.getRoi());
        if (req.getImpact() != null)      existing.setImpact(req.getImpact());
        if (req.getDuration() != null)    existing.setDuration(req.getDuration());
        if (req.getBudgetRange() != null) existing.setBudgetRange(req.getBudgetRange());
        if (req.getImageUrl() != null)    existing.setImageUrl(req.getImageUrl());
        existing.setIndianSpecific(req.isIndianSpecific());

        log.info("Improvement updated: id={} by {}", id, editorEmail);
        return toDTO(improvementRepository.save(existing));
    }

    @Transactional
    public void delete(Long id, String deleterEmail) {
        Improvement existing = improvementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Improvement", id));
        User deleter = findUser(deleterEmail);

        if (deleter.getRole() == Role.DECOR) {
            if (existing.getCreatedByUser() == null
                    || !existing.getCreatedByUser().getEmail().equals(deleterEmail)) {
                throw new UnauthorizedException("You can only delete your own improvement suggestions.");
            }
        }

        improvementRepository.delete(existing);
        log.info("Improvement deleted: id={} by {}", id, deleterEmail);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    public ImprovementDTO toDTO(Improvement i) {
        return ImprovementDTO.builder()
                .id(i.getId())
                .title(i.getTitle())
                .description(i.getDescription())
                .room(i.getRoom())
                .cost(i.getCost())
                .effort(i.getEffort())
                .roi(i.getRoi())
                .impact(i.getImpact())
                .duration(i.getDuration())
                .budgetRange(i.getBudgetRange())
                .imageUrl(i.getImageUrl())
                .source(i.getSource())
                .indianSpecific(i.isIndianSpecific())
                .createdByEmail(i.getCreatedByUser() != null ? i.getCreatedByUser().getEmail() : null)
                .createdAt(i.getCreatedAt())
                .build();
    }
}
