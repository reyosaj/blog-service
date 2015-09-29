/**
 * 
 */
package com.blog.mongodb.dao;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.blog.mongodb.ServiceFactory;
import com.blog.mongodb.model.User;

/**
 * @author reyos
 *
 */
public class UserDAOImpl implements UserDAO {

	private final Datastore dataStore;

	public UserDAOImpl() {
		dataStore = ServiceFactory.getMongoDB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blog.mongodb.dao.UserDAO#getSignedInUsers()
	 */
	@Override
	public List<User> getSignedInUsers() throws Exception {
		Query<User> query = dataStore.createQuery(User.class).field("signedIn")
				.equal(true);
		return query.asList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blog.mongodb.dao.UserDAO#getAllUsers()
	 */
	@Override
	public List<User> getAllUsers() throws Exception {
		Query<User> query = dataStore.createQuery(User.class);
		return query.asList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blog.mongodb.dao.UserDAO#createUser(com.blog.mongodb.model.User)
	 */
	@Override
	public Boolean createUser(User u) throws Exception {
		dataStore.save(u);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blog.mongodb.dao.UserDAO#authenticate(com.blog.mongodb.model.User)
	 */
	@Override
	public User authenticate(String usrName, String pwd) throws Exception {
		Query<User> query = dataStore.createQuery(User.class);
		query.field("userName").equal(usrName);
		query.field("password").equal(pwd);
		List<User> users = query.asList();

		return (users.isEmpty() ? null : users.get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blog.mongodb.dao.UserDAO#updateAfterSessionExpiry(java.util.Date)
	 */
	@Override
	public Boolean updateAfterSessionExpiry(Date sessionStart) throws Exception {
		Query<User> q = dataStore.createQuery(User.class);
		q.field("signedIn").equals(true);
		q.field("lastAccessed").lessThanOrEq(sessionStart);
		UpdateOperations<User> ops = dataStore.createUpdateOperations(
				User.class).set("signedIn", false);
		dataStore.findAndModify(q, ops);
		return true;
	}

	@Override
	public Boolean keepAlive(String userId) throws Exception {
		List<User> users = dataStore.createQuery(User.class)
				.field("id").equal(new ObjectId(userId)).asList();

		if (!users.isEmpty()) {
			User u = users.get(0);
			u.setSignedIn(true);
			u.setLastAccessed(new Date());

			dataStore.save(u);
		}
		return true;
	}

}
