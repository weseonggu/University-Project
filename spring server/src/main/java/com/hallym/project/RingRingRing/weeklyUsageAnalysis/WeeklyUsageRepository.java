package com.hallym.project.RingRingRing.weeklyUsageAnalysis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hallym.project.RingRingRing.weeklyUsageAnalysis.entity.WeeklyUsageAnalysisEntity;

@Repository
public interface WeeklyUsageRepository extends JpaRepository<WeeklyUsageAnalysisEntity, Long> {

    @Query(value = "SELECT SUM(w.timestamp) as duration, AVG(w.timestamp) as average " +
            "from WeeklyUsageAnalysisEntity as w " +
            "where w.user.id= :id " +
            "and w.week between :start and :end ")
    Map<String, Object> findWeeklyUsageByIdAndTimestampBetween(@Param("id") Long id , @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
