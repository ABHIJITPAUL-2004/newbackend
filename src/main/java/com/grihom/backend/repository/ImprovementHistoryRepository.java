package com.grihom.backend.repository;

import com.grihom.backend.model.ImprovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImprovementHistoryRepository extends JpaRepository<ImprovementHistory, Long> {
    List<ImprovementHistory> findAllByOrderByPerformedAtDesc();
    List<ImprovementHistory> findTop10ByOrderByPerformedAtDesc();
}
