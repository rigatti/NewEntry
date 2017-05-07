package org.belex.fly;

import java.io.Serializable;

public class Fly implements Serializable {

	private static final long serialVersionUID = 1L;
	private String airportCode;
	private String flyNumber;
	private String ltaNumber;
	private int fret;
	
	public Fly() {
	}
	public String getFlyNumber() {
		return flyNumber;
	}
	public void setFlyNumber(String flyNumber) {
		this.flyNumber = flyNumber;
	}
	public int getFret() {
		return fret;
	}
	public void setFret(int fret) {
		this.fret = fret;
	}
	public String getLtaNumber() {
		return ltaNumber;
	}
	public void setLtaNumber(String ltaNumber) {
		this.ltaNumber = ltaNumber;
	}
	public String getAirportCode() {
		return airportCode;
	}
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

}