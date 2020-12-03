package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.dao.UserConnectionDAO;
import com.spring.social.entity.AppUser;
import com.spring.social.entity.UserConnection;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

	@Autowired
	private AppUserDAO appUserDAO;
	
	@Autowired
	private UserConnectionDAO userConnectionDAO;

	@RequestMapping(method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// After user login successfully.
		AppUser logineduser2 = this.appUserDAO.findAppUserByUserName(principal.getName());
		
		//Check if user is connected with social
		UserConnection userConnection = userConnectionDAO.findUserConnectionByUserName(principal.getName());

		model.addAttribute("appUser", logineduser2);
		model.addAttribute("providerId", userConnection == null ? null : userConnection.getProviderId());
		
		return "userInfoPage";
	}
}