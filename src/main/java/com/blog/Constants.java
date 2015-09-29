/**
 * 
 */
package com.blog;

/**
 * @author reyos
 *
 */
public class Constants {
	public static final Integer ROUTER_PORT = 8080;
	public static final Integer SESSION_TIMEOUT = 10 * 60 * 1000; //10 MINUTES
	
	
	
	public static final String MONGODB_URI = "mongodb://localhost:27017";
	public static final String MONGODB_STORE = "blogservices";
	public static final String MONGODB_MODEL_PACKAGE = "com.blog.mongodb.model";
	
	public static final String ZK_CLIENT_CONN = "localhost:2180";
	public static final Integer ZK_CLIENT_TIMEOUT = 3000;
	
}
