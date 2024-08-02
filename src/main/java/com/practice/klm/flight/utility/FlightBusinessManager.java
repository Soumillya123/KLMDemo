package com.practice.klm.flight.utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.practice.klm.flight.model.Flight;

@Component
public class FlightBusinessManager {

	public static List<Flight> sortFlightListByDuration(List<Flight>unsortedFlightList) {
		List<Flight> flightList=new ArrayList<Flight>();
		if(Optional.ofNullable(unsortedFlightList).isPresent() && unsortedFlightList.size()>1 ) {
			flightList=unsortedFlightList.stream().sorted(Comparator.comparing(Flight::getDuration)).collect(Collectors.toList());
			return flightList;
		}
		
		else
			return unsortedFlightList;
	}
	
}
