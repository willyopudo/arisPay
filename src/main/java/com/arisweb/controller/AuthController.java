package com.arisweb.controller;

import com.arisweb.dto.UserDto;
import com.arisweb.model.User;
import com.arisweb.repository.UserRepository;
import com.arisweb.security.CustomUserDetails;
import com.arisweb.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import java.util.List;

@Controller
public class AuthController {

	private UserService userService;
	private UserRepository userRepository;
	@Value("${app.name}")
	private String appName;

	public AuthController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	// handler method to handle home page request
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView model = new ModelAndView();
		model.addObject("title", "MilkMan Home");
		model.setViewName("index");
		return model;
	}

	// handler method to handle login request
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "arisPay Login");
		model.addAttribute("appName", appName);
		return "login";
	}

	// handler method to handle user registration form request
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		// create model object to store form data
		UserDto user = new UserDto();
		model.addAttribute("title", "arisPay Register");
		model.addAttribute("user", user);
		model.addAttribute("appName", appName);
		return "register";
	}

	// handler method to handle user registration form submit request
	@PostMapping("/register/save")
	public String registration(@Valid @ModelAttribute("user") UserDto userDto,
	                           BindingResult result,
	                           Model model) {
		String redirectUrl = "login";
		User existingUser = userService.findUserByEmail(userDto.getEmail());

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
			if (userDto.getAddedOrEditedFrom() == 2)
				return "userform";

			return "register";
		}
		if (userDto.getAddedOrEditedFrom() == 2) {
			redirectUrl = "users";
		}
		System.out.println("User channel " + userDto.getAddedOrEditedFrom());
		userService.saveUser(userDto);

		return "redirect:/" + redirectUrl + "?success";
	}

	@PostMapping("/user/modify")
	public String editUser(@Valid @ModelAttribute("user") UserDto userDto,
	                       BindingResult result,
	                       Model model) {
		System.out.println("User status " + userDto.getStatus());
		String redirectUrl = "users";
		if (userDto.getAddedOrEditedFrom() == 3) {
			redirectUrl = "user/profile/" + userDto.getUserName();
		}
		User existingUser = userService.findUserByUserName(userDto.getUserName());

		if (result.hasErrors()) {
			for (ObjectError element : result.getAllErrors()) {

				System.out.print(element.toString() + " ");
			}
			model.addAttribute("user", userDto);
			if (userDto.getAddedOrEditedFrom() == 3)
				return "profile";

			return "userform";
		}
		existingUser.setName(userDto.getFirstName() + " " + userDto.getLastName());
		existingUser.setEmail(userDto.getEmail());
		existingUser.setAddress(userDto.getAddress());
		existingUser.setTown(userDto.getTown());
		existingUser.setZipCode(userDto.getZipCode());

		if (userDto.getAddedOrEditedFrom() != 3) {
			existingUser.setStatus(userDto.getStatus());
			existingUser.setIdNumber(userDto.getIdNumber());
			existingUser.setPhoneNumber(userDto.getPhoneNumber());
		}
		userRepository.save(existingUser);

		return "redirect:/" + redirectUrl + "?success_edit";
	}

	@RequestMapping(value = "/user/delete/{id}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable int id) {
		UserDto user = userService.findUserById(id);
		if (user != null)
			userService.deleteUserById(user.getId());
		return "redirect:/users?success_delete";
	}

	@RequestMapping(value = "/user/add", method = RequestMethod.GET)
	public ModelAndView addUser() {
		ModelAndView model = new ModelAndView();

		UserDto user = new UserDto();
		model.addObject("user", user);
		model.addObject("title", "Add User");
		model.setViewName("userform");

		return model;
	}

	@RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable int id) {
		ModelAndView model = new ModelAndView();

		UserDto user = userService.findUserById(id);
		model.addObject("user", user);
		model.addObject("title", "Edit User");
		model.setViewName("userform");

		return model;
	}

	@RequestMapping(value = "/user/profile/{username}", method = RequestMethod.GET)
	public ModelAndView userProfile(@PathVariable String username) {
		ModelAndView model = new ModelAndView();

		UserDto user = userService.findUserByUserName2(username);
		model.addObject("user", user);
		model.addObject("title", "User Profile");
		model.setViewName("profile");

		return model;
	}

	// handler method to handle list of users
	@GetMapping("/users")
	public String users(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		String userName = userDetails.getUsername();
		System.out.println("User name active " + userName);
		User user = userService.findUserByUserName(userName);
		List<UserDto> users = userService.findAllUsers();

		model.addAttribute("activeUser", user);
		model.addAttribute("users", users);
		model.addAttribute("title", "List of Users");
		return "users";
	}
}
