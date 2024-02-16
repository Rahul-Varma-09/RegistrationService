package com.montran.recruitment.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InterviewToBeScheduled {

	private Long candidateId;
	private String interviewScheduledBy;
	private String interviewMode;
	private String base64encodedCv;

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public String getInterviewScheduledBy() {
		return interviewScheduledBy;
	}

	public void setInterviewScheduledBy(String interviewScheduledBy) {
		this.interviewScheduledBy = interviewScheduledBy;
	}

	public String getInterviewMode() {
		return interviewMode;
	}

	public void setInterviewMode(String interviewMode) {
		this.interviewMode = interviewMode;
	}

	public String getBase64encodedCv() {
		return base64encodedCv;
	}

	public void setBase64encodedCv(String base64encodedCv) {
		this.base64encodedCv = base64encodedCv;
	}

}
