package models;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.Required;

public class User {
	
	private static List<User> allUsers = new ArrayList<User>();
	
	private Long id;

	@Required
	private String name;

	@Required
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static List<User> all() {
		return allUsers;
	}

	public static void create(User user) {
		allUsers.add(user);
	}
}
