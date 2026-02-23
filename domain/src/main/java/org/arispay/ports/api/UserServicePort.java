package org.arispay.ports.api;

import org.arispay.data.UserDto;
import org.arispay.data.UserFilterDto;
import org.arispay.data.UserImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserServicePort {
	UserDto saveUser(UserDto userDto);

	void deleteUserById(Long id);

	UserDto findUserByEmail(String email);

	UserDto findUserById(int id);

	UserDto findUserByUsername(String username);

	UserDto findUserByUserName2(String username);

	Page<UserDto> findAllUsers(Pageable pageable, UserFilterDto filterDto);

	UserDto findUserByToken (String token);

	UserDto setPassword(String token, String password);

	//public void findUserCompanyByUserIdAndCompanyId(Long userId, Long companyId);

	public void deleteUserCompanyById(Long userId, Long companyId);

	UserDto uploadProfilePicture(Long userId, MultipartFile file) throws IOException;

	UserImageDto getUserImage(Long userId) throws IOException;
}
