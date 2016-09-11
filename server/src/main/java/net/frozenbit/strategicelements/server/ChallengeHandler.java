package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

class ChallengeHandler extends RequestHandler {

	@Override
	JsonObject handleRequest(JsonObject request, ConnectionState state) {
		String name = request.get(Connection.JSON_ATTR_NAME).getAsString();
		return null;
	}
}
