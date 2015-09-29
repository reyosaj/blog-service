/**
 * 
 */
package com.blog.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author reyos
 *
 */
@SuppressWarnings("serial")
public class UserDTO implements Serializable {

	/**
	 * 
	 */

	private String id;
	private String first;
	private String last;
	private String userName;
	@JsonInclude(Include.NON_EMPTY)
	private String password;
	private Boolean isCompany;
	private String email;
	@JsonInclude(Include.NON_EMPTY)
	private Boolean signedIn;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the userFirst
	 */
	public String getFirst() {
		return first;
	}

	/**
	 * @param userFirst
	 *            the userFirst to set
	 */
	public void setFirst(String userFirst) {
		this.first = userFirst;
	}

	/**
	 * @return the userLast
	 */
	public String getLast() {
		return last;
	}

	/**
	 * @param userLast
	 *            the userLast to set
	 */
	public void setLast(String userLast) {
		this.last = userLast;
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

}
