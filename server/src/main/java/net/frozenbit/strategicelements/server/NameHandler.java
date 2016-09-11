package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

import java.util.Set;

class NameHandler extends RequestHandler {

	private Connection connection;

	public NameHandler(Connection connection) {
		this.connection = connection;
	}

	@Override
	public JsonObject handleRequest(JsonObject request, ConnectionState state) {
		String name = request.get(Connection.JSON_ATTR_NAME).getAsString();
		boolean success = ServerState.getInstance().addConnection(name, connection);
		if (success)
			state.setName(name);
		JsonObject response = new JsonObject();
		response.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_NAME);
		response.addProperty(Connection.JSON_ATTR_SUCCESS, success);
		return response;
	}
}
