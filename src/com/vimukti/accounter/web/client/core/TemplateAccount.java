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
public class TemplateAccount implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;

	private String name;

	private boolean defaultValue;

	private boolean isSystemOnly;

	private String[] contries;

	private String cashFlowType;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the defaultValue
	 */
	public boolean getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the isSystemOnly
	 */
	public boolean isSystemOnly() {
		return isSystemOnly;
	}

	/**
	 * @param isSystemOnly
	 *            the isSystemOnly to set
	 */
	public void setSystemOnly(boolean isSystemOnly) {
		this.isSystemOnly = isSystemOnly;
	}

	/**
	 * @return the contries
	 */
	public String[] getContries() {
		return contries;
	}

	/**
	 * @param contries
	 *            the contries to set
	 */
	public void setContries(String[] contries) {
		this.contries = contries;
	}

	/**
	 * @return the cashFlowType
	 */
	public String getCashFlowType() {
		return cashFlowType;
	}

	/**
	 * @param cashFlowType
	 *            the cashFlowType to set
	 */
	public void setCashFlowType(String cashFlowType) {
		this.cashFlowType = cashFlowType;
	}

	public int getCashFlowAsInt() {
		if (cashFlowType.equals("Financing")) {
			return ClientAccount.CASH_FLOW_CATEGORY_FINANCING;
		} else if (cashFlowType.equals("Investing")) {
			return ClientAccount.CASH_FLOW_CATEGORY_INVESTING;
		} else if (cashFlowType.equals("Operating")) {
			return ClientAccount.CASH_FLOW_CATEGORY_OPERATING;
		} else {
			return ClientAccount.CASH_FLOW_CATEGORY_FINANCING;
		}
	}

	/**
	 * @return
	 */
	public int getTypeAsInt() {
		// TODO Auto-generated method stub
		return 0;
	}

}
