package com.practice.klm.flight.service;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.practice.klm.flight.model.Flight;
import com.practice.klm.flight.repositary.FlightRepositary;
import com.practice.klm.flight.utility.FlightBusinessManager;

@Service
public class FlightService {
@Autowired
private FlightRepositary flightRepositary;
	
	public List<Flight>getAllFlights(){
		var flights=flightRepositary.findAll();
		return FlightBusinessManager.sortFlightListByDuration(flights);
	}

	public Optional<Flight> getFlightByNumber(String flightNumber) {
		Optional<Flight> flightDetails= flightRepositary.findById(flightNumber);
		return Optional.ofNullable(flightDetails) != null?flightDetails:Optional.empty();
	}
	
	
	public Flight saveFlight(Flight flight) {
		return flightRepositary.save(flight);
	}

	public boolean updateFlight(String flightNumber, Flight updatedFlight) {
		if(flightRepositary.existsById(flightNumber)) {
			flightRepositary.save(updatedFlight);
			return true;
		}
		return false;
	}

	public boolean deleteFlight(String flightNumber) {
		if(flightRepositary.existsById(flightNumber)) {
			flightRepositary.deleteById(flightNumber);
			return true;
		}
		return false;
	}

	public List<Flight> getFlightsByOriginDestination(String origin, String destination) {
		var flights=flightRepositary.findByOriginCodeAndDestinationCode(origin, destination);
		return FlightBusinessManager.sortFlightListByDuration(flights);
	}






	
}


