package com.grihom.backend.repository;

import com.grihom.backend.model.Report;
import com.grihom.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserOrderByCreatedAtDesc(User user);
    long countByUser(User user);

    @Query("SELECT r FROM Report r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Report> findByUserIdOrdered(@Param("userId") Long userId);
}
