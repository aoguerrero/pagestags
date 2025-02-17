package onl.andres.pt.cntr;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.thinmvc.cntr.RedirectController;

public class LogoutRdrcCntr extends RedirectController {

	public LogoutRdrcCntr(String path) {
		super(path);
	}

	@Override
	public String execute(HttpRequest request) {
		getResponseHeaders().add("Set-Cookie", "sessionId=; Path=/");
		return null;
	}

}
