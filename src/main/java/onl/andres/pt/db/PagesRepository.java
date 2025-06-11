package onl.andres.pt.db;

import java.util.List;

import onl.andres.pt.mdl.Page;

public class PagesRepository {

	private PagesCache pagesCache;

	public PagesRepository(PagesCache pagesCache) {
		this.pagesCache = pagesCache;
	}

	public List<Page> getPages() {
		return pagesCache.getPages();
	}

	public List<Page> getPages(List<String> tags) {
		return getPages().stream().filter(p -> p.tags().containsAll(tags)).toList();
	}

	public void putPage(Page page) {
		pagesCache.addPage(page);
	}

	public void removePage(String id) {
		pagesCache.removePage(id);
	}

	public List<String> getTags(boolean auth) {
		return pagesCache.getPages().stream().filter(p -> p.pblic() || auth).flatMap(p -> p.tags().stream()).distinct()
				.sorted().toList();
	}
}
