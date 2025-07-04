package onl.andres.pt.cntr;

import static onl.andres.pt.AppParameters.PAGES_PATH;

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

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.cntr.TemplateController;
import onl.andres.mvcly.utl.FileSystemUtils;
import onl.andres.pt.AppParameters;
import onl.andres.pt.auth.AuthValidator;
import onl.andres.pt.db.PagesCache;
import onl.andres.pt.db.PagesRepository;

public class HomeTmplCntr extends TemplateController {

	private PagesCache pagesCache;

	private HtmlRenderer renderer;
	private Parser parser;

	public HomeTmplCntr(String path, PagesCache pagesCache) {
		super(path);
		Set<Extension> extensions = Set.of(AutolinkExtension.create(), TablesExtension.create(),
				ImageAttributesExtension.create());
		this.parser = Parser.builder().extensions(extensions).build();
		this.renderer = HtmlRenderer.builder().build();

		this.pagesCache = pagesCache;
	}

	public Map<String, Object> getContext(HttpRequest request) {
		Path path = Paths.get(PAGES_PATH.get(), "home").toAbsolutePath();
		String[] lines = new String(FileSystemUtils.getFileContent(path), StandardCharsets.UTF_8).split("\\R");
		StringBuilder content = new StringBuilder();
		for (int i = 3; i < lines.length; i++) {
			content.append(lines[i]).append("\n");
		}
		boolean auth = AuthValidator.isAuthenticated(request);
		PagesRepository pagesRepo = new PagesRepository(pagesCache);
		List<String> allTags = new ArrayList<>(pagesRepo.getTags(auth));

		Map<String, Object> data = new HashMap<>();
		data.put("page_title", AppParameters.WEBSITE.get());
		data.put("auth", auth);
		data.put("title", lines[0]);
		data.put("content", renderer.render(parser.parse(content.toString())));
		data.put("tags", allTags);
		return data;
	}

}
