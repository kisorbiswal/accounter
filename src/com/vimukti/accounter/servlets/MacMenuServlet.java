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
		builder = new StringBuilder();

		addHeader();

		addCompanyMenuItem();

		if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (preferences.getDoYouPaySalesTax()) {
				addVatMenuItem();
			}
		}
		addCustomerMenuItem();

		addVendorMenuItem(company.getAccountingType());

		if (user.getPermissions().getTypeOfBankReconcilation() == RolePermissions.TYPE_YES) {
			addBankingMenuItem();
		}

		if (preferences.isSalesOrderEnabled()) {
			addSalesOrderMenuItem();
		}

		if (preferences.isPurchaseOrderEnabled()) {
			addPurchaseMenuItem();
		}

		if (user.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_READ_ONLY
				|| user.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES) {
			addReportsMenuItem();
		}

		if (user.getPermissions().getTypeOfSystemSettings() == RolePermissions.TYPE_YES) {
			addSettingsMenuItem();
		}

		addFooter();
	}

	private void addFooter() {
		builder.append("</menus></xml>");
	}

	private void addHeader() {
		builder.append("<xml><AccounterLogout text=\"Logout\">do/logout</AccounterLogout><ChangePassword text= \"User Details\">accounter#userDetails</ChangePassword><menus>");
	}

	private void addVatMenuItem() {
		StringBuilder mainValue = new StringBuilder();
		StringBuilder newValue = new StringBuilder();
		subMenu(newValue, "New VAT Item", "V", "accounter#newVatItem");
		subMenu(newValue, "New VAT Code", "accounter#newVatCode");
		subMenu(newValue, "New VAT Agency", "accounter#newVatAgency");
		menu(mainValue, "New", newValue);

		separator(mainValue);

		menu(mainValue, "VAT Adjustment", "accounter#vatAdjustment");
		menu(mainValue, "File VAT", "accounter#fileVAT");
		menu(mainValue, "Pay VAT", "accounter#payVat");
		menu(mainValue, "Receive VAT", "accounter#receiveVat");

		separator(mainValue);

		StringBuilder vatListValue = new StringBuilder();
		subMenu(vatListValue, "VAT Items", "V", "accounter#vatItems");
		subMenu(vatListValue, "VAT Codes", "accounter#vatCodes");
		menu(mainValue, "VAT List", vatListValue);

		mainMenu(builder, "VAT", mainValue);
	}

	private void addSettingsMenuItem() {
		StringBuilder settingsValue = new StringBuilder();
		menu(settingsValue, "General Settings", "accounter#generalSettings");
		mainMenu(builder, "Settings", settingsValue);
	}

	private void addReportsMenuItem() {
		StringBuilder reportsValue = new StringBuilder();

		StringBuilder financialValue = new StringBuilder();
		subMenu(financialValue, "Profit And Loss", "accounter#profitAndLoss");
		subMenu(financialValue, "Balance Sheet", "accounter#balanceSheet");
		subMenu(financialValue, "Cash Flow Report", "accounter#cashFlowReport");
		subMenu(financialValue, "Trial Balance", "accounter#trialBalance");
		subMenu(financialValue, "Transaction Detail By Account",
				"accounter#transactionDetailByAccount");
		subMenu(financialValue, "Expense Report", "accounter#expenseReport");
		menu(reportsValue, "Company And Financial", financialValue);

		StringBuilder receivablesValue = new StringBuilder();
		subMenu(receivablesValue, "A/R Ageing Summary",
				"accounter#arAgingSummary");
		subMenu(receivablesValue, "A/R Ageing Detail",
				"accounter#arAgingDetail");
		subMenu(receivablesValue, "Customer Statement",
				"accounter#customerStatement");
		subMenu(receivablesValue, "Customer Transaction History",
				"accounter#customerTransactionHistory");
		menu(reportsValue, "Customers And Receivables", receivablesValue);

		StringBuilder salesValue = new StringBuilder();
		subMenu(salesValue, "Sales By Customer Summary",
				"accounter#salesByCustomerSummary");
		subMenu(salesValue, "Sales By Customer Detail",
				"accounter#salesByCustomerDetail");
		subMenu(salesValue, "Sales By Item Summary",
				"accounter#salesByItemSummary");
		subMenu(salesValue, "Sales By Item Detail",
				"accounter#salesByItemDetail");
		subMenu(salesValue, "Sales Order Report", "accounter#salesOrderReport");
		menu(reportsValue, "Sales", salesValue);

		StringBuilder suppliersValue = new StringBuilder();
		subMenu(suppliersValue, "A/P Ageing Summary",
				"accounter#apAgingSummary");
		subMenu(suppliersValue, "A/P Ageing Detail", "accounter#apAgingDetail");
		subMenu(suppliersValue, "Supplier Transaction History",
				"accounter#supplierTransactionHistory");
		menu(reportsValue, "Suppliers And Payables", suppliersValue);

		StringBuilder purchasesValue = new StringBuilder();
		subMenu(purchasesValue, "Purchase By Supplier Summary",
				"accounter#purchaseBySupplierSummary");
		subMenu(purchasesValue, "Purchase By Supplier Detail",
				"accounter#purchaseBySupplierDetail");
		subMenu(purchasesValue, "Purchase By Item Summary",
				"accounter#purchaseByItemSummary");
		subMenu(purchasesValue, "Purchase By Item Detail",
				"accounter#purchaseByItemDetail");
		subMenu(purchasesValue, "Purchase Order Report",
				"accounter#purchaseOrderReport");
		menu(reportsValue, "Purchases", purchasesValue);

		StringBuilder vatValue = new StringBuilder();
		subMenu(vatValue, "Prior VAT Returns", "accounter#priorVatReturns");
		subMenu(vatValue, "VAT Detail", "accounter#vatDetail");
		subMenu(vatValue, "VAT 100", "accounter#vat100");
		subMenu(vatValue, "Uncategorised VAT Amounts",
				"accounter#uncategorisedVatAmounts");
		subMenu(vatValue, "VAT Item Summary", "accounter#vatItemSummary");
		subMenu(vatValue, "EC Sales List", "accounter#ecSalesList");
		menu(reportsValue, "VAT", vatValue);

		mainMenu(builder, "Reports", reportsValue);
	}

	private void addPurchaseMenuItem() {
		StringBuilder purchaValues = new StringBuilder();
		menu(purchaValues, "Purchase Order", "accounter#purchaseOrder");
		menu(purchaValues, "PurchaseOrder List", "accounter#purchaseOrderList");
		menu(purchaValues, "PurchaseOrder Report",
				"accounter#purchaseOrderReport");
		mainMenu(builder, "Purchases", purchaValues);
	}

	private void addSalesOrderMenuItem() {
		StringBuilder salesValues = new StringBuilder();
		menu(salesValues, "Sales Order", "accounter#salesOrder");
		menu(salesValues, "SalesOrder List", "accounter#salesOrderList");
		menu(salesValues, "SalesOrder Report", "r",
				"accounter#salesOrderReport");
		mainMenu(builder, "Sales", salesValues);
	}

	private void addBankingMenuItem() {
		StringBuilder bankingValues = new StringBuilder();
		menu(bankingValues, "New Bank Account", "b", "accounter#newBankAccount");
		separator(bankingValues);
		menu(bankingValues, "Deposit / Transfer Funds",
				"accounter#depositTransferFunds");
		menu(bankingValues, "Pay Bills", "accounter#payBill");
		separator(bankingValues);
		menu(bankingValues, "New Credit Card Charge",
				"accounter#creditCardCharge");
		separator(bankingValues);
		StringBuilder bankListValues = new StringBuilder();
		subMenu(bankListValues, "Payments", "accounter#payments");
		menu(bankingValues, "Banking List", bankListValues);
		mainMenu(builder, "Banking", bankingValues);
	}

	private void addVendorMenuItem(int accountingType) {
		StringBuilder vendorValue = new StringBuilder();
		menu(vendorValue, "Suppliers Home", "S", "accounter#supplierHome");
		separator(vendorValue);

		StringBuilder newValues = new StringBuilder();
		subMenu(newValues, "New Supplier", "accounter#newSupplier");
		subMenu(newValues, "New Item", "accounter#newSupplierItem");
		subMenu(newValues, "Cash Purchase", "accounter#newCashPurchase");
		subMenu(newValues, "Supplier Credit", "accounter#supplierCredit");
		menu(vendorValue, "New", newValues);
		separator(vendorValue);

		menu(vendorValue, "Enter Bill", "B", "accounter#enterBill");
		menu(vendorValue, "Pay Bills", "accounter#payBill");
		menu(vendorValue, "Issue Payments", "accounter#issuePayments");
		menu(vendorValue, "Supplier PrePayment", "accounter#supplierPrePayment");
		menu(vendorValue, "Record Expenses", "accounter#recordExpenses");
		menu(vendorValue, "Expense Claims", "accounter#expenseClaims");
		separator(vendorValue);

		StringBuilder supplierValues = new StringBuilder();
		subMenu(supplierValues, "Suppliers", "accounter#supplierList");
		subMenu(supplierValues, "Items", "accounter#purchaseItems");
		subMenu(supplierValues, "Bills And Expenses",
				"accounter#billsAndExpenses");
		subMenu(supplierValues, "Supplier Payments",
				"accounter#supplierPayments");
		menu(vendorValue, "Supplier Lists", supplierValues);

		String name = "";
		if (accountingType == 1) {// UK
			name = "Suppliers";
		} else {
			name = "Vendor";
		}
		mainMenu(builder, name, vendorValue);
	}

	private void addCustomerMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();
		menu(mainMenuValue, "Customers Home", "accounter#customerHome");
		separator(mainMenuValue);

		StringBuilder newValue = new StringBuilder();
		subMenu(newValue, "New Customer", "C", "accounter#newCustomer");
		subMenu(newValue, "New Item", "accounter#newCustomerItem");
		subMenu(newValue, "New Quote", "accounter#newQuote");
		subMenu(newValue, "New Invoice", "accounter#newInvoice");
		subMenu(newValue, "New Cash Sale", "accounter#newCashSale");
		subMenu(newValue, "New Credit", "accounter#newCredit");
		menu(mainMenuValue, "New", newValue);

		separator(mainMenuValue);

		menu(mainMenuValue, "Customer PrePayment",
				"accounter#customerPrepayment");
		menu(mainMenuValue, "Receive Payment", "accounter#receivePayment");
		menu(mainMenuValue, "Customer Refund", "accounter#customerRefund");
		separator(mainMenuValue);

		StringBuilder customerListValue = new StringBuilder();
		subMenu(customerListValue, "Customers", "accounter#customers");
		subMenu(customerListValue, "Items", "accounter#items");
		subMenu(customerListValue, "Quotes", "accounter#quotes");
		subMenu(customerListValue, "Invoices", "accounter#invoices");
		subMenu(customerListValue, "Receive Payments",
				"accounter#receivePayments");
		subMenu(customerListValue, "Customer Refunds",
				"accounter#customerRefunds");
		menu(mainMenuValue, "Customer List", customerListValue);

		mainMenu(builder, "Customer", mainMenuValue);
	}

	private void addCompanyMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();

		menu(mainMenuValue, "Dashboard", "D", "accounter#dashBoard");
		separator(mainMenuValue);
		menu(mainMenuValue, "New Journal Entry", "J",
				"accounter#newJournalEntry");
		menu(mainMenuValue, "New Account", "A", "accounter#newAccount");
		separator(mainMenuValue);
		menu(mainMenuValue, "Company Preferences",
				"accounter#companyPreferences");
		separator(mainMenuValue);

		StringBuilder manageSupportLists = new StringBuilder();
		subMenu(manageSupportLists, "Customer Group List",
				"accounter#customerGroupList");
		subMenu(manageSupportLists, "Supplier Group List",
				"accounter#supplierGroupList");
		subMenu(manageSupportLists, "Payment Term List",
				"accounter#paymentTerms");
		subMenu(manageSupportLists, "Shipping Method List",
				"accounter#shippingMethodsList");
		subMenu(manageSupportLists, "Shipping Term List",
				"accounter#shippingTermsList");
		subMenu(manageSupportLists, "Price Level List", "accounter#priceLevels");
		subMenu(manageSupportLists, "Item Group List",
				"accounter#itemGroupList");
		subMenu(manageSupportLists, "Credit Rating List",
				"accounter#creditRatingList");
		menu(mainMenuValue, "Manage Support Lists", manageSupportLists);

		menu(mainMenuValue, "Manage Fiscal Year", "accounter#manageFiscalYear");
		separator(mainMenuValue);

		StringBuilder companyLists = new StringBuilder();
		subMenu(companyLists, "Accounts List", "accounter#accountsList");
		subMenu(companyLists, "Journal Entries", "accounter#journalEntries");
		subMenu(companyLists, "Items", "accounter#items");
		subMenu(companyLists, "Company Customers", "accounter#customers");
		subMenu(companyLists, "Company Suppliers", "accounter#supplierList");
		subMenu(companyLists, "Payments", "accounter#payments");
		subMenu(companyLists, "Sales Persons", "accounter#salesPersons");
		menu(mainMenuValue, "Company Lists", companyLists);

		mainMenu(builder, "Company", mainMenuValue);
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
