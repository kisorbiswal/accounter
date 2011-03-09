package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptException;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

@SuppressWarnings("serial")
public class ClientCompany implements IAccounterCore {

	private static final int TYPE_SOLE_PROPRIETORSHIP = 1;
	private static final int TYPE_PARTNERSHIP_OR_LLP = 2;
	private static final int TYPE_LLC_SINGLE_MEMBER = 3;
	private static final int TYPE_LLC_MULTI_MEMBER = 4;
	private static final int TYPE_CORPORATION = 5;
	private static final int TYPE_S_CORPORATION = 6;
	private static final int TYPE_NON_PROFIT = 7;
	public static final int TYPE_BASIC = 8;
	private static final int TYPE_OTHER_NONE = 99999;

	private static final int INDUSTRY_ACCOUNTING_OR_BOOKKEEPING = 11;
	private static final int ADVERTISING_OR_private_RELATIONS = 12;
	private static final int AGRICULTURE_RANCHING_OR_FARMING = 13;
	private static final int ARTWRITING_OR_PHOTOGRAPHY = 14;
	private static final int AUTOMOTICE_SALES_OR_REPAIR = 15;

	private static final int CUSTOMER_TYPE_CLIENTS = 45;
	private static final int CUSTOMER_TYPE_CUSTOMERS = 46;
	private static final int CUSTOMER_TYPE_DONORS = 47;
	private static final int CUSTOMER_TYPE_GUESTS = 48;
	private static final int CUSTOMER_TYPE_MEMBERS = 49;
	private static final int CUSTOMER_TYPE_PATIENTS = 50;
	private static final int CUSTOMER_TYPE_TENANTS = 51;

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;

	public static final String CHECK = "Check";
	public static final String CHECK_FOR_UK = "Cheque";
	public static final String CREDIT_CARD = "Credit Card";
	public static final String CASH = "Cash";

	private Map<String, String> paymentMethods = new HashMap<String, String>();

	int accountingType = 0;

	public String stringID;

	String name;

	String legalName;

	private String registrationNumber;

	String companyEmail;

	String companyEmailForCustomers;

	boolean isConfigured;

	ClientContact contact = new ClientContact(); // ClientContact
	// String contact;

	String ein;

	int firstMonthOfFiscalYear;

	int firstMonthOfIncomeTaxYear;

	int taxForm;

	long booksClosingDate;

	int closingDateWarningType;

	boolean enableAccountNumbers;

	int customerType;

	boolean enableAutoRecall;

	boolean restartSetupInterviews;

	String taxId;

	int fiscalYearStarting;

	int industry;

	String accountsReceivableAccount;

	String accountsPayableAccount;

	String openingBalancesAccount;

	String retainedEarningsAccount;

	String otherCashIncomeAccount;

	String otherCashExpenseAccount;

	String VATliabilityAccount;

	String pendingItemReceiptsAccount;
	// String prepaidVATaccount;
	// String ECAcquisitionVATaccount;

	String phone;

	String fax;

	String webSite;

	String bankAccountNo;

	String sortCode;

	String serviceItemDefaultIncomeAccount = "Cash Discount Given";

	String serviceItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	String nonInventoryItemDefaultIncomeAccount = "Cash Discount Given";

	String nonInventoryItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	String ukServiceItemDefaultIncomeAccount = "Early Payment Discount Given";
	String ukServiceItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	String ukNonInventoryItemDefaultIncomeAccount = "Early Payment Discount Given";

	String ukNonInventoryItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	ClientCompanyPreferences preferences = new ClientCompanyPreferences();

	private List<ClientAddress> addresses;

	private List<ClientAccount> accounts;

	private List<ClientCustomer> customers;

	private List<ClientVendor> vendors;

	// private List<ClientTaxAgency> taxAgencies;

	private List<ClientItem> items;

	private List<ClientCustomerGroup> customerGroups;

	private List<ClientVendorGroup> vendorGroups;

	private List<ClientPaymentTerms> paymentTerms;

	private List<ClientShippingTerms> shippingTerms;

	private List<ClientShippingMethod> shippingMethods;

	private List<ClientPriceLevel> priceLevels;

	private List<ClientItemGroup> ItemGroups;

	private List<ClientTAXGroup> taxGroups;

	private List<ClientPaySalesTax> paySalesTaxs;

	private List<ClientCreditRating> creditRatings;

	private List<ClientSalesPerson> salesPersons;

	// private List<ClientTaxCode> taxCodes;

	private List<ClientTAXItemGroup> taxItemGroups;

	private List<ClientPayee> payees;

	private List<ClientFiscalYear> fiscalYears;
	private List<ClientBank> banks;

	private List<ClientFixedAsset> fixedAssets;

	// private List<ClientSellingOrDisposingFixedAsset> sellingDisposedItems;

	private List<ClientVATReturn> vatReturns;

	private List<ClientTAXAgency> taxAgencies;

	private List<ClientTAXCode> taxCodes;

	// private List<ClientTAXItemGroup> vatItemGroups;

	Set<ClientNominalCodeRange> nominalCodeRange = new HashSet<ClientNominalCodeRange>();

	public void setTaxItemGroups(List<ClientTAXItemGroup> taxItemGroups) {
		this.taxItemGroups = taxItemGroups;
	}

	// public void setVatItemGroups(List<ClientTAXItemGroup> vatItemGroups) {
	// this.vatItemGroups = vatItemGroups;
	// }
	//
	// public List<ClientTAXItemGroup> getVatItemGroups() {
	// return vatItemGroups;
	// }

	public void setTaxCodes(List<ClientTAXCode> TaxCodes) {
		this.taxCodes = TaxCodes;
	}

	public List<ClientTAXCode> getTaxCodes() {
		return taxCodes;
	}

	public List<ClientTAXCode> getActiveTaxCodes() {
		List<ClientTAXCode> activeTaxCodes = new ArrayList<ClientTAXCode>();
		for (ClientTAXCode taxCode : taxCodes) {
			if (taxCode.isActive())
				activeTaxCodes.add(taxCode);
		}
		return Utility.getArrayList(activeTaxCodes);
	}

	public void setTaxAgencies(List<ClientTAXAgency> taxAgencies) {
		this.taxAgencies = taxAgencies;
	}

	public List<ClientTAXAgency> getTaxAgencies() {
		return taxAgencies;
	}

	public List<ClientTAXAgency> getActiveTAXAgencies() {
		List<ClientTAXAgency> activeTaxAgencies = new ArrayList<ClientTAXAgency>();
		for (ClientTAXAgency taxAgency : taxAgencies) {
			if (taxAgency.isActive())
				activeTaxAgencies.add(taxAgency);
		}
		return Utility.getArrayList(activeTaxAgencies);
	}

	/**
	 * @return the vatReturns
	 */
	public List<ClientVATReturn> getVatReturns() {
		return vatReturns;
	}

	/**
	 * @param vatReturns
	 *            the vatReturns to set
	 */
	public void setVatReturns(List<ClientVATReturn> vatReturns) {
		this.vatReturns = vatReturns;
	}

	/**
	 * @return the vatReturns
	 */
	public ClientVATReturn getVatReturn(String vatReturnID) {
		return Utility.getObject(this.vatReturns, vatReturnID);
	}

	/**
	 * @return the vatAdjustments
	 */
	public List<ClientTAXAdjustment> getTaxAdjustments() {
		return taxAdjustments;
	}

	/**
	 * @param taxAdjustments
	 *            the vatAdjustments to set
	 */
	public void setTaxAdjustments(List<ClientTAXAdjustment> taxAdjustments) {
		this.taxAdjustments = taxAdjustments;
	}

	/**
	 * @return the vatGroups
	 */
	public List<ClientTAXGroup> getVatGroups() {
		return vatGroups;
	}

	/**
	 * @param vatGroups
	 *            the vatGroups to set
	 */
	public void setVatGroups(List<ClientTAXGroup> vatGroups) {
		this.vatGroups = vatGroups;
	}

	// /**
	// * @return the vatItems
	// */
	// public List<ClientTAXItem> getVatItems() {
	// return vatItems;
	// }

	public Set<ClientNominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	public String getPendingItemReceiptsAccount() {
		return pendingItemReceiptsAccount;
	}

	public void setPendingItemReceiptsAccount(String pendingItemReceiptsAccount) {
		this.pendingItemReceiptsAccount = pendingItemReceiptsAccount;
	}

	// public List<ClientTAXItem> getActiveVatItems() {
	// List<ClientTAXItem> activeVatItems = new ArrayList<ClientTAXItem>();
	// for (ClientTAXItem vatitem : vatItems) {
	// if (vatitem.isActive())
	// activeVatItems.add(vatitem);
	// }
	// return Utility.getArrayList(activeVatItems);
	// }

	public void setNominalCodeRange(Set<ClientNominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
	}

	// /**
	// *
	// * @param vatAgency
	// * @return
	// */
	// public List<ClientTAXItem> getVatItems(ClientTAXAgency taxAgency) {
	//
	// List<ClientTAXItem> vatItems = new ArrayList<ClientTAXItem>();
	//
	// if (this.vatItems == null) {
	// return vatItems;
	// }
	//
	// if (taxAgency == null) {
	// return vatItems;
	// }
	//
	// for (ClientTAXItem clientVATItem : getVatItems()) {
	//
	// if (clientVATItem.getTaxAgency().equals(taxAgency.getStringID())) {
	// vatItems.add(clientVATItem);
	// }
	// }
	//
	// return vatItems;
	// }

	// /**
	// * @param vatItems
	// * the vatItems to set
	// */
	// public void setVatItems(List<ClientTAXItem> vatItems) {
	// this.vatItems = vatItems;
	// }

	public List<ClientTAXItem> getActiveTaxItems() {
		List<ClientTAXItem> activeTaxItems = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem taxItem : taxItems) {
			if (taxItem.isActive())
				activeTaxItems.add(taxItem);
		}
		return Utility.getArrayList(activeTaxItems);
	}

	public List<ClientTAXItem> getTaxItems() {
		return taxItems;
	}

	public List<ClientTAXItem> getTaxItems(ClientTAXAgency taxAgency) {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();

		if (this.taxItems == null) {
			return taxItems;
		}
		if (taxAgency == null) {
			return taxItems;
		}
		for (ClientTAXItem clientTaxItem : getTaxItems()) {
			if (clientTaxItem.getTaxAgency().equals(taxAgency.getStringID())) {
				taxItems.add(clientTaxItem);

			}
		}
		return taxItems;

	}

	// public List<ClientTaxItem> getTaxItems(ClientTaxAgency taxAgency) {
	// List<ClientTaxItem> taxItems = new ArrayList<ClientTaxItem>();
	//
	// if (this.taxItems == null) {
	// return taxItems;
	// }
	// if (taxAgency == null) {
	// return taxItems;
	// }
	// for (ClientTaxItem clientTaxItem : getTaxItems()) {
	// if (clientTaxItem.getTaxAgency().equals(taxAgency.getStringID())) {
	// taxItems.add(clientTaxItem);
	//
	// }
	// }
	// return taxItems;
	//
	// }

	public void setTaxItems(List<ClientTAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	private List<ClientTAXAdjustment> taxAdjustments;

	private List<ClientTAXGroup> vatGroups;
	private List<ClientTAXItem> taxItems;

	// private List<ClientTAXItem> vatItems;
	private List<ClientVATReturnBox> vatReturnBoxes;

	// private List<ClientTaxItem> taxItems;

	public ClientCompany() {
		paymentMethods.put("1", "Cash");
		paymentMethods.put("2", "Check");
		paymentMethods.put("3", "Credit Card");
	}

	// List<ClientPayType> payTypes;

	public List<ClientCustomer> getCustomers() {
		return Utility.getArrayList(customers);
	}

	public List<ClientSalesPerson> getsalesPerson() {
		return Utility.getArrayList(salesPersons);
	}

	public List<ClientCustomer> getActiveCustomers() {
		List<ClientCustomer> activeCustomers = new ArrayList<ClientCustomer>();
		for (ClientCustomer customer : customers) {
			if (customer.isActive())
				activeCustomers.add(customer);
		}
		return Utility.getArrayList(activeCustomers);
	}

	public List<ClientBank> getBanks() {
		return Utility.getArrayList(banks);
	}

	public void setBanks(List<ClientBank> banks) {
		this.banks = banks;
	}

	public List<ClientPayee> getPayees() {
		List<ClientPayee> temp = new ArrayList<ClientPayee>();
		for (ClientPayee payee : payees) {
			if (payee instanceof ClientSalesPerson) {
				temp.add(payee);
			}
		}
		payees.removeAll(temp);
		return Utility.getArrayList(payees);
	}

	public List<ClientPayee> getActivePayees() {
		List<ClientPayee> activePayees = new ArrayList<ClientPayee>();
		for (ClientPayee payee : getPayees()) {
			if (payee.isActive())
				activePayees.add(payee);
		}
		return Utility.getArrayList(activePayees);
	}

	public void setCustomers(List<ClientCustomer> customers) {
		this.customers = customers;
	}

	public List<ClientFiscalYear> getFiscalYears() {
		return Utility.getArrayList(fiscalYears);
	}

	public void setFiscalYears(List<ClientFiscalYear> fiscalYears) {
		this.fiscalYears = fiscalYears;
	}

	public List<ClientVendor> getVendors() {
		return Utility.getArrayList(vendors);
	}

	public List<ClientVendor> getActiveVendors() {
		List<ClientVendor> activeVendors = new ArrayList<ClientVendor>();
		for (ClientVendor vendor : vendors) {
			if (vendor.isActive())
				activeVendors.add(vendor);
		}
		return Utility.getArrayList(activeVendors);
	}

	public void setVendors(List<ClientVendor> vendors) {
		this.vendors = vendors;
	}

	public ClientContact getContact() {
		return contact;
	}

	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	// public List<ClientTAXAgency> getTaxAgencies() {
	// return Utility.getArrayList(taxAgencies);
	// }
	//
	// public void setTaxAgencies(List<ClientTAXAgency> taxAgencies) {
	// this.taxAgencies = taxAgencies;
	// }

	public ClientCompanyPreferences getpreferences() {
		return preferences;
	}

	public void setpreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

	public List<ClientItem> getItems() {
		return Utility.getArrayList(items);
	}

	public List<ClientItem> getServiceItems() {
		List<ClientItem> serviceitems = new ArrayList<ClientItem>();
		for (ClientItem item : getActiveItems()) {
			if (item.getType() == ClientItem.TYPE_SERVICE)
				serviceitems.add(item);

		}
		return Utility.getArrayList(serviceitems);
	}

	public List<ClientItem> getProductItems() {
		List<ClientItem> productitems = new ArrayList<ClientItem>();
		for (ClientItem item : getActiveItems()) {
			if (item.getType() != ClientItem.TYPE_SERVICE)
				productitems.add(item);

		}
		return Utility.getArrayList(productitems);
	}

	public List<ClientItem> getPurchaseItems() {
		List<ClientItem> purchaseitems = new ArrayList<ClientItem>();
		for (ClientItem item : getActiveItems()) {
			if (item.isIBuyThisItem)
				purchaseitems.add(item);

		}
		return Utility.getArrayList(purchaseitems);
	}

	public List<ClientItem> getSalesItems() {
		List<ClientItem> salesitems = new ArrayList<ClientItem>();
		for (ClientItem item : getActiveItems()) {
			if (item.isISellThisItem)
				salesitems.add(item);

		}
		return Utility.getArrayList(salesitems);
	}

	public List<ClientItem> getActiveItems() {
		List<ClientItem> activeItems = new ArrayList<ClientItem>();
		for (ClientItem item : items) {
			// if (item.isActive())
			activeItems.add(item);
		}
		return Utility.getArrayList(activeItems);
	}

	public void setItems(List<ClientItem> items) {
		this.items = items;
	}

	public List<ClientCustomerGroup> getCustomerGroups() {
		return Utility.getArrayList(customerGroups);
	}

	public void setCustomerGroups(List<ClientCustomerGroup> customerGroups) {
		this.customerGroups = customerGroups;
	}

	public List<ClientVendorGroup> getVendorGroups() {
		return Utility.getArrayList(vendorGroups);
	}

	public void setVendorGroups(List<ClientVendorGroup> vendorGroups) {
		this.vendorGroups = vendorGroups;
	}

	public List<ClientPaymentTerms> getPaymentsTerms() {
		return Utility.getArrayList(paymentTerms);
	}

	public Map<String, String> getPaymentMethods() {

		return paymentMethods;
	}

	public void setPaymentsTerms(List<ClientPaymentTerms> paymentsTerms) {
		this.paymentTerms = paymentsTerms;
	}

	public List<ClientShippingTerms> getShippingTerms() {
		return Utility.getArrayList(shippingTerms);
	}

	public void setShippingTerms(List<ClientShippingTerms> shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public List<ClientShippingMethod> getShippingMethods() {
		return Utility.getArrayList(shippingMethods);
	}

	public void setShippingMethods(List<ClientShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public List<ClientPriceLevel> getPriceLevels() {
		return Utility.getArrayList(priceLevels);
	}

	public List<ClientTAXItemGroup> getTaxItemGroups() {
		return Utility.getArrayList(taxItemGroups);
	}

	public void setPriceLevels(List<ClientPriceLevel> priceLevels) {
		this.priceLevels = priceLevels;
	}

	public List<ClientItemGroup> getItemGroups() {
		return Utility.getArrayList(ItemGroups);
	}

	public void setItemGroups(List<ClientItemGroup> itemGroups) {
		ItemGroups = itemGroups;
	}

	// public List<ClientTaxCode> getTaxcodes() {
	// return Utility.getArrayList(taxCodes);
	// }
	// public List<ClientTaxCode> getActiveTaxCodes() {
	// List<ClientTaxCode> activeTaxCodes = new ArrayList<ClientTaxCode>();
	// for (ClientTaxCode taxCode : taxCodes) {
	// if (taxCode.getIsActive())
	// activeTaxCodes.add(taxCode);
	// }
	// return Utility.getArrayList(activeTaxCodes);
	// }
	//
	// public void setTaxcodes(List<ClientTaxCode> taxcodes) {
	// this.taxCodes = taxcodes;
	// }

	public List<ClientTAXGroup> getTaxGroups() {
		return Utility.getArrayList(taxGroups);
	}

	public void setTaxGroups(List<ClientTAXGroup> taxGroups) {
		this.taxGroups = taxGroups;
	}

	public List<ClientTAXAgency> gettaxAgencies() {
		return Utility.getArrayList(taxAgencies);
	}

	public List<ClientTAXAgency> getActiveTaxAgencies() {
		List<ClientTAXAgency> activeTaxAgencies = new ArrayList<ClientTAXAgency>();
		for (ClientTAXAgency taxAgency : taxAgencies) {
			if (taxAgency.isActive())
				activeTaxAgencies.add(taxAgency);
		}
		return Utility.getArrayList(activeTaxAgencies);
	}

	public void settaxAgencies(List<ClientTAXAgency> taxAgencies) {
		this.taxAgencies = taxAgencies;
	}

	public List<ClientPaySalesTax> getPaySalesTaxs() {
		return Utility.getArrayList(paySalesTaxs);
	}

	public void setPaySalesTaxs(List<ClientPaySalesTax> paySalesTaxs) {
		this.paySalesTaxs = paySalesTaxs;
	}

	public List<ClientCreditRating> getCreditRatings() {
		return Utility.getArrayList(creditRatings);
	}

	public void setCreditRatings(List<ClientCreditRating> creditRatings) {
		this.creditRatings = creditRatings;
	}

	public List<ClientSalesPerson> getSalesPersons() {
		return Utility.getArrayList(salesPersons);
	}

	public List<ClientSalesPerson> getActiveSalesPersons() {
		List<ClientSalesPerson> activeSalesPersons = new ArrayList<ClientSalesPerson>();
		for (ClientSalesPerson salesPerson : salesPersons) {
			if (salesPerson.isActive())
				activeSalesPersons.add(salesPerson);
		}
		return Utility.getArrayList(activeSalesPersons);
	}

	public void setSalesPersons(List<ClientSalesPerson> salesPersons) {
		this.salesPersons = salesPersons;
	}

	// public void setTaxCodes(List<ClientTaxCode> taxCodes) {
	// this.taxCodes = taxCodes;
	// }

	public String getCompanyEmailForCustomers() {
		return companyEmailForCustomers;
	}

	public void setCompanyEmailForCustomers(String companyEmailForCustomers) {
		this.companyEmailForCustomers = companyEmailForCustomers;
	}

	// public void setContact(String contact) {
	// this.contact = contact;
	// }

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

	public String getAccountsReceivableAccountId() {
		return accountsReceivableAccount;
	}

	public void setAccountsReceivableAccount(String accountsReceivableAccount) {
		this.accountsReceivableAccount = accountsReceivableAccount;
	}

	public String getAccountsPayableAccount() {
		return accountsPayableAccount;
	}

	public void setAccountsPayableAccountId(String accountsPayableAccount) {
		this.accountsPayableAccount = accountsPayableAccount;
	}

	public String getOpeningBalancesAccount() {
		return openingBalancesAccount;
	}

	public void setOpeningBalancesAccountId(String openingBalancesAccount) {
		this.openingBalancesAccount = openingBalancesAccount;
	}

	public String getRetainedEarningsAccount() {
		return retainedEarningsAccount;
	}

	public void setRetainedEarningsAccount(String retainedEarningsAccount) {
		this.retainedEarningsAccount = retainedEarningsAccount;
	}

	public String getOtherCashIncomeAccount() {
		return otherCashIncomeAccount;
	}

	public void setOtherCashIncomeAccount(String otherCashIncomeAccount) {
		this.otherCashIncomeAccount = otherCashIncomeAccount;
	}

	public String getOtherCashExpenseAccount() {
		return otherCashExpenseAccount;
	}

	public void setOtherCashExpenseAccount(String otherCashExpenseAccount) {
		this.otherCashExpenseAccount = otherCashExpenseAccount;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public String getName() {

		return this.name;
	}

	/**
	 * Called Later at Process Command
	 * 
	 * @param accounts
	 */

	/**
	 * @return the accounts
	 */

	public void setAccounts(List<ClientAccount> accounts) {
		this.accounts = accounts;
	}

	public List<ClientAccount> getAccounts() {
		return Utility.getArrayList(accounts);
	}

	public List<ClientAccount> getActiveAccounts() {
		List<ClientAccount> activeAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : accounts) {
			if (account.getIsActive())
				activeAccounts.add(account);
		}
		return Utility.getArrayList(activeAccounts);
	}

	public ClientCompanyPreferences getPreferences() {
		return this.preferences;
	}

	public String getTradingName() {
		return this.legalName;
	}

	public String getTaxId() {

		return this.taxId;
	}

	public String getFax() {
		return this.fax;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getWebSite() {
		return this.webSite;
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

	public String getCompanyEmail() {
		return this.companyEmail;
	}

	public List<ClientAddress> getAddresses() {
		return this.addresses;
	}

	// public String getContact() {
	// return contact;
	// }

	public void setName(String stringValue) {
		this.name = stringValue;
	}

	public void setTradingName(String stringValue) {
		this.legalName = stringValue;
	}

	public void setPhone(String stringValue) {
		this.phone = stringValue;
	}

	public void setCompanyEmail(String stringValue) {
		this.companyEmail = stringValue;
	}

	public void setTaxId(String stringValue) {
		this.taxId = stringValue;
	}

	public void setFax(String stringValue) {
		this.fax = stringValue;
	}

	public void setWebSite(String stringValue) {
		this.webSite = stringValue;
	}

	public void setAddresses(List<ClientAddress> addresss) {

		this.addresses = addresss;
	}

	public void setIndustry(int typeBasic) {
		this.industry = typeBasic;

	}

	public int getIndustry() {
		return this.industry;
	}

	public ClientPaymentTerms getPaymentTerms(String paymentTermsId) {

		return Utility.getObject(this.paymentTerms, paymentTermsId);
	}

	public ClientPaySalesTax getPaySalesTax(String paysalesTaxId) {

		return Utility.getObject(this.paySalesTaxs, paysalesTaxId);
	}

	public ClientCustomerGroup getCustomerGroup(String customerGroupId) {

		return Utility.getObject(this.customerGroups, customerGroupId);
	}

	public ClientVendorGroup getVendorGroup(String vendorGroupId) {

		return Utility.getObject(this.vendorGroups, vendorGroupId);
	}

	public ClientShippingMethod getShippingMethod(String shippingMethodId) {

		return Utility.getObject(this.shippingMethods, shippingMethodId);
	}

	public ClientShippingTerms getShippingTerms(String shippingTermsId) {

		return Utility.getObject(this.shippingTerms, shippingTermsId);
	}

	public ClientItemGroup getItemGroup(String itemGroupId) {

		return Utility.getObject(this.ItemGroups, itemGroupId);
	}

	public ClientCreditRating getCreditRating(String creditRatingId) {

		return Utility.getObject(this.creditRatings, creditRatingId);
	}

	public ClientTAXAgency getTaxAgency(String taxAgencyId) {

		return Utility.getObject(this.taxAgencies, taxAgencyId);
	}

	// public ClientTaxCode getTaxCode(String taxCodeId) {
	//
	// return Utility.getObject(this.taxCodes, taxCodeId);
	// }

	public ClientCustomer getCustomer(String customerId) {

		return Utility.getObject(this.customers, customerId);
	}

	public ClientCustomer getCustomerByName(String customerName) {
		return Utility.getObjectByName(this.customers, customerName);
	}

	public ClientBank getBank(String bankId) {

		return Utility.getObject(this.banks, bankId);
	}

	public ClientVendor getVendor(String vendorId) {

		return Utility.getObject(this.vendors, vendorId);
	}

	public ClientVendor getVendorByName(String vendorName) {

		return Utility.getObjectByName(this.vendors, vendorName);
	}

	public ClientItem getItem(String itemId) {

		return Utility.getObject(this.items, itemId);
	}

	public ClientAccount getAccount(String accountId) {

		return Utility.getObject(this.accounts, accountId);
	}

	public ClientAccount getAccountByName(String accountName) {

		return Utility.getObjectByName(this.accounts, accountName);
	}

	public ClientAddress getAddress(String addressId) {

		return Utility.getObject(this.addresses, addressId);
	}

	public ClientTAXGroup getTaxGroup(String taxGroupId) {

		return Utility.getObject(this.taxGroups, taxGroupId);
	}

	public ClientTAXItem getTaxItem(String taxGroupId) {

		return Utility.getObject(this.taxItems, taxGroupId);
	}

	public ClientSalesPerson getSalesPerson(String salesPersonId) {

		return Utility.getObject(this.salesPersons, salesPersonId);
	}

	public ClientFixedAsset getFixedAsset(String fixedAssetId) {

		return Utility.getObject(this.fixedAssets, fixedAssetId);
	}

	public ClientTAXCode getTAXCode(String taxCodeId) {

		return Utility.getObject(this.taxCodes, taxCodeId);
	}

	// public ClientTAXItemGroup getVATItemGroup(String vatItemGrpId) {
	//
	// return Utility.getObject(this.vatItemGroups, vatItemGrpId);
	// }

	public ClientTAXItemGroup getTAXItemGroup(String taxItemGrpId) {

		return Utility.getObject(this.taxItemGroups, taxItemGrpId);
	}

	public ClientPriceLevel getPriceLevel(String priceLevelId) {
		return Utility.getObject(this.priceLevels, priceLevelId);
	}

	public ClientTAXItem getTAXItem(String stringID) {
		return Utility.getObject(this.taxItems, stringID);
	}

	// public ClientTAXAgency getTaxAgency(String stringID) {
	// return Utility.getObject(this.taxAgencies, stringID);
	// }

	public ClientVATReturnBox getVatReturnBox(String stringID) {
		return Utility.getObject(this.vatReturnBoxes, stringID);
	}

	public ClientFiscalYear getFixelYear(String stringID) {
		return Utility.getObject(this.fiscalYears, stringID);
	}

	public ClientTAXAgency getVatAgencyByName(String name) {
		return Utility.getObjectByName(this.taxAgencies, name);
	}

	public void deleteSalesPerson(String salesPersonId) {
		this.salesPersons.remove(this.getSalesPerson(salesPersonId));
	}

	public void deletePaymentTerms(String paymentTermsId) {
		this.paymentTerms.remove(this.getPaymentTerms(paymentTermsId));
	}

	public void deletePriceLevel(String priceLevelId) {
		this.priceLevels.remove(this.getPriceLevel(priceLevelId));
	}

	public void deleteCustomerGroup(String customerGroup) {
		this.customerGroups.remove(this.getCustomerGroup(customerGroup));
	}

	public void deleteVendorGroup(String vendorGroup) {
		this.vendorGroups.remove(this.getVendorGroup(vendorGroup));
	}

	public void deleteShippingMethod(String shippingMethod) {
		this.shippingMethods.remove(this.getShippingMethod(shippingMethod));
	}

	public void deleteShippingTerms(String shippingTerm) {
		this.shippingTerms.remove(this.getShippingTerms(shippingTerm));
	}

	public void deleteItemGroup(String itemGroup) {
		this.ItemGroups.remove(this.getItemGroup(itemGroup));
	}

	public void deleteCreditRating(String creditRating) {
		this.creditRatings.remove(this.getCreditRating(creditRating));
	}

	// public void deleteTaxAgency(String taxAgency) {
	// this.taxAgencies.remove(this.getTaxAgency(taxAgency));
	// }

	// public void deleteTaxCode(String taxCode) {
	// this.taxCodes.remove(this.getTaxCode(taxCode));
	// }

	public void deleteCustomer(String customerId) {
		this.customers.remove(this.getCustomer(customerId));
	}

	public void deleteVendor(String vendorId) {
		this.vendors.remove(this.getVendor(vendorId));
	}

	public void deleteItem(String itemId) {
		this.items.remove(this.getItem(itemId));
	}

	public void deleteAccount(String accountId) {
		this.accounts.remove(this.getAccount(accountId));
	}

	public void deleteAddress(String addressID) {
		this.addresses.remove(this.getAddress(addressID));
	}

	public void deleteTaxGroup(String taxGroup) {
		this.taxGroups.remove(this.getTaxGroup(taxGroup));
	}

	public void deleteBank(String bankId) {
		this.banks.remove(this.getBank(bankId));
	}

	public void deleteTaxCode(String taxCode) {
		this.taxCodes.remove(this.getTAXCode(taxCode));
	}

	// public void deleteVatGroup(String vatGroup) {
	// this.vatGroups.remove(this.getVATItem(vatGroup));
	// }

	public void deleteTaxItem(String taxItem) {
		this.taxItems.remove(this.getTAXItem(taxItem));
	}

	public void deleteTaxAgency(String taxAgencyId) {
		this.taxAgencies.remove(this.getTaxAgency(taxAgencyId));
	}

	public void deleteVAtReturn(String vatReturnId) {
		this.vatReturns.remove(this.getVatReturnBox(vatReturnId));
	}

	public void deleteFixelYear(String fixelYearId) {
		this.fiscalYears.remove(this.getFixelYear(fixelYearId));
	}

	/**
	 * 
	 * @param accounterCoreObject
	 */

	public void processCommand(IAccounterCore accounterCoreObject) {

		if (accounterCoreObject == null)
			return;

		if (accounterCoreObject instanceof AccounterCommand) {
			AccounterCommand command = (AccounterCommand) accounterCoreObject;
			switch (command.command) {
			case AccounterCommand.CREATION_SUCCESS:
			case AccounterCommand.UPDATION_SUCCESS:
				processUpdateOrCreateObject(command);
				break;
			case AccounterCommand.DELETION_SUCCESS:
				processDeleteObject(command);
				return;
			default:
				break;
			}

		} else if (accounterCoreObject.getObjectType() == AccounterCoreType.ERROR) {
			MainFinanceWindow.getViewManager().operationFailed(
					(InvalidOperationException) accounterCoreObject);
			return;

		}

	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.COMPANY;
	}

	public int getAccountingType() {
		return accountingType;
	}

	public void setAccountingType(int accountingType) {
		this.accountingType = accountingType;
	}

	public String getServiceItemDefaultIncomeAccount() {
		return serviceItemDefaultIncomeAccount;
	}

	public void setServiceItemDefaultIncomeAccount(
			String serviceItemDefaultIncomeAccount) {
		this.serviceItemDefaultIncomeAccount = serviceItemDefaultIncomeAccount;
	}

	public String getServiceItemDefaultExpenseAccount() {
		return serviceItemDefaultExpenseAccount;
	}

	public void setServiceItemDefaultExpenseAccount(
			String serviceItemDefaultExpenseAccount) {
		this.serviceItemDefaultExpenseAccount = serviceItemDefaultExpenseAccount;
	}

	public String getNonInventoryItemDefaultIncomeAccount() {
		return nonInventoryItemDefaultIncomeAccount;
	}

	public void setNonInventoryItemDefaultIncomeAccount(
			String nonInventoryItemDefaultIncomeAccount) {
		this.nonInventoryItemDefaultIncomeAccount = nonInventoryItemDefaultIncomeAccount;
	}

	public String getNonInventoryItemDefaultExpenseAccount() {
		return nonInventoryItemDefaultExpenseAccount;
	}

	public void setNonInventoryItemDefaultExpenseAccount(
			String nonInventoryItemDefaultExpenseAccount) {
		this.nonInventoryItemDefaultExpenseAccount = nonInventoryItemDefaultExpenseAccount;
	}

	public List<ClientAccount> getAccounts(int accountType) {
		List<ClientAccount> accounts = new ArrayList<ClientAccount>();

		for (ClientAccount clientAccount : getAccounts()) {
			if (clientAccount != null)
				if (clientAccount.type == accountType) {
					accounts.add(clientAccount);
				}

		}

		return accounts;
	}

	public void processUpdateOrCreateObject(AccounterCommand cmd) {

		try {

			// Accounter.showInformation("I came to processUpdateOrCreateObject.......");
			IAccounterCore accounterCoreObject = cmd.getData();
			if (accounterCoreObject != null)
				switch (cmd.getObjectType()) {

				case ACCOUNT:

					ClientAccount account = (ClientAccount) accounterCoreObject;

					// Utility.updateClientList(account, accounts);

					ClientAccount existObj = Utility.getObject(accounts,
							account.getStringID());
					if (existObj != null) {
						if (account.getNumber().equals(existObj.getNumber())) {
							accounts.remove(existObj);

							UIUtils.updateAccountsInSortedOrder(accounts,
									account);
						}
						// else {
						// int index = accounts.indexOf(existObj);
						// accounts.remove(existObj);
						// accounts.add(index, account);
						// }
					} else {

						UIUtils.updateAccountsInSortedOrder(accounts, account);
					}

					ViewManager.updateComboDataInViews(SelectItemType.ACCOUNT,
							account);
					ViewManager.getInstance().updateHomePageLists(
							accounterCoreObject);
					if (account.getType() == ClientAccount.TYPE_BANK)
						ViewManager.getInstance().updateDashBoardData(
								accounterCoreObject);
					break;

				case CUSTOMER:

					ClientCustomer customer = (ClientCustomer) accounterCoreObject;

					Utility.updateClientList(customer, customers);
					Utility.updateClientList(customer, payees);

					ViewManager.updateComboDataInViews(SelectItemType.CUSTOMER,
							accounterCoreObject);

					break;

				case VENDOR:

					ClientVendor vendor = (ClientVendor) accounterCoreObject;

					Utility.updateClientList(vendor, vendors);
					Utility.updateClientList(vendor, payees);

					ViewManager.updateComboDataInViews(SelectItemType.VENDOR,
							accounterCoreObject);

					break;

				case TAXAGENCY:

					ClientTAXAgency taxAgency = (ClientTAXAgency) accounterCoreObject;

					Utility.updateClientList(taxAgency, taxAgencies);
					Utility.updateClientList(taxAgency, payees);

					ViewManager.updateComboDataInViews(
							SelectItemType.TAX_AGENCY, accounterCoreObject);

					break;

				case ITEM:

					ClientItem item = (ClientItem) accounterCoreObject;

					Utility.updateClientList(item, items);

					ViewManager.updateComboDataInViews(SelectItemType.ITEM,
							accounterCoreObject);

					break;

				case TAX_GROUP:

					ClientTAXGroup taxGroup = (ClientTAXGroup) accounterCoreObject;

					// Utility.updateClientList(taxGroup, taxGroups);
					UIUtils.updateClientListAndTaxItemGroup(taxGroup, taxItems,
							taxGroups, taxItemGroups);
					if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
						ClientTAXCode code = getTAXCodeForTAXItemGroup((ClientTAXItemGroup) taxGroup);
						// Utility.updateClientList(code, taxCodes);
					}

					ViewManager.updateComboDataInViews(
							SelectItemType.TAX_GROUP, accounterCoreObject);

					break;

				case TAX_CODE:

					ClientTAXCode taxCode = (ClientTAXCode) accounterCoreObject;

					Utility.updateClientList(taxCode, taxCodes);

					ViewManager.updateComboDataInViews(SelectItemType.TAX_CODE,
							accounterCoreObject);

					break;

				case TAXITEM:

					ClientTAXItem taxItem = (ClientTAXItem) accounterCoreObject;

					// Utility.updateClientList(taxItem, taxItems);
					UIUtils.updateClientListAndTaxItemGroup(taxItem, taxItems,
							taxGroups, taxItemGroups);
					if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
						ClientTAXCode code = getTAXCodeForTAXItemGroup((ClientTAXItemGroup) taxItem);
						// Utility.updateClientList(code, taxCodes);
					}

					ViewManager.updateComboDataInViews(SelectItemType.TAXITEM,
							accounterCoreObject);

					break;

				case CUSTOMER_GROUP:

					ClientCustomerGroup customerGroup = (ClientCustomerGroup) accounterCoreObject;

					Utility.updateClientList(customerGroup, customerGroups);

					ViewManager.updateComboDataInViews(
							SelectItemType.CUSTOMER_GROUP, accounterCoreObject);

					break;

				case VENDOR_GROUP:

					ClientVendorGroup vendorGroup = (ClientVendorGroup) accounterCoreObject;

					Utility.updateClientList(vendorGroup, vendorGroups);

					ViewManager.updateComboDataInViews(
							SelectItemType.VENDOR_GROUP, accounterCoreObject);

					break;

				case PAYMENT_TERM:

					ClientPaymentTerms paymentsTerms = (ClientPaymentTerms) accounterCoreObject;

					Utility.updateClientList(paymentsTerms, paymentTerms);

					ViewManager.updateComboDataInViews(
							SelectItemType.PAYMENT_TERMS, accounterCoreObject);

					break;

				case SHIPPING_METHOD:

					ClientShippingMethod shippingMethod = (ClientShippingMethod) accounterCoreObject;

					Utility.updateClientList(shippingMethod, shippingMethods);

					ViewManager
							.updateComboDataInViews(
									SelectItemType.SHIPPING_METHOD,
									accounterCoreObject);

					break;

				case SHIPPING_TERM:

					ClientShippingTerms shippingTerm = (ClientShippingTerms) accounterCoreObject;

					Utility.updateClientList(shippingTerm, shippingTerms);

					ViewManager.updateComboDataInViews(
							SelectItemType.SHIPPING_TERMS, accounterCoreObject);

					break;

				case PRICE_LEVEL:

					ClientPriceLevel priceLevel = (ClientPriceLevel) accounterCoreObject;

					Utility.updateClientList(priceLevel, priceLevels);

					ViewManager.updateComboDataInViews(
							SelectItemType.PRICE_LEVEL, accounterCoreObject);

					break;

				case ITEM_GROUP:

					ClientItemGroup itemGroup = (ClientItemGroup) accounterCoreObject;

					Utility.updateClientList(itemGroup, ItemGroups);

					ViewManager.updateComboDataInViews(
							SelectItemType.ITEM_GROUP, accounterCoreObject);

					break;

				case SALES_PERSON:

					ClientSalesPerson salesPerson = (ClientSalesPerson) accounterCoreObject;

					Utility.updateClientList(salesPerson, salesPersons);

					ViewManager.updateComboDataInViews(
							SelectItemType.SALES_PERSON, accounterCoreObject);

					break;

				case CREDIT_RATING:

					ClientCreditRating creditRating = (ClientCreditRating) accounterCoreObject;

					Utility.updateClientList(creditRating, creditRatings);

					ViewManager.updateComboDataInViews(
							SelectItemType.CREDIT_RATING, accounterCoreObject);

					break;

				case PAY_SALES_TAX:

					ClientPaySalesTax paySalesTax = (ClientPaySalesTax) accounterCoreObject;

					Utility.updateClientList(paySalesTax, paySalesTaxs);

					break;

				case BANK:

					ClientBank clientBank = (ClientBank) accounterCoreObject;
					Utility.updateClientList(clientBank, banks);
					ViewManager.updateComboDataInViews(
							SelectItemType.BANK_ACCOUNT, accounterCoreObject);
					break;

				case FIXEDASSET:
					ClientFixedAsset fixedAsset = (ClientFixedAsset) accounterCoreObject;
					Utility.updateClientList(fixedAsset, fixedAssets);
					break;

				// case VATITEM:
				// ClientTAXItem vatItem = (ClientTAXItem) accounterCoreObject;
				// Utility.updateClientList(vatItem, this.vatItems);
				// ViewManager.updateComboDataInViews(SelectItemType.VAT_ITEM,
				// accounterCoreObject);
				// break;
				// case VATGROUP:
				// ClientTAXGroup vatGroup = (ClientTAXGroup)
				// accounterCoreObject;
				// Utility.updateClientList(vatGroup, this.vatGroups);
				//
				// break;
				// case VATCODE:
				// ClientVATCode code = (ClientVATCode) accounterCoreObject;
				// Utility.updateClientList(code, this.vatCodes);
				// break;
				// case VATAGENCY:
				// ClientVATAgency vagy = (ClientVATAgency) accounterCoreObject;
				// Utility.updateClientList(vagy, this.vatAgencies);
				// Utility.updateClientList(vagy, payees);
				// break;
				case VATRETURN:
					ClientVATReturn vaReturn = (ClientVATReturn) accounterCoreObject;
					Utility.updateClientList(vaReturn, this.vatReturns);
					break;

				case FISCALYEAR:
					ClientFiscalYear fiscalYear = (ClientFiscalYear) accounterCoreObject;
					Utility.updateClientList(fiscalYear, this.fiscalYears);
					Utility.sortFiscalYears();
					break;

				case COMPANY_PREFERENCES:
					this.preferences = (ClientCompanyPreferences) accounterCoreObject;
					break;
				case COMPANY:
					ClientCompany cmp = (ClientCompany) accounterCoreObject;
					this.getToClientCompany(cmp);
					break;
				}
			ViewManager.getInstance().operationSuccessFull(cmd);
			if (accounterCoreObject instanceof ClientTransaction)
				ViewManager.getInstance().updateDashBoardData(
						accounterCoreObject);
		} catch (Exception e) {
			if (e instanceof JavaScriptException) {
				Accounter.showInformation("Execption occur:"
						+ ((JavaScriptException) (e)).getDescription());

			} else {
				Accounter.showInformation("Execption occur:" + e.toString());
			}
		}

	}

	public void processDeleteObject(IAccounterCore accounterCoreObject) {

		AccounterCoreType accounterCoreObjectType = accounterCoreObject
				.getObjectType();

		if (accounterCoreObjectType == null)
			return;
		String id = accounterCoreObject.getStringID();
		switch (accounterCoreObjectType) {

		case ACCOUNT:

			deleteAccount(id);

			break;

		case CUSTOMER:
			deleteCustomer(id);
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientPayee payee : getActivePayees()) {
					if (payee.stringID.equals(id)) {
						Utility.isDelete = true;
						Utility.updateClientList(payee, payees);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case VENDOR:

			deleteVendor(id);
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientPayee payee : getActivePayees()) {
					if (payee.stringID.equals(id)) {
						Utility.isDelete = true;
						Utility.updateClientList(payee, payees);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case TAXAGENCY:

			deleteTaxAgency(id);
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientPayee payee : getActivePayees()) {
					if (payee.stringID.equals(id)) {
						Utility.isDelete = true;
						Utility.updateClientList(payee, payees);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case ITEM:

			deleteItem(id);

			break;

		case TAX_GROUP:

			deleteTaxGroup(id);
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientTAXCode taxCode : getActiveTaxCodes()) {
					if (taxCode.stringID.equals(id)) {
						Utility.isDelete = true;
						Utility.updateClientList(taxCode, taxCodes);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case TAX_CODE:

			deleteTaxCode(id);

			break;

		case CUSTOMER_GROUP:
			deleteCustomerGroup(id);
			break;

		case VENDOR_GROUP:

			deleteVendorGroup(id);
			break;
		case PAYMENT_TERM:
			deletePaymentTerms(id);
			break;
		case SHIPPING_METHOD:
			deleteShippingMethod(id);
			break;

		case SHIPPING_TERM:
			deleteShippingTerms(id);
			break;

		case PRICE_LEVEL:
			deletePriceLevel(id);
			break;

		case ITEM_GROUP:
			deleteItemGroup(id);
			break;

		case SALES_PERSON:
			deleteSalesPerson(id);
			break;

		case CREDIT_RATING:

			deleteCreditRating(id);
			break;

		case BANK:
			deleteBank(id);
			break;

		case FIXEDASSET:

			deleteFixedAsset(id);
			break;
		case TAXITEM:
			deleteTaxItem(id);
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientTAXCode taxCode : getActiveTaxCodes()) {
					if (taxCode.stringID.equals(id)) {
						Utility.isDelete = true;
						Utility.updateClientList(taxCode, taxCodes);
						Utility.isDelete = false;
					}
				}
			}

			break;
		// case VATGROUP:
		// deleteVatGroup(id);
		// break;
		// case VATCODE:
		// deleteVatCode(id);
		// break;
		// case VATAGENCY:
		// deleteVatAgency(id);
		// break;
		case VATRETURN:
			deleteVAtReturn(id);
			break;
		case FISCALYEAR:
			deleteFixelYear(id);
		}
		ViewManager.getInstance().deleteSuccess(accounterCoreObject);
	}

	// private void deleteSoldDisposedAsset(String id) {
	// this.sellingDisposedItems.remove(this.getSellingDisposedItem(id));
	//
	// }

	public void deleteFixedAsset(String fixedAssetId) {
		this.fixedAssets.remove(this.getFixedAsset(fixedAssetId));
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientCompany";
	}

	public void setFixedAssets(List<ClientFixedAsset> fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public List<ClientFixedAsset> getFixedAssets() {
		return fixedAssets;
	}

	// public List<ClientSellingOrDisposingFixedAsset> getSellingDisposedItems()
	// {
	// return sellingDisposedItems;
	// }
	//
	// public void setSellingDisposedItems(
	// List<ClientSellingOrDisposingFixedAsset> assets) {
	// this.sellingDisposedItems = assets;
	// }
	//
	// public ClientSellingOrDisposingFixedAsset getSellingDisposedItem(
	// String assetID) {
	// return Utility.getObject(this.sellingDisposedItems, assetID);
	// }

	public boolean objectExists(IAccounterCore core) {

		if (core == null || core.getObjectType() == null) {
			return false;
		}

		AccounterCoreType type = core.getObjectType();

		boolean alreadyExists = false;

		switch (type) {
		case ACCOUNT:
			alreadyExists = Utility.getObjectByName(accounts, core.getName()) != null;
			break;

		case CUSTOMER:
			alreadyExists = Utility.getObjectByName(customers, core.getName()) != null;
			break;

		case VENDOR:
			alreadyExists = Utility.getObjectByName(vendors, core.getName()) != null;
			break;

		case TAX_CODE:
			break;

		default:
			break;
		}

		return alreadyExists;
	}

	public Integer[] getNominalCodeRange(int accountSubBaseType) {

		for (ClientNominalCodeRange nomincalCode : this.getNominalCodeRange()) {
			if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
				return new Integer[] { nomincalCode.getMinimum(),
						nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public boolean isConfigured() {
		return isConfigured;
	}

	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	public void setVatReturnBoxes(List<ClientVATReturnBox> vatReturnBoxes) {
		this.vatReturnBoxes = vatReturnBoxes;
	}

	public List<ClientVATReturnBox> getVatReturnBoxes() {
		return vatReturnBoxes;
	}

	public ClientVATReturnBox getVatReturnBoxByID(String vatReturnBoxID) {
		return Utility.getObject(this.vatReturnBoxes, vatReturnBoxID);
	}

	// public ClientTAXItem getVATItemByName(String name) {
	// return Utility.getObjectByName(this.vatItems, name);
	// }

	public void setUkServiceItemDefaultIncomeAccount(
			String ukServiceItemDefaultIncomeAccount) {
		this.ukServiceItemDefaultIncomeAccount = ukServiceItemDefaultIncomeAccount;
	}

	public String getUkServiceItemDefaultIncomeAccount() {
		return ukServiceItemDefaultIncomeAccount;
	}

	public void setUkServiceItemDefaultExpenseAccount(
			String ukServiceItemDefaultExpenseAccount) {
		this.ukServiceItemDefaultExpenseAccount = ukServiceItemDefaultExpenseAccount;
	}

	public String getUkServiceItemDefaultExpenseAccount() {
		return ukServiceItemDefaultExpenseAccount;
	}

	public void setUkNonInventoryItemDefaultIncomeAccount(
			String ukNonInventoryItemDefaultIncomeAccount) {
		this.ukNonInventoryItemDefaultIncomeAccount = ukNonInventoryItemDefaultIncomeAccount;
	}

	public String getUkNonInventoryItemDefaultIncomeAccount() {
		return ukNonInventoryItemDefaultIncomeAccount;
	}

	public void setUkNonInventoryItemDefaultExpenseAccount(
			String ukNonInventoryItemDefaultExpenseAccount) {
		this.ukNonInventoryItemDefaultExpenseAccount = ukNonInventoryItemDefaultExpenseAccount;
	}

	public String getUkNonInventoryItemDefaultExpenseAccount() {
		return ukNonInventoryItemDefaultExpenseAccount;
	}

	public boolean isUKAccounting() {

		return this.accountingType == ACCOUNTING_TYPE_UK;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void getToClientCompany(ClientCompany clientCompany) {

		this.name = clientCompany.name;
		this.addresses = clientCompany.addresses;
		this.phone = clientCompany.phone;
		this.companyEmail = clientCompany.companyEmail;
		this.legalName = clientCompany.legalName;
		this.fax = clientCompany.fax;
		this.webSite = clientCompany.webSite;
		this.registrationNumber = clientCompany.registrationNumber;
		this.taxId = clientCompany.taxId;
		this.bankAccountNo = clientCompany.bankAccountNo;
		this.sortCode = clientCompany.sortCode;
		this.preferences = clientCompany.preferences;

	}

	public ClientTAXCode getTAXCodeForTAXItemGroup(
			ClientTAXItemGroup taxItemGroup) {
		boolean exist = false;
		ClientTAXCode taxCode = new ClientTAXCode();
		taxCode.setStringID(taxItemGroup.getStringID());
		taxCode.setName(taxItemGroup.getName());
		taxCode.setDescription(taxItemGroup.description);
		taxCode.setActive(taxItemGroup.isActive());
		taxCode.setTAXItemGrpForSales(taxItemGroup.getStringID());
		taxCode.setTaxable(true);
		taxCode.setECSalesEntry(false);
		taxCode.setTAXItemGrpForPurchases(null);
		for (ClientTAXCode tempCode : taxCodes) {
			if (tempCode.getName().equalsIgnoreCase(taxCode.getName())) {
				exist = true;
				break;
			}
		}
		if (!exist)
			Utility.updateClientList(taxCode, taxCodes);
		return taxCode;
	}
}
