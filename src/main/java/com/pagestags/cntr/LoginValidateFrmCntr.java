package com.pagestags.cntr;

import static com.pagestags.Constants.PASSWORD;
import static com.pagestags.Constants.SESSION_ID;

import onl.andres.thinmvc.cntr.FormController;
import onl.andres.thinmvc.excp.ServiceException;;

public class LoginValidateFrmCntr extends FormController {

	public LoginValidateFrmCntr(String path) {
		super(path);
	}

	@Override
	public String execute() {
		String userPassword = getFormData().get("password");
		if (System.getProperty(PASSWORD) != null) {
			if (userPassword.equals(System.getProperty(PASSWORD))) {
				getResponseHeaders().add("Set-Cookie", "sessionId=" + System.getProperty(SESSION_ID) + "; Path=/");
			} else {
				throw new ServiceException.Unauthorized();
			}
		}
		return null;
	}

}
