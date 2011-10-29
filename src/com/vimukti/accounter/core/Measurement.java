package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Srikanth.J
 * 
 */
public class Measurement extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private Unit defaultUnit;

	private String desctiption;

	private String name;

	private Set<Unit> units;

	public Measurement() {
		units = new HashSet<Unit>();
	}

	public Measurement(String name, String description) {
		units = new HashSet<Unit>();
		this.name = name;
		this.desctiption = description;
	}

	/**
	 * 
	 * NOTE: the 'factor' value should be pre-calculated to default measure [or]
	 * all factor values of this 'Unit' are should be calculated in one measure.
	 * 
	 * @param measureType
	 * @param factor
	 */
	public void addUnit(String unitType, double factor) {
		Unit unit = new Unit(unitType, factor);
		unit.setCompany(getCompany());
		unit.setMeasurement(this);
		units.add(unit);
	}

	public void addUnit(Unit unit) {
		unit.setMeasurement(this);
		units.add(unit);
	}

	/**
	 * Gives factor for converting given measure to default measure.
	 * 
	 * @param fromMeasure
	 * @return
	 */
	public double getConversionFactor(String fromMeasure) {
		return getConversionFactor(fromMeasure, getDefaultUnitType());
	}

	private String getDefaultUnitType() {
		for (Unit unit : this.units) {
			if (unit.isDefault()) {
				return unit.getType();
			} else
				return "";
		}
		return null;
	}

	/**
	 * This will give you the factor to convert from type to another.
	 * 
	 * <pre>
	 *  result = fromMeasureFactor/toMeasureFactor.
	 * </pre>
	 * 
	 * @param fromMeasure
	 * @param toMeasure
	 * @return
	 */
	public double getConversionFactor(String fromUnit, String toUnit) {
		return getFactor(fromUnit) / getFactor(toUnit);
	}

	//
	// public Unit getDefaultUnit() {
	// return defaultUnit;
	// }

	public String getDesctiption() {
		return desctiption;
	}

	/**
	 * 
	 * @param unitType
	 * @return
	 * @exception IllegalArgumentException
	 *                if unitType not found.
	 */
	public double getFactor(String unitType) {

		for (Unit unit : units) {
			if (unit.getType().equals(unitType)) {
				return unit.getFactor();
			}
		}

		throw new IllegalArgumentException(
				"Specified unit type not found in measure.");
	}

	public String getName() {
		return name;
	}

	public Set<Unit> getUnits() {
		return units;
	}

	/**
	 * 
	 * @param defaultMeasurement
	 * @throws IllegalStateException
	 *             defaultMeasurement may be null or not registered in this
	 *             Unit.
	 */
	// public void setDefaultUnit(Unit unit) {
	// this.defaultUnit = unit;
	// }

	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public long getID() {
		return 0;
	}

	public Measurement clone() {
		Measurement measurementClone = (Measurement) this.clone();
		Set<Unit> units = new HashSet<Unit>();
		for (Unit unit : this.units) {
			units.add(unit.clone());
		}
		measurementClone.units = units;

		return measurementClone;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return false;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		for (Unit unit : this.units) {
			unit.setMeasurement(this);
		}
		return super.onSave(session);
	}

	public Unit getDefaultUnit() {
		for (Unit unit : units) {
			if (unit.isDefault()) {
				return unit;
			}
		}
		return null;
	}
}
