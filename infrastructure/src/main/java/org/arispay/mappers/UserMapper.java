package org.arispay.mappers;

import org.arispay.data.UserDto;
import org.arispay.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
	User convert(UserDto userDto);

	@InheritInverseConfiguration
	UserDto convert(User user);

	List<UserDto> userListToUserDtoList(List<User> userList);

	List<User> userDtoListToUserList(List<UserDto> userDtoList);
}
