package controllers;

import models.User;
import views.html.index;
import views.html.login;
import views.html.register;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class Application extends Controller {

	@Security.Authenticated(Secured.class)
	public static Result index() {
		return ok(index.render(User.find.where().eq("name", request().username()).findUnique()));
	}
	
	public static Result register() {
        return ok(register.render(UserController.userForm));
    }
	
	public static Result login() {
        return ok(login.render(Form.form(Login.class)));
    }

	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session().clear();
			session("name", loginForm.get().name);
			return redirect(routes.Application.index());
		}
	}
	
	@Security.Authenticated(Secured.class)
	public static Result logout() {
		session().clear();
	    return redirect(routes.Application.login());
	}

	public static class Login {

		public String name;

		public String password;

		public String validate() {
			if (User.authenticate(name, password) == null) {
				return "Invalid user or password";
			}
			return null;
		}
	}
}
