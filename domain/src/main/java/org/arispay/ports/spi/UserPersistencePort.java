package org.arispay.ports.spi;

import org.arispay.data.UserDto;

import java.util.List;

public interface UserPersistencePort {
	UserDto saveUser(UserDto userDto);

	void deleteUserById(Long id);

	UserDto findUserByEmail(String email);

	UserDto findUserById(int id);

	UserDto findUserByUserName(String username);

	UserDto findUserByUserName2(String username);

	List<UserDto> findAllUsers();
}
