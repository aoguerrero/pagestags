package onl.andres.pt.auth;

import static onl.andres.pt.PTParameters.SESSION_ID;

import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.thinmvc.utl.HttpUtils;

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
