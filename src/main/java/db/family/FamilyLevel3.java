package db.family;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}