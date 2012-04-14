package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * On Production Calculation Type is used to calculate the pay value based on
 * the Production/Work down. The production data can be entered in Attendance
 * voucher against the Production type.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class ProductionPayHead extends PayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AttendanceOrProductionType productionType;

	public ProductionPayHead() {
		super(CALCULATION_TYPE_ON_PRODUCTION);
	}

	/**
	 * @return the productionType
	 */
	public AttendanceOrProductionType getProductionType() {
		return productionType;
	}

	/**
	 * @param productionType
	 *            the productionType to set
	 */
	public void setProductionType(AttendanceOrProductionType productionType) {
		this.productionType = productionType;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
