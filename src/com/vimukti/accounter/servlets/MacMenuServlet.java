package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class MacMenuServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			Company company = getCompany(req);
			generateXML(company, emailId);
			resp.setContentType("text/xml");
			if (builder != null) {
				resp.getOutputStream().write(builder.toString().getBytes());
			}
		}

	}

	private StringBuilder builder;
	private User user;
	private Company company;
	private CompanyPreferences preferences;
	private IGlobal iGlobal;

	private void generateXML(Company company, String emailId)
			throws IOException {
		iGlobal = new ServerGlobal();
		if (company == null) {
			return;
		}
		User user = null;
		Iterator<User> iterator = company.getUsers().iterator();
		while (iterator.hasNext()) {
			User next = iterator.next();
			if (next.getEmail().endsWith(emailId)) {
				user = next;
				break;
			}
		}
		if (user == null) {
			return;
		}
		CompanyPreferences preferences = company.getPreferences();
		this.user = user;
		this.company = company;
		this.preferences = preferences;

		builder = new StringBuilder();

		addHeader();

		addCompanyMenuItem();

		if (isUKType()) {
			if (preferences.getDoYouPaySalesTax()) {
				addVatMenuItem();
			}
		}
		addCustomerMenuItem();

		addVendorMenuItem();

		if (canDoBanking()) {
			addBankingMenuItem();
		}

		if (preferences.isSalesOrderEnabled()) {
			addSalesOrderMenuItem();
		}

		if (preferences.isPurchaseOrderEnabled()) {
			addPurchaseMenuItem();
		}

		if (canViewReports()) {
			addReportsMenuItem();
		}

		if (canChangeSettings()) {
			addSettingsMenuItem();
		}

		addFooter();
	}

	private boolean canManageFiscalYears() {
		return user.getPermissions().getTypeOfLockDates() == RolePermissions.TYPE_YES;
	}

	private boolean canDoInvoiceTransactions() {
		return user.getPermissions().getTypeOfInvoices() == RolePermissions.TYPE_YES;
	}

	private boolean canChangeSettings() {
		return user.getPermissions().getTypeOfSystemSettings() == RolePermissions.TYPE_YES;
	}

	private boolean canViewReports() {
		return user.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_READ_ONLY;
	}

	private boolean canDoBanking() {
		return user.getPermissions().getTypeOfBankReconcilation() == RolePermissions.TYPE_YES
				|| user.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES;
	}

	private boolean isUKType() {
		return company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK;
	}

	private boolean isUSType() {
		return company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US;
	}

	private void addFooter() {
		builder.append("</menus></xml>");
	}

	private void addHeader() {
		builder.append("<xml><AccounterLogout text=\"Logout\">do/logout</AccounterLogout><ChangePassword text= \"User Details\">accounter#userDetails</ChangePassword><menus>");
	}

	private void addVatMenuItem() {
		StringBuilder mainValue = new StringBuilder();

		if (canDoInvoiceTransactions()) {
			StringBuilder newValue = new StringBuilder();
			subMenu(newValue, iGlobal.constants().newVATItem(), "V",
					"accounter#newVatItem");
			subMenu(newValue, iGlobal.constants().newVATCode(),
					"accounter#newVatCode");
			subMenu(newValue, iGlobal.constants().newVATAgency(),
					"accounter#newVatAgency");
			menu(mainValue, iGlobal.constants().new1(), newValue);
			separator(mainValue);
		}

		if (canDoInvoiceTransactions()) {
			menu(mainValue, iGlobal.constants().vatAdjustment(),
					"accounter#vatAdjustment");
			menu(mainValue, iGlobal.constants().fileVAT(),
					"accounter#fileVAT");
		}

		if (canDoBanking()) {
			menu(mainValue, iGlobal.constants().payVAT(), "accounter#payVat");
			menu(mainValue, iGlobal.constants().receiveVAT(),
					"accounter#receiveVat");
		}

		separator(mainValue);

		StringBuilder vatListValue = new StringBuilder();
		subMenu(vatListValue, iGlobal.constants().vatItems(), "V",
				"accounter#vatItems");
		subMenu(vatListValue, iGlobal.constants().vatCodes(),
				"accounter#vatCodes");
		menu(mainValue, iGlobal.constants().vatList(), vatListValue);

		mainMenu(builder, iGlobal.constants().vat(), mainValue);
	}

	private void addSettingsMenuItem() {
		StringBuilder settingsValue = new StringBuilder();
		menu(settingsValue, iGlobal.constants().generalSettings(),
				"accounter#generalSettings");
		mainMenu(builder, iGlobal.constants().settings(), settingsValue);
	}

	private void addReportsMenuItem() {
		StringBuilder reportsValue = new StringBuilder();

		StringBuilder financialValue = new StringBuilder();
		subMenu(financialValue, iGlobal.constants().profitAndLoss(),
				"accounter#profitAndLoss");
		subMenu(financialValue, iGlobal.constants().balanceSheet(),
				"accounter#balanceSheet");
		subMenu(financialValue, iGlobal.constants().cashFlowReport(),
				"accounter#cashFlowReport");
		subMenu(financialValue, iGlobal.constants().trialBalance(),
				"accounter#trialBalance");
		subMenu(financialValue, iGlobal.messages()
				.transactionDetailByAccount(Global.get().Account()),
				"accounter#transactionDetailByAccount");
		if (isUSType()) {
			subMenu(financialValue,
					iGlobal.constants().generalLedgerReport(),
					"accounter#generalLedger");
		}
		subMenu(financialValue, iGlobal.constants().expenseReport(),
				"accounter#expenseReport");
		if (isUSType()) {
			subMenu(financialValue, iGlobal.constants().salesTaxLiability(),
					"accounter#salesTaxLiability");
			subMenu(financialValue, iGlobal.constants()
					.transactionDetailByTaxItem(),
					"accounter#transactionDetailByTaxItem");
		}
		menu(reportsValue, iGlobal.constants().companyAndFinance(),
				financialValue);

		StringBuilder receivablesValue = new StringBuilder();
		subMenu(receivablesValue, iGlobal.constants().arAgeingSummary(),
				"accounter#arAgingSummary");
		subMenu(receivablesValue, iGlobal.constants().arAgeingDetail(),
				"accounter#arAgingDetail");
		subMenu(receivablesValue,
				iGlobal.messages().customerStatement(Global.get().Customer()),
				"accounter#customerStatement");
		subMenu(receivablesValue, iGlobal.messages()
				.customerTransactionHistory(Global.get().Customer()),
				"accounter#customerTransactionHistory");
		menu(reportsValue,
				iGlobal.messages().customersAndReceivable(
						Global.get().Customer()), receivablesValue);

		StringBuilder salesValue = new StringBuilder();
		subMenu(salesValue,
				iGlobal.messages().salesByCustomerSummary(
						Global.get().Customer()),
				"accounter#salesByCustomerSummary");
		subMenu(salesValue,
				iGlobal.messages().salesByCustomerDetail(
						Global.get().Customer()),
				"accounter#salesByCustomerDetail");
		subMenu(salesValue, iGlobal.constants().salesByItemSummary(),
				"accounter#salesByItemSummary");
		subMenu(salesValue, iGlobal.constants().salesByItemDetail(),
				"accounter#salesByItemDetail");
		if (preferences.isSalesOrderEnabled()) {
			subMenu(salesValue, iGlobal.constants().salesOrderReport(),
					"accounter#salesOrderReport");
		}
		menu(reportsValue, iGlobal.constants().sales(), salesValue);

		StringBuilder suppliersValue = new StringBuilder();
		subMenu(suppliersValue, iGlobal.constants().apAgeingSummary(),
				"accounter#apAgingSummary");
		subMenu(suppliersValue, iGlobal.constants().apAgeingDetail(),
				"accounter#apAgingDetail");
		subMenu(suppliersValue,
				iGlobal.messages().vendorTransactionHistory(
						Global.get().Vendor()),
				"accounter#supplierTransactionHistory");
		menu(reportsValue,
				iGlobal.messages().vendorsAndPayables(Global.get().Vendor()),
				suppliersValue);

		StringBuilder purchasesValue = new StringBuilder();
		subMenu(purchasesValue,
				iGlobal.messages().purchaseByVendorSummary(
						Global.get().Vendor()),
				"accounter#purchaseBySupplierSummary");
		subMenu(purchasesValue,
				iGlobal.messages().purchaseByVendorDetail(
						Global.get().Vendor()),
				"accounter#purchaseBySupplierDetail");
		subMenu(purchasesValue, iGlobal.constants().purchaseByItemSummary(),
				"accounter#purchaseByItemSummary");
		subMenu(purchasesValue,
				iGlobal.constants().purchaseByProductDetail(),
				"accounter#purchaseByItemDetail");
		if (preferences.isPurchaseOrderEnabled()) {
			subMenu(purchasesValue,
					iGlobal.constants().purchaseOrderReport(),
					"accounter#purchaseOrderReport");
		}
		menu(reportsValue, iGlobal.constants().purchases(), purchasesValue);

		if (preferences.getDoYouPaySalesTax()) {
			StringBuilder vatValue = new StringBuilder();
			subMenu(vatValue, iGlobal.constants().priorVATReturns(),
					"accounter#priorVatReturns");
			subMenu(vatValue, iGlobal.constants().vatDetail(),
					"accounter#vatDetail");
			subMenu(vatValue, iGlobal.constants().vat100(),
					"accounter#vat100");
			subMenu(vatValue, iGlobal.constants().uncategorisedVATAmounts(),
					"accounter#uncategorisedVatAmounts");
			subMenu(vatValue, iGlobal.constants().vatItemSummary(),
					"accounter#vatItemSummary");
			subMenu(vatValue, iGlobal.constants().ecSalesList(),
					"accounter#ecSalesList");
			menu(reportsValue, iGlobal.constants().vat(), vatValue);
		}
		mainMenu(builder, iGlobal.constants().reports(), reportsValue);
	}

	private void addPurchaseMenuItem() {
		StringBuilder purchaValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(purchaValues, iGlobal.constants().purchaseOrder(),
					"accounter#purchaseOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(purchaValues, iGlobal.constants().purchaseOrderList(),
					"accounter#purchaseOrderList");
		}
		if (canViewReports()) {
			menu(purchaValues, iGlobal.constants().purchaseOrderReport(),
					"accounter#purchaseOrderReport");
		}
		mainMenu(builder, iGlobal.constants().purchases(), purchaValues);
	}

	private void addSalesOrderMenuItem() {
		StringBuilder salesValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(salesValues, iGlobal.constants().salesOrder(),
					"accounter#salesOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(salesValues, iGlobal.constants().salesOrderList(),
					"accounter#salesOrderList");
		}
		if (canViewReports()) {
			menu(salesValues, iGlobal.constants().salesOrderReport(), "r",
					"accounter#salesOrderReport");
		}
		mainMenu(builder, iGlobal.constants().sales(), salesValues);
	}

	private void addBankingMenuItem() {
		StringBuilder bankingValues = new StringBuilder();
		menu(bankingValues,
				iGlobal.messages().newBankAccount(Global.get().Account()),
				"b", "accounter#newBankAccount");
		separator(bankingValues);
		if (isUSType()) {
			menu(bankingValues, iGlobal.constants().writeCheck(),
					"accounter#writeCheck");
		}
		menu(bankingValues, iGlobal.constants().depositTransferFunds(),
				"accounter#depositTransferFunds");
		menu(bankingValues, iGlobal.constants().payBills(),
				"accounter#payBill");
		separator(bankingValues);
		menu(bankingValues, iGlobal.constants().newCreditCardCharge(),
				"accounter#creditCardCharge");
		separator(bankingValues);
		StringBuilder bankListValues = new StringBuilder();
		subMenu(bankListValues, iGlobal.constants().payments(),
				"accounter#payments");
		menu(bankingValues, iGlobal.constants().bankingList(), bankListValues);
		mainMenu(builder, iGlobal.constants().banking(), bankingValues);
	}

	private void addVendorMenuItem() {

		StringBuilder vendorValue = new StringBuilder();
		menu(vendorValue,
				iGlobal.messages().vendorsHome(Global.get().Vendor()), "S",
				"accounter#vendorHome");
		separator(vendorValue);

		int items = 0;
		StringBuilder newValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			subMenu(newValues,
					iGlobal.messages().newVendor(Global.get().Vendor()),
					"accounter#newVendor");
			subMenu(newValues, iGlobal.constants().newItem(),
					"accounter#newItem");
			items += 2;
		}
		if (canDoBanking()) {
			subMenu(newValues, iGlobal.constants().cashPurchase(),
					"accounter#newCashPurchase");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValues,
					iGlobal.messages().vendorCredit(Global.get().Vendor()),
					"accounter#vendorCredit");
			if (isUSType()) {
				subMenu(newValues, iGlobal.constants().newCheck(),
						"accounter#check");
				items += 1;
			}
			items += 1;
		}
		if (items > 0) {
			menu(vendorValue, iGlobal.constants().new1(), newValues);
			separator(vendorValue);
		}

		if (canDoInvoiceTransactions()) {
			menu(vendorValue, iGlobal.constants().enterBill(), "B",
					"accounter#enterBill");
		}
		if (canDoBanking()) {
			menu(vendorValue, iGlobal.constants().payBills(),
					"accounter#payBill");
			menu(vendorValue, iGlobal.constants().issuePayments(),
					"accounter#issuePayments");
			menu(vendorValue,
					iGlobal.messages()
							.vendorPrePayment(Global.get().Vendor()),
					"accounter#vendorPrePayment");
		}
		if (canDoInvoiceTransactions()) {
			menu(vendorValue, iGlobal.constants().recordExpenses(),
					"accounter#recordExpenses");
			if (preferences.isHaveEpmloyees()
					&& preferences.isTrackEmployeeExpenses()) {
				menu(vendorValue, iGlobal.constants().expenseClaims(),
						"accounter#expenseClaims");
			}
			separator(vendorValue);
		}

		StringBuilder supplierValues = new StringBuilder();
		subMenu(supplierValues,
				iGlobal.messages().vendors(Global.get().Vendor()),
				"accounter#VendorList");
		if (canSeeInvoiceTransactions()) {
			subMenu(supplierValues, iGlobal.constants().items(),
					"accounter#items");
			subMenu(supplierValues, "Bills And Expenses",
					"accounter#billsAndExpenses");
		}
		if (canSeeBanking()) {
			subMenu(supplierValues,
					iGlobal.messages().vendorPayments(Global.get().Vendor()),
					"accounter#vendorPayments");
		}
		menu(vendorValue,
				iGlobal.messages().vendorLists(Global.get().Vendor()),
				supplierValues);

		mainMenu(builder, Global.get().Vendor(), vendorValue);
	}

	private void addCustomerMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();
		menu(mainMenuValue,
				iGlobal.messages().customersHome(Global.get().Customer()),
				"accounter#customerHome");
		separator(mainMenuValue);

		int items = 0;
		StringBuilder newValue = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			subMenu(newValue,
					iGlobal.messages().newCustomer(Global.get().Customer()),
					"C", "accounter#newCustomer");
			subMenu(newValue, iGlobal.constants().newItem(),
					"accounter#newCustomerItem");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(newValue, iGlobal.constants().newQuote(),
						"accounter#newQuote");
				items += 1;
			}
			subMenu(newValue, iGlobal.constants().newInvoice(),
					"accounter#newInvoice");
			items += 3;
		}
		if (canDoBanking()) {
			subMenu(newValue, iGlobal.constants().newCashSale(),
					"accounter#newCashSale");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValue, iGlobal.constants().newCredit(),
					"accounter#newCredit");
			items += 1;
		}
		if (items > 0) {
			menu(mainMenuValue, iGlobal.constants().new1(), newValue);
			separator(mainMenuValue);
		}

		if (canDoBanking()) {
			menu(mainMenuValue,
					iGlobal.messages().customerPrePayment(
							Global.get().Customer()),
					"accounter#customerPrepayment");
			menu(mainMenuValue, iGlobal.constants().receivePayment(),
					"accounter#receivePayment");
			menu(mainMenuValue,
					iGlobal.messages()
							.customerRefund(Global.get().Customer()),
					"accounter#customerRefund");
			separator(mainMenuValue);
		}

		StringBuilder customerListValue = new StringBuilder();
		subMenu(customerListValue,
				iGlobal.messages().customers(Global.get().Customer()),
				"accounter#customers");
		if (canSeeInvoiceTransactions()) {
			subMenu(customerListValue, iGlobal.constants().items(),
					"accounter#items");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(customerListValue, iGlobal.constants().quotes(),
						"accounter#quotes");
			}
			subMenu(customerListValue, iGlobal.constants().invoices(),
					"accounter#invoices");
		}
		if (canSeeBanking()) {
			subMenu(customerListValue,
					iGlobal.constants().receivedPayments(),
					"accounter#receivePayments");
			subMenu(customerListValue,
					iGlobal.messages().customerRefunds(
							Global.get().Customer()),
					"accounter#customerRefunds");
		}
		menu(mainMenuValue,
				iGlobal.messages().customerList(Global.get().Customer()),
				customerListValue);

		mainMenu(builder, Global.get().Customer(), mainMenuValue);
	}

	private void addCompanyMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();

		menu(mainMenuValue, iGlobal.constants().dashBoard(), "D",
				"accounter#dashBoard");
		separator(mainMenuValue);

		if (canDoBanking()) {
			menu(mainMenuValue, iGlobal.constants().journalEntry(), "J",
					"accounter#newJournalEntry");
		}

		if (canDoInvoiceTransactions()) {
			menu(mainMenuValue,
					iGlobal.messages().newAccount(Global.get().Account()),
					"A", "accounter#newAccount");
			separator(mainMenuValue);
		}

		if (canChangeSettings()) {
			menu(mainMenuValue, iGlobal.constants().companyPreferences(),
					"accounter#companyPreferences");
			separator(mainMenuValue);
		}

		if (isUSType()) {
			if (preferences.getDoYouPaySalesTax()) {
				StringBuilder salesTaxValues = new StringBuilder();
				if (canDoInvoiceTransactions()) {
					subMenu(salesTaxValues, iGlobal.constants()
							.manageSalesTaxGroups(),
							"accounter#manageSalesTaxGroups");
				} else {
					subMenu(salesTaxValues, iGlobal.constants()
							.salesTaxGroups(), "accounter#salesTaxGroups");
				}
				if (canDoInvoiceTransactions()) {
					subMenu(salesTaxValues, iGlobal.constants()
							.manageSalesItems(),
							"accounter#manageSalesTaxItems");
				} else {
					subMenu(salesTaxValues, iGlobal.constants()
							.salesTaxItems(), "accounter#salesTaxItems");
				}

				if (canDoBanking()) {
					subMenu(salesTaxValues,
							iGlobal.constants().paySalesTax(),
							"accounter#customerGroupList");
				}

				if (canDoInvoiceTransactions()) {
					if (isUKType()) {
						subMenu(salesTaxValues, iGlobal.constants()
								.newVATAgency(), "accounter#newVatAgency");
					} else {
						subMenu(salesTaxValues, iGlobal.constants()
								.newTAXAgency(), "accounter#newTaxAgency");
					}
				}
				menu(mainMenuValue, iGlobal.constants().itemTax(),
						salesTaxValues);
			}
		}

		if (canChangeSettings()) {
			StringBuilder manageSupportLists = new StringBuilder();
			subMenu(manageSupportLists,
					iGlobal.messages().customerGroupList(
							Global.get().Customer()),
					"accounter#customerGroupList");
			subMenu(manageSupportLists,
					iGlobal.messages().vendorGroupList(Global.get().vendor()),
					"accounter#supplierGroupList");
			subMenu(manageSupportLists,
					iGlobal.constants().paymentTermList(),
					"accounter#paymentTerms");
			subMenu(manageSupportLists, iGlobal.constants()
					.shippingMethodList(), "accounter#shippingMethodsList");
			subMenu(manageSupportLists, iGlobal.constants()
					.shippingTermList(), "accounter#shippingTermsList");
			subMenu(manageSupportLists, iGlobal.constants().priceLevelList(),
					"accounter#priceLevels");
			subMenu(manageSupportLists, iGlobal.constants().itemGroupList(),
					"accounter#itemGroupList");
			subMenu(manageSupportLists, iGlobal.constants()
					.creditRatingList(), "accounter#creditRatingList");
			menu(mainMenuValue, iGlobal.constants().managerSupportLists(),
					manageSupportLists);
		}

		if (canManageFiscalYears()) {
			menu(mainMenuValue, iGlobal.constants().manageFiscalYear(),
					"accounter#manageFiscalYear");
			separator(mainMenuValue);
		}

		// TODO Next version
		// StringBuilder mergeValue = new StringBuilder();
		// subMenu(mergeValue, "Merge Customers", "accounter#mergeCustomers");
		// subMenu(mergeValue, "Merge Vendor", "accounter#mergeVendors");
		// subMenu(mergeValue, "Merge Account", "accounter#mergeAccount");
		// subMenu(mergeValue, "Merge Item", "accounter#mergeItem");
		// menu(mainMenuValue, "Merge Accounts", mergeValue);
		// separator(mainMenuValue);

		StringBuilder companyLists = new StringBuilder();
		if (canSeeInvoiceTransactions()) {
			subMenu(companyLists,
					iGlobal.messages().accountsList(Global.get().Account()),
					"accounter#accountsList");
		}
		if (canSeeBanking()) {
			subMenu(companyLists, iGlobal.constants().journalEntries(),
					"accounter#journalEntries");
		}

		if (canSeeInvoiceTransactions()) {
			subMenu(companyLists, iGlobal.constants().items(),
					"accounter#items");
		}
		subMenu(companyLists,
				iGlobal.messages().companyCustomers(Global.get().Customer()),
				"accounter#customers");
		subMenu(companyLists,
				iGlobal.messages().companySuppliers(Global.get().Vendor()),
				"accounter#supplierList");
		if (canSeeBanking()) {
			subMenu(companyLists, iGlobal.constants().payments(),
					"accounter#payments");
		}
		subMenu(companyLists, iGlobal.constants().salesPersons(),
				"accounter#salesPersons");
		menu(mainMenuValue, iGlobal.constants().companyLists(), companyLists);

		mainMenu(builder, iGlobal.constants().company(), mainMenuValue);
	}

	private boolean canSeeBanking() {
		return user.getPermissions().getTypeOfBankReconcilation() != RolePermissions.TYPE_NO;
	}

	private boolean canSeeInvoiceTransactions() {
		return user.getPermissions().getTypeOfInvoices() != RolePermissions.TYPE_NO;
	}

	private void menu(StringBuilder builder, String text, String shortcut,
			String value) {
		// <Menu text="Dashboard" shortcut="D">accounter#dashBoard</Menu>
		builder.append("	<Menu text=\"").append(text).append("\" shortcut=\"")
				.append(shortcut).append("\">").append(value).append("</Menu>");
	}

	private void menu(StringBuilder builder, String text, StringBuilder value) {
		menu(builder, text, value.toString());
	}

	private void menu(StringBuilder builder, String text, String value) {
		// <Menu text="Dashboard">accounter#dashBoard</Menu>
		builder.append("	<Menu text=\"").append(text).append("\">")
				.append(value).append("</Menu>");
	}

	private void subMenu(StringBuilder builder, String text, String value) {
		// <SubMenu text="Accounts List">accounter#accountsList</SubMenu>
		builder.append("	<SubMenu text=\"").append(text).append("\">")
				.append(value).append("</SubMenu>");
	}

	private void subMenu(StringBuilder builder, String text, String sortcut,
			String value) {
		// <SubMenu text="New Customer"
		// shortcut="C">accounter#newCustomer</SubMenu>
		builder.append("	<SubMenu text=\"").append(text)
				.append("\" shortcut=\"").append(sortcut).append("\">")
				.append(value).append("</SubMenu>");
	}

	private void separator(StringBuilder builder) {
		// <Seperator/>
		builder.append("	<Seperator />");
	}

	private void mainMenu(StringBuilder builder, String text,
			StringBuilder value) {
		// <MainMenu text="Company"></MainMenu>
		builder.append("	<MainMenu text=\"").append(text).append("\">")
				.append(value).append("</MainMenu>");
	}
}
