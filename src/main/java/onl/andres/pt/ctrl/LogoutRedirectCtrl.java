package onl.andres.pt.ctrl;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.ctrl.RedirectController;

public class LogoutRedirectCtrl extends RedirectController {

	public LogoutRedirectCtrl(String path) {
		super(path);
	}

	@Override
	public String execute(HttpRequest request) {
		getResponseHeaders().add("Set-Cookie", "sessionId=; Path=/");
		return null;
	}

}
