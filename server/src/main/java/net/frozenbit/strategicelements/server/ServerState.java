package net.frozenbit.strategicelements.server;

import java.util.HashSet;
import java.util.Set;

public class ServerState {
	private static ServerState instance;

	private Set<String> names;

	private ServerState() {
		names = new HashSet<>();
	}

	public static ServerState getInstance() {
		if (instance == null)
			instance = new ServerState();
		return instance;
	}

	public boolean addName(String name) {
		return names.add(name);
	}

	public boolean removeName(String name) {
		return names.remove(name);
	}

	public int playerCount() {
		return names.size();
	}
}
