package onl.andres.pt;

public enum AppParameters {

	PAGES_PATH("pages_path", "pages"), SESSION_ID("session_id", null), USERNAME("username", "editor"),
	PASSWORD("password", "editor"), WEBSITE("website", "");

	private final String name;
	private final String defaultValue;

	private AppParameters(String name, String defaultValue) {
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
