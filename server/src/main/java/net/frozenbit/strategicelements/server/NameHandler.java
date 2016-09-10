package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

import java.util.Set;

public class NameHandler extends RequestHandler {
	private static final String JSON_ATTR_NAME = "name";
	private static final String JSON_ATTR_SUCCESS = "success";

	private Connection connection;

	public NameHandler(Connection connection) {
		this.connection = connection;
	}

	@Override
	public JsonObject handleRequest(JsonObject request, ConnectionState state) {
		String name = request.get(JSON_ATTR_NAME).getAsString();
		boolean success = ServerState.getInstance().addConnection(name, connection);
		if (success)
			state.setName(name);
		JsonObject response = new JsonObject();
		response.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_NAME);
		response.addProperty(JSON_ATTR_SUCCESS, success);
		return response;
	}
}
