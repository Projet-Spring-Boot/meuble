package com.spring.social.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.spring.social.configuration.SocialProperties;
import com.spring.social.dao.UserConnectionDAO;
import com.spring.social.entity.UserConnection;
import com.spring.social.form.MessageForm;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Controller
@RequestMapping("/sendMessage")
public class FeedController {

	@Autowired
	private UserConnectionDAO userConnectionDAO;

	@Autowired
	private SocialProperties socialProperties;

	@RequestMapping(method = RequestMethod.GET)
	public String sendMessage(Model model) {
		model.addAttribute("messageForm", new MessageForm());
		return "feed";
	}

	@RequestMapping(method = RequestMethod.POST)
	public void sendPostMessage(WebRequest request, Model model,
			@ModelAttribute("messageForm") @Validated MessageForm messageForm, Principal principal) {

		UserConnection uc = userConnectionDAO.findUserConnectionByUserName(principal.getName());

		try {
			Twitter twitter = new TwitterFactory().getInstance();

			twitter.setOAuthConsumer(socialProperties.getTwitterConsumerKey(),
					socialProperties.getTwitterConsumerSecret());
			AccessToken accessToken = new AccessToken(uc.getAccessToken(), uc.getSecret());

			twitter.setOAuthAccessToken(accessToken);

			// Statusmleline
			// ResponseList<Status> timeline = twitter.getHomeTimeline() ;
			 



			// post tweet

			// with img if needed
			/*
			 * File file = new File("/images/Done.jpg");
			 * 
			 * StatusUpdate status = new StatusUpdate(statusMessage); status.setMedia(file);
			 * // set the image to be uploaded here. twitter.updateStatus(status);
			 */

			twitter.updateStatus(messageForm.getMessage());

			// send DM
			// twitter.sendDirectMessage("@_doolmen", "Hello !");

		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}
	
	
}
