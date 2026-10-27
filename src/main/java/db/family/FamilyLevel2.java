package db.family;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}