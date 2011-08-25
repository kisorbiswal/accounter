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
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
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

	private void generateXML(Company company, String emailId) {
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
			subMenu(newValue, Accounter.constants().newVATItem(), "V",
					"accounter#newVatItem");
			subMenu(newValue, Accounter.constants().newVATCode(),
					"accounter#newVatCode");
			subMenu(newValue, Accounter.constants().newVATAgency(),
					"accounter#newVatAgency");
			menu(mainValue, "New", newValue);
			separator(mainValue);
		}

		if (canDoInvoiceTransactions()) {
			menu(mainValue, Accounter.constants().vatAdjustment(),
					"accounter#vatAdjustment");
			menu(mainValue, Accounter.constants().fileVAT(),
					"accounter#fileVAT");
		}

		if (canDoBanking()) {
			menu(mainValue, Accounter.constants().payVAT(), "accounter#payVat");
			menu(mainValue, Accounter.constants().receiveVAT(),
					"accounter#receiveVat");
		}

		separator(mainValue);

		StringBuilder vatListValue = new StringBuilder();
		subMenu(vatListValue, Accounter.constants().vatItems(), "V",
				"accounter#vatItems");
		subMenu(vatListValue, Accounter.constants().vatCodes(),
				"accounter#vatCodes");
		menu(mainValue, Accounter.constants().vatList(), vatListValue);

		mainMenu(builder, Accounter.constants().vat(), mainValue);
	}

	private void addSettingsMenuItem() {
		StringBuilder settingsValue = new StringBuilder();
		menu(settingsValue, Accounter.constants().generalSettings(),
				"accounter#generalSettings");
		mainMenu(builder, Accounter.constants().settings(), settingsValue);
	}

	private void addReportsMenuItem() {
		StringBuilder reportsValue = new StringBuilder();

		StringBuilder financialValue = new StringBuilder();
		subMenu(financialValue, Accounter.constants().profitAndLoss(),
				"accounter#profitAndLoss");
		subMenu(financialValue, Accounter.constants().balanceSheet(),
				"accounter#balanceSheet");
		subMenu(financialValue, Accounter.constants().cashFlowReport(),
				"accounter#cashFlowReport");
		subMenu(financialValue, Accounter.constants().trialBalance(),
				"accounter#trialBalance");
		subMenu(financialValue, Accounter.messages()
				.transactionDetailByAccount(Global.get().Account()),
				"accounter#transactionDetailByAccount");
		if (isUSType()) {
			subMenu(financialValue,
					Accounter.constants().generalLedgerReport(),
					"accounter#generalLedger");
		}
		subMenu(financialValue, Accounter.constants().expenseReport(),
				"accounter#expenseReport");
		if (isUSType()) {
			subMenu(financialValue, Accounter.constants().salesTaxLiability(),
					"accounter#salesTaxLiability");
			subMenu(financialValue, Accounter.constants()
					.transactionDetailByTaxItem(),
					"accounter#transactionDetailByTaxItem");
		}
		menu(reportsValue, Accounter.constants().companyAndFinance(),
				financialValue);

		StringBuilder receivablesValue = new StringBuilder();
		subMenu(receivablesValue, Accounter.constants().ageingSummary(),
				"accounter#arAgingSummary");
		subMenu(receivablesValue, Accounter.constants().arAgeingDetail(),
				"accounter#arAgingDetail");
		subMenu(receivablesValue,
				Accounter.messages().customerStatement(Global.get().Customer()),
				"accounter#customerStatement");
		subMenu(receivablesValue, Accounter.messages()
				.customerTransactionHistory(Global.get().Customer()),
				"accounter#customerTransactionHistory");
		menu(reportsValue,
				Accounter.messages().customersAndReceivable(
						Global.get().Customer()), receivablesValue);

		StringBuilder salesValue = new StringBuilder();
		subMenu(salesValue,
				Accounter.messages().salesByCustomerSummary(
						Global.get().Customer()),
				"accounter#salesByCustomerSummary");
		subMenu(salesValue,
				Accounter.messages().salesByCustomerDetail(
						Global.get().Customer()),
				"accounter#salesByCustomerDetail");
		subMenu(salesValue, Accounter.constants().salesByItemSummary(),
				"accounter#salesByItemSummary");
		subMenu(salesValue, Accounter.constants().salesByItemDetail(),
				"accounter#salesByItemDetail");
		if (preferences.isSalesOrderEnabled()) {
			subMenu(salesValue, Accounter.constants().salesOrderReport(),
					"accounter#salesOrderReport");
		}
		menu(reportsValue, Accounter.constants().sales(), salesValue);

		StringBuilder suppliersValue = new StringBuilder();
		subMenu(suppliersValue, Accounter.constants().apAgeingSummary(),
				"accounter#apAgingSummary");
		subMenu(suppliersValue, Accounter.constants().apAgeingDetail(),
				"accounter#apAgingDetail");
		subMenu(suppliersValue,
				Accounter.messages().vendorTransactionHistory(
						Global.get().Vendor()),
				"accounter#supplierTransactionHistory");
		menu(reportsValue,
				Accounter.messages().vendorsAndPayables(Global.get().Vendor()),
				suppliersValue);

		StringBuilder purchasesValue = new StringBuilder();
		subMenu(purchasesValue,
				Accounter.messages().purchaseByVendorSummary(
						Global.get().Vendor()),
				"accounter#purchaseBySupplierSummary");
		subMenu(purchasesValue,
				Accounter.messages().purchaseByVendorDetail(
						Global.get().Vendor()),
				"accounter#purchaseBySupplierDetail");
		subMenu(purchasesValue, Accounter.constants().purchaseByItemSummary(),
				"accounter#purchaseByItemSummary");
		subMenu(purchasesValue,
				Accounter.constants().purchaseByProductDetail(),
				"accounter#purchaseByItemDetail");
		if (preferences.isPurchaseOrderEnabled()) {
			subMenu(purchasesValue,
					Accounter.constants().purchaseOrderReport(),
					"accounter#purchaseOrderReport");
		}
		menu(reportsValue, Accounter.constants().purchases(), purchasesValue);

		if (preferences.getDoYouPaySalesTax()) {
			StringBuilder vatValue = new StringBuilder();
			subMenu(vatValue, Accounter.constants().priorVATReturns(),
					"accounter#priorVatReturns");
			subMenu(vatValue, Accounter.constants().vatDetail(),
					"accounter#vatDetail");
			subMenu(vatValue, Accounter.constants().vat100(),
					"accounter#vat100");
			subMenu(vatValue, Accounter.constants().uncategorisedVATAmounts(),
					"accounter#uncategorisedVatAmounts");
			subMenu(vatValue, Accounter.constants().vatItemSummary(),
					"accounter#vatItemSummary");
			subMenu(vatValue, Accounter.constants().ecSalesList(),
					"accounter#ecSalesList");
			menu(reportsValue, Accounter.constants().vat(), vatValue);
		}
		mainMenu(builder, Accounter.constants().reports(), reportsValue);
	}

	private void addPurchaseMenuItem() {
		StringBuilder purchaValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(purchaValues, Accounter.constants().purchaseOrder(),
					"accounter#purchaseOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(purchaValues, Accounter.constants().purchaseOrderList(),
					"accounter#purchaseOrderList");
		}
		if (canViewReports()) {
			menu(purchaValues, Accounter.constants().purchaseOrderReport(),
					"accounter#purchaseOrderReport");
		}
		mainMenu(builder, Accounter.constants().purchases(), purchaValues);
	}

	private void addSalesOrderMenuItem() {
		StringBuilder salesValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(salesValues, Accounter.constants().salesOrder(),
					"accounter#salesOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(salesValues, Accounter.constants().salesOrderList(),
					"accounter#salesOrderList");
		}
		if (canViewReports()) {
			menu(salesValues, Accounter.constants().salesOrderReport(), "r",
					"accounter#salesOrderReport");
		}
		mainMenu(builder, Accounter.constants().sales(), salesValues);
	}

	private void addBankingMenuItem() {
		StringBuilder bankingValues = new StringBuilder();
		menu(bankingValues,
				Accounter.messages().newBankAccount(Global.get().Account()),
				"b", "accounter#newBankAccount");
		separator(bankingValues);
		if (isUSType()) {
			menu(bankingValues, Accounter.constants().writeCheck(),
					"accounter#writeCheck");
		}
		menu(bankingValues, Accounter.constants().depositTransferFunds(),
				"accounter#depositTransferFunds");
		menu(bankingValues, Accounter.constants().payBills(),
				"accounter#payBill");
		separator(bankingValues);
		menu(bankingValues, Accounter.constants().newCreditCardCharge(),
				"accounter#creditCardCharge");
		separator(bankingValues);
		StringBuilder bankListValues = new StringBuilder();
		subMenu(bankListValues, Accounter.constants().payments(),
				"accounter#payments");
		menu(bankingValues, Accounter.constants().bankingList(), bankListValues);
		mainMenu(builder, Accounter.constants().banking(), bankingValues);
	}

	private void addVendorMenuItem() {
		String name = "";
		if (isUKType()) {
			name = "Supplier";
		} else {
			name = "Vendor";
		}

		StringBuilder vendorValue = new StringBuilder();
		menu(vendorValue, name + "s Home", "S", "accounter#vendorHome");
		separator(vendorValue);

		int items = 0;
		StringBuilder newValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			subMenu(newValues, "New " + name, "accounter#newVendor");
			subMenu(newValues, Accounter.constants().newItem(),
					"accounter#newItem");
			items += 2;
		}
		if (canDoBanking()) {
			subMenu(newValues, Accounter.constants().cashPurchase(),
					"accounter#newCashPurchase");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValues, name + Accounter.constants().credit(),
					"accounter#vendorCredit");
			if (isUSType()) {
				subMenu(newValues, Accounter.constants().newCheck(),
						"accounter#check");
				items += 1;
			}
			items += 1;
		}
		if (items > 0) {
			menu(vendorValue, Accounter.constants().new1(), newValues);
			separator(vendorValue);
		}

		if (canDoInvoiceTransactions()) {
			menu(vendorValue, Accounter.constants().enterBill(), "B",
					"accounter#enterBill");
		}
		if (canDoBanking()) {
			menu(vendorValue, Accounter.constants().payBills(),
					"accounter#payBill");
			menu(vendorValue, Accounter.constants().issuePayments(),
					"accounter#issuePayments");
			menu(vendorValue, name + " PrePayment",
					"accounter#vendorPrePayment");
		}
		if (canDoInvoiceTransactions()) {
			menu(vendorValue, Accounter.constants().recordExpenses(),
					"accounter#recordExpenses");
			if (preferences.isHaveEpmloyees()
					&& preferences.isTrackEmployeeExpenses()) {
				menu(vendorValue, Accounter.constants().expenseClaims(),
						"accounter#expenseClaims");
			}
			separator(vendorValue);
		}

		StringBuilder supplierValues = new StringBuilder();
		subMenu(supplierValues, name + "s", "accounter#VendorList");
		if (canSeeInvoiceTransactions()) {
			subMenu(supplierValues, Accounter.constants().items(),
					"accounter#items");
			subMenu(supplierValues, "Bills And Expenses",
					"accounter#billsAndExpenses");
		}
		if (canSeeBanking()) {
			subMenu(supplierValues, name + Accounter.constants().payments(),
					"accounter#vendorPayments");
		}
		menu(vendorValue, name + " Lists", supplierValues);

		mainMenu(builder, name, vendorValue);
	}

	private void addCustomerMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();
		menu(mainMenuValue,
				Accounter.messages().customersHome(Global.get().Customer()),
				"accounter#customerHome");
		separator(mainMenuValue);

		int items = 0;
		StringBuilder newValue = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			subMenu(newValue,
					Accounter.messages().newCustomer(Global.get().Customer()),
					"C", "accounter#newCustomer");
			subMenu(newValue, Accounter.constants().newItem(),
					"accounter#newCustomerItem");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(newValue, Accounter.constants().newQuote(),
						"accounter#newQuote");
				items += 1;
			}
			subMenu(newValue, Accounter.constants().newInvoice(),
					"accounter#newInvoice");
			items += 3;
		}
		if (canDoBanking()) {
			subMenu(newValue, Accounter.constants().newCashSale(),
					"accounter#newCashSale");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValue, Accounter.constants().newCredit(),
					"accounter#newCredit");
			items += 1;
		}
		if (items > 0) {
			menu(mainMenuValue, Accounter.constants().new1(), newValue);
			separator(mainMenuValue);
		}

		if (canDoBanking()) {
			menu(mainMenuValue,
					Accounter.messages().customerPrePayment(
							Global.get().Customer()),
					"accounter#customerPrepayment");
			menu(mainMenuValue, Accounter.constants().receivePayment(),
					"accounter#receivePayment");
			menu(mainMenuValue,
					Accounter.messages()
							.customerRefund(Global.get().Customer()),
					"accounter#customerRefund");
			separator(mainMenuValue);
		}

		StringBuilder customerListValue = new StringBuilder();
		subMenu(customerListValue,
				Accounter.messages().customers(Global.get().Customer()),
				"accounter#customers");
		if (canSeeInvoiceTransactions()) {
			subMenu(customerListValue, Accounter.constants().items(),
					"accounter#items");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(customerListValue, Accounter.constants().quotes(),
						"accounter#quotes");
			}
			subMenu(customerListValue, Accounter.constants().invoices(),
					"accounter#invoices");
		}
		if (canSeeBanking()) {
			subMenu(customerListValue,
					Accounter.constants().receivedPayments(),
					"accounter#receivePayments");
			subMenu(customerListValue,
					Accounter.messages().customerRefunds(
							Global.get().Customer()),
					"accounter#customerRefunds");
		}
		menu(mainMenuValue,
				Accounter.messages().customerList(Global.get().Customer()),
				customerListValue);

		mainMenu(builder, Global.get().Customer(), mainMenuValue);
	}

	private void addCompanyMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();

		menu(mainMenuValue, Accounter.constants().dashBoard(), "D",
				"accounter#dashBoard");
		separator(mainMenuValue);

		if (canDoBanking()) {
			menu(mainMenuValue, Accounter.constants().journalEntry(), "J",
					"accounter#newJournalEntry");
		}

		if (canDoInvoiceTransactions()) {
			menu(mainMenuValue,
					Accounter.messages().newAccount(Global.get().Account()),
					"A", "accounter#newAccount");
			separator(mainMenuValue);
		}

		if (canChangeSettings()) {
			menu(mainMenuValue, Accounter.constants().companyPreferences(),
					"accounter#companyPreferences");
			separator(mainMenuValue);
		}

		if (isUSType()) {
			if (preferences.getDoYouPaySalesTax()) {
				StringBuilder salesTaxValues = new StringBuilder();
				if (canDoInvoiceTransactions()) {
					subMenu(salesTaxValues, Accounter.constants()
							.manageSalesTaxGroups(),
							"accounter#manageSalesTaxGroups");
				} else {
					subMenu(salesTaxValues, Accounter.constants()
							.salesTaxGroups(), "accounter#salesTaxGroups");
				}
				if (canDoInvoiceTransactions()) {
					subMenu(salesTaxValues, Accounter.constants()
							.manageSalesItems(),
							"accounter#manageSalesTaxItems");
				} else {
					subMenu(salesTaxValues, Accounter.constants()
							.salesTaxItems(), "accounter#salesTaxItems");
				}

				if (canDoBanking()) {
					subMenu(salesTaxValues,
							Accounter.constants().paySalesTax(),
							"accounter#customerGroupList");
				}

				if (canDoInvoiceTransactions()) {
					if (isUKType()) {
						subMenu(salesTaxValues, Accounter.constants()
								.newVATAgency(), "accounter#newVatAgency");
					} else {
						subMenu(salesTaxValues, Accounter.constants()
								.newTAXAgency(), "accounter#newTaxAgency");
					}
				}
				menu(mainMenuValue, Accounter.constants().itemTax(),
						salesTaxValues);
			}
		}

		if (canChangeSettings()) {
			StringBuilder manageSupportLists = new StringBuilder();
			subMenu(manageSupportLists,
					Accounter.messages().customerGroupList(
							Global.get().Customer()),
					"accounter#customerGroupList");
			subMenu(manageSupportLists,
					Accounter.messages().vendorGroupList(Global.get().vendor()),
					"accounter#supplierGroupList");
			subMenu(manageSupportLists,
					Accounter.constants().paymentTermList(),
					"accounter#paymentTerms");
			subMenu(manageSupportLists, Accounter.constants()
					.shippingMethodList(), "accounter#shippingMethodsList");
			subMenu(manageSupportLists, Accounter.constants()
					.shippingTermList(), "accounter#shippingTermsList");
			subMenu(manageSupportLists, Accounter.constants().priceLevelList(),
					"accounter#priceLevels");
			subMenu(manageSupportLists, Accounter.constants().itemGroupList(),
					"accounter#itemGroupList");
			subMenu(manageSupportLists, Accounter.constants()
					.creditRatingList(), "accounter#creditRatingList");
			menu(mainMenuValue, Accounter.constants().managerSupportLists(),
					manageSupportLists);
		}

		if (canManageFiscalYears()) {
			menu(mainMenuValue, Accounter.constants().manageFiscalYear(),
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
					Accounter.messages().accountsList(Global.get().Account()),
					"accounter#accountsList");
		}
		if (canSeeBanking()) {
			subMenu(companyLists, Accounter.constants().journalEntries(),
					"accounter#journalEntries");
		}

		if (canSeeInvoiceTransactions()) {
			subMenu(companyLists, Accounter.constants().items(),
					"accounter#items");
		}
		subMenu(companyLists,
				Accounter.messages().companyCustomers(Global.get().Customer()),
				"accounter#customers");
		subMenu(companyLists,
				Accounter.messages().companySuppliers(Global.get().Vendor()),
				"accounter#supplierList");
		if (canSeeBanking()) {
			subMenu(companyLists, Accounter.constants().payments(),
					"accounter#payments");
		}
		subMenu(companyLists, Accounter.constants().salesPersons(),
				"accounter#salesPersons");
		menu(mainMenuValue, Accounter.constants().companyLists(), companyLists);

		mainMenu(builder, Accounter.constants().company(), mainMenuValue);
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
