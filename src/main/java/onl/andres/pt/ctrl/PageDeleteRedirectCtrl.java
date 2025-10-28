package onl.andres.pt.ctrl;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.ctrl.RedirectController;
import onl.andres.mvcly.excp.ServiceException;
import onl.andres.pt.core.PagesTagsCtx;
import onl.andres.pt.auth.AuthValidator;
import onl.andres.pt.mdl.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static onl.andres.pt.core.AppParameters.PAGES_PATH;

public class PageDeleteRedirectCtrl extends RedirectController {

    private final PagesTagsCtx ctx;

	public PageDeleteRedirectCtrl(String redirectPath, PagesTagsCtx ctx) {
		super(redirectPath);
		this.ctx = ctx;
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
                ctx.getPages().remove(new Page(id));
				return id;
			} catch (IOException ioe) {
				throw new ServiceException.InternalServer(ioe);
			}
		}
		throw new ServiceException.Unauthorized();
	}

}
