package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.Date;
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

	private Date createdDate;

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

	private ArrayList<ClientInvoice> invoices;

	private ArrayList<ClientTransactionItem> transactionItems;

	private ArrayList<ClientTransaction> transactions;

	private ArrayList<ClientSalesOrder> salesOrders;

	private ArrayList<ClientPurchaseOrder> purchaseOrders;

	private ArrayList<ClientBank> banks;

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

	public ArrayList<ClientAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<ClientAccount> accounts) {
		this.accounts = accounts;
	}

	public ArrayList<ClientCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(ArrayList<ClientCustomer> customers) {
		this.customers = customers;
	}

	public ArrayList<ClientVendor> getVendors() {
		return vendors;
	}

	public void setVendors(ArrayList<ClientVendor> vendors) {
		this.vendors = vendors;
	}

	public ArrayList<ClientItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<ClientItem> items) {
		this.items = items;
	}

	public ArrayList<ClientEstimate> getQuote() {
		return quote;
	}

	public void setQuote(ArrayList<ClientEstimate> quote) {
		this.quote = quote;
	}

	public ArrayList<ClientInvoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(ArrayList<ClientInvoice> invoices) {
		this.invoices = invoices;
	}

	public ArrayList<ClientTransactionItem> getTransactionItems() {
		return transactionItems;
	}

	public void setTransactionItems(
			ArrayList<ClientTransactionItem> transactionItems) {
		this.transactionItems = transactionItems;
	}

	public ArrayList<ClientTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<ClientTransaction> transactions) {
		this.transactions = transactions;
	}

	public ArrayList<ClientSalesOrder> getSalesOrders() {
		return salesOrders;
	}

	public void setSalesOrders(ArrayList<ClientSalesOrder> salesOrders) {
		this.salesOrders = salesOrders;
	}

	public ArrayList<ClientPurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(ArrayList<ClientPurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public Map<String, String> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(Map<String, String> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyEmailForCustomers() {
		return companyEmailForCustomers;
	}

	public void setCompanyEmailForCustomers(String companyEmailForCustomers) {
		this.companyEmailForCustomers = companyEmailForCustomers;
	}

	public boolean isConfigured() {
		return isConfigured;
	}

	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	public String getEin() {
		return ein;
	}

	public void setEin(String ein) {
		this.ein = ein;
	}

	public int getFirstMonthOfFiscalYear() {
		return firstMonthOfFiscalYear;
	}

	public void setFirstMonthOfFiscalYear(int firstMonthOfFiscalYear) {
		this.firstMonthOfFiscalYear = firstMonthOfFiscalYear;
	}

	public int getFirstMonthOfIncomeTaxYear() {
		return firstMonthOfIncomeTaxYear;
	}

	public void setFirstMonthOfIncomeTaxYear(int firstMonthOfIncomeTaxYear) {
		this.firstMonthOfIncomeTaxYear = firstMonthOfIncomeTaxYear;
	}

	public int getTaxForm() {
		return taxForm;
	}

	public void setTaxForm(int taxForm) {
		this.taxForm = taxForm;
	}

	public long getBooksClosingDate() {
		return booksClosingDate;
	}

	public void setBooksClosingDate(long booksClosingDate) {
		this.booksClosingDate = booksClosingDate;
	}

	public int getClosingDateWarningType() {
		return closingDateWarningType;
	}

	public void setClosingDateWarningType(int closingDateWarningType) {
		this.closingDateWarningType = closingDateWarningType;
	}

	public boolean isEnableAccountNumbers() {
		return enableAccountNumbers;
	}

	public void setEnableAccountNumbers(boolean enableAccountNumbers) {
		this.enableAccountNumbers = enableAccountNumbers;
	}

	public int getCustomerType() {
		return customerType;
	}

	public void setCustomerType(int customerType) {
		this.customerType = customerType;
	}

	public boolean isEnableAutoRecall() {
		return enableAutoRecall;
	}

	public void setEnableAutoRecall(boolean enableAutoRecall) {
		this.enableAutoRecall = enableAutoRecall;
	}

	public boolean isRestartSetupInterviews() {
		return restartSetupInterviews;
	}

	public void setRestartSetupInterviews(boolean restartSetupInterviews) {
		this.restartSetupInterviews = restartSetupInterviews;
	}

	public int getFiscalYearStarting() {
		return fiscalYearStarting;
	}

	public void setFiscalYearStarting(int fiscalYearStarting) {
		this.fiscalYearStarting = fiscalYearStarting;
	}

	public int getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public long getAccountsReceivableAccount() {
		return accountsReceivableAccount;
	}

	public void setAccountsReceivableAccount(long accountsReceivableAccount) {
		this.accountsReceivableAccount = accountsReceivableAccount;
	}

	public long getAccountsPayableAccount() {
		return accountsPayableAccount;
	}

	public void setAccountsPayableAccount(long accountsPayableAccount) {
		this.accountsPayableAccount = accountsPayableAccount;
	}

	public long getOpeningBalancesAccount() {
		return openingBalancesAccount;
	}

	public void setOpeningBalancesAccount(long openingBalancesAccount) {
		this.openingBalancesAccount = openingBalancesAccount;
	}

	public long getRetainedEarningsAccount() {
		return retainedEarningsAccount;
	}

	public void setRetainedEarningsAccount(long retainedEarningsAccount) {
		this.retainedEarningsAccount = retainedEarningsAccount;
	}

	public long getOtherCashIncomeAccount() {
		return otherCashIncomeAccount;
	}

	public void setOtherCashIncomeAccount(long otherCashIncomeAccount) {
		this.otherCashIncomeAccount = otherCashIncomeAccount;
	}

	public long getOtherCashExpenseAccount() {
		return otherCashExpenseAccount;
	}

	public void setOtherCashExpenseAccount(long otherCashExpenseAccount) {
		this.otherCashExpenseAccount = otherCashExpenseAccount;
	}

	public long getPendingItemReceiptsAccount() {
		return pendingItemReceiptsAccount;
	}

	public void setPendingItemReceiptsAccount(long pendingItemReceiptsAccount) {
		this.pendingItemReceiptsAccount = pendingItemReceiptsAccount;
	}

	public long getCashDiscountsGiven() {
		return cashDiscountsGiven;
	}

	public void setCashDiscountsGiven(long cashDiscountsGiven) {
		this.cashDiscountsGiven = cashDiscountsGiven;
	}

	public long getCashDiscountsTaken() {
		return cashDiscountsTaken;
	}

	public void setCashDiscountsTaken(long cashDiscountsTaken) {
		this.cashDiscountsTaken = cashDiscountsTaken;
	}

	public long getTaxLiabilityAccount() {
		return taxLiabilityAccount;
	}

	public void setTaxLiabilityAccount(long taxLiabilityAccount) {
		this.taxLiabilityAccount = taxLiabilityAccount;
	}

	public long getVATFiledLiabilityAccount() {
		return VATFiledLiabilityAccount;
	}

	public void setVATFiledLiabilityAccount(long vATFiledLiabilityAccount) {
		VATFiledLiabilityAccount = vATFiledLiabilityAccount;
	}

	public Set<ClientCurrency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Set<ClientCurrency> currencies) {
		this.currencies = currencies;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public ArrayList<ClientBank> getBanks() {
		return banks;
	}

	public void setBanks(ArrayList<ClientBank> banks) {
		this.banks = banks;
	}

}
