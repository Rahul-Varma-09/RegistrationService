package com.montran.recruitment.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class Candidate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer age;
	private String email;
	private String phoneNumber;
	private String dateOfBirth;
	private String education;
	private String currentLocation;
	private String previousCompany;
	private Double workExperience;
	private Long channelId;
	private String referralType;
	private String referralId;
	private Boolean markAsStarred;
	private String cvStatus;
	private String cvUpdatedBy;
	private Boolean finalOnboardFlag;
	private String status;
	private String rejectionCode;
	private Date createdDate;
	private byte[] cv;
	private String base64EncodedCv;
	private String uniqueAccessToken;
	
}
