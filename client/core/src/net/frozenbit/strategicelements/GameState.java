package net.frozenbit.strategicelements;

public class GameState {
	private GamePhase phase;
	private String name;
	private boolean loggedIn;
	private String enemyName;
	private boolean turn;

	public GameState() {
		phase = GamePhase.INIT;
		name = null;
		loggedIn = false;
		enemyName = null;
		turn = false;
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

	public synchronized String getEnemyName() {
		return enemyName;
	}

	public synchronized void setEnemyName(String enemyName) {
		this.enemyName = enemyName;
	}

	public synchronized boolean isTurn() {
		return turn;
	}

	public synchronized void setTurn(boolean turn) {
		this.turn = turn;
	}

	public enum GamePhase {
		INIT, READY, WAITING, BUY, PLAY, OVER
	}
}
