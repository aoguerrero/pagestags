package com.pagestags.cntr;

import static com.pagestags.Constants.PAGES_PATH;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pagestags.auth.AuthValidator;
import com.pagestags.db.PagesRepository;
import com.pagestags.mdl.Page;

import onl.andres.thinmvc.cntr.FormController;
import onl.andres.thinmvc.excp.ServiceException;
import onl.andres.thinmvc.utl.FileSystemUtils;

public class PageSaveFrmCntr extends FormController {

	public PageSaveFrmCntr(String path) {
		super(path);
	}

	@Override
	public String execute() {
		if (!AuthValidator.isAuthenticated(getRequest())) {
			throw new ServiceException.Unauthorized();
		}
		String title = getFormData().get("title");

		Matcher matcher = Pattern.compile("/pages/(.*)/save").matcher(this.getRequest().uri());
		String id = getId(title);
		if (matcher.find()) {
			id = matcher.group(1);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(title).append("\n");
		String tags = getFormData().get("tags");
		tags = tags.replaceAll(" +", " ").trim();
		sb.append(tags).append("\n");
		String pblic = getFormData().get("public");
		if (pblic != null) {
			sb.append("public").append("\n");
		} else {
			sb.append("private").append("\n");
		}
		sb.append(getFormData().get("content"));

		Path savePath = Paths.get(System.getProperty(PAGES_PATH), id).toAbsolutePath();
		FileSystemUtils.writeStringToFile(savePath, sb.toString());

		PagesRepository pagesRepo = new PagesRepository();
		pagesRepo.removePage(id);
		pagesRepo.putPage(new Page(id, title, Arrays.asList(tags.split(" ")), pblic != null));
		return id;
	}
	
	private String getId(String title) {
		String id = Normalizer.normalize(title, Normalizer.Form.NFD).toLowerCase().replaceAll("\\W+", "_");
		boolean valid = false;
		int counter = 0;
		while(!valid) {
			Path savePath = Paths.get(System.getProperty(PAGES_PATH), id).toAbsolutePath();
			if(Files.exists(savePath)) {
				id = id + "-"+(++counter);
			} else {
				valid = true;
			}
		}
		return id;
	}
}