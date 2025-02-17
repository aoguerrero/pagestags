package onl.andres.pt.cntr;

import static onl.andres.pt.PTParameters.PASSWORD;
import static onl.andres.pt.PTParameters.SESSION_ID;

import java.util.Map;
import java.util.Optional;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.thinmvc.cntr.FormController;
import onl.andres.thinmvc.excp.ServiceException;;

public class LoginValidateFrmCntr extends FormController {

	public LoginValidateFrmCntr(String path) {
		super(path);
	}

	@Override
	public Optional<String> execute(HttpRequest request, Map<String, String> formData) {
		String userPassword = formData.get("password");
		if (userPassword.equals(PASSWORD.get())) {
			getResponseHeaders().add("Set-Cookie", "sessionId=" + SESSION_ID.get() + "; Path=/");
		} else {
			throw new ServiceException.Unauthorized();
		}
		return Optional.empty();
	}

}
