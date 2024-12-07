package org.arispay.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.CompanyDto;
import org.arispay.data.GenericHttpResponse;
import org.arispay.data.UserCompanyDto;
import org.arispay.data.UserDto;
import org.arispay.globconfig.security.ApplicationUserRole;
import org.arispay.helpers.AuthUtil;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.UserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Random;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private final UserServicePort userServicePort;
    @Autowired
    private final CompanyServicePort companyServicePort;

    @Autowired
    private PasswordEncoder passwordEncoder;
    ApplicationUserRole[] roles = ApplicationUserRole.class.getEnumConstants();
    @Value("${spring.application.name}")
    private String appName;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    // Register new user
    @PostMapping
    public ResponseEntity<GenericHttpResponse<UserDto>> register(@Valid @RequestBody UserDto userDto,
                                                            BindingResult result,
                                                            Model model, Principal principal) {
        userDto.setId(null);
        GenericHttpResponse<UserDto> response = new GenericHttpResponse<>();
        UserDto existingUser = userServicePort.findUserByEmail(userDto.getEmail());

        return AuthUtil.getGenericHttpResponseResponseEntity(userDto, result, response, existingUser, logger, passwordEncoder, userServicePort);
    }
    // Fetch list of users
    @GetMapping
    public ResponseEntity<List<UserDto>> users() {
        Random rn = new Random();
        List<UserDto> users = userServicePort.findAllUsers();
        for (UserDto user : users) {
            user.setPassword(null);
            user.setStatus("active");
            user.setCurrentPlan("enterprise");
            user.setAvatar("/images/avatars/" + (1 + rn.nextInt(2 - 1 + 1)) + ".png");
        }
        //users.forEach(e -> e.setPassword(null));
        return ResponseEntity.ok(users);
    }
    //Fetch single user
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        UserDto user = userServicePort.findUserById(Math.toIntExact(id));
        if (user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.notFound().build();
    }

    //Modify User
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        GenericHttpResponse<UserDto> response = new GenericHttpResponse<>();
        UserDto existingUser = userServicePort.findUserById(Math.toIntExact(id));
        if (existingUser != null) {
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            existingUser.setRole(userDto.getRole());
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());

            for(UserCompanyDto ucDto : userDto.getUserCompanies()){
                CompanyDto companyDto = companyServicePort.getCompanyById(ucDto.getCompanyId());
                UserCompanyDto uc = new UserCompanyDto(ucDto.getId(), companyDto.getId(), ucDto.isDefault());

                //Check if company in this iteration is not already related to the user we are updating
                if(existingUser.getUserCompanies().stream().noneMatch((e) -> Objects.equals(e.getCompanyId(), ucDto.getCompanyId())))
                    existingUser.getUserCompanies().add(uc);
                else{
                    //If the company exists for the user, we'll update only 'isDefault' field and persist later
                    UserCompanyDto existingUc = existingUser.getUserCompanies().stream().filter((e) -> Objects.equals(e.getCompanyId(), ucDto.getCompanyId())).findFirst().get();
                    existingUc.setDefault(ucDto.isDefault());
                }
            }

            //Let's iterate over the UserCompanies for the user we want to update
            //If a company is not in the list submitted in the Dto, we remove the association and persist change
            existingUser.getUserCompanies().removeIf(uc -> userDto.getUserCompanies().stream().noneMatch((e) -> Objects.equals(e.getCompanyId(), uc.getCompanyId())));

            UserDto updatedUser = userServicePort.saveUser(existingUser);
            updatedUser.setPassword(null);
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("User updated successfully");
            response.setData(updatedUser);

        }
        else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setMessage("User not found");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GenericHttpResponse<?>> deleteUser(@PathVariable int id)  {
        GenericHttpResponse<?> response = new GenericHttpResponse<>();
        UserDto user = userServicePort.findUserById(id);
        if (user != null) {
            logger.debug("User found for delete {}", user.getUsername());
            try {
                userServicePort.deleteUserById(user.getId());
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("User deleted successfully");
            } catch (Exception ex) {
                logger.error(ex);
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                response.setMessage("Error while deleting user");
            }

        } else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setMessage("User not found");
        }
        return new ResponseEntity<>(response, response.getHttpStatus());

    }
}
