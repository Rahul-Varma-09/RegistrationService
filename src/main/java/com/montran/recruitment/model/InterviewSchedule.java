package com.montran.recruitment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long candidateId;
	private String interviewScheduledBy;
	private String interviewMode;
	private String base64encodedCv;


}
