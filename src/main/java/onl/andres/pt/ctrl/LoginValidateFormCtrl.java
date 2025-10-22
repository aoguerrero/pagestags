package onl.andres.pt.ctrl;

import static onl.andres.pt.AppParameters.USERNAME;
import static onl.andres.pt.AppParameters.PASSWORD;
import static onl.andres.pt.AppParameters.SESSION_ID;

import java.util.Map;
import java.util.Optional;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.ctrl.FormController;
import onl.andres.mvcly.excp.ServiceException;

public class LoginValidateFormCtrl extends FormController {

	public LoginValidateFormCtrl(String redirectPath) {
		super(redirectPath);
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
