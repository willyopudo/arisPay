package org.arispay.ports.api;

import org.arispay.data.UserDto;

import java.util.List;

public interface UserServicePort {
	UserDto saveUser(UserDto userDto);

	void deleteUserById(Long id);

	UserDto findUserByEmail(String email);

	UserDto findUserById(int id);

	UserDto findUserByUsername(String username);

	UserDto findUserByUserName2(String username);

	List<UserDto> findAllUsers();
}
