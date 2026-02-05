package db.family;

public class FamilyLevel3 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String description;
    private String descriptionShort;
    
    private String idFamilyLevel1;
    private String idFamilyLevel2;
    private String idFamilyLevel3;
	
    public FamilyLevel3() {}
	
    /** full constructor */
    public FamilyLevel3(int id) {
        this.id = id;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getIdFamilyLevel3() {
		return idFamilyLevel3;
	}

	public void setIdFamilyLevel3(String idFamilyLevel3) {
		this.idFamilyLevel3 = idFamilyLevel3;
	}
}