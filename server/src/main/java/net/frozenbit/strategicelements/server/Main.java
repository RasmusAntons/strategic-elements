package net.frozenbit.strategicelements.server;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try (Server server = new Server(5123)) {
			server.run();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
