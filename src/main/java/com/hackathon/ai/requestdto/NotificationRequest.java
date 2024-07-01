package com.hackathon.ai.requestdto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
	
	private String message;
	private LocalDateTime dateTime;
	private boolean isRead;

}
