package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class UserController extends Controller {

	static Form<User> userForm = Form.form(User.class);

	public static Result index() {
		return ok(views.html.users.render(User.all()));
	}

	public static Result register() {
		Form<User> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(filledForm));
		} else {
			User.create(filledForm.get());
			return redirect(routes.UserController.index());
		}
	}

}
