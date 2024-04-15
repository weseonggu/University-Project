package com.hallym.project.RingRingRing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hallym.project.RingRingRing.Entity.TemporaryEmail;
import com.hallym.project.RingRingRing.Entity.UserEntity;

import jakarta.transaction.Transactional;

public interface TemporaryEmailRepository extends JpaRepository<TemporaryEmail, Long>{
	List<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
	
	@Modifying
	@Transactional
    @Query("DELETE FROM TemporaryEmail t WHERE t.checkTime <= :tenMinutesAgo")
    void deleteOlderThanTenMinutes(LocalDateTime tenMinutesAgo);
	
	
}
