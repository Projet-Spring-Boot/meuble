package com.spring.social.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.spring.social.form.AppUserForm;
import com.spring.social.validator.AppUserValidator;

@Controller
@Transactional
public class MainController {

	@Autowired
	private AppUserValidator appUserValidator;

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {

		// Form target
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == AppUserForm.class) {
			dataBinder.setValidator(appUserValidator);
		}
		
	}

//	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
//	public String logoutSuccessfulPage(Model model) {
//		model.addAttribute("title", "Logout");
//		return "logoutSuccessfulPage";
//	}
}