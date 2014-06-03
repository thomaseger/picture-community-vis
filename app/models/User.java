package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class User extends Model {

	private static final long serialVersionUID = 1L;

	public static Finder<Long, User> find = new Finder<Long, User>(Long.class,
			User.class);

	@Id
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
		return find.all();
	}
	
	public static User byName(String name) {
		return find.where().eq("name", name).findUnique();
	}

	/**
	 * @return the authenticated user or null if authentication failed
	 */
	public static User authenticate(String name, String password) {
		return find.where().eq("name", name).eq("password", password).findUnique();
	}

	public static void create(User user) {
		user.save();
	}
}
