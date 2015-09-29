package com.blog.mongodb.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author reyos
 *
 */
@Entity
public class User {
	@Id
	private ObjectId id;
	private String userFirst;
	private String userLast;
	private String userName;
	private String password;
	private Boolean isCompany;
	private String email;
	private Boolean signedIn;
	private Date lastAccessed;

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * @return the userFirst
	 */
	public String getUserFirst() {
		return userFirst;
	}

	/**
	 * @param userFirst
	 *            the userFirst to set
	 */
	public void setUserFirst(String userFirst) {
		this.userFirst = userFirst;
	}

	/**
	 * @return the userLast
	 */
	public String getUserLast() {
		return userLast;
	}

	/**
	 * @param userLast
	 *            the userLast to set
	 */
	public void setUserLast(String userLast) {
		this.userLast = userLast;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the isCompany
	 */
	public Boolean getIsCompany() {
		return isCompany;
	}

	/**
	 * @param isCompany the isCompany to set
	 */
	public void setIsCompany(Boolean isCompany) {
		this.isCompany = isCompany;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the signedIn
	 */
	public Boolean getSignedIn() {
		return signedIn;
	}

	/**
	 * @param signedIn the signedIn to set
	 */
	public void setSignedIn(Boolean signedIn) {
		this.signedIn = signedIn;
	}

	/**
	 * @return the lastAccessed
	 */
	public Date getLastAccessed() {
		return lastAccessed;
	}

	/**
	 * @param lastAccessed the lastAccessed to set
	 */
	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

}
