package com.hackathon.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hackathon.ai"})
public class AiPersonalHealthAdvisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiPersonalHealthAdvisorApplication.class, args);
	}

}
