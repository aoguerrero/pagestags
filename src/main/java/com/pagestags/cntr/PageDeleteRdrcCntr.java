package com.pagestags.cntr;

import static com.pagestags.Constants.PAGES_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pagestags.auth.AuthValidator;
import com.pagestags.db.PagesRepository;

import onl.andres.thinmvc.cntr.RedirectController;
import onl.andres.thinmvc.excp.ServiceException;

public class PageDeleteRdrcCntr extends RedirectController {

	public PageDeleteRdrcCntr(String path) {
		super(path);

	}

	@Override
	public String execute() {
		if(!AuthValidator.isAuthenticated(getRequest())) {
			throw new ServiceException.Unauthorized();
		}
		Matcher matcher = Pattern.compile("/pages/(.*)/delete").matcher(this.getRequest().uri());
		if (matcher.find()) {
			try {
				String id = matcher.group(1);
				Path trashDir = Paths.get(System.getProperty(PAGES_PATH), "trash").toAbsolutePath();
				if (!Files.isDirectory(trashDir)) {
					Files.createDirectory(trashDir);
				}
				Path deletePath = Paths.get(System.getProperty(PAGES_PATH), id).toAbsolutePath();
				Files.move(deletePath, Paths.get(trashDir.toString(), id).toAbsolutePath());
				PagesRepository pagesRepo = new PagesRepository();
				pagesRepo.removePage(id);
				return id;
			} catch (IOException ioe) {
				throw new ServiceException.InternalServer(ioe);
			}
		}
		throw new ServiceException.Unauthorized();
	}

}