package com.arisweb.controller;

import com.arisweb.dto.UserDto;
import com.arisweb.repository.UserRepository;
import com.arisweb.iservices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@ControllerAdvice
public class FileUploadExceptionHandler {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException e, Principal principal) {
		ModelAndView modelAndView = new ModelAndView("profile");
		UserDto user = userService.findUserByUserName2(principal.getName());
		modelAndView.addObject("fileError", "File size exceeds the allowed limit.");
		modelAndView.addObject("user", user);
		return modelAndView;
	}
}
