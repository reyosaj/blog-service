/**
 * 
 */
package com.blog.rest.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import com.blog.dto.BlogDTO;
import com.blog.dto.CommentDTO;
import com.blog.mongodb.model.Blog;
import com.blog.mongodb.model.Comment;

/**
 * @author reyos
 *
 */
public class BlogWrapper {

	/**
	 * @param dto
	 * @return
	 */
	public static Blog toModel(BlogDTO dto) {
		Blog blog = new Blog();
		if (dto.getId() != null) {
			blog.setId(new ObjectId(dto.getId()));
		}
		blog.setContent(dto.getContent());
		blog.setDate(dto.getDate());
		if (dto.getTags() != null) {
			blog.setTags(Arrays.asList(dto.getTags().split(",")));
		}

		blog.setTitle(dto.getTitle());
		blog.setUserFirst(dto.getUserFirst());
		blog.setUserLast(dto.getUserLast());
		blog.setUserId(dto.getUserId());
		blog.setComments(commentToModel(dto.getComments()));

		return blog;
	}

	/**
	 * @param dtoList
	 * @return
	 */
	public static List<Comment> commentToModel(List<CommentDTO> dtoList) {
		List<Comment> list = new ArrayList<Comment>();

		if (dtoList == null) {
			return list;
		}

		for (CommentDTO dto : dtoList) {
			Comment c = new Comment();
			c.setBlogId(dto.getBlogId());
			c.setContent(dto.getContent());
			c.setDate(dto.getDate());
			c.setUserFirst(dto.getUserFirst());
			c.setUserLast(dto.getUserLast());
			list.add(c);
		}

		return list;
	}

	/**
	 * @param b
	 * @return
	 */
	public static BlogDTO toDTO(Blog b) {
		BlogDTO dto = new BlogDTO();
		dto.setContent(b.getContent());
		dto.setDate(b.getDate());
		if (b.getId() != null) {
			dto.setId(b.getId().toHexString());
		}
		if (b.getTags() != null) {
			dto.setTags(StringUtils.join(b.getTags(), ','));
		}
		dto.setTitle(b.getTitle());
		dto.setUserFirst(b.getUserFirst());
		dto.setUserId(b.getUserId());
		dto.setUserLast(b.getUserLast());
		dto.setComments(commentsToDTO(b.getComments()));
		return dto;
	}

	/**
	 * @param comList
	 * @return
	 */
	public static List<CommentDTO> commentsToDTO(List<Comment> comList) {

		List<CommentDTO> dtoList = new ArrayList<CommentDTO>();

		if (comList == null) {
			return dtoList;
		}

		for (Comment com : comList) {
			CommentDTO dto = new CommentDTO();
			dto.setBlogId(com.getBlogId());
			dto.setContent(com.getContent());
			dto.setDate(com.getDate());
			dto.setUserFirst(com.getUserFirst());
			dto.setUserLast(com.getUserLast());
			dto.setUserId(com.getUserId());
			dtoList.add(dto);
		}

		return dtoList;

	}

	public static CommentDTO commentToDTO(Comment com) {

		CommentDTO dto = new CommentDTO();
		dto.setBlogId(com.getBlogId());
		dto.setContent(com.getContent());
		dto.setDate(com.getDate());
		dto.setUserFirst(com.getUserFirst());
		dto.setUserLast(com.getUserLast());
		dto.setUserId(com.getUserId());

		return dto;

	}

	public static List<BlogDTO> toDTOList(List<Blog> list) {
		List<BlogDTO> result = new ArrayList<BlogDTO>();

		for (Blog b : list) {
			result.add(toDTO(b));
		}

		return result;
	}
}
