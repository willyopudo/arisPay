package com.example.security;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Username is " + username);
		User user = userRepository.findByUsername(username);
		//System.out.println("Username is " + user.getUsername());

		if (user != null) {
			//return new org.springframework.security.core.userdetails.User(user.getUsername(),
			//user.getPassword(),
			//mapRolesToAuthorities(user.getRoles()));
			return new CustomUserDetails(user);
		} else {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
		Collection<? extends GrantedAuthority> mapRoles = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		return mapRoles;
	}
}