package onl.andres.pt.ctrl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.ctrl.TemplateController;

public class PageDeleteConfirmationTemplateCtrl extends TemplateController {

	public PageDeleteConfirmationTemplateCtrl(String path) {
		super(path);
	}

	@Override
	public Map<String, Object> getContext(HttpRequest request) {
		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "Delete Confirmation");
		Matcher matcher = Pattern.compile("/pages/(.*)/delete/confirmation").matcher(request.uri());
		if (matcher.find()) {
			data.put("id", matcher.group(1));
		}
		return data;
	}

}
