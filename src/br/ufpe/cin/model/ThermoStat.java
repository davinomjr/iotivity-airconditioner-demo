package br.ufpe.cin.model;

public class ThermoStat {
	
	private int mTemperature;
	private string mRange;
	private String mUnits;
	
	public ThermoStat() {
		
	}
	
	public int getTemperature() {
		return mTemperature;
	}
	public void setTemperature(int mTemperature) {
		this.mTemperature = mTemperature;
	}
	public string getRange() {
		return mRange;
	}
	public void setRange(string mRange) {
		this.mRange = mRange;
	}
	public String getUnits() {
		return mUnits;
	}
	public void setUnits(String mUnits) {
		this.mUnits = mUnits;
	}	
	
	public void update(OcRepresentation rep) {
		this.mTemperature = rep.getValue("temperature");
		this.mRange = rep.getValue("range");
		this.mUnits = rep.getValue("units");
	}
		
	public OcRepresentation serialize() {
		OcRepresentation rep = new OcRepresentation ();
		rep.setValue("temperature", this.mTemperature);
		rep.setValue("mRange", this.mRange);
		rep.setValue("mUnits", this.mUnits);
		return rep;
	}
}
