package com.jForce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jForce.model.Candidate;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, Integer> {

}
