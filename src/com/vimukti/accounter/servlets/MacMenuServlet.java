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
			subMenu(newValue, "New VAT Item", "V", "accounter#newVatItem");
			subMenu(newValue, "New VAT Code", "accounter#newVatCode");
			subMenu(newValue, "New VAT Agency", "accounter#newVatAgency");
			menu(mainValue, "New", newValue);
			separator(mainValue);
		}

		if (canDoInvoiceTransactions()) {
			menu(mainValue, "VAT Adjustment", "accounter#vatAdjustment");
			menu(mainValue, "File VAT", "accounter#fileVAT");
		}

		if (canDoBanking()) {
			menu(mainValue, "Pay VAT", "accounter#payVat");
			menu(mainValue, "Receive VAT", "accounter#receiveVat");
		}

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
		if (isUSType()) {
			subMenu(financialValue, "General Ledger Report",
					"accounter#generalLedger");
		}
		subMenu(financialValue, "Expense Report", "accounter#expenseReport");
		if (isUSType()) {
			subMenu(financialValue, "Sales tax liability",
					"accounter#salesTaxLiability");
			subMenu(financialValue, "Transaction Detail by Tax Item",
					"accounter#transactionDetailByTaxItem");
		}
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
		if (preferences.isSalesOrderEnabled()) {
			subMenu(salesValue, "Sales Order Report",
					"accounter#salesOrderReport");
		}
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
		if (preferences.isPurchaseOrderEnabled()) {
			subMenu(purchasesValue, "Purchase Order Report",
					"accounter#purchaseOrderReport");
		}
		menu(reportsValue, "Purchases", purchasesValue);

		if (preferences.getDoYouPaySalesTax()) {
			StringBuilder vatValue = new StringBuilder();
			subMenu(vatValue, "Prior VAT Returns", "accounter#priorVatReturns");
			subMenu(vatValue, "VAT Detail", "accounter#vatDetail");
			subMenu(vatValue, "VAT 100", "accounter#vat100");
			subMenu(vatValue, "Uncategorised VAT Amounts",
					"accounter#uncategorisedVatAmounts");
			subMenu(vatValue, "VAT Item Summary", "accounter#vatItemSummary");
			subMenu(vatValue, "EC Sales List", "accounter#ecSalesList");
			menu(reportsValue, "VAT", vatValue);
		}
		mainMenu(builder, "Reports", reportsValue);
	}

	private void addPurchaseMenuItem() {
		StringBuilder purchaValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(purchaValues, "Purchase Order", "accounter#purchaseOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(purchaValues, "PurchaseOrder List",
					"accounter#purchaseOrderList");
		}
		if (canViewReports()) {
			menu(purchaValues, "PurchaseOrder Report",
					"accounter#purchaseOrderReport");
		}
		mainMenu(builder, "Purchases", purchaValues);
	}

	private void addSalesOrderMenuItem() {
		StringBuilder salesValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(salesValues, "Sales Order", "accounter#salesOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(salesValues, "SalesOrder List", "accounter#salesOrderList");
		}
		if (canViewReports()) {
			menu(salesValues, "SalesOrder Report", "r",
					"accounter#salesOrderReport");
		}
		mainMenu(builder, "Sales", salesValues);
	}

	private void addBankingMenuItem() {
		StringBuilder bankingValues = new StringBuilder();
		menu(bankingValues, "New Bank Account", "b", "accounter#newBankAccount");
		separator(bankingValues);
		if (isUSType()) {
			menu(bankingValues, "Write check", "accounter#writeCheck");
		}
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
			subMenu(newValues, "New Item", "accounter#newItem");
			items += 2;
		}
		if (canDoBanking()) {
			subMenu(newValues, "Cash Purchase", "accounter#newCashPurchase");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValues, name + " Credit", "accounter#vendorCredit");
			if (isUSType()) {
				subMenu(newValues, "New Check", "accounter#check");
				items += 1;
			}
			items += 1;
		}
		if (items > 0) {
			menu(vendorValue, "New", newValues);
			separator(vendorValue);
		}

		if (canDoInvoiceTransactions()) {
			menu(vendorValue, "Enter Bill", "B", "accounter#enterBill");
		}
		if (canDoBanking()) {
			menu(vendorValue, "Pay Bills", "accounter#payBill");
			menu(vendorValue, "Issue Payments", "accounter#issuePayments");
			menu(vendorValue, name + " PrePayment",
					"accounter#vendorPrePayment");
		}
		if (canDoInvoiceTransactions()) {
			menu(vendorValue, "Record Expenses", "accounter#recordExpenses");
			if (preferences.isHaveEpmloyees()
					&& preferences.isTrackEmployeeExpenses()) {
				menu(vendorValue, "Expense Claims", "accounter#expenseClaims");
			}
			separator(vendorValue);
		}

		StringBuilder supplierValues = new StringBuilder();
		subMenu(supplierValues, name + "s", "accounter#VendorList");
		if (canSeeInvoiceTransactions()) {
			subMenu(supplierValues, "Items", "accounter#items");
			subMenu(supplierValues, "Bills And Expenses",
					"accounter#billsAndExpenses");
		}
		if (canSeeBanking()) {
			subMenu(supplierValues, name + " Payments",
					"accounter#vendorPayments");
		}
		menu(vendorValue, name + " Lists", supplierValues);

		mainMenu(builder, name, vendorValue);
	}

	private void addCustomerMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();
		menu(mainMenuValue, "Customers Home", "accounter#customerHome");
		separator(mainMenuValue);

		int items = 0;
		StringBuilder newValue = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			subMenu(newValue, "New Customer", "C", "accounter#newCustomer");
			subMenu(newValue, "New Item", "accounter#newCustomerItem");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(newValue, "New Quote", "accounter#newQuote");
				items += 1;
			}
			subMenu(newValue, "New Invoice", "accounter#newInvoice");
			items += 3;
		}
		if (canDoBanking()) {
			subMenu(newValue, "New Cash Sale", "accounter#newCashSale");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValue, "New Credit Note", "accounter#newCredit");
			items += 1;
		}
		if (items > 0) {
			menu(mainMenuValue, "New", newValue);
			separator(mainMenuValue);
		}

		if (canDoBanking()) {
			menu(mainMenuValue, "Customer PrePayment",
					"accounter#customerPrepayment");
			menu(mainMenuValue, "Receive Payment", "accounter#receivePayment");
			menu(mainMenuValue, "Customer Refund", "accounter#customerRefund");
			separator(mainMenuValue);
		}

		StringBuilder customerListValue = new StringBuilder();
		subMenu(customerListValue, "Customers", "accounter#customers");
		if (canSeeInvoiceTransactions()) {
			subMenu(customerListValue, "Items", "accounter#items");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(customerListValue, "Quotes", "accounter#quotes");
			}
			subMenu(customerListValue, "Invoices", "accounter#invoices");
		}
		if (canSeeBanking()) {
			subMenu(customerListValue, "Receive Payments",
					"accounter#receivePayments");
			subMenu(customerListValue, "Customer Refunds",
					"accounter#customerRefunds");
		}
		menu(mainMenuValue, "Customer List", customerListValue);

		mainMenu(builder, "Customer", mainMenuValue);
	}

	private void addCompanyMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();

		menu(mainMenuValue, "Dashboard", "D", "accounter#dashBoard");
		separator(mainMenuValue);

		if (canDoBanking()) {
			menu(mainMenuValue, "New Journal Entry", "J",
					"accounter#newJournalEntry");
		}

		if (canDoInvoiceTransactions()) {
			menu(mainMenuValue, "New Account", "A", "accounter#newAccount");
			separator(mainMenuValue);
		}

		if (canChangeSettings()) {
			menu(mainMenuValue, "Company Preferences",
					"accounter#companyPreferences");
			separator(mainMenuValue);
		}

		if (isUSType()) {
			if (preferences.getDoYouPaySalesTax()) {
				StringBuilder salesTaxValues = new StringBuilder();
				if (canDoInvoiceTransactions()) {
					subMenu(salesTaxValues, "Manage Sales Tax Groups",
							"accounter#manageSalesTaxGroups");
				} else {
					subMenu(salesTaxValues, "Sales Tax Groups",
							"accounter#salesTaxGroups");
				}
				if (canDoInvoiceTransactions()) {
					subMenu(salesTaxValues, "Manage Sales Tax Items",
							"accounter#manageSalesTaxItems");
				} else {
					subMenu(salesTaxValues, "Sales Tax Items",
							"accounter#salesTaxItems");
				}

				if (canDoBanking()) {
					subMenu(salesTaxValues, "Pay sales tax",
							"accounter#customerGroupList");
				}

				if (canDoInvoiceTransactions()) {
					if (isUKType()) {
						subMenu(salesTaxValues, "New VAT Agency",
								"accounter#newVatAgency");
					} else {
						subMenu(salesTaxValues, "New Tax Agency",
								"accounter#newTaxAgency");
					}
				}
				menu(mainMenuValue, "Item tax", salesTaxValues);
			}
		}

		if (canChangeSettings()) {
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
			subMenu(manageSupportLists, "Price Level List",
					"accounter#priceLevels");
			subMenu(manageSupportLists, "Item Group List",
					"accounter#itemGroupList");
			subMenu(manageSupportLists, "Credit Rating List",
					"accounter#creditRatingList");
			menu(mainMenuValue, "Manage Support Lists", manageSupportLists);
		}

		if (canManageFiscalYears()) {
			menu(mainMenuValue, "Manage Fiscal Year",
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
			subMenu(companyLists, "Accounts List", "accounter#accountsList");
		}
		if (canSeeBanking()) {
			subMenu(companyLists, "Journal Entries", "accounter#journalEntries");
		}

		if (canSeeInvoiceTransactions()) {
			subMenu(companyLists, "Items", "accounter#items");
		}
		subMenu(companyLists, "Company Customers", "accounter#customers");
		subMenu(companyLists, "Company Suppliers", "accounter#supplierList");
		if (canSeeBanking()) {
			subMenu(companyLists, "Payments", "accounter#payments");
		}
		subMenu(companyLists, "Sales Persons", "accounter#salesPersons");
		menu(mainMenuValue, "Company Lists", companyLists);

		mainMenu(builder, "Company", mainMenuValue);
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
