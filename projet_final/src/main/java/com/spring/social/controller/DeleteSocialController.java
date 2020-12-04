package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.dao.UserConnectionDAO;


@Controller
@RequestMapping("/deleteSocial")
public class DeleteSocialController {

	@Autowired
	UserConnectionDAO userConnectionDAO;
	
	@RequestMapping(method = RequestMethod.GET)
	public String deleteSocial(Principal principal) {
		userConnectionDAO.deleteConnectionByUserName(principal.getName());
		return "redirect:/userInfo";
	}

}