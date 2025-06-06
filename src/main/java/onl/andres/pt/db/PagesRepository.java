package onl.andres.pt.db;

import static onl.andres.pt.AppParameters.PAGES_PATH;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import onl.andres.mvcly.excp.ServiceException;
import onl.andres.pt.mdl.Page;

public class PagesRepository {

	private Path pagesPath = Path.of(PAGES_PATH.get()).toAbsolutePath();

	private PagesList pagesList = PagesList.INSTANCE;

	public void scanPages() {
		try (Stream<Path> filePathList = Files.list(pagesPath)) {
			pagesList.addAllPages(filePathList.filter(p -> Files.isRegularFile(p, LinkOption.NOFOLLOW_LINKS))
					.map(this::getContent).toList());

		} catch (Exception e) {
			throw new ServiceException.InternalServer(e);
		}
	}

	private Page getContent(Path path) {
		try (BufferedReader bf = Files.newBufferedReader(path)) {
			String title = bf.readLine();
			String pagesTags = bf.readLine();
			String pblic = bf.readLine();
			return new Page(path.getFileName().toString(), title, Arrays.asList(pagesTags.split(" ")),
					pblic.startsWith("public"));
		} catch (Exception e) {
			throw new ServiceException.InternalServer(e);
		}
	}

	public List<Page> getPages() {
		return pagesList.getPages();
	}

	public List<Page> getPages(List<String> tags) {
		return getPages().stream().filter(p -> p.tags().containsAll(tags)).toList();
	}

	public void putPage(Page page) {
		pagesList.addPage(page);
	}

	public void removePage(String id) {
		pagesList.removePage(id);
	}

	public List<String> getTags(boolean auth) {
		return pagesList.getPages().stream().filter(p -> p.pblic() || auth).flatMap(p -> p.tags().stream()).distinct().sorted()
				.toList();
	}
}
