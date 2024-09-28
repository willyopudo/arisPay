package org.arispay.mappers;

import org.arispay.data.UserDto;
import org.arispay.entity.Company;
import org.arispay.entity.Role;
import org.arispay.entity.User;
import org.arispay.entity.UserCompany;
import org.arispay.repository.*;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = CompanyRepository.class)
public abstract class UserMapper {
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserCompanyRepository userCompanyRepository;

	@Autowired
	private UserRepository userRepository;

	//mock comment
	@Mapping(source = "companyIds", target = "companies", qualifiedByName = "idsToCompanies")
	@Mapping(source = "role", target = "roles", qualifiedByName = "roleNameToRoles")
	public abstract User convert(UserDto userDto);

	@Mapping(source = "companies", target = "companyIds", qualifiedByName = "companiesToIds")
	@Mapping(source = "roles", target = "role", qualifiedByName = "RoleListToRoleName")
	@InheritInverseConfiguration
	public abstract UserDto convert(User user);

	@Mapping(source = "companies", target = "companyIds", qualifiedByName = "companiesToIds")
	@Mapping(source = "roles", target = "role", qualifiedByName = "RoleListToRoleName")
	public abstract List<UserDto> userListToUserDtoList(List<User> userList);

	@Mapping(source = "companyIds", target = "companies", qualifiedByName = "idsToCompanies")
	@Mapping(source = "role", target = "roles", qualifiedByName = "roleNameToRoles")
	public abstract List<User> userDtoListToUserList(List<UserDto> userDtoList);

	@Named("idsToCompanies")
	public List<UserCompany> userIdToUserCompanies(List<Long> userIds) {
		List<UserCompany> userCompanies = new ArrayList<>();
		UserCompany userCompany = new UserCompany();
		userCompany.setUser(userRepository.findById(userIds.getFirst()).orElse(null));
		return userCompanyRepository.findAll(Example.of(userCompany));
	}

	@Named("companiesToIds")
	public static List<Long> companyToId(List<UserCompany> companies) {

		List<Long> companyIds = new ArrayList<>();
		for( UserCompany company: companies ) {
			companyIds.add(company.getCompany().getId());
		}

		return companyIds;
	}

	@Named("roleNameToRoles")
	public List<Role> roleNameToRoles(String role) {
		Role existingRole =  roleRepository.findByName(role);
		if(existingRole == null) {
			Role newRole = new Role();
			newRole.setName(role);
			existingRole =  roleRepository.save(newRole);
		}
		return List.of(existingRole);
	}
	@Named("RoleListToRoleName")
	public String RoleListToRoleName(List<Role> roles) {
		return roles.getFirst().getName();
	}
}
