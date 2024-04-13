package com.hallym.project.RingRingRing.repository;

import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import com.hallym.project.RingRingRing.Entity.ScenarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioRepository extends JpaRepository<ScenarioEntity, Long> {

    @Query("select com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO(function('yearweek', s.timestamp), sum(s.duration))" +
            "from ScenarioEntity as s " +
            "where s.id = :id " +
            "group by function('yearweek', s.timestamp)")
    List<WeeklyUsageDTO> findWeeklyUsageById(@Param("id") Long id);



}
