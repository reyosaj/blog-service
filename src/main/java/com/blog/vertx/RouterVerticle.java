/**
 * 
 */
package com.blog.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import com.blog.Constants;
import com.blog.vertx.routes.BlogHandler;
import com.blog.vertx.routes.CompanyHandler;
import com.blog.vertx.routes.UserActivityHandler;
import com.blog.vertx.routes.UserHandler;
import com.blog.vertx.ws.WebSocketHandler;

/**
 * @author reyos
 *
 */
public class RouterVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		HttpServer server = vertx.createHttpServer();
		
		server.websocketHandler(new WebSocketHandler());

		Router router = Router.router(vertx);
		router.route().handler(CookieHandler.create());

		SessionStore sessionStore = LocalSessionStore.create(vertx);
		SessionHandler sessionHandler = SessionHandler.create(sessionStore);
		sessionHandler.setSessionTimeout(Constants.SESSION_TIMEOUT);

		// Session handler
		router.route().handler(sessionHandler);
		
		router.route().handler(new UserActivityHandler()::trackActivity);

		// Mount all feature services as sub-routes
		router.mountSubRouter("/Services/rest/user",
				new UserHandler(vertx).getRouter());
		router.mountSubRouter("/Services/rest/blogs",
				new BlogHandler(vertx).getRouter());
		router.mountSubRouter("/Services/rest/company", new CompanyHandler(
				vertx).getRouter());

		// Finally, mount the static route for all HTML content
		router.route("/*").handler(
				StaticHandler.create("webroot").setDirectoryListing(true));

		server.requestHandler(router::accept).listen(Constants.ROUTER_PORT);

		System.out.println("STARTED ROUTER ON PORT " + Constants.ROUTER_PORT);

		startFuture.complete();
	}

}
