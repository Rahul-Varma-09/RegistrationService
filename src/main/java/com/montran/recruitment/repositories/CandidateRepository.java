package com.montran.recruitment.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.montran.recruitment.entities.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>{
	
	List<Candidate> findByIdIn(List<Long> idList);
	
	List<Candidate> findByCvStatus(String cvStatus);
	
	Candidate findByNameAndEmail(String candidateName,String email);
	
}
