package com.arisweb.iservices;

import com.arisweb.dto.UserDto;
import com.arisweb.model.User;

import java.util.List;

public interface UserService {
	void saveUser(UserDto userDto);

	void deleteUserById(Long id);

	User findUserByEmail(String email);

	UserDto findUserById(int id);

	User findUserByUserName(String username);

	UserDto findUserByUserName2(String username);

	List<UserDto> findAllUsers();
}
