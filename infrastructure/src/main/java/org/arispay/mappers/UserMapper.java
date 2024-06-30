package org.arispay.mappers;

import org.arispay.data.UserDto;
import org.arispay.entity.Company;
import org.arispay.entity.Role;
import org.arispay.entity.User;
import org.arispay.repository.ClientRepository;
import org.arispay.repository.CompanyRepository;
import org.arispay.repository.RoleRepository;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = CompanyRepository.class)
public abstract class UserMapper {
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private RoleRepository roleRepository;

	//mock comment
	@Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
	@Mapping(source = "role", target = "roles", qualifiedByName = "roleNameToRoles")
	public abstract User convert(UserDto userDto);

	@Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
	@Mapping(source = "roles", target = "role", qualifiedByName = "RoleListToRoleName")
	@InheritInverseConfiguration
	public abstract UserDto convert(User user);

	@Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
	@Mapping(source = "roles", target = "role", qualifiedByName = "RoleListToRoleName")
	public abstract List<UserDto> userListToUserDtoList(List<User> userList);

	@Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
	@Mapping(source = "role", target = "roles", qualifiedByName = "roleNameToRoles")
	public abstract List<User> userDtoListToUserList(List<UserDto> userDtoList);

	@Named("idToCompany")
	public Company idToCompany(Long id) {
		return companyRepository.findById(id).orElse(null);
	}

	@Named("companyToId")
	public static Long companyToId(Company company) {
		return company.getId();
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
