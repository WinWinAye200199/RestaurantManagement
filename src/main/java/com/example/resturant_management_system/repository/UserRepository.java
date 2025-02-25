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

//	@Query("SELECT u FROM User u WHERE u.username COLLATE utf8mb4_general_ci = :username")
//	Optional<User> findByUsername(@Param("username")String username);
	
	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);
	
//	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
//    List<User> findByRoleName(String roleName);
	
	List<User> findByRole(Role role);
	
	List<User> findByRoleAndJobRole(Role role, JobRole jobRole);
	
}