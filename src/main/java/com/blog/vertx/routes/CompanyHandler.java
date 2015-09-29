/**
 * 
 */
package com.blog.vertx.routes;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

import com.blog.dto.CompanyDTO;

/**
 * @author reyos
 *
 */
public class CompanyHandler {

	private final Router router;

	/**
	 * @param vertx
	 * 
	 *            Configure all Routes
	 */
	public CompanyHandler(Vertx vertx) {
		this.router = Router.router(vertx);
		this.router.get().handler(this::getAllCompanies);
	}

	/**
	 * @return
	 */
	public Router getRouter() {
		return this.router;
	}

	private void getAllCompanies(RoutingContext routingContext) {
		System.out.println("CompanyHandler.getAllCompanies()");
		
		HttpServerResponse response = routingContext.response();
		response.putHeader("content-type", "application/json");

		final List<CompanyDTO> list = new ArrayList<CompanyDTO>();
		list.add(new CompanyDTO("1", "social.com"));
		list.add(new CompanyDTO("2", "media.com"));
		list.add(new CompanyDTO("3", "impact.com"));
		list.add(new CompanyDTO("4", "awareness.com"));
		list.add(new CompanyDTO("5", "reponsibility.com"));

		final JsonArray array = new JsonArray(list);

		response.setStatusCode(200).end(array.toString());
	}

}
