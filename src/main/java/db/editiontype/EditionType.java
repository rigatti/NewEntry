package db.editiontype;

public class EditionType implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
    private String description;
    private boolean followed;
	
    public EditionType() {}
	
    /** full constructor */
    public EditionType(String id) {
        this.id = id;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}


}