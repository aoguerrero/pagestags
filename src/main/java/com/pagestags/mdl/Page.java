package com.pagestags.mdl;

import java.util.List;
import java.util.Objects;

public class Page implements Comparable<Page> {

	private final String id;
	private final String title;
	private final List<String> tags;
	private final boolean pblic;

	public Page(String id, String title, List<String> tags, boolean pblic) {
		this.id = id;
		this.title = title;
		this.tags = tags;
		this.pblic = pblic;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getTags() {
		return tags;
	}

	public boolean isPblic() {
		return pblic;
	}

	@Override
	public int compareTo(Page page) {
		return this.getTitle().toLowerCase().compareTo(page.getTitle().toLowerCase());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || (getClass() != obj.getClass()))
			return false;
		Page other = (Page) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
