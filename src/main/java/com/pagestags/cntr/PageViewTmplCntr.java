package com.pagestags.cntr;

import static com.pagestags.Constants.PAGES_PATH;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pagestags.auth.AuthValidator;
import com.pagestags.thinmvc.cntr.TemplateController;
import com.pagestags.thinmvc.excp.ServiceException;
import com.pagestags.thinmvc.utl.FileSystemUtils;

public class PageViewTmplCntr extends TemplateController {

	public PageViewTmplCntr(String path) {
		super(path);
	}

	@Override
	public Map<String, Object> getContext() {
		Map<String, Object> data = new HashMap<>();
		Matcher matcher = Pattern.compile("/pages/(.*)/(view|edit)").matcher(request.uri());
		boolean auth = AuthValidator.isAuthenticated(request);
		if (matcher.find()) {
			String id = matcher.group(1);
			String action = matcher.group(2);
			Path path = Paths.get(System.getProperty(PAGES_PATH), id).toAbsolutePath();
			String[] lines = (new String(FileSystemUtils.getFileContent(path), StandardCharsets.UTF_8)).split("\\R");
			boolean pblic = lines[2].equals("public");
			if (!pblic || action.equals("edit")) {
				if (!auth) {
					throw new ServiceException.Unauthorized();
				}
			}
			StringBuilder content = new StringBuilder();
			if (lines.length > 3) {
				for (int i = 3; i < lines.length; i++) {
					content.append(lines[i]).append("\n");
				}
			}

			data.put("page_title", action.equals("view") ? "View Page" : "Edit Page");
			data.put("content", content.toString());

			data.put("id", id);
			data.put("title", lines[0]);
			data.put("tags", lines[1]);
			data.put("public", pblic);
			data.put("auth", AuthValidator.isAuthenticated(request));
		}
		return data;
	}

}
