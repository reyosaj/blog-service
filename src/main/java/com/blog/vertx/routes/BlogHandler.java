package com.blog.vertx.routes;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.blog.dto.BlogDTO;
import com.blog.dto.CommentDTO;
import com.blog.ixc.DistributedStore;
import com.blog.ixc.zookeeper.DistributedStoreZooImpl;
import com.blog.mongodb.dao.BlogDAO;
import com.blog.mongodb.dao.BlogDAOImpl;
import com.blog.mongodb.model.Blog;
import com.blog.mongodb.model.Comment;
import com.blog.mongodb.model.User;
import com.blog.rest.wrapper.BlogWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author reyos
 * 
 *         Handler for all User api's
 *
 */
public class BlogHandler {

	private final Router router;
	private final DistributedStore<String, Comment> commentStore;
	private final BlogDAO dao;

	/**
	 * @param vertx
	 * 
	 *            Configure all Routes
	 * @throws Exception
	 */
	public BlogHandler(Vertx vertx) throws Exception {

		this.router = Router.router(vertx);
		this.commentStore = new DistributedStoreZooImpl<String, Comment>();

		this.router.get().handler(this::getAllBlogs);
		this.router.route().handler(new AuthHandler()::authenticate);
		this.router.post("/:blogId/comments").handler(this::postComment);
		this.router.post().handler(this::createBlog);
		this.dao = new BlogDAOImpl();
	}

	/**
	 * @return
	 */
	public Router getRouter() {
		return this.router;
	}

	private void getAllBlogs(RoutingContext routingContext) {

		System.out.println("BlogHandler.getAllBlogs()");

		HttpServerResponse response = routingContext.response();
		response.putHeader("content-type", "application/json");

		List<Blog> blogs;
		try {
			blogs = dao.getAllBlogs();
		} catch (Exception e) {
			System.out
					.println("BlogHandler.getAllBlogs() exception retrieving blogs");
			final JsonArray array = new JsonArray();
			response.setStatusCode(400).end(array.toString());
			return;
		}
		blogs = loadCachedComments(blogs);

		final List<BlogDTO> finalList = BlogWrapper.toDTOList(blogs);
		final JsonArray array = new JsonArray(finalList);

		response.setStatusCode(200).end(array.toString());
	}

	private List<Blog> loadCachedComments(List<Blog> asList) {

		for (Blog b : asList) {
			try {
				List<Comment> list = commentStore.getValues(b.getId()
						.toHexString());
				if (!list.isEmpty()) {
					b.getComments().addAll(list);
					// order by date ascending
					Collections.sort(b.getComments());
				}
			} catch (Exception e) {
				System.out.println("Error Loading comments for "
						+ b.getId().toHexString());
			}
		}
		return asList;
	}

	private void createBlog(RoutingContext routingContext) {

		HttpServerRequest req = routingContext.request();
		req.bodyHandler(buf -> {

			String json = buf.toString("UTF-8");

			System.out
					.println("BlogHandler.createBlog() request Body : " + buf);

			ObjectMapper mapper = new ObjectMapper();
			BlogDTO dto = null;
			HttpServerResponse response = routingContext.response();
			try {
				dto = mapper.readValue(json, BlogDTO.class);

				Session session = routingContext.session();
				User u = (User) session.get("user");

				dto.setUserFirst(u.getUserFirst());
				dto.setUserLast(u.getUserLast());
				dto.setUserId(u.getId().toHexString());
				dto.setDate(new Date());

				Blog b = BlogWrapper.toModel(dto);
				String blogId = dao.addBlog(b);

				JsonObject obj = new JsonObject();
				obj.put("blogId", blogId);

				response.setStatusCode(201).end(obj.toString());

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Could not process request");
				response.setStatusCode(400).end("Bad Request");
			}

		});
	}

	private void postComment(RoutingContext routingContext) {
		HttpServerRequest req = routingContext.request();
		req.bodyHandler(buf -> {

			String blogId = routingContext.request().getParam("blogId");
			HttpServerResponse response = routingContext.response();
			String json = buf.toString("UTF-8");

			System.out.println("BlogHandler.postComment() request body : "
					+ json);

			if (blogId == null) {
				response.setStatusCode(400).end("Bad Request");
			}

			ObjectMapper mapper = new ObjectMapper();
			CommentDTO dto = null;
			try {
				dto = mapper.readValue(json, CommentDTO.class);

				Blog blog = null;
				try {
					blog = dao.getBlogById(blogId);
				} catch (Exception e1) {
					System.out.println("BlogHandler.postComment() Blog "
							+ blogId + " NOT found.");
				}

				if (blog == null) {
					response.setStatusCode(404).end("Blog Not Found");
					return;
				}
				Session session = routingContext.session();
				User u = (User) session.get("user");

				// Blog b = blogs.get(0);
				Comment c = new Comment();
				c.setBlogId(blogId);
				c.setContent(dto.getContent());
				c.setDate(new Date());
				c.setUserFirst(u.getUserFirst());
				c.setUserId(u.getId().toHexString());
				c.setUserLast(u.getUserLast());

				// will not use mongodb for project assignment
				// b.getComments().add(c);
				// dataStore.save(b);

				try {
					// send to in-memory store
					commentStore.add(blogId, c);
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatusCode(500).end(
							"Could Not store Comment for Blog: " + blogId);
					return;
				}

				JsonNode node = mapper.valueToTree(BlogWrapper.commentToDTO(c));
				response.setStatusCode(201).end(node.toString());

			} catch (IOException e) {
				System.err.println("Could not process request");
				response.setStatusCode(400).end("Bad Request");
			}
		});

	}
}