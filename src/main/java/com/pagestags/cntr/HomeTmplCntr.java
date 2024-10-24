package com.pagestags.cntr;

import static com.pagestags.Constants.PAGES_PATH;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pagestags.auth.AuthValidator;
import com.pagestags.db.PagesRepository;
import com.pagestags.thinmvc.cntr.TemplateController;
import com.pagestags.thinmvc.utl.FileSystemUtils;

public class HomeTmplCntr extends TemplateController {

	public HomeTmplCntr(String path) {
		super(path);
	}

	public Map<String, Object> getContext() {
		Path path = Paths.get(System.getProperty(PAGES_PATH), "home").toAbsolutePath();
		String[] lines = new String(FileSystemUtils.getFileContent(path), StandardCharsets.UTF_8).split("\\R");
		StringBuilder content = new StringBuilder();
		if (lines.length > 3) {
			for (int i = 3; i < lines.length; i++) {
				content.append(lines[i]).append("\n");
			}
		}
		boolean auth = AuthValidator.isAuthenticated(request);
		PagesRepository pagesRepo = new PagesRepository();
		List<String> allTags = new ArrayList<>(pagesRepo.getTags(auth));

		Map<String, Object> data = new HashMap<>();
		String contentStr = content.toString();
		data.put("page_title", "Main Page");
		data.put("auth", auth);
		data.put("title", lines[0]);
		data.put("content", contentStr);
		data.put("tags", allTags);
		return data;
	}

}
