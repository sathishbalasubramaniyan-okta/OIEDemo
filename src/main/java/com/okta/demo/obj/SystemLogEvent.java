package com.okta.demo.obj;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "systemLogEvent")

public class SystemLogEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private String eventType;
	private String displayMessage;
	private String result;
	private String reason;
	private String published;
	private String user;
	private String ip;
	
	public SystemLogEvent() {
	}

	public SystemLogEvent(String eventType, String displayMessage, String result, String reason, String published, String user, String ip) {
		this.eventType = eventType;
		this.displayMessage = displayMessage;
		this.result = result;
		this.reason = reason;
		this.published = published;
		this.user = user;
		this.ip = ip;
	}

	public String getEventType() {
		return eventType;
	}

	@XmlElement
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getdisplayMessage() {
		return displayMessage;
	}

	@XmlElement
	public void setdisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}
	
	public String getResult() {
		return result;
	}

	@XmlElement
	public void setResult(String result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	@XmlElement
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getPublished() {
		return published;
	}

	@XmlElement
	public void setPublished(String published) {
		this.published = published;
	}
	
	public String getUser() {
		return user;
	}

	@XmlElement
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getIp() {
		return ip;
	}

	@XmlElement
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String toXML() {
		StringBuffer xml = new StringBuffer("<systemLogEvent>\n");
		xml.append("    <eventType>" + this.getEventType() + "</eventType>\n");
		xml.append("    <result>" + this.getResult() + "</result>\n");
		xml.append("    <reason>" + this.getReason() + "</reason>\n");
		xml.append("    <published>" + this.getPublished() + "</published>\n");
		xml.append("    <user>" + this.getUser() + "</user>\n");
		xml.append("    <ip>" + this.getIp() + "</ip>\n");
		xml.append("</systemLogEvent>");
		
		return xml.toString();
	}

	public static void main(String[] args) {
		// For testing only
		SystemLogEvent systemLogEvent = new SystemLogEvent();
		systemLogEvent.setEventType("security.request.blocked");
		systemLogEvent.setResult("SUCCESS");
		systemLogEvent.setReason("NETWORK_ZONE_BLACKLIST");
		systemLogEvent.setPublished("2019-04-20T06:42:31.208Z");
		systemLogEvent.setUser("system@okta.com");
		systemLogEvent.setIp("54.190.160.64");
		System.out.println(systemLogEvent.toXML());
	}
}