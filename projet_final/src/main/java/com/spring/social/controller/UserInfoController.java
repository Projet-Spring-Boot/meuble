package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.dao.InfoConnectionDAO;
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
	
	@Autowired
	private InfoConnectionDAO infoConnectionDAO;
	
	@RequestMapping(method = RequestMethod.GET)
	public String userInfoNew(Model model, Principal principal) {

		// After user login successfully.
		AppUser logineduser2 = this.appUserDAO.findAppUserByUserName(principal.getName());
		UserConnection userConnection = userConnectionDAO.findUserConnectionByUserName(principal.getName());
		
		long connectionId = infoConnectionDAO.getMaxConnectionIdByUserId(logineduser2.getUserId());
		
		model.addAttribute("providerId", userConnection == null ? null : userConnection.getProviderId());

		
		model.addAttribute("appUser", logineduser2);
		Long dureeSession = infoConnectionDAO.getElapsedTime(connectionId);
		Long nbConnexion = (long) infoConnectionDAO.getConnectionIdByUserId(logineduser2.getUserId()).size();

		model.addAttribute("dureeSession", dureeSession);
		model.addAttribute("nbConnexion", nbConnexion);

		return "userInfoPage";
	}
}