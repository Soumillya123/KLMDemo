package com.practice.klm.flight.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.klm.flight.model.Flight;

public interface FlightRepositary extends JpaRepository<Flight, String> {
	List<Flight> findByFlightNumber(String flightNumber);
    List<Flight> findByOriginCodeAndDestinationCode(String originCode, String destinationCode);
}
