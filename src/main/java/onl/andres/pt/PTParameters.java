package onl.andres.pt;

public enum PTParameters {

	PAGES_PATH("pages_path", "pages"), SESSION_ID("session_id", null), PASSWORD("password", "secret");

	private final String name;
	private final String defaultValue;

	private PTParameters(String name, String defaultValue) {
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
