package org.arispay.adapters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.CompanyDto;
import org.arispay.data.UserDto;
import org.arispay.entity.Role;
import org.arispay.entity.User;
import org.arispay.entity.UserCompany;
import org.arispay.entity.UserCompanyId;
import org.arispay.mappers.UserMapper;
import org.arispay.ports.spi.UserPersistencePort;
import org.arispay.repository.CompanyRepository;
import org.arispay.repository.RoleRepository;
import org.arispay.repository.UserCompanyRepository;
import org.arispay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserJpaAdapter implements UserPersistencePort {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserCompanyRepository userCompanyRepository;
	@Autowired
	private CompanyRepository companyRepository;

	private static final Logger logger = LogManager.getLogger(UserJpaAdapter.class);

	@Override
	public UserDto saveUser(UserDto userDto) {
		try {
			User user = userMapper.convert(userDto);
			for(UserCompany userCompany : user.getUserCompanies()) {
				userCompany.setUser(user);
			}
			User userSaved = userRepository.save(user);

//			List<UserCompany> userCompanies = userCompanyRepository.findByUserId(userSaved.getId());
//
//			boolean isDefaultCompany = userCompanies.isEmpty();
//			user.getCompanies().getFirst().setDefault(isDefaultCompany);
//
//
//			if (userCompanyRepository.findByUserIdAndCompanyId(user.getId(), Objects.requireNonNull(companyRepository.findById(user.getCompanies().getFirst().getCompany().getId()).orElse(null)).getId()).isEmpty()) {
//				userCompanyRepository.save(user.getCompanies().getFirst());
//				userCompanies.add(user.getCompanies().getFirst());
//			}
//
//			userSaved.setCompanies(userCompanies);

			return userMapper.convert(userSaved);
		}catch (Exception e){
            logger.error("Error occurred while saving user: {}", e.getMessage());
			throw e;
		}

	}

	@Override
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
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
//	public void findUserCompanyByUserIdAndCompanyId(Long userId, Long companyId) {
//		userCompanyRepository.findByUserIdAndCompanyId(userId, companyId);
//	}
	@Override
	public void deleteUserCompanyById(Long userId, Long companyId) {
		UserCompanyId userCompanyId = new UserCompanyId(userId, companyId);
		userCompanyRepository.deleteById(userCompanyId);
	}

}
