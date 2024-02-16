package com.montran.recruitment.services.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.montran.recruitment.entities.Candidate;
import com.montran.recruitment.enums.CvReviewStage;
import com.montran.recruitment.http.response.GenericResponse;
import com.montran.recruitment.repositories.CandidateRepository;
import com.montran.recruitment.services.CandidateService;

@Service
public class CandidateServiceImpl implements CandidateService {
	
	private static final Logger log = LoggerFactory.getLogger(CandidateServiceImpl.class);
	
	@Autowired private CandidateRepository candidateRepository;
	
	@Value("${channelServiceUrl}") 
	private String channelServiceUrl;

	@Value("${channelServiceUpdateSourceEndpoint}")
	private String channelServiceUpdateSourceEndpoint;
	
	private Candidate buildCandidateObject(JsonNode rootNode, MultipartFile file) {
		try {
			Candidate candidate = new Candidate();
			candidate.setName(rootNode.get("name").asText());
			candidate.setEmail(rootNode.get("email").asText());
			candidate.setPhoneNumber(rootNode.get("phoneNumber").asText());
			candidate.setEducation(rootNode.get("education").asText());
			candidate.setAge(rootNode.get("age").asInt());
			candidate.setDateOfBirth(rootNode.get("dateOfBirth").asText());
			candidate.setCurrentLocation(rootNode.get("currentLocation").asText());
			candidate.setPreviousCompany(rootNode.get("previousCompany").asText());
			candidate.setWorkExperience(rootNode.get("workExperience").asDouble());
			candidate.setCvStatus(CvReviewStage.PENDING_FOR_CV_REVIEW.name());
			candidate.setCreatedDate(new Date());
			candidate.setUniqueAccessToken(rootNode.get("uniqueAccessToken").asText());
			candidate.setCv(file.getBytes());
			return candidate;
		} catch (Exception e) {
			log.error("Error in saving Recruitment record in DB", e);
			return null;
		}
	}
	
	/**
	 * Register a candidate
	 */
	@Override
	public GenericResponse<?> registerCandidate(String data, MultipartFile file) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(data);
			String candidateName = rootNode.get("name").asText();
			String candidateEmail = rootNode.get("email").asText();
			
			/** Check if Candidate Data is Present in Cooling Period */
			Boolean isCandidateInCoolingPeriod = checkCoolingStatusForCandidate(candidateName, candidateEmail);
			if (isCandidateInCoolingPeriod) {
				return GenericResponse.builder().message("Registration Failed...Please wait for 6 months from Previous Registration").code("ERR").body(null).build();
			} 
			
			Candidate candidate = buildCandidateObject(rootNode, file);
			if (candidate == null) {
				return GenericResponse.builder().message("Error in Registering Candidate").code("ERR").body(null).build();
			}
			
			Candidate savedCandidate = candidateRepository.save(candidate);
			GenericResponse<?> channelServiceResponse = updateCandidateSource(savedCandidate);
			log.info("Channel Service Response for Candidate " + savedCandidate.getId() + " is" + channelServiceResponse.getMessage());
			return GenericResponse.builder().message("Candidate Registered Successfully").code("OK").body(savedCandidate).build();
		} catch (Exception e) {
			log.error("Error in registering candidates", e);
			return GenericResponse.builder().message("Error In Registering Candidate").code("ERR").body(null).build();
		}
	}

	private GenericResponse<?> updateCandidateSource(Candidate savedCandidate) {
		/** Update Candidate Id in Channel Service */
		RestTemplate restTemplate = new RestTemplate(); 
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(channelServiceUrl).append(channelServiceUpdateSourceEndpoint).append(savedCandidate.getUniqueAccessToken()).append("/").append(savedCandidate.getId());
		log.info("Url Formed :" + urlBuilder.toString());
		GenericResponse<?> channelServiceResponse = restTemplate.getForObject(urlBuilder.toString(),GenericResponse.class);
		return channelServiceResponse;
	}

	@Override
	public GenericResponse<?> fetchCandidatesByCvStatus(String cvStatus) {
		try {
			List<Candidate> eligibleCandidateList = candidateRepository.findByCvStatus(cvStatus);
			if (eligibleCandidateList.isEmpty()) {
				return GenericResponse.builder().code("WARN").message("No Eligible Candidates Found").body(null).build();
			} 
			return GenericResponse.builder().code("OK").message("Eligible Candidates Fetched SuccessFully").body(eligibleCandidateList).build();
		} catch (Exception e) {
			log.error("Unable to fetch Candidate :: " , e);
			return GenericResponse.builder().code("ERR").message("Unable To Fetch Eligible Candidates For Interview").body(null).build();
		}
	}

	/** Check if candidate has registered previously and is in cooling period */
	public boolean checkCoolingStatusForCandidate(String candidateName, String candidateEmail) {
		Candidate oldRecord = candidateRepository.findByNameAndEmail(candidateName, candidateEmail);
		if (oldRecord == null) {
			return false;
		}
		LocalDate oldRecordLocalDate = oldRecord.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (!oldRecord.getFinalOnboardFlag() && oldRecordLocalDate.isAfter(LocalDate.now().minusMonths(6))) {
			return true;
		} 
		return false; 
	}

}
