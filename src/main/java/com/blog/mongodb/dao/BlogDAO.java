/**
 * 
 */
package com.blog.mongodb.dao;

import java.util.List;

import com.blog.mongodb.model.Blog;

/**
 * @author reyos
 *
 */
public interface BlogDAO {
	List<Blog> getAllBlogs() throws Exception;

	String addBlog(Blog b) throws Exception;

	Blog getBlogById(String blogId) throws Exception;

}
