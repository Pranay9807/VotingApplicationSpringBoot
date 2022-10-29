package com.jForce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jForce.model.Candidate;
import com.jForce.repository.CandidateRepository;

@Service
public class CandidateService {

	@Autowired
	private CandidateRepository cr;

	// ADD
	public Candidate add(Candidate candidate) {
		return this.cr.save(candidate);
	}

	// GET ALL
	public Iterable<Candidate> getAll() {
		return this.cr.findAll();
	}

	// GET BY ID
	public Candidate getById(Integer id) {
		return this.cr.findById(id).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id does not exists");
		});
	}

	// UPDATE BY ID
	public Candidate update(Candidate candidate, Integer id) {
		this.getById(id);
		candidate.setId(id);
		return this.cr.save(candidate);
	}

	// DELETE BY ID
	public void deleteById(Integer id) {
		this.cr.deleteById(id);
	}
}
