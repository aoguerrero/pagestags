package com.pagestags.cntr;

import static com.pagestags.PagestagsParameters.PAGES_PATH;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.pagestags.auth.AuthValidator;
import com.pagestags.db.PagesRepository;
import com.pagestags.thinmvc.cntr.TemplateController;
import com.pagestags.thinmvc.utl.FileSystemUtils;

public class HomeTmplCntr extends TemplateController {

	private HtmlRenderer renderer;
	private Parser parser;

	public HomeTmplCntr(String path) {
		super(path);
		Set<Extension> EXTENSIONS = Set.of(AutolinkExtension.create(), TablesExtension.create(),
				ImageAttributesExtension.create());
		this.parser = Parser.builder().extensions(EXTENSIONS).build();
		this.renderer = HtmlRenderer.builder().build();
	}

	public Map<String, Object> getContext() {
		Path path = Paths.get(PAGES_PATH.get(), "home").toAbsolutePath();
		String[] lines = new String(FileSystemUtils.getFileContent(path), StandardCharsets.UTF_8).split("\\R");
		StringBuilder content = new StringBuilder();
		for (int i = 3; i < lines.length; i++) {
			content.append(lines[i]).append("\n");
		}
		boolean auth = AuthValidator.isAuthenticated(request);
		PagesRepository pagesRepo = new PagesRepository();
		List<String> allTags = new ArrayList<>(pagesRepo.getTags(auth));

		Map<String, Object> data = new HashMap<>();
		data.put("page_title", "Main Page");
		data.put("auth", auth);
		data.put("title", lines[0]);
		data.put("content", renderer.render(parser.parse(content.toString())));
		data.put("tags", allTags);
		return data;
	}

}
