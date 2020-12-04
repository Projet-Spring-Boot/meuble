package com.spring.social.security.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.spring.social.dao.AppUserDAO;
import com.spring.social.dao.InfoConnectionDAO;
import com.spring.social.entity.AppUser;


@Component
public class CustomLogoutSuccessHandler extends
    AbstractAuthenticationTargetUrlRequestHandler implements
    LogoutSuccessHandler {
	
	@Autowired
	InfoConnectionDAO infoConnectionDAO;
	
	@Autowired
	AppUserDAO appUserDAO;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		
		AppUser user = appUserDAO.findAppUserByUserName(authentication.getName());
		
		System.out.println("COUCOU1");
		long max = infoConnectionDAO.getMaxConnectionIdByUserId(user.getUserId());
		System.out.println("max connection id : "+ max);
		infoConnectionDAO.AddLogout(max);
		System.out.println("COUCOU2");

	}
}