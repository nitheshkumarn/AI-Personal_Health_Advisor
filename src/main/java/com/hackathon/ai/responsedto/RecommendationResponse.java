package com.hackathon.ai.responsedto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {
	
	private Integer recId;
	private String text;
	private LocalDateTime dateTime;
	private int userId;
	private int healthId;

}
