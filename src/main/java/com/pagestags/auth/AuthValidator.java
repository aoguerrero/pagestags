package com.pagestags.auth;

import static com.pagestags.PagestagsParameters.SESSION_ID;

import java.util.Map;

import com.pagestags.thinmvc.utl.HttpUtils;

import io.netty.handler.codec.http.HttpRequest;

public class AuthValidator {

	private AuthValidator() {
	}

	public static boolean isAuthenticated(HttpRequest request) {
		String cookiesStr = request.headers().get("Cookie");
		Map<String, String> cookies = HttpUtils.cookiesToMap(cookiesStr);
		String sessionId = cookies.get("sessionId");
		if (sessionId != null && sessionId.equals(SESSION_ID.get())) {
			return true;
		}
		return false;
	}
}
