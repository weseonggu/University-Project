package com.hallym.project.RingRingRing.repository;

import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import com.hallym.project.RingRingRing.Entity.WeeklyUsageAnalysisEntity;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeeklyUsageRepository extends JpaRepository<WeeklyUsageAnalysisEntity, Long> {

    @Query(value =
            "SELECT new com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO( " +
            "w.id, w.timestamp, SUM(w.timestamp), w.week) " +
            "from WeeklyUsageAnalysisEntity as w " +
            "where w.user.email= :email " +
            "and w.week between :start and :end " +
            "group by w.id "
    )
    List<WeeklyUsageDTO> findWeeklyUsageByEmailAndTimestampBetween(@Param("email") String email, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
//            "group by w.user.email "
//new com.hallym.project.RingRingRing.Entity.WeeklyUsageAnalysisEntity(FUNCTION('yearweek', w.timestamp)


//@Query("SELECT SUM(w.timestamp) as hlp " +
//        "from WeeklyUsageAnalysisEntity as w " +
//        "where w.user.email= :email " )
