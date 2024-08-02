package com.practice.klm.flight.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.practice.klm.flight.model.Airport;
import com.practice.klm.flight.model.Flight;
import com.practice.klm.flight.repositary.FlightRepositary;
import com.practice.klm.flight.service.FlightService;

public class FlightServiceTest {
	@Mock
	private FlightRepositary flightRepository;
	
	@InjectMocks
	private FlightService flightService;
	
	@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
    void testAddFlight() {
        Flight flight = new Flight("FL123", new Airport("JFK"), new Airport("LAX"), Duration.ofHours(5));
        flightService.saveFlight(flight);
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testGetAllFlights() {
        List<Flight> flights = Arrays.asList(
                new Flight("FL123", new Airport("JFK"), new Airport("LAX"), Duration.ofHours(5)),
                new Flight("FL456", new Airport("LAX"), new Airport("SFO"), Duration.ofHours(1))
        );
        when(flightRepository.findAll()).thenReturn(flights);
        List<Flight> results = flightService.getAllFlights();
	    assertEquals(2, results.size());
	    assertEquals("FL456", results.get(0).getFlightNumber());
	    assertNotEquals("FL456", results.get(1).getFlightNumber());//to check the sort based on duration
    }
    
    
    @Test
    void testGetFlightByNumber() {
        Flight flight = new Flight("FL123", new Airport("JFK"), new Airport("LAX"), Duration.ofHours(5));
        when(flightRepository.findById("FL123")).thenReturn(Optional.of(flight));

        Optional<Flight> result = flightService.getFlightByNumber("FL123");
        assertNotNull(result.get());
        assertEquals("FL123", result.get().getFlightNumber());
    }
    
    @Test
    void testGetFlightsByOriginDestination() {
        List<Flight> flights = Arrays.asList(
                new Flight("FL123", new Airport("JFK"), new Airport("LAX"), Duration.ofHours(6)),
                new Flight("FL456", new Airport("JFK"), new Airport("LAX"), Duration.ofHours(5))
        );
        when(flightRepository.findByOriginCodeAndDestinationCode("JFK", "LAX")).thenReturn(flights);

        List<Flight> result = flightService.getFlightsByOriginDestination("JFK", "LAX");
        assertEquals(2, result.size());
        assertEquals("FL456", result.get(0).getFlightNumber());
        assertEquals("FL123", result.get(1).getFlightNumber());
    }

    @Test
    void testUpdateFlight() {
        Flight flight = new Flight("FL123", new Airport("JFK"), new Airport("LAX"), Duration.ofHours(5));
        when(flightRepository.existsById("FL123")).thenReturn(true);

        boolean result = flightService.updateFlight("FL123", flight);
        assertTrue(result);
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testDeleteFlight() {
        when(flightRepository.existsById("FL123")).thenReturn(true);

        boolean result = flightService.deleteFlight("FL123");
        assertTrue(result);
        verify(flightRepository, times(1)).deleteById("FL123");
    }
    
    
    
    
    
}
