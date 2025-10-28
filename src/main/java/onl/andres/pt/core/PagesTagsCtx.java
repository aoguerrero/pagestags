package onl.andres.pt.core;

import onl.andres.mvcly.core.AppCtx;
import onl.andres.pt.mdl.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PagesTagsCtx extends AppCtx {

    private List<Page> pages;

    public PagesTagsCtx() {
        super();
        this.pages = Collections.synchronizedList(new ArrayList<Page>(PagesScanner.scanPages()));
    }

    public List<Page> getPages() {
        return this.pages;
    }

    public List<Page> getPages(List<String> tags) {
        return this.pages.stream().filter(p -> p.tags().containsAll(tags)).toList();
    }

    public List<String> getTags(boolean auth) {
        return this.pages.stream().filter(p -> p.pblic() || auth).flatMap(p -> p.tags().stream()).distinct()
                .sorted().toList();
    }
}
