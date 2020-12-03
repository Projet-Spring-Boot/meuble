package com.spring.social.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = {"/resetPw" })
public class ResetPwController {
	@RequestMapping(method = RequestMethod.GET)
	public String resetPw(Model model) {
		
		return "resetPwPage";
	}
}
