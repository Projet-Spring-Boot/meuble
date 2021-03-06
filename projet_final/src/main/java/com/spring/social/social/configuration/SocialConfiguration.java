package com.spring.social.social.configuration;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.spring.social.configuration.SocialProperties;
import com.spring.social.dao.AppUserDAO;
import com.spring.social.social.ConnectionSignUpImpl;

@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer, WebMvcConfigurer {

	private boolean autoSignUp = false;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private SocialProperties socialProperties;

	// @env: read from social-cfg.properties file.
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {

		try {
			this.autoSignUp = Boolean.parseBoolean(socialProperties.getAutoSignup());
		} catch (Exception e) {
			this.autoSignUp = false;
		}

		// Twitter
		TwitterConnectionFactory tfactory = new TwitterConnectionFactory(socialProperties.getTwitterConsumerKey(),
				socialProperties.getTwitterConsumerSecret());

		cfConfig.addConnectionFactory(tfactory);

		// Google
		GoogleConnectionFactory gfactory = new GoogleConnectionFactory(socialProperties.getGoogleClientId(),
				socialProperties.getGoogleClientSecret());

		gfactory.setScope(socialProperties.getGoogleScope());

		cfConfig.addConnectionFactory(gfactory);
	}

	// The UserIdSource determines the userID of the user.
	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	// USERCONNECTION.
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

		// org.springframework.social.security.SocialAuthenticationServiceRegistry
		JdbcUsersConnectionRepository usersConnectionRepository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator,

				Encryptors.noOpText());

		if (autoSignUp) {
			// After logging in to social networking.
			// Automatically creates corresponding APP_USER if it does not exist.
			ConnectionSignUp connectionSignUp = new ConnectionSignUpImpl(appUserDAO);
			usersConnectionRepository.setConnectionSignUp(connectionSignUp);
		} else {
			// After logging in to social networking.
			// If the corresponding APP_USER record is not found.
			// Navigate to registration page.
			usersConnectionRepository.setConnectionSignUp(null);
		}
		return usersConnectionRepository;
	}

	// This bean manages the connection flow between the account provider
	// and the example application.
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, //
			ConnectionRepository connectionRepository) {
		return new ConnectController(connectionFactoryLocator, connectionRepository);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}