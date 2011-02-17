package com.vimukti.accounter.web.client.ui;

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

	final String COMPANY = "Company";
	final String COMPANY_NEW_ACCOUNT = "New Account";

	final String CUSTOMERS = "Customers";
	final String CUSTOMERS_HOME = "Customer Home";
	final String CUSTOMERS_NEW = "New Customer";
	final String CUSTOMERS_NEW_ITEM = "New Item";
	final String CUSTOMERS_NEW_QUOTE = "New Quote";
	final String CUSTOMERS_NEW_INVOICE = "New Invoice";
	final String CUSTOMER_RECEIVED_PAYMENT = "Received Payment";
	final String CUSTOMER_NEW_CASH_SALE = "New Cash Sale";
	final String CUSTOMER_NEW_CREDITS_AND_REFUNDS = "New Credits and Refunds";
	final String CUSTOMER_CREDIT_CARD_PROCESSING = "Credit Card Processing";
	final String CUSTOMER_FIND_CUSTOMERS = "Customers";
	final String CUSTOMER_FIND_ITEMS = "Products";
	final String CUSTOMER_FIND_QUOTES = "Quotes";
	final String CUSTOMER_FIND_INVOICES = "Invoices";
	final String CUSTOMER_FIND_RECEIVED_PAYMENTS = "Received Payments";
	final String CUSTOMER_FIND_CUSTOMER_REFUNDS = "Customer Refunds";

	final String VENDORS = UIUtils.getVendorString("Supplier", "Vendors");
	final String VENDORS_HOME = UIUtils.getVendorString("Supplier Home",
			"Vendor Home");
	final String VENDORS_NEW_VENDOR = UIUtils.getVendorString("New Supplier",
			"New Vendor");
	final String VENDORS_NEW_ITEM = "New Product";
	final String VENDORS_ENTER_BILLS = "Enter Bills";
	final String VENDORS_PAY_BILLS = "Pay Bills";
	final String VENDORS_NEW_CASH_PURCHASE = "New Cash Purchase";
	final String VENDORS_ISSUE_PAYMENT = "Issue Payment";
	final String VENDORS_NEW_CREDIT_MEMO = "New Credit";
	final String VENDORS_FIND_VENDORS = UIUtils.getVendorString("Suppliers",
			"Vendors");
	final String VENDORS_FIND_ITEMS = "Products";
	final String VENDORS_FIND_BILLS = "Bills";
	final String VENDORS_FIND_VENDOR_PAYMENTS = UIUtils.getVendorString(
			"Supplier Payments", "Vendor Payments");

	final String BANKING = "Banking";
	final String BANKING_HOME = "Banking Home";
	final String BANKING_NEW_BANK_ACCOUNT = "New Bank Account";
	final String BANKING_WRITE_CHECKS = "Write Checks";
	final String BANKING_MAKE_DEPOSIT = "Make Deposit";
	final String BANKING_TRANSFER_FUNDS = "Transfer Funds";
	final String BANKING_PAY_BILLS = "Pay Bills";
	final String BANKING_CREDITCARD_CHARGE = "Credit Card Charge";
	final String BANKING_FIND_CHART_OF_ACCOUNTS = "Chart of Accounts";
	final String BANKING_FIND_PAYMENTS = "Payments";

	final String START_NEW_TASK = "Start a new Task";
	final String HOME = "Company Home";
	final String FIND = "Find";

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

	final String[] sectionLabels = { "Customers",
			UIUtils.getVendorString("Suppliers", "Vendors"), "Employees",
			"Section #1", "Section #2", "Section #3", "Section #4" };

	final String[][][] labels = {

	};
	final String[] custStartLabels = { "New Customer", "New Product",
			"New Quote", "New Invoice", "Received Payment", "New Cash Sale",
			"New Credits and Refunds", "Credit Card Processing" };

	final String[] custFindLabels = { "Customers", "Products" };

	// Vendors Section
	final String[] vendStartLabels = {
			UIUtils.getVendorString("Supplier Bendor", "Vendor Bendor"),
			UIUtils.getVendorString("Supplier Blah ", "Vendor Blah"),
			UIUtils.getVendorString("Supplier Whendor", "Vendor Whendor") };

	final String[] vendFindLabels = { "Find tis", "Find tat", "Find bla" };

	// Employees Section
	final String[] empStartLabels = { "Employee Empty", "Employee Blah",
			"Employee Temployee" };

	final String[] empFindLabels = { "Find tis", "Find tat", "Find bla" };

	// ...
	final String[] startLabels = { "Empty!", "Empty!!", "Enuff!" };

	final String[] findLabels = { "wat?", "u?", "want?" };

}
