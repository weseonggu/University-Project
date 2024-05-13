package com.hallym.project.RingRingRing.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hallym.project.RingRingRing.Entity.AuthorityEntity;
@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long>{

}
