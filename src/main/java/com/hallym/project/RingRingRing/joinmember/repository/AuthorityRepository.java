package com.hallym.project.RingRingRing.joinmember.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hallym.project.RingRingRing.joinmember.entity.AuthorityEntity;
@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long>{

}
