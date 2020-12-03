package com.spring.social.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {

		// model.addAttribute("host", "http://localhost:8081/login");
		return "loginPage";
	}

	// User login with social networking,
	// but does not allow the app to view basic information
	// application will redirect to page / signin.
	@RequestMapping(value = { "/signin" }, method = RequestMethod.GET)
	public String signInPage(Model model) {
		return "redirect:/login";
	}
	
	/*
	@RequestMapping(value = { "/resetPw" }, method = RequestMethod.GET)
	public String resetPw(Model model) {
		
		return "resetPwPage";
	}
	*/
}
