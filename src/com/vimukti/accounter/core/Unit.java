package com.vimukti.accounter.core;


public class Unit implements IAccounterServerCore, Cloneable {
	private long id;

	private Measurement measurement;
	private String type;
	private double factor;

	private int version;

	public Unit() {
	}

	public Unit(String type, double factor) {
		setType(type);
		setFactor(factor);
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public long getId() {
		return id;
	}

	public Unit clone() {
		Unit unitClone = (Unit) this.clone();
		return unitClone;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
		
	}
}
