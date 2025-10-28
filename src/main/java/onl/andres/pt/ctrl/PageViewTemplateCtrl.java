package onl.andres.pt.ctrl;

import static onl.andres.pt.core.AppParameters.PAGES_PATH;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import onl.andres.mvcly.core.AppCtx;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import io.netty.handler.codec.http.HttpRequest;
import onl.andres.mvcly.ctrl.TemplateController;
import onl.andres.mvcly.excp.ServiceException;
import onl.andres.mvcly.utl.FileSystemUtils;
import onl.andres.pt.auth.AuthValidator;

public class PageViewTemplateCtrl extends TemplateController {

	private final HtmlRenderer renderer;
	private final Parser parser;

	public PageViewTemplateCtrl(String path, AppCtx ctx) {
		super(path, ctx);
		Set<Extension> extensions = Set.of(AutolinkExtension.create(), TablesExtension.create(),
				ImageAttributesExtension.create());
		this.parser = Parser.builder().extensions(extensions).build();
		this.renderer = HtmlRenderer.builder().extensions(extensions).build();
	}

	@Override
	public Map<String, Object> getContext(HttpRequest request) {
		Map<String, Object> data = new HashMap<>();
		Matcher matcher = Pattern.compile("/pages/(.*)/(view|edit)").matcher(request.uri());
		boolean auth = AuthValidator.isAuthenticated(request);
		if (matcher.find()) {
			String id = matcher.group(1);
			String action = matcher.group(2);
			Path path = Paths.get(PAGES_PATH.get(), id).toAbsolutePath();
			String[] lines = (new String(FileSystemUtils.getFileContent(path), StandardCharsets.UTF_8)).split("\\R");
			boolean pblic = lines[2].equals("public");
			boolean edit = action.equals("edit");

			if ((!pblic || edit) && !auth) {
				throw new ServiceException.Unauthorized();
			}
			StringBuilder content = new StringBuilder();
			if (lines.length > 3) {
				for (int i = 3; i < lines.length; i++) {
					content.append(lines[i]).append("\n");
				}
			}
			String contentStr;
			if (!edit) {
				contentStr = renderer.render(parser.parse(content.toString()));
			} else {
				contentStr = content.toString();
			}
			data.put("page_title", lines[0]);
			data.put("content", contentStr);
			data.put("id", id);
			data.put("title", lines[0]);
			data.put("tags", lines[1]);
			data.put("tags_url", lines[1].trim().replace(" ", "$"));
			data.put("public", pblic);
			data.put("auth", AuthValidator.isAuthenticated(request));
			data.put("identifier", id);
		}
		return data;
	}

}
