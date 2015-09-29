package com.blog.vertx.routes;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import com.blog.mongodb.model.User;

/**
 * Handler to validate session 
 * @author reyos
 *
 */
public class AuthHandler {

	public void authenticate(RoutingContext routingContext) {
		System.out.println("AuthHandler.authenticate()");
		HttpServerResponse response = routingContext.response();
		Session session = routingContext.session();
		User u = (User) session.get("user");

		System.out.println("AuthHandler.authenticate() Authenticated ="
				+ ((u != null) ? true : false));
		if (u == null) {
			response.setStatusCode(401).end("Not Authorized");
		} else {
			routingContext.next();
		}
	}
}
