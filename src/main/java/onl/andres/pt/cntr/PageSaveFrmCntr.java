package onl.andres.pt.cntr;

import static onl.andres.pt.AppParameters.PAGES_PATH;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.cntr.FormController;
import onl.andres.mvcly.excp.ServiceException;
import onl.andres.mvcly.utl.FileSystemUtils;
import onl.andres.pt.auth.AuthValidator;
import onl.andres.pt.db.PagesCache;
import onl.andres.pt.db.PagesRepository;
import onl.andres.pt.mdl.Page;

public class PageSaveFrmCntr extends FormController {

	private PagesCache pagesCache;

	public PageSaveFrmCntr(String path, PagesCache pagesCache) {
		super(path);
		this.pagesCache = pagesCache;
	}

	@Override
	public Optional<String> execute(HttpRequest request, Map<String, String> formData) {
		if (!AuthValidator.isAuthenticated(request)) {
			throw new ServiceException.Unauthorized();
		}
		String title = formData.get("title");

		Matcher matcher = Pattern.compile("/pages/(.*)/save").matcher(request.uri());
		String id = getId(title);
		if (matcher.find()) {
			id = matcher.group(1);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(title).append("\n");
		String tags = formData.get("tags");
		tags = tags.replaceAll(" +", " ").trim();
		sb.append(tags).append("\n");
		String pblic = formData.get("public");
		if (pblic != null) {
			sb.append("public").append("\n");
		} else {
			sb.append("private").append("\n");
		}
		sb.append(formData.get("content"));
		String newId = formData.get("identifier");

		Path savePath = Paths.get(PAGES_PATH.get(), id).toAbsolutePath();
		FileSystemUtils.writeStringToFile(savePath, sb.toString());
		PagesRepository pagesRepo = new PagesRepository(pagesCache);
		pagesRepo.removePage(id);
		List<String> tagList = Arrays.asList(tags.split(" "));
		if (newId != null && !newId.equals(id)) {
			Path newPath = Paths.get(PAGES_PATH.get(), newId).toAbsolutePath();
			FileSystemUtils.renameFile(savePath, newPath);
			pagesRepo.putPage(new Page(newId, title, tagList, pblic != null));
			return Optional.of(newId);
		}
		pagesRepo.putPage(new Page(id, title, tagList, pblic != null));
		return Optional.of(id);
	}

	private String getId(String title) {
		StringBuilder id = new StringBuilder(
				Normalizer.normalize(title, Normalizer.Form.NFD).toLowerCase().replaceAll("\\W+", "_"));
		boolean valid = false;
		int counter = 0;
		while (!valid) {
			Path savePath = Paths.get(PAGES_PATH.get(), id.toString()).toAbsolutePath();
			if (Files.exists(savePath)) {
				id.append("-").append(++counter);
			} else {
				valid = true;
			}
		}
		return id.toString();
	}
}
