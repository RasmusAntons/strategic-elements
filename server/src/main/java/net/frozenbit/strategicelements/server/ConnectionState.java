package net.frozenbit.strategicelements.server;

public class ConnectionState {
	private GamePhase phase;
	private String name;
	private Connection enemyConnection;

	public ConnectionState() {
		phase = GamePhase.INIT;
		name = null;
	}

	public synchronized GamePhase getPhase() {
		return phase;
	}

	public synchronized void setPhase(GamePhase phase) {
		this.phase = phase;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Connection getEnemyConnection() {
		return enemyConnection;
	}

	public void setEnemyConnection(Connection enemyConnection) {
		this.enemyConnection = enemyConnection;
	}

	public enum GamePhase {
		INIT, READY, WAITING, BUY, PLAY, OVER
	}
}
