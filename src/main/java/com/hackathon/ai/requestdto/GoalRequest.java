package com.hackathon.ai.requestdto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoalRequest {

	private String description;
	private LocalDate targetDate;
	private boolean achieved;
	
}