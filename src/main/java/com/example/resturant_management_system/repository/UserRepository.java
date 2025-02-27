package com.example.resturant_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.resturant_management_system.model.entities.JobRole;
import com.example.resturant_management_system.model.entities.Role;
import com.example.resturant_management_system.model.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);
	
	List<User> findByRole(Role role);
	
	List<User> findByRoleAndJobRole(Role role, JobRole jobRole);
	
}