package net.frozenbit.strategicelements.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Closeable {
	private static final int SO_TIMEOUT = 10000;

	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(new InetSocketAddress(port));
		serverSocket.setSoTimeout(SO_TIMEOUT);

		Runtime.getRuntime().addShutdownHook(
			new Thread("server-shutdown-hook") {
				@Override
				public void run() {
					close();
				}
			});
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

	public void close() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
