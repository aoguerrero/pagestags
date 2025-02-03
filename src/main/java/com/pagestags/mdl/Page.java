package com.pagestags.mdl;

import java.util.List;

public record Page(String id, String title, List<String> tags, boolean pblic) implements Comparable<Page> {

	@Override
	public int compareTo(Page o) {
		if (o == null)
			return -1;
		return o.id.compareTo(this.id);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		return ((Page) o).id.equals(this.id);
	}
}
