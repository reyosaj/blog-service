package com.blog;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class MongoVertxMain {

	static Vertx vertex;

	public static void main(String[] args) {
		System.setProperty("vertx.disableFileCaching", "true");
		
		VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
		vertex = Vertx.vertx(options);
		DeploymentOptions depOptions = new DeploymentOptions();
		//configure multiple verticle instances
		depOptions.setInstances(2);
		vertex.deployVerticle("com.blog.vertx.RouterVerticle", depOptions);

	}
	
}
