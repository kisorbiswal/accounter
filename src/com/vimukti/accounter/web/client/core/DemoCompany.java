package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DemoCompany implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	public static final int ACCOUNTING_TYPE_OTHER = 3;

	public static final int TYPE_BASIC = 8;

	private Map<String, String> paymentMethods = new HashMap<String, String>();

	// int accountingType = 0;

	private String registrationNumber;

	private String companyEmail;

	private String companyEmailForCustomers;

	boolean isConfigured;

	private String ein;

	private int firstMonthOfFiscalYear;

	private int firstMonthOfIncomeTaxYear;

	private int taxForm;

	private long booksClosingDate;

	private int closingDateWarningType;

	private boolean enableAccountNumbers;

	private int customerType;

	private boolean enableAutoRecall;

	boolean restartSetupInterviews;

	private int fiscalYearStarting;

	private int industry;

	private long accountsReceivableAccount;

	private long accountsPayableAccount;

	private long openingBalancesAccount;

	private long retainedEarningsAccount;

	private long otherCashIncomeAccount;

	private long otherCashExpenseAccount;

	private long pendingItemReceiptsAccount;

	private long cashDiscountsGiven;

	private long cashDiscountsTaken;

	private long taxLiabilityAccount;

	private long VATFiledLiabilityAccount;

	private Set<ClientCurrency> currencies;

	String bankAccountNo;

	String sortCode;

	ClientCompanyPreferences preferences = new ClientCompanyPreferences();

	private ArrayList<ClientAccount> accounts;

	private ArrayList<ClientCustomer> customers;

	private ArrayList<ClientVendor> vendors;

	private ArrayList<ClientItem> items;

	private ArrayList<ClientEstimate> quote;

	private ArrayList<ClientInvoice> invoice;

	private ArrayList<ClientTransactionItem> transactionItems;

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

}
