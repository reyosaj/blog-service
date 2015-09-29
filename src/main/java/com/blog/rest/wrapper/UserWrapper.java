/**
 * 
 */
package com.blog.rest.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.blog.dto.UserDTO;
import com.blog.mongodb.model.User;

/**
 * @author reyos
 *
 */
public class UserWrapper {

	public static User toModel(UserDTO dto) {
		User user = new User();
		if (dto.getId() != null) {
			user.setId(new ObjectId(dto.getId()));
		}
		user.setPassword(dto.getPassword());
		user.setUserFirst(dto.getFirst());
		user.setUserLast(dto.getLast());
		user.setUserName(dto.getUserName());
		user.setIsCompany(dto.getIsCompany());
		user.setEmail(dto.getEmail());
		user.setSignedIn(dto.getSignedIn());

		return user;
	}

	public static UserDTO toDTO(User u) {
		UserDTO dto = new UserDTO();

		if (u.getId() != null) {
			dto.setId(u.getId().toHexString());
		}
		dto.setFirst(u.getUserFirst());
		dto.setLast(u.getUserLast());
		dto.setUserName(u.getUserName());
		dto.setIsCompany(u.getIsCompany());
		dto.setEmail(u.getEmail());

		return dto;
	}

	public static List<UserDTO> toDTOList(List<User> list) {
		List<UserDTO> result = new ArrayList<UserDTO>();

		for (User u : list) {
			result.add(toDTO(u));
		}

		return result;
	}
}
