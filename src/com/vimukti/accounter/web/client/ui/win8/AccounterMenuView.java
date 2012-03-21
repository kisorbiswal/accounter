package com.vimukti.accounter.web.client.ui.win8;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.countries.UnitedKingdom;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class AccounterMenuView extends BaseView {

	private boolean canDoBanking;

	private boolean canDoInvoiceAndBillTransactions;

	private boolean canDoPayBillAndReceivePayment;

	private boolean canChangeSettings;

	private boolean isTrackTax;

	private boolean isLocationTracking;

	private boolean isClassTracking;

	private boolean canSeeBanking;

	private boolean canSeeInvoiceTransactions;

	private boolean isDoyouwantEstimates;

	private boolean isDelayedchargesEnabled;

	private boolean isHaveEpmloyees;

	private boolean isTrackEmployeeExpenses;

	private boolean isKeepTrackofBills;

	private boolean canViewReports;

	private boolean iswareHouseEnabled;

	private boolean isPurchaseOrderEnabled;

	private boolean isSalesOrderEnabled;

	private boolean isClassTrackingEnabled;

	private boolean isLocationTrackingEnabled;

	private boolean isTaxTracking;

	private ICountryPreferences company;

	private boolean isInventoryEnabled;

	private boolean notReadOnlyUser;

	private boolean tdsEnabled;

	private boolean canDoTaxTransactions;

	private boolean canDoUserManagement;

	private boolean canDoInventory;

	private boolean canDoManageAccounts;

	private boolean isPriceLevelEnabled;

	private String countryOrRegion;

	private final ClientCompanyPreferences preferences = Global.get()
			.preferences();

	@Override
	public void init() {
		super.init();
		ICountryPreferences countryPreferences = Accounter.getCompany()
				.getCountryPreferences();
		setPreferencesandPermissions(preferences, Accounter.getUser(),
				countryPreferences);
	}

	private void setPreferencesandPermissions(
			ClientCompanyPreferences preferences2, ClientUser clientUser,
			ICountryPreferences countryPreferences) {

		this.canDoInvoiceAndBillTransactions = canDoInvoiceTransactions(clientUser);

		this.canDoPayBillAndReceivePayment = canDoPayBillAndReceivePayment(clientUser);

		this.canChangeSettings = canChangeSettings(clientUser);

		this.isTrackTax = preferences.isTrackTax();

		this.isLocationTracking = preferences.isLocationTrackingEnabled();

		this.canDoBanking = canDoBanking(clientUser);

		this.isClassTracking = preferences.isClassTrackingEnabled();

		this.canSeeBanking = canSeeBanking(clientUser);

		this.canSeeInvoiceTransactions = canSeeInvoiceTransactions(clientUser);

		this.isDoyouwantEstimates = preferences.isDoyouwantEstimates();

		tdsEnabled = preferences.isTDSEnabled();

		this.isDelayedchargesEnabled = preferences.isDelayedchargesEnabled();

		this.isHaveEpmloyees = preferences.isHaveEpmloyees();

		this.isTrackEmployeeExpenses = preferences.isTrackEmployeeExpenses();

		this.isKeepTrackofBills = preferences.isKeepTrackofBills();

		this.canViewReports = canViewReports(clientUser);

		this.iswareHouseEnabled = preferences.iswareHouseEnabled();

		this.isPurchaseOrderEnabled = preferences.isPurchaseOrderEnabled();

		this.isSalesOrderEnabled = preferences.isSalesOrderEnabled();

		this.isClassTrackingEnabled = preferences.isClassTrackingEnabled();

		this.isLocationTrackingEnabled = preferences
				.isLocationTrackingEnabled();

		this.isTaxTracking = preferences.isTrackTax();

		this.isInventoryEnabled = preferences.isInventoryEnabled();

		this.company = countryPreferences;

		this.notReadOnlyUser = !clientUser.getUserRole().equalsIgnoreCase(
				messages.readOnly());

		this.canDoTaxTransactions = canDoTaxTransactions(clientUser);

		this.canDoUserManagement = canDoUserManagement(clientUser);

		this.canDoInventory = canDoInventory(clientUser);

		this.canDoManageAccounts = CanDoManageAccounts(clientUser);

		this.isPriceLevelEnabled = preferences.isPricingLevelsEnabled();

		this.countryOrRegion = preferences.getTradingAddress()
				.getCountryOrRegion();

		getMenu();
	}

	private boolean CanDoManageAccounts(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfManageAccounts() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoInventory(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoUserManagement(ClientUser clientUser) {
		if (clientUser.isCanDoUserManagement())
			return true;
		else
			return false;
	}

	private boolean canDoTaxTransactions(ClientUser clientUser) {
		if (clientUser.getUserRole().equals(RolePermissions.ADMIN)
				|| clientUser.getUserRole().equals(
						RolePermissions.FINANCIAL_ADVISER))
			return true;
		else
			return false;
	}

	private boolean canViewReports(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES
				|| clientUser.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_READ_ONLY)
			return true;
		else
			return false;
	}

	private boolean canSeeInvoiceTransactions(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfInvoicesBills() != RolePermissions.TYPE_NO;
	}

	private boolean canSeeBanking(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfBankReconcilation() != RolePermissions.TYPE_NO;
	}

	private boolean canDoBanking(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfBankReconcilation() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canChangeSettings(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoPayBillAndReceivePayment(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoInvoiceTransactions(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfInvoicesBills() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private void getMenu() {

		StyledPanel mainMenuPanel = new StyledPanel("mainMenu");

		mainMenuPanel.add(getCompanyMenu(messages.company()));

		mainMenuPanel.add(getCustomerMenu(Global.get().Customer()));

		mainMenuPanel.add(getVendorsMenu(Global.get().Vendor()));

		if (canDoBanking) {
			mainMenuPanel.add(getBankingMenu(messages.banking()));
		}
		if (canDoTaxTransactions && isTrackTax) {
			mainMenuPanel.add(getTaxMenu(messages.tax()));
		}
		if (canDoInventory && isInventoryEnabled) {
			mainMenuPanel.add(getInventoryMenu(messages.inventory()));
		}

		if (canChangeSettings || canDoUserManagement) {
			mainMenuPanel.add(getSettingsMenu(messages.settings()));
		}

		this.add(mainMenuPanel);
		addStyleName("mainMenuView");

	}

	private DynamicForm getCompanyMenu(String company) {

		DynamicForm companyMenuForm = new DynamicForm("companyMenuForm");

		Label companyLabel = new Label(company);
		companyLabel.setStyleName("menuName");

		DynamicForm companyListForm = new DynamicForm("companyListForm");

		W8MenuItem transactionCenterItem = new W8MenuItem(
				messages.transactionscenter(), "",
				HistoryTokens.TRANSACTIONS_CENTER);
		companyListForm.add(transactionCenterItem);

		if (canDoManageAccounts) {
			W8MenuItem journalEntryItem = new W8MenuItem(
					messages.journalEntry(),
					"Create a new journal entry from here.",
					HistoryTokens.NEWJOURNALENTRY);
			W8MenuItem newAccountItem = new W8MenuItem(messages.newAccount(),
					"", HistoryTokens.NEWACCOUNT);
			companyListForm.add(journalEntryItem);
			companyListForm.add(newAccountItem);
		}

		if (canDoTaxTransactions) {
			W8MenuItem budgetItem = new W8MenuItem(messages.budget(),
					"Create your monthly budget and decrease your expenses",
					HistoryTokens.BUDGET);
			companyListForm.add(budgetItem);
		}

		if (canChangeSettings) {
			// companyListForm.add(getManageSupportListMenu());
			// W8MenuItem manageSupportListSubmenus = new W8MenuItem(
			// messages.manageSupportLists(),
			// "Get the list all Support items",
			// HistoryTokens.MANAGESUPPORTLIST);
			// companyListForm.add(manageSupportListSubmenus);
		}

		if (canDoTaxTransactions) {
			// companyListForm.add(getFixedAssetsMenu());
			// W8MenuItem fixedAssetsSubMenu = new W8MenuItem(
			// messages.fixedAssetsList(),
			// "Manage fixed assets for your company from here.",
			// HistoryTokens.FIXEDASSESTSLIST);
			// companyListForm.add(fixedAssetsSubMenu);
			//
			// W8MenuItem mergeSubMenu = new W8MenuItem(messages.merge(),
			// "Merge the accounts or items you want.",
			// HistoryTokens.MERGESUBMENULIST);
			// companyListForm.add(mergeSubMenu);
			// companyListForm.add(getMergeMenu());
		}

		if (countryOrRegion.equals(CountryPreferenceFactory.SINGAPORE)) {
			W8MenuItem generateGSTItem = new W8MenuItem(
					messages.generateIrasAuditFile(), "",
					HistoryTokens.GST_FILE);
			companyListForm.add(generateGSTItem);
		}

		companyMenuForm.add(companyLabel);
		companyMenuForm.add(companyListForm);

		return companyMenuForm;
	}

	private DynamicForm getManageSupportListMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem shppingTermsItem = new W8MenuItem(
				messages.shippingTermList(), "",
				HistoryTokens.SHIPPINGTERMSLIST);
		W8MenuItem shippingMethodsItem = new W8MenuItem(
				messages.shippingMethodList(), "",
				HistoryTokens.SHIPPINGMETHODSLIST);
		W8MenuItem paymentTerms = new W8MenuItem(messages.paymentTerms(), "",
				HistoryTokens.PAYMENTTERMS);
		W8MenuItem currenciesItem = new W8MenuItem(messages.currencyList(), "",
				HistoryTokens.CURRENCYGROUPLIST);

		menuItems.add(shppingTermsItem);
		menuItems.add(shippingMethodsItem);
		menuItems.add(paymentTerms);
		menuItems.add(currenciesItem);

		if (isClassTracking) {
			W8MenuItem classItem = new W8MenuItem(
					messages.accounterClassList(), "",
					HistoryTokens.ACCOUNTERCLASSLIST);
			menuItems.add(classItem);
		}

		if (isLocationTracking) {
			W8MenuItem locationItem = new W8MenuItem(
					messages.locationsList(Global.get().Location()), "",
					HistoryTokens.LOCATIONGROUPLIST);
			menuItems.add(locationItem);
		}

		if (isPriceLevelEnabled) {
			W8MenuItem priceLevelItem = new W8MenuItem(
					messages.priceLevelList(), "", HistoryTokens.PRICELEVELLIST);
			menuItems.add(priceLevelItem);
		}

		return new W8MenuItem(messages.manageSupportLists(), "", menuItems);

	}

	private DynamicForm getCompanyListMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		if (canSeeInvoiceTransactions) {
			W8MenuItem accountsListItem = new W8MenuItem(
					messages.payeeList(messages.Accounts()), "",
					HistoryTokens.ACCOUNTSLIST);
			menuItems.add(accountsListItem);
		}

		if (canSeeBanking) {
			W8MenuItem journalEntriesItem = new W8MenuItem(
					messages.journalEntries(), "", HistoryTokens.JOURNALENTRIES);
			menuItems.add(journalEntriesItem);
		}

		W8MenuItem salesPersonsListItem = new W8MenuItem(
				messages.salesPersons(), "", HistoryTokens.SALESPRESONS);
		W8MenuItem userActivityLogItem = new W8MenuItem(
				messages.usersActivityLogTitle(), "",
				HistoryTokens.USERACTIVITY);
		W8MenuItem recurringTransactionsItem = new W8MenuItem(
				messages.recurringTransactions(), "",
				HistoryTokens.RECURRINGTRANSACTIONS);
		W8MenuItem remindersListItem = new W8MenuItem(messages.remindersList(),
				"", HistoryTokens.RECURRINGREMINDERS);

		if (canSeeInvoiceTransactions) {
			W8MenuItem items = new W8MenuItem(messages.productAndServices(),
					"", HistoryTokens.ALLITEMS);
			menuItems.add(items);
		}

		menuItems.add(salesPersonsListItem);
		menuItems.add(userActivityLogItem);
		menuItems.add(recurringTransactionsItem);
		menuItems.add(remindersListItem);

		return new W8MenuItem(messages.companyLists(), "comepnyDesc", menuItems);
	}

	private DynamicForm getMergeMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem mergeItemsItem = new W8MenuItem(messages.mergeItems(), "",
				HistoryTokens.MERGEITEM);
		W8MenuItem mergeCustomersItem = new W8MenuItem(
				messages.mergeCustomers(Global.get().Customers()), "",
				HistoryTokens.MERGECUSTOMERS);
		W8MenuItem mergeAccountsItem = new W8MenuItem(messages.mergeAccounts(),
				"", HistoryTokens.MERGEACCOUNT);
		W8MenuItem mergeVendorsItem = new W8MenuItem(
				messages.mergeVendors(Global.get().Vendors()), "",
				HistoryTokens.MERGEVENDOR);

		menuItems.add(mergeItemsItem);
		menuItems.add(mergeCustomersItem);
		menuItems.add(mergeAccountsItem);
		menuItems.add(mergeVendorsItem);

		return new W8MenuItem(messages.merge(), messages.mergeDescription(),
				menuItems);

	}

	private DynamicForm getFixedAssetsMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem deprecationItem = new W8MenuItem(messages.depreciation(),
				"", HistoryTokens.DEPRICATION);
		W8MenuItem pendingItemsItem = new W8MenuItem(
				messages.pendingItemsList(), "", HistoryTokens.PENDINGITEMS);
		W8MenuItem registeredItemsItem = new W8MenuItem(
				messages.registeredItemsList(), "",
				HistoryTokens.REGISTEREDITEMS);
		W8MenuItem soldItemsItem = new W8MenuItem(messages.soldDisposedItems(),
				"", HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);

		menuItems.add(deprecationItem);
		menuItems.add(pendingItemsItem);
		menuItems.add(registeredItemsItem);
		menuItems.add(soldItemsItem);

		return new W8MenuItem(messages.fixedAssets(), "", menuItems);
	}

	private DynamicForm getFixedAssetReportSubMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem depreciationReport = new W8MenuItem(
				messages.depreciationReport(), "",
				HistoryTokens.DEPRECIATIONSHEDULE);
		menuItems.add(depreciationReport);

		return new W8MenuItem(messages.fixedAssets(), "", menuItems);

	}

	private DynamicForm getVATReportMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		if (company instanceof UnitedKingdom) {
			W8MenuItem priorVATReturns = new W8MenuItem(
					messages.priorVATReturns(), "",
					HistoryTokens.PRIORVATRETURN);
			menuItems.add(priorVATReturns);

			W8MenuItem vatDetail = new W8MenuItem(messages.vatDetail(), "",
					HistoryTokens.VATDETAIL);
			menuItems.add(vatDetail);

			W8MenuItem vat100 = new W8MenuItem(messages.vat100(), "",
					HistoryTokens.VAT100);
			menuItems.add(vat100);

			W8MenuItem uncategorisedVATAmounts = new W8MenuItem(
					messages.uncategorisedVATAmounts(), "",
					HistoryTokens.UNCATEGORISEDVATAMOUNT);
			menuItems.add(uncategorisedVATAmounts);

			W8MenuItem ecSalesList = new W8MenuItem(messages.ecSalesList(), "",
					HistoryTokens.ECSALESLIST);
			menuItems.add(ecSalesList);

		} else {
			W8MenuItem taxItemDetailReport = new W8MenuItem(
					messages.taxItemDetailReport(), "",
					HistoryTokens.TAXITEMDETAIL);
			menuItems.add(taxItemDetailReport);

			W8MenuItem taxItemExceptionDetailReport = new W8MenuItem(
					messages.taxItemExceptionDetailReport(), "",
					HistoryTokens.TAXITEMEXCEPTIONDETAILS);
			menuItems.add(taxItemExceptionDetailReport);
		}

		W8MenuItem vatItemSummary = new W8MenuItem(messages.vatItemSummary(),
				"", HistoryTokens.VATITEMSUMMARY);
		menuItems.add(vatItemSummary);

		return new W8MenuItem(messages.tax(), "", menuItems);

	}

	private DynamicForm getBudgetSubMenus() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem budgetOverview = new W8MenuItem(messages.budgetOverview(),
				"", HistoryTokens.BUDGETREPORTOVERVIEW);
		menuItems.add(budgetOverview);

		W8MenuItem budgetvsActuals = new W8MenuItem(messages.budgetvsActuals(),
				"", HistoryTokens.BUDGETVSACTUALS);
		menuItems.add(budgetvsActuals);

		return new W8MenuItem(messages.budget(), "", menuItems);
	}

	private DynamicForm getPurchaseMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem purchaseByVendorSummary = new W8MenuItem(
				messages.purchaseByVendorSummary(Global.get().Vendor()), "",
				HistoryTokens.PURCHASEBYVENDORSUMMARY);
		menuItems.add(purchaseByVendorSummary);

		W8MenuItem purchaseByVendorDetail = new W8MenuItem(
				messages.purchaseByVendorDetail(Global.get().Vendor()), "",
				HistoryTokens.PURCHASEBYVENDORDETAIL);
		menuItems.add(purchaseByVendorDetail);

		W8MenuItem purchaseByItemSummary = new W8MenuItem(
				messages.purchaseByItemSummary(), "",
				HistoryTokens.PURCHASEBYITEMSUMMARY);
		menuItems.add(purchaseByItemSummary);

		W8MenuItem purchaseByItemDetail = new W8MenuItem(
				messages.purchaseByItemDetail(), "",
				HistoryTokens.PURCHASEBYITEMDETAIL);
		menuItems.add(purchaseByItemDetail);

		if (isPurchaseOrderEnabled) {
			W8MenuItem purchaseOrderReport = new W8MenuItem(
					messages.purchaseOrderReport(), "",
					HistoryTokens.PURCHASEORDERREPORT);
			menuItems.add(purchaseOrderReport);
		}

		return new W8MenuItem(messages.purchases(), "", menuItems);
	}

	private DynamicForm getVendorAndPayablesMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem apAgeingSummary = new W8MenuItem(messages.apAgeingSummary(),
				"", HistoryTokens.APAGINGSUMMARY);
		menuItems.add(apAgeingSummary);

		W8MenuItem apAgeingDetail = new W8MenuItem(messages.apAgeingDetail(),
				"", HistoryTokens.APAGINGDETAIL);
		menuItems.add(apAgeingDetail);

		W8MenuItem payeeStatement = new W8MenuItem(
				messages.payeeStatement(Global.get().Vendors()), "",
				HistoryTokens.VENDORSTATEMENT);
		menuItems.add(payeeStatement);

		W8MenuItem payeeTransactionHistory = new W8MenuItem(
				messages.payeeTransactionHistory(Global.get().Vendor()), "",
				HistoryTokens.VENDORTRANSACTIONHISTORY);
		menuItems.add(payeeTransactionHistory);

		return new W8MenuItem(messages.vendorsAndPayables(Global.get()
				.Vendors()), "", menuItems);

	}

	private DynamicForm getSalesMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem salesByCustomerSummary = new W8MenuItem(
				messages.salesByCustomerSummary(Global.get().Customer()), "",
				HistoryTokens.SALESBYCUSTOMERSUMMARY);
		menuItems.add(salesByCustomerSummary);

		W8MenuItem salesByCustomerDetail = new W8MenuItem(
				messages.salesByCustomerDetail(Global.get().Customer()), "",
				HistoryTokens.SALESBYCUSTOMERDETAIL);
		menuItems.add(salesByCustomerDetail);

		W8MenuItem salesByItemSummary = new W8MenuItem(
				messages.salesByItemSummary(), "",
				HistoryTokens.SALESBYITEMSUMMARY);
		menuItems.add(salesByItemSummary);

		W8MenuItem salesByItemDetail = new W8MenuItem(
				messages.salesByItemDetail(), "",
				HistoryTokens.SALESBYITEMDETAIL);
		menuItems.add(salesByItemDetail);

		if (isSalesOrderEnabled) {

			W8MenuItem salesOrderReport = new W8MenuItem(
					messages.salesOrderReport(), "",
					HistoryTokens.SALESORDERREPORT);
			menuItems.add(salesOrderReport);
		}
		if (isLocationTrackingEnabled) {

			W8MenuItem getSalesByLocationDetails = new W8MenuItem(
					messages.getSalesByLocationDetails(Global.get().Location()),
					"", HistoryTokens.SALESBYCLASSDETAILS);
			menuItems.add(getSalesByLocationDetails);

			W8MenuItem salesByLocationSummary = new W8MenuItem(
					messages.salesByLocationSummary(Global.get().Location()),
					"", HistoryTokens.SALESBYCLASSSUMMARY);
			menuItems.add(salesByLocationSummary);
		}

		if (isClassTrackingEnabled) {

			W8MenuItem salesByClassDetails = new W8MenuItem(
					messages.salesByClassDetails(), "",
					HistoryTokens.SALESBYLOCATIONDETAILS);
			menuItems.add(salesByClassDetails);

			W8MenuItem salesByClassSummary = new W8MenuItem(
					messages.salesByClassSummary(), "",
					HistoryTokens.SALESBYLOCATIONSUMMARY);
			menuItems.add(salesByClassSummary);
		}

		return new W8MenuItem(messages.sales(), "", menuItems);

	}

	/**
	 * creating getCustomersAndReceivableMenu menu
	 * 
	 * @param companyAndFinance
	 * @return
	 */
	private DynamicForm getCustomersAndReceivableMenu() {

		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem arAgeingSummary = new W8MenuItem(messages.arAgeingSummary(),
				"", HistoryTokens.ARAGINGSUMMARY);
		menuItems.add(arAgeingSummary);

		W8MenuItem arAgeingDetail = new W8MenuItem(messages.arAgeingDetail(),
				"", HistoryTokens.ARAGINGDETAIL);
		menuItems.add(arAgeingDetail);

		W8MenuItem payeeStatement = new W8MenuItem(
				messages.payeeStatement(Global.get().Customers()), "",
				HistoryTokens.CUSTOMERSTATEMENT);
		menuItems.add(payeeStatement);

		W8MenuItem customer = new W8MenuItem(
				messages.payeeTransactionHistory(messages.customer()), "",
				HistoryTokens.CUSTOMERTRANSACTIONHISTORY);
		menuItems.add(customer);

		return new W8MenuItem(messages.customersAndReceivable(Global.get()
				.Customers()), "", menuItems);
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		buttonBar.setVisible(false);
	}

	private DynamicForm getSettingsMenu(String settings) {
		DynamicForm settingsMenuForm = new DynamicForm("settingsMenuForm");

		DynamicForm settingdListForm = new DynamicForm("settingdListForm");

		Label settingsLabel = new Label(settings);
		settingsLabel.setStyleName("menuName");

		if (canChangeSettings) {
			W8MenuItem companySettingsItem = new W8MenuItem(
					messages.companyPreferences(), "",
					HistoryTokens.COMPANYPREFERENCES);
			W8MenuItem invoiceBrandingItem = new W8MenuItem(
					messages.invoiceBranding(),
					"Generate your own branding theme for invoices",
					HistoryTokens.INVOICEBRANDING);

			settingdListForm.add(companySettingsItem);
			settingdListForm.add(invoiceBrandingItem);
		}

		if (canDoUserManagement) {
			W8MenuItem usersItem = new W8MenuItem(messages.users(),
					"Add or manage company users from here.",
					HistoryTokens.USERS);
			settingdListForm.add(usersItem);
		}

		W8MenuItem chequePrintItem = new W8MenuItem(
				messages.chequePrintSetting(), "Create cheques",
				HistoryTokens.CHECK_PRINT_SETTING);
		settingdListForm.add(chequePrintItem);

		settingsMenuForm.add(settingsLabel);
		settingsMenuForm.add(settingdListForm);

		return settingsMenuForm;
	}

	/**
	 * creating getCompanyAndFinancialMenu report menu
	 * 
	 * @param customersAndReceivable
	 * @return
	 */
	private DynamicForm getCompanyAndFinancialMenu() {
		ArrayList<W8MenuItem> menuItems = new ArrayList<W8MenuItem>();

		W8MenuItem chequePrintItem = new W8MenuItem(messages.profitAndLoss(),
				"", HistoryTokens.PROFITANDLOSS);
		menuItems.add(chequePrintItem);

		W8MenuItem balanceSheet = new W8MenuItem(messages.balanceSheet(), "",
				HistoryTokens.BALANCESHEET);
		menuItems.add(balanceSheet);

		W8MenuItem cashFlowReport = new W8MenuItem(messages.cashFlowReport(),
				"", HistoryTokens.CASHFLOWREPORT);
		menuItems.add(cashFlowReport);

		W8MenuItem trialBalance = new W8MenuItem(messages.trialBalance(), "",
				HistoryTokens.TRIALBALANCE);
		menuItems.add(trialBalance);

		W8MenuItem transactionDetailByAccount = new W8MenuItem(
				messages.transactionDetailByAccount(), "",
				HistoryTokens.TRANSACTIONDETAILBYACCOUNT);
		menuItems.add(transactionDetailByAccount);

		W8MenuItem generalLedgerReport = new W8MenuItem(
				messages.generalLedgerReport(), "", HistoryTokens.GENERALLEDGER);
		menuItems.add(generalLedgerReport);

		W8MenuItem expenseReport = new W8MenuItem(messages.expenseReport(), "",
				HistoryTokens.EXPENSEREPORT);
		menuItems.add(expenseReport);

		W8MenuItem automaticTransactions = new W8MenuItem(
				messages.automaticTransactions(), "",
				HistoryTokens.AUTOMATICTRANSACTIONS);
		menuItems.add(automaticTransactions);

		if (isTaxTracking) {

			W8MenuItem salesTaxLiability = new W8MenuItem(
					messages.salesTaxLiability(), "",
					HistoryTokens.SALESTAXLIABILITY);
			menuItems.add(salesTaxLiability);

			W8MenuItem transactionDetailByTaxItem = new W8MenuItem(
					messages.transactionDetailByTaxItem(), "",
					HistoryTokens.TRANSACTIONDETAILBYTAXITEM);
			menuItems.add(transactionDetailByTaxItem);
		}
		if (isLocationTrackingEnabled) {

			W8MenuItem profitAndLossByLocation = new W8MenuItem(
					messages.profitAndLossByLocation(Global.get().Location()),
					"", HistoryTokens.PROFITANDLOSSBYLOCATION);
			menuItems.add(profitAndLossByLocation);
		}
		if (isClassTrackingEnabled) {
			W8MenuItem profitAndLossbyClass = new W8MenuItem(
					messages.profitAndLossbyClass(), "",
					HistoryTokens.PROFITANDLOSSBYCLASS);
			menuItems.add(profitAndLossbyClass);
		}

		W8MenuItem reconciliationsReport = new W8MenuItem(
				messages.reconciliationsReport(), "",
				HistoryTokens.RECONCILATION_LIST);
		menuItems.add(reconciliationsReport);

		return new W8MenuItem(messages.companyAndFinancial(), "", menuItems);

	}

	private DynamicForm getInventoryMenu(String inventory) {

		DynamicForm inventoryMenuForm = new DynamicForm("inventoryMenuForm");

		DynamicForm inventoryListForm = new DynamicForm("inventoryListForm");

		Label inventoryLabel = new Label(inventory);
		inventoryLabel.setStyleName("menuName");

		W8MenuItem stockAdjustmentsItem = new W8MenuItem(
				messages.stockAdjustments(), "Perform the stock adjustments",
				HistoryTokens.STOCKADJUSTMENTS);

		inventoryListForm.add(inventoryLabel);
		inventoryListForm.add(stockAdjustmentsItem);

		inventoryMenuForm.add(inventoryLabel);
		inventoryMenuForm.add(inventoryListForm);

		return inventoryMenuForm;
	}

	private DynamicForm getBankingMenu(String banking) {

		DynamicForm bankingMenuForm = new DynamicForm("bankingMenuForm");

		DynamicForm bankingListForm = new DynamicForm("bankingListForm");

		Label bankingLabel = new Label(banking);
		bankingLabel.setStyleName("menuName");

		W8MenuItem depositAndTransfersItem = new W8MenuItem(
				messages.depositTransferFunds(),
				"Deposite or transfer funds in between your accounts.",
				HistoryTokens.DEPOSITETRANSFERFUNDS);
		W8MenuItem reconciliationItem = new W8MenuItem(
				messages.ReconciliationsList(),
				"Compare your bank statements with your transactions.",
				HistoryTokens.RECOUNCILATIONSLIST);

		bankingListForm.add(depositAndTransfersItem);
		bankingListForm.add(reconciliationItem);

		bankingMenuForm.add(bankingLabel);
		bankingMenuForm.add(bankingListForm);

		return bankingMenuForm;
	}

	private DynamicForm getVendorsMenu(String vendor) {

		DynamicForm vendorMenuForm = new DynamicForm("vendorMenuForm");

		DynamicForm vendorListForm = new DynamicForm("vendorListForm");

		Label vendorLabel = new Label(vendor);
		vendorLabel.setStyleName("menuName");

		if (canDoInvoiceAndBillTransactions || canDoBanking
				|| canDoManageAccounts) {
			if (canDoBanking || canDoManageAccounts) {
				W8MenuItem cashPurchaseItem = new W8MenuItem(
						messages.newCashPurchase(),
						"Create new Cash purchases",
						HistoryTokens.NEWCASHPURCHASE);
				vendorListForm.add(cashPurchaseItem);
			}

			if (canDoInvoiceAndBillTransactions) {
				W8MenuItem vendorCreditItem = new W8MenuItem(
						messages.vendorCreditMemo(),
						"Generate the credit memo for your vendors",
						HistoryTokens.VENDORCREDIT);
				vendorListForm.add(vendorCreditItem);
			}

		}

		if (canDoPayBillAndReceivePayment) {
			W8MenuItem vendorPrepaymentItem = new W8MenuItem(
					messages.payeePrePayment(Global.get().Vendor()),
					"Add all vendor prepayments here.",
					HistoryTokens.VENDORPREPAYMENT);
			vendorListForm.add(vendorPrepaymentItem);
		}

		vendorMenuForm.add(vendorLabel);
		vendorMenuForm.add(vendorListForm);

		return vendorMenuForm;
	}

	private DynamicForm getCustomerMenu(String customer) {

		DynamicForm customerMenuForm = new DynamicForm("customerMenuForm");

		Label customerLabel = new Label(customer);
		customerLabel.setStyleName("menuName");

		DynamicForm customerForm = new DynamicForm("customerForm");

		customerForm.add(customerLabel);

		if (canDoInvoiceAndBillTransactions || canDoBanking
				|| canDoManageAccounts) {
			if (isDoyouwantEstimates) {
				W8MenuItem quoteItem = new W8MenuItem(messages.newQuote(),
						"Create a new quote to provide to your customer.",
						HistoryTokens.NEWQUOTE);
				customerForm.add(quoteItem);
			}

			W8MenuItem invoiceItem = new W8MenuItem(messages.newInvoice(),
					"Generate a new invoice here.", HistoryTokens.NEWINVOICE);
			customerForm.add(invoiceItem);
			if (isDelayedchargesEnabled) {
				W8MenuItem chargeItem = new W8MenuItem(messages.newCharge(),
						"", HistoryTokens.NEWCHARGE);
				W8MenuItem creditItem = new W8MenuItem(messages.newCredit(),
						"", HistoryTokens.NEWCREDIT);

				customerForm.add(chargeItem);
				customerForm.add(creditItem);
			}
		}

		if (canDoBanking || canDoManageAccounts) {
			W8MenuItem cashSaleItem = new W8MenuItem(messages.newCashSale(),
					"", HistoryTokens.NEWCASHSALE);
			customerForm.add(cashSaleItem);
		}

		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem customerCreditMemoItem = new W8MenuItem(
					messages.customerCreditNote(Global.get().Customer()),
					"Create customer related credit notes",
					HistoryTokens.NRECREDITNOTE);
			customerForm.add(customerCreditMemoItem);
		}
		customerMenuForm.add(customerLabel);
		customerMenuForm.add(customerForm);

		return customerMenuForm;
	}

	private DynamicForm getTaxMenu(String tax) {
		DynamicForm taxMenuForm = new DynamicForm("taxMenuForm");

		Label taxLabel = new Label(tax);
		taxLabel.setStyleName("menuName");

		W8MenuItem taxItemsItem = new W8MenuItem(messages.taxItemsList(),
				"Add new tax items for your transactions",
				HistoryTokens.VATITEMS);
		W8MenuItem taxCodesItem = new W8MenuItem(messages.taxCodesList(),
				"Add new tax codes", HistoryTokens.VATCODES);
		W8MenuItem taxAgencyItem = new W8MenuItem(messages.taxAgenciesList(),
				"Check the list of all tax agencies",
				HistoryTokens.TAXAGENCYLIST);

		DynamicForm taxListForm = new DynamicForm("taxListForm");

		taxListForm.add(taxItemsItem);
		taxListForm.add(taxCodesItem);
		taxListForm.add(taxAgencyItem);

		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem taxAdjustmentItem = new W8MenuItem(
					messages.taxAdjustment(), "", HistoryTokens.TAXADJUSTMENT);
			W8MenuItem fileTaxItem = new W8MenuItem(messages.fileTAX(),
					"File a tax", HistoryTokens.FILETAX);

			taxListForm.add(taxAdjustmentItem);
			taxListForm.add(fileTaxItem);
		}

		if (canDoManageAccounts) {
			W8MenuItem payTaxItem = new W8MenuItem(messages.payTax(),
					"Pay tax to government", HistoryTokens.PAYTAX);
			W8MenuItem taxRefundItem = new W8MenuItem(messages.tAXRefund(),
					"Monitor tax refunds", HistoryTokens.TAXREFUND);
			W8MenuItem taxHistoryItem = new W8MenuItem(messages.taxHistory(),
					"Get details of tax history items",
					HistoryTokens.TAXHISTORY);

			taxListForm.add(payTaxItem);
			taxListForm.add(taxRefundItem);
			taxListForm.add(taxHistoryItem);
		}
		taxMenuForm.add(taxLabel);
		taxMenuForm.add(taxListForm);

		return taxMenuForm;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public String getViewTitle() {
		return "Menus";
	}

	@Override
	public List getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

}
