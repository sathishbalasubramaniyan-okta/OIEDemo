package com.okta.demo.util;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

public class JwtParser {

	/**
	 * Decodes a JWT and then parses it into a JSON Object.
	 * @param jwt
	 * @return
	 */
	public static JSONObject parseJWT(String jwt) {
		
		JSONObject jObj = null;
		try {
			String[] split_string = jwt.split("\\.");
	        String base64EncodedBody = split_string[1];
	        Base64 base64Url = new Base64(true);
	        //System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
	        String body = new String(base64Url.decode(base64EncodedBody));
	        //System.out.println("JWT Body : "+body);    
			
	        jObj = new JSONObject(body);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        
		return jObj;
		
	}
	
}
