package org.arispay.ports.api;

import org.arispay.data.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserServicePort {
	UserDto saveUser(UserDto userDto);

	void deleteUserById(Long id);

	UserDto findUserByEmail(String email);

	UserDto findUserById(int id);

	UserDto findUserByUsername(String username);

	UserDto findUserByUserName2(String username);

	Page<UserDto> findAllUsers(int page, int itemsPerPage);

	UserDto findUserByToken (String token);

	UserDto setPassword(String token, String password);

	//public void findUserCompanyByUserIdAndCompanyId(Long userId, Long companyId);

	public void deleteUserCompanyById(Long userId, Long companyId);
}
