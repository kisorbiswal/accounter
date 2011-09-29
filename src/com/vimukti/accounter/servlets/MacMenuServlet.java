package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.server.FinanceTool;

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
			String companyID = getCookie(req, COMPANY_COOKIE);

			Company company = getCompany(req);
			Session hibernateSession = HibernateUtil.openSession();
			try {
				CompanyPreferenceThreadLocal.set(new FinanceTool()
						.getClientCompanyPreferences(company));
			} catch (Exception e) {
			} finally {
				if (hibernateSession.isOpen()) {
					hibernateSession.close();
				}
			}
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

		if (preferences.isTrackTax()) {
			addVatMenuItem();
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
		if (user.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES
				|| user.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_READ_ONLY)
			return true;
		else
			return false;
	}

	private boolean canDoBanking() {
		if (user.getPermissions().getTypeOfBankReconcilation() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean isTaxTracking() {
		return preferences.isTrackTax();
	}

	private boolean isClassTracking() {
		return preferences.isClassTrackingEnabled();
	}

	private boolean isLocationTracking() {
		return preferences.isLocationTrackingEnabled();
	}

	// private boolean isUKType() {
	// return company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK;
	// }

	private boolean isUSType() {
		return company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US;
	}

	private boolean isTrackTax() {
		return preferences.isTrackTax();
	}

	private void addFooter() {
		builder.append("</menus></xml>");
	}

	private void addHeader() {
		builder.append("<xml><AccounterLogout text=\"Logout\">main/logout</AccounterLogout><ChangePassword text= \"User Details\">company/accounter#userDetails</ChangePassword><menus>");
	}

	private void addVatMenuItem() {
		StringBuilder mainValue = new StringBuilder();

		if (canDoInvoiceTransactions()) {
			StringBuilder newValue = new StringBuilder();
			subMenu(newValue, iGlobal.constants().newTaxItem(), "V",
					"company/accounter#newTaxItem");
			subMenu(newValue, iGlobal.constants().newTaxCode(),
					"company/accounter#newVatCode");
			subMenu(newValue, iGlobal.constants().newTAXAgency(),
					"company/accounter#newTaxAgency");
			menu(mainValue, iGlobal.constants().new1(), newValue);
			separator(mainValue);
		}

		if (canDoInvoiceTransactions()) {
			menu(mainValue, iGlobal.constants().taxAdjustment(),
					"company/accounter#taxAdjustment");
			menu(mainValue, iGlobal.constants().fileVAT(),
					"company/accounter#fileVAT");
		}

		if (canDoBanking()) {
			menu(mainValue, iGlobal.constants().payTax(),
					"company/accounter#paySalesTax");
			menu(mainValue, iGlobal.constants().receiveVAT(),
					"company/accounter#receiveVat");
		}

		separator(mainValue);

		StringBuilder vatListValue = new StringBuilder();
		subMenu(vatListValue, iGlobal.constants().taxItemsList(), "V",
				"company/accounter#vatItems");
		subMenu(vatListValue, iGlobal.constants().taxCodesList(),
				"company/accounter#vatCodes");
		menu(mainValue, iGlobal.constants().vatList(), vatListValue);

		mainMenu(builder, iGlobal.constants().tax(), mainValue);
	}

	private void addSettingsMenuItem() {
		StringBuilder settingsValue = new StringBuilder();
		menu(settingsValue, iGlobal.constants().generalSettings(),
				"company/accounter#generalSettings");
		mainMenu(builder, iGlobal.constants().settings(), settingsValue);
	}

	private void addReportsMenuItem() {
		StringBuilder reportsValue = new StringBuilder();
		menu(reportsValue, iGlobal.constants().reportsHome(),
				"company/accounter#reportHome");
		separator(reportsValue);
		StringBuilder financialValue = new StringBuilder();
		subMenu(financialValue, iGlobal.constants().profitAndLoss(),
				"company/accounter#profitAndLoss");
		subMenu(financialValue, iGlobal.constants().balanceSheet(),
				"company/accounter#balanceSheet");
		subMenu(financialValue, iGlobal.constants().cashFlowReport(),
				"company/accounter#cashFlowReport");
		subMenu(financialValue, iGlobal.constants().trialBalance(),
				"company/accounter#trialBalance");
		subMenu(financialValue,
				iGlobal.messages()
						.transactionDetailByAccount(iGlobal.Account()),
				"company/accounter#transactionDetailByAccount");
		if (isUSType()) {
			subMenu(financialValue, iGlobal.constants().generalLedgerReport(),
					"company/accounter#generalLedger");
		}
		subMenu(financialValue, iGlobal.constants().expenseReport(),
				"company/accounter#expenseReport");
		if (isTaxTracking()) {
			subMenu(financialValue, iGlobal.constants().salesTaxLiability(),
					"company/accounter#salesTaxLiability");
			subMenu(financialValue, iGlobal.constants()
					.transactionDetailByTaxItem(),
					"company/accounter#transactionDetailByTaxItem");
		}
		if (isLocationTracking()) {
			subMenu(financialValue, iGlobal.constants().profitAndLoss() + "By"
					+ iGlobal.messages().location(Global.get().Location()),
					"company/accounter#profitAndLossByLocation");
		}
		if (isClassTracking()) {
			subMenu(financialValue, iGlobal.constants().profitAndLossbyClass(),
					"company/accounter#profitAndLossByClass");
		}
		menu(reportsValue, iGlobal.constants().companyAndFinance(),
				financialValue);

		StringBuilder receivablesValue = new StringBuilder();
		subMenu(receivablesValue, iGlobal.constants().arAgeingSummary(),
				"company/accounter#arAgingSummary");
		subMenu(receivablesValue, iGlobal.constants().arAgeingDetail(),
				"company/accounter#arAgingDetail");
		subMenu(receivablesValue,
				iGlobal.messages().customerStatement(iGlobal.Customer()),
				"company/accounter#customerStatement");
		subMenu(receivablesValue, iGlobal.messages()
				.customerTransactionHistory(iGlobal.Customer()),
				"company/accounter#customerTransactionHistory");
		menu(reportsValue,
				iGlobal.messages().customersAndReceivable(iGlobal.Customer()),
				receivablesValue);

		StringBuilder salesValue = new StringBuilder();
		subMenu(salesValue,
				iGlobal.messages().salesByCustomerSummary(iGlobal.Customer()),
				"company/accounter#salesByCustomerSummary");
		subMenu(salesValue,
				iGlobal.messages().salesByCustomerDetail(iGlobal.Customer()),
				"company/accounter#salesByCustomerDetail");
		subMenu(salesValue, iGlobal.constants().salesByItemSummary(),
				"company/accounter#salesByItemSummary");
		subMenu(salesValue, iGlobal.constants().salesByItemDetail(),
				"company/accounter#salesByItemDetail");
		if (preferences.isSalesOrderEnabled()) {
			subMenu(salesValue, iGlobal.constants().salesOrderReport(),
					"company/accounter#salesOrderReport");
		}
		if (preferences.isLocationTrackingEnabled()) {
			subMenu(salesValue,
					iGlobal.messages().getSalesByLocationDetails(
							Global.get().Location()),
					"company/accounter#salesByClassDetails");
			subMenu(salesValue,
					iGlobal.messages().salesByLocationSummary(
							Global.get().Location()),
					"company/accounter#salesByClassSummary");
		}

		if (preferences.isClassTrackingEnabled()) {
			subMenu(salesValue, iGlobal.constants().salesByClassDetails(),
					"company/accounter#salesByLocationDetails");
			subMenu(salesValue, iGlobal.constants().salesByClassSummary(),
					"company/accounter#salesByLocationSummary");
		}

		menu(reportsValue, iGlobal.constants().sales(), salesValue);

		StringBuilder suppliersValue = new StringBuilder();
		subMenu(suppliersValue, iGlobal.constants().apAgeingSummary(),
				"company/accounter#apAgingSummary");
		subMenu(suppliersValue, iGlobal.constants().apAgeingDetail(),
				"company/accounter#apAgingDetail");
		subMenu(suppliersValue,
				iGlobal.messages().vendorTransactionHistory(iGlobal.Vendor()),
				"company/accounter#vendorTransactionHistory");
		menu(reportsValue,
				iGlobal.messages().vendorsAndPayables(iGlobal.Vendor()),
				suppliersValue);

		StringBuilder purchasesValue = new StringBuilder();
		subMenu(purchasesValue,
				iGlobal.messages().purchaseByVendorSummary(iGlobal.Vendor()),
				"company/accounter#purchaseByVendorSummary");
		subMenu(purchasesValue,
				iGlobal.messages().purchaseByVendorDetail(iGlobal.Vendor()),
				"company/accounter#purchaseByVendorDetail");
		subMenu(purchasesValue, iGlobal.constants().purchaseByItemSummary(),
				"company/accounter#purchaseByItemSummary");
		subMenu(purchasesValue, iGlobal.constants().purchaseByProductDetail(),
				"company/accounter#purchaseByItemDetail");
		if (preferences.isPurchaseOrderEnabled()) {
			subMenu(purchasesValue, iGlobal.constants().purchaseOrderReport(),
					"company/accounter#purchaseOrderReport");
		}
		menu(reportsValue, iGlobal.constants().purchases(), purchasesValue);

		if (preferences.isTrackTax()) {
			StringBuilder vatValue = new StringBuilder();
			subMenu(vatValue, iGlobal.constants().priorVATReturns(),
					"company/accounter#priorVatReturns");
			subMenu(vatValue, iGlobal.constants().vatDetail(),
					"company/accounter#vatDetail");
			subMenu(vatValue, iGlobal.constants().vat100(),
					"company/accounter#vat100");
			subMenu(vatValue, iGlobal.constants().uncategorisedVATAmounts(),
					"company/accounter#uncategorisedVatAmounts");
			subMenu(vatValue, iGlobal.constants().vatItemSummary(),
					"company/accounter#vatItemSummary");
			subMenu(vatValue, iGlobal.constants().ecSalesList(),
					"company/accounter#ecSalesList");
			menu(reportsValue, iGlobal.constants().vat(), vatValue);
		}
		mainMenu(builder, iGlobal.constants().reports(), reportsValue);
	}

	private void addPurchaseMenuItem() {
		StringBuilder purchaValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(purchaValues, iGlobal.constants().purchaseOrder(),
					"company/accounter#purchaseOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(purchaValues, iGlobal.constants().purchaseOrderList(),
					"company/accounter#purchaseOrderList");
		}
		if (canViewReports()) {
			menu(purchaValues, iGlobal.constants().purchaseOrderReport(),
					"company/accounter#purchaseOrderReport");
		}
		mainMenu(builder, iGlobal.constants().purchases(), purchaValues);
	}

	private void addSalesOrderMenuItem() {
		StringBuilder salesValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			menu(salesValues, iGlobal.constants().salesOrder(),
					"company/accounter#salesOrder");
		}
		if (canSeeInvoiceTransactions()) {
			menu(salesValues, iGlobal.constants().salesOrderList(),
					"company/accounter#salesOrderList");
		}
		if (canViewReports()) {
			menu(salesValues, iGlobal.constants().salesOrderReport(), "r",
					"company/accounter#salesOrderReport");
		}
		mainMenu(builder, iGlobal.constants().sales(), salesValues);
	}

	private void addBankingMenuItem() {
		StringBuilder bankingValues = new StringBuilder();
		menu(bankingValues, iGlobal.messages()
				.newBankAccount(iGlobal.Account()), "b",
				"company/accounter#newBankAccount");
		separator(bankingValues);
		menu(bankingValues, iGlobal.constants().writeCheck(),
				"company/accounter#writeCheck");
		menu(bankingValues, iGlobal.constants().depositTransferFunds(),
				"company/accounter#depositTransferFunds");
		menu(bankingValues, iGlobal.constants().payBills(),
				"company/accounter#payBill");
		separator(bankingValues);
		menu(bankingValues, iGlobal.constants().newCreditCardCharge(),
				"company/accounter#creditCardCharge");
		separator(bankingValues);
		menu(bankingValues, iGlobal.constants().ReconciliationsList(),
				"company/accounter#recounciliationsList");
		separator(bankingValues);
		StringBuilder bankListValues = new StringBuilder();
		subMenu(bankListValues, iGlobal.constants().payments(),
				"company/accounter#payments");
		subMenu(bankListValues, iGlobal.constants().bankAccounts(),
				"company/accounter#bankAccounts");
		menu(bankingValues, iGlobal.constants().bankingList(), bankListValues);
		mainMenu(builder, iGlobal.constants().banking(), bankingValues);
	}

	private void addVendorMenuItem() {

		StringBuilder vendorValue = new StringBuilder();
		menu(vendorValue, iGlobal.messages().vendorsHome(iGlobal.Vendor()),
				"S", "company/accounter#vendorHome");
		separator(vendorValue);

		int items = 0;
		StringBuilder newValues = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			subMenu(newValues, iGlobal.messages().newVendor(iGlobal.Vendor()),
					"company/accounter#newVendor");
			subMenu(newValues, iGlobal.constants().newItem() + "s",
					"company/accounter#newItemSupplier");
			items += 2;
		}
		if (canDoBanking()) {
			subMenu(newValues, iGlobal.constants().cashPurchase(),
					"company/accounter#newCashPurchase");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValues, iGlobal.messages()
					.vendorCredit(iGlobal.Vendor()),
					"company/accounter#vendorCredit");
			if (isUSType()) {
				subMenu(newValues, iGlobal.constants().newCheck(),
						"company/accounter#check");
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
					"company/accounter#enterBill");
		}
		if (canDoBanking()) {
			menu(vendorValue, iGlobal.constants().payBills(),
					"company/accounter#payBill");
			menu(vendorValue, iGlobal.constants().issuePayments(),
					"company/accounter#issuePayments");
			menu(vendorValue,
					iGlobal.messages().vendorPrePayment(iGlobal.Vendor()),
					"company/accounter#vendorPrePayment");
		}
		if (canDoInvoiceTransactions()) {
			menu(vendorValue, iGlobal.constants().recordExpenses(),
					"company/accounter#recordExpenses");
			if (preferences.isHaveEpmloyees()
					&& preferences.isTrackEmployeeExpenses()) {
				menu(vendorValue, iGlobal.constants().expenseClaims(),
						"company/accounter#expenseClaims");
			}
			separator(vendorValue);
		}

		StringBuilder supplierValues = new StringBuilder();
		subMenu(supplierValues, iGlobal.messages().vendors(iGlobal.Vendor()),
				"company/accounter#VendorList");
		if (canSeeInvoiceTransactions()) {
			subMenu(supplierValues, iGlobal.constants().items(),
					"company/accounter#items");
			subMenu(supplierValues, "Bills And Expenses",
					"company/accounter#billsAndExpenses");
		}
		if (canSeeBanking()) {
			subMenu(supplierValues,
					iGlobal.messages().vendorPayments(iGlobal.Vendor()),
					"company/accounter#vendorPayments");
		}
		menu(vendorValue, iGlobal.messages().vendorLists(iGlobal.Vendor()),
				supplierValues);

		mainMenu(builder, iGlobal.Vendor(), vendorValue);
	}

	private void addCustomerMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();
		menu(mainMenuValue, iGlobal.messages()
				.customersHome(iGlobal.Customer()),
				"company/accounter#customerHome");
		separator(mainMenuValue);

		int items = 0;
		StringBuilder newValue = new StringBuilder();
		if (canDoInvoiceTransactions()) {
			subMenu(newValue, iGlobal.messages()
					.newCustomer(iGlobal.Customer()), "C",
					"company/accounter#newCustomer");
			subMenu(newValue, iGlobal.constants().newItem(),
					"company/accounter#newItemCustomer");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(newValue, iGlobal.constants().newQuote(),
						"company/accounter#newQuote");
				items += 1;
			}
			subMenu(newValue, iGlobal.constants().newInvoice(),
					"company/accounter#newInvoice");
			items += 3;
		}
		if (canDoBanking()) {
			subMenu(newValue, iGlobal.constants().newCashSale(),
					"company/accounter#newCashSale");
			items += 1;
		}
		if (canDoInvoiceTransactions()) {
			subMenu(newValue, iGlobal.constants().newCredit(),
					"company/accounter#newCredit");
			items += 1;
		}
		if (items > 0) {
			menu(mainMenuValue, iGlobal.constants().new1(), newValue);
			separator(mainMenuValue);
		}

		if (canDoBanking()) {
			menu(mainMenuValue,
					iGlobal.messages().customerPrePayment(iGlobal.Customer()),
					"company/accounter#customerPrepayment");
			menu(mainMenuValue, iGlobal.constants().receivePayment(),
					"company/accounter#receivePayment");
			menu(mainMenuValue,
					iGlobal.messages().customerRefund(iGlobal.Customer()),
					"company/accounter#customerRefund");
			separator(mainMenuValue);
		}

		StringBuilder customerListValue = new StringBuilder();
		subMenu(customerListValue,
				iGlobal.messages().customers(iGlobal.Customer()),
				"company/accounter#customers");
		if (canSeeInvoiceTransactions()) {
			subMenu(customerListValue, iGlobal.constants().items(),
					"company/accounter#items");
			if (preferences.isDoyouwantEstimates()) {
				subMenu(customerListValue, iGlobal.constants().quotes(),
						"company/accounter#quotes");
			}
			subMenu(customerListValue, iGlobal.constants().invoices(),
					"company/accounter#invoices");
		}
		if (canSeeBanking()) {
			subMenu(customerListValue, iGlobal.constants().receivedPayments(),
					"company/accounter#receivePayments");
			subMenu(customerListValue,
					iGlobal.messages().customerRefunds(iGlobal.Customer()),
					"company/accounter#customerRefunds");
		}
		menu(mainMenuValue,
				iGlobal.messages().customerList(iGlobal.Customer()),
				customerListValue);

		mainMenu(builder, iGlobal.Customer(), mainMenuValue);
	}

	private void addCompanyMenuItem() {
		StringBuilder mainMenuValue = new StringBuilder();

		menu(mainMenuValue, iGlobal.constants().dashBoard(), "D",
				"company/accounter#dashBoard");
		separator(mainMenuValue);

		if (canDoBanking()) {
			menu(mainMenuValue, iGlobal.constants().journalEntry(), "J",
					"company/accounter#newJournalEntry");
		}

		if (canDoInvoiceTransactions()) {
			String account = iGlobal.Account();
			AccounterMessages messages = iGlobal.messages();
			menu(mainMenuValue, messages.newAccount(account), "A",
					"company/accounter#newAccount");
			separator(mainMenuValue);
		}

		if (canChangeSettings()) {
			menu(mainMenuValue, iGlobal.constants().companyPreferences(),
					"company/accounter#companyPreferences");
			separator(mainMenuValue);
		}

		if (preferences.isTrackTax()) {
			StringBuilder salesTaxValues = new StringBuilder();

			if (canDoInvoiceTransactions()) {
				subMenu(salesTaxValues, iGlobal.constants()
						.manageSalesTaxGroups(),
						"company/accounter#manageSalesTaxGroups");
			} else {
				subMenu(salesTaxValues, iGlobal.constants().salesTaxGroups(),
						"company/accounter#salesTaxGroups");
			}

			if (canDoInvoiceTransactions()) {
				subMenu(salesTaxValues, iGlobal.constants().manageSalesItems(),
						"company/accounter#manageSalesTaxItems");
			} else {
				subMenu(salesTaxValues, iGlobal.constants().salesTaxItems(),
						"company/accounter#salesTaxItems");
			}

			if (canDoInvoiceTransactions()) {
				subMenu(salesTaxValues, iGlobal.constants().taxAdjustment(),
						"company/accounter#taxAdjustment");
			}

			if (canDoBanking()) {

				subMenu(salesTaxValues, iGlobal.constants().payTax(),
						"company/accounter#paySalesTax");
			}

			if (canDoInvoiceTransactions()) {
				subMenu(salesTaxValues, iGlobal.constants().newTAXAgency(),
						"company/accounter#newTaxAgency");
			}
			menu(mainMenuValue, iGlobal.constants().itemTax(), salesTaxValues);
		}

		if (canChangeSettings()) {
			StringBuilder manageSupportLists = new StringBuilder();
			subMenu(manageSupportLists,
					iGlobal.messages().customerGroupList(iGlobal.Customer()),
					"company/accounter#customerGroupList");
			subMenu(manageSupportLists,
					iGlobal.messages().vendorGroupList(iGlobal.vendor()),
					"company/accounter#vendorGroupList");
			subMenu(manageSupportLists, iGlobal.constants().paymentTermList(),
					"company/accounter#paymentTerms");
			subMenu(manageSupportLists, iGlobal.constants()
					.shippingMethodList(),
					"company/accounter#shippingMethodsList");
			subMenu(manageSupportLists, iGlobal.constants().shippingTermList(),
					"company/accounter#shippingTermsList");
			subMenu(manageSupportLists, iGlobal.constants().priceLevelList(),
					"company/accounter#priceLevels");
			subMenu(manageSupportLists, iGlobal.constants().itemGroupList(),
					"company/accounter#itemGroupList");
			subMenu(manageSupportLists, iGlobal.constants().creditRatingList(),
					"company/accounter#creditRatingList");
			menu(mainMenuValue, iGlobal.constants().manageSupportLists(),
					manageSupportLists);
		}

		// if (canManageFiscalYears()) {
		// menu(mainMenuValue, iGlobal.constants().manageFiscalYear(),
		// "company/accounter#manageFiscalYear");
		// separator(mainMenuValue);
		// }

		StringBuilder companyLists = new StringBuilder();
		if (canSeeInvoiceTransactions()) {
			subMenu(companyLists,
					iGlobal.messages().accountsList(iGlobal.Account()),
					"company/accounter#accountsList");
		}
		if (canSeeBanking()) {
			subMenu(companyLists, iGlobal.constants().journalEntries(),
					"company/accounter#journalEntries");
		}

		if (canSeeInvoiceTransactions()) {
			subMenu(companyLists, iGlobal.constants().items(),
					"company/accounter#items");
		}
		subMenu(companyLists,
				iGlobal.messages().companyCustomers(iGlobal.Customer()),
				"company/accounter#customers");
		subMenu(companyLists,
				iGlobal.messages().companySuppliers(iGlobal.Vendor()),
				"company/accounter#VendorList");
		if (canSeeBanking()) {
			subMenu(companyLists, iGlobal.constants().payments(),
					"company/accounter#payments");
		}
		subMenu(companyLists, iGlobal.constants().salesPersons(),
				"company/accounter#salesPersons");
		subMenu(companyLists, iGlobal.constants().usersActivity(),
				"company/accounter#userActivity");
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
