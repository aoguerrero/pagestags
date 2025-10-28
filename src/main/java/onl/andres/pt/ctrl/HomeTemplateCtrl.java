package onl.andres.pt.ctrl;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.ctrl.TemplateController;
import onl.andres.mvcly.utl.FileSystemUtils;
import onl.andres.pt.core.AppParameters;
import onl.andres.pt.core.PagesTagsCtx;
import onl.andres.pt.auth.AuthValidator;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static onl.andres.pt.core.AppParameters.PAGES_PATH;

public class HomeTemplateCtrl extends TemplateController {

	private final HtmlRenderer renderer;
	private final Parser parser;
    private final PagesTagsCtx ctx;

	public HomeTemplateCtrl(String path, PagesTagsCtx ctx) {
		super(path, ctx);
		Set<Extension> extensions = Set.of(AutolinkExtension.create(), TablesExtension.create(),
				ImageAttributesExtension.create());
		this.parser = Parser.builder().extensions(extensions).build();
		this.renderer = HtmlRenderer.builder().build();
        this.ctx = ctx;
	}

	public Map<String, Object> getContext(HttpRequest request) {
		Path path = Paths.get(PAGES_PATH.get(), "home").toAbsolutePath();
		String[] lines = new String(FileSystemUtils.getFileContent(path), StandardCharsets.UTF_8).split("\\R");
		StringBuilder content = new StringBuilder();
		for (int i = 3; i < lines.length; i++) {
			content.append(lines[i]).append("\n");
		}
		boolean auth = AuthValidator.isAuthenticated(request);
		List<String> allTags = new ArrayList<>(ctx.getTags(auth));

		Map<String, Object> data = new HashMap<>();
		data.put("page_title", AppParameters.WEBSITE.get());
		data.put("auth", auth);
		data.put("title", lines[0]);
		data.put("content", renderer.render(parser.parse(content.toString())));
		data.put("tags", allTags);
		return data;
	}

}
