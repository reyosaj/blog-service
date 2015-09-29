/**
 * 
 */
package com.blog.mongodb.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.blog.mongodb.ServiceFactory;
import com.blog.mongodb.model.Blog;

/**
 * @author reyos
 *
 */
public class BlogDAOImpl implements BlogDAO {

	private Datastore dataStore;

	public BlogDAOImpl() {
		dataStore = ServiceFactory.getMongoDB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blog.mongodb.dao.BlogDAO#getAllBlogs()
	 */
	@Override
	public List<Blog> getAllBlogs() throws Exception {
		final Query<Blog> query = dataStore.createQuery(Blog.class);
		return query.asList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blog.mongodb.dao.BlogDAO#addBlog(com.blog.mongodb.model.Blog)
	 */
	@Override
	public String addBlog(Blog b) throws Exception {
		Key<Blog> blog = dataStore.save(b);
		return blog.getId().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blog.mongodb.dao.BlogDAO#getBlogById(java.lang.String)
	 */
	@Override
	public Blog getBlogById(String blogId) throws Exception {
		List<Blog> blogs = dataStore.createQuery(Blog.class).field("id")
				.equal(new ObjectId(blogId)).asList();
		return (blogs.isEmpty() ? null : blogs.get(0));
	}

}
