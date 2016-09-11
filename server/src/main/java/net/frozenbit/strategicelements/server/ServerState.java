package net.frozenbit.strategicelements.server;

import java.util.HashMap;

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

	public Connection getWaitingPlayer() {
		synchronized (connections) {
			for (Connection connection : connections.values()) {
				if (connection.getState().getPhase() == ConnectionState.GamePhase.WAITING) {
					return connection;
				}
			}
		}
		return null;
	}

	public static void printInfo() {
		System.out.print(String.format("%d players online: ", getInstance().playerCount()));
		for (Connection connection : getInstance().connections.values()) {
			System.out.print(String.format("%s (%s) ", connection.getState().getName(), connection.getState().getPhase().toString()));
		}
		System.out.println("");
	}

	public int playerCount() {
		synchronized (connections) {
			return connections.size();
		}
	}
}
