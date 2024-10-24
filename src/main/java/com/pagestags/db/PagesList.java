package com.pagestags.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pagestags.mdl.Page;

public class PagesList {

	private static final PagesList instance = new PagesList();

	private List<Page> pages;

	private PagesList() {
		pages = new ArrayList<>();
	}

	public static synchronized PagesList getInstance() {
		return instance;
	}

	public synchronized void addPage(Page page) {
		pages.add(page);
		Collections.sort(pages);
	}

	public synchronized void removePage(String id) {
		pages.remove(new Page(id, null, null, false));
	}

	public List<Page> getPages() {
		return pages;
	}
}
