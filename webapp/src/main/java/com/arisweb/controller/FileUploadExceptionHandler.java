package com.arisweb.controller;

import lombok.RequiredArgsConstructor;
import org.arispay.data.UserDto;
import org.arispay.ports.api.UserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class FileUploadExceptionHandler {
	@Autowired
	private UserServicePort userServicePort;


	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException e, Principal principal) {
		ModelAndView modelAndView = new ModelAndView("profile");
		UserDto user = userServicePort.findUserByUserName2(principal.getName());
		modelAndView.addObject("fileError", "File size exceeds the allowed limit.");
		modelAndView.addObject("user", user);
		return modelAndView;
	}
}
