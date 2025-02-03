package com.pagestags.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pagestags.mdl.Page;

public enum PagesList {
	
	INSTANCE;

	private List<Page> pages;

	private PagesList() {
		pages = new ArrayList<>();
	}

	public void addPage(Page page) {
		pages.add(page);
		Collections.sort(pages);
	}

	public void removePage(String id) {
		pages.remove(new Page(id, null, null, false));
	}

	public List<Page> getPages() {
		return pages;
	}
}
