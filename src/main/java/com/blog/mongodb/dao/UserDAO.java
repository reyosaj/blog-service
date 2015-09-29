/**
 * 
 */
package com.blog.mongodb.dao;

import java.util.Date;
import java.util.List;

import com.blog.mongodb.model.User;

/**
 * @author reyos
 *
 */
public interface UserDAO {

	List<User> getSignedInUsers() throws Exception;

	List<User> getAllUsers() throws Exception;

	Boolean createUser(User u) throws Exception;

	User authenticate(String usrName, String pwd) throws Exception;

	Boolean updateAfterSessionExpiry(Date sessionStart) throws Exception;
	
	Boolean keepAlive(String userId) throws Exception;

}
