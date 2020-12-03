package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.dao.AppUserDAO;

@Controller
@RequestMapping("/")
public class WelcomeController {
	
	@Autowired
	AppUserDAO appUserDao;
	
	@RequestMapping(method = RequestMethod.GET)
	public String welcomePage(Model model, Principal principal) {
		model.addAttribute("title", "Welcome");
		if(principal != null) {
			return "redirect:/sendMessage";
		}
		return "welcomePage";
	}
}
