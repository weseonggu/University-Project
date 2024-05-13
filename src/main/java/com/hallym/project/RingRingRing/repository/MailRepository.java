package com.hallym.project.RingRingRing.repository;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hallym.project.RingRingRing.Entity.EmailAuthenticationEntity;

import jakarta.transaction.Transactional;
@Repository
public interface MailRepository extends JpaRepository<EmailAuthenticationEntity, Long> {
	
	List<EmailAuthenticationEntity> findByEmail(String email, Sort sort);
	
	
	@Transactional
	void deleteByEmail(String email);

}
