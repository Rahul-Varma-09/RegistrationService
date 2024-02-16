package com.montran.recruitment.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.montran.recruitment.services.CandidateService;
import com.montran.recruitment.services.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);
	
	@Autowired private CandidateService candidateService;

	
	

	

}
