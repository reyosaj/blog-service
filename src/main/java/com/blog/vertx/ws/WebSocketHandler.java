/**
 * 
 */
package com.blog.vertx.ws;

import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author reyos
 *
 */
public class WebSocketHandler implements Handler<ServerWebSocket> {

	// Maintains a list of all current websocket connections to the server
	private static List<ServerWebSocket> allConnectedSockets = new ArrayList<>();

	@Override
	public void handle(ServerWebSocket serverWebSocket) {
		System.out.println("WebSocketHandler.handle() New Connection: "
				+ serverWebSocket.remoteAddress() + ", path: " + serverWebSocket.path());

		// Store new connection in list
		allConnectedSockets.add(serverWebSocket);
		// Setup handler to receive the data
		serverWebSocket.handler(handler -> {

			final JsonObject inJson = new JsonObject(new String(handler
					.getBytes()));
			// data coming as string. convert to json
				final JsonObject data = new JsonObject(inJson.getString("data"));
				final JsonObject newJson = new JsonObject();
				newJson.put("event", inJson.getString("event"));
				newJson.put("data", data);
				String message = newJson.toString();
				System.out.println("WebSocketHandler message: " + message);

				// Now broadcast received message to all other clients
				for (ServerWebSocket sock : allConnectedSockets) {
					sock.writeFinalTextFrame(message);
				}
			});
		// Register handler to remove connection from list when connection is
		// closed
		serverWebSocket.closeHandler(handler -> {
			System.out.println("WebSocketHandler.handle() connection close ");
			allConnectedSockets.remove(serverWebSocket);
		});
	}

}
