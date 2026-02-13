package org.arispay.controller;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.*;
import org.arispay.data.validation.OnAdminUpdate;
import org.arispay.data.validation.OnUserUpdate;
import org.arispay.globconfig.security.ApplicationUserRole;
import org.arispay.helpers.AuthUtil;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.UserServicePort;
import org.arispay.repository.UserRepository;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    @Autowired
    private final UserServicePort userServicePort;
    @Autowired
    private final CompanyServicePort companyServicePort;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AuthUtil authUtil;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final Validator validator;

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

        return authUtil.getGenericHttpResponseResponseEntity(userDto, result, response, existingUser, logger, passwordEncoder, userServicePort);
    }
    // Fetch list of users
    @GetMapping
    public ResponseEntity<Pair<Page<UserDto>, ISummary>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int itemsPerPage,
                                                                     @RequestParam(name = "status", required = false) String status,
                                                                     @RequestParam(name = "role", required = false) String role,
                                                                     @RequestParam(name = "plan", required = false) String currentPlan,
                                                                     @RequestParam(name = "sortBy", defaultValue = "firstName", required = false) String sortBy,
                                                                     @RequestParam(name = "orderBy", defaultValue = "asc", required = false) String orderBy,
                                                                     @RequestParam(name = "search", required = false) String search,
                                                                     HttpServletRequest request) {
        Random rn = new Random();

        List<Sort.Order> orders = new ArrayList<>();
        // Validate the sortBy field against User entity properties
        List<String> validSortFields = Arrays.asList(
                "id", "username", "firstName", "lastName", "email",
                "phoneNumber", "address", "town", "zipCode", "createdDate",

                // Special field for role name sorting
                "roleName"
        );

        if (!validSortFields.contains(translateSortBy(sortBy))) {
            sortBy = "firstName"; // Default to id if invalid field
        }

        // Determine sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(orderBy)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Add information to the filter DTO
        UserFilterDto filterDto = new UserFilterDto(status, role, currentPlan, search, direction, translateSortBy(sortBy));

        // Create pageable based on whether we're sorting by role or standard field
        Pageable pageable = PageRequest.of(page-1, itemsPerPage);

        Page<UserDto> users = userServicePort.findAllUsers(pageable, filterDto);

        for (UserDto user : users) {
            user.setPassword(null);
            user.setAvatar("/images/avatars/" + (1 + rn.nextInt(2 - 1 + 1)) + ".png");
        }
        //users.forEach(e -> e.setPassword(null));
        //Fetch user summary stats
        Claims claims = jwtUtil.resolveClaims(request);

        Long companyId = claims.get("companyId", Long.class);
        ISummary userSummary = userRepository.getUserSummaries(companyId).orElse(null);
        return ResponseEntity.ok(new Pair<>(users, userSummary));
    }

    //Fetch single user
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        UserDto user = userServicePort.findUserById(Math.toIntExact(id));
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    //Modify User
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDto userDto, HttpServletRequest request) {
        GenericHttpResponse<UserDto> response = new GenericHttpResponse<>();

        // Check if the requesting user is an admin FIRST (before validation)
        Claims claims = jwtUtil.resolveClaims(request);
        String userRole = claims.get("role", String.class);
        boolean isAdmin = userRole != null && userRole.equals(ApplicationUserRole.ADMIN.name());

        // Perform validation based on user role using validation groups
        Set<ConstraintViolation<UserDto>> violations;
        if (isAdmin) {
            // Validate with admin group - all fields including role, status, etc.
            violations = validator.validate(userDto, OnAdminUpdate.class);
        } else {
            // Validate with user group - only basic profile fields
            violations = validator.validate(userDto, OnUserUpdate.class);
        }

        // Check for validation errors
        if (!violations.isEmpty()) {
            logger.error("Validation errors: {}", violations);
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Validation failed: " + errorMessage);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Log incoming data for debugging
        logger.debug("Update user request - ID: {}, LastName: '{}', LastName length: {}",
            id, userDto.getLastName(), userDto.getLastName() != null ? userDto.getLastName().length() : "null");

        UserDto existingUser = userServicePort.findUserById(Math.toIntExact(id));
        if (existingUser != null) {

            // Non-admin users can only update these fields:
            // firstName, lastName, email, phoneNumber, address, town, zipCode
            if (!isAdmin) {
                // Only allow non-admin users to update specific fields
                existingUser.setFirstName(userDto.getFirstName());
                existingUser.setLastName(userDto.getLastName());
                existingUser.setEmail(userDto.getEmail());
                existingUser.setPhoneNumber(userDto.getPhoneNumber());
                existingUser.setAddress(userDto.getAddress());
                existingUser.setTown(userDto.getTown());
                existingUser.setZipCode(userDto.getZipCode());
                // All other fields remain unchanged
            } else {
                // Admin users can update all fields
                existingUser.setAvatar(userDto.getAddress());
                existingUser.setPhoneNumber(userDto.getPhoneNumber());
                existingUser.setEmail(userDto.getEmail());
                existingUser.setCurrentPlan(userDto.getCurrentPlan());
                existingUser.setStatus(userDto.getStatus());
                existingUser.setFirstName(userDto.getFirstName());
                existingUser.setLastName(userDto.getLastName());
                existingUser.setAddress(userDto.getAddress());
                existingUser.setTown(userDto.getTown());
                existingUser.setZipCode(userDto.getZipCode());

                // Update user companies only for admins
                if (userDto.getUserCompanies() != null && !userDto.getUserCompanies().isEmpty()) {
                    for(UserCompanyDto ucDto : userDto.getUserCompanies()){
                        CompanyDto companyDto = companyServicePort.getCompanyById(ucDto.getCompanyId());
                        UserCompanyDto uc = new UserCompanyDto(ucDto.getId(), companyDto.getId(), ucDto.getCompanyName(), ucDto.isDefault());

                        //Check if company in this iteration is not already related to the user we are updating
                        if(existingUser.getUserCompanies().stream().noneMatch((e) -> Objects.equals(e.getCompanyId(), ucDto.getCompanyId())))
                            existingUser.getUserCompanies().add(uc);
                        else{
                            //If the company exists for the user, we'll update only 'isDefault' field and persist later
                            UserCompanyDto existingUc = existingUser.getUserCompanies().stream().filter((e) -> Objects.equals(e.getCompanyId(), ucDto.getCompanyId())).findFirst().orElse(null);
                            if (existingUc != null) {
                                existingUc.setDefault(ucDto.isDefault());
                            }
                        }
                    }

                    //Let's iterate over the UserCompanies for the user we want to update
                    //If a company is not in the list submitted in the Dto, we remove the association and persist change
                    existingUser.getUserCompanies().removeIf(uc -> userDto.getUserCompanies().stream().noneMatch((e) -> Objects.equals(e.getCompanyId(), uc.getCompanyId())));
                }
            }

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

    private String translateSortBy(String sortBy) {
        return switch (sortBy) {
            case "plan" -> "currentPlan";
            case "role" -> "roleName";
            case "status" -> "isEnabled";
            default -> "firstName";
        };
    }
}
