package com.spring.social.model;

import org.springframework.stereotype.Component;

@Component
public class MediaUtils {
	private String mediaText;
	private String mediaUrl;
	
	public String getMediaText() {
		return mediaText;
	}
	public void setMediaText(String mediaText) {
		this.mediaText = mediaText;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	
	
}
