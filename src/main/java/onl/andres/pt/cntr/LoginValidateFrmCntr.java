package onl.andres.pt.cntr;

import static onl.andres.pt.AppParameters.USERNAME;
import static onl.andres.pt.AppParameters.PASSWORD;
import static onl.andres.pt.AppParameters.SESSION_ID;

import java.util.Map;
import java.util.Optional;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.cntr.FormController;
import onl.andres.mvcly.excp.ServiceException;

public class LoginValidateFrmCntr extends FormController {

	public LoginValidateFrmCntr(String path) {
		super(path);
	}

	@Override
	public Optional<String> execute(HttpRequest request, Map<String, String> formData) {
		String username = formData.get("username");
		String password = formData.get("password");
		if (username.equals(USERNAME.get()) && password.equals(PASSWORD.get())) {
			getResponseHeaders().add("Set-Cookie", "sessionId=" + SESSION_ID.get() + "; Path=/");
		} else {
			throw new ServiceException.Unauthorized();
		}
		return Optional.empty();
	}

}
