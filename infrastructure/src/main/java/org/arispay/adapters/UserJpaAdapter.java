package org.arispay.adapters;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.CompanyDto;
import org.arispay.data.UserCompanyDto;
import org.arispay.data.UserDto;
import org.arispay.data.UserFilterDto;
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
import org.arispay.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
	@Autowired
	private EntityManager entityManager;

	private static final Logger logger = LogManager.getLogger(UserJpaAdapter.class);

	@Override
	public UserDto saveUser(UserDto userDto) {
		try {
			User user = userMapper.convert(userDto);
			for(UserCompany userCompany : user.getUserCompanies()) {
				userCompany.setUser(user);
			}
			User userSaved = userRepository.save(user);
			//Let's iterate over the UserCompanies for the user we just updated
			//If a company is not in the list submitted in the Dto, we remove the association and persist change
			if(user.getId() != null) {
				List<UserCompany> userCompanies = userCompanyRepository.findByUserId(user.getId());
				Iterator<UserCompany> ucs = userCompanies.iterator();
				while (ucs.hasNext()) {
					UserCompany uc = ucs.next();
					if (userDto.getUserCompanies().stream().noneMatch((e) -> e.getCompanyId() == uc.getCompany().getId().intValue())) {
						ucs.remove();
						userCompanyRepository.delete(uc);
					}
				}
			}


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
	public Page<UserDto> findAllUsers(Pageable pageable, UserFilterDto filterDto) {
		// Check if we need to sort by role name
		if (filterDto.getSortBy() != null && "roleName".equals(filterDto.getSortBy())) {
			return findUsersWithRoleSorting(pageable, filterDto);
		}

		// Apply specification and pagination
		Specification<User> specification = UserSpecification.getSpecification(filterDto);

		// Create sort for standard fields if specified
		if (filterDto.getSortBy() != null && filterDto.getDirection() != null) {
			Sort sort = Sort.by(filterDto.getDirection(), filterDto.getSortBy());
			pageable = PageRequest.of(
					pageable.getPageNumber(),
					pageable.getPageSize(),
					sort
			);
		}
		Page<User> users = userRepository.findAll(specification,pageable);
		return userMapper.usersPagetoUsersDtoPage(users);
	}

	@Override
	public UserDto findUserByToken(String token) {
		UserDto convert = userMapper.convert(userRepository.findByToken(token).get());
		return convert;
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
		UserCompany userCompany = userCompanyRepository.findByUserIdAndCompanyId(userId, companyId);
		if(userCompany != null)
			userCompanyRepository.delete(userCompany);
	}

	@Override
	public UserDto setPassword(String token, String password) {
		User user = userRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid token"));
		if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Token expired");
		}
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		user.setToken(null);
		user.setTokenExpiration(null);
		User savedUser = userRepository.save(user);
		savedUser.setPassword(null);
		return userMapper.convert(savedUser);

	}

	private Page<UserDto> findUsersWithRoleSorting(Pageable pageable, UserFilterDto filterDto) {
		// For PostgreSQL, we need to change our approach to handle DISTINCT with ORDER BY

		// First, get user IDs with role names for sorting
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<User> root = query.from(User.class);

		// Join with roles table for filtering and sorting
		Join<User, Role> roleJoin = root.join("roles", JoinType.LEFT);

		// Select user ID and role name for sorting
		query.multiselect(root.get("id"), roleJoin.get("name"));

		// Apply filters
		List<Predicate> predicates = new ArrayList<>();

		if (filterDto.getStatus() != null && !filterDto.getStatus().isEmpty()) {
			predicates.add(cb.equal(root.get("status"), filterDto.getStatus()));
		}

		if (filterDto.getRole() != null && !filterDto.getRole().isEmpty()) {
			predicates.add(cb.equal(roleJoin.get("name"), filterDto.getRole()));
		}

		if (filterDto.getCurrentPlan() != null && !filterDto.getCurrentPlan().isEmpty()) {
			predicates.add(cb.equal(root.get("currentPlan"), filterDto.getCurrentPlan()));
		}

		// Apply filters to query
		if (!predicates.isEmpty()) {
			query.where(cb.and(predicates.toArray(new Predicate[0])));
		}

		// Apply sorting by role name
		if (filterDto.getDirection() == Sort.Direction.ASC) {
			query.orderBy(cb.asc(roleJoin.get("name")));
		} else {
			query.orderBy(cb.desc(roleJoin.get("name")));
		}

		// Make the query distinct to avoid duplicates
		query.distinct(true);

		// Execute query with pagination to get IDs in the right order
		List<Object[]> results = entityManager.createQuery(query)
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();

		// Extract user IDs in the sorted order
		List<Long> userIds = results.stream()
				.map(result -> (Long) result[0])
				.collect(Collectors.toList());

		// If no results, return empty page
		if (userIds.isEmpty()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		}

		// Now fetch complete user entities by IDs in the correct order
		// We can use a more efficient approach with the 'IN' clause and manual ordering
		TypedQuery<User> userQuery = entityManager.createQuery(
				"SELECT u FROM User u WHERE u.id IN :ids ORDER BY CASE " +
						userIds.stream().map(id -> "WHEN u.id = " + id + " THEN " + userIds.indexOf(id))
								.collect(Collectors.joining(" ")) +
						" END", User.class);
		userQuery.setParameter("ids", userIds);
		List<User> users = userQuery.getResultList();

		// Count total elements for pagination info
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<User> countRoot = countQuery.from(User.class);
		Join<User, Role> countRoleJoin = countRoot.join("roles", JoinType.LEFT);

		// Apply the same filters to count query
		if (!predicates.isEmpty()) {
			countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
		}

		countQuery.select(cb.countDistinct(countRoot));
		Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

		// Map to DTOs
		List<UserDto> userDtos = users.stream()
				.map(userMapper::convert)
				.collect(Collectors.toList());

		// Create page object
		return new PageImpl<>(userDtos, pageable, totalElements);
	}
}
