package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import models.Picture;
import models.User;
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
		String username = request().username();
		List<Picture> pictures = Picture.getAllImagesOfUser(username);
		return ok(views.html.pictures.render("Eigene Bilder", pictures));
	}
	
	public static Result newPicture() throws IOException {
		DynamicForm requestData = DynamicForm.form().bindFromRequest();
		
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("file");
		if (picture != null) {
			String fileName = picture.getFilename();
			String contentType = picture.getContentType(); 
			File file = picture.getFile();
			String description = requestData.get("description");
			Boolean publicVisible = Boolean.valueOf(requestData.get("publicVisible"));
			
			Picture pic = new Picture();
			pic.setName(fileName);
			pic.setCreationDate(new Date());
			pic.setOwner(User.byName(request().username()));
			pic.setMimeType(contentType);
			pic.setPublicVisible(publicVisible);
			pic.setData(FileUtils.readFileToByteArray(file));
			pic.setDescription(description);
			
			Picture.newPicture(pic);
			
			return ok("File uploaded");
		} else {
			flash("error", "Missing file");
			return redirect(routes.Application.index());    
		}

	}
	
	public static Result thumbnail(Long id) {
		Picture pic = Picture.find.where().eq("id", id).findUnique();
		
		return ok(pic.getData()).as(pic.getMimeType());
	}
}
