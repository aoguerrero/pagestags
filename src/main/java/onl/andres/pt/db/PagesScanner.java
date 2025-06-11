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

public class PagesScanner {

	private PagesScanner() {
	}

	public static List<Page> scanPages() {
		Path pagesPath = Path.of(PAGES_PATH.get()).toAbsolutePath();

		try (Stream<Path> filePathList = Files.list(pagesPath)) {
			return filePathList.filter(p -> Files.isRegularFile(p, LinkOption.NOFOLLOW_LINKS))
					.map(PagesScanner::getContent).toList();

		} catch (Exception e) {
			throw new ServiceException.InternalServer(e);
		}
	}

	private static Page getContent(Path path) {
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

}
