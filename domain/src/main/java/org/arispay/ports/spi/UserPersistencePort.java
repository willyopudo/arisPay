package org.arispay.ports.spi;

import org.arispay.data.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserPersistencePort {
	UserDto saveUser(UserDto userDto);

	void deleteUserById(Long id);

	UserDto findUserByEmail(String email);

	UserDto findUserById(int id);

	UserDto findUserByUserName(String username);

	UserDto findUserByUserName2(String username);

	Page<UserDto> findAllUsers(int page, int itemsPerPage);

	UserDto findUserByToken (String token);

	void deleteUserCompanyById(Long userId, Long companyId);

	UserDto setPassword(String token, String password);
}
