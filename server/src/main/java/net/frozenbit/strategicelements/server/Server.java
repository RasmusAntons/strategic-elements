package net.frozenbit.strategicelements.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Closeable {
	private static final int SO_TIMEOUT = 10000;

	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(SO_TIMEOUT);
	}

	public void run() {
		Socket socket;
		while (true) {
			try {
				socket = serverSocket.accept();
				new Connection(socket).start();
			} catch (SocketTimeoutException ignore) {

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() throws IOException {
		serverSocket.close();
	}
}
