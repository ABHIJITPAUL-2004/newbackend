package com.grihom.backend.repository;

import com.grihom.backend.model.PlannedImprovement;
import com.grihom.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannedImprovementRepository extends JpaRepository<PlannedImprovement, Long> {
    List<PlannedImprovement> findByUser(User user);
    Optional<PlannedImprovement> findByUserAndImprovementId(User user, String improvementId);

    @Transactional
    void deleteByUserAndImprovementId(User user, String improvementId);
}
