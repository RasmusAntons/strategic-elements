package net.frozenbit.strategicelements;

public class Config {
	private static Config instance;

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
