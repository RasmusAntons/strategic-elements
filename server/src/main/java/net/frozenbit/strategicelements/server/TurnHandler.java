package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

public class TurnHandler extends RequestHandler {

	@Override
	JsonObject handleRequest(JsonObject request, ConnectionState state) {
		state.getEnemyConnection().send(request);
		return null;
	}
}
