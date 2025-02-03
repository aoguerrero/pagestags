package com.pagestags.cntr;

import static com.pagestags.thinmvc.ThinmvcParameters.BASE_PATH;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pagestags.thinmvc.cntr.TemplateController;

public class PageNewTmplCntr extends TemplateController {

	public PageNewTmplCntr(String path) {
		super(path);
	}

	public Map<String, Object> getContext() {

		Matcher matcher = Pattern.compile(BASE_PATH.get() + "/pages/new/(.*)").matcher(request.uri());

		String selectedTagsStr = "";
		String selectedTagsStrSp = "";
		if (matcher.find()) {
			selectedTagsStr = matcher.group(1);
			selectedTagsStrSp = selectedTagsStr.replace("$", " ");
		}

		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "New Page");
		data.put("selected_tags_str", selectedTagsStr);
		data.put("selected_tags_str_sp", selectedTagsStrSp);
		return data;
	}

}
