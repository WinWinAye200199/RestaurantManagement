package com.example.resturant_management_system.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.resturant_management_system.exception.BadRequestException;
import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.repository.UserRepository;
import com.example.resturant_management_system.security.UserPrincipal;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Username Not Found!"));
		if(user == null) {
			throw new BadRequestException("User not found with this " + email);
		}
		
		return UserPrincipal.build(user);

		
	}

}