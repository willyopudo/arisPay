package com.arisweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {

	// handler method to handle dashboard page request
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView model = new ModelAndView("dashboard");
		model.addObject("title", "ArisPay Dashboard");
		//model.setViewName("index");
		return model;
	}
}
