package com.pagestags.cntr;

import static com.pagestags.thinmvc.ThinmvcParameters.BASE_PATH;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pagestags.TemplateUtil;
import com.pagestags.auth.AuthValidator;
import com.pagestags.db.PagesRepository;
import com.pagestags.mdl.Page;
import com.pagestags.thinmvc.cntr.TemplateController;

public class PageListTmplCntr extends TemplateController {

	public PageListTmplCntr(String path) {
		super(path);
	}

	public Map<String, Object> getContext() {
		List<Page> pages;
		PagesRepository pagesRepo = new PagesRepository();
		boolean auth = AuthValidator.isAuthenticated(request);

		List<String> selectedTags;
		Matcher matcher = Pattern.compile(BASE_PATH.get() + "/pages/list/(.+)").matcher(request.uri());

		boolean filtered = matcher.find();
		if (filtered) {
			selectedTags = Arrays.asList(matcher.group(1).split(Pattern.quote("$")));
			pages = pagesRepo.getPages(selectedTags).stream().filter(p -> p.pblic() || auth).toList();
		} else {
			selectedTags = Collections.emptyList();
			pages = pagesRepo.getPages().stream().filter(p -> p.pblic() || auth).toList();
		}

		List<String> allTags = pages.stream().flatMap(p -> p.tags().stream()).distinct()
				.filter(t -> !selectedTags.contains(t)).toList();

		if (!filtered && !auth) {
			pages = Collections.emptyList();
		}

		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "List Pages");
		data.put("auth", auth);
		data.put("selected_tags_str", String.join("$", selectedTags));
		data.put("selected_tags", selectedTags);
		data.put("tags", allTags);
		data.put("items", pages);
		data.put("template_util", TemplateUtil.class);
		data.put("filtered", filtered);

		return data;
	}

}
