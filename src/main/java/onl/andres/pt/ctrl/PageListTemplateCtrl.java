package onl.andres.pt.ctrl;

import io.netty.handler.codec.http.HttpRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import onl.andres.mvcly.ctrl.TemplateController;
import onl.andres.pt.core.AppParameters;
import onl.andres.pt.core.PagesTagsCtx;
import onl.andres.pt.vm.TemplateUtil;
import onl.andres.pt.auth.AuthValidator;
import onl.andres.pt.mdl.Page;

public class PageListTemplateCtrl extends TemplateController {

    private final PagesTagsCtx ctx;

    public PageListTemplateCtrl(String path, PagesTagsCtx ctx) {
        super(path, ctx);
        this.ctx = ctx;
    }

    public Map<String, Object> getContext(HttpRequest request) {
        List<Page> pages;
        boolean auth = AuthValidator.isAuthenticated(request);

        List<String> selectedTags;
        Matcher matcher = Pattern.compile("/pages/list/(.+)").matcher(request.uri());

        boolean filtered = matcher.find();
        if (filtered) {
            selectedTags = Arrays.asList(matcher.group(1).split(Pattern.quote("$")));
            pages = ctx.getPages(selectedTags).stream().filter(p -> p.pblic() || auth)
                    .toList();
        } else {
            selectedTags = Collections.emptyList();
            pages = ctx.getPages().stream().filter(p -> p.pblic() || auth).toList();
        }

        List<String> allTags =
                pages.stream()
                        .flatMap(p -> p.tags().stream())
                        .distinct()
                        .filter(t -> !selectedTags.contains(t))
                        .sorted()
                        .toList();

        if (!filtered && !auth) {
            pages = Collections.emptyList();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("page_title", AppParameters.WEBSITE.get());
        data.put("auth", auth);
        data.put("selected_tags_str", String.join("$", selectedTags));
        data.put("selected_tags", selectedTags);
        data.put("tags", allTags);
        List<Page> sortedPages = new ArrayList<>(pages);
        sortedPages.sort(
                (Page p1, Page p2) -> p1.title().toLowerCase().compareTo(p2.title().toLowerCase()));
        data.put("items", sortedPages);
        data.put("template_util", TemplateUtil.class);
        data.put("filtered", filtered);

        return data;
    }
}
