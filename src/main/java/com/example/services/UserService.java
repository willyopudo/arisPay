package com.example.services;

import com.example.dto.UserDto;
import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
	void saveUser(UserDto userDto);

	void deleteUserById(Long id);

	User findUserByEmail(String email);

	UserDto findUserById(int id);

	User findUserByUserName(String username);

	List<UserDto> findAllUsers();
}
