package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.register;

public class UserController extends Controller {

	static Form<User> userForm = Form.form(User.class);

	public static Result index() {
		return ok(views.html.users.render(User.all()));
	}

	public static Result register() {
		Form<User> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(register.render(filledForm));
		} else {
			User.create(filledForm.get());
			return redirect(routes.UserController.index());
		}
	}

}
