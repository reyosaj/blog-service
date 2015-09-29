/**
 * 
 */
package com.blog.vertx.routes;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.query.Query;

import com.blog.Constants;
import com.blog.dto.UserDTO;
import com.blog.mongodb.dao.UserDAO;
import com.blog.mongodb.dao.UserDAOImpl;
import com.blog.mongodb.model.User;
import com.blog.rest.wrapper.UserWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author reyos
 * 
 *         Handler for all User api's
 *
 */
public class UserHandler {

	private final Router router;
	private final UserDAO dao;

	/**
	 * @param vertx
	 * 
	 *            Configure all Routes
	 */
	public UserHandler(Vertx vertx) {
		this.router = Router.router(vertx); 
		this.router.get().handler(this::getAllUsers);
		this.router.post("/auth").handler(this::validateUser);
		this.router.post().handler(this::createUser);
		this.dao = new UserDAOImpl();
	}

	/**
	 * @return
	 */
	public Router getRouter() {
		return this.router;
	}

	/**
	 * List All Users
	 * 
	 * @param routingContext
	 */
	private void getAllUsers(RoutingContext routingContext) {

		HttpServerResponse response = routingContext.response();

		String signedIn = routingContext.request().getParam("signedIn");

		response.putHeader("content-type", "application/json");

		@SuppressWarnings("unused")
		Query<User> query;
		final List<UserDTO> users;
		try {
			if ("true".equals(signedIn)) {
				List<User> allUsers = dao.getSignedInUsers();
				users = UserWrapper.toDTOList(getActiveUsers(allUsers));
			} else {
				List<User> allUsers = dao.getAllUsers();
				users = UserWrapper.toDTOList(allUsers);
			}
		} catch (Exception e) {
			System.out
					.println("UserHandler.getAllUsers() Exception getting users");
			response.setStatusCode(500).end("Error getting users");
			return;
		}
		System.out.println("UserHandler.getUsers() signedIn=" + signedIn
				+ " size=" + users.size());
		final JsonArray array = new JsonArray(users);

		response.setStatusCode(200).end(array.toString());
	}

	/**
	 * Create and Persist new User
	 * 
	 * @param routingContext
	 */
	private void createUser(RoutingContext routingContext) {
		HttpServerRequest req = routingContext.request();
		req.bodyHandler(buf -> {

			String json = buf.toString("UTF-8");

			System.out.println("UserHandler.handleCreateUser() request Body : "
					+ buf);

			ObjectMapper mapper = new ObjectMapper();
			UserDTO dto = null;
			HttpServerResponse response = routingContext.response();
			try {
				dto = mapper.readValue(json, UserDTO.class);
				User u = UserWrapper.toModel(dto);
				u.setSignedIn(false);
				u.setLastAccessed(new Date());
				dao.createUser(u);
				response.setStatusCode(201).end("Created");

			} catch (Exception e) {
				System.err.println("Could not process request");
				response.setStatusCode(400).end("Bad Request");
			}

		});
	}

	/**
	 * Validate login userName and password
	 * 
	 * @param routingContext
	 */
	private void validateUser(RoutingContext routingContext) {
		HttpServerRequest req = routingContext.request();
		req.bodyHandler(buf -> {
			String json = buf.toString("UTF-8");

			System.out.println("UserHandler.validateUser() request :" + json);

			JsonObject obj = new JsonObject(json);
			String user = obj.getString("userName");
			String pwd = obj.getString("password");

			HttpServerResponse response = routingContext.response();

			User u;
			try {
				u = dao.authenticate(user, pwd);
			} catch (Exception e) {
				System.out.println("UserHandler.validateUser() exception validating  credentials");
				response.setStatusCode(500).end("Could Not authenticate");
				return;
			}
			if (null == u) {
				response.setStatusCode(404).end("not found");
				return;
			}

			Long sessionTime = (new Date()).getTime()
					- Constants.SESSION_TIMEOUT;
			Long lastActivity = u.getLastAccessed().getTime();

			if (u.getSignedIn() && lastActivity > sessionTime) {
				response.setStatusCode(401).end("User already logged in");
				return;
			}

			Session session = routingContext.session();
			session.put("user", u);

			UserDTO dto = UserWrapper.toDTO(u);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.valueToTree(dto);
			response.setStatusCode(200).end(node.toString());

		});
	}

	private List<User> getActiveUsers(List<User> allUsers) {
		final List<User> expiredUsers = new ArrayList<User>();
		final List<User> activeUsers = new ArrayList<User>();
		Long sessionStartTime = (new Date()).getTime()
				- Constants.SESSION_TIMEOUT;

		for (User u : allUsers) {
			Long lastActivityTime = u.getLastAccessed().getTime();
			if (u.getSignedIn() && lastActivityTime <= sessionStartTime) {
				u.setSignedIn(false);
				expiredUsers.add(u);
			} else {
				activeUsers.add(u);
			}
		}
		if (!expiredUsers.isEmpty()) {
			try {
				dao.updateAfterSessionExpiry(new Date(sessionStartTime));
			} catch (Exception e) {
				System.out
						.println("UserHandler.getActiveUsers() Exception processing session cleanup");
			}
		}
		return activeUsers;
	}

}
