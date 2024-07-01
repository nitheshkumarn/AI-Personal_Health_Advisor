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
public class NotificationResponse {
	
	private Integer notId;
	private String message;
	private LocalDateTime dateTime;
	private boolean isRead;
	private int userId;

}
