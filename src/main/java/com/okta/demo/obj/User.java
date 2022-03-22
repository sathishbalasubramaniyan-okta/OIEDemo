package com.okta.demo.obj;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String username;
	private String displayName;
	private String mobilePhone;
	private String email;
	private String propertyAddress;
	private String emailVerified;

	public User() {
	}

	public User(String id, String username, String displayName, String mobilePhone, String email, String propertyAddress, String emailVerified) {
		this.id = id;
		this.username = username;
		this.displayName = displayName;
		this.mobilePhone = mobilePhone;
		this.email = email;
		this.propertyAddress = propertyAddress;
		this.emailVerified = emailVerified;
	}

	public String getId() {
		return id;
	}

	@XmlElement
	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	@XmlElement
	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	@XmlElement
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	@XmlElement
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	@XmlElement
	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}
	
	@XmlElement
	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getEmailVerified() {
		return emailVerified;
	}
	
	public String toXML() {
		StringBuffer xml = new StringBuffer("<user>\n");
		xml.append("    <id>" + this.getId() + "</id>\n");
		xml.append("    <username>" + this.getUsername() + "</username>\n");
		xml.append("    <displayName>" + this.getDisplayName() + "</displayName>\n");
		xml.append("    <mobilePhone>" + this.getMobilePhone() + "</mobilePhone>\n");
		xml.append("    <email>" + this.getEmail() + "</email>\n");
		xml.append("    <propertyAddress>" + this.getPropertyAddress() + "</propertyAddress>\n");
		xml.append("</user>");
		
		return xml.toString();
	}

	public static void main(String[] args) {
		// For testing only
		User user = new User();
		user.setId("0oa1l6l8j5rdMBWtG356");
		user.setUsername("mark@acme.com");
		user.setDisplayName("Mark Smith");
		user.setMobilePhone("0434185567");
		user.setEmail("okta.apac@gmail.com");
		user.setPropertyAddress("123 First Street, San Francisco, CA, USA");
		System.out.println(user.toXML());
	}
}