package com.hallym.project.RingRingRing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hallym.project.RingRingRing.Entity.UserEntity;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	List<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
	@Transactional
	void deleteById(int id);
	
	
}
