package com.montran.recruitment.services;

import org.springframework.web.multipart.MultipartFile;

import com.montran.recruitment.http.response.GenericResponse;

public interface CandidateService {
	
	public GenericResponse<?> registerCandidate(String data, MultipartFile file);
	
	public GenericResponse<?> fetchCandidatesByCvStatus(String cvStatus);

}
