package com.hackathon.ai.serviceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hackathon.ai.entity.HealthData;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.repository.HealthDataRepository;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.requestdto.HealthDataRequest;
import com.hackathon.ai.responsedto.HealthDataResponse;
import com.hackathon.ai.service.HealthDataService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HealthDataServiceImpl implements HealthDataService {

	ResponseStructure<HealthDataResponse> structure;

	private HealthDataRepository healthDataRepo;
	private UserRepository userRepo;

	private HealthData mapToHealthData(HealthDataRequest healthDataRequest, User user) {
		return HealthData.builder().date(healthDataRequest.getDate()).diet(healthDataRequest.getDiet())
				.exerciseMinutes(healthDataRequest.getExerciseMinutes()).sleepHours(healthDataRequest.getSleepHours())
				.user(user).build();
	}

	private HealthDataResponse mapToHealthDataResponse(HealthData healthData) {
		return HealthDataResponse.builder().healthId(healthData.getHealthId()).date(healthData.getDate())
				.diet(healthData.getDiet()).exerciseMinutes(healthData.getExerciseMinutes())
				.sleepHours(healthData.getSleepHours()).userId(healthData.getUser().getUserId()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<HealthDataResponse>> addHealthData(HealthDataRequest healthDataRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		return userRepo.findByUserName(userName).map(user -> {
			HealthData healthData = mapToHealthData(healthDataRequest,user);
			healthData = healthDataRepo.save(healthData);
			
			user.getSetOfhHealthData().add(healthData);
			user = userRepo.save(user);
			healthData.setUser(user);
			
			healthData = healthDataRepo.save(healthData);
			
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Health Data for the Day Created");
			structure.setData(mapToHealthDataResponse(healthData));
			
			return new ResponseEntity<ResponseStructure<HealthDataResponse>>(structure, HttpStatus.CREATED);
			
		}).orElseThrow(() -> new RuntimeException("User not Found"));

		/*User user = userRepo.findByUserName(userName).orElseThrow(() -> new RuntimeException(" user not found"));
		HealthData healthData = mapToHealthData(healthDataRequest,user);
		healthData = healthDataRepo.save(healthData);
		user.getSetOfhHealthData().add(healthData);
		userRepo.save(user);
		

		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Health Data for the Day Created");
		structure.setData(mapToHealthDataResponse(healthData));

		return new ResponseEntity<ResponseStructure<HealthDataResponse>>(structure, HttpStatus.CREATED);*/
		
		
	}

	}


