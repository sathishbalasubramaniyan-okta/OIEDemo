package com.okta.demo.obj;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "linkedIdp")

public class LinkedIdp implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String type;
	private String name;
	private String status;
	private String created;

	public LinkedIdp() {
	}

	public LinkedIdp(String id, String type, String name, String status, String created) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.status = status;
		this.created = created;
	}

	public String getId() {
		return id;
	}

	@XmlElement
	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	@XmlElement
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	@XmlElement
	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated() {
		return created;
	}

	@XmlElement
	public void setCreated(String created) {
		this.created = created;
	}
	
	public String toXML() {
		StringBuffer xml = new StringBuffer("<linkedIdps>\n");
		xml.append("    <id>" + this.getId() + "</id>\n");
		xml.append("    <type>" + this.getType() + "</type>\n");
		xml.append("    <name>" + this.getName() + "</name>\n");
		xml.append("    <status>" + this.getStatus() + "</status>\n");
		xml.append("    <created>" + this.getCreated() + "</created>\n");
		xml.append("</linkedIdps>");
		
		return xml.toString();
	}

	public static void main(String[] args) {
		// For testing only
		LinkedIdp linkedIdp = new LinkedIdp();
		linkedIdp.setId("0oa1l6l8j5rdMBWtG356");
		linkedIdp.setType("GOOGLE");
		linkedIdp.setName("Login to Google");
		linkedIdp.setStatus("ACTIVE");
		linkedIdp.setCreated("2018-09-11T03:10:14.000Z");
		System.out.println(linkedIdp.toXML());
	}
}