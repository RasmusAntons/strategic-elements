package net.frozenbit.strategicelements.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;
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
	public static final String JSON_ATTR_TURN = "turn";
	public static final String JSON_ATTR_MESSAGE = "msg";
	public static final String JSON_TYPE_CLOSE = "close";

	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	private JsonParser jsonParser;
	private ConnectionState state;
	private final BlockingQueue<String> outQueue;

	public Connection(Socket socket) throws IOException {
		this.socket = socket;
		state = new ConnectionState();
		jsonParser = new JsonParser();
		outQueue = new LinkedBlockingQueue<>();
		receiver = new Receiver(socket.getInputStream());
		sender = new Sender(socket.getOutputStream());
		receiver.start();
		sender.start();
	}

	public ConnectionState getState() {
		return state;
	}

	public void send(JsonObject response) {
		try {
			outQueue.put(response.toString());
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		sender.interrupt();
		receiver.interrupt();
		try {
			socket.close();
		} catch(IOException ignore) {

		}
	}

	private class Receiver extends Thread {
		private BufferedReader in;

		Receiver(InputStream inStream) {
			in = new BufferedReader(new InputStreamReader(inStream));
		}

		@Override
		public void run() {
			boolean close = false;
			JsonObject request, response;
			while (!close) {
				try {
					request = jsonParser.parse(in.readLine()).getAsJsonObject();
				} catch (Exception e) {
					System.out.println(e);
					break;
				}
				System.out.println(request);

				String type = request.has(JSON_ATTR_TYPE) ? request.get(JSON_ATTR_TYPE).getAsString() : null;
				if (type == null)
					continue;
				switch (request.get(JSON_ATTR_TYPE).getAsString()) {
					case JSON_TYPE_NAME:
						response = new NameHandler(Connection.this).handleRequest(request, state);
						break;
					case JSON_TYPE_CHALLENGE:
						response = new ChallengeHandler(Connection.this).handleRequest(request, state);
						break;
					case JSON_TYPE_BUY:
						response = new BuyHandler().handleRequest(request, state);
						break;
					case JSON_TYPE_TURN:
						response = new TurnHandler().handleRequest(request, state);
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
					Connection.this.send(response);
				}
			}
			close();
		}

		@Override
		public void interrupt() {
			close();
			super.interrupt();
		}

		void close() {
			if (state.getName() != null)
				ServerState.getInstance().removeConnection(state.getName());
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
				out.close();
			} catch (IOException ignore) {

			}
		}
	}
}
