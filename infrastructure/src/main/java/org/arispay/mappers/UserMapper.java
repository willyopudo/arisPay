package org.arispay.mappers;

import org.arispay.data.UserDto;
import org.arispay.entity.Company;
import org.arispay.entity.User;
import org.arispay.repository.ClientRepository;
import org.arispay.repository.CompanyRepository;
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

	@Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
	public abstract User convert(UserDto userDto);

	@Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
	@InheritInverseConfiguration
	public abstract UserDto convert(User user);

	@Mapping(source = "company", target = "companyId", qualifiedByName = "companyToId")
	public abstract List<UserDto> userListToUserDtoList(List<User> userList);

	@Mapping(source = "companyId", target = "company", qualifiedByName = "idToCompany")
	public abstract List<User> userDtoListToUserList(List<UserDto> userDtoList);

	@Named("idToCompany")
	public Company idToCompany(Long id) {
		return companyRepository.findById(id).orElse(null);
	}

	@Named("companyToId")
	public static Long companyToId(Company company) {
		return company.getId();
	}
}
