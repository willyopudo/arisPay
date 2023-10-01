//package com.arisweb.controller;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class CustomErrorController implements ErrorController {
//
//	@RequestMapping("/error")
//	public String handleError(HttpServletRequest request) {
//		// get error status
//		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//		// TODO: log error details here
//
//		if (status != null) {
//			int statusCode = Integer.parseInt(status.toString());
//
//			// display specific error page
//			if (statusCode == HttpStatus.NOT_FOUND.value()) {
//				return "error/404";
//			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//				return "error/500";
//			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
//				return "error/403";
//			}
//		}
//
//		// display generic error
//		return "error";
//	}
//}