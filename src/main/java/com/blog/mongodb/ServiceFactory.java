/**
 * 
 */
package com.blog.mongodb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.blog.Constants;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * @author reyos
 *
 */
public class ServiceFactory {

	private static final ThreadLocal<Datastore> MTL = new ThreadLocal<Datastore>();

	public static Datastore getMongoDB() {
		if (MTL.get() == null) {
			MongoClientURI connectionString = new MongoClientURI(
					Constants.MONGODB_URI);
			MongoClient mongoClient = new MongoClient(connectionString);
			Morphia morphia = new Morphia();
			morphia.mapPackage(Constants.MONGODB_MODEL_PACKAGE);
			Datastore datastore = morphia.createDatastore(mongoClient,
					Constants.MONGODB_STORE);
			datastore.ensureIndexes();
			MTL.set(datastore);
			return datastore;
		}
		return MTL.get();
	}

}
