package com.spring.social.form;

import org.springframework.stereotype.Component;

@Component
public class ChangePasswordForm {
	
	private String old_password;
	private String new_password;
	private String new_password_checked;
	
	public String getOld_password() {
		return old_password;
	}
	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	public String getNew_password_checked() {
		return new_password_checked;
	}
	public void setNew_password_checked(String new_password_checked) {
		this.new_password_checked = new_password_checked;
	}
	
	

}
