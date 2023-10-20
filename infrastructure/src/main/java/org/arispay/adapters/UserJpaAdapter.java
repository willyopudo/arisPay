package org.arispay.adapters;

import org.arispay.data.UserDto;
import org.arispay.entity.User;
import org.arispay.mappers.UserMapper;
import org.arispay.ports.spi.UserPersistencePort;
import org.arispay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserJpaAdapter implements UserPersistencePort {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;


	@Override
	public UserDto saveUser(UserDto userDto) {
		User user = userMapper.convert(userDto);
		User userSaved = userRepository.save(user);

		return userMapper.convert(userSaved);

	}

	@Override
	public void deleteUserById(Long id) {

	}

	@Override
	public UserDto findUserByEmail(String email) {
		return userMapper.convert(userRepository.findByEmail(email));
	}

	@Override
	public UserDto findUserById(int id) {
		return userMapper.convert(userRepository.findById(id));
	}

	@Override
	public UserDto findUserByUserName(String username) {
		return userMapper.convert(userRepository.findByUsername(username));
	}

	@Override
	public UserDto findUserByUserName2(String username) {
		return userMapper.convert(userRepository.findByUsername(username));
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return userMapper.userListToUserDtoList(users);
	}
}
