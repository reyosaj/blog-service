package com.blog.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author reyos
 *
 */
@Entity
public class BlogDTO {
	@Id
	private String id;
	private String content;
	private Date date;
	private String tags;
	private String userId;
	private List<CommentDTO> comments;
	private String title;
	private String userFirst;
	private String userLast;

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
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
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
	 * @return the comments
	 */
	public List<CommentDTO> getComments() {
		if (comments == null) {
			comments = new ArrayList<CommentDTO>();
		}
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<CommentDTO> comments) {
		this.comments = comments;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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

}
