package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class PictureController extends Controller {

	@Security.Authenticated(Secured.class)
	public static Result index() {
		return TODO;
	}
	
}
