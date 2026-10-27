package db.family;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}