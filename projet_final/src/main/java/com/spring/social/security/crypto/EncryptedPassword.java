package com.spring.social.security.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptedPassword {

	// Encryte Password with BCryptPasswordEncoder
	public static String encrytePassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
	public static boolean checkPassword(String password, String encodedPassword) {
		return new BCryptPasswordEncoder().matches(password, encodedPassword);
	}
}
