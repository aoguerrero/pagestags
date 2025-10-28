package onl.andres.pt.ctrl;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.core.AppCtx;
import onl.andres.mvcly.ctrl.TemplateController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageDeleteConfirmationTemplateCtrl extends TemplateController {

	public PageDeleteConfirmationTemplateCtrl(String path, AppCtx ctx) {
		super(path, ctx);
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
