package com.arisweb.services;

import com.arisweb.dto.UserDto;
import com.arisweb.iservices.UserService;
import com.arisweb.model.Role;
import com.arisweb.model.User;
import com.arisweb.repository.RoleRepository;
import com.arisweb.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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
		user.setTown(userDto.getTown());
		user.setAddress(userDto.getAddress());
		user.setIdNumber(userDto.getIdNumber());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setZipCode(userDto.getZipCode());
		user.setModifiedBy(userDto.getModifiedBy());
		user.setModifiedDate(userDto.getModifiedDate());
		user.setCompany(userDto.getUserCompany());
		user.setCreatedBy(userDto.getCreatedBy());
		user.setCreatedDate(userDto.getCreatedDate());
		// encrypt the password using spring security
		if (userDto.getAddedOrEditedFrom() == 83659)
			user.setPassword(userDto.getPassword());
		else
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		Role role = roleRepository.findByName(userDto.getRole());
		if (role == null) {
			role = checkRoleExist(userDto.getRole());
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

	@Override
	public UserDto findUserByUserName2(String username) {

		return mapToUserDto(userRepository.findByUsername(username));
	}


	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(this::mapToUserDto)
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
		userDto.setIdNumber(user.getIdNumber());
		userDto.setPhoneNumber(user.getPhoneNumber());
		userDto.setAddress(user.getAddress());
		userDto.setZipCode(user.getZipCode());
		userDto.setTown(user.getTown());
		if (!user.getRoles().isEmpty())
			userDto.setRole(user.getRoles().get(0).getName().substring(user.getRoles().get(0).getName().lastIndexOf("_") + 1));
		userDto.setUserCompany(user.getCompany());
		return userDto;
	}

	private Role checkRoleExist(String roleName) {
		Role role = new Role();
		role.setName(roleName);
		return roleRepository.save(role);
	}
}
