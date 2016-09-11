package net.frozenbit.strategicelements;

public class GameState {
	private GamePhase phase;
	private String name;
	private boolean loggedIn;

	public GameState() {
		phase = GamePhase.INIT;
		name = null;
		loggedIn = false;
	}

	public synchronized GamePhase getPhase() {
		return phase;
	}

	public synchronized void setPhase(GamePhase phase) {
		this.phase = phase;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized boolean isLoggedIn() {
		return loggedIn;
	}

	public synchronized void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public enum GamePhase {
		INIT, BUY, PLAY, OVER
	}
}
