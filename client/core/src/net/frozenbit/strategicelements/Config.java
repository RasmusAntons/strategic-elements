package net.frozenbit.strategicelements;

public class Config {
	private static Config instance;
	private String name;

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
