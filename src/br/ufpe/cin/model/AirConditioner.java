package br.ufpe.cin.model;

public class AirConditioner {

	private boolean mIsOn;
	
	public void turnOn() {
		this.mIsOn = true;
	}
	
	public void turnOff() {
		this.mIsOn = false;
	}
	
	public OcRepresentation serialize() {
	      OcRepresentation rep = new OcRepresentation();
	      rep.setValue("isOn", mIsOn ? "Yes" : "NO");
	      return rep;
	}
	
	public void toggleMode() {
		this.mIsOn = !this.mIsOn;
	}
	
}
