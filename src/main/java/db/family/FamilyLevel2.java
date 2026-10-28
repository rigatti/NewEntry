package db.family;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FamilyLevel2 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
    private String idFamilyLevel1;
	@EqualsAndHashCode.Include
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