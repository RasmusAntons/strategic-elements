package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {
	private static final String JSON_ATTR_TYPE = "type";
	private static final String JSON_TYPE_NAME = "name";
	private static final String JSON_TYPE_CLOSE = "close";

	private Socket socket;
	private JsonParser jsonParser;
	private ConnectionState state;

	public Connection(Socket socket) {
		this.socket = socket;
		state = new ConnectionState();
		jsonParser = new JsonParser();
	}

	public void run() {
		boolean close = false;
		try(
			Socket socket = this.socket;
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
		) {
			while (!close) {
				JsonObject request = jsonParser.parse(in.readLine()).getAsJsonObject();
				System.out.println(request);
				JsonObject response;
				switch(request.get(JSON_ATTR_TYPE).getAsString()) {
					case JSON_TYPE_NAME:
						response = new NameHandler().handleRequest(request, state);
						break;
					case JSON_TYPE_CLOSE:
						response = null;
						close = true;
						break;
					default:
						response = null;
				}
				System.out.println(response);
				if (response != null) {
					out.write(response.toString());
					out.flush();
				}
				ServerState.getInstance().removeName(state.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
