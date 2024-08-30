package com.practice.klm.flight.test.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
    @Autowired
    private WebApplicationContext wac;
   
    
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
		 
		mockMvc=MockMvcBuilders.webAppContextSetup(wac).build();
		
		
	 }
	 
	    @Test
	    @WithMockUser(roles = "USER")
	    void testGetAllFlightsIsAllowedForUser() throws Exception {
	        mockMvc.perform(get("/flights/getAllFlights"))
	                .andExpect(status().isOk());
	    }
	 
	    @Test
	    @WithMockUser(roles = "ADMIN")
	    void testGetAllFlightsIsAllowedForAdmin() throws Exception {
	        mockMvc.perform(get("/flights/getAllFlights"))
	                .andExpect(status().isOk());
	    }
	 
		/*
		 * @Test
		 * 
		 * @WithMockUser(roles = "USER") void testPostFlightIsForbiddenForUser() throws
		 * Exception { mockMvc.perform(post("/flights") )
		 * .andExpect(status().isForbidden()); }
		 */
		
		  @Test
		  @WithMockUser(roles = "ADMIN")
		  void testPostFlightIsAllowedForAdmin() throws Exception { 
			  mockMvc.perform(post("/flights")
			.contentType("application/json")
		    .content("{\"flightNumber\":\"AA123\",\"origin\":{\"code\":\"JFK\"},\"destination\":{\"code\":\"LAX\"},\"duration\":\"PT4H\"}")).
			  andExpect(status().isCreated()); 
			  }
		 
	    
	 
	    @Test
	    void testPostFlightIsAllowedForAdmin_httpBasic() throws Exception {
	        mockMvc.perform(post("/flights")
	                .with(httpBasic("admin", "password"))
	                .contentType("application/json")
	                .content("{\"flightNumber\":\"AA123\",\"origin\":{\"code\":\"JFK\"},\"destination\":{\"code\":\"LAX\"},\"duration\":\"PT4H\"}"))
	                .andExpect(status().isCreated());
	    }
	    
	    
	    
	 @Test
	 @WithMockUser(roles = {"USER","ADMIN"})
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
	    @WithMockUser(roles = {"USER","ADMIN"})
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
                     .with(httpBasic("admin", "password"))
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(flight)))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.flightNumber").value(flight.getFlightNumber()))
	                .andExpect(jsonPath("$.origin.code").value(flight.getOrigin().getCode()))
	                .andExpect(jsonPath("$.destination.code").value(flight.getDestination().getCode()))
	                .andExpect(jsonPath("$.duration").value(flight.getDuration().toString()));
	    }
	   

	   
	    @Test
	    @WithMockUser(roles = "ADMIN")
	    void testUpdateFlight() throws Exception {
	        given(flightService.updateFlight(anyString(), any(Flight.class))).willReturn(true);

	        Flight updatedFlight = new Flight();
	        updatedFlight.setFlightNumber("FL124");
	        updatedFlight.setOrigin(origin);
	        updatedFlight.setDestination(destination);
	        updatedFlight.setDuration(Duration.ofHours(3));

	        mockMvc.perform(put("/flights/{flightNumber}", "FL125")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(updatedFlight)))
	                .andExpect(status().isNoContent());
	    }
	   
	    
	    
	    
	    @Test
	    @WithMockUser(roles = "ADMIN")
	    void testDeleteFlight() throws Exception {
	    	 given(flightService.deleteFlight(anyString())).willReturn(true);
	        mockMvc.perform(delete("/flights/{flightNumber}", "FL123")
	        		)
	                .andExpect(status().isNoContent());
	    }

	    
	    @Test
	    @WithMockUser(roles = {"ADMIN","USER"})
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
