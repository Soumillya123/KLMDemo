package com.practice.klm.flight.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jakarta.persistence.Embeddable;

@Embeddable
public class Airport {
	@NotBlank(message = "Airport code cannot be blank")
	@Size(min = 3,message="Code should have alleast 3 characters")
	private String code;
	
public Airport() {}

public Airport(@NotBlank(message = "Airport code cannot be blank") String code) {
	
	this.code = code;
}

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}
   


}
