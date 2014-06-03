package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Die Klasse beschreibt ein Bild in der Datenbank.
 * @author Holger Vogelsang
 */
@Entity
public class Picture extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Finder<Long, Picture> find = new Finder<Long, Picture>(Long.class,
			Picture.class);

	// Eindeutige Datenbank-ID des Bildes.
	@Id
	private Long id;

	// Mime-Typ des Bildes.
	@Required
	private String mimeType;

	// Eigentlicher Inhalt des Bildes.
	@Required
	private byte[] data;

	// Textuelle Beschreibung des Bildes.
	@Required
	private String description;

	// Datum, an dem das Bildobjekt erzeugt wurde.
	@Required
	private Date creationDate = new Date();

	// Name des Bildes.
	@Required
	private String name;

	// Ist das Bild oeffentlich einsehbar?
	// Wenn nicht, dann muessen sich andere Anwender
	// vorher anmelden.
	@Required
	private boolean publicVisible;

	// Anwender, dem das Bild gehoert.
	@Required
	private User owner;

	/**
	 * Eindeutige Datenbank-ID auslesen.
	 * @return Eindeutige Datenbank-ID.
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Eintragen der ID. Diese Methode sollte nie direkt
	 * verwendet werden.
	 * @param id Neue ID des Bildes.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Mime-Typ des Bildes auslesen.
	 * @return Mime-Typ des Bildes.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Mime-Typ des Bildes aendern.
	 * @param mimeType Neuer Mime-Typ des Bildes.
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Daten des Bildes auslesen.
	 * @return Daten des Bildes.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Daten des Bildes aendern.
	 * @param data Neue Daten des Bildes.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Textuelle Beschreibung des Bildes auslesen.
	 * @return Textuelle Beschreibung des Bildes.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Textuelle Beschreibung des Bildes aendern.
	 * @param description Neue textuelle Beschreibung des Bildes.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Erzeugungsdatum des Bildes auslesen.
	 * @return Erzeugungsdatum des Bildes.
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Erzeugungsdatum des Bildes aendern.
	 * @param creationDate Neues Erzeugungsdatum des Bildes.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Besitzer des Bildes auslesen.
	 * @return Besitzer des Bildes.
	 */
	@ManyToOne(targetEntity=User.class)
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User user) {
		this.owner = user;
	}

	/**
	 * Name des Bildes auslesen.
	 * @return Name des Bildes.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Dem Bild einen neuen Namen geben.
	 * @param name Neuer Name des Bildes.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Test, ob das Bild fuer nicht angemeldete Anwender sichtbar ist.
	 * @return <code>true</code>, wenn das Bild fuer nicht angemeldete Anwender
	 * 			sichtbar ist.
	 */
	public boolean isPublicVisible() {
		return publicVisible;
	}

	/**
	 * Festlegen, ob das Bild fuer nicht angemeldete Anwender sichtbar ist.
	 * @param publicVisible <code>true</code>, wenn nicht angemeldete Anwender
	 * 			das Bild ansehen koennen.
	 */
	public void setPublicVisible(boolean publicVisible) {
		this.publicVisible = publicVisible;
	}

	/**
	 * Zwei Bilder sind dann gleich, wenn sie dieselbe ID
	 * besitzen.
	 * @param other Zu vergleichendes Bild.
	 * @return <code>true</code>, wenn das Bild gleich dem aktuellen
	 * 			Bild-Objekt ist.
	 */
	@Override
	public boolean equals(Object other) {
		if (other != null && other.getClass() == getClass()) {
			Picture pic = (Picture) other;
			if (getId() == null && pic.getId() == null) {
				return true;
			}
			if (getId() == null && pic.getId() != null ||
			    getId() != null && pic.getId() == null) {
				return false;
			}
			return getId().equals(pic.getId());
		}
		return false;
	}
	
	/**
	 * Als Hash-Code dient hier nur die ID, die sich bei allen Objekten
	 * unterscheidet.
	 * @return Hash-Code eines Bildes.
	 */
	@Override
	public int hashCode() {
		return getId().intValue();
	}
	
	/**
	 * Fuer eine lesbarerer textuelle des Bildes.
	 * @return Textuelle Darstellung der Bildinformationen.
	 */
	@Override
	public String toString() {
		return "[Picture " + getName() + ": " + owner + " length=" + ((data != null) ? data.length : "<null>") + "]";
	}
	
	public static List<Picture> getPublicImagesOfUser(String username) {
		User user = User.find.where().eq("name", username).findUnique();

		List<Picture> pictures = find.where()
				.eq("owner", user)
				.eq("publicVisible", true)
				.findList();
		return pictures;
	}
	
	public static List<Picture> getAllImagesOfUser(String username) {
		List<Picture> pictures = find
				.where()
					.like("owner.name", username)
				.findList();

		return pictures;
	}

	public static void newPicture(Picture picture) {
		picture.save();
	}
}
