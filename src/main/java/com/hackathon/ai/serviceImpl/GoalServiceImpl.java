package com.hackathon.ai.serviceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hackathon.ai.entity.Goal;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.repository.GoalRepository;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.requestdto.GoalRequest;
import com.hackathon.ai.responsedto.GoalResponse;
import com.hackathon.ai.service.GoalService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GoalServiceImpl implements GoalService {

	ResponseStructure<GoalResponse> structure;

	private UserRepository userRepo;
	private GoalRepository goalRepo;

	private Goal mapToGoal(GoalRequest goalRequest, User user) {
		return Goal.builder().description(goalRequest.getDescription()).targetDate(goalRequest.getTargetDate())
				.achieved(false).user(user).build();

	}

	private GoalResponse mapToGoalResponse(Goal goal) {
		return GoalResponse.builder().goalId(goal.getGoalId()).description(goal.getDescription())
				.targetDate(goal.getTargetDate()).achieved(false).userId(goal.getUser().getUserId()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<GoalResponse>> addGoal(GoalRequest goalRequest) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		return userRepo.findByUserName(userName).map(user -> {
			Goal goal = mapToGoal(goalRequest, user);
			goal = goalRepo.save(goal);

			user.getSetOfGoals().add(goal);
			user = userRepo.save(user);
			goal.setUser(user);

			goal = goalRepo.save(goal);

			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Health Data for the Day Created");
			structure.setData(mapToGoalResponse(goal));

			return new ResponseEntity<ResponseStructure<GoalResponse>>(structure, HttpStatus.CREATED);

		}).orElseThrow(() -> new RuntimeException("User not Found"));
	}

}