package com.vimukti.accounter.web.client.imports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public class Field<T> {

	/**
	 * Name of the Field in the File
	 */
	private String columnName;

	/**
	 * Name of the Field
	 */
	private String name;

	/**
	 * Value of the Field
	 */
	private T value;

	/**
	 * Tells whether this Field is Required or not
	 */
	private boolean isRequired;

	private String desplayName;

	public Field(String name, String displayName) {
		this(name, displayName, false);

	}

	public Field(String name, String displayName, boolean isRequired) {
		this.name = name;
		this.desplayName = displayName;
		this.isRequired = isRequired;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the isRequired
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * @param isRequired
	 *            the isRequired to set
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getDesplayName() {
		return desplayName;
	}

	public void setDesplayName(String desplayName) {
		this.desplayName = desplayName;
	}

	public boolean isValid() {
		return isRequired ? value != null : true;
	}

	public boolean validate(String value) {
		try {
			Object obj = null;
			if (this.value instanceof Integer) {
				obj = Integer.valueOf(value);
			} else if (this.value instanceof Long) {
				obj = Long.valueOf(value);
			} else if (this.value instanceof Double) {
				obj = Double.valueOf(value);
			} else if (this.value instanceof ClientFinanceDate) {
				// TODO Implemet Fully
				obj = new ClientFinanceDate(value);
			}
			this.value = (T) obj;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
