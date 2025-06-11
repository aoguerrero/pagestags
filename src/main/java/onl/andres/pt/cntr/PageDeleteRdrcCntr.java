package onl.andres.pt.cntr;

import static onl.andres.pt.AppParameters.PAGES_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.cntr.RedirectController;
import onl.andres.mvcly.excp.ServiceException;
import onl.andres.pt.auth.AuthValidator;
import onl.andres.pt.db.PagesCache;
import onl.andres.pt.db.PagesRepository;

public class PageDeleteRdrcCntr extends RedirectController {

	private PagesCache pagesCache;
	
	public PageDeleteRdrcCntr(String path, PagesCache pagesCache) {
		super(path);
		this.pagesCache = pagesCache;
	}

	@Override
	public String execute(HttpRequest request) {
		if (!AuthValidator.isAuthenticated(request)) {
			throw new ServiceException.Unauthorized();
		}
		Matcher matcher = Pattern.compile("/pages/(.*)/delete").matcher(request.uri());
		if (matcher.find()) {
			try {
				String id = matcher.group(1);
				Path trashDir = Paths.get(PAGES_PATH.get(), "trash").toAbsolutePath();
				if (!Files.isDirectory(trashDir)) {
					Files.createDirectory(trashDir);
				}
				Path deletePath = Paths.get(PAGES_PATH.get(), id).toAbsolutePath();
				Files.move(deletePath, Paths.get(trashDir.toString(), id).toAbsolutePath());
				PagesRepository pagesRepo = new PagesRepository(pagesCache);
				pagesRepo.removePage(id);
				return id;
			} catch (IOException ioe) {
				throw new ServiceException.InternalServer(ioe);
			}
		}
		throw new ServiceException.Unauthorized();
	}

}
