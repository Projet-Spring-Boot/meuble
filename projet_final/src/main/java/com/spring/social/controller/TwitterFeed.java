package com.spring.social.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.social.configuration.SocialProperties;
import com.spring.social.dao.FlowDAO;
import com.spring.social.dao.UserConnectionDAO;
import com.spring.social.entity.UserConnection;
import com.spring.social.model.Flow;
import com.spring.social.random.TokenGenerator;

import twitter4j.MediaEntity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Controller
@RequestMapping("/twitterfeed")
public class TwitterFeed {

	@Autowired
	private UserConnectionDAO userConnectionDAO;

	@Autowired
	private SocialProperties socialProperties;

	@Autowired
	private FlowDAO flowDAO;

	@RequestMapping(method = RequestMethod.GET)
	public String twitterFeed(Model model, Principal principal,
			@RequestParam(name = "action", required = false) String action) {

		System.out.println("coucou lol");

		Map<String, Flow> mapFlow = updateFlow(principal.getName());

		mapFlow.forEach((key, value) -> {
			System.out.println("===key: ===\n" + key);
			System.out.println("===value :===");
			System.out.println("id : " + value.getId());
			System.out.println("published_content : " + value.getPublished_content());
			System.out.println("date : " + value.getPublishing());
			System.out.println("user image :" + value.getUser_img());
			System.out.println("media :");
			value.getPublished_media().forEach(media -> {
				System.out.println("\t" + media);
			});

			System.out.println();
			System.out.println();
			System.out.println("*******************************************************");
			System.out.println();
			System.out.println();
		});

		// model.addAttribute('timeline',mapFlow);
		return "feedPage";
	}

	@RequestMapping(value = "/updateFlow", method = RequestMethod.POST)
	public Map<String, Flow> updateFlow(@RequestBody String username) {

		Map<String, Flow> mapFlow = null;

		// [Step 1] : Set up Twitter4j
		UserConnection uc = userConnectionDAO.findUserConnectionByUserName(username);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(socialProperties.getTwitterConsumerKey(), socialProperties.getTwitterConsumerSecret());
		AccessToken accessToken = new AccessToken(uc.getAccessToken(), uc.getSecret());
		twitter.setOAuthAccessToken(accessToken);

		try {
			// [Step 2] : Récupérer la timeline de l'utilisateur
			ResponseList<Status> timeline = twitter.getHomeTimeline();

			flowDAO.drop();
			// [Step 3.bis] : enregistrer dans mapFlow depuis la Timeline créée

			timeline.forEach(tweet -> {
				Flow flow = new Flow();
				flow.setId(TokenGenerator.generateNewToken());
				flow.setUser_name(tweet.getUser().getName());
				flow.setUser_img(tweet.getUser().get400x400ProfileImageURL());
				flow.setPublished_content(tweet.getText());
				flow.setPublishing(tweet.getCreatedAt());

				MediaEntity[] medias = tweet.getMediaEntities();
				List<String> mediaList = new ArrayList<String>();
				for (int i = 0; i < medias.length; i++) {
					mediaList.add(medias[i].getText());
					mediaList.add(medias[i].getMediaURL());
				}
				flow.setPublished_media(mediaList);

				// Enregister la timelined en BDD Redis
				flowDAO.save(flow);
			});

			mapFlow = flowDAO.findAll();

		} catch (TwitterException error) {
			// Si il y a un problème avec la requête HTTP, on récupère dans la BDD
			mapFlow = flowDAO.findAll();
		}
		return mapFlow;
	}
}
