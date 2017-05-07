package db.family;

public class FamilyLevel2 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
    private String idFamilyLevel1;
    private String idFamilyLevel2;
    
	private String description;
    private String descriptionShort;
	
    public FamilyLevel2() {}
	
    /** full constructor */
    public FamilyLevel2(String idFamilyLevel1, String idFamilyLevel2) {
        this.idFamilyLevel1 = idFamilyLevel1;
        this.idFamilyLevel2 = idFamilyLevel2;
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

	public String getIdFamilyLevel1() {
		return idFamilyLevel1;
	}

	public void setIdFamilyLevel1(String idFamilyLevel1) {
		this.idFamilyLevel1 = idFamilyLevel1;
	}

	public String getIdFamilyLevel2() {
		return idFamilyLevel2;
	}

	public void setIdFamilyLevel2(String idFamilyLevel2) {
		this.idFamilyLevel2 = idFamilyLevel2;
	}
}