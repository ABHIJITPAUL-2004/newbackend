package com.grihom.backend.repository;

import com.grihom.backend.model.Improvement;
import com.grihom.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImprovementRepository extends JpaRepository<Improvement, Long> {

    // Derived queries
    List<Improvement> findByRoom(String room);
    List<Improvement> findByCost(String cost);
    List<Improvement> findByCreatedByUser(User user);
    List<Improvement> findBySource(String source);

    // JPQL with dynamic filters
    @Query("SELECT i FROM Improvement i WHERE " +
           "(:room IS NULL OR i.room = :room) AND " +
           "(:cost IS NULL OR i.cost = :cost) AND " +
           "(:effort IS NULL OR i.effort = :effort) " +
           "ORDER BY i.createdAt DESC")
    List<Improvement> findWithFilters(
            @Param("room") String room,
            @Param("cost") String cost,
            @Param("effort") String effort);

    @Query("SELECT COUNT(i) FROM Improvement i WHERE i.source = 'admin'")
    long countAdminImprovements();
}
