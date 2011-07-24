package com.vimukti.accounter.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.Email;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TaxCode;
import com.vimukti.accounter.core.TaxGroup;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.services.AccounterService;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.services.IAccounterDAOService;
import com.vimukti.accounter.services.IAccounterGUIDAOService;
import com.vimukti.accounter.services.IAccounterReportDAOService;
import com.vimukti.accounter.services.IAccounterService;

public class FiscalYearTest extends
		AbstractDependencyInjectionSpringContextTests {
	IAccounterService accounter;
	IAccounterDAOService accounterDao;
	IAccounterGUIDAOService accounterGUIDao;
	IAccounterReportDAOService accounterReportDAOService;

	Company company;

	boolean createSubAccounts = true,

	createCustomers = true, createVendors = true,

	createItems = true,

	createCashSales = false, createInvoices = true,
			createCustomerCreditMemos = true, createCustomerRefunds = true,
			createEnterBills = true, createVendorCreditMemos = true,
			createCashPurchases = true, createWriteChecks = true,
			createMakeDeposits = false, createReceivePayments = true,
			createPayBills = true, createVendorPayments = true,
			createCreditCardCharges = true, createTransferFunds = true,
			createPaySalesTaxes = true, createExpense = false,
			createPayExpense = false,

			voidCashSales = true, voidInvoices = true,
			voidCustomerCreditMemos = true, voidCustomerRefunds = true,
			voidEnterBills = true, voidVendorCreditMemos = true,
			voidCashPurchases = true, voidWriteChecks = true,
			voidMakeDeposits = false, voidReceivePayments = true,
			voidPayBills = true, voidVendorPayments = true,
			voidCreditCardCharges = true, voidTransferFunds = true,
			voidPaySalesTaxes = true,

			checkAccountRegister = false, checkTrialBalance = false,
			checkBalanceSheet = true, checkCustomerTransactionHistory = false,
			checkVendorTransactionHistory = false, checkTesting = true,
			checkVoid = false,

			createMore = false;
	double[] d;

	public void setAccounter(IAccounterService dao) {
		this.accounter = dao;
	}

	public void setAccounterDao(IAccounterDAOService dao) {
		this.accounterDao = dao;
	}

	public void setAccounterGUIDao(IAccounterGUIDAOService dao) {
		this.accounterGUIDao = dao;
	}

	public void setAccounterReportDAOService(IAccounterReportDAOService dao) {
		this.accounterReportDAOService = dao;
	}

	@Override
	protected String getConfigPath() {
		return "/test-context.xml";
	}

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public void testCreateCompany() throws DAOException {

		// Admin User creation
		User user1 = new User();
		user1.fullName = "User1";
		user1.setEmail("User1@vimukti.com");
		user1.setPasswordSha1Hash("***REMOVED***");

		// Company Creation

		company = new Company(Company.ACCOUNTING_TYPE_US);
		company.setName("Company1");
		System.out.println("Creating company");
		try {
			// company.initDefaultUSAccounts();

			company.getUsers().add(user1);
			accounter.createCompany(company);
		} catch (DAOException e) {
			e.printStackTrace();
			fail("There should not be any exception");
		}
		assertNotNull(company.getID());
		List<Account> accounts = accounterDao.getAccounts(company.getID());
		Iterator<Account> i = accounts.iterator();
		while (i.hasNext()) {
			Account acc = (Account) i.next();
			assertEquals(acc.getName(), acc.getTotalBalance(), 0.0);
			assertEquals(acc.getName(), acc.getOpeningBalance(), 0.0);
			assertEquals(acc.getName(), acc.getCurrentBalance(), 0.0);
		}
		setDefaultAccountVariables(accounts, company.getID());
	}

	public void testCreateFiscalYear() throws Exception {

		Date startDate = format.parse("2008-03-04");
		company = accounterDao.getCompany(1L);
		Set<FiscalYear> fiscalYears = company.getFiscalYears();
		for (FiscalYear f : fiscalYears) {
			// if(f.status == FiscalYear.STATUS_OPEN){
			// f.status = FiscalYear.STATUS_CLOSE;
			// accounter.alterFiscalYear(f);
			// break;
			// }
			f.setStartDate(startDate);
			accounter.alterFiscalYear(f);

		}
	}

	public void setDefaultAccountVariables(List<Account> accounts, long c)
			throws DAOException {
		company = accounterDao.getCompany(1L);
		d = new double[accounts.size()];

		for (int i = 0; i < accounts.size(); i++)
			d[i] = accounts.get(i).getTotalBalance();
	}

	public String dueDate(int l) {
		Calendar cl = Calendar.getInstance();
		int m = cl.get(Calendar.MONTH) + 1 + l;
		int y = (m > 12 ? 1 : (m < 0 ? -1 : 0));

		String dt = ""
				+ (cl.get(Calendar.YEAR) + y)
				+ "-"
				+ new String()
						.concat(("" + ((y != 0) ? (y > 0 ? (m - 12 > 9 ? (m - 12)
								: "0" + (m - 12))
								: (m > -4 ? (12 + m) : "0" + (12 + m)))
								: m > 9 ? m : "0" + m))) + "-01";
		return dt;
	}

	public void testCreateOtherAccounts() throws DAOException, ParseException {
		double amount = 0D;

		company = accounterDao.getCompany(1L);

		double totalBalance = accounterDao.getAccount(company.getID(),
				"Opening Balances").getTotalBalance();
		double currentBalance = accounterDao.getAccount(company.getID(),
				"Opening Balances").getCurrentBalance();

		Account AccountExpense = new Account();
		AccountExpense.setCompany(accounterDao.getCompany(1L));
		AccountExpense.setNumber("3333");
		AccountExpense.setName("Account Expense");
		AccountExpense.setType(Account.TYPE_EXPENSE);
		AccountExpense.setIsActive(true);
		AccountExpense.setIncrease(true);
		AccountExpense.isConsiderAsCashAccount = true;
		AccountExpense.setOpeningBalance(amount);
		AccountExpense.setAsOf(format.parse("2008-03-04"));
		accounter.createAccount(AccountExpense);
	}

	public void testAlterAccounts() throws Exception {
		company = accounterDao.getCompany(1L);
		for (Account account : company.getAccounts()) {
			if (account.getName().equals(AccounterConstants.WRITE_OFF)
					|| account.getName().equals(
							AccounterConstants.CASH_DISCOUNT_GIVEN)
					|| account.getName().equals(
							AccounterConstants.RETAINED_EARNINGS)) {
				account.isConsiderAsCashAccount = true;
				accounter.alterAccount(account);
			}
		}
	}

	public void testCallCreateSubAccount(String num, String name, int type,
			String p, boolean a, boolean i, double bal, String date, String comp)
			throws Exception {
		Account acc = new Account();

		company = accounterDao.getCompany(1L);

		acc.setNumber(num);
		acc.setName(name);
		acc.setType(type);
		acc.setParent(accounterDao.getAccount(company.getID(), p));
		acc.setIsActive(a);
		acc.setIncrease(i);
		acc.setOpeningBalance(bal);
		acc.setAsOf(format.parse(date));

		if (type != Account.TYPE_CREDIT_CARD && type != Account.TYPE_BANK)
			acc.setHierarchy((acc.getParent() != null) ? Utility
					.getHierarchy(acc.getParent())
					+ acc.getName() : null);

		acc.setCompany(company);
		accounter.createAccount(acc);

	}

	public void testCreateSubAccount() throws Exception {

		try {

			company = accounterDao.getCompany(1l);

			if (createSubAccounts) {
				testCallCreateSubAccount("4201", "SubWO", Account.TYPE_INCOME,
						"Write off", true, true, 0D, "2008-01-01", "Company1");
				testCallCreateSubAccount("6251", "SubBC", Account.TYPE_EXPENSE,
						"Bank Charge", true, false, 0D, "2009-01-01",
						"Company1");
				// testCallCreateSubAccount("3001", "SubOB",
				// Account.TYPE_EQUITY,
				// "Opening Balances", true, true, 1000D, dueDate(-1),
				// "Company1");
				// testCallCreateSubAccount("3101", "SubRE",
				// Account.TYPE_EQUITY,
				// "Retained Earnings", true, true, 1000D, dueDate(-1),
				// "Company1");
				// testCallCreateSubAccount("1701", "SubAC", Account.TYPE_CASH,
				// "Account Cash", true, false, 1000D, dueDate(-1),
				// "Company1");
				// testCallCreateSubAccount("1001", "SubUDF", Account.TYPE_CASH,
				// "Un Deposited Funds", true, false, 1000D, dueDate(-1),
				// "Company1");
				// testCallCreateSubAccount("2501", "SubOCA",
				// Account.TYPE_OTHER_CURRENT_ASSET, "Account OCA", true,
				// false, 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("4501", "SubFA",
				// Account.TYPE_FIXED_ASSET, "Account FA", true, false,
				// 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("3701", "SubOA",
				// Account.TYPE_OTHER_ASSET, "Account OA", true, false,
				// 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("2011", "SubPIR",
				// Account.TYPE_OTHER_CURRENT_LIABILITY,
				// "Pending Item Receipts", true, true, 1000D,
				// dueDate(-1), "Company1");
				// testCallCreateSubAccount("2051", "SubSTP",
				// Account.TYPE_OTHER_CURRENT_LIABILITY,
				// "Sales Tax Payable", true, true, 1000D, dueDate(-1),
				// "Company1");
				// testCallCreateSubAccount("2111", "SubEPL",
				// Account.TYPE_PAYROLL_LIABILITY,
				// "Employee Payroll Liabilities", true, true, 1000D,
				// dueDate(-1), "Company1");
				// testCallCreateSubAccount("5501", "SubLL",
				// Account.TYPE_LONG_TERM_LIABILITY, "Account LL", true,
				// true, 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("5101", "SubCDT",
				// Account.TYPE_COST_OF_GOODS_SOLD, "Cash Discount taken",
				// true, false, 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("4101", "SubCDG",
				// Account.TYPE_INCOME,
				// "Cash Discount Given", true, true, 1000D, dueDate(-1),
				// "Company1");
				// testCallCreateSubAccount("501", "SubOI",
				// Account.TYPE_OTHER_INCOME, "Account OI", true, true,
				// 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("1501", "SubOE",
				// Account.TYPE_OTHER_EXPENSE, "Account OE", true, false,
				// 1000D, dueDate(-1), "Company1");

			}
		} catch (DAOException E) {
			E.printStackTrace();
			fail("Sub Account Creation Failed!");
		}
	}

	public Customer testCallCreateCustomer(String name, String FAs, String cg,
			Set<Email> em, Set<Contact> c, Set<Address> a, String since,
			String asof, double bal, Company company) throws DAOException {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setFileAs(FAs);
		customer.setCustomerGroup(cg != null ? accounterDao.getCustomerGroup(
				company.getID(), cg) : null);
		customer.setEmails(em);
		customer.setAddress(a);
		customer.setContacts(c);
		try {
			customer.setCustomerGroup(accounterDao.getCustomerGroup(company
					.getID(), 1L));
		} catch (DAOException e1) {
			e1.printStackTrace();
		}

		try {
			customer.setPayeeSince(format.parse(since));
			customer.setBalanceAsOf(format.parse(asof));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		customer.setOpeningBalance(bal);
		customer.setCompany(company);
		accounter.createCustomer(customer);
		return customer;
	}

	@SuppressWarnings("unchecked")
	public void testCreateCustomer() throws Exception {

		if (createCustomers) {
			company = accounterDao.getCompany(1L);
			double amt1 = 0.0, amt2 = 0.0;
			double OBCurrentBalance = accounterDao.getAccount(company.getID(),
					AccounterConstants.OPENING_BALANCE).getCurrentBalance(), OBTotalBalance = accounterDao
					.getAccount(company.getID(),
							AccounterConstants.OPENING_BALANCE)
					.getTotalBalance();

			ArrayList<Customer> custs = new ArrayList<Customer>();
			try {

				amt1 = accounterDao.getAccount(company.getID(),
						"Accounts Receivable").getOpeningBalance();

				custs.add(testCallCreateCustomer("Customer1",
						"Customer1 Fileas", null, null, null, null,
						"2009-12-28", "2008-03-04", 20000.0, company));
				custs.add(testCallCreateCustomer("Customer2",
						"Customer2 Fileas", null, null, null, null,
						"2009-12-28", "2009-01-01", 25000.0, company));

			} catch (DAOException e) {
				e.printStackTrace();
				fail("Customer failed!");
			}

		}
	}

	public double getAmountAllLineTotal(List<TransactionItem> ti) {
		double total = 0.0;
		Iterator<TransactionItem> i = ti.iterator();

		while (i.hasNext()) {
			TransactionItem tr = (TransactionItem) i.next();
			total += tr.getLineTotal();
		}

		return total;
	}

	public Invoice testCallCreateInvoice(String d, long cm, String cu,
			String sp, String tg, List<TransactionItem> ti, String dd,
			double p, Invoice inv) throws Exception {

		Customer customer = accounterDao.getCustomer(cm, cu);

		inv.setNumber(accounterGUIDao.getNextTransactionNumber(cm,
				Transaction.TYPE_INVOICE));
		inv.setCustomer(customer);
		inv.setCompany(accounterDao.getCompany(1l));
		inv.setType(Transaction.TYPE_INVOICE);
		TaxGroup t = tg != null ? accounterDao.getTaxGroup(cm, tg) : null;
		for (int i = 0; i < ti.size(); i++) {
			if (ti.get(i).isTaxable)
				ti.get(i).taxGroup = t;
		}
		inv.setDate(format.parse(d));
		inv.setSalesPerson(sp != null ? accounterDao.getSalesPerson(cm, sp)
				: null);
		inv.setTransactionItems(ti);
		// inv.setSalesTaxAmount(getAmountSalesTax(tg, ti));
		inv.setDueDate(format.parse(dd));
		inv.setAllLineTotal(getAmountAllLineTotal(ti));
		// inv.setDiscountTotal(getAmountDiscountTotal(ti));
		inv
				.setBalanceDue((inv.getAllLineTotal() + inv.getSalesTaxAmount())
						- p);
		inv.setTotal(inv.getAllLineTotal() + inv.getSalesTaxAmount());
		inv.setPayments(p);
		// inv.setAllTaxableLineTotal(getAmountAllTaxableLineTotal(ti));
		double customerBalance = customer.getBalance();

		accounter.createInvoice(inv);
		if (checkTesting) {
			customer = accounterDao.getCustomer(cm, cu);
			assertEquals(customerBalance + inv.getTotal(), customer
					.getBalance());
		}
		return inv;
	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double q, double up, boolean it, String ac, Invoice inv)
			throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setQuantity(q);
		item.setUnitPrice(2000d);
		item.setLineTotal(q * up);
		item.isTaxable = (it);
		item.setAccount(accounterDao.getAccount(accounterDao.getCompany(1L)
				.getID(), ac));
		item.setTransaction(inv);
		return item;
	}

	public void testCreateInvoice() throws Exception {
		if (createInvoices) {

			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Item> items = accounterDao.getItems(company.getID());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getID());
			ListIterator<Account> acs = accounts.listIterator();

			List<TaxCode> codes = accounterDao.getTaxCodes(company.getID());
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getName().equals("None"))
					codes.remove(i);
			}
			ListIterator<TaxCode> tcs = codes.listIterator();

			// taxAgencies = new double[codes.size()];
			// for (int i = 0; i < taxAgencies.length; i++)
			// taxAgencies[getTaxAgencyVariable(codes, codes.get(i)
			// .getTaxAgency().getName(), company)] = codes.get(i)
			// .getTaxAgency().getBalance();

			setDefaultAccountVariables(accounts, company.getID());

			Invoice invoice1 = new Invoice();
			Invoice invoice2 = new Invoice();
			// Invoice invoice3 = new Invoice();
			// Invoice invoice4 = new Invoice();
			// Invoice invoice5 = new Invoice();
			// Invoice invoice6 = new Invoice();
			// Invoice invoice7 = new Invoice();
			// Invoice invoice8 = new Invoice();
			// Invoice invoice9 = new Invoice();
			// Invoice invoice10 = new Invoice();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems3 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems4 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems5 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems6 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems7 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems8 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems9 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems10 = new
			// ArrayList<TransactionItem>();

			// testCallTransactionItem( Type in int, for item type entry
			// Quantity in Double,
			// UnitPrice in double,
			// Discount in
			// Double,
			// itemtax in long,
			// item in long,
			// Invoice ) returns
			// TransactionItems object
			//
			// testCallTransactionItem( Type in int, for account type entry
			// quantity
			// unitprice
			// itemTax in long, // Account
			// Transaction(Invoice) ) returns TransactionItems object
			//
			// testCallTransactionItem( Type in int, for SALESTAX ACCOUNT type
			// entry
			// LineTotal in Double,
			// TaxCode
			// Transaction(Invoice) ) returns TransactionItems object

			//transactionItems1.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true, //
			// "Retained Earnings", invoice1));
			//transactionItems2.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true, //
			// "Employee Payroll Liabilities", invoice2));

			if (invoice1 != null) {
				// for (int n = 0; n < (items.size() / 2); n++) {
				// Item item = nextItem(its);
				// transactionItems1.add(testCallCreateTransactionItem(company
				// .getName(), TransactionItem.TYPE_ITEM, 1D, item
				// .getSalesPrice(), 0D, true, item.getName(),
				// invoice1));

				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
						false, AccounterConstants.RETAINED_EARNINGS, invoice1));
				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 100D,
						false, AccounterConstants.WRITE_OFF, invoice1));
				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 300D,
						false, "SubWO", invoice1));
				transactionItems1
						.add(testCallCreateTransactionItem(company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 400D, false,
								AccounterConstants.CASH_DISCOUNT_TAKEN,
								invoice1));
				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 500D,
						false, "Account Expense", invoice1));

			}

			if (invoice2 != null) {
				// for (int n = (items.size() / 2) + 1; n <= items.size(); n++)
				// {
				// Item item = nextItem(its);
				// transactionItems2.add(testCallCreateTransactionItem(company
				// .getName(), TransactionItem.TYPE_ITEM, 1D, item
				// .getSalesPrice(), 0D, true, item.getName(),
				// invoice2));
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 500D,
						false, AccounterConstants.RETAINED_EARNINGS, invoice2));
				transactionItems2
						.add(testCallCreateTransactionItem(company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 250D, false,
								AccounterConstants.CASH_DISCOUNT_GIVEN,
								invoice2));
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 350D,
						false, AccounterConstants.WRITE_OFF, invoice2));
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 450D,
						false, "SubWO", invoice2));
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 550D,
						false, AccounterConstants.BANK_CHARGE, invoice2));
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 650D,
						false, "SubBC", invoice2));

			}

			// if (invoice3 != null) {
			// // for (int n = 0; n < (accounts.size() / 2); n++)
			// transactionItems3.add(testCallCreateTransactionItem(company
			// .getName(), TransactionItem.TYPE_ACCOUNT, 1D,
			// 1000D, true, AccounterConstants.WRITE_OFF, invoice3));
			// }
			//
			// if (invoice4 != null) {
			// // for (int n = (accounts.size() / 2) + 1; n <= accounts.size();
			// n++)
			// transactionItems4.add(testCallCreateTransactionItem(company
			// .getName(), TransactionItem.TYPE_ACCOUNT, 1D,
			// 100D, true, AccounterConstants.CASH_DISCOUNT_TAKEN, invoice4));
			// }

			// if (invoice5 != null) {
			// for (int n = 0; n < (codes.size() * 5); n++)
			// transactionItems5.add(testCallCreateTransactionItem(company
			// .getName(), TransactionItem.TYPE_SALESTAX, 1000D,
			// nextTaxCode(tcs).getName(), invoice5));
			// }

			// testCallCreateInvoice() returns Invoice Object
			// * Date in String
			// * Company in Long
			// * Accounts Receivable in Long
			// * Customer in String
			// * SalesPerson in String, // * TaxGroup in Long
			// * TransactionItems in List
			// * Due date in String
			// * Payments in Double
			// * Invoice in Invoice
			// *

			ArrayList<Invoice> Invoices = new ArrayList<Invoice>();

			if (transactionItems1.size() > 0)
				Invoices.add(testCallCreateInvoice("2008-05-01", 1l,
						"Customer1", null, null, transactionItems1,
						"2008-05-01", 0d, invoice1));

			if (transactionItems2.size() > 0)
				Invoices.add(testCallCreateInvoice("2009-12-28", 1l,
						"Customer2", null, null, transactionItems2,
						"2009-12-28", 0d, invoice2));

			// if (transactionItems3.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer3",
			// "SalesPerson1", "TG31", transactionItems3, dueDate(1),
			// 0d, invoice3));
			//
			// if (transactionItems4.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer4",
			// "SalesPerson1", "TG123", transactionItems4, dueDate(2),
			// 0d, invoice4));
			//
			// if (transactionItems5.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer5",
			// "SalesPerson1", "TG12", transactionItems5, dueDate(1),
			// 0d, invoice5));
			//
			// if (transactionItems6.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer1",
			// "SalesPerson1", "TG12", transactionItems6, dueDate(1),
			// 0d, invoice6));
			//
			// if (transactionItems7.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer2",
			// "SalesPerson1", "TG23", transactionItems7, dueDate(2),
			// 0d, invoice7));
			//
			// if (transactionItems8.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer3",
			// "SalesPerson1", "TG31", transactionItems8, dueDate(1),
			// 0d, invoice8));
			//
			// if (transactionItems9.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer4",
			// "SalesPerson1", "TG123", transactionItems9, dueDate(2),
			// 0d, invoice9));
			//
			// if (transactionItems10.size() > 0)
			// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer5",
			// "SalesPerson1", "TG12", transactionItems10, dueDate(1),
			// 0d, invoice10));

		}
	}

	public CustomerCreditMemo testCallCreateCustomerCreditMemo(String d,
			long cm, String cu, String sp, String tg, List<TransactionItem> ti,
			CustomerCreditMemo ccm) throws Exception {

		ccm.setCustomer(accounterDao.getCustomer(accounterDao.getCompany(1L)
				.getID(), cu));
		ccm.setCompany(accounterDao.getCompany(1L));
		ccm.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);
		ccm.setDate(format.parse(d));
		ccm.setSalesPerson(sp != null ? accounterDao.getSalesPerson(
				accounterDao.getCompany(1L).getID(), sp) : null);
		TaxGroup t = tg != null ? accounterDao.getTaxGroup(cm, tg) : null;
		for (int i = 0; i < ti.size(); i++) {
			if (ti.get(i).isTaxable)
				ti.get(i).taxGroup = t;
		}
		ccm.setTransactionItems(ti);
		// ccm.setSalesTax(getAmountSalesTax(tg, ti));
		ccm.setNumber(accounterGUIDao.getNextTransactionNumber(cm,
				Transaction.TYPE_CUSTOMER_CREDIT_MEMO));
		ccm.setAllLineTotal(getAmountAllLineTotal(ti));
		// ccm.setDiscountTotal(getAmountDiscountTotal(ti));
		ccm.setTotal((ccm.getAllLineTotal() + ccm.getSalesTax())
				- ccm.getDiscountTotal());
		// ccm.setAllTaxableLineTotal(getAmountAllTaxableLineTotal(ti));
		double customerBalance = ccm.getCustomer().getBalance();
		accounter.createCustomerCreditMemo(ccm);
		if (checkTesting)
			assertEquals(customerBalance - ccm.getTotal(), accounterDao
					.getCustomer(company.getID(), ccm.getCustomer().getID())
					.getBalance());
		return ccm;

	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double q, double up, boolean it, String ac, CustomerCreditMemo ccm)
			throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setQuantity(q);
		item.setUnitPrice(up);
		item.setLineTotal(q * up);
		item.isTaxable = (it);
		item.setAccount(accounterDao.getAccount(accounterDao.getCompany(1L)
				.getID(), ac));
		item.setTransaction(ccm);
		return item;
	}

	public void testCreateCustomerCreditMemo() throws Exception {

		if (createCustomerCreditMemos) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Item> items = accounterDao.getItems(company.getID());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getID());
			ListIterator<Account> acs = accounts.listIterator();

			List<TaxCode> codes = accounterDao.getTaxCodes(company.getID());
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getName().equals("None"))
					codes.remove(i);
			}
			ListIterator<TaxCode> tcs = codes.listIterator();

			setDefaultAccountVariables(accounts, company.getID());

			CustomerCreditMemo creditMemo1 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo2 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo3 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo4 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo5 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo6 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo7 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo8 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo9 = new CustomerCreditMemo();
			// CustomerCreditMemo creditMemo10 = new CustomerCreditMemo();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems3 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems4 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems5 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems6 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems7 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems8 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems9 = new
			// ArrayList<TransactionItem>();
			// List<TransactionItem> transactionItems10 = new
			// ArrayList<TransactionItem>();

			// /*testCallTransactionItem( company in long
			// Type in int, for item type entry
			// Quantity in Double, // UnitPrice in double, // Discount% in
			// Double, // itemtax in String, // item in String, //
			// CustomerCreditmemo ) returns TransactionItems object
			//
			// /*testCallTransactionItem( company in long
			// Type in int, for account type entry
			// quantity in double
			// unitprice in double
			// itemTax in String, // Account in String
			// Transaction(Invoice) ) returns TransactionItems object
			//
			// /*testCallTransactionItem( company in long
			// Type in int, for account type entry
			// LineTotal in Double, // Taxcode in String
			// Transaction(ccm) ) returns TransactionItems object

			//transactionItems1.add(testCallCreateTransactionItem(company.getName
			// (),
			// TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true,
			// AccounterConstants.OPENING_BALANCE, creditMemo1));
			//transactionItems2.add(testCallCreateTransactionItem(company.getName
			// (),
			// TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true, "Bank Charge",
			// creditMemo2));
			if (creditMemo1 != null) {
				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 700D,
						false, AccounterConstants.RETAINED_EARNINGS,
						creditMemo1));
				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 600D,
						false, AccounterConstants.CASH_DISCOUNT_GIVEN,
						creditMemo1));
			}

			if (creditMemo2 != null) {
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 500D,
						false, AccounterConstants.WRITE_OFF, creditMemo2));
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 400D,
						false, AccounterConstants.BANK_CHARGE, creditMemo2));

			}

			// /* testCallCreateCustomerCreditMemo() returns CustomerCreditMemo
			// Object
			// * Type in int
			// * Date in String
			// * Company in Long
			// * Accounts Receivable in Long
			// * Customer in String
			// * SalesPerson in String, // * TaxGroup in Long
			// * TransactionItems in List
			// * ALL Line Total in Double
			// * Discount Total in Double
			// * SalesTax in Double
			// * ALLTaxableLIneTotal in Double
			// * Customercreditmemo in Customer credit memo

			ArrayList<CustomerCreditMemo> ccms = new ArrayList<CustomerCreditMemo>();

			if (transactionItems1.size() > 0)
				ccms
						.add(testCallCreateCustomerCreditMemo("2008-12-31", 1l,
								"Customer1", null, null, transactionItems1,
								creditMemo1));

			if (transactionItems2.size() > 0)
				ccms
						.add(testCallCreateCustomerCreditMemo("2008-12-31", 1l,
								"Customer2", null, null, transactionItems2,
								creditMemo2));

		}
	}

	public void testCloseFiscalYear() throws Exception {

		company = accounterDao.getCompany(1L);
		Set<FiscalYear> fiscalYears = company.getFiscalYears();
		for (FiscalYear f : fiscalYears) {
			if (f.status == FiscalYear.STATUS_OPEN) {
				f.status = FiscalYear.STATUS_CLOSE;
				accounter.alterFiscalYear(f);
				break;
			}
			// f.setStartDate(startDate);
			// accounter.alterFiscalYear(f);

		}
	}

}
