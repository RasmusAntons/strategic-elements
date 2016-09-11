package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

public class BuyHandler extends RequestHandler {

	@Override
	JsonObject handleRequest(JsonObject request, ConnectionState state) {
		state.getEnemyConnection().send(request);
		state.setPhase(ConnectionState.GamePhase.PLAY);

		if (state.getEnemyConnection().getState().getPhase() == ConnectionState.GamePhase.PLAY) {
			JsonObject enemyPlayResponse = new JsonObject();
			enemyPlayResponse.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_PLAY);
			enemyPlayResponse.addProperty(Connection.JSON_ATTR_SUCCESS, true);
			state.getEnemyConnection().send(enemyPlayResponse);
			JsonObject response = new JsonObject();
			response.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_PLAY);
			response.addProperty(Connection.JSON_ATTR_SUCCESS, false);
			return response;
		}

		return null;
	}
}
