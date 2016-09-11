package net.frozenbit.strategicelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection implements Closeable {
	public static final String JSON_ATTR_TYPE = "type";
	public static final String JSON_TYPE_NAME = "name";
	public static final String JSON_TYPE_CHALLENGE = "challenge";
	public static final String JSON_TYPE_BUY = "buy";
	public static final String JSON_TYPE_TURN = "turn";
	public static final String JSON_TYPE_PLAY = "play";
	public static final String JSON_ATTR_NAME = "name";
	public static final String JSON_ATTR_SUCCESS = "success";
	public static final String JSON_ATTR_ENTITIES = "entities";
	public static final String JSON_ATTR_MESSAGE = "msg";
	public static final String JSON_TYPE_CLOSE = "close";

	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	private boolean online;
	private final BlockingQueue<String> outQueue;
	private final List<NetworkListener> networkListeners;
	private JsonParser parser;

	public Connection(String host, int port) {
		outQueue = new LinkedBlockingQueue<>();
		networkListeners = new ArrayList<>();
		parser = new JsonParser();
		SocketHints hints = new SocketHints();
		try {
			this.socket = Gdx.net.newClientSocket(Net.Protocol.TCP, host, port, hints);
			receiver = new Receiver(socket.getInputStream());
			sender = new Sender(socket.getOutputStream());
			receiver.start();
			sender.start();
			online = true;
		} catch (GdxRuntimeException e) {
			online = false;
		}
	}

	public void send(JsonObject request) {
		try {
			outQueue.put(request.toString());
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void registerListener(NetworkListener listener) {
		synchronized (networkListeners) {
			networkListeners.add(listener);
		}
	}

	public void removeListener(NetworkListener listener) {
		synchronized (networkListeners) {
			networkListeners.remove(listener);
		}
	}

	public void close() {
		if (sender != null)
			sender.interrupt();
		if (receiver != null)
			receiver.interrupt();
		if (socket !=null)
			socket.dispose();
	}

	public boolean isOnline() {
		return online;
	}

	private class Receiver extends Thread {
		private BufferedReader in;

		Receiver(InputStream inStream) {
			in = new BufferedReader(new InputStreamReader(inStream));
		}

		@Override
		public void run() {
			try {
				while (true) {
					JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
					synchronized (networkListeners) {
						for (NetworkListener listener : networkListeners) {
							listener.onDataReceived(response);
						}
					}
				}
			} catch (IOException e) {
				if (!isInterrupted())
					e.printStackTrace();
			} finally {
				close();
			}
		}

		@Override
		public void interrupt() {
			close();
			super.interrupt();
		}

		void close() {
			try {
				in.close();
			} catch (IOException ignore) {

			}
		}
	}

	private class Sender extends Thread {
		private BufferedWriter out;

		Sender(OutputStream outStream) {
			out = new BufferedWriter(new OutputStreamWriter(outStream));
		}

		@Override
		public void run() {
			//noinspection Duplicates
			try {
				//noinspection InfiniteLoopStatement
				while (true) {
					out.write(outQueue.take() + "\n");
					out.flush();
				}
			} catch(InterruptedException ignore) {

			} catch (IOException e) {
				if (!isInterrupted())
					e.printStackTrace();
			} finally {
				close();
			}
		}

		@Override
		public void interrupt() {
			close();
			super.interrupt();
		}

		void close() {
			try {
				JsonObject close = new JsonObject();
				close.addProperty(JSON_ATTR_TYPE, JSON_TYPE_CLOSE);
				out.write(close.toString());
				out.flush();
				out.close();
			} catch (IOException ignore) {

			}
		}
	}
}
