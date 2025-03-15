package onl.andres.pt.mdl;

import java.util.List;

public record Page(String id, String title, List<String> tags, boolean pblic) {}
