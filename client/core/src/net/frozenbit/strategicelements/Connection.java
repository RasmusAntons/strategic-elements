package net.frozenbit.strategicelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection implements Closeable {
	public static final String JSON_TYPE_NAME = "name";
	public static final String JSON_ATTR_TYPE = "type";
	public static final String JSON_ATTR_NAME = "name";
	public static final String JSON_ATTR_SUCCESS = "success";

	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	private final BlockingQueue<String> outQueue;
	private final List<NetworkListener> networkListeners;
	private JsonParser parser;

	public Connection(String host, int port) {
		SocketHints hints = new SocketHints();
		this.socket = Gdx.net.newClientSocket(Net.Protocol.TCP, host, port, hints);
		outQueue = new LinkedBlockingQueue<>();
		networkListeners = new ArrayList<NetworkListener>();
		receiver = new Receiver(socket.getInputStream());
		sender = new Sender(socket.getOutputStream());
		parser = new JsonParser();
	}

	public void send(JsonObject request) {
		try {
			outQueue.put(request.toString());
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		sender.interrupt();
		receiver.interrupt();
		socket.dispose();
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
				e.printStackTrace();
			} finally {
				close();
			}
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
			try {
				//noinspection InfiniteLoopStatement
				while (true) {
					out.write(outQueue.take());
					out.flush();
				}
			} catch(InterruptedException ignore) {

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}

		void close() {
			try {
				out.close();
			} catch (IOException ignore) {

			}
		}
	}
}
