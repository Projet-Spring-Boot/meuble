package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.entity.AppUser;
import com.spring.social.form.ChangePasswordForm;
import com.spring.social.security.crypto.EncryptedPassword;

@Controller
public class NewpasswordController {
	
	@Autowired
	AppUserDAO appUserDao;
	
	
	@RequestMapping(value = { "/newpassword" }, method = RequestMethod.GET)
	public String changePasswordGet(Model model, Principal principal) {
		AppUser appUser = appUserDao.findAppUserByUserName(principal.getName());

		//model.addAttribute("host", "http://localhost:8081/login");
		model.addAttribute("encryptedPassword", appUser.getEncrytedPassword());
		return "newpassword";
	}
	
	@RequestMapping(value = { "/newpassword" }, method = RequestMethod.POST)
	public void changePasswordPost(@ModelAttribute("myForm") @Validated ChangePasswordForm changePasswordForm, Principal principal) {
		
		AppUser appUser = appUserDao.findAppUserByUserName(principal.getName());
		appUser.setEncrytedPassword(EncryptedPassword.encrytePassword(changePasswordForm.getNew_password()));
		appUserDao.editUserAccount(appUser);
	}
	
	@RequestMapping(value = { "/checkPassword" }, method = RequestMethod.GET)
	@ResponseBody
	public boolean check(@RequestParam(name = "rawPassword") String rawPassword,
						 @RequestParam(name = "encryptedPassword") String encryptedPassword) {
		
		
		if(EncryptedPassword.checkPassword(rawPassword, encryptedPassword)) {
			System.out.println("true");
			return true;
		}
		System.out.println("false");
		return false;

		
	}
}

