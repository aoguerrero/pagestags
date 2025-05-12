package onl.andres.pt.cntr;

import java.util.HashMap;
import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.cntr.TemplateController;

public class LoginTmplCntr extends TemplateController {

	public LoginTmplCntr(String path) {
		super(path);
	}

	public Map<String, Object> getContext(HttpRequest request) {
		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "Login");
		return data;
	}

}
