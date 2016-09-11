package net.frozenbit.strategicelements.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ServerState {
	private static ServerState instance;

	private final HashMap<String, Connection> connections;

	private ServerState() {
		connections = new HashMap<>();
	}

	public static ServerState getInstance() {
		if (instance == null)
			instance = new ServerState();
		return instance;
	}

	public boolean addConnection(String name, Connection connection) {
		synchronized (connections) {
			if (connections.containsKey(name)) {
				return false;
			} else {
				connections.put(name, connection);
				return true;
			}
		}
	}

	public boolean removeConnection(String name) {
		synchronized (connections) {
			return connections.remove(name) == null;
		}
	}

	public Connection getConnection(String name) {
		synchronized (connections) {
			return connections.get(name);
		}
	}

	public Connection getReadyPlayer() {
		synchronized (connections) {
			for (Connection connection : connections.values()) {
				if (connection.getState().getPhase() == ConnectionState.GamePhase.READY) {
					return connection;
				}
			}
		}
		return null;
	}

	public int playerCount() {
		synchronized (connections) {
			return connections.size();
		}
	}
}
