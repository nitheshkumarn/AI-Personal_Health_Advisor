package com.hackathon.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.ai.requestdto.GoalRequest;
import com.hackathon.ai.responsedto.GoalResponse;
import com.hackathon.ai.service.GoalService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5175/")

@RequestMapping("/api/v1/goal")
public class GoalController {
	
	private GoalService gs;
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<GoalResponse>> addGoal(@RequestBody GoalRequest goalRequest) {
		
		return gs.addGoal(goalRequest);
	}

}
