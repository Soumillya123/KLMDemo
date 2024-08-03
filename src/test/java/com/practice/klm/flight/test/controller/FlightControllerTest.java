package com.practice.klm.flight.test.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.klm.flight.controller.FlightController;
import com.practice.klm.flight.model.Airport;
import com.practice.klm.flight.model.Flight;
import com.practice.klm.flight.service.FlightService;
@WebMvcTest(FlightController.class)
public class FlightControllerTest {
    @Autowired
	private MockMvc mockMvc;
    @MockBean
    private FlightService flightService;
    @Autowired
   	private ObjectMapper objectMapper;
    private Flight flight;
    private Airport origin;
    private Airport destination;
	
	 @BeforeEach
	    void setUp() {
		 origin=new Airport();
		 origin.setCode("JFK");
		 destination=new Airport();
		 destination.setCode("CCU");
		 flight=new Flight();
		 flight.setFlightNumber("FL123"); 
		 flight.setOrigin(origin);
		 flight.setDestination(destination);
		 flight.setDuration(Duration.ofHours(12));
		 
	 }
	 
	 @Test
	    void testGetAllFlights() throws Exception {
	        given(flightService.getAllFlights()).willReturn(Arrays.asList(flight));

	        mockMvc.perform(get("/flights/getAllFlights"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].flightNumber").value(flight.getFlightNumber()))
	                .andExpect(jsonPath("$[0].origin.code").value(flight.getOrigin().getCode()))
	                .andExpect(jsonPath("$[0].destination.code").value(flight.getDestination().getCode()))
	                .andExpect(jsonPath("$[0].duration").value(flight.getDuration().toString()));
	    }

	    @Test
	    void testGetFlightById() throws Exception {
	        given(flightService.getFlightByNumber(anyString())).willReturn(Optional.of(flight));

	        mockMvc.perform(get("/flights/{id}", "FL123"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.flightNumber").value(flight.getFlightNumber()))
	                .andExpect(jsonPath("$.origin.code").value(flight.getOrigin().getCode()))
	                .andExpect(jsonPath("$.destination.code").value(flight.getDestination().getCode()))
	                .andExpect(jsonPath("$.duration").value(flight.getDuration().toString()));
	    }

	    @Test
	    void testCreateFlight() throws Exception {
	        given(flightService.saveFlight(any(Flight.class))).willReturn(flight);

	        mockMvc.perform(post("/flights")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(flight)))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.flightNumber").value(flight.getFlightNumber()))
	                .andExpect(jsonPath("$.origin.code").value(flight.getOrigin().getCode()))
	                .andExpect(jsonPath("$.destination.code").value(flight.getDestination().getCode()))
	                .andExpect(jsonPath("$.duration").value(flight.getDuration().toString()));
	    }

	   
	    @Test
	    void testUpdateFlight() throws Exception {
	        given(flightService.updateFlight(anyString(), any(Flight.class))).willReturn(true);

	        Flight updatedFlight = new Flight();
	        updatedFlight.setFlightNumber("FL124");
	        updatedFlight.setOrigin(origin);
	        updatedFlight.setDestination(destination);
	        updatedFlight.setDuration(Duration.ofHours(3));

	        mockMvc.perform(put("/flights/{flightNumber}", "AI101")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(updatedFlight)))
	                .andExpect(status().isNoContent());
	    }
	   
	    
	    
	    
	    @Test
	    void testDeleteFlight() throws Exception {
	    	 given(flightService.deleteFlight(anyString())).willReturn(true);
	        mockMvc.perform(delete("/flights/{flightNumber}", "FL123"))
	                .andExpect(status().isNoContent());
	    }

	    
	    @Test
	    void testGetFlightsByOriginDestination() throws Exception {
	        given(flightService.getFlightsByOriginDestination(anyString(), anyString()))
	                .willReturn(Arrays.asList(flight));

	        mockMvc.perform(get("/flights/search?origin={origin}&destination={destination}", "LAX", "CCU"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].flightNumber").value(flight.getFlightNumber()))
	                .andExpect(jsonPath("$[0].origin.code").value(flight.getOrigin().getCode()))
	                .andExpect(jsonPath("$[0].destination.code").value(flight.getDestination().getCode()))
	                .andExpect(jsonPath("$[0].duration").value(flight.getDuration().toString()));
	    }
	 
	 
}
