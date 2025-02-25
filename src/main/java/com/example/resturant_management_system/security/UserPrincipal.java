package com.example.resturant_management_system.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.resturant_management_system.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrincipal implements UserDetails{

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String email;
	private String phone;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserPrincipal(Long id, String name, String email, String phone, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.authorities = authorities;
	}
	
	public static UserPrincipal build(User user) {
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
		return new UserPrincipal(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getPassword(), List.of(authority));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return password;
	}

	@Override
	public String getUsername() {
		
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

}