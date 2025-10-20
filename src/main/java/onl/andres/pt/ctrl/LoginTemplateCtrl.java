package onl.andres.pt.ctrl;

import java.util.HashMap;
import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.ctrl.TemplateController;

public class LoginTemplateCtrl extends TemplateController {

	public LoginTemplateCtrl(String path) {
		super(path);
	}

	public Map<String, Object> getContext(HttpRequest request) {
		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "Login");
		return data;
	}

}
