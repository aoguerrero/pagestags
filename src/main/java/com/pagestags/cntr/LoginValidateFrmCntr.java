package com.pagestags.cntr;

import static com.pagestags.PagestagsParameters.PASSWORD;
import static com.pagestags.PagestagsParameters.SESSION_ID;

import java.util.Optional;

import com.pagestags.thinmvc.cntr.FormController;
import com.pagestags.thinmvc.excp.ServiceException;;

public class LoginValidateFrmCntr extends FormController {

	public LoginValidateFrmCntr(String path) {
		super(path);
	}

	@Override
	public Optional<String> execute() {
		String userPassword = getFormData().get("password");
		if (userPassword.equals(PASSWORD.get())) {
			getResponseHeaders().add("Set-Cookie", "sessionId=" + SESSION_ID.get() + "; Path=/");
		} else {
			throw new ServiceException.Unauthorized();
		}
		return Optional.empty();
	}

}
