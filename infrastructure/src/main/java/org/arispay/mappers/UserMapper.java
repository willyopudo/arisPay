package org.arispay.mappers;

import org.arispay.data.UserCompanyDto;
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

//	@Autowired
//	private UserRepository userRepository;

	//mock comment
	@Mapping(source = "userCompanies", target = "userCompanies", qualifiedByName = "idsToCompanies")
	@Mapping(source = "role", target = "roles", qualifiedByName = "roleNameToRoles")
	public abstract User convert(UserDto userDto);

	@Mapping(source = "userCompanies", target = "userCompanies", qualifiedByName = "companiesToIds")
	@Mapping(source = "roles", target = "role", qualifiedByName = "RoleListToRoleName")
	@InheritInverseConfiguration
	public abstract UserDto convert(User user);

	@Mapping(source = "userCompanies", target = "userCompanies", qualifiedByName = "companiesToIds")
	@Mapping(source = "roles", target = "role", qualifiedByName = "RoleListToRoleName")
	public abstract List<UserDto> userListToUserDtoList(List<User> userList);

	@Mapping(source = "userCompanies", target = "userCompanies", qualifiedByName = "idsToCompanies")
	@Mapping(source = "role", target = "roles", qualifiedByName = "roleNameToRoles")
	public abstract List<User> userDtoListToUserList(List<UserDto> userDtoList);

	@Named("idsToCompanies")
	public List<UserCompany> userCompanyIdsToUserCompanies(List<UserCompanyDto> userCompanyDtos) {
		List<UserCompany> userCompanies = new ArrayList<>();
		for(UserCompanyDto userCompanyDto : userCompanyDtos) {
			Company company = companyRepository.findById(Long.valueOf(userCompanyDto.getId())).orElse(null);
			UserCompany userCompany = new UserCompany();
			userCompany.setCompany(company);
			userCompany.setDefault(userCompanyDto.isDefault());
			userCompanies.add(userCompany);

		}
		return userCompanies;
		//return userCompanyRepository.findAll(Example.of(userCompany));
	}

	@Named("companiesToIds")
	public static List<UserCompanyDto> companyToId(List<UserCompany> companies) {

		List<UserCompanyDto> userCompanies = new ArrayList<>();
		for( UserCompany company: companies ) {
			userCompanies.add(new UserCompanyDto(company.getCompany().getId().intValue(), company.isDefault()));
		}

		return userCompanies;
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
