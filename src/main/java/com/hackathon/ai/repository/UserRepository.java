package com.hackathon.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByUserEmail(String userEmail);

}
