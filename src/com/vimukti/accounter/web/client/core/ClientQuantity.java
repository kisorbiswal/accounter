/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientQuantity implements IsSerializable, Serializable, Cloneable,
		Comparable<ClientQuantity> {

	private ClientUnit unit;
	private double value;

	/**
	 * @return the unit
	 */
	public ClientUnit getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(ClientUnit unit) {
		this.unit = unit;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int compareTo(ClientQuantity o) {
		// TODO verify this method later
		ClientQuantity thisQty = convertToDefaultUnit();
		ClientQuantity otherQty = o.convertToDefaultUnit();
		return (int) (thisQty.getValue() - otherQty.getValue());
	}

	/**
	 * Converts the present quantity into default measurement.
	 * 
	 * @return
	 */
	private ClientQuantity convertToDefaultUnit() {
		double conversionFactor = unit == null ? 1 : unit.getMeasurement()
				.getConversionFactor(unit.getType());

		ClientQuantity quantity = new ClientQuantity();
		quantity.setValue(value * conversionFactor);
		if (unit != null)
			quantity.setUnit(unit.getMeasurement().getDefaultUnit());

		return quantity;
	}

	public ClientQuantity clone() {
		ClientQuantity quantity = (ClientQuantity) this.clone();
		quantity.unit = this.unit.clone();
		return quantity;
	}

}
