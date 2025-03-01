package org.arispay.mappers;

import org.arispay.data.UserCompanyDto;
import org.arispay.data.UserDto;
import org.arispay.entity.*;
import org.arispay.repository.*;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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

	// ✅ Manually map Page<User> to Page<UserDto>
	public  Page<UserDto> usersPagetoUsersDtoPage(Page<User> usersPage) {
		List<UserDto> dtoList = userListToUserDtoList(usersPage.getContent());  // Convert list
		return new PageImpl<>(dtoList, usersPage.getPageable(), usersPage.getTotalElements());
	}

	@Named("idsToCompanies")
	public List<UserCompany> userCompanyIdsToUserCompanies(List<UserCompanyDto> userCompanyDtos) {
		List<UserCompany> userCompanies = new ArrayList<>();
		if(!(userCompanyDtos == null)) {
			for(UserCompanyDto userCompanyDto : userCompanyDtos) {
				Company company = companyRepository.findById(userCompanyDto.getCompanyId()).orElse(null);
				UserCompany userCompany =  new UserCompany(company, userCompanyDto.isDefault());
				userCompany.setId(userCompanyDto.getId());
				userCompanies.add(userCompany);

			}
		}

		return userCompanies;
		//return userCompanyRepository.findAll(Example.of(userCompany));
	}

	@Named("companiesToIds")
	public static List<UserCompanyDto> companyToId(List<UserCompany> companies) {

		List<UserCompanyDto> userCompanies = new ArrayList<>();
		for( UserCompany company: companies ) {
			userCompanies.add(new UserCompanyDto(company.getId(),company.getCompany().getId(), company.getCompany().getName(), company.isDefault()));
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
