package com.example.services;

import com.example.dto.UserDto;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository,
	                       RoleRepository roleRepository,
	                       PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void saveUser(UserDto userDto) {
		User user = new User();
		user.setId(userDto.getId());
		user.setUsername(userDto.getUserName());
		user.setName(userDto.getFirstName() + " " + userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setStatus(userDto.getStatus());
		// encrypt the password using spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		Role role = roleRepository.findByName("ROLE_ADMIN");
		if (role == null) {
			role = checkRoleExist();
		}
		user.setRoles(Arrays.asList(role));
		userRepository.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDto findUserById(int id) {
		User user = userRepository.findById(id);
		return this.mapToUserDto(user);
	}


	@Override
	public User findUserByUserName(String username) {
		return userRepository.findByUsername(username);
	}


	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map((user) -> mapToUserDto(user))
				.collect(Collectors.toList());
	}

	private UserDto mapToUserDto(User user) {
		UserDto userDto = new UserDto();
		String[] str = user.getName().split(" ");
		userDto.setId(user.getId());
		userDto.setUserName(user.getUsername());
		userDto.setFirstName(str[0]);
		userDto.setLastName(str[1]);
		userDto.setEmail(user.getEmail());
		userDto.setStatus(user.getStatus());
		return userDto;
	}

	private Role checkRoleExist() {
		Role role = new Role();
		role.setName("ROLE_ADMIN");
		return roleRepository.save(role);
	}
}
