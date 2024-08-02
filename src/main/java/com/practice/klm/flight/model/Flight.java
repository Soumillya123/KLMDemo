package com.practice.klm.flight.model;

import java.time.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Flight {
	@Id
    @NotBlank(message = "Flight number cannot be blank")
	@Size(min = 5,message="FlightNumber should have alleast 5 characters")
   private String flightNumber;
	@Embedded
    @Valid
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "origin_code"))
    })
    @NotNull(message = "Origin cannot be null")
   private Airport origin;
	@Embedded
    @Valid
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "destination_code"))
    })
   @NotNull(message = "Destination cannot be null")
   private Airport destination;
	@NotNull(message = "Duration cannot be null")
   private Duration duration;
   
   
   public Flight() {
		
		
	}
   
public Flight(String flightNumber, Airport origin, Airport destination, Duration duration) {
	super();
	this.flightNumber = flightNumber;
	this.origin = origin;
	this.destination = destination;
	this.duration = duration;
}

public String getFlightNumber() {
	return flightNumber;
}


public void setFlightNumber(String flightNumber) {
	this.flightNumber = flightNumber;
}
public Airport getOrigin() {
	return origin;
}
public void setOrigin(Airport origin) {
	this.origin = origin;
}
public Airport getDestination() {
	return destination;
}
public void setDestination(Airport destination) {
	this.destination = destination;
}
public Duration getDuration() {
	return duration;
}
public void setDuration(Duration duration) {
	this.duration = duration;
}
   

}
