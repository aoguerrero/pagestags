package com.pagestags.cntr;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pagestags.auth.AuthValidator;
import com.pagestags.db.PagesRepository;
import com.pagestags.mdl.Page;

import onl.andres.thinmvc.cntr.TemplateController;

public class PageListTmplCntr extends TemplateController {

	public PageListTmplCntr(String path) {
		super(path);
	}

	public Map<String, Object> getContext() {
		List<Page> pages;
		PagesRepository pagesRepo = new PagesRepository();
		boolean auth = AuthValidator.isAuthenticated(request);

		List<String> selectedTags;
		Matcher matcher = Pattern.compile("/pages/list/(.+)").matcher(request.uri());
		if (matcher.find()) {
			selectedTags = Arrays.asList(matcher.group(1).split("-"));
			pages = pagesRepo.getPages(selectedTags).stream().filter(p -> p.isPblic() || auth).toList();
		} else {
			selectedTags = Collections.emptyList();
			pages = pagesRepo.getPages().stream().filter(p -> p.isPblic() || auth).toList();
		}
		
		List<String> allTags = pages.stream().flatMap(p -> p.getTags().stream()).distinct()
				.filter(t -> !selectedTags.contains(t)).toList();

		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "List Pages");
		data.put("auth", auth);
		data.put("selectedTagsDisplay", String.join(" ", selectedTags));
		data.put("selectedTags", String.join("-", selectedTags));
		data.put("tags", allTags);
		data.put("items", pages);
		return data;
	}

}