package com.pagestags;

public enum PagestagsParameters {

	PAGES_PATH("pages_path", "pages"), SESSION_ID("session_id", null), PASSWORD("password", "secret");

	private final String name;
	private final String defaultValue;

	private PagestagsParameters(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}
	
	public String get() {
		return System.getProperty(name, defaultValue);
	}
}
