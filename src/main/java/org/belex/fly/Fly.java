package org.belex.fly;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Fly implements Serializable {

	private static final long serialVersionUID = 1L;
	private String airportCode;
	private String flyNumber;
	private String ltaNumber;
	private int fret;
	
	public Fly() {
	}

}