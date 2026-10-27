package db.editiontype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}