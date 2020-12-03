package com.spring.social.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.utils.WebUtil;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@RequestMapping(method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		// After user login successfully.
		String userName = principal.getName();

		System.out.println("User Name: " + userName);

		UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtil.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "adminPage";
	}
}
