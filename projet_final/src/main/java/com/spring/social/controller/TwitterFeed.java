package com.spring.social.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.spring.social.configuration.SocialProperties;
import com.spring.social.dao.FlowDAO;
import com.spring.social.dao.UserConnectionDAO;
import com.spring.social.entity.UserConnection;
import com.spring.social.form.MessageForm;
import com.spring.social.model.Flow;
import com.spring.social.model.FlowUtils;
import com.spring.social.model.MediaUtils;
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
	public String twitterFeed(Model model, Principal principal) {


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
		
		List<Flow> listFlow = new ArrayList<Flow>(mapFlow.values());
		model.addAttribute("messageForm", new MessageForm());
		
		List<FlowUtils> flowsUtils = new ArrayList<FlowUtils>();
		
		listFlow.forEach(flow -> {
			List<MediaUtils> mediasUtils = new ArrayList<MediaUtils>();
			
			List<String> publishedMedias = flow.getPublished_media();
			
			for(int i=0 ; i<publishedMedias.size() ; i+=2) {
				MediaUtils mediaUtils = new MediaUtils();
				mediaUtils.setMediaText(publishedMedias.get(i));
				mediaUtils.setMediaUrl(publishedMedias.get(i+1));
				mediasUtils.add(mediaUtils);
			}
			
			FlowUtils flowUtils = new FlowUtils();
			flowUtils.setFlow(flow);
			flowUtils.setMediasUtils(mediasUtils);
			
			
			
			flowsUtils.add(flowUtils);
			
		});
		
		flowsUtils.sort(new Comparator<FlowUtils>() {

			@Override
			public int compare(FlowUtils o1, FlowUtils o2) {
				return o2.getFlow().getPublishing().compareTo(o1.getFlow().getPublishing());
			}
		});
		
		model.addAttribute("flowsUtils", flowsUtils);
		

		
		
		return "feed";
	}

	@RequestMapping(value = "/updateFlow", method = RequestMethod.POST)
	@Cacheable("FLOW")
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
	
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public String sendPostMessage(WebRequest request, Model model,
			@ModelAttribute("messageForm") @Validated MessageForm messageForm, Principal principal) {

		UserConnection uc = userConnectionDAO.findUserConnectionByUserName(principal.getName());
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(socialProperties.getTwitterConsumerKey(),
				socialProperties.getTwitterConsumerSecret());
		AccessToken accessToken = new AccessToken(uc.getAccessToken(), uc.getSecret());
		twitter.setOAuthAccessToken(accessToken);

		try {
			twitter.updateStatus(messageForm.getMessage());

		} catch (TwitterException te) {
			te.printStackTrace();
		}
		
		return "redirect:/twitterfeed";
	}
}