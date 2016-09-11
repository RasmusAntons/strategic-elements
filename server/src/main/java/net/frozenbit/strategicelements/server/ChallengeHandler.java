package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

class ChallengeHandler extends RequestHandler {

	private Connection connection;

	public ChallengeHandler(Connection connection) {
		this.connection = connection;
	}


	@Override
	JsonObject handleRequest(JsonObject request, ConnectionState state) {
		String name = request.get(Connection.JSON_ATTR_NAME).isJsonNull()
			? null
			: request.get(Connection.JSON_ATTR_NAME).getAsString();
		boolean random = name == null;
		Connection enemyConnection = random
			? ServerState.getInstance().getWaitingPlayer()
			: ServerState.getInstance().getConnection(name);
		if (enemyConnection == null) {
			state.setPhase(ConnectionState.GamePhase.WAITING);
			ServerState.printInfo();
			return null;
		}
		if (enemyConnection.getState().getPhase() != ConnectionState.GamePhase.WAITING) {
			state.setPhase(ConnectionState.GamePhase.WAITING);
			return null;
		}

		state.setPhase(ConnectionState.GamePhase.BUY);
		state.setEnemyConnection(enemyConnection);
		enemyConnection.getState().setPhase(ConnectionState.GamePhase.BUY);
		enemyConnection.getState().setEnemyConnection(connection);

		JsonObject enemyResponse = new JsonObject();
		enemyResponse.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_CHALLENGE);
		enemyResponse.addProperty(Connection.JSON_TYPE_NAME, state.getName());
		enemyConnection.send(enemyResponse);

		JsonObject response = new JsonObject();
		response.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_CHALLENGE);
		response.addProperty(Connection.JSON_TYPE_NAME, enemyConnection.getState().getName());
		return response;
	}
}
