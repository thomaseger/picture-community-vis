package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import models.Picture;
import models.User;

import org.apache.commons.io.FileUtils;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;

public class PictureController extends Controller {

	static Form<Picture> pictureForm = Form.form(Picture.class);
	
	@Security.Authenticated(Secured.class)
	public static Result index() {
		return redirect(routes.PictureController.pictures(request().username()));
	}
	
	public static Result pictures(String username) {
		String loggedInUsername = session().get("name");
		List<Picture> pictures;
		if (username.equals(loggedInUsername)) {
			pictures = Picture.getAllImagesOfUser(username);
		} else {
			pictures = Picture.getPublicImagesOfUser(username);
		}
		return ok(views.html.pictures.render("Bilder von " + username, pictures));
	}
	
	@Security.Authenticated(Secured.class)
	public static Result uploadForm() {
		return ok(views.html.uploadPicture.render());
	}
	
	@Security.Authenticated(Secured.class)
	public static Result newPicture() throws IOException {
		String username = request().username();
		
		DynamicForm requestData = DynamicForm.form().bindFromRequest();
		System.out.println("data: " + requestData.data());
		System.out.println("public: " + requestData.get("publicVisible"));
		
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("file");
		if (picture != null) {
			String fileName = picture.getFilename();
			String contentType = picture.getContentType(); 
			File file = picture.getFile();
			String description = requestData.get("description");
			Boolean publicVisible = "on".equalsIgnoreCase(requestData.get("publicVisible"));
			
			Picture pic = new Picture();
			pic.setName(fileName);
			pic.setCreationDate(new Date());
			pic.setOwner(User.byName(username));
			pic.setPublicVisible(publicVisible);
			pic.setData(FileUtils.readFileToByteArray(file));
			pic.setDescription(description);
			pic.setMimeType(contentType);
			
			Picture.newPicture(pic);
			
			return redirect(routes.PictureController.index());
		} else {
			flash("error", "Missing file");
			return redirect(routes.Application.index());    
		}

	}
	
	public static Result thumbnail(Long id) {
		String username = session().get("name");
		
		Picture pic = Picture.find.where().eq("id", id).findUnique();
		if (pic.isPublicVisible() || pic.getOwner().getName().equals(username)) {
			return ok(pic.getData()).as(pic.getMimeType());
		} else {
			return forbidden("Access denied!");
		}
	}
}
