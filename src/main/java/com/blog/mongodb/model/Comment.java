package com.blog.mongodb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author reyos
 *
 */
public class Comment implements Serializable, Comparable<Comment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6625018864680184699L;
	private String content;
	private Date date;
	private String userId;
	private String blogId;
	private String userFirst;
	private String userLast;

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the blogId
	 */
	public String getBlogId() {
		return blogId;
	}

	/**
	 * @param blogId
	 *            the blogId to set
	 */
	public void setBlogId(String blogId) {
		this.blogId = blogId;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append("content=" + this.content);
		builder.append(", userId=" + this.userId);
		builder.append(", date=" + this.date);
		builder.append(", blogId=" + this.blogId);
		builder.append(", userFirst=" + this.userFirst);
		builder.append(", userLast=" + this.userLast);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Comment o) {
		if (null == o || this.getDate() == null || o.getDate() == null) {
			return 0;
		} else if (this.getDate().before(o.getDate())) {
			return -1;
		} else if (this.getDate().after(o.getDate())) {
			return 1;
		}
		return 0;
	}

}
