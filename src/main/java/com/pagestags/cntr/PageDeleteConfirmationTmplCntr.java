package com.pagestags.cntr;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pagestags.thinmvc.cntr.TemplateController;

public class PageDeleteConfirmationTmplCntr extends TemplateController {

	public PageDeleteConfirmationTmplCntr(String path) {
		super(path);
	}

	@Override
	public Map<String, Object> getContext() {
		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "Delete Confirmation");
		Matcher matcher = Pattern.compile("/pages/(.*)/delete/confirmation").matcher(request.uri());
		if (matcher.find()) {
			data.put("id", matcher.group(1));
		}
		return data;
	}

}
