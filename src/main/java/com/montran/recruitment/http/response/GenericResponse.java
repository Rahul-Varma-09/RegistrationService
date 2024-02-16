package com.montran.recruitment.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Builder @Data
public class GenericResponse<T> {
	
	private String code;
	private String message;
	private T body;

}
