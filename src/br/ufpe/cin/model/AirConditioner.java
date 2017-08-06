package br.ufpe.cin.model;

import org.iotivity.base.OcException;
import org.iotivity.base.OcRepresentation;

public class AirConditioner {

	private boolean mIsOn;
	
	public void turnOn() {
		this.mIsOn = true;
	}
	
	public void turnOff() {
		this.mIsOn = false;
	}
	
	public OcRepresentation serialize() throws OcException {
	      OcRepresentation rep = new OcRepresentation();
	      rep.setValue("isOn", mIsOn ? "Yes" : "NO");
	      return rep;
	}
	
	public void toggleMode() {
		this.mIsOn = !this.mIsOn;
	}

}
