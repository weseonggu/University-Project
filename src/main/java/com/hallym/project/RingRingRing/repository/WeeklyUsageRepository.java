package com.hallym.project.RingRingRing.repository;

import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import com.hallym.project.RingRingRing.Entity.WeeklyUsageAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyUsageRepository extends JpaRepository<WeeklyUsageAnalysisEntity, Long> {

    @Query("SELECT new com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO(FUNCTION('yearweek', w.timestamp), SUM(w.duration))" +
            "from WeeklyUsageAnalysisEntity as w " +
            "where w.id = :id " +
            "group by function('yearweek', w.timestamp)")
    List<WeeklyUsageDTO> findWeeklyUsageById(@Param("id") Long id);



}
