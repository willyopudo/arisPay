package com.arisweb.controller;

import lombok.RequiredArgsConstructor;
import org.arispay.data.CompanyDto;
import org.arispay.data.UserDto;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.UserServicePort;
import com.arisweb.security.ApplicationUserRole;
import com.arisweb.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private final UserServicePort userServicePort;
	@Autowired
	private final CompanyServicePort companyServicePort;

	@Autowired
	private PasswordEncoder passwordEncoder;
	ApplicationUserRole[] roles = ApplicationUserRole.class.getEnumConstants();
	@Value("${spring.application.name}")
	private String appName;
	private static final Logger logger = LogManager.getLogger(AuthController.class);

	// handler method to handle home page request
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public ModelAndView home(Principal principal) {
		ModelAndView model = new ModelAndView();
		if (!(principal == null)) {
			model.addObject("title", appName + " Dashboard");
			model.setViewName("dashboard");
		} else {
			model.addObject("title", appName + " Home");
			model.setViewName("index");
		}
		return model;
	}

	// handler method to handle login request
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", appName + " Login");
		model.addAttribute("appName", appName);
		return "login";
	}

	// handler method to handle user registration form request
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		// create model object to store form data
		UserDto user = new UserDto();
		List<CompanyDto> companies = companyServicePort.getCompanies();
		model.addAttribute("title", appName + " Register");
		model.addAttribute("user", user);
		model.addAttribute("appName", appName);
		model.addAttribute("companies", companies);
		return "register";
	}

	// handler method to handle user registration form submit request
	@PostMapping("/register/save")
	public String registration(@Valid @ModelAttribute("user") UserDto userDto,
	                           BindingResult result,
	                           Model model, Principal principal) {
		String redirectUrl = "login";
		UserDto existingUser = userServicePort.findUserByEmail(userDto.getEmail());
		List<CompanyDto> companies = companyServicePort.getCompanies();

		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null,
					"There is already an account registered with the same email");
		}
		if (userDto.getPassword().isEmpty()) {
			result.rejectValue("password", null,
					"Password cannot be empty");
		}
		if (result.hasErrors()) {
			System.out.println("There were errors");
			for (ObjectError element : result.getAllErrors()) {

				System.out.print(element.toString() + " ");
			}
			model.addAttribute("user", userDto);
			model.addAttribute("userRoles", roles);
			model.addAttribute("companies", companies);


			return "register";
		}


		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		if (userDto.getRole() == null)
			userDto.setRole("ROLE_USER");
		userServicePort.saveUser(userDto);

		return "redirect:/" + redirectUrl + "?success";
	}

	@PostMapping("/user/modify")
	public String editUser(@Valid @ModelAttribute("user") UserDto userDto,
	                       BindingResult result,
	                       Model model, Principal principal) {
		model.addAttribute("user", userDto);
		model.addAttribute("userRoles", roles);

		String redirectUrl = "users";

		UserDto existingUser = userServicePort.findUserByUsername(userDto.getUsername());

		if (result.hasErrors()) {
			for (ObjectError element : result.getAllErrors()) {

				logger.debug(element.toString() + "\n");
			}



			return "userform";
		}

		userDto.setId(existingUser.getId());
		userDto.setPassword(existingUser.getPassword());
		if (userDto.getRole() == null)
			userDto.setRole("ROLE_USER");


		try {
			userServicePort.saveUser(userDto);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			model.addAttribute("exception", ex.getMessage());


			return "userform";
		}

		return "redirect:/" + redirectUrl + "?success_edit";
	}

	@RequestMapping(value = "/user/add", method = RequestMethod.GET)
	public ModelAndView addUser() {
		ModelAndView model = new ModelAndView();


		UserDto user = new UserDto();
		List<CompanyDto> companies = companyServicePort.getCompanies();
		model.addObject("user", user);
		model.addObject("title", "Add User");
		model.addObject("userRoles", roles);
		model.addObject("companies", companies);
		model.setViewName("userform");
		//System.out.println(Arrays.toString(roles));
		return model;
	}

	@RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable int id) {
		ModelAndView model = new ModelAndView();

		UserDto user = userServicePort.findUserById(id);
		//user.setCompanyId(user.getCompany().getId());

		List<CompanyDto> companies = companyServicePort.getCompanies();
		//logger.debug(user.getRoles().toString());
		//System.out.println(Arrays.toString(roles));
		model.addObject("user", user);
		model.addObject("title", "Edit User");
		model.addObject("userRoles", roles);
		model.addObject("companies", companies);
		model.setViewName("userform");

		return model;
	}

	@RequestMapping(value = "/user/profile/{username}", method = RequestMethod.GET)
	public ModelAndView userProfile(@PathVariable String username) {
		ModelAndView model = new ModelAndView();

		UserDto user = userServicePort.findUserByUsername(username);
		//user.setCompanyId(user.getCompany().getId());

		model.addObject("user", user);
		model.addObject("title", "User Profile");
		model.setViewName("profile");

		return model;
	}

	// Fetch list of users
	@GetMapping("/users")
	public String users(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		String userName = userDetails.getUsername();
		System.out.println("User name active " + userName);
		UserDto user = userServicePort.findUserByUsername(userName);
		List<UserDto> users = userServicePort.findAllUsers();

		model.addAttribute("activeUser", user);
		model.addAttribute("users", users);
		model.addAttribute("title", "List of Users");
		return "users";
	}

	@RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<JsonNode> deleteUser(@PathVariable int id) throws JsonProcessingException {

		UserDto user = userServicePort.findUserById(id);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree("{\"status\": \"OK\", \"message\": \"Record deleted successfully\"}");
		if (user != null) {
			logger.debug("User found for delete " + user.getUsername());
			try {
				userServicePort.deleteUserById(user.getId());
			} catch (Exception ex) {
				logger.error(ex);
			}

		} else {
			json = mapper.readTree("{\"status\": \"NOK\", \"message\": \"Record was not found\"}");
		}
		return ResponseEntity.ok(json);


	}

	private String[] extractFirstLastName(String name) {
		return name.split(" ");
	}
}
