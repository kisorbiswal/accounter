package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

public class UserPreferences implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6090587330383430576L;
	String dashBoardPreferences = "WELCOME,BANKING_SUMMARY,PROFIT_AND_LOSS,CREDIT_OVERVIEW,DEBIT_OVERVIEW,LATEST_QUOTE,EXPENSES";
	String customerSectionViewPreferences = "NEW_CUSTOMER,SALES_ITEM,PAYMENT_RECEIVED,CASH_SALES,CREDIT_AND_REFUNDS";
	String vendorSectionViewPreferences = "NEW_VENDOR,ITEM_PURCHASE,BILL_PAID,CASH_PURCHASE";
	String bankingSectionViewPreferences = "BANKING_SUMMARY,CHECK_ISSUED,DEPOSITE,FUND_TRANSFERED,CREDIT_CARD_CHARGES";

	public UserPreferences() {

	}

	/**
	 * 
	 * @return DashBoard Preferences made last time user visited
	 */
	public String getDashBoardPreferences() {
		return dashBoardPreferences;
	}

	public String getCustomerSectionViewPreferences() {
		return customerSectionViewPreferences;
	}

	public String getVendorSectionViewPreferences() {
		return vendorSectionViewPreferences;
	}

	public String getBankingSectionViewPreferences() {
		return bankingSectionViewPreferences;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {

		return true;
	}

}
