package com.pagestags.cntr;

import com.pagestags.thinmvc.cntr.RedirectController;

public class LogoutRdrcCntr extends RedirectController {

	public LogoutRdrcCntr(String path) {
		super(path);
	}

	@Override
	public String execute() {
		getResponseHeaders().add("Set-Cookie", "sessionId=; Path=/");
		return null;
	}

}
