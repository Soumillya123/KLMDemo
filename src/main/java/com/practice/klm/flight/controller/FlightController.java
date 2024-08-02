package com.practice.klm.flight.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.klm.flight.model.Flight;
import com.practice.klm.flight.service.FlightService;



@RestController
@RequestMapping("/flights")
public class FlightController {
	@Autowired
	private FlightService flightService;
	

	@GetMapping("/getAllFlights")
	public ResponseEntity<List<Flight>> getAllFlights(){
		var allFlights=Optional.ofNullable(flightService.getAllFlights()).get();
		return ResponseEntity.ok(allFlights);
	}
	
	@GetMapping("/{flightNumber}")
    public EntityModel<Flight> getFlightByNumber(@PathVariable @NotNull String flightNumber) {
        Optional<Flight> flight = flightService.getFlightByNumber(flightNumber);
        EntityModel<Flight>flightEntityModel=EntityModel.of(flight.get());
        WebMvcLinkBuilder link= linkTo(methodOn(this.getClass()).getAllFlights());
        flightEntityModel.add(link.withRel("all-flights"));
        //return flight.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        return flightEntityModel;
	}
	
	@PostMapping
	    public ResponseEntity<Flight> createFlight(@Valid @RequestBody Flight flight) {
	        Flight savedFlight = flightService.saveFlight(flight);
	        return ResponseEntity.ok(savedFlight);
	    }
	
	
	// Update
    @PutMapping("/{flightNumber}")
    public ResponseEntity<Void> updateFlight(@PathVariable String flightNumber, @RequestBody Flight updatedFlight) {
        boolean isUpdated = flightService.updateFlight(flightNumber, updatedFlight);
        if (isUpdated) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Delete
    @DeleteMapping("/{flightNumber}")
    public ResponseEntity<Void> deleteFlight(@PathVariable String flightNumber) {
        boolean isDeleted = flightService.deleteFlight(flightNumber);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Flight>> getFlightsByOriginDestination(
            @RequestParam @NotNull @NotEmpty String origin, @RequestParam @NotNull @NotEmpty String destination) {
        List<Flight> flights = flightService.getFlightsByOriginDestination(origin, destination);
        return ResponseEntity.ok(flights);
    }
	
}
