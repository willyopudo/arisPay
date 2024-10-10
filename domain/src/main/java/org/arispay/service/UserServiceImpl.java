package org.arispay.service;

import org.arispay.data.UserDto;
import org.arispay.ports.api.UserServicePort;
import org.arispay.ports.spi.UserPersistencePort;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserServicePort {

	private final UserPersistencePort userPersistencePort;

	public UserServiceImpl(UserPersistencePort userPersistencePort) {
		this.userPersistencePort = userPersistencePort;
	}

	@Override
	public UserDto saveUser(UserDto userDto) {
		return userPersistencePort.saveUser(userDto);
	}

	@Override
	public UserDto findUserByEmail(String email) {
		return userPersistencePort.findUserByEmail(email);
	}

	@Override
	public UserDto findUserById(int id) {
		return userPersistencePort.findUserById(id);
	}

	@Override
	public UserDto findUserByUsername(String username) {
		return userPersistencePort.findUserByUserName(username);
	}

	@Override
	public UserDto findUserByUserName2(String username) {

		return userPersistencePort.findUserByUserName2(username);
	}


	public void deleteUserById(Long id) {
		userPersistencePort.deleteUserById(id);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<UserDto> users = userPersistencePort.findAllUsers();
//		return users.stream()
//				.map(this::mapToUserDto)
//				.collect(Collectors.toList());
		return users;
	}

	@Override
	public void deleteUserCompanyById(Long userId, Long companyId) {
		userPersistencePort.deleteUserCompanyById(userId, companyId);
	}
}
