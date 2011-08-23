package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;

class Section {
	String title;
	String[] heads;
	String[][] options;

	Section(String t, String[] h, String[][] o) {
		title = t;
		heads = h;
		options = o;
	}
}

public interface IOptionsAccordionSections {
	AccounterConstants constants = Accounter.constants();
	final String COMPANY = constants.company();
	final String COMPANY_NEW_ACCOUNT = Accounter.messages().newAccount(
			Global.get().Account());

	final String CUSTOMERS = Accounter.messages().customers(
			Global.get().Customer());
	final String CUSTOMERS_HOME = Accounter.messages().customersHome(
			Global.get().Customer());
	final String CUSTOMERS_NEW = Accounter.messages().newCustomer(
			Global.get().Customer());
	final String CUSTOMERS_NEW_ITEM = constants.newItem();
	final String CUSTOMERS_NEW_QUOTE = constants.newQuote();
	final String CUSTOMERS_NEW_INVOICE = constants.newInvoice();
	final String CUSTOMER_RECEIVED_PAYMENT = constants.receivedPayment();
	final String CUSTOMER_NEW_CASH_SALE = constants.newCashSale();
	final String CUSTOMER_NEW_CREDITS_AND_REFUNDS = constants
			.creditAndRefunds();
	final String CUSTOMER_CREDIT_CARD_PROCESSING = constants
			.creditCardProcessing();
	final String CUSTOMER_FIND_CUSTOMERS = Accounter.messages().customers(
			Global.get().Customer());
	final String CUSTOMER_FIND_ITEMS = constants.products();
	final String CUSTOMER_FIND_QUOTES = constants.quotes();
	final String CUSTOMER_FIND_INVOICES = constants.invoices();
	final String CUSTOMER_FIND_RECEIVED_PAYMENTS = constants.receivedPayments();
	final String CUSTOMER_FIND_CUSTOMER_REFUNDS = Accounter.messages()
			.customerRefunds(Global.get().customer());

	final String VENDORS = Global.get().messages()
			.vendors(Global.get().Vendor());
	final String VENDORS_HOME = Global.get().messages()
			.supplierHome(Global.get().Vendor());
	final String VENDORS_NEW_VENDOR = Global.get().messages()
			.newSupplier(Global.get().Vendor());
	final String VENDORS_NEW_ITEM = constants.newProduct();
	final String VENDORS_ENTER_BILLS = constants.enterBills();
	final String VENDORS_PAY_BILLS = constants.payBills();
	final String VENDORS_NEW_CASH_PURCHASE = constants.newCashPurchase();
	final String VENDORS_ISSUE_PAYMENT = constants.issuePayment();
	final String VENDORS_NEW_CREDIT_MEMO = constants.newCredit();
	final String VENDORS_FIND_VENDORS = Global.get().messages()
			.suppliers(Global.get().Vendor());
	final String VENDORS_FIND_ITEMS = constants.products();
	final String VENDORS_FIND_BILLS = constants.bills();
	final String VENDORS_FIND_VENDOR_PAYMENTS = Global.get().messages()
			.supplierPayments(Global.get().Vendor());

	final String BANKING = constants.banking();
	final String BANKING_HOME = constants.bankingHome();
	final String BANKING_NEW_BANK_ACCOUNT = Accounter.messages()
			.newBankAccount(Global.get().Account());
	final String BANKING_WRITE_CHECKS = constants.writeChecks();
	final String BANKING_MAKE_DEPOSIT = constants.makeDeposit();
	final String BANKING_TRANSFER_FUNDS = constants.transferFunds();
	final String BANKING_PAY_BILLS = constants.payBills();
	final String BANKING_CREDITCARD_CHARGE = constants.creditCardCharge();
	final String BANKING_FIND_CHART_OF_ACCOUNTS = Accounter.messages()
			.chartOfAccounts(Global.get().Account());
	final String BANKING_FIND_PAYMENTS = constants.payments();

	final String START_NEW_TASK = constants.startanewTask();
	final String HOME = constants.companyHome();
	final String FIND = constants.find();

	final Section[] sections = {

			new Section(COMPANY, new String[] { START_NEW_TASK },
					new String[][] { { HOME, COMPANY_NEW_ACCOUNT } }),

			new Section(CUSTOMERS, new String[] { START_NEW_TASK, FIND },
					new String[][] {
							{ CUSTOMERS_HOME, CUSTOMERS_NEW,
									CUSTOMERS_NEW_ITEM, CUSTOMERS_NEW_QUOTE,
									CUSTOMERS_NEW_INVOICE,
									CUSTOMER_RECEIVED_PAYMENT,
									CUSTOMER_NEW_CASH_SALE,
									CUSTOMER_NEW_CREDITS_AND_REFUNDS,
									CUSTOMER_CREDIT_CARD_PROCESSING },
							{ CUSTOMER_FIND_CUSTOMERS, CUSTOMER_FIND_ITEMS,
									CUSTOMER_FIND_QUOTES,
									CUSTOMER_FIND_INVOICES,
									CUSTOMER_FIND_RECEIVED_PAYMENTS,
									CUSTOMER_FIND_CUSTOMER_REFUNDS } }),

			new Section(VENDORS, new String[] { START_NEW_TASK, FIND },
					new String[][] {
							{ VENDORS_HOME, VENDORS_NEW_VENDOR,
									VENDORS_NEW_ITEM, VENDORS_ENTER_BILLS,
									VENDORS_PAY_BILLS,
									VENDORS_NEW_CASH_PURCHASE,
									VENDORS_ISSUE_PAYMENT,
									VENDORS_NEW_CREDIT_MEMO },
							{ VENDORS_FIND_VENDORS, VENDORS_FIND_ITEMS,
									VENDORS_FIND_BILLS,
									VENDORS_FIND_VENDOR_PAYMENTS } }),

			// new Section("Employees",
			// new String[] { "Start a new Task", "Find" },
			// new String[][] {
			// { "Employee Empty", "Employee Blah",
			// "Employee Temployee" },
			// { "Find tis", "Find tat", "Find bla" } }),

			// new Section("Animals", new String[] { "Domestic", "Wild",
			// "Whatever" }, new String[][] { { "Dog", "Cat" },
			// { "Lion", "Anaconda", "Tiger" },
			// { "Mouse", "Elephant", "Snake" } }), };

			new Section(BANKING, new String[] { START_NEW_TASK, FIND },
					new String[][] {
							{ BANKING_HOME, BANKING_NEW_BANK_ACCOUNT,
									BANKING_WRITE_CHECKS, BANKING_MAKE_DEPOSIT,
									BANKING_TRANSFER_FUNDS, BANKING_PAY_BILLS,
									BANKING_CREDITCARD_CHARGE },
							{ BANKING_FIND_CHART_OF_ACCOUNTS,
									BANKING_FIND_PAYMENTS

							}

					}), };

	final String[] sectionLabels = {
			Accounter.messages().customers(Global.get().Customer()),
			Global.get().messages().suppliers(Global.get().Vendor()),
			constants.employees(), constants.sectionhash1(),
			constants.sectionhash2(), constants.sectionhash3(),
			constants.sectionhash4() };

	final String[][][] labels = {

	};
	final String[] custStartLabels = {
			Accounter.messages().newCustomer(Global.get().Customer()),
			constants.newProduct(), constants.newQuote(),
			constants.newInvoice(), constants.receivedPayment(),
			constants.newCashSale(), constants.newCreditsAndRefunds(),
			constants.creditCardProcessing() };

	final String[] custFindLabels = {
			Accounter.messages().customers(Global.get().Customer()),
			constants.products() };

	// Vendors Section
	final String[] vendStartLabels = {
			Global.get().messages().supplierBendor(Global.get().Vendor()),
			Global.get().messages().supplierBlah(Global.get().Vendor()),
			Global.get().messages().supplierWhendor(Global.get().Vendor()) };

	final String[] vendFindLabels = { constants.findtis(), constants.findtat(),
			constants.findbla() };

	// Employees Section
	final String[] empStartLabels = { constants.employeeEmpty(),
			constants.employeeBlah(), constants.employeeTemployee() };

	final String[] empFindLabels = { constants.findtis(), constants.findtat(),
			constants.findbla() };

	// ...
	final String[] startLabels = { constants.emptyexlematorymark1(),
			constants.emptyexlematorymark2(), constants.enuffexlematorymark() };

	final String[] findLabels = { constants.wat(), constants.u(),
			constants.want() };

}
