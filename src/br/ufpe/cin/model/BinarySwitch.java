package br.ufpe.cin.model;

public class BinarySwitch {
	
	private boolean mValue;

	
	public BinarySwitch() {
	}
	
	
	public boolean getValue() {
		return mValue;
	}

	public void setValue(boolean mValue) {
		this.mValue = mValue;
	}
	
	public void toggleBinarySwitch() {
		this.mValue = !mValue;		
	}
	
	public OcRepresentation serialize() {
		OcRepresentation rep = new OcRepresentation ();
		rep.setValue("value", this.mValue);
		return rep;
	}
	
}
