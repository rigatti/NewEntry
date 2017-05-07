package db.family;

public class FamilyLevel1 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String description;
    private String descriptionShort;
    private float margin;
	
    public FamilyLevel1() {}
	
    /** full constructor */
    public FamilyLevel1(String id) {
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

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public float getMargin() {
		return margin;
	}

	public void setMargin(float margin) {
		this.margin = margin;
	}



}