package com.montran.recruitment.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.montran.recruitment.entities.Candidate;
import com.montran.recruitment.http.response.GenericResponse;
import com.montran.recruitment.repositories.CandidateRepository;
import com.montran.recruitment.services.CandidateService;

@RestController
@RequestMapping("/candidate")
@CrossOrigin(allowedHeaders = "*")
public class RegistrationController {
	
	@Autowired private CandidateService candidateService;
	@Autowired private CandidateRepository candidateRepository;
	
	@PostMapping("/open/registration")
	public GenericResponse<?> registerCandidate(@RequestParam(value = "data") String data, @RequestParam(value = "file") MultipartFile file) throws IOException {
		return candidateService.registerCandidate(data, file);
	}
		
	@GetMapping("/fetch/{collegeName}")
	public List<Candidate> getCandidateIdsFromCollegeName(@PathVariable("collegeName") String collegeName) {
		RestTemplate restTemplate = new RestTemplate();
		Long[] idList = restTemplate.getForEntity("http://localhost:8080/access/getByCollegeName/"+ collegeName, Long[].class).getBody();
		return candidateRepository.findByIdIn(Arrays.asList(idList));
	}
	
	@GetMapping("/fetchByCVStatus/{cvStatus}")
	public GenericResponse<?> fetchByCVStatus(@PathVariable ("cvStatus") String cvStatus)  {
		return candidateService.fetchCandidatesByCvStatus(cvStatus);
	}

	
}
