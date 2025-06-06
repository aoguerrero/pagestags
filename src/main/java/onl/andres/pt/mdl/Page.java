package onl.andres.pt.mdl;

import java.util.List;

public record Page(String id, String title, List<String> tags, boolean pblic) {

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Page page) {
			return id.equals(page.id);
		}
		return false;
	}
}
