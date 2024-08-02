package org.arispay.adapters;

import org.arispay.data.UserDto;
import org.arispay.entity.Role;
import org.arispay.entity.User;
import org.arispay.mappers.UserMapper;
import org.arispay.ports.spi.UserPersistencePort;
import org.arispay.repository.RoleRepository;
import org.arispay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserJpaAdapter implements UserPersistencePort {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleRepository roleRepository;

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
		User user = userRepository.findById(id);
		user.setPassword(null);
		return userMapper.convert(user);
	}

	@Override
	public UserDto findUserByUserName(String username) {
		User user = userRepository.findByUsername(username);
		return userMapper.convert(user);
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

	private Role checkRoleExist(String roleName) {
		Role role = new Role();
		role.setName(roleName);
		return roleRepository.save(role);
	}

}
