package com.vimukti.accounter.web.client.ui.win8;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.countries.India;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
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

	private Set<String> features;

	private final ClientCompanyPreferences preferences = Global.get()
			.preferences();

	@Override
	public void init() {
		super.init();
		ICountryPreferences countryPreferences = Accounter.getCompany()
				.getCountryPreferences();
		setPreferencesandPermissions(preferences, Accounter.getUser(),
				countryPreferences, Accounter.getFeatures());
	}

	public void setPreferencesandPermissions(
			ClientCompanyPreferences preferences, ClientUser clientUser,
			ICountryPreferences countryPreferences, Set<String> features) {

		this.features = features;

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

	public boolean hasPermission(String feature) {
		return features.contains(feature);
	}

	private void getMenu() {

		StyledPanel mainMenuPanel = new StyledPanel("mainMenu");

		mainMenuPanel.add(getCompanyMenu(messages.company()));

		if (canDoTaxTransactions && isTrackTax) {
			mainMenuPanel.add(getVATMenu(messages.tax()));
		}

		mainMenuPanel.add(getCustomerMenu(Global.get().Customer()));

		mainMenuPanel.add(getVendorMenu(Global.get().Vendor()));

		if (canDoBanking) {
			mainMenuPanel.add(getBankingMenu(messages.banking()));
		}

		if (canDoInventory && isInventoryEnabled) {
			mainMenuPanel.add(getInventoryMenu(messages.inventory()));
		}

		// this.addMenu(getFixedAssetsMenu(messages.fixedAssets()));

		if (canViewReports) {
			mainMenuPanel.add(getReportMenu(messages.reports()));
		}
		if (canChangeSettings || canDoUserManagement) {
			mainMenuPanel.add(getSettingsMenu(messages.settings()));
		}

		this.add(mainMenuPanel);
		addStyleName("mainMenuView");

	}

	private StyledPanel getSettingsMenu(String settings) {
		StyledPanel settingsMenuForm = new StyledPanel("settingsMenuForm");

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

	private StyledPanel getReportMenu(String reports) {
		StyledPanel reportMenu = new StyledPanel("reportMenu");

		return reportMenu;
	}

	private StyledPanel getInventoryMenu(String inventory) {

		StyledPanel inventoryMenuForm = new StyledPanel("inventoryMenuForm");

		DynamicForm inventoryListForm = new DynamicForm("inventoryListForm");

		Label inventoryLabel = new Label(inventory);
		inventoryLabel.setStyleName("menuName");

		W8MenuItem stockAdjustmentsItem = new W8MenuItem(
				messages.stockAdjustments(), "Perform the stock adjustments",
				HistoryTokens.STOCKADJUSTMENTS);
		W8MenuItem warehouseDetailsItem = new W8MenuItem("",
				"Get the details of warehouses your company is having.",
				HistoryTokens.WAREHOUSELIST);
		W8MenuItem warehouseTransfersItem = new W8MenuItem("",
				"Get the history of all previous warehouse transfers",
				HistoryTokens.WAREHOUSETRANSFERLIST);
		W8MenuItem measurementItem = new W8MenuItem("Measurements",
				"Create your predefined measurements",
				HistoryTokens.MEASUREMENTLIST);

		inventoryListForm.add(inventoryLabel);
		inventoryListForm.add(stockAdjustmentsItem);
		inventoryListForm.add(warehouseDetailsItem);
		inventoryListForm.add(warehouseTransfersItem);
		inventoryListForm.add(measurementItem);

		inventoryMenuForm.add(inventoryLabel);
		inventoryMenuForm.add(inventoryListForm);

		return inventoryMenuForm;
	}

	private StyledPanel getBankingMenu(String banking) {

		StyledPanel bankingMenuForm = new StyledPanel("bankingMenuForm");

		DynamicForm bankingListForm = new DynamicForm("bankingListForm");

		Label bankingLabel = new Label(banking);
		bankingLabel.setStyleName("menuName");

		W8MenuItem bankAccountItem = new W8MenuItem(messages.bankAccount(), "",
				HistoryTokens.NEWBANKACCOUNT);
		W8MenuItem writeCheckItem = new W8MenuItem(messages.writeCheck(), "",
				HistoryTokens.WRITECHECK);
		W8MenuItem depositAndTransfersItem = new W8MenuItem(
				messages.depositTransferFunds(),
				"Deposite or transfer funds in between your accounts.",
				HistoryTokens.DEPOSITETRANSFERFUNDS);
		W8MenuItem reconciliationItem = new W8MenuItem(
				messages.ReconciliationsList(),
				"Compare your bank statements with your transactions.",
				HistoryTokens.RECOUNCILATIONSLIST);

		bankingListForm.add(bankAccountItem);
		bankingListForm.add(writeCheckItem);
		bankingListForm.add(depositAndTransfersItem);
		bankingListForm.add(reconciliationItem);

		bankingMenuForm.add(bankingLabel);
		bankingMenuForm.add(bankingListForm);

		return bankingMenuForm;
	}

	private StyledPanel getVendorMenu(String vendor) {

		StyledPanel vendorMenuForm = new StyledPanel("vendorMenuForm");

		DynamicForm vendorListForm = new DynamicForm("vendorListForm");

		Label vendorLabel = new Label(vendor);
		vendorLabel.setStyleName("menuName");

		W8MenuItem vendorCenterItem = new W8MenuItem(
				messages.vendorCentre(Global.get().Vendor()), "",
				HistoryTokens.VENDORCENTRE);

		vendorListForm.add(vendorCenterItem);

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

		if (canDoInvoiceAndBillTransactions) {
			if (isKeepTrackofBills) {
				W8MenuItem enterBillItem = new W8MenuItem(messages.enterBill(),
						"", HistoryTokens.ENTERBILL);
				vendorListForm.add(enterBillItem);
			}
		}

		if (canDoPayBillAndReceivePayment) {
			if (isKeepTrackofBills) {
				W8MenuItem paybillItem = new W8MenuItem(messages.payBill(), "",
						HistoryTokens.PAYBILL);
				W8MenuItem printChequeItem = new W8MenuItem(
						messages.printCheque(), "", HistoryTokens.PRINTCHEQUE);

				vendorListForm.add(paybillItem);
				vendorListForm.add(printChequeItem);
			}
			W8MenuItem vendorPrepaymentItem = new W8MenuItem(
					messages.payeePrePayment(Global.get().Vendor()),
					"Add all vendor prepayments here.",
					HistoryTokens.VENDORPREPAYMENT);
			vendorListForm.add(vendorPrepaymentItem);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem recordExpenseItem = new W8MenuItem(
					messages.recordExpenses(), "", HistoryTokens.RECORDEXPENSES);

			vendorListForm.add(recordExpenseItem);
		}

		vendorMenuForm.add(vendorLabel);
		vendorMenuForm.add(vendorListForm);

		return vendorMenuForm;
	}

	private StyledPanel getCustomerMenu(String customer) {

		StyledPanel customerMenuForm = new StyledPanel("customerMenuForm");

		Label customerLabel = new Label(customer);
		customerLabel.setStyleName("menuName");

		DynamicForm customerForm = new DynamicForm("customerForm");

		W8MenuItem customerCenterItem = new W8MenuItem(
				messages.customerCentre(Global.get().Customer()), "",
				HistoryTokens.CUSTOMERCENTRE);

		customerForm.add(customerLabel);
		customerForm.add(customerCenterItem);

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

	private StyledPanel getVATMenu(String tax) {

		StyledPanel vatmenu = new StyledPanel("vatmenu");
		vatmenu.add(new Label(tax));

		DynamicForm menuForm = new DynamicForm("menuForm");

		if (canDoInvoiceAndBillTransactions) {
			StyledPanel vatNews = new StyledPanel("vatNews");
			vatNews.add(new Label(messages.new1()));

			DynamicForm submenuForm = new DynamicForm("submenuForm");

			W8MenuItem newTaxItem = new W8MenuItem(messages.newTaxItem(), "",
					HistoryTokens.NEWTAXITEM);
			submenuForm.add(newTaxItem);

			W8MenuItem newTaxCode = new W8MenuItem(messages.newTaxCode(), "",
					HistoryTokens.NEWVATCODE);
			submenuForm.add(newTaxCode);

			W8MenuItem newTAXAgency = new W8MenuItem(messages.newTAXAgency(),
					"", HistoryTokens.NEWTAXAGENCY);
			submenuForm.add(newTAXAgency);

			vatNews.add(submenuForm);

			menuForm.add(vatNews);
		}

		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem taxAdjustment = new W8MenuItem(messages.taxAdjustment(),
					"", HistoryTokens.TAXADJUSTMENT);
			menuForm.add(taxAdjustment);

			W8MenuItem fileTAX = new W8MenuItem(messages.fileTAX(), "",
					HistoryTokens.FILETAX);
			menuForm.add(fileTAX);
		}

		if (canDoManageAccounts) {
			W8MenuItem payTax = new W8MenuItem(messages.payTax(), "",
					HistoryTokens.PAYTAX);
			menuForm.add(payTax);

			W8MenuItem tAXRefund = new W8MenuItem(messages.tAXRefund(), "",
					HistoryTokens.TAXREFUND);
			menuForm.add(tAXRefund);

			W8MenuItem taxHistory = new W8MenuItem(messages.taxHistory(), "",
					HistoryTokens.TAXHISTORY);
			menuForm.add(taxHistory);
		}

		if (company instanceof India) {
			if (tdsEnabled) {
				menuForm.add(getDeductorMasterMenu(messages.deducatorMaster()));

				menuForm.add(getForm16AMenu(messages.tds()));
			}
		}
		W8MenuItem taxItemsList = new W8MenuItem(messages.taxItemsList(), "",
				HistoryTokens.VATITEMS);
		menuForm.add(taxItemsList);

		W8MenuItem taxCodesList = new W8MenuItem(messages.taxCodesList(), "",
				HistoryTokens.VATCODES);
		menuForm.add(taxCodesList);

		W8MenuItem payeeList = new W8MenuItem(messages.payeeList(messages
				.taxAgencies()), "", HistoryTokens.TAXAGENCYLIST);
		menuForm.add(payeeList);

		if (company instanceof India) {
			if (tdsEnabled) {
				W8MenuItem chalan = new W8MenuItem("Chalan Details List", "",
						HistoryTokens.CHALANDETAILSLIST);
				menuForm.add(chalan);
			}
		}

		vatmenu.add(menuForm);
		return vatmenu;
	}

	private StyledPanel getForm16AMenu(String tds) {

		StyledPanel form16menu = new StyledPanel("tds");
		form16menu.add(new Label(tds));

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem challanDetails = new W8MenuItem(messages.challanDetails(),
				"", HistoryTokens.CHALANDETAILS);
		menuForm.add(challanDetails);

		W8MenuItem eTDSFilling = new W8MenuItem(messages.eTDSFilling(), "",
				HistoryTokens.eTDSFILLING);
		menuForm.add(eTDSFilling);

		W8MenuItem Ack = new W8MenuItem("Enter Ack No.", "",
				HistoryTokens.ENTER_TDS_ACK_NO);
		menuForm.add(Ack);

		W8MenuItem f16A = new W8MenuItem("Form 16A", "",
				HistoryTokens.TDS_FORM16A);
		menuForm.add(f16A);

		form16menu.add(menuForm);
		return form16menu;
	}

	private StyledPanel getDeductorMasterMenu(String deducatorMaster) {

		StyledPanel eductorMasterMenu = new StyledPanel("deducatorMaster");
		eductorMasterMenu.add(new Label(deducatorMaster));

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem deducatorDetails = new W8MenuItem(
				messages.deducatorDetails(), "", HistoryTokens.DEDUCTORDETAILS);
		menuForm.add(deducatorDetails);

		W8MenuItem responsePersonDetails = new W8MenuItem(
				messages.responsePersonDetails(), "",
				HistoryTokens.PERSONDETAILS);
		menuForm.add(responsePersonDetails);

		eductorMasterMenu.add(menuForm);
		return eductorMasterMenu;
	}

	private StyledPanel getCompanyMenu(String company2) {

		StyledPanel companyMenuBar = new StyledPanel("companyMenuBar");
		companyMenuBar.add(new Label(company2));

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem dashBoard = new W8MenuItem(messages.dashBoard(), "",
				HistoryTokens.DASHBOARD);
		menuForm.add(dashBoard);

		if (canDoManageAccounts) {
			W8MenuItem journalEntry = new W8MenuItem(messages.journalEntry(),
					"", HistoryTokens.NEWJOURNALENTRY);
			menuForm.add(journalEntry);

		}
		W8MenuItem transactionscenter = new W8MenuItem(
				messages.transactionscenter(), "",
				HistoryTokens.TRANSACTIONS_CENTER);
		menuForm.add(transactionscenter);

		if (canDoManageAccounts) {
			W8MenuItem Account = new W8MenuItem(messages.Account(), "",
					HistoryTokens.NEWACCOUNT);
			menuForm.add(Account);
		}

		if (canChangeSettings) {
			W8MenuItem companyPreferences = new W8MenuItem(
					messages.companyPreferences(), "",
					HistoryTokens.COMPANYPREFERENCES);
			menuForm.add(companyPreferences);
		}

		if (canDoTaxTransactions) {
			W8MenuItem budget = new W8MenuItem(messages.budget(), "",
					HistoryTokens.BUDGET);
			menuForm.add(budget);

		}

		if (canDoTaxTransactions && isTrackTax) {
			menuForm.add(getSalesTaxSubmenu(messages.itemTax()));

		}
		if (canChangeSettings) {
			menuForm.add(getManageSupportListSubmenu(messages
					.manageSupportLists()));
		}

		if (canDoTaxTransactions) {
			menuForm.add(getFixedAssetsMenu(messages.fixedAssets()));

			if (hasPermission(Features.MERGING)) {
				menuForm.add(getMergeSubMenu(messages.mergeAccounts()));
			}
		}
		menuForm.add(getCompanyListMenu(messages.companyLists()));

		if (countryOrRegion.equals(CountryPreferenceFactory.SINGAPORE)) {
			W8MenuItem generateIrasAuditFile = new W8MenuItem(
					messages.generateIrasAuditFile(), "",
					HistoryTokens.GST_FILE);
			menuForm.add(generateIrasAuditFile);
		}

		companyMenuBar.add(menuForm);
		return companyMenuBar;
	}

	private StyledPanel getCompanyListMenu(String companyLists) {
		StyledPanel companyListMenuBar = new StyledPanel("companyListMenuBar");
		companyListMenuBar.add(new Label(companyLists));

		DynamicForm menuForm = new DynamicForm("menuForm");

		if (canSeeInvoiceTransactions) {
			W8MenuItem payeeList = new W8MenuItem(messages.payeeList(messages
					.Accounts()), "", HistoryTokens.ACCOUNTSLIST);
			menuForm.add(payeeList);
		}
		if (canSeeBanking) {
			W8MenuItem journalEntries = new W8MenuItem(
					messages.journalEntries(), "", HistoryTokens.JOURNALENTRIES);
			menuForm.add(journalEntries);
		}

		if (canSeeInvoiceTransactions) {
			W8MenuItem items = new W8MenuItem(messages.items(), "",
					HistoryTokens.ALLITEMS);
			menuForm.add(items);
		}
		W8MenuItem Customers = new W8MenuItem(Global.get().Customers(), "",
				HistoryTokens.CUSTOMERS);
		menuForm.add(Customers);

		W8MenuItem Vendors = new W8MenuItem(Global.get().Vendors(), "",
				HistoryTokens.VENDORLIST);
		menuForm.add(Vendors);
		if (canSeeBanking) {
			W8MenuItem payments = new W8MenuItem(messages.payments(), "",
					HistoryTokens.PAYMENTS);
			menuForm.add(payments);
		}
		W8MenuItem salesPersons = new W8MenuItem(messages.salesPersons(), "",
				HistoryTokens.SALESPRESONS);
		menuForm.add(salesPersons);

		if (hasPermission(Features.USER_ACTIVITY)) {
			W8MenuItem usersActivityLogTitle = new W8MenuItem(
					messages.usersActivityLogTitle(), "",
					HistoryTokens.USERACTIVITY);
			menuForm.add(usersActivityLogTitle);
		}
		W8MenuItem recurringTransactions = new W8MenuItem(
				messages.recurringTransactions(), "",
				HistoryTokens.RECURRINGTRANSACTIONS);
		menuForm.add(recurringTransactions);

		W8MenuItem remindersList = new W8MenuItem(messages.remindersList(), "",
				HistoryTokens.RECURRINGREMINDERS);
		menuForm.add(remindersList);

		companyListMenuBar.add(menuForm);
		return companyListMenuBar;

	}

	private StyledPanel getMergeSubMenu(String mergeAccounts) {

		StyledPanel mergeAccountsMenuBar = new StyledPanel(
				"mergeAccountsMenuBar");
		mergeAccountsMenuBar.add(new Label(mergeAccounts));

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem mergeCustomers = new W8MenuItem(
				messages.mergeCustomers(Global.get().Customers()), "",
				HistoryTokens.MERGECUSTOMERS);
		menuForm.add(mergeCustomers);

		W8MenuItem mergeVendors = new W8MenuItem(messages.mergeVendors(Global
				.get().Vendors()), "", HistoryTokens.MERGEVENDOR);
		menuForm.add(mergeVendors);

		W8MenuItem remindersList = new W8MenuItem(messages.mergeAccounts(), "",
				HistoryTokens.MERGEACCOUNT);
		menuForm.add(remindersList);

		W8MenuItem mergeItems = new W8MenuItem(messages.mergeItems(), "",
				HistoryTokens.MERGEITEM);
		menuForm.add(mergeItems);

		mergeAccountsMenuBar.add(menuForm);
		return mergeAccountsMenuBar;
	}

	private StyledPanel getFixedAssetsMenu(String fixedAssets) {

		StyledPanel fixedAssetMenu = new StyledPanel("fixedAssetMenu");
		fixedAssetMenu.add(new Label(fixedAssets));

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem newFixedAsset = new W8MenuItem(messages.newFixedAsset(), "",
				HistoryTokens.NEWFIXEDASSETS);
		menuForm.add(newFixedAsset);

		W8MenuItem depreciation = new W8MenuItem(messages.depreciation(), "",
				HistoryTokens.DEPRICATION);
		menuForm.add(depreciation);

		W8MenuItem pendingItemsList = new W8MenuItem(
				messages.pendingItemsList(), "", HistoryTokens.PENDINGITEMS);
		menuForm.add(pendingItemsList);

		W8MenuItem registeredItemsList = new W8MenuItem(
				messages.registeredItemsList(), "",
				HistoryTokens.REGISTEREDITEMS);
		menuForm.add(registeredItemsList);

		W8MenuItem soldAndDisposedItems = new W8MenuItem(
				messages.soldAndDisposedItems(), "",
				HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);
		menuForm.add(soldAndDisposedItems);

		fixedAssetMenu.add(menuForm);
		return fixedAssetMenu;
	}

	private StyledPanel getManageSupportListSubmenu(String manageSupportLists) {

		StyledPanel manageSupportListMenuBar = new StyledPanel(
				"manageSupportListMenuBar");
		manageSupportListMenuBar.add(new Label(manageSupportLists));

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem payeeGroupList = new W8MenuItem(
				messages.payeeGroupList(Global.get().Customer()), "",
				HistoryTokens.CUSTOMERGROUPLIST);
		menuForm.add(payeeGroupList);

		W8MenuItem vendrGroupList = new W8MenuItem(
				messages.payeeGroupList(Global.get().Vendor()), "",
				HistoryTokens.VENDORGROUPLIST);
		menuForm.add(vendrGroupList);

		W8MenuItem paymentTermList = new W8MenuItem(messages.paymentTermList(),
				"", HistoryTokens.PAYMENTTERMS);
		menuForm.add(paymentTermList);

		W8MenuItem shippingMethodList = new W8MenuItem(
				messages.shippingMethodList(), "",
				HistoryTokens.SHIPPINGMETHODSLIST);
		menuForm.add(shippingMethodList);

		W8MenuItem shippingTermList = new W8MenuItem(
				messages.shippingTermList(), "",
				HistoryTokens.SHIPPINGTERMSLIST);
		menuForm.add(shippingTermList);

		W8MenuItem itemGroupList = new W8MenuItem(messages.itemGroupList(), "",
				HistoryTokens.ITEMGROUPLIST);
		menuForm.add(itemGroupList);

		W8MenuItem currencyList = new W8MenuItem(messages.currencyList(), "",
				HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);
		menuForm.add(currencyList);

		if (hasPermission(Features.CLASS)) {
			if (isClassTracking) {
				W8MenuItem accounterClassList = new W8MenuItem(
						messages.accounterClassList(), "",
						HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);
				menuForm.add(accounterClassList);

			}
		}
		if (hasPermission(Features.LOCATION)) {
			if (isLocationTracking) {
				W8MenuItem locationsList = new W8MenuItem(
						messages.locationsList(Global.get().Location()), "",
						HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);
				menuForm.add(locationsList);

			}
		}
		if (isPriceLevelEnabled) {
			W8MenuItem priceLevelList = new W8MenuItem(
					messages.priceLevelList(), "", HistoryTokens.PRICELEVELLIST);
			menuForm.add(priceLevelList);
		}

		manageSupportListMenuBar.add(menuForm);
		return manageSupportListMenuBar;

	}

	private StyledPanel getSalesTaxSubmenu(String itemTax) {

		StyledPanel salesTaxMenuBar = new StyledPanel("salesTaxMenuBar");
		salesTaxMenuBar.add(new Label(itemTax));

		DynamicForm menuForm = new DynamicForm("menuForm");

		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem manageSalesTaxGroups = new W8MenuItem(
					messages.manageSalesTaxGroups(), "",
					HistoryTokens.MANAGESALESTAXGROUP);
			menuForm.add(manageSalesTaxGroups);
		} else {
			W8MenuItem salesTaxGroups = new W8MenuItem(
					messages.salesTaxGroups(), "",
					HistoryTokens.SALESTAXGROUPsalesTaxGroup);
			menuForm.add(salesTaxGroups);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem manageSalesItems = new W8MenuItem(
					messages.manageSalesItems(), "",
					HistoryTokens.MANAGESALESTAXITEMS);
			menuForm.add(manageSalesItems);
		} else {
			W8MenuItem salesTaxItems = new W8MenuItem(messages.salesTaxItems(),
					"", HistoryTokens.SALESTAXITEMS);
			menuForm.add(salesTaxItems);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem taxAdjustment = new W8MenuItem(messages.taxAdjustment(),
					"", HistoryTokens.TAXADJUSTMENT);
			menuForm.add(taxAdjustment);
		}
		if (canDoManageAccounts) {
			W8MenuItem payTax = new W8MenuItem(messages.payTax(), "",
					HistoryTokens.PAYSALESTAX);
			menuForm.add(payTax);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem newTAXAgency = new W8MenuItem(messages.newTAXAgency(),
					"", HistoryTokens.NEWTAXAGENCY);
			menuForm.add(newTAXAgency);
		}
		return salesTaxMenuBar;
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
