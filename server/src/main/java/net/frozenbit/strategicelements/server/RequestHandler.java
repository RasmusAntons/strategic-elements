package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;

public abstract class RequestHandler {
	abstract JsonObject handleRequest(JsonObject request, ConnectionState state);
}
