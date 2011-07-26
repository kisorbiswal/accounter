package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Srikanth.J
 * 
 */
public class Measurement {

	private Unit defaultUnit;

	private String desctiption;

	private long id;

	private String name;
	private Set<Unit> units;

	public Measurement() {
		units = new HashSet<Unit>();
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
		return getConversionFactor(fromMeasure, defaultUnit.getType());
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

	public Unit getDefaultUnit() {
		return defaultUnit;
	}

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

	public long getId() {
		return id;
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
	public void setDefaultUnit(Unit unit) {
		this.defaultUnit = unit;
	}

	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
