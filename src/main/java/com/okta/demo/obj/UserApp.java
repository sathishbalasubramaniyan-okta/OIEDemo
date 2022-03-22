package com.okta.demo.obj;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userApps")

public class UserApp implements Serializable {
	private static final long serialVersionUID = 1L;
	private String label;
	private String logoUrl;
	private String linkUrl;

	public UserApp() {
	}

	public UserApp(String label, String logoUrl, String linkUrl) {
		this.label = label;
		this.logoUrl = logoUrl;
		this.linkUrl = linkUrl;
	}

	public String getLabel() {
		return label;
	}

	@XmlElement
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	@XmlElement
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	@XmlElement
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public String toXML() {
		StringBuffer xml = new StringBuffer("<userApps>\n");
		xml.append("    <label>" + this.getLabel() + "</label>\n");
		xml.append("    <logoUrl>" + this.getLogoUrl() + "</logoUrl>\n");
		xml.append("    <linkUrl>" + this.getLinkUrl() + "</linkUrl>\n");
		xml.append("</userApps>");
		
		return xml.toString();
	}

	public static void main(String[] args) {
		// For testing only
		UserApp linkedIdp = new UserApp();
		linkedIdp.setLabel("Assesment Master");
		linkedIdp.setLogoUrl("https://ok7static.oktacdn.com/bc/image/fileStoreRecord?id=fs024jqlb45KIl6DX356");
		linkedIdp.setLinkUrl("https://nzqa.okta.com/home/bookmark/0oa24k2ieEXJ7kyzj356/2557");
		System.out.println(linkedIdp.toXML());
	}
}