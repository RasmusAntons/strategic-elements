package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

import java.util.Set;

public class NameHandler extends RequestHandler {
	private static final String JSON_ATTR_NAME = "name";
	private static final String JSON_ATTR_SUCCESS = "success";

	@Override
	public JsonObject handleRequest(JsonObject request, ConnectionState state) {
		JsonObject response = new JsonObject();
		boolean success = ServerState.getInstance().addName(request.get(JSON_ATTR_NAME).getAsString());
		response.addProperty(JSON_ATTR_SUCCESS, success);
		return response;
	}
}
