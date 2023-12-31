package com.arisweb.security;

import lombok.RequiredArgsConstructor;
import org.arispay.entity.Role;
import org.arispay.entity.User;
import org.arispay.mappers.UserMapper;
import org.arispay.ports.api.UserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private final UserServicePort userServicePort;
	@Autowired
	private UserMapper userMapper;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Username is " + username);
		User user = userMapper.convert(userServicePort.findUserByUsername(username));

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