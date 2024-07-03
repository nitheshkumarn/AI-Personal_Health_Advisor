package com.hackathon.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.ai.requestdto.HealthDataRequest;
import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.HealthDataResponse;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.service.HealthDataService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5175/")

@RequestMapping("/api/v1/healthdata")
public class HealthDataController {
	
	private HealthDataService hs;
	
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<HealthDataResponse>> addHealthData(@RequestBody HealthDataRequest healthDataRequest) {
		
		return hs.addHealthData(healthDataRequest);
	}
	
	

}
