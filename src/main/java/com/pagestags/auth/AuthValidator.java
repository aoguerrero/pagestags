package com.pagestags.auth;

import static com.pagestags.Constants.SESSION_ID;

import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.thinmvc.utl.HttpUtils;

public class AuthValidator {

	private AuthValidator() {}

	public static boolean isAuthenticated(HttpRequest request) {
		if (request != null && request.headers().get("Cookie") != null) {
			String cookiesStr = request.headers().get("Cookie");
			Map<String, String> cookies = HttpUtils.cookiesToMap(cookiesStr);
			String sessionId = cookies.get("sessionId");
			if (sessionId != null && sessionId.equals(System.getProperty(SESSION_ID))) {
				return true;
			}
		}
		return false;
	}
}
