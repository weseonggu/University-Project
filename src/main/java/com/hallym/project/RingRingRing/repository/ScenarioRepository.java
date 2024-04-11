package com.hallym.project.RingRingRing.repository;

import com.hallym.project.RingRingRing.Entity.ScenarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioRepository extends JpaRepository<ScenarioEntity, Long> {

}
