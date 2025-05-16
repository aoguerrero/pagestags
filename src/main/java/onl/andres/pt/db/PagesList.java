package onl.andres.pt.db;

import java.util.ArrayList;
import java.util.List;

import onl.andres.pt.mdl.Page;

public enum PagesList {
	
	INSTANCE;

	private List<Page> pages;

	private PagesList() {
		this.pages = new ArrayList<>();
	}
	
	public synchronized void addAllPages(List<Page> pages) {
		this.pages = pages;
	}
	
	public synchronized void addPage(Page page) {
		this.pages.add(page);
	}

	public synchronized void removePage(String id) {
		this.pages.remove(new Page(id, null, null, false));
	}

	public List<Page> getPages() {
		return this.pages;
	}
}
