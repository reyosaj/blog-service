/**
 * 
 */
package com.blog.vertx.routes;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import com.blog.mongodb.dao.UserDAO;
import com.blog.mongodb.dao.UserDAOImpl;
import com.blog.mongodb.model.User;

/**
 * @author reyos
 *
 */
public class UserActivityHandler {

	private final UserDAO dao;

	public UserActivityHandler() {
		dao = new UserDAOImpl();
	}

	public void trackActivity(RoutingContext routingContext) {
		System.out.println("UserActivityHandler.trackActivity()");
		Session session = routingContext.session();

		if (session != null) {
			User uObj = (User) session.get("user");

			if (uObj != null) {
				try {
					dao.keepAlive(uObj.getId().toHexString());
				} catch (Exception e) {
					System.out
							.println("UserActivityHandler.trackActivity() Error logging user activity.");
				}
			}
		}

		routingContext.next();

	}

}
