package com.vimukti.accounter.core;

import java.io.FileOutputStream;
import java.io.IOException;
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
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.Email;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Expense;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PayExpense;
import com.vimukti.accounter.core.PaySalesTax;
import com.vimukti.accounter.core.PaySalesTaxEntries;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.TaxAgency;
import com.vimukti.accounter.core.TaxCode;
import com.vimukti.accounter.core.TaxGroup;
import com.vimukti.accounter.core.TaxRates;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionCreditsAndPayments;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.TransactionPayExpense;
import com.vimukti.accounter.core.TransactionPaySalesTax;
import com.vimukti.accounter.core.TransactionReceivePayment;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.services.IAccounterDAOService;
import com.vimukti.accounter.services.IAccounterGUIDAOService;
import com.vimukti.accounter.services.IAccounterReportDAOService;
import com.vimukti.accounter.services.IAccounterService;
import com.vimukti.accounter.web.client.core.Lists.MakeDepositTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;

public class AccounterDAOTest extends
		AbstractDependencyInjectionSpringContextTests {

	IAccounterService accounter;
	IAccounterDAOService accounterDao;
	IAccounterGUIDAOService accounterGUIDao;
	IAccounterReportDAOService accounterReportDAOService;

	Company company;

	boolean createSubAccounts = true,

	createCustomers = true, createVendors = true,

	createItems = false,

	createCashSales = false, createInvoices = true,
			createCustomerCreditMemos = true, createCustomerRefunds = false,
			createEnterBills = true, createVendorCreditMemos = true,
			createCashPurchases = false, createWriteChecks = false,
			createMakeDeposits = false, createReceivePayments = true,
			createPayBills = true, createVendorPayments = true,
			createCreditCardCharges = false, createTransferFunds = true,
			createPaySalesTaxes = false, createExpense = false,
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

			createMore = true;

	double[] d;
	double[] trialBalance;
	double[][] t;
	double[] taxAgencies;
	List<TrialBalance> trialBalanceList;

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

	public void testCreateUser() throws DAOException, IOException {

		// User user1 = new User();
		// user1.fullName = "User1";
		// user1.setEmail("User1@vimukti.com");
		// user1.setPasswordSha1Hash("***REMOVED***");
		//
		// try {
		// accounter.createUser(user1);
		// } catch (DAOException e) {
		// e.printStackTrace();
		// fail("There should not be any exception");
		// }
		// assertNotNull(user1.getId());
		// System.out.println("HH");
		// // FileOutputStream fs=new FileOutputStream("gh");
		// byte[] b = new byte[2];
		// b[0] = 65;
		// b[1] = 66;
		// FileOutputStream fs;
		// fs.write(b, 0, 1);

	}

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
		assertNotNull(company.getId());
		List<Account> accounts = accounterDao.getAccounts(company.getId());
		Iterator<Account> i = accounts.iterator();
		while (i.hasNext()) {
			Account acc = (Account) i.next();
			assertEquals(acc.getName(), acc.getTotalBalance(), 0.0);
			assertEquals(acc.getName(), acc.getOpeningBalance(), 0.0);
			assertEquals(acc.getName(), acc.getCurrentBalance(), 0.0);
		}
		setDefaultAccountVariables(accounts, company.getId());
	}

	public void testCreateUser2() throws DAOException {

		company = accounterDao.getCompany(1L);

		User user1 = new User();
		user1.setFullName("ravi");
		user1.setEmail("ravi@vimukti.com");
		user1.setPasswordSha1Hash("***REMOVED***");
		user1.setCompany(company);

		User user2 = new User();
		user2.setFullName("raj");
		user2.setEmail("raj@vimukti.com");
		user2.setPasswordSha1Hash("***REMOVED***");
		user2.setCompany(company);

		User user3 = new User();
		user3.setFullName("pavani");
		user3.setEmail("pavani@vimukti.com");
		user3.setPasswordSha1Hash("***REMOVED***");
		user3.setCompany(company);

		User user4 = new User();
		user4.setFullName("fernandez");
		user4.setEmail("fernandez@vimukti.com");
		user4.setPasswordSha1Hash("***REMOVED***");
		user4.setCompany(company);

		User user5 = new User();
		user5.setFullName("kumar");
		user5.setEmail("kumar@vimukti.com");
		user5.setPasswordSha1Hash("***REMOVED***");
		user5.setCompany(company);

		User user6 = new User();
		user6.setFullName("rajesh");
		user6.setEmail("rajesh@vimukti.com");
		user6.setPasswordSha1Hash("***REMOVED***");
		user6.setCompany(company);

		try {
			accounter.createUser(user1);
			accounter.createUser(user2);
			accounter.createUser(user3);
			accounter.createUser(user4);
			accounter.createUser(user5);
			accounter.createUser(user6);
		} catch (DAOException e) {
			e.printStackTrace();
			fail("There should not be any exception");
		}
		assertNotNull(user2.getId());
	}

	public String today() {
		Calendar cl = Calendar.getInstance();
		String dt = ""
				+ cl.get(Calendar.YEAR)
				+ "-"
				+ ((cl.get(Calendar.MONTH) + 1) < 10 ? "0"
						+ (cl.get(Calendar.MONTH) + 1) : ""
						+ (cl.get(Calendar.MONTH) + 1))
				+ "-"
				+ (cl.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ (cl.get(Calendar.DAY_OF_MONTH)) : ""
						+ cl.get(Calendar.DAY_OF_MONTH)) + "";

		return dt;
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

	public void testCreateCashAccount() throws Exception {

		double amount = 0D;

		company = accounterDao.getCompany(1L);

		double totalBalance = accounterDao.getAccount(company.getId(),
				"Opening Balances").getTotalBalance();
		double currentBalance = accounterDao.getAccount(company.getId(),
				"Opening Balances").getCurrentBalance();

		Account cash = new Account();
		cash.setCompany(company);
		cash.setNumber("1101");
		cash.setName("Account Cash");
		cash.setType(Account.TYPE_CASH);
		cash.setIsActive(true);
		cash.setIncrease(false);
		cash.setOpeningBalance(amount);
		cash.setAsOf(format.parse(dueDate(-1)));
		if (cash.getParent() != null)
			cash.setHierarchy(Utility.getHierarchy(cash.getParent())
					+ cash.getName());
		else
			cash.setHierarchy(null);
		accounter.createAccount(cash);
		if (checkTesting) {
			assertNotNull("Account Cash", accounterDao.getAccount(
					company.getId(), "Account Cash").getId());
			assertEquals("Account Cash", accounterDao.getAccount(
					company.getId(), "Account Cash").getName());

			assertEquals("Account Cash", accounterDao.getAccount(
					company.getId(), "Account Cash").getTotalBalance(), amount);
			assertEquals("Account Cash", accounterDao.getAccount(
					company.getId(), "Account Cash").getOpeningBalance(),
					amount);
			assertEquals("Account Cash", accounterDao.getAccount(
					company.getId(), "Account Cash").getCurrentBalance(),
					amount);

			if (cash.isIncrease()) {
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances").getTotalBalance(),
						totalBalance - amount);
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances")
						.getCurrentBalance(), currentBalance - amount);
			} else {
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances").getTotalBalance(),
						totalBalance + amount);
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances")
						.getCurrentBalance(), currentBalance + amount);
			}
		}
	}

	public void testCreateBankAccount() throws DAOException, ParseException {

		double bankAmount = 0D;
		double OCAAmount = 0D;
		double OAAmount = 0D;
		double LLAmount = 0D;
		double OEAmount = 0D;
		double FAAmount = 0D;
		double OIAmount = 0D;
		double IAAmount = 0D;

		company = accounterDao.getCompany(1l);

		double totalBalance = accounterDao.getAccount(company.getId(),
				"Opening Balances").getTotalBalance();
		double currentBalance = accounterDao.getAccount(company.getId(),
				"Opening Balances").getCurrentBalance();

		Account bank = new Account();
		Account OCA = new Account();
		Account OA = new Account();
		Account LL = new Account();
		Account OE = new Account();
		Account FA = new Account();
		Account OI = new Account();
		Account IA = new Account();

		bank.setCompany(accounterDao.getCompany(1L));
		OCA.setCompany(accounterDao.getCompany(1L));
		OA.setCompany(accounterDao.getCompany(1L));
		LL.setCompany(accounterDao.getCompany(1L));
		OE.setCompany(accounterDao.getCompany(1L));
		FA.setCompany(accounterDao.getCompany(1L));
		OI.setCompany(accounterDao.getCompany(1L));
		IA.setCompany(accounterDao.getCompany(1L));

		bank.setNumber("1111");
		OCA.setNumber("2500");
		OA.setNumber("3700");
		LL.setNumber("5500");
		OE.setNumber("1500");
		FA.setNumber("4500");
		IA.setNumber("3500");
		OI.setNumber("500");

		bank.setName("Account Bank");
		OCA.setName("Account OCA");
		OA.setName("Account OA");
		LL.setName("Account LL");
		OE.setName("Account OE");
		FA.setName("Account FA");
		IA.setName("Account IA");
		OI.setName("Account OI");

		bank.setType(Account.TYPE_BANK);
		OCA.setType(Account.TYPE_OTHER_CURRENT_ASSET);
		OA.setType(Account.TYPE_OTHER_ASSET);
		LL.setType(Account.TYPE_LONG_TERM_LIABILITY);
		OE.setType(Account.TYPE_OTHER_EXPENSE);
		FA.setType(Account.TYPE_FIXED_ASSET);
		IA.setType(Account.TYPE_INVENTORY_ASSET);
		OI.setType(Account.TYPE_OTHER_INCOME);

		bank.setIsActive(true);
		OCA.setIsActive(true);
		OA.setIsActive(true);
		LL.setIsActive(true);
		OE.setIsActive(true);
		FA.setIsActive(true);
		IA.setIsActive(true);
		OI.setIsActive(true);

		OCA.setHierarchy((OCA.getParent() != null) ? Utility.getHierarchy(OCA
				.getParent())
				+ OCA.getName() : null);
		OA.setHierarchy((OA.getParent() != null) ? Utility.getHierarchy(OA
				.getParent())
				+ OA.getName() : null);
		LL.setHierarchy((LL.getParent() != null) ? Utility.getHierarchy(LL
				.getParent())
				+ LL.getName() : null);
		OE.setHierarchy((OE.getParent() != null) ? Utility.getHierarchy(OE
				.getParent())
				+ OE.getName() : null);
		FA.setHierarchy((FA.getParent() != null) ? Utility.getHierarchy(FA
				.getParent())
				+ FA.getName() : null);
		IA.setHierarchy((IA.getParent() != null) ? Utility.getHierarchy(IA
				.getParent())
				+ IA.getName() : null);
		OI.setHierarchy((OI.getParent() != null) ? Utility.getHierarchy(OI
				.getParent())
				+ OI.getName() : null);

		bank.setIncrease(false);
		OCA.setIncrease(false);
		OA.setIncrease(false);
		LL.setIncrease(true);
		OE.setIncrease(false);
		FA.setIncrease(false);
		IA.setIncrease(false);
		OI.setIncrease(true);

		bank.setOpeningBalance(bankAmount);
		OCA.setOpeningBalance(OCAAmount);
		OA.setOpeningBalance(OAAmount);
		LL.setOpeningBalance(LLAmount);
		OE.setOpeningBalance(OEAmount);
		FA.setOpeningBalance(FAAmount);
		IA.setOpeningBalance(OIAmount);
		OI.setOpeningBalance(IAAmount);

		bank.setAsOf(format.parse(dueDate(-1)));
		OCA.setAsOf(format.parse(dueDate(-1)));
		OA.setAsOf(format.parse(dueDate(-1)));
		LL.setAsOf(format.parse(dueDate(-1)));
		OE.setAsOf(format.parse(dueDate(-1)));
		FA.setAsOf(format.parse(dueDate(-1)));
		IA.setAsOf(format.parse(dueDate(-1)));
		OI.setAsOf(format.parse(dueDate(-1)));

		List<Account> accounts = new ArrayList<Account>();
		accounts.add(bank);
		accounts.add(OCA);
		accounts.add(OA);
		accounts.add(LL);
		accounts.add(OE);
		accounts.add(FA);
		accounts.add(IA);
		accounts.add(OI);

		accounter.createAccount(bank);
		accounter.createAccount(OCA);
		accounter.createAccount(OA);
		accounter.createAccount(LL);
		accounter.createAccount(OE);
		accounter.createAccount(FA);
		accounter.createAccount(IA);
		accounter.createAccount(OI);

		assertNotNull("Bank account created", accounterDao.getAccount(company
				.getId(), "Account Bank"));
		assertNotNull("OCA account created", accounterDao.getAccount(company
				.getId(), "Account OCA"));
		assertNotNull("OA account created", accounterDao.getAccount(company
				.getId(), "Account OA"));
		assertNotNull("LL account created", accounterDao.getAccount(company
				.getId(), "Account LL"));
		assertNotNull("OE account created", accounterDao.getAccount(company
				.getId(), "Account OE"));
		assertNotNull("FA account created", accounterDao.getAccount(company
				.getId(), "Account FA"));
		assertNotNull("ia account created", accounterDao.getAccount(company
				.getId(), "Account IA"));
		assertNotNull("OI account created", accounterDao.getAccount(company
				.getId(), "Account OI"));

		if (checkTesting) {
			Iterator<Account> i = accounts.iterator();
			double value = 0D;
			while (i.hasNext()) {
				Account a = i.next();

				if (a.isIncrease())
					value -= a.getOpeningBalance();
				else
					value += a.getOpeningBalance();

			}
			assertEquals("Opening Balances", accounterDao.getAccount(
					company.getId(), "Opening Balances").getTotalBalance(),
					totalBalance + value);
			assertEquals("Opening Balances", accounterDao.getAccount(
					company.getId(), "Opening Balances").getCurrentBalance(),
					currentBalance + value);
		}
	}

	public void testCreateCreditCardAccount() throws DAOException,
			ParseException {
		double amount = 0D;

		company = accounterDao.getCompany(1L);

		double totalBalance = accounterDao.getAccount(company.getId(),
				"Opening Balances").getTotalBalance();
		double currentBalance = accounterDao.getAccount(company.getId(),
				"Opening Balances").getCurrentBalance();

		Account CC = new Account();
		CC.setCompany(accounterDao.getCompany(1L));
		CC.setNumber("3333");
		CC.setName("Account Credit Card");
		CC.setType(Account.TYPE_CREDIT_CARD);
		CC.setIsActive(true);
		CC.setIncrease(true);
		CC.setOpeningBalance(amount);
		CC.setAsOf(format.parse(dueDate(-1)));
		accounter.createAccount(CC);
		if (checkTesting) {
			assertNotNull("Credit Card Account created", accounterDao
					.getAccount(company.getId(), "Account Credit Card"));

			assertEquals("Account Credit Card", accounterDao.getAccount(
					company.getId(), "Account Credit Card").getTotalBalance(),
					amount);
			assertEquals("Account Credit Card",
					accounterDao.getAccount(company.getId(),
							"Account Credit Card").getOpeningBalance(), amount);
			assertEquals("Account Credit Card",
					accounterDao.getAccount(company.getId(),
							"Account Credit Card").getCurrentBalance(), amount);

			if (CC.isIncrease()) {
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances").getTotalBalance(),
						totalBalance - amount);
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances")
						.getCurrentBalance(), currentBalance - amount);
			} else {
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances").getTotalBalance(),
						totalBalance + amount);
				assertEquals("Opening Balances", accounterDao.getAccount(
						company.getId(), "Opening Balances")
						.getCurrentBalance(), currentBalance + amount);
			}
		}
	}

	public void setAllDefaultAccountVariables(List<Account> accounts, long c)
			throws DAOException {
		company = accounterDao.getCompany(1L);
		t = new double[accounts.size()][3];

		for (int i = 0; i < accounts.size(); i++) {
			t[i][0] = accounts.get(i).getOpeningBalance();
			t[i][1] = accounts.get(i).getCurrentBalance();
			t[i][2] = accounts.get(i).getTotalBalance();
		}
	}

	public void updateParentBalance(Account acc, double amount,
			Company company, List<Account> accounts) throws DAOException {
		if (acc != null && acc.getParent() != null) {
			int v = getAccountVariable(accounts, acc.getParent().getName(),
					company);
			if (!acc.getParent().getName().equals(
					AccounterConstants.OPENING_BALANCE))
				t[v][2] += amount;
			else
				t[v][1] -= amount;
			updateParentBalance(acc.getParent(), amount, company, accounts);
		}
	}

	public boolean isSubAccountOfOpeningBalance(Account acc) {
		if (acc.getParent() != null) {
			if (acc.getParent().getId() == 1)
				return true;
			else
				return isSubAccountOfOpeningBalance(acc.getParent());
		}
		return false;
	}

	public void testCallCreateSubAccount(String num, String name, int type,
			String p, boolean a, boolean i, double bal, String date, String comp)
			throws Exception {
		Account acc = new Account();

		company = accounterDao.getCompany(1L);

		List<Account> accounts = accounterDao.getAccounts(company.getId());

		setAllDefaultAccountVariables(accounts, company.getId());

		acc.setNumber(num);
		acc.setName(name);
		acc.setType(type);
		acc.setParent(accounterDao.getAccount(company.getId(), p));
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

		if (checkTesting) {
			assertEquals(name, accounterDao.getAccount(company.getId(), name)
					.getTotalBalance(), bal);
			assertEquals(name, accounterDao.getAccount(company.getId(), name)
					.getOpeningBalance(), bal);
			assertEquals(name, accounterDao.getAccount(company.getId(), name)
					.getCurrentBalance(), bal);

			updateParentBalance(acc, bal, company, accounts);

			int v = getAccountVariable(accounts,
					AccounterConstants.OPENING_BALANCE, company);
			if (!isSubAccountOfOpeningBalance(acc)) {
				if (acc.isIncrease()) {
					t[v][1] -= bal;
					if (!isSubAccountOfOpeningBalance(acc))
						t[v][2] -= bal;

				} else {
					t[v][1] += bal;
					if (!isSubAccountOfOpeningBalance(acc))
						t[v][2] += bal;
				}
			}

			for (int j = 0; j < accounts.size(); j++) {
				System.out.println("j=" + j);
				String accName = accounts.get(j).getName();
				assertEquals(acc.getName() + ", " + accName, accounterDao
						.getAccount(company.getId(), accName)
						.getOpeningBalance(), t[getAccountVariable(accounts,
						accName, company)][0]);
				assertEquals(acc.getName() + ", " + accName, accounterDao
						.getAccount(company.getId(), accName)
						.getCurrentBalance(), t[getAccountVariable(accounts,
						accName, company)][1]);
				assertEquals(acc.getName() + ", " + accName,
						accounterDao.getAccount(company.getId(), accName)
								.getTotalBalance(), t[getAccountVariable(
								accounts, accName, company)][2]);
			}
		}
	}

	public void testCreateSubAccount() throws Exception {

		try {

			// * testCallCreateSubAccount( number in String, // * NAme in String
			// * Type in predefined int
			// * Parent in String
			// * Active in boolean
			// * Increase in boolean
			// * Open bal in double
			// * AsOf in String
			// * company in long

			company = accounterDao.getCompany(1l);

			if (createSubAccounts) {
				testCallCreateSubAccount("4201", "SubWO", Account.TYPE_INCOME,
						"Write off", true, true, 1000D, dueDate(-1), "Company1");
				testCallCreateSubAccount("6251", "SubBC", Account.TYPE_EXPENSE,
						"Bank Charge", true, false, 1000D, dueDate(-1),
						"Company1");
				testCallCreateSubAccount("3001", "SubOB", Account.TYPE_EQUITY,
						"Opening Balances", true, true, 1000D, dueDate(-1),
						"Company1");
				testCallCreateSubAccount("3101", "SubRE", Account.TYPE_EQUITY,
						"Retained Earnings", true, true, 1000D, dueDate(-1),
						"Company1");
				testCallCreateSubAccount("1701", "SubAC", Account.TYPE_CASH,
						"Account Cash", true, false, 1000D, dueDate(-1),
						"Company1");
				testCallCreateSubAccount("1001", "SubUDF", Account.TYPE_CASH,
						"Un Deposited Funds", true, false, 1000D, dueDate(-1),
						"Company1");
				testCallCreateSubAccount("2501", "SubOCA",
						Account.TYPE_OTHER_CURRENT_ASSET, "Account OCA", true,
						false, 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("4501", "SubFA",
				// Account.TYPE_FIXED_ASSET, "Account FA", true, false, 1000D,
				// dueDate(-1), "Company1");
				// testCallCreateSubAccount("3701", "SubOA",
				// Account.TYPE_OTHER_ASSET, "Account OA", true, false, 1000D,
				// dueDate(-1), "Company1");
				// testCallCreateSubAccount("2011", "SubPIR",
				// Account.TYPE_OTHER_CURRENT_LIABILITY,
				// "Pending Item Receipts", true, true, 1000D, dueDate(-1),
				// "Company1");
				// testCallCreateSubAccount("2051", "SubSTP",
				// Account.TYPE_OTHER_CURRENT_LIABILITY, "Sales Tax Payable",
				// true, true, 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("2111", "SubEPL",
				// Account.TYPE_PAYROLL_LIABILITY,
				// "Employee Payroll Liabilities", true, true, 1000D,
				// dueDate(-1), "Company1");
				// testCallCreateSubAccount("5501", "SubLL",
				// Account.TYPE_LONG_TERM_LIABILITY, "Account LL", true, true,
				// 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("5101", "SubCDT",
				// Account.TYPE_COST_OF_GOODS_SOLD, "Cash Discount taken", true,
				// false, 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("4101", "SubCDG",
				// Account.TYPE_INCOME, "Cash Discount Given", true, true,
				// 1000D, dueDate(-1), "Company1");
				// testCallCreateSubAccount("501", "SubOI",
				// Account.TYPE_OTHER_INCOME, "Account OI", true, true, 1000D,
				// dueDate(-1), "Company1");
				// testCallCreateSubAccount("1501", "SubOE",
				// Account.TYPE_OTHER_EXPENSE, "Account OE", true, false, 1000D,
				// dueDate(-1), "Company1");

				// // 2nd level
				if (createMore) {
					testCallCreateSubAccount("4202", "SubSubWO",
							Account.TYPE_INCOME, "SubWO", true, true, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("6252", "SubSubBC",
							Account.TYPE_EXPENSE, "SubBC", true, false, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("3002", "SubSubOB",
							Account.TYPE_EQUITY, "SubOB", true, true, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("3102", "SubSubRE",
							Account.TYPE_EQUITY, "SubRE", true, true, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("1702", "SubSubAC",
							Account.TYPE_CASH, "SubAC", true, false, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("1002", "SubSubUDF",
							Account.TYPE_CASH, "SubUDF", true, false, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("2502", "SubSubOCA",
							Account.TYPE_OTHER_CURRENT_ASSET, "SubOCA", true,
							false, 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("4502", "SubSubFA",
					// Account.TYPE_FIXED_ASSET, "SubFA", true, false, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("3702", "SubSubOA",
					// Account.TYPE_OTHER_ASSET, "SubOA", true, false, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("2012", "SubSubPIR",
					// Account.TYPE_OTHER_CURRENT_LIABILITY, "SubPIR", true,
					// true, 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("2052", "SubSubSTP",
					// Account.TYPE_OTHER_CURRENT_LIABILITY, "SubSTP", true,
					// true, 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("2112", "SubSubEPL",
					// Account.TYPE_PAYROLL_LIABILITY, "SubEPL", true, true,
					// 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("5502", "SubSubLL",
					// Account.TYPE_LONG_TERM_LIABILITY, "SubLL", true, true,
					// 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("5102", "SubSubCDT",
					// Account.TYPE_COST_OF_GOODS_SOLD, "SubCDT", true, false,
					// 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("4102", "SubSubCDG",
					// Account.TYPE_INCOME, "SubCDG", true, true, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("502", "SubSubOI",
					// Account.TYPE_OTHER_INCOME, "SubOI", true, true, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("1502", "SubSubOE",
					// Account.TYPE_OTHER_EXPENSE, "SubOE", true, false, 1000D,
					// dueDate(-1), "Company1");

					// 3rd level
					testCallCreateSubAccount("4203", "SubSubSubWO",
							Account.TYPE_INCOME, "SubSubWO", true, true, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("6253", "SubSubSubBC",
							Account.TYPE_EXPENSE, "SubSubBC", true, false,
							1000D, dueDate(-1), "Company1");
					testCallCreateSubAccount("3003", "SubSubSubOB",
							Account.TYPE_EQUITY, "SubSubOB", true, true, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("3103", "SubSubSubRE",
							Account.TYPE_EQUITY, "SubSubRE", true, true, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("1703", "SubSubSubAC",
							Account.TYPE_CASH, "SubSubAC", true, false, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("1003", "SubSubSubUDF",
							Account.TYPE_CASH, "SubSubUDF", true, false, 1000D,
							dueDate(-1), "Company1");
					testCallCreateSubAccount("2503", "SubSubSubOCA",
							Account.TYPE_OTHER_CURRENT_ASSET, "SubSubOCA",
							true, false, 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("4503", "SubSubSubFA",
					// Account.TYPE_FIXED_ASSET, "SubSubFA", true, false, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("3703", "SubSubSubOA",
					// Account.TYPE_OTHER_ASSET, "SubSubOA", true, false, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("2013", "SubSubSubPIR",
					// Account.TYPE_OTHER_CURRENT_LIABILITY, "SubSubPIR", true,
					// true, 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("2053", "SubSubSubSTP",
					// Account.TYPE_OTHER_CURRENT_LIABILITY, "SubSubSTP", true,
					// true, 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("2113", "SubSubSubEPL",
					// Account.TYPE_PAYROLL_LIABILITY, "SubSubEPL", true, true,
					// 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("5503", "SubSubSubLL",
					// Account.TYPE_LONG_TERM_LIABILITY, "SubSubLL", true, true,
					// 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("5103", "SubSubSubCDT",
					// Account.TYPE_COST_OF_GOODS_SOLD, "SubSubCDT", true,
					// false, 1000D, dueDate(-1), "Company1");
					// testCallCreateSubAccount("4103", "SubSubSubCDG",
					// Account.TYPE_INCOME, "SubSubCDG", true, true, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("503", "SubSubSubOI",
					// Account.TYPE_OTHER_INCOME, "SubSubOI", true, true, 1000D,
					// dueDate(-1), "Company1");
					// testCallCreateSubAccount("1503", "SubSubSubOE",
					// Account.TYPE_OTHER_EXPENSE, "SubSubOE", true, false,
					// 1000D, dueDate(-1), "Company1");
				}

			}
		} catch (DAOException E) {
			E.printStackTrace();
			fail("Sub Account Creation Failed!");
		}
	}

	public void testEditAccount() throws Exception {

		Company company = accounterDao.getCompany(1l);
		Account subWriteOff = accounterDao.getAccount(company.id, "SubWO");
		subWriteOff.setParent(accounterDao
				.getAccount(company.id, "Bank Charge"));
		accounter.alterAccount(subWriteOff);
	}

	public void testCreateCustomerGroup() throws DAOException {

		CustomerGroup customerGroup = new CustomerGroup();
		CustomerGroup customerGroup1 = new CustomerGroup();
		CustomerGroup customerGroup2 = new CustomerGroup();

		customerGroup.setName("CG1");
		customerGroup1.setName("CG2");
		customerGroup2.setName("CG3");

		try {

			company = accounterDao.getCompany(1L);
			customerGroup.setCompany(company);
			customerGroup1.setCompany(company);
			customerGroup2.setCompany(company);

			accounter.createCustomerGroup(customerGroup);
			accounter.createCustomerGroup(customerGroup1);
			accounter.createCustomerGroup(customerGroup2);

		} catch (DAOException e) {
			e.printStackTrace();
			fail("There should not be any exception");
		}
		if (checkTesting) {
			assertNotNull(customerGroup.getId());
			List<CustomerGroup> l = accounterDao.getCustomerGroups(company
					.getId());

			assertNotNull(l);
			Iterator<CustomerGroup> i = l.iterator();
			while (i.hasNext()) {
				CustomerGroup cg = (CustomerGroup) i.next();
				assertNotNull(cg.getName());
				assertNotSame("", cg.getName());
			}
		}
	}

	public void testCreateItemGroup() throws DAOException {

		company = accounterDao.getCompany(1L);
		ItemGroup itemGroup1 = new ItemGroup();
		ItemGroup itemGroup2 = new ItemGroup();

		itemGroup1.setName("IG1");
		itemGroup2.setName("IGS");

		try {

			Company company = accounterDao.getCompany(1L);

			itemGroup1.setCompany(company);
			itemGroup2.setCompany(company);

			accounter.createItemGroup(itemGroup1);
			accounter.createItemGroup(itemGroup2);

		} catch (DAOException e) {
			e.printStackTrace();
			fail("There should not be any exception");
		}
		if (checkTesting) {
			assertNotNull(itemGroup1.getId());
			assertNotNull(itemGroup2.getId());
			List<ItemGroup> l = accounterDao.getItemGroups(company.getId());

			assertNotNull(l);
			Iterator<ItemGroup> i = l.iterator();
			while (i.hasNext()) {
				ItemGroup cg = (ItemGroup) i.next();
				assertNotNull(cg.getName());
				assertNotSame("", cg.getName());
			}
		}
	}

	public Account getRandomLiabilityAccount(List<Account> accounts) {
		Account a = null;
		while (true) {
			a = accounts.get(new Random().nextInt(accounts.size()));
			if (a.getType() != Account.TYPE_ACCOUNT_PAYABLE
					&& a.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
					&& a.getType() != Account.TYPE_INVENTORY_ASSET
					&& a.getType() != Account.TYPE_EQUITY
					&& a.getType() != Account.TYPE_EXPENSE
					&& a.getType() != Account.TYPE_COST_OF_GOODS_SOLD
					&& a.getType() != Account.TYPE_INCOME)
				return a;
		}
	}

	public void testCreateTaxAgency() throws DAOException {

		TaxAgency taxAgency1 = new TaxAgency();
		taxAgency1.setName("TA1");
		TaxAgency taxAgency2 = new TaxAgency();
		taxAgency2.setName("TA2");
		TaxAgency taxAgency3 = new TaxAgency();
		taxAgency3.setName("TA3");
		try {

			company = accounterDao.getCompany(1L);

			taxAgency1.setCompany(company);
			taxAgency2.setCompany(company);
			taxAgency3.setCompany(company);

			taxAgency1.setLiabilityAccount(accounterDao.getAccount(company
					.getId(), AccounterConstants.SALES_TAX_PAYABLE));
			taxAgency2.setLiabilityAccount(accounterDao.getAccount(company
					.getId(), AccounterConstants.SALES_TAX_PAYABLE));
			taxAgency3.setLiabilityAccount(accounterDao.getAccount(company
					.getId(), AccounterConstants.SALES_TAX_PAYABLE));

			accounter.createTaxAgency(taxAgency1);
			accounter.createTaxAgency(taxAgency2);
			accounter.createTaxAgency(taxAgency3);

		} catch (DAOException e) {

			e.printStackTrace();
			fail("There should not be any exception");
		}
		if (checkTesting) {
			assertNotNull(taxAgency1.getId());
			assertNotNull(taxAgency2.getId());
			assertNotNull(taxAgency3.getId());

			List<TaxAgency> l = accounterDao.getTaxAgencies(company.getId());

			assertNotNull(l);
			Iterator<TaxAgency> i = l.iterator();

			while (i.hasNext()) {
				TaxAgency cg = (TaxAgency) i.next();
				assertNotNull(cg.getName());
				assertNotSame("", cg.getName());
			}
		}
	}

	public void testCreateTaxCode() throws DAOException {

		TaxCode taxCode1 = new TaxCode();
		taxCode1.setName("TC1");
		TaxCode taxCode2 = new TaxCode();
		taxCode2.setName("TC2");
		TaxCode taxCode3 = new TaxCode();
		taxCode3.setName("TC3");

		try {

			company = accounterDao.getCompany(1L);

			taxCode1.setCompany(company);
			taxCode2.setCompany(company);
			taxCode3.setCompany(company);

			Set<TaxRates> taxRates1 = new HashSet<TaxRates>();
			Set<TaxRates> taxRates2 = new HashSet<TaxRates>();
			Set<TaxRates> taxRates3 = new HashSet<TaxRates>();

			TaxRates taxRates1_1 = new TaxRates();
			taxRates1_1.setRate(10d);
			TaxRates taxRates2_1 = new TaxRates();
			taxRates2_1.setRate(10d);
			TaxRates taxRates3_1 = new TaxRates();
			taxRates3_1.setRate(10d);

			try {

				taxRates1_1.setAsOf(format.parse(dueDate(-1)));
				taxRates2_1.setAsOf(format.parse(dueDate(-1)));
				taxRates3_1.setAsOf(format.parse(dueDate(-1)));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			taxRates1.add(taxRates1_1);
			taxRates2.add(taxRates2_1);
			taxRates3.add(taxRates3_1);

			taxCode1.setTaxRates(taxRates1);
			taxCode2.setTaxRates(taxRates2);
			taxCode3.setTaxRates(taxRates3);

			taxCode1.setTaxAgency(accounterDao.getTaxAgency(company.getId(),
					"TA1"));
			taxCode2.setTaxAgency(accounterDao.getTaxAgency(company.getId(),
					"TA2"));
			taxCode3.setTaxAgency(accounterDao.getTaxAgency(company.getId(),
					"TA3"));

			accounter.createTaxCode(taxCode1);
			accounter.createTaxCode(taxCode2);
			accounter.createTaxCode(taxCode3);

		} catch (DAOException e) {
			e.printStackTrace();
			fail("There should not be any exception");
		}
		if (checkTesting) {
			assertNotNull(taxCode1.getId());
			assertNotNull(taxCode2.getId());
			assertNotNull(taxCode3.getId());

			List<TaxCode> l = accounterDao.getTaxCodes(company.getId());

			assertNotNull(l);
			Iterator<TaxCode> i = l.iterator();

			while (i.hasNext()) {
				TaxCode cg = (TaxCode) i.next();
				assertNotNull(cg.getName());
				assertNotSame("", cg.getName());
			}
		}
	}

	public void testCreateTaxGroup() throws DAOException {

		TaxGroup taxGroup1 = new TaxGroup();
		TaxGroup taxGroup2 = new TaxGroup();
		TaxGroup taxGroup3 = new TaxGroup();
		TaxGroup taxGroup4 = new TaxGroup();

		taxGroup1.setName("TG12");
		taxGroup2.setName("TG23");
		taxGroup3.setName("TG31");
		taxGroup4.setName("TG123");
		company = accounterDao.getCompany(1L);

		try {

			taxGroup1.setCompany(company);
			taxGroup2.setCompany(company);
			taxGroup3.setCompany(company);
			taxGroup4.setCompany(company);

			Set<TaxCode> taxCodes1 = new HashSet<TaxCode>();
			taxCodes1.add(accounterDao.getTaxCode(company.getId(), "TC1"));
			taxCodes1.add(accounterDao.getTaxCode(company.getId(), "TC2"));

			Set<TaxCode> taxCodes2 = new HashSet<TaxCode>();
			taxCodes2.add(accounterDao.getTaxCode(company.getId(), "TC2"));
			taxCodes2.add(accounterDao.getTaxCode(company.getId(), "TC3"));

			Set<TaxCode> taxCodes3 = new HashSet<TaxCode>();
			taxCodes3.add(accounterDao.getTaxCode(company.getId(), "TC3"));
			taxCodes3.add(accounterDao.getTaxCode(company.getId(), "TC1"));

			Set<TaxCode> taxCodes4 = new HashSet<TaxCode>();
			taxCodes4.add(accounterDao.getTaxCode(company.getId(), "TC1"));
			taxCodes4.add(accounterDao.getTaxCode(company.getId(), "TC2"));
			taxCodes4.add(accounterDao.getTaxCode(company.getId(), "TC3"));

			taxGroup1.setTaxCodes(taxCodes1);
			taxGroup2.setTaxCodes(taxCodes2);
			taxGroup3.setTaxCodes(taxCodes3);
			taxGroup4.setTaxCodes(taxCodes4);

			accounter.createTaxGroup(taxGroup1);
			accounter.createTaxGroup(taxGroup2);
			accounter.createTaxGroup(taxGroup3);
			accounter.createTaxGroup(taxGroup4);

		} catch (DAOException e) {
			e.printStackTrace();
			fail("There should not be any exception");
		}
		if (checkTesting) {
			assertNotNull(taxGroup1.getId());
			assertNotNull(taxGroup2.getId());
			assertNotNull(taxGroup3.getId());
			assertNotNull(taxGroup4.getId());

			List<TaxGroup> l = accounterDao.getTaxGroups(company.getId());
			assertNotNull(l);
			Iterator<TaxGroup> i = l.iterator();

			while (i.hasNext()) {
				TaxGroup cg = (TaxGroup) i.next();
				assertNotNull(cg.getName());
				assertNotSame("", cg.getName());
			}
		}
	}

	public void testCreateSalesPerson() throws DAOException {

		SalesPerson salesPerson = new SalesPerson();
		salesPerson.setFirstName("SalesPerson1");
		company = accounterDao.getCompany(1L);
		salesPerson.setCompany(company);
		accounter.createSalesPerson(salesPerson);
		if (checkTesting) {
			assertNotNull(salesPerson.getId());
			assertNotNull(salesPerson.getFirstName(), accounterDao
					.getSalesPerson(company.getId(), "SalesPerson1"));
		}
	}

	public Address testCallCreateAddress(String street, String city,
			String state, String country) {
		Address address1 = new Address();
		address1.setCity(city);
		address1.setCountryOrRegion(country);
		address1.setStateOrProvinence(state);
		address1.setStreet(street);
		return address1;
	}

	public Email testCallCreateEmail(String email) {
		Email e = new Email();
		e.setEmail(email);
		return e;
	}

	public Contact testCallCreateContact(String title, String email) {
		Contact c = new Contact();
		c.setTitle(title);
		c.setEmail(email);
		return c;
	}

	public Customer testCallCreateCustomer(String name, String FAs, String cg,
			Set<Email> em, Set<Contact> c, Set<Address> a, String since,
			String asof, double bal, Company company) throws DAOException {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setFileAs(FAs);
		customer.setCustomerGroup(accounterDao.getCustomerGroup(
				company.getId(), cg));
		customer.setEmails(em);
		customer.setAddress(a);
		customer.setContacts(c);
		try {
			customer.setCustomerGroup(accounterDao.getCustomerGroup(company
					.getId(), 1L));
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
			double OBCurrentBalance = accounterDao.getAccount(company.getId(),
					AccounterConstants.OPENING_BALANCE).getCurrentBalance(), OBTotalBalance = accounterDao
					.getAccount(company.getId(),
							AccounterConstants.OPENING_BALANCE)
					.getTotalBalance();

			ArrayList<Customer> custs = new ArrayList<Customer>();
			try {

				amt1 = accounterDao.getAccount(company.getId(),
						"Accounts Receivable").getOpeningBalance();

				// testCallCreateAddress( String str, // String city, // String
				// state, // String cnty)
				// returns Address object

				// for customer1
				Set<Address> address1 = new HashSet<Address>();
				address1.add(testCallCreateAddress("street1-1", "city1-1",
						"state1-1", "country1-1"));
				address1.add(testCallCreateAddress("street1-2", "city1-2",
						"state1-2", "country1-2"));
				// for customer2
				Set<Address> address2 = new HashSet<Address>();
				address1.add(testCallCreateAddress("street2-1", "city2-1",
						"state2-1", "country2-1"));
				address1.add(testCallCreateAddress("street2-2", "city2-2",
						"state2-2", "country2-2"));
				// for customer3
				Set<Address> address3 = new HashSet<Address>();
				address1.add(testCallCreateAddress("street3-1", "city3-1",
						"state3-1", "country3-1"));
				address1.add(testCallCreateAddress("street3-2", "city3-2",
						"state3-2", "country3-2"));
				// for customer4
				Set<Address> address4 = new HashSet<Address>();
				address4.add(testCallCreateAddress("street4-1", "city4-1",
						"state4-1", "country4-1"));
				address4.add(testCallCreateAddress("street4-2", "city4-2",
						"state4-2", "country4-2"));
				// for customer5
				Set<Address> address5 = new HashSet<Address>();
				address5.add(testCallCreateAddress("street5-1", "city5-1",
						"state5-1", "country5-1"));
				address5.add(testCallCreateAddress("street5-2", "city5-2",
						"state5-2", "country5-2"));

				// * testCallCreateEmail(String email
				// * returns Email object

				// for customer1
				Set<Email> emails1 = new HashSet<Email>();
				emails1.add(testCallCreateEmail("customer1@email1.com"));
				emails1.add(testCallCreateEmail("customer1@email2.com"));
				// for customer2
				Set<Email> emails2 = new HashSet<Email>();
				emails2.add(testCallCreateEmail("customer2@email1.com"));
				emails2.add(testCallCreateEmail("customer2@email2.com"));
				// for customer3
				Set<Email> emails3 = new HashSet<Email>();
				emails3.add(testCallCreateEmail("customer3@email1.com"));
				emails3.add(testCallCreateEmail("customer3@email2.com"));
				// for customer4
				Set<Email> emails4 = new HashSet<Email>();
				emails4.add(testCallCreateEmail("customer4@email1.com"));
				emails4.add(testCallCreateEmail("customer4@email2.com"));
				// for customer5
				Set<Email> emails5 = new HashSet<Email>();
				emails5.add(testCallCreateEmail("customer5@email1.com"));
				emails5.add(testCallCreateEmail("customer5@email2.com"));

				// testCallCreateContacts( String title
				// String email
				// ) returns Contact object

				// for customer1
				Set<Contact> contacts1 = new HashSet<Contact>();
				contacts1.add(testCallCreateContact("cust1-title1",
						"cust1@contact1.com"));
				contacts1.add(testCallCreateContact("cust1-title2",
						"cust1@contact2.com"));
				// for customer2
				Set<Contact> contacts2 = new HashSet<Contact>();
				contacts2.add(testCallCreateContact("cust2-title1",
						"cust2@contact1.com"));
				contacts2.add(testCallCreateContact("cust2-title2",
						"cust2@contact2.com"));
				// for customer3
				Set<Contact> contacts3 = new HashSet<Contact>();
				contacts3.add(testCallCreateContact("cust3-title1",
						"cust3@contact1.com"));
				contacts3.add(testCallCreateContact("cust3-title2",
						"cust3@contact2.com"));
				// for customer4
				Set<Contact> contacts4 = new HashSet<Contact>();
				contacts4.add(testCallCreateContact("cust4-title1",
						"cust4@contact1.com"));
				contacts4.add(testCallCreateContact("cust4-title2",
						"cust4@contact2.com"));
				// for customer5
				Set<Contact> contacts5 = new HashSet<Contact>();
				contacts5.add(testCallCreateContact("cust5-title1",
						"cust5@contact1.com"));
				contacts5.add(testCallCreateContact("cust5-title2",
						"cust5@contact2.com"));

				// testCallCreateCustomer( String name
				// String file as
				// GroupName in String
				// Set<email> email
				// Set<contacts> contact
				// Set<Address> address
				// cust since date in string
				// bal asof date in string
				// balance in double
				// company in company) return a Customer Object

				custs.add(testCallCreateCustomer("Customer1",
						"Customer1 Fileas", "CG1", emails1, contacts1,
						address1, dueDate(-1), dueDate(-1), 10000.0, company));
				// custs.add(testCallCreateCustomer("Customer1",
				// "Customer1 Fileas", // // "CG1", emails1, contacts1,
				// address1, dueDate(-1), dueDate(-1), // // 10000.0, company));
				custs.add(testCallCreateCustomer("Customer2",
						"Customer2 Fileas", "CG2", emails2, contacts2,
						address2, dueDate(-1), dueDate(-1), 20000.0, company));
				custs.add(testCallCreateCustomer("Customer3",
						"Customer3 Fileas", "CG3", emails3, contacts3,
						address3, dueDate(-1), dueDate(-1), 30000.0, company));
				custs.add(testCallCreateCustomer("Customer4",
						"Customer4 Fileas", "CG1", emails4, contacts4,
						address4, dueDate(-1), dueDate(-1), 40000.0, company));
				custs.add(testCallCreateCustomer("Customer5",
						"Customer5 Fileas", "CG2", emails5, contacts5,
						address5, dueDate(-1), dueDate(-1), 50000.0, company));

			} catch (DAOException e) {
				e.printStackTrace();
				fail("Customer failed!");
			}

			if (checkTesting) {
				// //Testing starts from here
				amt2 = 0.0;

				List<Customer> l = accounterDao.getCustomers(company.getId());
				ArrayList<Customer> l2 = custs;
				assertNotNull(l);
				assertNotNull(l2);
				Iterator i = l.iterator();
				Iterator j = l2.iterator();

				while (i.hasNext() && j.hasNext()) {
					Customer cg = (Customer) i.next();// This returns the
					// customer from
					// starting of the customer list
					Customer c1 = (Customer) j.next();
					// check every field in the customer object whether it is
					// saved
					// correctly in the database
					// check name
					assertNotNull(cg.getName());
					assertNotNull(c1.getName());
					assertEquals(c1.getName(), cg.getName());
					// check File As
					assertNotNull(cg.getFileAs());
					assertNotNull(c1.getFileAs());
					assertEquals(c1.getFileAs(), cg.getFileAs());
					// check Addresses
					// As address field consists of several entries, we need to
					// take an
					// iterator to iterate aLL the values
					assertNotNull(cg.getAddress());
					assertNotNull(c1.getAddress());

					Iterator i2 = cg.getAddress().iterator();
					Iterator j2 = c1.getAddress().iterator();

					while (i2.hasNext())// &&j2.hasNext())
					{
						Address a = (Address) i2.next();
						Address b = a;
						while (j2.hasNext()) {
							b = (Address) j2.next();
							assertNotNull(b);
							assertNotNull(a.getCity());
							assertNotNull(b.getCity());
							if (a.getCity() == b.getCity()) {
								// check the city
								assertEquals(a.getCity(), b.getCity());
								// check street
								assertNotNull(a.getStreet());
								assertNotNull(b.getStreet());
								assertEquals(a.getStreet(), b.getStreet());
								// check country
								assertNotNull(a.getCountryOrRegion());
								assertNotNull(b.getCountryOrRegion());
								assertEquals(a.getCountryOrRegion(), b
										.getCountryOrRegion());
								// check state
								assertNotNull(a.getStateOrProvinence());
								assertNotNull(b.getStateOrProvinence());
								assertEquals(a.getStateOrProvinence(), b
										.getStateOrProvinence());
								break;
							}
						}
						if (!i2.hasNext())
							assertEquals(a.getCity(), b.getCity());
					}

					assertNotNull(cg.getEmails());
					assertNotNull(c1.getEmails());

					Iterator i3 = cg.getEmails().iterator();
					Iterator j3 = cg.getEmails().iterator();

					while (i3.hasNext())// &&j3.hasNext())
					{
						// checking Emails
						Email a = (Email) i3.next();
						Email b = a;

						while (j3.hasNext()) {
							b = (Email) j3.next();
							assertNotNull(b);
							assertNotNull(a.getEmail());
							assertNotNull(b.getEmail());
							// check the email one by one
							if (a.getEmail() == b.getEmail()) {
								assertEquals(a.getEmail(), b.getEmail());
								break;
							}
						}
						if (!i3.hasNext())
							assertEquals(a.getEmail(), b.getEmail());
					}

					assertEquals(cg.getPayeeSince(), c1.getPayeeSince());
					assertEquals(cg.getBalanceAsOf(), c1.getBalanceAsOf());
					assertEquals(cg.getBalance(), c1.getBalance());
					assertEquals(cg.getOpeningBalance(), c1.getBalance());
					assertEquals(cg.getBalance(), c1.getOpeningBalance());
					assertEquals(cg.getOpeningBalance(), c1.getOpeningBalance());

					amt2 += cg.getBalance();

					assertNotNull(cg.getContacts());
					assertNotNull(c1.getContacts());

					Iterator i4 = cg.getContacts().iterator();
					Iterator j4 = cg.getContacts().iterator();

					while (i4.hasNext() && j4.hasNext()) {
						Contact a = (Contact) i4.next();
						Contact b = a;
						// checking Contacts
						while (j4.hasNext()) {
							b = (Contact) j4.next();
							assertNotNull(b);
							assertNotNull(a.getTitle());
							assertNotNull(b.getTitle());
							// checking contacts one by one
							if (a.getTitle() == b.getTitle()) {
								// check the title in the contacts
								assertEquals(a.getTitle(), b.getTitle());
								// checking email in the contacts
								assertNotNull(a.getEmail());
								assertNotNull(b.getEmail());

								assertEquals(a.getEmail(), b.getEmail());
								break;
							}
						}
						if (!i4.hasNext())
							assertEquals(a.getEmail(), b.getEmail());
					}
					// Now checking the AccountsReceivable accounts
				}
				assertNotNull(amt1);
				assertNotNull(amt2);

				Account acc = accounterDao.getAccount(company.getId(),
						"Accounts Receivable");
				assertEquals(AccounterConstants.ACCOUNTS_RECEIVABLE,
						(amt1 + amt2), acc.getCurrentBalance());
				assertEquals(AccounterConstants.ACCOUNTS_RECEIVABLE,
						(amt1 + amt2), acc.getTotalBalance());

				acc = accounterDao.getAccount(company.getId(),
						AccounterConstants.OPENING_BALANCE);
				assertEquals(AccounterConstants.OPENING_BALANCE,
						(OBCurrentBalance + amt1 + amt2), acc
								.getCurrentBalance());
				assertEquals(AccounterConstants.OPENING_BALANCE,
						(OBTotalBalance + amt1 + amt2), acc.getTotalBalance());
			}
		}
	}

	public void testCreateVendorGroup() throws DAOException {

		VendorGroup vendorGroup1 = new VendorGroup();
		VendorGroup vendorGroup2 = new VendorGroup();

		vendorGroup1.setName("VG1");
		vendorGroup2.setName("VG2");
		List<VendorGroup> VGS = new ArrayList<VendorGroup>();
		try {

			company = accounterDao.getCompany(1L);

			vendorGroup1.setCompany(company);
			vendorGroup2.setCompany(company);

			accounter.createVendorGroup(vendorGroup1);
			accounter.createVendorGroup(vendorGroup2);

			VGS.add(vendorGroup1);
			VGS.add(vendorGroup2);

		} catch (DAOException e) {
			e.printStackTrace();
			fail("There should not be any exception");
		}

		if (checkTesting) {
			assertNotNull(vendorGroup1.getId());
			assertNotNull(vendorGroup2.getId());

			List<VendorGroup> l = accounterDao.getVendorGroups(company.getId());
			List<VendorGroup> l2 = VGS;
			Iterator<VendorGroup> i = l.iterator();
			Iterator<VendorGroup> j = l2.iterator();

			while (i.hasNext()) {
				// checking with the names of the vendor groups
				VendorGroup a = (VendorGroup) i.next();
				VendorGroup b = a;

				while (j.hasNext()) {
					b = (VendorGroup) j.next();
					assertNotNull(b);
					assertNotNull(a.getName());
					assertNotNull(b.getName());
					// check the names one by one
					if (a.getName() == b.getName()) {
						assertEquals(a.getName(), b.getName());
						break;
					}
				}
				if (!i.hasNext())
					assertEquals(a.getName(), b.getName());
			}
		}
	}

	public Vendor testCallCreateVendor(String name, String FileAs,
			Set<Email> em, Set<Contact> c, Set<Address> a, String since,
			String asof, double bal, Company company) throws DAOException {
		Vendor v = new Vendor();
		v.setName(name);
		v.setFileAs(FileAs);
		v.setEmails(em);
		v.setAddress(a);
		v.setContacts(c);

		try {
			v.setPayeeSince(format.parse(since));
			v.setBalanceAsOf(format.parse(asof));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		v.setOpeningBalance(bal);
		v.setCompany(company);
		accounter.createVendor(v);
		return v;
	}

	public void testCreateVendor() throws DAOException {

		if (createVendors) {
			company = accounterDao.getCompany(1L);
			double amt1 = 0.0, amt2 = 0.0;
			double OBCurrentBalance = accounterDao.getAccount(company.getId(),
					AccounterConstants.OPENING_BALANCE).getCurrentBalance(), OBTotalBalance = accounterDao
					.getAccount(company.getId(),
							AccounterConstants.OPENING_BALANCE)
					.getTotalBalance();
			ArrayList<Vendor> vends = new ArrayList<Vendor>();

			try {

				// testCallCreateAddress( String str, // String city, // String
				// state, // String cnty)
				// returns Address object

				// for vendor1
				Set<Address> address1 = new HashSet<Address>();
				address1.add(testCallCreateAddress("vstreet1-1", "vcity1-1",
						"vstate1-1", "vcountry1-1"));
				address1.add(testCallCreateAddress("vstreet1-2", "vcity1-2",
						"vstate1-2", "vcountry1-2"));
				// for vendor2
				Set<Address> address2 = new HashSet<Address>();
				address2.add(testCallCreateAddress("vstreet2-1", "vcity2-1",
						"vstate2-1", "vcountry2-1"));
				address2.add(testCallCreateAddress("vstreet2-2", "vcity2-2",
						"vstate2-2", "vcountry2-2"));
				// for vendor3
				Set<Address> address3 = new HashSet<Address>();
				address3.add(testCallCreateAddress("vstreet3-1", "vcity3-1",
						"vstate3-1", "vcountry3-1"));
				address3.add(testCallCreateAddress("vstreet3-2", "vcity3-2",
						"vstate3-2", "vcountry3-2"));
				// for vendor4
				Set<Address> address4 = new HashSet<Address>();
				address4.add(testCallCreateAddress("vstreet4-1", "vcity4-1",
						"vstate4-1", "vcountry4-1"));
				address4.add(testCallCreateAddress("vstreet4-2", "vcity4-2",
						"vstate4-2", "vcountry4-2"));
				// for vendor5
				Set<Address> address5 = new HashSet<Address>();
				address5.add(testCallCreateAddress("vstreet5-1", "vcity5-1",
						"vstate5-1", "vcountry5-1"));
				address5.add(testCallCreateAddress("vstreet5-2", "vcity5-2",
						"vstate5-2", "vcountry5-2"));

				// testCallCreateEmails(
				// String email
				// ) returns Contact object

				// for vendor1
				Set<Email> emails1 = new HashSet<Email>();
				emails1.add(testCallCreateEmail("vendor1@email1.com"));
				emails1.add(testCallCreateEmail("vendor1@email2.com"));
				// for vendor2
				Set<Email> emails2 = new HashSet<Email>();
				emails2.add(testCallCreateEmail("vendor2@email1.com"));
				emails2.add(testCallCreateEmail("vendor2@email2.com"));
				// for vendor3
				Set<Email> emails3 = new HashSet<Email>();
				emails3.add(testCallCreateEmail("vendor3@email1.com"));
				emails3.add(testCallCreateEmail("vendor3@email2.com"));
				// for vendor4
				Set<Email> emails4 = new HashSet<Email>();
				emails4.add(testCallCreateEmail("vendor4@email1.com"));
				emails4.add(testCallCreateEmail("vendor4@email2.com"));
				// for vendor5
				Set<Email> emails5 = new HashSet<Email>();
				emails5.add(testCallCreateEmail("vendor5@email1.com"));
				emails5.add(testCallCreateEmail("vendor5@email2.com"));

				// testCallCreateContacts( String title
				// String email
				// ) returns Contact object

				// for vendor1
				Set<Contact> contacts1 = new HashSet<Contact>();
				contacts1.add(testCallCreateContact("contact1",
						"vend1@contact1.com"));
				contacts1.add(testCallCreateContact("contact2",
						"vend1@contact2.com"));
				// for vendor2
				Set<Contact> contacts2 = new HashSet<Contact>();
				contacts2.add(testCallCreateContact("contact1",
						"vend2@contact1.com"));
				contacts2.add(testCallCreateContact("contact2",
						"vend2@contact2.com"));
				// for vendor3
				Set<Contact> contacts3 = new HashSet<Contact>();
				contacts3.add(testCallCreateContact("contact1",
						"vend3@contact1.com"));
				contacts3.add(testCallCreateContact("contact2",
						"vend3@contact2.com"));
				// for vendor4
				Set<Contact> contacts4 = new HashSet<Contact>();
				contacts4.add(testCallCreateContact("contact1",
						"vend4@contact1.com"));
				contacts4.add(testCallCreateContact("contact2",
						"vend4@contact2.com"));
				// for vendor5
				Set<Contact> contacts5 = new HashSet<Contact>();
				contacts5.add(testCallCreateContact("contact1",
						"vend5@contact1.com"));
				contacts5.add(testCallCreateContact("contact2",
						"vend5@contact2.com"));

				// testCallCreateVendor( String name
				// String file as
				// List<email> email
				// List<contacts> contact
				// List<Address> address
				// cust since date in string
				// bal asof date in string
				// balance in double
				// company in company

				vends.add(testCallCreateVendor("Vendor1", "Vendor1 File As",
						emails1, contacts1, address1, dueDate(-1), dueDate(-1),
						-10000.0, company));
				vends.add(testCallCreateVendor("Vendor2", "Vendor2 File As",
						emails2, contacts2, address2, dueDate(-1), dueDate(-1),
						20000.0, company));
				vends.add(testCallCreateVendor("Vendor3", "Vendor3 File As",
						emails3, contacts3, address3, dueDate(-1), dueDate(-1),
						30000.0, company));
				vends.add(testCallCreateVendor("Vendor4", "Vendor4 File As",
						emails4, contacts4, address4, dueDate(-1), dueDate(-1),
						40000.0, company));
				vends.add(testCallCreateVendor("Vendor5", "Vendor5 File As",
						emails5, contacts5, address5, dueDate(-1), dueDate(-1),
						50000.0, company));

			} catch (DAOException e) {
				e.printStackTrace();
				fail("Vendor failed!");
			}

			if (checkTesting) {
				amt2 = 0.0;

				List<Vendor> l = accounterDao.getVendors(company.getId());
				ArrayList<Vendor> l2 = vends;

				assertNotNull(l);
				assertNotNull(l2);
				Iterator<Vendor> i = l.iterator();
				Iterator<Vendor> j = l2.iterator();

				while (i.hasNext() && j.hasNext()) {
					Vendor cg = (Vendor) i.next();// This returns the vendor
					// from
					// starting of the customer list
					Vendor c1 = (Vendor) j.next();
					// check every field in the vendor object whether it is
					// saved
					// correctly in the database
					// check name
					assertNotNull(cg.getName());
					assertNotNull(c1.getName());
					assertEquals(c1.getName(), cg.getName());
					// check File As
					assertNotNull(cg.getFileAs());
					assertNotNull(c1.getFileAs());
					assertEquals(c1.getFileAs(), cg.getFileAs());
					// check Addresses
					// As address field consists of several entries, we need to
					// take an
					// iterator to iterate aLL the values
					assertNotNull(cg.getAddress());
					assertNotNull(c1.getAddress());

					Iterator<Address> i2 = cg.getAddress().iterator();
					Iterator<Address> j2 = c1.getAddress().iterator();

					while (i2.hasNext())// &&j2.hasNext())
					{
						Address a = (Address) i2.next();
						Address b = a;
						while (j2.hasNext()) {
							b = (Address) j2.next();
							assertNotNull(b);
							assertNotNull(a.getCity());
							assertNotNull(b.getCity());
							if (a.getCity() == b.getCity()) {
								// check the city
								assertEquals(a.getCity(), b.getCity());
								// check street
								assertNotNull(a.getStreet());
								assertNotNull(b.getStreet());
								assertEquals(a.getStreet(), b.getStreet());
								// check country
								assertNotNull(a.getCountryOrRegion());
								assertNotNull(b.getCountryOrRegion());
								assertEquals(a.getCountryOrRegion(), b
										.getCountryOrRegion());
								// check state
								assertNotNull(a.getStateOrProvinence());
								assertNotNull(b.getStateOrProvinence());
								assertEquals(a.getStateOrProvinence(), b
										.getStateOrProvinence());
								break;
							}
						}
						if (!i2.hasNext())
							assertEquals(a.getCity(), b.getCity());
					}

					assertNotNull(cg.getEmails());
					assertNotNull(c1.getEmails());

					Iterator<Email> i3 = cg.getEmails().iterator();
					Iterator<Email> j3 = cg.getEmails().iterator();

					while (i3.hasNext())// &&j3.hasNext())
					{
						// checking Emails
						Email a = (Email) i3.next();
						Email b = a;

						while (j3.hasNext()) {
							b = (Email) j3.next();
							assertNotNull(b);
							assertNotNull(a.getEmail());
							assertNotNull(b.getEmail());
							// check the email one by one
							if (a.getEmail() == b.getEmail()) {
								assertEquals(a.getEmail(), b.getEmail());
								break;
							}
						}
						if (!i3.hasNext())
							assertEquals(a.getEmail(), b.getEmail());
					}

					assertEquals(cg.getPayeeSince(), c1.getPayeeSince());
					assertEquals(cg.getBalanceAsOf(), c1.getBalanceAsOf());
					assertEquals(cg.getBalance(), c1.getBalance());
					assertEquals(cg.getOpeningBalance(), c1.getBalance());
					assertEquals(cg.getBalance(), c1.getOpeningBalance());
					assertEquals(cg.getOpeningBalance(), c1.getOpeningBalance());

					amt2 += cg.getBalance();

					assertNotNull(cg.getContacts());
					assertNotNull(c1.getContacts());

					Iterator<Contact> i4 = cg.getContacts().iterator();
					Iterator<Contact> j4 = cg.getContacts().iterator();

					while (i4.hasNext() && j4.hasNext()) {
						Contact a = (Contact) i4.next();
						Contact b = a;
						// checking Contacts
						while (j4.hasNext()) {
							b = (Contact) j4.next();
							assertNotNull(b);
							assertNotNull(a.getTitle());
							assertNotNull(b.getTitle());
							// checking contacts one by one
							if (a.getTitle() == b.getTitle()) {
								// check the title in the contacts
								assertEquals(a.getTitle(), b.getTitle());
								// checking email in the contacts
								assertNotNull(a.getEmail());
								assertNotNull(b.getEmail());

								assertEquals(a.getEmail(), b.getEmail());
								break;
							}
						}
						if (!i4.hasNext())
							assertEquals(a.getEmail(), b.getEmail());
					}
					// Now checking the AccountsReceivable accounts
				}
				assertNotNull(amt1);
				assertNotNull(amt2);

				Account acc = accounterDao.getAccount(company.getId(),
						"Accounts Payable");
				assertEquals(AccounterConstants.ACCOUNTS_PAYABLE,
						(amt1 + amt2), acc.getCurrentBalance());
				assertEquals(AccounterConstants.ACCOUNTS_PAYABLE,
						(amt1 + amt2), acc.getTotalBalance());

				acc = accounterDao.getAccount(company.getId(),
						AccounterConstants.OPENING_BALANCE);
				assertEquals(AccounterConstants.OPENING_BALANCE,
						(OBCurrentBalance - amt1 - amt2), acc
								.getCurrentBalance());
				assertEquals(AccounterConstants.OPENING_BALANCE,
						(OBTotalBalance - amt1 - amt2), acc.getTotalBalance());

			}
		}
	}

	public Item testCallCreateItem(Company company, boolean active, int type,
			String name, boolean it, boolean seLL, String ia, double sp,
			double sc, long ig, boolean buy, double pp, String ea)
			throws DAOException {

		Item i = new Item();
		i.setCompany(company);
		i.setActive(active);
		i.setType(type);
		i.setName(name);
		i.isTaxable = it;
		i.setISellThisItem(seLL);
		i.setIncomeAccount(accounterDao.getAccount(company.getId(), ia));
		i.setSalesPrice(sp);
		i.setStandardCost(sc);
		i.setItemGroup(accounterDao.getItemGroup(company.getId(), ig));
		i.setIBuyThisItem(buy);
		i.setPurchasePrice(pp);
		i.setExpenseAccount(accounterDao.getAccount(company.getId(), ea));
		accounter.createItem(i);
		return i;
	}

	public int getAccountVariable(List<Account> accounts, String s,
			Company company) throws DAOException {
		// For an Account name we will return the corresponding index in the
		// account array.
		for (int i = 0; i < accounts.size(); i++)
			if (s.equals(accounts.get(i).getName()))
				return i;
		System.out
				.println("In GetAccountVariable function , s================="
						+ s + "/");
		return -1;
	}

	public int getTaxAgencyVariable(List<TaxCode> codes, String s,
			Company company) throws DAOException {
		// For an Account name we will return the corresponding index in the
		// account array.
		for (int i = 0; i < codes.size(); i++)
			if (s.equals(codes.get(i).getTaxAgency().getName()))
				return i;
		System.out
				.println("In GetAccountVariable function , s================="
						+ s + "/");
		return -1;
	}

	public double setAccountBal(String s, Company company) throws DAOException {
		company = accounterDao.getCompany(1L);
		return accounterDao.getAccount(company.getId(), s).getTotalBalance();
	}

	public void setDefaultAccountVariables(List<Account> accounts, long c)
			throws DAOException {
		company = accounterDao.getCompany(1L);
		d = new double[accounts.size()];

		for (int i = 0; i < accounts.size(); i++)
			d[i] = accounts.get(i).getTotalBalance();
	}

	public double getAmountAmount(List<TransactionPayBill> tpl) {
		double d = 0.0;
		Iterator<TransactionPayBill> i = tpl.iterator();
		while (i.hasNext()) {
			TransactionPayBill tp = (TransactionPayBill) i.next();
			d += tp.getAmountDue();
		}

		return d;
	}

	public double getAmountPayment(List<TransactionPayBill> tpl) {
		double d = 0.0;
		Iterator<TransactionPayBill> i = tpl.iterator();
		while (i.hasNext()) {
			TransactionPayBill tp = (TransactionPayBill) i.next();
			d += tp.getPayment();
		}

		return d;
	}

	public double getAmountTotalDiscount(List<TransactionPayBill> tpl) {
		double d = 0.0;
		Iterator<TransactionPayBill> i = tpl.iterator();
		while (i.hasNext()) {
			TransactionPayBill tp = (TransactionPayBill) i.next();
			d += tp.getCashDiscount();
		}

		return d;
	}

	public double getAmountSalesTax(String tg, List<TransactionItem> ti)
			throws DAOException {
		company = accounterDao.getCompany(1L);
		TaxGroup tg1 = accounterDao.getTaxGroup(company.getId(), tg);
		double rate = 0.0, rate2 = 0.0;
		Iterator<TransactionItem> j = ti.iterator();
		if ((tg1 != null) && (tg1.getTaxCodes() != null) && (j.hasNext())) {
			Set<TaxCode> taxcodes = tg1.getTaxCodes();
			Iterator<TaxCode> i2 = taxcodes.iterator();
			while (i2.hasNext()) {
				TaxCode tc = (TaxCode) i2.next();

				if (tc.getTaxRates() != null) {

					Set<TaxRates> taxrates = tc.getTaxRates();
					Iterator<TaxRates> i3 = taxrates.iterator();
					Date date;
					if (i3.hasNext()) {
						TaxRates tr = (TaxRates) i3.next();
						date = (Date) tr.getAsOf();
						rate = tr.getRate();
						while (i3.hasNext()) {
							if (date.after(tr.getAsOf())) {
								date = tr.getAsOf();
								rate = tr.getRate();
							}
							tr = (TaxRates) i3.next();
						}

					}
					rate2 += rate;
				}
			}
		}

		rate = 0.0;
		while (j.hasNext()) {
			TransactionItem tr = (TransactionItem) j.next();
			if (tr.getType() != TransactionItem.TYPE_SALESTAX && tr.isTaxable())
				rate += (rate2 / 100.0) * tr.getLineTotal();
		}

		return rate;
	}

	public double getTaxRate(TaxCode tc) {
		double rate = 0.0;

		if (tc.getTaxRates() != null) {
			Set<TaxRates> taxrates = tc.getTaxRates();
			Iterator<TaxRates> i3 = taxrates.iterator();

			Date date;
			if (i3.hasNext()) {
				TaxRates tr = (TaxRates) i3.next();
				date = (Date) tr.getAsOf();
				rate = tr.getRate();
				while (i3.hasNext()) {
					// The date which is latest but not after the today's date
					// is to be taken
					// and its taxrate must be used in the calculations.
					if (date.after(tr.getAsOf())) {
						date = tr.getAsOf();
						rate = tr.getRate();
					}
					tr = (TaxRates) i3.next();
				}
			}
			rate = (rate / 100.0);
		}

		return rate;
	}

	public double getAmountAllTaxableLineTotal(List<TransactionItem> ti)
			throws DAOException {
		double rate = 0.0;
		Iterator<TransactionItem> j = ti.iterator();

		while (j.hasNext()) {
			TransactionItem tr = (TransactionItem) j.next();
			if (tr.getType() != TransactionItem.TYPE_SALESTAX
					&& (tr.isTaxable()))
				rate += tr.getLineTotal();
		}

		return rate;
	}

	public double getAmountDiscountTotal(List<TransactionItem> t)
			throws Exception {
		double discount = 0.0;
		Iterator<TransactionItem> i = t.iterator();

		while (i.hasNext()) {
			TransactionItem ti = (TransactionItem) i.next();
			discount += ti.getDiscount();
		}

		return discount;
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

	public void testCallSetValuesTransactionItem(TransactionItem item1,
			String compny, int type, String item, double quantity,
			double unitprice) throws DAOException {

		company = accounterDao.getCompany(1L);
		item1.setType(type);
		if (type == TransactionItem.TYPE_ITEM)
			item1.setItem(accounterDao.getItem(company.getId(), item));
		else if (type == TransactionItem.TYPE_ACCOUNT)
			item1.setAccount(accounterDao.getAccount(company.getId(), item));
		else if (type == TransactionItem.TYPE_SALESTAX)
			item1.setTaxCode(accounterDao.getTaxCode(company.getId(), item));
		item1.setQuantity(quantity);
		item1.setUnitPrice(unitprice);
		item1.setLineTotal(item1.getQuantity() * item1.getUnitPrice());
	}

	public CashSales testCallCreateCashSales(CashSales cs, String cu, long cm,
			String date, String depIn, String tg, List<TransactionItem> ti)
			throws Exception {

		company = accounterDao.getCompany(1L);
		TaxGroup t = accounterDao.getTaxGroup(1l, tg);
		Customer customer = accounterDao.getCustomer(cm, cu);
		cs.setCustomer(customer);
		cs.setCompany(company);
		cs.setType(Transaction.TYPE_CASH_SALES);
		cs.setNumber(accounterGUIDao.getNextTransactionNumber(company.getId(),
				Transaction.TYPE_CASH_SALES));
		cs.setDate(format.parse(date));
		cs.setDepositIn(accounterDao.getAccount(cm, depIn));

		cs.setTransactionItems(ti);

		for (int i = 0; i < ti.size(); i++) {
			if (ti.get(i).isTaxable)
				ti.get(i).taxGroup = t;
		}
		cs.setSalesTax((tg != null) ? getAmountSalesTax(tg, ti) : 0.0);
		cs.setAllLineTotal(getAmountAllLineTotal(ti));
		cs.setTotal(cs.getAllLineTotal() + cs.getSalesTax());
		cs.setAllTaxableLineTotal(getAmountAllTaxableLineTotal(ti));
		double customerBalance = customer.getBalance();
		accounter.createCashSales(cs);
		if (checkTesting)
			assertEquals("Customer Balance", customerBalance, accounterDao
					.getCustomer(company.getId(), cs.getCustomer().getId())
					.getBalance());
		return cs;
	}

	public TransactionItem testCallCreateTransactionItem(int type, double q,
			double up, double d, boolean it, String i, CashSales cs)
			throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setQuantity(q);
		item.setUnitPrice(up);
		item.setDiscount(d);
		item.setLineTotal(q * up);
		item.isTaxable = it;
		// if(item.itemTax.isTaxable)
		// item.taxGroup=accounterDao.getTaxGroup(1l);
		item.setItem(accounterDao.getItem(1L, i));
		item.setTransaction(cs);
		return item;
	}

	public TransactionItem testCallCreateTransactionItem(int type, double lt,
			String tc, CashSales cs) throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setLineTotal(lt);
		item.setTaxCode(accounterDao.getTaxCode(1L, tc));
		item.setTransaction(cs);
		return item;
	}

	public TransactionItem testCallCreateTransactionItem(int type, double q,
			double up, boolean it, String ac, CashSales c) throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setQuantity(q);
		item.setUnitPrice(up);
		item.setLineTotal(q * up);
		item.isTaxable = it;
		item.setAccount(accounterDao.getAccount(1L, ac));
		item.setTransaction(c);
		return item;
	}

	public boolean getAccountIsTaxAgencyAccount(Account a) throws DAOException {

		List<TaxCode> tcs = accounterDao.getTaxCodes(company.getId());

		Iterator<TaxCode> itr = tcs.iterator();
		while (itr.hasNext()) {
			TaxCode tc = (TaxCode) itr.next();
			if (a.getName().equals(
					tc.getTaxAgency().getLiabilityAccount().getName()))
				return true;
		}
		return false;
	}

	public boolean getAccountIsCashAccount(Account i) throws DAOException {
		if (i.getType() == Account.TYPE_CASH
				|| i.getType() == Account.TYPE_BANK
				|| i.getType() == Account.TYPE_INVENTORY_ASSET
				|| i.getType() == Account.TYPE_ACCOUNT_PAYABLE
				|| i.getType() == Account.TYPE_ACCOUNT_RECEIVABLE)
			return true;

		return false;
	}

	public Item nextItem(ListIterator<Item> its) throws DAOException {
		Item i = null;
		while (its != null) {
			if (its.hasNext())
				i = its.next();
			else {
				while (its.hasPrevious())
					i = its.previous();
			}
			if (!getAccountIsTaxAgencyAccount(i.getIncomeAccount()))
				return i;
		}
		return i;
	}

	public Item nextRandomItem(ListIterator<Item> its) throws DAOException {
		Item i = null;
		while (its != null) {
			if (its.hasNext())
				i = its.next();
			else {
				while (its.hasPrevious())
					i = its.previous();
			}
			if (!getAccountIsTaxAgencyAccount(i.getIncomeAccount()))
				return i;
		}
		return i;
	}

	public Account nextAccount(ListIterator<Account> its) throws DAOException {
		Account i = null;
		while (true) {
			if (its.hasNext())
				i = its.next();
			else {
				while (its.hasPrevious())
					i = its.previous();
			}
			if ((!getAccountIsTaxAgencyAccount(i))
					&& (!getAccountIsCashAccount(i)))
				return i;
		}

	}

	public Account nextMakeDepositAccount(ListIterator<Account> its)
			throws DAOException {
		Account i = null;
		while (true) {
			if (its.hasNext())
				i = its.next();
			else {
				while (its.hasPrevious())
					i = its.previous();
			}
			if ((!getAccountIsTaxAgencyAccount(i))
					&& (i.getType() != Account.TYPE_ACCOUNT_PAYABLE
							&& i.getType() != Account.TYPE_ACCOUNT_RECEIVABLE && i
							.getType() != Account.TYPE_INVENTORY_ASSET))
				return i;
		}

	}

	public Account nextAccount(List<Account> acs) throws DAOException {
		ListIterator<Account> its = acs.listIterator();
		Account i = null;
		while (its != null) {
			if (its.hasNext())
				i = its.next();
			else {
				while (its.hasPrevious())
					i = its.previous();
			}
			if (!getAccountIsTaxAgencyAccount(i)
					&& i.getType() != Account.TYPE_ACCOUNT_PAYABLE
					&& i.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
					&& i.getType() != Account.TYPE_INVENTORY_ASSET)
				return i;
		}
		return i;
	}

	public Account getRandomMakeDepositAccount(List<Account> acs)
			throws DAOException {

		Account a = null;
		while (true) {
			a = acs.get(new Random().nextInt(acs.size()));
			if ((!getAccountIsTaxAgencyAccount(a))
					&& a.getType() != Account.TYPE_ACCOUNT_PAYABLE
					&& a.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
					&& a.getType() != Account.TYPE_INVENTORY_ASSET)
				return a;
		}
	}

	public Account getRandomAccount(List<Account> acs) throws DAOException {
		Account a = null;
		while (true) {
			a = acs.get(new Random().nextInt(acs.size()));
			if ((!getAccountIsTaxAgencyAccount(a))
					&& (!getAccountIsCashAccount(a)))
				return a;
		}
	}

	public Account getRandomPayFromAccount(List<Account> acs)
			throws DAOException {
		Account a = null;
		while (true) {
			a = acs.get(new Random().nextInt(acs.size()));
			if ((!getAccountIsTaxAgencyAccount(a))
					&& a.getType() != Account.TYPE_ACCOUNT_PAYABLE
					&& a.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
					&& a.getType() != Account.TYPE_INVENTORY_ASSET)
				return a;
		}
	}

	public Account getRandomCashAccount(List<Account> acs) throws DAOException {
		Account a = null;
		while (true) {
			a = acs.get(new Random().nextInt(acs.size()));
			if (!getAccountIsTaxAgencyAccount(a)
					&& a.getType() != Account.TYPE_ACCOUNT_PAYABLE
					&& a.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
					&& a.getType() != Account.TYPE_INVENTORY_ASSET)
				return a;
		}
	}

	public TaxCode nextTaxCode(ListIterator<TaxCode> its) throws DAOException {
		TaxCode i = null;
		while (its != null) {
			if (its.hasNext())
				i = its.next();
			else {
				while (its.hasPrevious())
					i = its.previous();
			}
			return i;
		}
		return i;
	}

	public void testCreateItems() throws DAOException {

		company = accounterDao.getCompany(1L);
		ArrayList<Item> items = new ArrayList<Item>();

		// testCallCreateItem( company in Company, // isactive in boolean, // //
		// type in predefined int, // name in String, // Itemtax in Long, // //
		// setSeLL in boolean, // Income acc in long
		// seLL price in double
		// stndrd cost in double
		// Item group in long
		// setBuy in boolean
		// purchase in double
		// expense acc in long

		if (createItems) {
			items.add(testCallCreateItem(company, true,
					Item.TYPE_NON_INVENTORY_PART, "UDF 2000", true, true,
					"Un Deposited Funds", 2000d, 2000d, 1l, true, 2000d,
					"Un Deposited Funds"));
			items.add(testCallCreateItem(company, true,
					Item.TYPE_NON_INVENTORY_PART, "OCA 2000", true, true,
					"Account OCA", 2000.0, 2000.0, 1l, true, 2000.0,
					"Account OCA"));
			items.add(testCallCreateItem(company, true,
					Item.TYPE_NON_INVENTORY_PART, "OA 2000", true, true,
					"Account OA", 2000.0, 2000.0, 1l, true, 2000.0,
					"Account OA"));
			items.add(testCallCreateItem(company, true,
					Item.TYPE_NON_INVENTORY_PART, "ACC 2000", true, true,
					"Account Credit Card", 2000.0, 2000.0, 1l, true, 2000.0,
					"Account Credit Card"));
			items.add(testCallCreateItem(company, true,
					Item.TYPE_NON_INVENTORY_PART, "OE 2000", true, true,
					"Account OE", 2000.0, 2000.0, 1l, true, 2000.0,
					"Account OE"));
			items.add(testCallCreateItem(company, true,
					Item.TYPE_NON_INVENTORY_PART, "AC 2000", true, true,
					"Account Cash", 2000.0, 2000.0, 1l, true, 2000.0,
					"Account Cash"));
			items.add(testCallCreateItem(company, true,
					Item.TYPE_NON_INVENTORY_PART, "LL 2000", true, true,
					"Account LL", 2000.0, 2000.0, 1l, true, 2000.0,
					"Account LL"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"EPL 2000", true, true, "Employee Payroll Liabilities",
					2000.0, 2000.0, 2l, true, 2000.0,
					"Employee Payroll Liabilities"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"OB 2000", true, true, "Opening Balances", 2000.0, 2000.0,
					2l, true, 2000.0, "Opening Balances"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"RE 2000", true, true, "Retained Earnings", 2000.0, 2000.0,
					2l, true, 2000.0, "Retained Earnings"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"CDG 2000", true, true, "Cash Discount Given", 2000.0,
					2000.0, 2l, true, 2000.0, "Cash Discount Given"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"WO 2000", true, true, "Write off", 2000.0, 2000.0, 2l,
					true, 2000.0, "Write off"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"CDT 2000", true, true, "Cash Discount taken", 2000.0,
					2000.0, 2l, true, 2000.0, "Cash Discount taken"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"BC 2000", true, true, "Bank Charge", 2000.0, 2000.0, 2l,
					true, 2000.0, "Bank Charge"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"OI 2000", true, true, "Account OI", 2000.0, 2000.0, 2l,
					true, 2000.0, "Account OI"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"STP 2000", true, true, "Sales Tax Payable", 2000.0,
					2000.0, 2l, true, 2000.0, "Sales Tax Payable"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"PIR 2000", true, true, "Pending Item Receipts", 2000.0,
					2000.0, 2l, true, 2000.0, "Pending Item Receipts"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"AB 2000", true, true, "Account Bank", 2000.0, 2000.0, 2l,
					true, 2000.0, "Account Bank"));
			items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
					"FA 2000", true, true, "Account FA", 2000.0, 2000.0, 2l,
					true, 2000.0, "Account FA"));

			if (createSubAccounts) {
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubUDF 2000", true,
						true, "SubUDF", 2000.0, 2000.0, 1l, true, 2000.0,
						"SubUDF"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubEPL 2000", true,
						true, "SubEPL", 2000.0, 2000.0, 1l, true, 2000.0,
						"SubEPL"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubOB 2000", true, true,
						"SubOB", 2000.0, 2000.0, 1l, true, 2000.0, "SubOB"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubRE 2000", true, true,
						"SubRE", 2000.0, 2000.0, 1l, true, 2000.0, "SubRE"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubCDG 2000", true,
						true, "SubCDG", 2000.0, 2000.0, 1l, true, 2000.0,
						"SubCDG"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubBC 2000", true, true,
						"SubBC", 2000.0, 2000.0, 1l, true, 2000.0, "SubBC"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubCDT 2000", true,
						true, "SubCDT", 2000.0, 2000.0, 1l, true, 2000.0,
						"SubCDT"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubOI 2000", true, true,
						"SubOI", 2000.0, 2000.0, 1l, true, 2000.0, "SubOI"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubOA 2000", true, true,
						"SubOA", 2000.0, 2000.0, 1l, true, 2000.0, "SubOA"));
				items.add(testCallCreateItem(company, true,
						Item.TYPE_NON_INVENTORY_PART, "SubOCA 2000", true,
						true, "SubOCA", 2000.0, 2000.0, 1l, true, 2000.0,
						"SubOCA"));
				items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
						"SubSTP 2000", true, true, "SubSTP", 2000.0, 2000.0,
						2l, true, 2000.0, "SubSTP"));
				items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
						"SubOE 2000", true, true, "SubOE", 2000.0, 2000.0, 2l,
						true, 2000.0, "SubOE"));
				items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
						"SubFA 2000", true, true, "SubFA", 2000.0, 2000.0, 2l,
						true, 2000.0, "SubFA"));
				items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
						"SubAC 2000", true, true, "SubAC", 2000.0, 2000.0, 2l,
						true, 2000.0, "SubAC"));
				items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
						"SubPIR 2000", true, true, "SubPIR", 2000.0, 2000.0,
						2l, true, 2000.0, "SubPIR"));
				items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
						"SubLL 2000", true, true, "SubLL", 2000.0, 2000.0, 2l,
						true, 2000.0, "SubLL"));
				items.add(testCallCreateItem(company, true, Item.TYPE_SERVICE,
						"SubWO 2000", true, true, "SubWO", 2000.0, 2000.0, 2l,
						true, 2000.0, "SubWO"));

				if (createMore) {
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubUDF 2000",
							true, true, "SubSubUDF", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubUDF"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubEPL 2000",
							true, true, "SubSubEPL", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubEPL"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubOB 2000",
							true, true, "SubSubOB", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubOB"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubRE 2000",
							true, true, "SubSubRE", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubRE"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubCDG 2000",
							true, true, "SubSubCDG", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubCDG"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubBC 2000",
							true, true, "SubSubBC", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubBC"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubCDT 2000",
							true, true, "SubSubCDT", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubCDT"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubOI 2000",
							true, true, "SubSubOI", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubOI"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubOA 2000",
							true, true, "SubSubOA", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubOA"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubOCA 2000",
							true, true, "SubSubOCA", 2000.0, 2000.0, 1l, true,
							2000.0, "SubSubOCA"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubPIR 2000", true, true,
							"SubSubPIR", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubPIR"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSTP 2000", true, true,
							"SubSubSTP", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSTP"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubOE 2000", true, true,
							"SubSubOE", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubOE"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubFA 2000", true, true,
							"SubSubFA", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubFA"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubAC 2000", true, true,
							"SubSubAC", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubAC"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubLL 2000", true, true,
							"SubSubLL", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubLL"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubWO 2000", true, true,
							"SubSubWO", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubWO"));

					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubUDF 2000",
							true, true, "SubSubSubUDF", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubUDF"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubEPL 2000",
							true, true, "SubSubSubEPL", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubEPL"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubOB 2000",
							true, true, "SubSubSubOB", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubOB"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubRE 2000",
							true, true, "SubSubSubRE", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubRE"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubCDG 2000",
							true, true, "SubSubSubCDG", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubCDG"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubBC 2000",
							true, true, "SubSubSubBC", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubBC"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubCDT 2000",
							true, true, "SubSubSubCDT", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubCDT"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubOI 2000",
							true, true, "SubSubSubOI", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubOI"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubOA 2000",
							true, true, "SubSubSubOA", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubOA"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_NON_INVENTORY_PART, "SubSubSubOCA 2000",
							true, true, "SubSubSubOCA", 2000.0, 2000.0, 1l,
							true, 2000.0, "SubSubSubOCA"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSubPIR 2000", true, true,
							"SubSubSubPIR", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSubPIR"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSubSTP 2000", true, true,
							"SubSubSubSTP", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSubSTP"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSubOE 2000", true, true,
							"SubSubSubOE", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSubOE"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSubFA 2000", true, true,
							"SubSubSubFA", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSubFA"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSubAC 2000", true, true,
							"SubSubSubAC", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSubAC"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSubLL 2000", true, true,
							"SubSubSubLL", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSubLL"));
					items.add(testCallCreateItem(company, true,
							Item.TYPE_SERVICE, "SubSubSubWO 2000", true, true,
							"SubSubSubWO", 2000.0, 2000.0, 2l, true, 2000.0,
							"SubSubSubWO"));
				}
			}
		}

		if (checkTesting) {
			List<Item> items2 = accounterDao.getItems(company.getId());
			Iterator<Item> i = items.iterator();
			Iterator<Item> j = items2.iterator();

			while (i.hasNext() && j.hasNext()) {
				Item a = (Item) i.next();
				Item b = (Item) j.next();
				assertNotNull(a.getName());
				assertNotNull(b.getName());
				assertEquals(a.getName(), b.getName());
				assertNotNull(a.getType());
				assertNotNull(b.getType());
				assertEquals(a.getType(), b.getType());
				assertEquals(a.getCompany().getName(), b.getCompany().getName());
				assertEquals(a.isTaxable, b.isTaxable);
				assertEquals(a.getIncomeAccount().getName(), b
						.getIncomeAccount().getName());
				assertNotNull(a.getSalesPrice());
				assertNotNull(b.getSalesPrice());
				assertEquals(a.getSalesPrice(), b.getSalesPrice());
				assertNotNull(a.getStandardCost());
				assertNotNull(b.getStandardCost());
				assertEquals(a.getStandardCost(), b.getStandardCost());
				assertNotNull(a.getPurchasePrice());
				assertNotNull(b.getPurchasePrice());
				assertEquals(a.getPurchasePrice(), b.getPurchasePrice());
				assertNotNull(a.getExpenseAccount());
				assertNotNull(a.getExpenseAccount().getName());
				assertNotNull(b.getExpenseAccount());
				assertNotNull(b.getExpenseAccount().getName());
				assertEquals(a.getExpenseAccount().getName(), b
						.getExpenseAccount().getName());
				assertEquals(a.getItemGroup().getName(), b.getItemGroup()
						.getName());
				assertEquals(a.isActive(), b.isActive());
				assertEquals(a.isIBuyThisItem(), b.isIBuyThisItem());
				assertEquals(a.isISellThisItem(), b.isISellThisItem());
			}
		}
	}

	public void testCreateCashSales() throws Exception {

		if (createCashSales) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			List<TaxCode> codes = accounterDao.getTaxCodes(company.getId());
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getName().equals("None"))
					codes.remove(i);
			}
			ListIterator<TaxCode> tcs = codes.listIterator();

			taxAgencies = new double[codes.size()];
			for (int i = 0; i < taxAgencies.length; i++)
				taxAgencies[getTaxAgencyVariable(codes, codes.get(i)
						.getTaxAgency().getName(), company)] = codes.get(i)
						.getTaxAgency().getBalance();

			setDefaultAccountVariables(accounts, company.getId());

			CashSales cashSales1 = new CashSales();
			CashSales cashSales2 = new CashSales();
			CashSales cashSales3 = new CashSales();
			CashSales cashSales4 = new CashSales();
			CashSales cashSales5 = new CashSales();
			CashSales cashSales6 = new CashSales();
			CashSales cashSales7 = new CashSales();
			CashSales cashSales8 = new CashSales();
			CashSales cashSales9 = new CashSales();
			CashSales cashSales10 = new CashSales();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

			// testCallTransactionItem( Type in int, for item type entry
			// Quantity in Double, // UnitPrice in double, // Discount in
			// Double, // //LineTotal in Double, // testCreateCashSales itemtax
			// in String, // item in String, // CashSales ) returns
			// TransactionItems object
			//
			// testCallTransactionItem( Type in int, for sales tax account type
			// entry
			// LineTotal in Double, // taxcode in String, // CashSales ) returns
			// TransactionItems object

			// testCallTransactionItem( Type in int, for tax account type entry
			// Quantity in double
			// Unit price in double
			// //LineTotal in Double, // itemTax in String, // Account in String
			// CashSales ) returns TransactionItems object

			// Random rt = new Random();

			// for(int n=0;n<(accounts.size());n++)
			// {
			// Account account=nextAccount(acs);
			// //
			//transactionItems1.add(testCallCreateTransactionItem(TransactionItem
			// .TYPE_ACCOUNT, 1D, 1000D, true, //
			// account.getName(),cashSales1));

			//			 
			//transactionItems1.add(testCallCreateTransactionItem(TransactionItem
			// .TYPE_ACCOUNT,
			// 1D, 1000D, true, "Write off", cashSales1));
			//			
			//transactionItems2.add(testCallCreateTransactionItem(TransactionItem
			// .TYPE_ACCOUNT,
			// 1D, 1000D, true, "Opening Balances", cashSales2));
			//			
			//transactionItems3.add(testCallCreateTransactionItem(TransactionItem
			// .TYPE_ACCOUNT,
			// 1D, 1000D, true, "Bank Charge",cashSales3));

			//transactionItems1.add(testCallCreateTransactionItem(TransactionItem
			// .TYPE_SALESTAX, 1000D, "TC1", cashSales1));
			//transactionItems1.add(testCallCreateTransactionItem(TransactionItem
			// .TYPE_ITEM, 1D, 2000D, 0D, true, "UDF 2000", cashSales1));

			if (cashSales1 != null) {
				for (int n = 0; n < (items.size() / 2); n++) {
					Item item = nextItem(its);
					transactionItems1.add(testCallCreateTransactionItem(
							TransactionItem.TYPE_ITEM, 1D,
							item.getSalesPrice(), 0D, true, item.getName(),
							cashSales1));
				}
			}

			if (cashSales2 != null) {
				for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
					Item item = nextItem(its);
					transactionItems2.add(testCallCreateTransactionItem(
							TransactionItem.TYPE_ITEM, 1D,
							item.getSalesPrice(), 0D, true, item.getName(),
							cashSales2));
				}
			}

			if (cashSales3 != null) {
				for (int n = 0; n < (accounts.size() / 2); n++) {
					Account account = nextAccount(acs);
					transactionItems3.add(testCallCreateTransactionItem(
							TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true,
							account.getName(), cashSales3));
				}
			}

			if (cashSales4 != null) {
				for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++) {
					Account account = nextAccount(acs);
					transactionItems4.add(testCallCreateTransactionItem(
							TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true,
							account.getName(), cashSales4));
				}
			}

			if (cashSales5 != null) {
				for (int n = 0; n < (codes.size()); n++) {
					TaxCode tc = nextTaxCode(tcs);
					transactionItems5.add(testCallCreateTransactionItem(
							TransactionItem.TYPE_SALESTAX, 1000D, tc.getName(),
							cashSales5));
				}
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size() + codes
						.size()) / 4); n += 3) {
					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						if (cashSales6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), cashSales6));

						item = items.get(new Random().nextInt(items.size()));
						if (cashSales7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), cashSales7));

						item = items.get(new Random().nextInt(items.size()));
						if (cashSales8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), cashSales8));

						item = items.get(new Random().nextInt(items.size()));
						if (cashSales9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), cashSales9));

						item = items.get(new Random().nextInt(items.size()));
						if (cashSales10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), cashSales10));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						if (cashSales6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											cashSales6));

						account = getRandomAccount(accounts);
						if (cashSales7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											cashSales7));

						account = getRandomAccount(accounts);
						if (cashSales8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											cashSales8));

						account = getRandomAccount(accounts);
						if (cashSales9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											cashSales9));

						account = getRandomAccount(accounts);
						if (cashSales10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											cashSales10));
					}

				}
			}

			// testCallCreateCashSales( CashSales, // Customer in String, //
			// Company in long , // Date in string, // DepositIn Account in
			// long, // TaxGroup in Long, // TransactionItems in set, // )
			// returns cashsales object

			ArrayList<CashSales> cashsales = new ArrayList<CashSales>();
			List<Account> cashAccounts = Utility.getDepositInAccounts(company);// getRandomCashAccount
			// (
			// cashAccounts
			// )
			// .
			// getName
			// (
			// )

			if (transactionItems1.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales1, "Customer1",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG12", transactionItems1));

			if (transactionItems2.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales2, "Customer2",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG23", transactionItems2));

			if (transactionItems3.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales3, "Customer3",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG31", transactionItems3));

			if (transactionItems4.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales4, "Customer4",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG123", transactionItems4));

			if (transactionItems5.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales5, "Customer5",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG12", transactionItems5));

			if (transactionItems6.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales6, "Customer1",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG23", transactionItems6));

			if (transactionItems7.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales7, "Customer2",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG31", transactionItems7));

			if (transactionItems8.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales8, "Customer3",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG123", transactionItems8));

			if (transactionItems9.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales9, "Customer4",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG12", transactionItems9));

			if (transactionItems10.size() > 0)
				cashsales.add(testCallCreateCashSales(cashSales10, "Customer5",
						1L, today(), getRandomCashAccount(cashAccounts)
								.getName(), "TG23", transactionItems10));

			if (checkTesting) {
				// From here testing starts
				// All the CashSales are stored in a ArrayList, and we will
				// iterate
				// them.
				// For every CashSale, we perform necessary operations on the
				// accounts array and
				// atlast we check them with the actual values that are stored
				// in
				// the database.

				Iterator<CashSales> i = cashsales.iterator();

				CashSales c;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				while (i.hasNext()) {
					c = (CashSales) i.next();
					assertNotNull(c);
					assertNotNull(c.getCustomer());
					assertNotNull(c.getCustomer().getName());
					assertEquals(c.getCustomer().getName(), accounterDao
							.getCashSales(company.getId(), c.getId())
							.getCustomer().getName());
					assertEquals(c.getDate(), accounterDao.getCashSales(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getDepositIn());
					assertNotNull(c.getDepositIn().getName());
					assertEquals(c.getDepositIn().getName(), accounterDao
							.getCashSales(company.getId(), c.getId())
							.getDepositIn().getName());
					assertEquals(c.getDiscountTotal(), accounterDao
							.getCashSales(company.getId(), c.getId())
							.getDiscountTotal());
					assertEquals(c.getType(), accounterDao.getCashSales(
							company.getId(), c.getId()).getType());
					assertEquals(c.getTotal(), accounterDao.getCashSales(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getSalesTax(), accounterDao.getCashSales(
							company.getId(), c.getId()).getSalesTax());
					if (c.getSalesPerson() != null)
						if (c.getSalesPerson().getFirstName() != null)
							assertEquals(c.getSalesPerson().getFirstName(),
									accounterDao.getCashSales(company.getId(),
											c.getId()).getSalesPerson()
											.getFirstName());
					if (c.getPaymentMethod() != null)
						if (c.getPaymentMethod() != null)
							assertEquals(c.getPaymentMethod(), accounterDao
									.getCashSales(company.getId(), c.getId())
									.getPaymentMethod());
					assertEquals(c.getNumber(), accounterDao.getCashSales(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getIncomeAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else if (it.getType() == TransactionItem.TYPE_SALESTAX)
							acc = it.getTaxCode().getTaxAgency()
									.getLiabilityAccount();
						else
							// if(it.getType()==TransactionItem.TYPE_SALESTAX)
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase
						if (it.getType() == TransactionItem.TYPE_SALESTAX)
							taxAgencies[getTaxAgencyVariable(codes, it
									.getTaxCode().getTaxAgency().getName(),
									company)] += it.getLineTotal();

						if (it.isTaxable && it.taxGroup != null
								&& it.taxGroup.getTaxCodes() != null) {// if
							// taxgroup
							// is not
							// null
							Set<TaxCode> taxcodes = it.taxGroup.getTaxCodes();
							Iterator<TaxCode> i2 = taxcodes.iterator();
							while (i2.hasNext()) {// i2.hasNext
								TaxCode tc = (TaxCode) i2.next();
								double rate = 0.0;

								if (tc.getTaxRates() != null) {// taxrate ==
									// null

									Set<TaxRates> taxrates = tc.getTaxRates();
									Iterator<TaxRates> i3 = taxrates.iterator();

									Date date;// =new Date();
									if (i3.hasNext()) {// if i3.hasNext
										TaxRates tr = (TaxRates) i3.next();
										date = (Date) tr.getAsOf();
										rate = tr.getRate();
										while (i3.hasNext()) {
											// The date which is latest but not
											// after
											// the today's date is to be taken
											// and its taxrate must be used in
											// the
											// calculations.
											if (date.after(tr.getAsOf())) {
												date = tr.getAsOf();
												rate = tr.getRate();
											}
											tr = (TaxRates) i3.next();
										}// while i3.hasNext()
									}// if i3.hasNext()
									rate = (rate / 100.0) * it.lineTotal;
									// changing the taxagency's liability
									// account as per
									// the 'isIncrease()' value of the account.
									if (tc.getTaxAgency().getLiabilityAccount()
											.isIncrease()) {// taxAgency account
										// isIncrease

										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] += rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] += rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}// taxAgency account isIncrease
									else {
										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] -= rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] -= rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}
								}// taxrate == null

								taxAgencies[getTaxAgencyVariable(codes, tc
										.getTaxAgency().getName(), company)] += rate;

							}// i2.hasNext
						}// if Taxgroup is not null
					}// while there is a transaction item

					// checking whether the depositIn account is updated. If it
					// has
					// a parent it also need to be updated
					if (!c.getDepositIn().isIncrease()) {
						d[getAccountVariable(accounts, c.getDepositIn()
								.getName(), company)] += c.getTotal();
						String str = c.getDepositIn().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, c.getDepositIn()
								.getName(), company)] -= c.getTotal();
						String str = c.getDepositIn().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					// Now we need to calculate the tax amount that the customer
					// had
					// to be paid.
					// For that we need to take the taxGroup in the cashsale
					// transaction
					// and checking the taxrates that are related with taxcodes
					// in
					// the taxgroup
					// And finally the liability account in the corresponding
					// taxagency must be updated...

					// If the last transaction item of the last CashSale is
					// done, we
					// need to check the accounts table
					// whether all the accounts are updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++) {
							String name = accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName();
							// if(accounterDao.getAccount(company.getId(), (l1 +
							// 1)).getName().equals("SubWO"))
							System.out.println("here - " + name);
							assertEquals(name, d[getAccountVariable(accounts,
									name, company)], accounterDao.getAccount(
									company.getId(), name).getTotalBalance());
						}
						for (long l1 = 0; l1 < codes.size(); l1++) {
							String name = codes.get((int) l1).getTaxAgency()
									.getName();
							assertEquals(name,
									taxAgencies[getTaxAgencyVariable(codes,
											name, company)],
									accounterDao.getTaxAgency(company.getId(),
											name).getBalance());
						}

					}
				}
			}
		}
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
		TaxGroup t = accounterDao.getTaxGroup(cm, tg);
		for (int i = 0; i < ti.size(); i++) {
			if (ti.get(i).isTaxable)
				ti.get(i).taxGroup = t;
		}
		inv.setDate(format.parse(d));
		inv.setSalesPerson(accounterDao.getSalesPerson(cm, sp));
		inv.setTransactionItems(ti);
		inv.setSalesTaxAmount(getAmountSalesTax(tg, ti));
		inv.setDueDate(format.parse(dd));
		inv.setAllLineTotal(getAmountAllLineTotal(ti));
		inv.setDiscountTotal(getAmountDiscountTotal(ti));
		inv
				.setBalanceDue((inv.getAllLineTotal() + inv.getSalesTaxAmount())
						- p);
		inv.setTotal(inv.getAllLineTotal() + inv.getSalesTaxAmount());
		inv.setPayments(p);
		inv.setAllTaxableLineTotal(getAmountAllTaxableLineTotal(ti));
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
			double q, double up, double d, boolean itemTax, String item1,
			Invoice inv) throws Exception {

		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setQuantity(q);
		item.setUnitPrice(1000d);
		item.setDiscount(d);
		item.setLineTotal((q * up) - d);
		item.isTaxable = itemTax;
		item.setItem(accounterDao.getItem(accounterDao.getCompany(
				accounterDao.getUser("User1@vimukti.com").getId()).getId(),
				item1));
		item.setTransaction(inv);
		return item;
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
				.getId(), ac));
		item.setTransaction(inv);
		return item;
	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double lt, String tc, Invoice inv) throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setLineTotal(lt);
		item.setTaxCode(accounterDao.getTaxCode(accounterDao.getCompany(1l)
				.getId(), tc));
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

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			List<TaxCode> codes = accounterDao.getTaxCodes(company.getId());
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getName().equals("None"))
					codes.remove(i);
			}
			ListIterator<TaxCode> tcs = codes.listIterator();

			taxAgencies = new double[codes.size()];
			for (int i = 0; i < taxAgencies.length; i++)
				taxAgencies[getTaxAgencyVariable(codes, codes.get(i)
						.getTaxAgency().getName(), company)] = codes.get(i)
						.getTaxAgency().getBalance();

			setDefaultAccountVariables(accounts, company.getId());

			Invoice invoice1 = new Invoice();
			Invoice invoice2 = new Invoice();
			Invoice invoice3 = new Invoice();
			Invoice invoice4 = new Invoice();
			Invoice invoice5 = new Invoice();
			Invoice invoice6 = new Invoice();
			Invoice invoice7 = new Invoice();
			Invoice invoice8 = new Invoice();
			Invoice invoice9 = new Invoice();
			Invoice invoice10 = new Invoice();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

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
				for (int n = 0; n < (items.size() / 2); n++) {
					Item item = nextItem(its);
					transactionItems1.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), 0D, true, item.getName(),
							invoice1));
				}
			}

			if (invoice2 != null) {
				for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
					Item item = nextItem(its);
					transactionItems2.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), 0D, true, item.getName(),
							invoice2));
				}
			}

			if (invoice3 != null) {
				for (int n = 0; n < (accounts.size() / 2); n++)
					transactionItems3.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, true, nextAccount(acs).getName(), invoice3));
			}

			if (invoice4 != null) {
				for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++)
					transactionItems4.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, true, nextAccount(acs).getName(), invoice4));
			}

			if (invoice5 != null) {
				for (int n = 0; n < (codes.size() * 5); n++)
					transactionItems5.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_SALESTAX, 1000D,
							nextTaxCode(tcs).getName(), invoice5));
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size() + codes
						.size()) / 4); n += 3) {
					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						if (invoice6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), invoice6));

						item = items.get(new Random().nextInt(items.size()));
						if (invoice7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), invoice7));

						item = items.get(new Random().nextInt(items.size()));
						if (invoice8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), invoice8));

						item = items.get(new Random().nextInt(items.size()));
						if (invoice9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), invoice9));

						item = items.get(new Random().nextInt(items.size()));
						if (invoice10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), invoice10));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						if (invoice6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											invoice6));

						account = getRandomAccount(accounts);
						if (invoice7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											invoice7));

						account = getRandomAccount(accounts);
						if (invoice8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											invoice8));

						account = getRandomAccount(accounts);
						if (invoice9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											invoice9));

						account = getRandomAccount(accounts);
						if (invoice10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											invoice10));
					}

					if (codes.size() > 0) {
						TaxCode tc = codes.get(new Random().nextInt(codes
								.size()));
						if (invoice6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), invoice6));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (invoice7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), invoice7));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (invoice8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), invoice8));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (invoice9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), invoice9));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (invoice10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), invoice10));
					}
				}
			}

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
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer1",
						"SalesPerson1", "TG12", transactionItems1, dueDate(1),
						0d, invoice1));

			if (transactionItems2.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer2",
						"SalesPerson1", "TG23", transactionItems2, dueDate(2),
						0d, invoice2));

			if (transactionItems3.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer3",
						"SalesPerson1", "TG31", transactionItems3, dueDate(1),
						0d, invoice3));

			if (transactionItems4.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer4",
						"SalesPerson1", "TG123", transactionItems4, dueDate(2),
						0d, invoice4));

			if (transactionItems5.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer5",
						"SalesPerson1", "TG12", transactionItems5, dueDate(1),
						0d, invoice5));

			if (transactionItems6.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer1",
						"SalesPerson1", "TG12", transactionItems6, dueDate(1),
						0d, invoice6));

			if (transactionItems7.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer2",
						"SalesPerson1", "TG23", transactionItems7, dueDate(2),
						0d, invoice7));

			if (transactionItems8.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer3",
						"SalesPerson1", "TG31", transactionItems8, dueDate(1),
						0d, invoice8));

			if (transactionItems9.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer4",
						"SalesPerson1", "TG123", transactionItems9, dueDate(2),
						0d, invoice9));

			if (transactionItems10.size() > 0)
				Invoices.add(testCallCreateInvoice(today(), 1l, "Customer5",
						"SalesPerson1", "TG12", transactionItems10, dueDate(1),
						0d, invoice10));

			if (checkTesting) {
				// testing of this transaction starts from here........

				Iterator<Invoice> i = Invoices.iterator();

				Invoice c;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				while (i.hasNext()) {
					c = (Invoice) i.next();
					assertNotNull(c);
					assertNotNull(c.getCustomer());
					assertNotNull(c.getCustomer().getName());
					assertEquals(c.getCustomer().getName(), accounterDao
							.getInvoice(company.getId(), c.getId())
							.getCustomer().getName());
					assertEquals(c.getDate(), accounterDao.getInvoice(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getDueDate());
					assertEquals(c.getDueDate(), accounterDao.getInvoice(
							company.getId(), c.getId()).getDueDate());
					assertEquals(c.getDiscountTotal(), accounterDao.getInvoice(
							company.getId(), c.getId()).getDiscountTotal());
					assertEquals(c.getType(), accounterDao.getInvoice(
							company.getId(), c.getId()).getType());
					assertEquals(c.getTotal(), accounterDao.getInvoice(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getSalesTaxAmount(), accounterDao
							.getInvoice(company.getId(), c.getId())
							.getSalesTaxAmount());
					if (c.getSalesPerson() != null)
						if (c.getSalesPerson().getFirstName() != null)
							assertEquals(c.getSalesPerson().getFirstName(),
									accounterDao.getInvoice(company.getId(),
											c.getId()).getSalesPerson()
											.getFirstName());
					if (c.getPayments() != 0)
						assertEquals(c.getPayments(), accounterDao.getInvoice(
								company.getId(), c.getId()).getPayments());
					assertEquals(c.getNumber(), accounterDao.getInvoice(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getIncomeAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else if (it.getType() == TransactionItem.TYPE_SALESTAX)
							acc = it.getTaxCode().getTaxAgency()
									.getLiabilityAccount();
						else
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase
						if (it.getType() == TransactionItem.TYPE_SALESTAX)
							taxAgencies[getTaxAgencyVariable(codes, it
									.getTaxCode().getTaxAgency().getName(),
									company)] += it.getLineTotal();

						if (it.taxGroup != null
								&& it.taxGroup.getTaxCodes() != null) {// if
							// taxgroup
							// is
							// not
							// null
							Set<TaxCode> taxcodes = it.taxGroup.getTaxCodes();
							Iterator<TaxCode> i2 = taxcodes.iterator();
							while (i2.hasNext()) {// i2.hasNext
								TaxCode tc = (TaxCode) i2.next();
								double rate = 0.0;// ,rate2=0.0;

								if (tc.getTaxRates() != null) {// taxrate ==
									// null

									Set<TaxRates> taxrates = tc.getTaxRates();
									Iterator<TaxRates> i3 = taxrates.iterator();
									Date date;// =new Date();
									if (i3.hasNext()) {// if i3.hasNext
										TaxRates tr = (TaxRates) i3.next();
										date = (Date) tr.getAsOf();
										rate = tr.getRate();
										while (i3.hasNext()) {
											// The date which is latest but not
											// after the today's date is to be
											// taken
											// and its taxrate must be used in
											// the calculations.

											if (date.after(tr.getAsOf())) {
												date = tr.getAsOf();
												rate = tr.getRate();
											}
											tr = (TaxRates) i3.next();
										}// while i3.hasNext()

									}// if i3.hasNext()
									rate = (rate / 100.0) * it.lineTotal;
									// changing the taxagency's liability
									// account as per
									// the 'isIncrease()' value of the account.
									if (tc.getTaxAgency().getLiabilityAccount()
											.isIncrease()) {// taxAgency account
										// isIncrease

										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] += rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] += rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}// taxAgency account isIncrease
									else {
										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] -= rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] -= rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}
								}// taxrate == null

								taxAgencies[getTaxAgencyVariable(codes, tc
										.getTaxAgency().getName(), company)] += rate;
							}// i2.hasNext

						}// if Taxgroup is not null

					}// while there is a transaction item

					// checking whether the Accounts Receivable account is
					// updated.
					// If it has a parent it also need to be updated
					d[getAccountVariable(accounts, "Accounts Receivable",
							company)] += c.getTotal();

					// Now we need to calculate the tax amount that the customer
					// had to be paid.
					// For that we need to take the taxGroup in the cashsale
					// transaction
					// and checking the taxrates that are related with taxcodes
					// in the taxgroup
					// And finally the liability account in the corresponding
					// taxagency must be updated...

					// If the last transaction item of the last CashSale is
					// done, we
					// need to check the accounts table
					// whether all the accounts are updated perfectly.

					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
						for (long l1 = 0; l1 < codes.size(); l1++) {
							String name = codes.get((int) l1).getTaxAgency()
									.getName();
							assertEquals(name,
									taxAgencies[getTaxAgencyVariable(codes,
											name, company)],
									accounterDao.getTaxAgency(company.getId(),
											name).getBalance());
						}
					}
				}
			}
		}
	}

	public CustomerCreditMemo testCallCreateCustomerCreditMemo(String d,
			long cm, String cu, String sp, String tg, List<TransactionItem> ti,
			CustomerCreditMemo ccm) throws Exception {

		ccm.setCustomer(accounterDao.getCustomer(accounterDao.getCompany(1L)
				.getId(), cu));
		ccm.setCompany(accounterDao.getCompany(1L));
		ccm.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);
		ccm.setDate(format.parse(d));
		ccm.setSalesPerson(accounterDao.getSalesPerson(accounterDao.getCompany(
				1L).getId(), sp));
		TaxGroup t = accounterDao.getTaxGroup(cm, tg);
		for (int i = 0; i < ti.size(); i++) {
			if (ti.get(i).isTaxable)
				ti.get(i).taxGroup = t;
		}
		ccm.setTransactionItems(ti);
		ccm.setSalesTax(getAmountSalesTax(tg, ti));
		ccm.setNumber(accounterGUIDao.getNextTransactionNumber(cm,
				Transaction.TYPE_CUSTOMER_CREDIT_MEMO));
		ccm.setAllLineTotal(getAmountAllLineTotal(ti));
		ccm.setDiscountTotal(getAmountDiscountTotal(ti));
		ccm.setTotal((ccm.getAllLineTotal() + ccm.getSalesTax())
				- ccm.getDiscountTotal());
		ccm.setAllTaxableLineTotal(getAmountAllTaxableLineTotal(ti));
		double customerBalance = ccm.getCustomer().getBalance();
		accounter.createCustomerCreditMemo(ccm);
		if (checkTesting)
			assertEquals(customerBalance - ccm.getTotal(), accounterDao
					.getCustomer(company.getId(), ccm.getCustomer().getId())
					.getBalance());
		return ccm;

	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double q, double up, double d, boolean it, String i,
			CustomerCreditMemo ccm) throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setQuantity(q);
		item.setUnitPrice(up);
		item.setDiscount(d);
		item.setLineTotal((q * up) - ((d * q * up) / 100));
		item.isTaxable = (it);
		item.setItem(accounterDao.getItem(accounterDao.getCompany(1L).getId(),
				i));
		item.setTransaction(ccm);
		return item;
	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double lt, String tc, CustomerCreditMemo ccm) throws Exception {
		TransactionItem item = new TransactionItem();
		item.setType(type);
		item.setLineTotal(lt);
		item.setTaxCode(accounterDao.getTaxCode(accounterDao.getCompany(1l)
				.getId(), tc));
		item.setTransaction(ccm);
		return item;
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
				.getId(), ac));
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

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			List<TaxCode> codes = accounterDao.getTaxCodes(company.getId());
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getName().equals("None"))
					codes.remove(i);
			}
			ListIterator<TaxCode> tcs = codes.listIterator();

			taxAgencies = new double[codes.size()];
			for (int i = 0; i < taxAgencies.length; i++)
				taxAgencies[getTaxAgencyVariable(codes, codes.get(i)
						.getTaxAgency().getName(), company)] = codes.get(i)
						.getTaxAgency().getBalance();

			setDefaultAccountVariables(accounts, company.getId());

			CustomerCreditMemo creditMemo1 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo2 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo3 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo4 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo5 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo6 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo7 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo8 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo9 = new CustomerCreditMemo();
			CustomerCreditMemo creditMemo10 = new CustomerCreditMemo();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

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

				for (int n = 0; n < (items.size() / 2); n++) {
					Item item = nextItem(its);
					transactionItems1.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), 0D, true, item.getName(),
							creditMemo1));
				}
			}

			if (creditMemo2 != null) {
				for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
					Item item = nextItem(its);
					transactionItems2.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), 0D, true, item.getName(),
							creditMemo2));
				}
			}

			if (creditMemo3 != null) {
				for (int n = 0; n < (accounts.size() / 2); n++)
					transactionItems3.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, true, nextAccount(acs).getName(),
							creditMemo3));
			}

			if (creditMemo4 != null) {
				for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++)
					transactionItems4.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, true, nextAccount(acs).getName(),
							creditMemo4));
			}

			if (creditMemo5 != null) {
				for (int n = 0; n < (codes.size() * 5); n++)
					transactionItems5.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_SALESTAX, 1000D,
							nextTaxCode(tcs).getName(), creditMemo5));
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size() + codes
						.size()) / 4); n += 3) {
					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						if (creditMemo6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), creditMemo6));

						item = items.get(new Random().nextInt(items.size()));
						if (creditMemo7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), creditMemo7));

						item = items.get(new Random().nextInt(items.size()));
						if (creditMemo8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), creditMemo8));

						item = items.get(new Random().nextInt(items.size()));
						if (creditMemo9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), creditMemo9));

						item = items.get(new Random().nextInt(items.size()));
						if (creditMemo10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), 0D, true,
											item.getName(), creditMemo10));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						if (creditMemo6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											creditMemo6));

						account = getRandomAccount(accounts);
						if (creditMemo7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											creditMemo7));

						account = getRandomAccount(accounts);
						if (creditMemo8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											creditMemo8));

						account = getRandomAccount(accounts);
						if (creditMemo9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											creditMemo9));

						account = getRandomAccount(accounts);
						if (creditMemo10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, true, account.getName(),
											creditMemo10));
					}

					if (codes.size() > 0) {
						TaxCode tc = codes.get(new Random().nextInt(codes
								.size()));
						if (creditMemo6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), creditMemo6));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (creditMemo7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), creditMemo7));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (creditMemo8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), creditMemo8));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (creditMemo9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), creditMemo9));

						tc = codes.get(new Random().nextInt(codes.size()));
						if (creditMemo10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_SALESTAX,
											1000D, tc.getName(), creditMemo10));
					}
				}
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
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer1", "SalesPerson1", "TG12", transactionItems1,
						creditMemo1));

			if (transactionItems2.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer2", "SalesPerson1", "TG23", transactionItems2,
						creditMemo2));

			if (transactionItems3.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer3", "SalesPerson1", "TG31", transactionItems3,
						creditMemo3));

			if (transactionItems4.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer4", "SalesPerson1", "TG123",
						transactionItems4, creditMemo4));

			if (transactionItems5.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer5", "SalesPerson1", "TG12", transactionItems5,
						creditMemo5));

			if (transactionItems6.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer1", "SalesPerson1", "TG23", transactionItems6,
						creditMemo6));

			if (transactionItems7.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer2", "SalesPerson1", "TG31", transactionItems7,
						creditMemo7));

			if (transactionItems8.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer3", "SalesPerson1", "TG123",
						transactionItems8, creditMemo8));

			if (transactionItems9.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer4", "SalesPerson1", "TG12", transactionItems9,
						creditMemo9));

			if (transactionItems10.size() > 0)
				ccms.add(testCallCreateCustomerCreditMemo(today(), 1l,
						"Customer5", "SalesPerson1", "TG23",
						transactionItems10, creditMemo10));

			if (checkTesting) {
				// testing of this transaction starts from here........

				Iterator<CustomerCreditMemo> i = ccms.iterator();
				company = accounterDao.getCompany(1L);
				CustomerCreditMemo c;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				while (i.hasNext()) {
					c = (CustomerCreditMemo) i.next();
					assertNotNull(c);
					assertNotNull(c.getCustomer());
					assertNotNull(c.getCustomer().getName());
					assertEquals(c.getCustomer().getName(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getCustomer().getName());
					assertEquals(c.getDate(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getDate());
					assertEquals(c.getDiscountTotal(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getDiscountTotal());
					assertEquals(c.getType(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getType());
					assertEquals(c.getTotal(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getTotal());
					assertEquals(c.getSalesTax(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getSalesTax());
					if (c.getSalesPerson() != null)
						if (c.getSalesPerson().getFirstName() != null)
							assertEquals(c.getSalesPerson().getFirstName(),
									accounterDao.getCustomerCreditMemo(
											company.getId(), c.getId())
											.getSalesPerson().getFirstName());
					assertEquals(c.getNumber(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getNumber());

					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getIncomeAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else if (it.getType() == TransactionItem.TYPE_SALESTAX)
							acc = it.getTaxCode().getTaxAgency()
									.getLiabilityAccount();
						else
							acc = it.getAccount();
						if (acc.getType() == Account.TYPE_ACCOUNT_PAYABLE)
							System.out.println("here");

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase
						if (it.getType() == TransactionItem.TYPE_SALESTAX)
							taxAgencies[getTaxAgencyVariable(codes, it
									.getTaxCode().getTaxAgency().getName(),
									company)] -= it.getLineTotal();

						if (it.taxGroup != null
								&& it.taxGroup.getTaxCodes() != null) {// if
							// taxgroup
							// is not
							// null
							Set<TaxCode> taxcodes = it.taxGroup.getTaxCodes();
							Iterator<TaxCode> i2 = taxcodes.iterator();
							while (i2.hasNext()) {// i2.hasNext
								TaxCode tc = (TaxCode) i2.next();
								double rate = 0.0;// ,rate2=0.0;

								if (tc.getTaxRates() != null) {// taxrate ==
									// null

									Set<TaxRates> taxrates = tc.getTaxRates();
									Iterator<TaxRates> i3 = taxrates.iterator();
									Date date;
									if (i3.hasNext()) {// if i3.hasNext
										TaxRates tr = (TaxRates) i3.next();
										date = (Date) tr.getAsOf();
										rate = tr.getRate();
										while (i3.hasNext()) {
											// The date which is latest but not
											// after
											// the today's date is to be taken
											// and its taxrate must be used in
											// the
											// calculations.
											if (date.after(tr.getAsOf())) {
												date = tr.getAsOf();
												rate = tr.getRate();
											}
											tr = (TaxRates) i3.next();
										}// while i3.hasNext()
									}// if i3.hasNext()
									rate = (rate / 100.0) * it.lineTotal;
									// changing the taxagency's liability
									// account as per
									// the 'isIncrease()' value of the account.
									if (tc.getTaxAgency().getLiabilityAccount()
											.isIncrease()) {// taxAgency account
										// isIncrease

										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] -= rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] -= rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}// taxAgency account isIncrease
									else {
										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] += rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] += rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}
								}// taxrate == null
								taxAgencies[getTaxAgencyVariable(codes, tc
										.getTaxAgency().getName(), company)] -= rate;
							}// i2.hasNext

						}// if Taxgroup is not null

					}// while there is a transaction item

					// checking whether the AccountsReceivable account is
					// updated. If it has
					// a parent it also need to be updated
					d[getAccountVariable(accounts, "Accounts Receivable",
							company)] -= c.getTotal();

					// Now we need to calculate the tax amount that the customer
					// had
					// to be paid.
					// For that we need to take the taxGroup in the
					// Customercrediememo
					// transaction
					// and checking the taxrates that are related with taxcodes
					// in
					// the taxgroup
					// And finally the liability account in the corresponding
					// taxagency must be updated...

					// If the last transaction item of the last
					// CustomerCreditMemo is done, we
					// need to check the accounts table whether all the accounts
					// are updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());

						for (long l1 = 0; l1 < codes.size(); l1++) {
							String name = codes.get((int) l1).getTaxAgency()
									.getName();
							assertEquals(name,
									taxAgencies[getTaxAgencyVariable(codes,
											name, company)],
									accounterDao.getTaxAgency(company.getId(),
											name).getBalance());
						}
					}
				}
			}
		}
	}

	public CustomerRefund testCallCreateCustomerRefunds(long cm, String payTo,
			String payFrom, double amt, String date, double payments,
			String paymentmethod, String checkno) throws Exception {

		company = accounterDao.getCompany(1L);
		CustomerRefund customerRefund1 = new CustomerRefund();
		customerRefund1.setCompany(company);
		customerRefund1.setType(Transaction.TYPE_CUSTOMER_REFUNDS);
		customerRefund1.setPayTo(accounterDao.getCustomer(company.getId(),
				payTo));
		customerRefund1.setPayFrom(accounterDao.getAccount(company.getId(),
				payFrom));
		customerRefund1.setNumber(accounterGUIDao.getNextTransactionNumber(
				company.getId(), Transaction.TYPE_CUSTOMER_REFUNDS));
		customerRefund1.paymentMethod = (paymentmethod);
		if (paymentmethod == AccounterConstants.PAYMENT_METHOD_CHECK)
			customerRefund1.setCheckNumber(checkno);
		customerRefund1.setTotal(amt);
		customerRefund1.setDate(format.parse(date));
		customerRefund1.setCustomerBalance(accounterDao.getCustomer(
				company.getId(), customerRefund1.getPayTo().getId())
				.getBalance()
				+ customerRefund1.getTotal());
		customerRefund1.setEndingBalance(accounterDao.getAccount(
				company.getId(), payFrom).getTotalBalance()
				+ customerRefund1.getTotal());
		customerRefund1.setPayments(payments);
		customerRefund1.setBalanceDue(customerRefund1.getTotal());
		double customerBalance = accounterDao.getCustomer(company.getId(),
				customerRefund1.getPayTo().getId()).getBalance();
		accounter.createCustomerRefunds(customerRefund1);
		if (checkTesting)
			assertEquals(customerBalance + customerRefund1.getTotal(),
					accounterDao.getCustomer(company.getId(),
							customerRefund1.getPayTo().getId()).getBalance());
		return customerRefund1;
	}

	public void testCreateCustomerRefunds() throws Exception {

		if (createCustomerRefunds) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Account> accounts = accounterDao.getAccounts(company.getId());

			setDefaultAccountVariables(accounts, company.getId());

			// /* testCallCreateCustomerRefund( company in long
			// * transation type in int
			// * customer in String
			// * Account in String
			// * Amount in double
			// * Date in String
			// * payments in double
			// * payment method in int
			// * check no in long
			//	         
			ArrayList<CustomerRefund> cfs = new ArrayList<CustomerRefund>();// cashAccounts
																			// .
																			// get
																			// (
																			// r
																			// .
																			// nextInt
																			// (
																			// cashAccounts
																			// .
																			// size
																			// (
																			// )
																			// )
																			// )
																			// .
																			// getName
																			// (
																			// )
			List<Account> payFromAccounts = Utility.getPayFromAccounts(company);// nextAccount
																				// (
																				// cashAccounts
																				// )
																				// .
																				// getName
																				// (
																				// )

			// cfs.add(testCallCreateCustomerRefunds(1l, "Customer1", //
			// "Account Cash", 1000d, today(), 0d,
			// AccounterConstants.PAYMENT_METHOD_CASH, //
			// 1111l));
			// cfs.add(testCallCreateCustomerRefunds(1l, "Customer1", //
			// "Account Cash", 1000d, today(), 0d,
			// AccounterConstants.PAYMENT_METHOD_CASH, //
			// 1111l));

			cfs.add(testCallCreateCustomerRefunds(1l, "Customer1",
					getRandomPayFromAccount(payFromAccounts).getName(), 1000d,
					today(), 0d, AccounterConstants.PAYMENT_METHOD_CASH,
					"1111l"));
			// cfs.add(testCallCreateCustomerRefunds(1l, "Customer2",
			// getRandomPayFromAccount(payFromAccounts).getName(), 1000d,
			// today(), 0d, AccounterConstants.PAYMENT_METHOD_CHECK, 1111l));
			// cfs.add(testCallCreateCustomerRefunds(1l, "Customer3",
			// getRandomPayFromAccount(payFromAccounts).getName(), 1000d,
			// today(), 0d, AccounterConstants.PAYMENT_METHOD_CREDITCARD,
			// 1111l));
			// cfs.add(testCallCreateCustomerRefunds(1l, "Customer4",
			// getRandomPayFromAccount(payFromAccounts).getName(), 1000d,
			// today(), 0d, AccounterConstants.PAYMENT_METHOD_CASH, 1111l));
			// cfs.add(testCallCreateCustomerRefunds(1l, "Customer5",
			// getRandomPayFromAccount(payFromAccounts).getName(), 1000d,
			// today(), 0d, AccounterConstants.PAYMENT_METHOD_CHECK, 1112l));

			if (createMore) {
				cfs.add(testCallCreateCustomerRefunds(1l, "Customer1",
						getRandomPayFromAccount(payFromAccounts).getName(),
						1000d, today(), 0d,
						AccounterConstants.PAYMENT_METHOD_CREDITCARD, "1111l"));
				cfs.add(testCallCreateCustomerRefunds(1l, "Customer2",
						getRandomPayFromAccount(payFromAccounts).getName(),
						1000d, today(), 0d,
						AccounterConstants.PAYMENT_METHOD_CASH, "1111l"));
				cfs.add(testCallCreateCustomerRefunds(1l, "Customer3",
						getRandomPayFromAccount(payFromAccounts).getName(),
						1000d, today(), 0d,
						AccounterConstants.PAYMENT_METHOD_CHECK, "1111l"));
				cfs.add(testCallCreateCustomerRefunds(1l, "Customer4",
						getRandomPayFromAccount(payFromAccounts).getName(),
						1000d, today(), 0d,
						AccounterConstants.PAYMENT_METHOD_CREDITCARD, "1111l"));
				cfs.add(testCallCreateCustomerRefunds(1l, "Customer5",
						getRandomPayFromAccount(payFromAccounts).getName(),
						1000d, today(), 0d,
						AccounterConstants.PAYMENT_METHOD_CREDITCARD, "1111l"));
			}

			if (checkTesting) {
				// testing of this transaction starts from here........

				Iterator<CustomerRefund> i = cfs.iterator();
				CustomerRefund c;
				while (i.hasNext()) {
					c = (CustomerRefund) i.next();
					assertNotNull(c);
					assertNotNull(c.getPayTo());
					assertNotNull(c.getPayTo().getName());
					assertEquals(c.getPayTo().getName(), accounterDao
							.getCustomerRefunds(company.getId(), c.getId())
							.getPayTo().getName());
					assertNotNull(c.getPayFrom());
					assertNotNull(c.getPayFrom().getName());
					assertEquals(c.getPayFrom().getName(), accounterDao
							.getCustomerRefunds(company.getId(), c.getId())
							.getPayFrom().getName());
					assertNotNull(c.getPaymentMethod());
					assertNotNull(c.getPaymentMethod());
					assertEquals(c.getPaymentMethod(), accounterDao
							.getCustomerRefunds(company.getId(), c.getId())
							.getPaymentMethod());
					assertEquals(c.getDate(), accounterDao.getCustomerRefunds(
							company.getId(), c.getId()).getDate());
					if (c.getDate() != null)
						assertEquals(c.getDate(), accounterDao
								.getCustomerRefunds(company.getId(), c.getId())
								.getDate());
					assertEquals(c.getType(), accounterDao.getCustomerRefunds(
							company.getId(), c.getId()).getType());
					assertEquals(c.getTotal(), accounterDao.getCustomerRefunds(
							company.getId(), c.getId()).getTotal());
					if (c.getPayments() != 0)
						assertEquals(c.getPayments(), accounterDao
								.getCustomerRefunds(company.getId(), c.getId())
								.getPayments());
					assertEquals(c.getNumber(), accounterDao
							.getCustomerRefunds(company.getId(), c.getId())
							.getNumber());

					// checking whether the depositIn account is updated. If it
					// has
					// a parent it also need to be updated
					d[getAccountVariable(accounts, "Accounts Receivable",
							company)] += c.getTotal();

					if (c.getPayFrom().isIncrease()) {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] += c
								.getTotal();
						String str = c.getPayFrom().getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] -= c
								.getTotal();
						String str = c.getPayFrom().getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					// If the last transaction item of the last CashSale is
					// done, we
					// need to check the accounts table whether all the accounts
					// are
					// updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									accounterDao.getAccount(company.getId(),
											(l1 + 1)).getTotalBalance(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)]);
					}
				}
			}
		}
	}

	public TransactionItem testCallCreateTransactionItem(String compny,
			int type, double quantity, double unitprice, String item,
			EnterBill enterbill) throws DAOException {

		TransactionItem item1 = new TransactionItem();
		testCallSetValuesTransactionItem(item1, compny, type, item, quantity,
				unitprice);
		item1.setTransaction(enterbill);
		return item1;

	}

	public EnterBill testCallCreateEnterBill(long compny, String vendor,
			String date, String duedate, List<TransactionItem> ti,
			EnterBill enterBill) throws Exception {

		enterBill.setNumber(accounterGUIDao.getNextTransactionNumber(compny,
				Transaction.TYPE_ENTER_BILL));
		enterBill.setVendor(accounterDao.getVendor(compny, vendor));
		enterBill.setCompany(accounterDao.getCompany(1L));
		enterBill.setType(Transaction.TYPE_ENTER_BILL);
		enterBill.setDate(format.parse(date));
		enterBill.setDueDate(format.parse(duedate));
		enterBill.setTransactionItems(ti);
		enterBill.setTotal(getAmountAllLineTotal(ti));
		double vendorBalance = accounterDao.getVendor(company.getId(),
				enterBill.getVendor().getId()).getBalance();
		accounter.createEnterBill(enterBill);
		if (checkTesting)
			assertEquals(vendorBalance + enterBill.getTotal(), accounterDao
					.getVendor(company.getId(), enterBill.getVendor().getId())
					.getBalance());
		return enterBill;
	}

	public void testCreateEnterBill() throws Exception {

		if (createEnterBills) {

			company = accounterDao.getCompany(1L);

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			setDefaultAccountVariables(accounts, company.getId());
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			EnterBill enterBill1 = new EnterBill();
			EnterBill enterBill2 = new EnterBill();
			EnterBill enterBill3 = new EnterBill();
			EnterBill enterBill4 = new EnterBill();
			EnterBill enterBill5 = new EnterBill();
			EnterBill enterBill6 = new EnterBill();
			EnterBill enterBill7 = new EnterBill();
			EnterBill enterBill8 = new EnterBill();
			EnterBill enterBill9 = new EnterBill();
			EnterBill enterBill10 = new EnterBill();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

			// testCallCreateTransactionItem( company in long
			// * TransactionItem type
			// * item in String
			// * quantity in double
			// * linetotal in double
			// * Enterbill
			// * )

			//transactionItems1.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Write off", //
			// enterBill1));
			//transactionItems2.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Bank Charge", //
			// enterBill2));

			if (enterBill1 != null) {
				for (int n = 0; n < (items.size() / 2); n++) {
					Item item = nextItem(its);
					transactionItems1.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), item.getName(), enterBill1));
				}
			}

			if (enterBill2 != null) {

				for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
					Item item = nextItem(its);
					transactionItems2.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), item.getName(), enterBill2));
				}
			}

			if (enterBill3 != null) {
				for (int n = 0; n < (accounts.size() / 2); n++)
					transactionItems3.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(), enterBill3));
			}

			if (enterBill4 != null) {
				for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++)
					transactionItems4.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(), enterBill4));
			}

			if (enterBill5 != null) {
				for (int n = 0; n <= accounts.size() / 4; n++)
					transactionItems5.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, getRandomAccount(accounts).getName(),
							enterBill5));
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size()) / 4); n++) {
					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						if (enterBill6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), enterBill6));

						item = items.get(new Random().nextInt(items.size()));
						if (enterBill7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), enterBill7));

						item = items.get(new Random().nextInt(items.size()));
						if (enterBill8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), enterBill8));

						item = items.get(new Random().nextInt(items.size()));
						if (enterBill9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), enterBill9));

						item = items.get(new Random().nextInt(items.size()));
						if (enterBill10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), enterBill10));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						if (enterBill6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											enterBill6));

						account = getRandomAccount(accounts);
						if (enterBill7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											enterBill7));

						account = getRandomAccount(accounts);
						if (enterBill8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											enterBill8));

						account = getRandomAccount(accounts);
						if (enterBill9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											enterBill9));

						account = getRandomAccount(accounts);
						if (enterBill10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											enterBill10));
					}
				}
			}

			// testCallCreateEnterBill( company in long
			// Vendor in String
			// TransactionItem Type
			// Date in String
			// dueDate in String
			// transaction Items set
			// Total in double
			// Enter bill
			// ) returns EnterBill Object

			ArrayList<EnterBill> enterbills = new ArrayList<EnterBill>();

			if (transactionItems1.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor1", today(),
						dueDate(1), transactionItems1, enterBill1));

			if (transactionItems2.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor2", today(),
						dueDate(2), transactionItems2, enterBill2));

			if (transactionItems3.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor3", today(),
						dueDate(3), transactionItems3, enterBill3));

			if (transactionItems4.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor4", today(),
						dueDate(1), transactionItems4, enterBill4));

			if (transactionItems5.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor5", today(),
						dueDate(2), transactionItems5, enterBill5));

			if (transactionItems6.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor1", today(),
						dueDate(3), transactionItems6, enterBill6));

			if (transactionItems7.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor2", today(),
						dueDate(1), transactionItems7, enterBill7));

			if (transactionItems8.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor3", today(),
						dueDate(2), transactionItems8, enterBill8));

			if (transactionItems9.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor4", today(),
						dueDate(3), transactionItems9, enterBill9));

			if (transactionItems10.size() > 0)
				enterbills.add(testCallCreateEnterBill(1l, "Vendor5", today(),
						dueDate(1), transactionItems10, enterBill10));

			if (checkTesting) {
				// testing of this transaction starts from here........

				Iterator<EnterBill> i = enterbills.iterator();

				EnterBill c;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				while (i.hasNext()) {
					c = (EnterBill) i.next();
					assertNotNull(c);
					assertNotNull(c.getVendor());
					assertNotNull(c.getVendor().getName());
					assertEquals(c.getVendor().getName(), accounterDao
							.getEnterBill(company.getId(), c.getId())
							.getVendor().getName());
					assertEquals(c.getDate(), accounterDao.getEnterBill(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getDueDate());
					assertEquals(c.getDueDate(), accounterDao.getEnterBill(
							company.getId(), c.getId()).getDueDate());
					assertEquals(c.getTotal(), accounterDao.getEnterBill(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getType(), accounterDao.getEnterBill(
							company.getId(), c.getId()).getType());
					if (c.getPayments() != 0)
						assertEquals(c.getPayments(), accounterDao
								.getEnterBill(company.getId(), c.getId())
								.getPayments());
					if (c.getPaymentTerm() != null)
						assertEquals(c.getPaymentTerm(), accounterDao
								.getEnterBill(company.getId(), c.getId())
								.getPaymentTerm());
					assertEquals(c.getNumber(), accounterDao.getEnterBill(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getExpenseAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}

						else {
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}

					}// while there is a transaction item

					// checking whether the Accounts Payable account is updated.

					d[getAccountVariable(accounts, "Accounts Payable", company)] += c
							.getTotal();

					// If the last transaction item of the last EnterBill is
					// done, // we need to check the accounts table whether all
					// the accounts are updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
					}
				}
			}
		}
	}

	public TransactionItem testCallCreateTransactionItem(String compny,
			int type, double quantity, double unitprice, String item,
			VendorCreditMemo vcm) throws DAOException {
		TransactionItem it = new TransactionItem();

		testCallSetValuesTransactionItem(it, compny, type, item, quantity,
				unitprice);
		it.setTransaction(vcm);
		return it;
	}

	public VendorCreditMemo testCallCreateVendorCreditMemo(String vendor,
			long cm, String date, List<TransactionItem> transactionitem,
			VendorCreditMemo vcm) throws Exception {

		vcm.setVendor(accounterDao.getVendor(1L, vendor));
		vcm.setCompany(accounterDao.getCompany(1l));
		vcm.setType(Transaction.TYPE_VENDOR_CREDIT_MEMO);
		vcm.setDate(format.parse(date));
		vcm.setNumber(accounterGUIDao.getNextTransactionNumber(cm,
				Transaction.TYPE_VENDOR_CREDIT_MEMO));
		vcm.setTransactionItems(transactionitem);
		vcm.setTotal(getAmountAllLineTotal(transactionitem));
		double vendorBalance = accounterDao.getVendor(company.getId(),
				vcm.getVendor().getId()).getBalance();
		accounter.createVendorCreditMemo(vcm);
		if (checkTesting)
			assertEquals(vendorBalance - vcm.getTotal(), accounterDao
					.getVendor(company.getId(), vcm.getVendor().getId())
					.getBalance());
		return vcm;
	}

	public void testCreateVendorCreditMemo() throws Exception {

		if (createVendorCreditMemos) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			setDefaultAccountVariables(accounts, company.getId());

			VendorCreditMemo vendorCreditMemo1 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo2 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo3 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo4 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo5 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo6 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo7 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo8 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo9 = new VendorCreditMemo();
			VendorCreditMemo vendorCreditMemo10 = new VendorCreditMemo();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

			// testCallCreateTransactionItem( company in long
			// * TransactionItem type
			// * item in String
			// * quantity in double
			// * linetotal in double
			// * VendoeCreditMemo
			// * )

			//transactionItems1.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Bank Charge", //
			// vendorCreditMemo1));
			//transactionItems2.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Write off", //
			// vendorCreditMemo2));
			if (vendorCreditMemo1 != null) {
				for (int n = 0; n < (items.size() / 2); n++) {
					Item item = nextItem(its);
					transactionItems1
							.add(testCallCreateTransactionItem(company
									.getName(), TransactionItem.TYPE_ITEM, 1D,
									item.getSalesPrice(), item.getName(),
									vendorCreditMemo1));
				}
			}

			if (vendorCreditMemo2 != null) {
				for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
					Item item = nextItem(its);
					transactionItems2
							.add(testCallCreateTransactionItem(company
									.getName(), TransactionItem.TYPE_ITEM, 1D,
									item.getSalesPrice(), item.getName(),
									vendorCreditMemo2));
				}
			}

			if (vendorCreditMemo3 != null) {
				for (int n = 0; n < (accounts.size() / 2); n++)
					transactionItems3.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(),
							vendorCreditMemo3));
			}

			if (vendorCreditMemo4 != null) {
				for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++)
					transactionItems4.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(),
							vendorCreditMemo4));
			}

			if (vendorCreditMemo5 != null) {
				for (int n = (accounts.size() / 4) + 1; n <= accounts.size(); n++)
					transactionItems5.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(),
							vendorCreditMemo5));
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size()) / 4); n++) {
					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						if (vendorCreditMemo6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(),
											vendorCreditMemo6));

						item = items.get(new Random().nextInt(items.size()));
						if (vendorCreditMemo7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(),
											vendorCreditMemo7));

						item = items.get(new Random().nextInt(items.size()));
						if (vendorCreditMemo8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(),
											vendorCreditMemo8));

						item = items.get(new Random().nextInt(items.size()));
						if (vendorCreditMemo9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(),
											vendorCreditMemo9));

						item = items.get(new Random().nextInt(items.size()));
						if (vendorCreditMemo10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(),
											vendorCreditMemo10));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						if (vendorCreditMemo6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											vendorCreditMemo6));

						account = getRandomAccount(accounts);
						if (vendorCreditMemo7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											vendorCreditMemo7));

						account = getRandomAccount(accounts);
						if (vendorCreditMemo8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											vendorCreditMemo8));

						account = getRandomAccount(accounts);
						if (vendorCreditMemo9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											vendorCreditMemo9));

						account = getRandomAccount(accounts);
						if (vendorCreditMemo10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											vendorCreditMemo10));
					}
				}
			}

			// testCallCreateVendorCreditMemo( vendor in String
			// company in long
			// type in predefined int
			// date in String
			// List transactionitem
			// total in double
			// VendorCreditMemo
			// )

			ArrayList<VendorCreditMemo> vcms = new ArrayList<VendorCreditMemo>();

			if (transactionItems1.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor1", 1l, today(),
						transactionItems1, vendorCreditMemo1));

			if (transactionItems2.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor2", 1l, today(),
						transactionItems2, vendorCreditMemo2));

			if (transactionItems3.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor3", 1l, today(),
						transactionItems3, vendorCreditMemo3));

			if (transactionItems4.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor4", 1l, today(),
						transactionItems4, vendorCreditMemo4));

			if (transactionItems5.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor5", 1l, today(),
						transactionItems5, vendorCreditMemo5));

			if (transactionItems6.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor1", 1l, today(),
						transactionItems6, vendorCreditMemo6));

			if (transactionItems7.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor2", 1l, today(),
						transactionItems7, vendorCreditMemo7));

			if (transactionItems8.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor3", 1l, today(),
						transactionItems8, vendorCreditMemo8));

			if (transactionItems9.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor4", 1l, today(),
						transactionItems9, vendorCreditMemo9));

			if (transactionItems10.size() > 0)
				vcms.add(testCallCreateVendorCreditMemo("Vendor5", 1l, today(),
						transactionItems10, vendorCreditMemo10));

			if (checkTesting) {
				Iterator<VendorCreditMemo> i = vcms.iterator();
				company = accounterDao.getCompany(1L);
				VendorCreditMemo c;

				while (i.hasNext()) {
					List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
					c = (VendorCreditMemo) i.next();
					assertNotNull(c);
					assertNotNull(c.getVendor());
					assertNotNull(c.getVendor().getName());
					assertEquals(c.getVendor().getName(), accounterDao
							.getVendorCreditMemo(company.getId(), c.getId())
							.getVendor().getName());
					assertEquals(c.getDate(), accounterDao.getVendorCreditMemo(
							company.getId(), c.getId()).getDate());
					assertEquals(c.getTotal(), accounterDao
							.getVendorCreditMemo(company.getId(), c.getId())
							.getTotal());
					assertEquals(c.getType(), accounterDao.getVendorCreditMemo(
							company.getId(), c.getId()).getType());
					assertEquals(c.getNumber(), accounterDao
							.getVendorCreditMemo(company.getId(), c.getId())
							.getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getExpenseAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase

					}// while there is a transaction item

					// checking whether the Accounts Payable account is updated.
					d[getAccountVariable(accounts, company
							.getAccountsPayableAccount().getName(), company)] -= c
							.getTotal();

					// If the last transaction item of the last vendorCreditMemo
					// is
					// done, we need to check the accounts table whether all the
					// accounts are updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
					}
				}
			}
		}
	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double quantity, double unitprice, String item, CashPurchase cp)
			throws Exception {

		TransactionItem item1 = new TransactionItem();
		testCallSetValuesTransactionItem(item1, cm, type, item, quantity,
				unitprice);
		item1.setTransaction(cp);
		return item1;
	}

	public CashPurchase testCallCreateCashPurchase(long cm, String vendor,
			String paymethod, String date, String payfrom,
			List<TransactionItem> ti, String checkno, CashPurchase cp)
			throws Exception {

		cp.setVendor(accounterDao.getVendor(cm, vendor));
		cp.setType(Transaction.TYPE_CASH_PURCHASE);
		cp.setCompany(accounterDao.getCompany(1L));
		cp.setNumber(accounterGUIDao.getNextTransactionNumber(cm,
				Transaction.TYPE_CASH_PURCHASE));
		cp.setPaymentMethod(paymethod);
		if (paymethod == AccounterConstants.PAYMENT_METHOD_CHECK)
			cp.setCheckNumber(checkno);
		cp.setDate(format.parse(date));
		cp.setTransactionItems(ti);
		cp.setTotal(getAmountAllLineTotal(ti));
		cp.setPayFrom(accounterDao.getAccount(1L, payfrom));
		double vendorBalance = accounterDao.getVendor(company.getId(),
				cp.getVendor().getId()).getBalance();
		accounter.createCashPurchase(cp);
		if (checkTesting)
			assertEquals(vendorBalance, accounterDao.getVendor(company.getId(),
					cp.getVendor().getId()).getBalance());
		return cp;

	}

	public void testCreateCashPurchase() throws Exception {

		if (createCashPurchases) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			setDefaultAccountVariables(accounts, company.getId());

			CashPurchase cashPurchase1 = new CashPurchase();
			CashPurchase cashPurchase2 = new CashPurchase();
			CashPurchase cashPurchase3 = new CashPurchase();
			CashPurchase cashPurchase4 = new CashPurchase();
			CashPurchase cashPurchase5 = new CashPurchase();
			CashPurchase cashPurchase6 = new CashPurchase();
			CashPurchase cashPurchase7 = new CashPurchase();
			CashPurchase cashPurchase8 = new CashPurchase();
			CashPurchase cashPurchase9 = new CashPurchase();
			CashPurchase cashPurchase10 = new CashPurchase();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

			// testCallCreateTransactionItem( company in long
			// TransactionItem type
			// item in String
			// quantity in double
			// linetotal in double
			// CashPurchase
			// )

			//transactionItems1.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Write off", //
			// cashPurchase1));
			//transactionItems2.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Bank Charge", //
			// cashPurchase2));

			if (cashPurchase1 != null) {
				for (int n = 0; n < (items.size() / 2); n++) {
					Item item = nextItem(its);
					transactionItems1.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), item.getName(), cashPurchase1));
				}
			}

			if (cashPurchase2 != null) {
				for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
					Item item = nextItem(its);
					transactionItems2.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ITEM, 1D, item
							.getSalesPrice(), item.getName(), cashPurchase2));
				}
			}

			if (cashPurchase3 != null) {
				for (int n = 0; n < (accounts.size() / 2); n++)
					transactionItems3.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(), cashPurchase3));
			}

			if (cashPurchase4 != null) {
				for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++)
					transactionItems4.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(), cashPurchase4));
			}

			if (cashPurchase5 != null) {
				for (int n = (accounts.size() / 4) + 1; n <= accounts.size(); n++)
					transactionItems5.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_ACCOUNT, 1D,
							1000D, nextAccount(acs).getName(), cashPurchase5));
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size()) / 4); n += 2) {
					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						if (cashPurchase6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), cashPurchase6));

						item = items.get(new Random().nextInt(items.size()));
						if (cashPurchase7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), cashPurchase7));

						item = items.get(new Random().nextInt(items.size()));
						if (cashPurchase8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), cashPurchase8));

						item = items.get(new Random().nextInt(items.size()));
						if (cashPurchase9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), cashPurchase9));

						item = items.get(new Random().nextInt(items.size()));
						if (cashPurchase10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ITEM, 1D, item
													.getSalesPrice(), item
													.getName(), cashPurchase10));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						if (cashPurchase6 != null)
							transactionItems6
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											cashPurchase6));

						account = getRandomAccount(accounts);
						if (cashPurchase7 != null)
							transactionItems7
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											cashPurchase7));

						account = getRandomAccount(accounts);
						if (cashPurchase8 != null)
							transactionItems8
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											cashPurchase8));

						account = getRandomAccount(accounts);
						if (cashPurchase9 != null)
							transactionItems9
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											cashPurchase9));

						account = getRandomAccount(accounts);
						if (cashPurchase10 != null)
							transactionItems10
									.add(testCallCreateTransactionItem(company
											.getName(),
											TransactionItem.TYPE_ACCOUNT, 1D,
											1000D, account.getName(),
											cashPurchase10));
					}
				}
			}

			ArrayList<CashPurchase> cps = new ArrayList<CashPurchase>();

			// testCallCreateCashPurchase( company in long
			// vendor in String
			// type in int
			// paymentmethod in int
			// date in String
			// payFrom in String
			// transactionItems List
			// total in double
			// CheckNo in String
			// CashPurchase object
			// ) returns CashPurchase object..

			List<Account> payFromAccounts = Utility.getPayFromAccounts(company);

			// cps.add(testCallCreateCashPurchase( 1l ,"Vendor1", //
			// AccounterConstants.PAYMENT_METHOD_CASH, today(), "Account Cash",
			// //
			// transactionItems1, "111", cashPurchase1));
			// cps.add(testCallCreateCashPurchase( 1l ,"Vendor2", //
			// AccounterConstants.PAYMENT_METHOD_CASH, today(),
			// "Account Credit Card", //
			// transactionItems2, "111", cashPurchase2));

			if (transactionItems1.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor1",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems1, "111", cashPurchase1));

			if (transactionItems2.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor2",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems2, "111", cashPurchase2));

			if (transactionItems3.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor3",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems3, "111", cashPurchase3));

			if (transactionItems4.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor4",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems4, "111", cashPurchase4));

			if (transactionItems5.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor5",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems5, "111", cashPurchase5));

			if (transactionItems6.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor1",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems6, "111", cashPurchase6));

			if (transactionItems7.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor2",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems7, "111", cashPurchase7));

			if (transactionItems8.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor3",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems8, "111", cashPurchase8));

			if (transactionItems9.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor4",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems9, "111", cashPurchase9));

			if (transactionItems10.size() > 0)
				cps.add(testCallCreateCashPurchase(1l, "Vendor5",
						AccounterConstants.PAYMENT_METHOD_CASH, today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						transactionItems10, "111", cashPurchase10));

			if (checkTesting) {
				// testing of this transaction starts from here........

				Iterator<CashPurchase> i = cps.iterator();
				CashPurchase c;

				while (i.hasNext()) {
					c = (CashPurchase) i.next();
					List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
					assertNotNull(c);
					assertNotNull(c.getVendor());
					assertNotNull(c.getVendor().getName());
					assertEquals(c.getVendor().getName(), accounterDao
							.getCashPurchase(company.getId(), c.getId())
							.getVendor().getName());
					assertEquals(c.getDate(), accounterDao.getCashPurchase(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getDate());
					assertEquals(c.getDate(), accounterDao.getCashPurchase(
							company.getId(), c.getId()).getDate());
					assertEquals(c.getTotal(), accounterDao.getCashPurchase(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getType(), accounterDao.getCashPurchase(
							company.getId(), c.getId()).getType());
					assertEquals(c.getPaymentMethod(), accounterDao
							.getCashPurchase(company.getId(), c.getId())
							.getPaymentMethod());
					if (c.getDeliveryDate() != null)
						assertEquals(c.getDeliveryDate(), accounterDao
								.getCashPurchase(company.getId(), c.getId())
								.getDeliveryDate());
					if (c.getPayFrom() != null)
						assertEquals(c.getPayFrom().getName(), accounterDao
								.getCashPurchase(company.getId(), c.getId())
								.getPayFrom().getName());
					assertEquals(c.getNumber(), accounterDao.getCashPurchase(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getExpenseAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase

					}// while there is a transaction item

					// checking whether the payFrom account is updated. If it
					// has a
					// parent it also need to be updated
					if (c.getPayFrom().isIncrease()) {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] += c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] -= c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					// If the last transaction item of the last CashPurchase is
					// done, we need to check the accounts table whether all the
					// accounts are updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
					}
				}
			}
		}
	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double quantity, double unitprice, String item, WriteCheck w)
			throws DAOException {

		TransactionItem it = new TransactionItem();

		testCallSetValuesTransactionItem(it, cm, type, item, quantity,
				unitprice);
		it.setTransaction(w);
		return it;
	}

	public void testCallCreateWriteCheck(String cm, String date, String bank,
			List<TransactionItem> ti, WriteCheck w) throws Exception {
		w.setType(Transaction.TYPE_WRITE_CHECK);
		w.setNumber(accounterGUIDao.getNextTransactionNumber(accounterDao
				.getCompany(1L).getId(), Transaction.TYPE_WRITE_CHECK));
		w.setDate(format.parse(date));
		w.setCompany(accounterDao.getCompany(1L));
		w.setBankAccount(accounterDao.getAccount(accounterDao.getCompany(1L)
				.getId(), bank));
		w.setBalance(w.getBankAccount().getTotalBalance());
		w.setTransactionItems(ti);
		w.setAmount(getAmountAllLineTotal(ti));
		w.setTotal(getAmountAllLineTotal(ti));
	}

	public String getTaxAgencyName(List<TransactionItem> ti) {
		String s = null;
		ListIterator<TransactionItem> til = ti.listIterator();
		if (til.hasNext()) {
			s = ((TransactionItem) til.next()).getTaxCode().getTaxAgency()
					.getName();
			while (til.hasNext()) {
				if (!(s.equals(((TransactionItem) til.next()).getTaxCode()
						.getTaxAgency().getName())))
					return null;
			}
		}
		return s;
	}

	public WriteCheck testCallCreateWriteCheckVendor(String cm, String date,
			String bank, String vendor, List<TransactionItem> ti, WriteCheck w)
			throws Exception {

		testCallCreateWriteCheck(cm, date, bank, ti, w);
		w.setVendor(accounterDao.getVendor(1L, vendor));
		w.setPayToType(WriteCheck.TYPE_VENDOR);
		double vendorBalance = accounterDao.getVendor(company.getId(),
				w.getVendor().getId()).getBalance();
		accounter.createWriteCheck(w);
		if (checkTesting)
			assertEquals(vendorBalance, accounterDao.getVendor(company.getId(),
					w.getVendor().getId()).getBalance());
		return w;
	}

	public WriteCheck testCallCreateWriteCheckCustomer(String cm, String date,
			String bank, String customer, List<TransactionItem> ti, WriteCheck w)
			throws Exception {

		testCallCreateWriteCheck(cm, date, bank, ti, w);
		w.setCustomer(accounterDao.getCustomer(1L, customer));
		w.setPayToType(WriteCheck.TYPE_CUSTOMER);
		double customerBalance = accounterDao.getCustomer(company.getId(),
				w.getCustomer().getId()).getBalance();
		accounter.createWriteCheck(w);
		if (checkTesting)
			assertEquals(customerBalance, accounterDao.getCustomer(
					company.getId(), w.getCustomer().getId()).getBalance());
		return w;
	}

	public WriteCheck testCallCreateWriteCheckTaxAgency(String cm, String date,
			String bank, List<TransactionItem> ti, WriteCheck w)
			throws Exception {

		testCallCreateWriteCheck(cm, date, bank, ti, w);
		company = accounterDao.getCompany(1L);
		w.setTaxAgency(accounterDao.getTaxAgency(company.getId(),
				getTaxAgencyName(ti)));
		w.setPayToType(WriteCheck.TYPE_TAX_AGENCY);
		double taxAgencyBalance = accounterDao.getTaxAgency(company.getId(),
				w.getTaxAgency().getId()).getBalance();
		accounter.createWriteCheck(w);
		if (checkTesting)
			assertEquals(taxAgencyBalance - w.getTotal(), accounterDao
					.getTaxAgency(company.getId(), w.getTaxAgency().getId())
					.getBalance());

		return w;
	}

	public TaxCode nextTaxCode(ListIterator<TaxCode> tcs, TaxAgency ta) {
		TaxCode tc = null;
		boolean allow = false;
		ListIterator<TaxCode> temp = tcs;
		while (temp.hasNext() && !allow) {
			if (((TaxCode) temp.next()).getTaxAgency().getName().equals(
					ta.getName()))
				allow = true;
		}
		while (temp.hasPrevious() && !allow) {
			if (((TaxCode) temp.previous()).getTaxAgency().getName().equals(
					ta.getName()))
				allow = true;
		}
		while (allow) {
			if (tcs.hasNext()) {
				tc = tcs.next();
			} else {
				while (tcs.hasPrevious())
					tc = tcs.previous();
			}
			if (tc.getTaxAgency().getName().equals(ta.getName()))
				return tc;
		}
		return tc;
	}

	public void testCreateWriteChecks() throws Exception {

		if (createWriteChecks) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			List<TaxCode> codes = accounterDao.getTaxCodes(company.getId());
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getName().equals("None"))
					codes.remove(i);
			}
			ListIterator<TaxCode> tcs = codes.listIterator();

			taxAgencies = new double[codes.size()];
			for (int i = 0; i < taxAgencies.length; i++)
				taxAgencies[getTaxAgencyVariable(codes, codes.get(i)
						.getTaxAgency().getName(), company)] = codes.get(i)
						.getTaxAgency().getBalance();

			setDefaultAccountVariables(accounts, company.getId());

			WriteCheck writeCheck1 = new WriteCheck();
			WriteCheck writeCheck2 = new WriteCheck();
			WriteCheck writeCheck3 = new WriteCheck();
			WriteCheck writeCheck4 = new WriteCheck();
			WriteCheck writeCheck5 = new WriteCheck();
			WriteCheck writeCheck6 = new WriteCheck();
			WriteCheck writeCheck7 = new WriteCheck();
			WriteCheck writeCheck8 = new WriteCheck();
			WriteCheck writeCheck9 = new WriteCheck();
			WriteCheck writeCheck10 = new WriteCheck();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

			// testCallCreateTransactionItem( company in String
			// * TransactionItem type int
			// * quantity in double // 1d for TaxCode
			// * unitprice in double
			// * item in String
			// * WriteChecks
			// * )

			//transactionItems1.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Write off", //
			// writeCheck1));
			//transactionItems2.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Bank Charge", //
			// writeCheck2));
			for (int n = 0; n < (items.size() / 2); n++) {
				Item item = nextItem(its);
				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ITEM, 1D, item
						.getSalesPrice(), item.getName(), writeCheck1));
			}

			for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
				Item item = nextItem(its);
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ITEM, 1D, item
						.getSalesPrice(), item.getName(), writeCheck2));
			}

			for (int n = 0; n < (accounts.size() / 2); n++) {
				Account account = nextAccount(acs);
				transactionItems3.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
						account.getName(), writeCheck3));
			}

			for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++) {
				Account account = nextAccount(acs);
				transactionItems4.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
						account.getName(), writeCheck4));
			}

			List<TaxAgency> agencies = accounterDao.getTaxAgencies(company
					.getId());
			for (int i = 0; i < agencies.size(); i++) {
				if (agencies.get(i).getName().equals("Tax Agency"))
					agencies.remove(i);
			}
			TaxAgency ta = agencies.get(new Random().nextInt(agencies.size()));

			for (int n = 0; n < (codes.size() * 2); n++) {
				TaxCode tc = nextTaxCode(tcs, ta);
				transactionItems5.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_SALESTAX, 1D, 1000d,
						tc.getName(), writeCheck5));
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size() + codes
						.size()) / 4); n++) {
					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						transactionItems6.add(testCallCreateTransactionItem(
								company.getName(), TransactionItem.TYPE_ITEM,
								1D, item.getSalesPrice(), item.getName(),
								writeCheck6));

						item = items.get(new Random().nextInt(items.size()));
						transactionItems7.add(testCallCreateTransactionItem(
								company.getName(), TransactionItem.TYPE_ITEM,
								1D, item.getSalesPrice(), item.getName(),
								writeCheck7));

						item = items.get(new Random().nextInt(items.size()));
						transactionItems8.add(testCallCreateTransactionItem(
								company.getName(), TransactionItem.TYPE_ITEM,
								1D, item.getSalesPrice(), item.getName(),
								writeCheck8));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						transactionItems6.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), writeCheck6));

						account = getRandomAccount(accounts);
						transactionItems7.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), writeCheck7));

						account = getRandomAccount(accounts);
						transactionItems8.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), writeCheck8));
					}
				}

				ta = agencies.get(new Random().nextInt(agencies.size()));
				TaxAgency ta2 = agencies.get(new Random().nextInt(agencies
						.size()));
				for (int n = 0; n < (codes.size()); n++) {
					TaxCode tc = nextTaxCode(tcs, ta);
					transactionItems9.add(testCallCreateTransactionItem(company
							.getName(), TransactionItem.TYPE_SALESTAX, 1D,
							1000D, tc.getName(), writeCheck9));

					tc = nextTaxCode(tcs, ta2);
					transactionItems10.add(testCallCreateTransactionItem(
							company.getName(), TransactionItem.TYPE_SALESTAX,
							1D, 1000D, tc.getName(), writeCheck10));
				}
			}

			ArrayList<WriteCheck> wcs = new ArrayList<WriteCheck>();

			// testCallCreateWriteCheckVendor( company in long
			// type in int,
			// date in String
			// Bank Account in String
			// Vendor in String
			// transactinitems set
			// amount in double
			// Write Checks

			List<Account> payFromAccounts = Utility.getPayFromAccounts(company);

			// wcs.add(testCallCreateWriteCheckVendor(company.getName(), //
			// today(), "Account Credit Card", "Vendor1", transactionItems1, //
			// writeCheck1));
			// wcs.add(testCallCreateWriteCheckVendor(company.getName(), //
			// today(), "Account Cash", "Vendor2", transactionItems2, //
			// writeCheck2));

			if (transactionItems1.size() > 0)
				wcs.add(testCallCreateWriteCheckVendor(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Vendor1", transactionItems1,
						writeCheck1));

			if (transactionItems2.size() > 0)
				wcs.add(testCallCreateWriteCheckCustomer(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Customer1", transactionItems2,
						writeCheck2));

			if (transactionItems3.size() > 0)
				wcs.add(testCallCreateWriteCheckVendor(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Vendor2", transactionItems3,
						writeCheck3));

			if (transactionItems4.size() > 0)
				wcs.add(testCallCreateWriteCheckCustomer(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Customer2", transactionItems4,
						writeCheck4));

			if (transactionItems5.size() > 0)
				wcs.add(testCallCreateWriteCheckTaxAgency(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems5, writeCheck5));

			if (transactionItems6.size() > 0)
				wcs.add(testCallCreateWriteCheckCustomer(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Customer3", transactionItems6,
						writeCheck6));

			if (transactionItems7.size() > 0)
				wcs.add(testCallCreateWriteCheckCustomer(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Customer4", transactionItems7,
						writeCheck7));

			if (transactionItems8.size() > 0)
				wcs.add(testCallCreateWriteCheckVendor(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Vendor3", transactionItems8,
						writeCheck8));

			if (transactionItems9.size() > 0)
				wcs.add(testCallCreateWriteCheckVendor(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), "Vendor3", transactionItems9,
						writeCheck9));

			if (transactionItems10.size() > 0)
				wcs.add(testCallCreateWriteCheckTaxAgency(company.getName(),
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems10, writeCheck10));

			if (checkTesting) {
				// testing of this transaction starts from here........

				Iterator<WriteCheck> i = wcs.iterator();
				company = accounterDao.getCompany(1L);
				WriteCheck c;

				while (i.hasNext()) {
					c = (WriteCheck) i.next();
					List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
					assertNotNull(c);
					assertEquals(c.getType(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getType());
					assertEquals(c.getPayToType(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getPayToType());
					if (c.getPayToType() == WriteCheck.TYPE_VENDOR) {
						assertNotNull(c.getVendor());
						assertNotNull(c.getVendor().getName());
						assertEquals(c.getVendor().getName(), accounterDao
								.getwriterCheck(company.getId(), c.getId())
								.getVendor().getName());
					} else if (c.getPayToType() == WriteCheck.TYPE_CUSTOMER) {
						assertNotNull(c.getCustomer());
						assertNotNull(c.getCustomer().getName());
						assertEquals(c.getCustomer().getName(), accounterDao
								.getwriterCheck(company.getId(), c.getId())
								.getCustomer().getName());
					} else {
						assertNotNull(c.getTaxAgency());
						assertNotNull(c.getTaxAgency().getName());
						assertEquals(c.getTaxAgency().getName(), accounterDao
								.getwriterCheck(company.getId(), c.getId())
								.getTaxAgency().getName());
					}
					assertEquals(c.getDate(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getDate());
					assertEquals(c.getDate(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getDate());
					assertEquals(c.getTotal(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getType(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getType());
					assertEquals(c.getBankAccount().getName(), accounterDao
							.getwriterCheck(company.getId(), c.getId())
							.getBankAccount().getName());
					assertEquals(c.getBalance(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getBalance());
					assertEquals(c.getNumber(), accounterDao.getwriterCheck(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// writecheck and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM) {
							if (c.getPayToType() == WriteCheck.TYPE_VENDOR)
								acc = it.getItem().getExpenseAccount();
							if (c.getPayToType() == WriteCheck.TYPE_CUSTOMER)
								acc = it.getItem().getIncomeAccount();
						} else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else if (it.getType() == TransactionItem.TYPE_SALESTAX)
							acc = it.getTaxCode().getTaxAgency()
									.getLiabilityAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase
						if (it.getType() == TransactionItem.TYPE_SALESTAX
								&& c.getPayToType() == WriteCheck.TYPE_CUSTOMER)
							taxAgencies[getTaxAgencyVariable(codes, it
									.getTaxCode().getTaxAgency().getName(),
									company)] -= it.getLineTotal();

					}// while there is a transaction item

					if (c.getPayToType() == WriteCheck.TYPE_TAX_AGENCY)
						taxAgencies[getTaxAgencyVariable(codes, c
								.getTaxAgency().getName(), company)] -= c
								.getTotal();

					// checking whether the Bank account is updated. If it has a
					// parent it also need to be updated
					if (c.getBankAccount().isIncrease()) {
						d[getAccountVariable(accounts, c.getBankAccount()
								.getName(), company)] += c.getTotal();
						String str = c.getBankAccount().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, c.getBankAccount()
								.getName(), company)] -= c.getTotal();
						String str = c.getBankAccount().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

					// If the last transaction item of the last CashSale is
					// done, we
					// need to check the accounts table whether all the accounts
					// are
					// updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());

						for (long l1 = 0; l1 < codes.size(); l1++) {
							String name = codes.get((int) l1).getTaxAgency()
									.getName();
							assertEquals(name,
									taxAgencies[getTaxAgencyVariable(codes,
											name, company)],
									accounterDao.getTaxAgency(company.getId(),
											name).getBalance());
						}
					}
				}
			}
		}
	}

	public MakeDeposit testCallCreateMakeDeposit(long cm, String depIn,
			String date, List<TransactionMakeDeposit> ti,
			String cashBackAccount, double cashBackAmount, MakeDeposit m)
			throws Exception {

		m.setType(Transaction.TYPE_MAKE_DEPOSIT);
		m.setNumber(accounterGUIDao.getNextTransactionNumber(cm,
				Transaction.TYPE_MAKE_DEPOSIT));
		m.setTransactionMakeDeposit(ti);
		m.setTotal(getAmountTotal(ti));
		m.setCashBackAccount(accounterDao.getAccount(cm, cashBackAccount));
		m.setCashBackAmount(cashBackAmount);
		m.setTotal(m.getTotal() - cashBackAmount);
		m.setDate(format.parse(date));
		m.setCompany(accounterDao.getCompany(1L));
		m.setDepositIn(accounterDao.getAccount(cm, depIn));
		accounter.createMakeDeposit(m);
		return m;
	}

	public TransactionMakeDeposit testCallCreateTransactionItem(
			MakeDepositTransactionsList md, long cm, MakeDeposit m)
			throws DAOException {

		company = accounterDao.getCompany(1L);
		TransactionMakeDeposit entry = new TransactionMakeDeposit();
		entry.setIsNewEntry(false);
		entry.setCashAccount(accounterDao.getAccount(company.getId(),
				AccounterConstants.UN_DEPOSITED_FUNDS));
		entry.setAccount(accounterDao.getAccount(company.getId(),
				AccounterConstants.UN_DEPOSITED_FUNDS));
		entry.setAmount(md.getAmount());
		entry.setMakeDeposit(m);
		entry.setType(md.getType());
		switch (md.getTransactionType()) {

		case Transaction.TYPE_CASH_PURCHASE:
			entry.setDepositedTransaction(accounterDao.getCashPurchase(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_CASH_SALES:
			entry.setDepositedTransaction(accounterDao.getCashSales(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
			entry.setDepositedTransaction(accounterDao.getCreditCardCharge(
					company.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			entry.setDepositedTransaction(accounterDao.getCustomerCreditMemo(
					company.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_CUSTOMER_REFUNDS:
			entry.setDepositedTransaction(accounterDao.getCustomerRefunds(
					company.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_ENTER_BILL:
			entry.setDepositedTransaction(accounterDao.getEnterBill(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_ESTIMATE:
			entry.setDepositedTransaction(accounterDao.getEstimate(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_INVOICE:
			entry.setDepositedTransaction(accounterDao.getInvoice(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_ISSUE_PAYMENT:
			entry.setDepositedTransaction(accounterDao.getIssuePayment(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_PAY_BILL:
			entry.setDepositedTransaction(accounterDao.getPayBill(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_PAY_SALES_TAX:
			entry.setDepositedTransaction(accounterDao.getPaySalesTax(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_RECEIVE_PAYMENT:
			entry.setDepositedTransaction(accounterDao.getReceivePayment(
					company.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_TRANSFER_FUND:
			entry.setDepositedTransaction(accounterDao.getTransferFund(company
					.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			entry.setDepositedTransaction(accounterDao.getVendorCreditMemo(
					company.getId(), md.getTransactionId()));
			break;
		case Transaction.TYPE_WRITE_CHECK:
			entry.setDepositedTransaction(accounterDao.getwriterCheck(company
					.getId(), md.getTransactionId()));
			break;

		}
		return entry;
	}

	public TransactionMakeDeposit testCallCreateNewTransactionCustomer(long cm,
			int type, String cust, double amount, MakeDeposit m)
			throws DAOException {
		TransactionMakeDeposit e = new TransactionMakeDeposit();
		e.setIsNewEntry(true);
		e.setType(type);
		e.setCustomer(accounterDao.getCustomer(cm, cust));
		e.setAmount(amount);
		e.setMakeDeposit(m);
		return e;
	}

	public TransactionMakeDeposit testCallCreateNewTransactionVendor(long cm,
			int type, String vendor, double amount, MakeDeposit m)
			throws DAOException {
		TransactionMakeDeposit e = new TransactionMakeDeposit();
		e.setIsNewEntry(true);
		e.setType(type);
		e.setVendor(accounterDao.getVendor(cm, vendor));
		e.setAmount(amount);
		e.setMakeDeposit(m);
		return e;
	}

	public TransactionMakeDeposit testCallCreateNewTransactionFinancialAccount(
			long cm, int type, String account, double amount, MakeDeposit m)
			throws DAOException {
		TransactionMakeDeposit e = new TransactionMakeDeposit();
		e.setIsNewEntry(true);
		e.setType(type);
		e.setAccount(accounterDao.getAccount(cm, account));
		e.setAmount(amount);
		e.setMakeDeposit(m);
		return e;
	}

	public double getAmountTotal(List<TransactionMakeDeposit> e) {
		double total = 0.0;
		Iterator<TransactionMakeDeposit> i = e.iterator();
		while (i.hasNext()) {
			TransactionMakeDeposit entry = (TransactionMakeDeposit) i.next();
			total += entry.getAmount();
		}
		return total;
	}

	public void testCreateMakeDeposit() throws Exception {

		if (createMakeDeposits) {
			company = accounterDao.getCompany(1L);

			List<Customer> customers = accounterDao.getCustomers(company
					.getId());
			System.out.println("Accounts Payable balance ="
					+ accounterDao.getAccount(company.getId(),
							"Accounts Payable").getTotalBalance());
			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			List<Vendor> vendors = accounterDao.getVendors(company.getId());

			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.
			setDefaultAccountVariables(accounts, company.getId());

			MakeDeposit makeDeposit1 = new MakeDeposit();
			MakeDeposit makeDeposit2 = new MakeDeposit();
			MakeDeposit makeDeposit3 = new MakeDeposit();
			MakeDeposit makeDeposit4 = new MakeDeposit();
			MakeDeposit makeDeposit5 = new MakeDeposit();
			MakeDeposit makeDeposit6 = new MakeDeposit();
			MakeDeposit makeDeposit7 = new MakeDeposit();
			MakeDeposit makeDeposit8 = new MakeDeposit();
			MakeDeposit makeDeposit9 = new MakeDeposit();
			MakeDeposit makeDeposit10 = new MakeDeposit();

			System.out.println(" ap = "
					+ accounterDao.getAccount(company.getId(),
							"Accounts Payable").getTotalBalance());

			List<TransactionMakeDeposit> entries1 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries2 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries3 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries4 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries5 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries6 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries7 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries8 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries9 = new ArrayList<TransactionMakeDeposit>();
			List<TransactionMakeDeposit> entries10 = new ArrayList<TransactionMakeDeposit>();

			List<MakeDepositTransactionsList> mdtlist = accounterGUIDao
					.getTransactionMakeDeposits(company.getId());
			// assertEquals(mdtlist.size(),3);
			Iterator<MakeDepositTransactionsList> itr = mdtlist.iterator();

			// testCallCreateTransactionItem( isNewEntry boolean
			// amount double7
			// cashAccountID long
			// MakeDeposit
			// )

			// testCallCreateNewTransactionItemXXXXXXXX( company in long
			// type in int
			// Customer/Vendor/Account in String
			// amount in double

			// entries1.add(testCallCreateNewTransactionFinancialAccount(1l, //
			// TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT, "Bank Charge", //
			// 1000d, makeDeposit1));
			// entries2.add(testCallCreateNewTransactionFinancialAccount(1l, //
			// TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT, "Write off", //
			// 1000d, makeDeposit2));

			for (int n = 0; n < 2; n++) {
				if (itr.hasNext()) {
					MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
							.next();
					entries1.add(testCallCreateTransactionItem(md, 1l,
							makeDeposit1));
				}

				if (itr.hasNext()) {
					MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
							.next();
					entries2.add(testCallCreateTransactionItem(md, 1l,
							makeDeposit2));
				}

				if (itr.hasNext()) {
					MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
							.next();
					entries3.add(testCallCreateTransactionItem(md, 1l,
							makeDeposit3));
				}

				if (itr.hasNext()) {
					MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
							.next();
					entries4.add(testCallCreateTransactionItem(md, 1l,
							makeDeposit4));
				}

				if (createMore) {
					if (itr.hasNext()) {
						MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
								.next();
						entries5.add(testCallCreateTransactionItem(md, 1l,
								makeDeposit5));
					}
					if (itr.hasNext()) {
						MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
								.next();
						entries6.add(testCallCreateTransactionItem(md, 1l,
								makeDeposit6));
					}
					if (itr.hasNext()) {
						MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
								.next();
						entries7.add(testCallCreateTransactionItem(md, 1l,
								makeDeposit7));
					}
					if (itr.hasNext()) {
						MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
								.next();
						entries8.add(testCallCreateTransactionItem(md, 1l,
								makeDeposit8));
					}
					if (itr.hasNext()) {
						MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
								.next();
						entries9.add(testCallCreateTransactionItem(md, 1l,
								makeDeposit9));
					}
					if (itr.hasNext()) {
						MakeDepositTransactionsList md = (MakeDepositTransactionsList) itr
								.next();
						entries10.add(testCallCreateTransactionItem(md, 1l,
								makeDeposit10));
					}
				}
			}

			for (int n = 0; n < customers.size(); n++)
				entries1.add(testCallCreateNewTransactionCustomer(1l,
						TransactionMakeDeposit.TYPE_CUSTOMER, customers.get(
								new Random().nextInt(customers.size()))
								.getName(), 1000d, makeDeposit1));

			for (int n = 0; n < vendors.size(); n++)
				entries2.add(testCallCreateNewTransactionVendor(1l,
						TransactionMakeDeposit.TYPE_VENDOR,
						vendors.get(new Random().nextInt(vendors.size()))
								.getName(), 1000d, makeDeposit2));

			for (int n = 0; n < accounts.size(); n += 3) {
				entries3.add(testCallCreateNewTransactionFinancialAccount(1l,
						TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
						nextMakeDepositAccount(acs).getName(), 1000d,
						makeDeposit3));
				entries4.add(testCallCreateNewTransactionFinancialAccount(1l,
						TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
						nextMakeDepositAccount(acs).getName(), 1000d,
						makeDeposit4));
				entries5.add(testCallCreateNewTransactionFinancialAccount(1l,
						TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
						nextMakeDepositAccount(acs).getName(), 1000d,
						makeDeposit5));
			}

			if (createMore) {
				for (int n = 0; n < (accounts.size() + vendors.size() + customers
						.size()) / 3; n += 3) {

					mdtlist = accounterGUIDao
							.getTransactionMakeDeposits(company.getId());
					if (mdtlist.size() > 0)
						entries6.add(testCallCreateTransactionItem(mdtlist
								.get(new Random().nextInt(mdtlist.size())), 1l,
								makeDeposit6));

					mdtlist = accounterGUIDao
							.getTransactionMakeDeposits(company.getId());
					if (mdtlist.size() > 0)
						entries7.add(testCallCreateTransactionItem(mdtlist
								.get(new Random().nextInt(mdtlist.size())), 1l,
								makeDeposit7));

					mdtlist = accounterGUIDao
							.getTransactionMakeDeposits(company.getId());
					if (mdtlist.size() > 0)
						entries8.add(testCallCreateTransactionItem(mdtlist
								.get(new Random().nextInt(mdtlist.size())), 1l,
								makeDeposit8));

					mdtlist = accounterGUIDao
							.getTransactionMakeDeposits(company.getId());
					if (mdtlist.size() > 0)
						entries9.add(testCallCreateTransactionItem(mdtlist
								.get(new Random().nextInt(mdtlist.size())), 1l,
								makeDeposit9));

					mdtlist = accounterGUIDao
							.getTransactionMakeDeposits(company.getId());
					if (mdtlist.size() > 0)
						entries10.add(testCallCreateTransactionItem(mdtlist
								.get(new Random().nextInt(mdtlist.size())), 1l,
								makeDeposit10));

					entries6.add(testCallCreateNewTransactionCustomer(1l,
							TransactionMakeDeposit.TYPE_CUSTOMER,
							customers.get(
									new Random().nextInt(customers.size()))
									.getName(), 1000d, makeDeposit6));
					entries6.add(testCallCreateNewTransactionVendor(1l,
							TransactionMakeDeposit.TYPE_VENDOR, vendors.get(
									new Random().nextInt(vendors.size()))
									.getName(), 1000d, makeDeposit6));

					entries7.add(testCallCreateNewTransactionVendor(1l,
							TransactionMakeDeposit.TYPE_VENDOR, vendors.get(
									new Random().nextInt(vendors.size()))
									.getName(), 1000d, makeDeposit7));
					entries7.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit7));

					entries8.add(testCallCreateNewTransactionCustomer(1l,
							TransactionMakeDeposit.TYPE_CUSTOMER,
							customers.get(
									new Random().nextInt(customers.size()))
									.getName(), 1000d, makeDeposit8));
					entries8.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit8));

					entries9.add(testCallCreateNewTransactionCustomer(1l,
							TransactionMakeDeposit.TYPE_CUSTOMER,
							customers.get(
									new Random().nextInt(customers.size()))
									.getName(), 1000d, makeDeposit9));
					entries9.add(testCallCreateNewTransactionVendor(1l,
							TransactionMakeDeposit.TYPE_VENDOR, vendors.get(
									new Random().nextInt(vendors.size()))
									.getName(), 1000d, makeDeposit9));
					entries9.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit9));
					entries9.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit9));
					entries9.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit9));

					entries10.add(testCallCreateNewTransactionCustomer(1l,
							TransactionMakeDeposit.TYPE_CUSTOMER,
							customers.get(
									new Random().nextInt(customers.size()))
									.getName(), 1000d, makeDeposit10));
					entries10.add(testCallCreateNewTransactionVendor(1l,
							TransactionMakeDeposit.TYPE_VENDOR, vendors.get(
									new Random().nextInt(vendors.size()))
									.getName(), 1000d, makeDeposit10));
					entries10.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit10));
					entries10.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit10));
					entries10.add(testCallCreateNewTransactionFinancialAccount(
							1l, TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT,
							getRandomMakeDepositAccount(accounts).getName(),
							1000d, makeDeposit10));

				}
			}
			ArrayList<MakeDeposit> mds = new ArrayList<MakeDeposit>();

			// testCallCreateMakeDeposit( company in long
			// Deposit In in Stirng
			// Date in string
			// TransactionMakeDeposit List
			// total in double
			// CashbackAccount in String
			// CashBackAmount in double
			// MakeDeposit
			// )

			List<Account> bankAccounts = Utility.getBankAccounts(company);
			List<Account> cashBackAccounts = Utility
					.getCashBackAccounts(company);
			// mds.add(testCallCreateMakeDeposit(1l, "Account Bank", today(), //
			// entries1, "Cash Discount taken", 500d, makeDeposit1));
			// mds.add(testCallCreateMakeDeposit(1l, "Account Bank", today(), //
			// entries2, "Cash Discount Given", 500d, makeDeposit2));

			if (entries1.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries1, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit1));

			if (entries2.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries2, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit2));

			if (entries3.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries3, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit3));

			if (entries4.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries4, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit4));

			if (entries5.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries5, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit5));

			if (entries6.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries6, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit6));

			if (entries7.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries7, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit7));

			if (entries8.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries8, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit8));

			if (entries9.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries9, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit9));

			if (entries10.size() > 0)
				mds.add(testCallCreateMakeDeposit(1l, bankAccounts.get(
						new Random().nextInt(bankAccounts.size())).getName(),
						today(), entries10, cashBackAccounts.get(
								new Random().nextInt(cashBackAccounts.size()))
								.getName(), 0d, makeDeposit10));

			if (checkTesting) {
				// testing of this transaction starts from here........

				Iterator<MakeDeposit> i = mds.iterator();
				MakeDeposit c;

				while (i.hasNext()) {
					c = (MakeDeposit) i.next();
					List<TransactionMakeDeposit> ti;
					List<TransactionMakeDeposit> ti2 = new ArrayList<TransactionMakeDeposit>();
					assertNotNull(c);
					assertEquals(c.getType(), accounterDao.getMakeDeposit(
							company.getId(), c.getId()).getType());
					assertEquals(c.getCashBackAccount().getName(), accounterDao
							.getMakeDeposit(company.getId(), c.getId())
							.getCashBackAccount().getName());
					assertEquals(c.getCashBackAmount(), accounterDao
							.getMakeDeposit(company.getId(), c.getId())
							.getCashBackAmount());
					assertEquals(c.getDate(), accounterDao.getMakeDeposit(
							company.getId(), c.getId()).getDate());
					assertEquals(c.getTotal(), accounterDao.getMakeDeposit(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getDepositIn().getName(), accounterDao
							.getMakeDeposit(company.getId(), c.getId())
							.getDepositIn().getName());
					assertEquals(c.getTotal(), accounterDao.getMakeDeposit(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getNumber(), accounterDao.getMakeDeposit(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// writecheck and store them in a List.
					ti = c.getTransactionMakeDeposit();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionMakeDeposit> j = ti.iterator();

					Account acc = null;
					while (j.hasNext()) {// while there is a transaction item
						TransactionMakeDeposit it = (TransactionMakeDeposit) j
								.next();
						ti2.add(it);
						if (it.getIsNewEntry()) {
							if (it.getType() == TransactionMakeDeposit.TYPE_CUSTOMER)
								acc = accounterDao.getAccount(company.getId(),
										"Accounts Receivable");
							else if (it.getType() == TransactionMakeDeposit.TYPE_VENDOR)
								acc = accounterDao.getAccount(company.getId(),
										"Accounts Payable");
							else
								acc = it.getAccount();
						} else
							acc = accounterDao.getAccount(company.getId(),
									"Un Deposited Funds");

						if (acc.isIncrease()
								|| acc.getName().equals("Accounts Payable")) {
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getAmount();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getAmount();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						} else {
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getAmount();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getAmount();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}

					}// while there is a transaction item

					d[getAccountVariable(accounts, c.getDepositIn().getName(),
							company)] += c.getTotal();
					// checking whether the depositIn account is updated. If it
					// has
					// a parent it also need to be updated
					acc = c.getCashBackAccount();
					if (acc.isIncrease()) {

						d[getAccountVariable(accounts, acc.getName(), company)] -= c
								.getCashBackAmount();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getCashBackAmount();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

					else {
						d[getAccountVariable(accounts, acc.getName(), company)] += c
								.getCashBackAmount();
						String str = acc.getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getCashBackAmount();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

					// If the last transaction item of the last CashSale is
					// done, we
					// need to check the accounts table whether all the accounts
					// are
					// updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
					}
				}
			}
		}
	}

	public double getAmountUnUsedCredits(Customer customer) throws DAOException {
		List<CreditsAndPayments> cpl = accounterGUIDao
				.getCustomerCreditsAndPayments(company.getId(), customer
						.getId());
		Iterator<CreditsAndPayments> i = cpl.iterator();
		double d = 0.0;
		assertNotNull(cpl);

		while (i.hasNext()) {
			CreditsAndPayments cp = (CreditsAndPayments) i.next();
			d += cp == null ? 0.0 : cp.getCreditAmount();
		}

		return d;
	}

	public double getAmountTotalAppliedCredits(List<TransactionReceivePayment> t) {
		double d = 0.0;

		Iterator<TransactionReceivePayment> i = t.iterator();

		while (i.hasNext()) {
			TransactionReceivePayment tp = (TransactionReceivePayment) i.next();
			d += tp == null ? 0.0 : tp.getAppliedCredits();
		}

		return d;
	}

	public double getAmountTotalCashDiscount(List<TransactionReceivePayment> t) {
		double d = 0.0;

		Iterator<TransactionReceivePayment> i = t.iterator();
		while (i.hasNext()) {
			TransactionReceivePayment tp = (TransactionReceivePayment) i.next();
			d += tp == null ? 0.0 : tp.getCashDiscount();
		}

		return d;
	}

	public double getAmountTotalWriteOff(List<TransactionReceivePayment> t) {
		double d = 0.0;

		Iterator<TransactionReceivePayment> i = t.iterator();
		while (i.hasNext()) {
			TransactionReceivePayment tp = (TransactionReceivePayment) i.next();
			d += tp == null ? 0.0 : tp.getWriteOff();
		}

		return d;
	}

	public double getAmountTotalPayments(List<TransactionReceivePayment> t) {
		double d = 0.0;

		Iterator<TransactionReceivePayment> i = t.iterator();
		while (i.hasNext()) {
			TransactionReceivePayment tp = (TransactionReceivePayment) i.next();
			d += tp == null ? 0.0 : tp.getPayment();
		}

		return d;
	}

	public double getAmountTotalPayments(Set<TransactionReceivePayment> t) {
		double d = 0.0;

		Iterator<TransactionReceivePayment> i = t.iterator();
		while (i.hasNext()) {
			TransactionReceivePayment tp = (TransactionReceivePayment) i.next();
			d += tp == null ? 0.0 : tp.getPayment();
		}

		return d;
	}

	public ReceivePayment testCallCreateReceivePayment(long cm,
			String customer, String depIn, String date, String paymethod,
			double amount, List<TransactionReceivePayment> tr, ReceivePayment r)
			throws Exception {
		company = accounterDao.getCompany(1l);
		r.setNumber(accounterGUIDao.getNextTransactionNumber(cm,
				Transaction.TYPE_RECEIVE_PAYMENT));
		r.setType(Transaction.TYPE_RECEIVE_PAYMENT);
		r.setCustomer(accounterDao.getCustomer(cm, customer));
		r.setCompany(accounterDao.getCompany(1L));
		r.setDepositIn(accounterDao.getAccount(cm, depIn));
		r.setDate(format.parse(date));
		r.setPaymentMethod(paymethod);
		r.setUnUsedCredits(getAmountUnUsedCredits(accounterDao.getCustomer(
				company.getId(), customer)));
		r.setTotalAppliedCredits(getAmountTotalAppliedCredits(tr));
		r.setTotalCashDiscount(getAmountTotalCashDiscount(tr));
		r.setTotalWriteOff(getAmountTotalWriteOff(tr));
		r.setTotal(getAmountTotalPayments(tr));
		r.setAmount(amount + r.getTotal());
		r.setUnUsedPayments(r.getAmount() - r.getTotal());
		r.setTransactionReceivePayment(tr);
		double customerBalance = accounterDao.getCustomer(company.getId(),
				r.getCustomer().getId()).getBalance();
		accounter.createReceivePayment(r);
		if (checkTesting)
			assertEquals(customerBalance
					- (r.getTotal() + r.getTotalCashDiscount() + r
							.getTotalWriteOff()), accounterDao.getCustomer(
					company.getId(), r.getCustomer().getId()).getBalance());
		return r;
	}

	public Object getLastTransaction(int n, int c,
			Iterator<ReceivePaymentTransactionList> i) {
		Object o = null;
		Iterator<ReceivePaymentTransactionList> k = i;
		c = c > n ? c - n : 1;
		if (k.hasNext()) {
			while (k.hasNext()) {
				c--;
				o = k.next();
				if (c == 0)
					break;
			}
		}

		return o;
	}

	public double testCallCreateTransactionCreditsAndPayments(
			boolean useFullCredits, long cu, Company company, double amtToUse,
			TransactionReceivePayment p,
			List<TransactionCreditsAndPayments> creditsAndPayments)
			throws DAOException {
		boolean flag = true;
		List<CreditsAndPayments> cpl = accounterGUIDao
				.getCustomerCreditsAndPayments(company.getId(), cu);
		Iterator<CreditsAndPayments> i = cpl.iterator();
		assertNotNull(cpl);

		while (i.hasNext() && flag) {
			CreditsAndPayments cp = (CreditsAndPayments) i.next();
			TransactionCreditsAndPayments c = new TransactionCreditsAndPayments();
			c.setMemo(cp.getMemo());
			c
					.setAmountToUse(useFullCredits ? cp.getCreditAmount()
							: (amtToUse > cp.getCreditAmount()) ? (amtToUse - (amtToUse -= cp
									.getCreditAmount()))
									: amtToUse);
			c.setTransactionReceivePayment(p);
			c.setCreditsAndPayments(accounterDao.getCreditAndPayment(company
					.getId(), cp.getId()));
			creditsAndPayments.add(c);
			if (amtToUse <= cp.getCreditAmount())
				flag = false;
			if (!i.hasNext() && flag && !useFullCredits)
				return amtToUse;
		}
		return 0.0;
	}

	public TransactionReceivePayment testCallCreateTransactionReceivePayment(
			long cm, String customer, double discountAmount,
			String discountAccount, String writeoff, double writeOffAmount,
			boolean useFullCredits, double creditAmountToUse, double payment,
			int noOfEntries, boolean addCreditToPayment, ReceivePayment r)
			throws DAOException {

		company = accounterDao.getCompany(1L);

		List<ReceivePaymentTransactionList> rpl = accounterGUIDao
				.getTransactionReceivePayments(company.getId(), accounterDao
						.getCustomer(company.getId(), customer).getId());
		if (rpl.size() > 0) {
			if (noOfEntries <= 0)
				noOfEntries = 1;

			ReceivePaymentTransactionList rp = (ReceivePaymentTransactionList) getLastTransaction(
					noOfEntries, rpl.size(), rpl.iterator());
			assertNotNull(rp);
			TransactionReceivePayment p = new TransactionReceivePayment();
			p.setReceivePayment(r);
			if (rp.getType() == Transaction.TYPE_INVOICE) {
				p.setInvoice(accounterDao.getInvoice(company.getId(), rp
						.getTransactionId()));
				p.isInvoice = true;
			} else {
				p.setCustomerRefund(accounterDao.getCustomerRefunds(company
						.getId(), rp.getTransactionId()));
				p.isInvoice = false;
			}
			p
					.setInvoiceAmount(rp.getType() == Transaction.TYPE_INVOICE ? accounterDao
							.getInvoice(company.getId(), rp.getTransactionId())
							.getTotal()
							: accounterDao.getCustomerRefunds(company.getId(),
									rp.getTransactionId()).getTotal());
			if (discountAccount != null) {
				p.setDiscountAccount(accounterDao.getAccount(company.getId(),
						discountAccount));
				p.setCashDiscount(discountAmount);
			} else {
				p.setDiscountAccount(null);
				p.setCashDiscount(0D);
			}
			if (writeoff != null) {
				p.setWriteOffAccount(accounterDao.getAccount(company.getId(),
						writeoff));
				p.setWriteOff(writeOffAmount);
			} else {
				p.setWriteOffAccount(null);
				p.setWriteOff(0D);
			}

			// testCallCreateTransactionCreditsAndPayments( useFullCredits in
			// boolean //If true, full credits are used.
			// customer in long,
			// company in Company
			// amount to use in double
			// TransactionReceivePayment object
			// TransactionCreditsAndPayments List
			// ) returns TransactionCreditsAndPayments List object

			List<TransactionCreditsAndPayments> creditsAndPayments = new ArrayList<TransactionCreditsAndPayments>();

			double d = testCallCreateTransactionCreditsAndPayments(
					useFullCredits, accounterDao.getCustomer(company.getId(),
							customer).getId(), company, creditAmountToUse, p,
					creditsAndPayments);

			if (addCreditToPayment)
				payment += d;

			p.setTransactionCreditsAndPayments(creditsAndPayments);
			p.setPayment(payment);
			p.setAppliedCredits(creditAmountToUse - d);
			return p;
		}
		return null;

	}

	public void testCreateReceivePayment() throws Exception {

		if (createReceivePayments && (createInvoices || createCustomerRefunds)) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Account> accounts = accounterDao.getAccounts(company.getId());

			setDefaultAccountVariables(accounts, company.getId());

			ReceivePayment receivePayment1 = new ReceivePayment();
			ReceivePayment receivePayment2 = new ReceivePayment();
			ReceivePayment receivePayment3 = new ReceivePayment();
			ReceivePayment receivePayment4 = new ReceivePayment();
			ReceivePayment receivePayment5 = new ReceivePayment();
			ReceivePayment receivePayment6 = new ReceivePayment();
			ReceivePayment receivePayment7 = new ReceivePayment();
			ReceivePayment receivePayment8 = new ReceivePayment();
			ReceivePayment receivePayment9 = new ReceivePayment();
			ReceivePayment receivePayment10 = new ReceivePayment();

			List<TransactionReceivePayment> transactionReceivePayment1 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment2 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment3 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment4 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment5 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment6 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment7 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment8 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment9 = new ArrayList<TransactionReceivePayment>();
			List<TransactionReceivePayment> transactionReceivePayment10 = new ArrayList<TransactionReceivePayment>();

			// testCallCreateTransactionReceivePayment( company in long
			// * Customer in String
			// * CashDiscountAmount in double
			// * DiscountAccount in String
			// * Write off Account in String
			// * Write off amount in double
			// UseFull Credits in boolean// if this is true, it wont bother
			// about the creditAmtToUse
			// and the no of entries in the credits transaction list.
			// and addXtraCreditToPayment value.
			// * Credit Amount to use in double
			//
			// * Payments in double //This is default of amountdue -
			// discount+writeoff+credits, // * if it is given as negative then
			// default values
			// * will be set.
			// * No of entries to use
			// * in credits&payments
			// transaction list in int // if max value exceeds it takes max
			// value and no negative value
			// from last
			//		  
			// * AddExtraCredit in boolean,// true to use shorted credits to
			// payment
			// * AmountToPayment and false to not to use.
			// *
			// * ReceivePayment object

			// transactionReceivePayment1.add(
			// testCallCreateTransactionReceivePayment(1l, "Customer1", 1000D,
			// // // null, null, 500d, false, 200d, 500d, 1, false, //
			// receivePayment1));
			// transactionReceivePayment2.add(
			// testCallCreateTransactionReceivePayment(1l, "Customer1", 1000D,
			// // // "Bank Charge", "Cash Discount taken", 500d, false, 200d,
			// 500d, // 1, // false, receivePayment2));

			{
				TransactionReceivePayment trp = testCallCreateTransactionReceivePayment(
						1l, "Customer1", 100.0 * (new Random().nextInt(10)),
						getRandomAccount(accounts).getName(), getRandomAccount(
								accounts).getName(), 100.0 * (new Random()
								.nextInt(10)), false, 100.0 * (new Random()
								.nextInt(10)), 500.0 * (new Random()
								.nextInt(10)), 1, false, receivePayment1);
				if (trp != null)
					transactionReceivePayment1.add(trp);

				trp = testCallCreateTransactionReceivePayment(1l, "Customer2",
						100.0 * (new Random().nextInt(10)), null, null,
						100.0 * (new Random().nextInt(10)), false,
						100.0 * (new Random().nextInt(10)),
						500.0 * (new Random().nextInt(10)), 1, false,
						receivePayment2);
				if (trp != null)
					transactionReceivePayment2.add(trp);

				trp = testCallCreateTransactionReceivePayment(1l, "Customer3",
						100.0 * (new Random().nextInt(10)), getRandomAccount(
								accounts).getName(), getRandomAccount(accounts)
								.getName(), 100.0 * (new Random().nextInt(10)),
						false, 100.0 * (new Random().nextInt(10)),
						500.0 * (new Random().nextInt(10)), 1, false,
						receivePayment3);
				if (trp != null)
					transactionReceivePayment3.add(trp);

				trp = testCallCreateTransactionReceivePayment(1l, "Customer4",
						100.0 * (new Random().nextInt(10)), null, null,
						100.0 * (new Random().nextInt(10)), false,
						100.0 * (new Random().nextInt(10)),
						500.0 * (new Random().nextInt(10)), 1, false,
						receivePayment4);
				if (trp != null)
					transactionReceivePayment4.add(trp);

				trp = testCallCreateTransactionReceivePayment(1l, "Customer5",
						100.0 * (new Random().nextInt(10)), getRandomAccount(
								accounts).getName(), getRandomAccount(accounts)
								.getName(), 100.0 * (new Random().nextInt(10)),
						false, 100.0 * (new Random().nextInt(10)),
						500.0 * (new Random().nextInt(10)), 1, false,
						receivePayment5);
				if (trp != null)
					transactionReceivePayment5.add(trp);

				if (createMore) {
					int count = 5;
					if (count != 0) {
						trp = testCallCreateTransactionReceivePayment(1l,
								"Customer1",
								100.0 * (new Random().nextInt(10)), null, null,
								100.0 * (new Random().nextInt(10)), false,
								100.0 * (new Random().nextInt(10)),
								500.0 * (new Random().nextInt(10)), 1, false,
								receivePayment6);
						if (trp != null) {
							transactionReceivePayment6.add(trp);
							count--;
						}
					}
					if (count != 0) {
						trp = testCallCreateTransactionReceivePayment(1l,
								"Customer2",
								100.0 * (new Random().nextInt(10)),
								getRandomAccount(accounts).getName(),
								getRandomAccount(accounts).getName(),
								100.0 * (new Random().nextInt(10)), false,
								100.0 * (new Random().nextInt(10)),
								500.0 * (new Random().nextInt(10)), 1, false,
								receivePayment7);
						if (trp != null) {
							transactionReceivePayment7.add(trp);
							count--;
						}
					}
					if (count != 0) {
						trp = testCallCreateTransactionReceivePayment(1l,
								"Customer3",
								100.0 * (new Random().nextInt(10)), null, null,
								100.0 * (new Random().nextInt(10)), false,
								100.0 * (new Random().nextInt(10)),
								500.0 * (new Random().nextInt(10)), 1, false,
								receivePayment8);
						if (trp != null) {
							transactionReceivePayment8.add(trp);
							count--;
						}
					}
					if (count != 0) {
						trp = testCallCreateTransactionReceivePayment(1l,
								"Customer4",
								100.0 * (new Random().nextInt(10)),
								getRandomAccount(accounts).getName(),
								getRandomAccount(accounts).getName(),
								100.0 * (new Random().nextInt(10)), false,
								100.0 * (new Random().nextInt(10)),
								500.0 * (new Random().nextInt(10)), 1, false,
								receivePayment9);
						if (trp != null) {
							transactionReceivePayment9.add(trp);
							count--;
						}
					}
					if (count != 0) {
						trp = testCallCreateTransactionReceivePayment(1l,
								"Customer5",
								100.0 * (new Random().nextInt(10)), null, null,
								100.0 * (new Random().nextInt(10)), false,
								100.0 * (new Random().nextInt(10)),
								500.0 * (new Random().nextInt(10)), 1, false,
								receivePayment10);
						if (trp != null) {
							transactionReceivePayment10.add(trp);
							count--;
						}
					}
				}
			}

			// testCallCreateReceivePayment( company in long
			// customer in String
			// depIn in String
			// date in String
			// payMethod in int
			// amount in double // leave as 0d, to pay full amount, morethan
			// zero adds to the full amount
			// TransactionReceivePayment object
			// ) returns ReceivePayment object

			ArrayList<ReceivePayment> rps = new ArrayList<ReceivePayment>();

			List<Account> depAccounts = Utility.getDepositInAccounts(company);

			// rps.add(testCallCreateReceivePayment(1l, "Customer1", //
			// "Account Credit Card", today(),
			// AccounterConstants.PAYMENT_METHOD_CASH, 0d, //
			// transactionReceivePayment1, receivePayment1));
			// rps.add(testCallCreateReceivePayment(1l, "Customer2", //
			// "Account Cash", today(), AccounterConstants.PAYMENT_METHOD_CASH,
			// 0d, //
			// transactionReceivePayment2, receivePayment2));

			if (transactionReceivePayment1.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer1",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CASH, 0d,
						transactionReceivePayment1, receivePayment1));

			if (transactionReceivePayment2.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer2",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CHECK, 0d,
						transactionReceivePayment2, receivePayment2));

			if (transactionReceivePayment3.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer3",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CREDITCARD,
						0d, transactionReceivePayment3, receivePayment3));

			if (transactionReceivePayment4.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer4",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CASH, 0d,
						transactionReceivePayment4, receivePayment4));

			if (transactionReceivePayment5.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer5",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CHECK, 0d,
						transactionReceivePayment5, receivePayment5));

			if (transactionReceivePayment6.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer1",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CREDITCARD,
						100.0 * (new Random().nextInt(10)),
						transactionReceivePayment6, receivePayment6));

			if (transactionReceivePayment7.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer2",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CASH,
						100.0 * (new Random().nextInt(10)),
						transactionReceivePayment7, receivePayment7));

			if (transactionReceivePayment8.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer3",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CHECK,
						100.0 * (new Random().nextInt(10)),
						transactionReceivePayment8, receivePayment8));

			if (transactionReceivePayment9.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer4",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CREDITCARD,
						100.0 * (new Random().nextInt(10)),
						transactionReceivePayment9, receivePayment9));

			if (transactionReceivePayment10.size() > 0)
				rps.add(testCallCreateReceivePayment(1l, "Customer5",
						getRandomMakeDepositAccount(depAccounts).getName(),
						today(), AccounterConstants.PAYMENT_METHOD_CASH,
						100.0 * (new Random().nextInt(10)),
						transactionReceivePayment10, receivePayment10));

			if (checkTesting) {
				Iterator<ReceivePayment> i = rps.iterator();

				while (i.hasNext()) {
					ReceivePayment r = i.next();

					if (r.getDepositIn().isIncrease()) {
						d[getAccountVariable(accounts, r.getDepositIn()
								.getName(), company)] -= r.getAmount();
						String str = r.getDepositIn().getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= r
									.getAmount();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, r.getDepositIn()
								.getName(), company)] += r.getAmount();
						String str = r.getDepositIn().getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += r
									.getAmount();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

					d[getAccountVariable(accounts,
							company.accountsReceivableAccount.name, company)] -= (r
							.getAmount()
							+ r.getTotalCashDiscount() + r.getTotalWriteOff());

					List<TransactionReceivePayment> trp = r
							.getTransactionReceivePayment();
					Iterator<TransactionReceivePayment> i2 = trp.iterator();
					while (i2.hasNext()) {
						TransactionReceivePayment tp = i2.next();
						if (tp.getDiscountAccount() != null) {
							if (tp.getDiscountAccount().isIncrease()) {
								d[getAccountVariable(accounts, tp
										.getDiscountAccount().getName(),
										company)] -= tp.getCashDiscount();
								String str = tp.getDiscountAccount().getName();
								while ((accounterDao.getAccount(
										company.getId(), str).getParent() != null)) {
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(), str)
											.getParent().getName(), company)] -= tp
											.getCashDiscount();
									str = accounterDao.getAccount(
											company.getId(), str).getParent()
											.getName();
								}
							} else {
								d[getAccountVariable(accounts, tp
										.getDiscountAccount().getName(),
										company)] += tp.getCashDiscount();
								String str = tp.getDiscountAccount().getName();
								while ((accounterDao.getAccount(
										company.getId(), str).getParent() != null)) {
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(), str)
											.getParent().getName(), company)] += tp
											.getCashDiscount();
									str = accounterDao.getAccount(
											company.getId(), str).getParent()
											.getName();
								}
							}
						}
						if (tp.getWriteOffAccount() != null) {
							if (tp.getWriteOffAccount().isIncrease()) {
								d[getAccountVariable(accounts, tp
										.getWriteOffAccount().getName(),
										company)] -= tp.getWriteOff();
								String str = tp.getWriteOffAccount().getName();
								while ((accounterDao.getAccount(
										company.getId(), str).getParent() != null)) {
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(), str)
											.getParent().getName(), company)] -= tp
											.getWriteOff();
									str = accounterDao.getAccount(
											company.getId(), str).getParent()
											.getName();
								}
							} else {
								d[getAccountVariable(accounts, tp
										.getWriteOffAccount().getName(),
										company)] += tp.getWriteOff();
								String str = tp.getWriteOffAccount().getName();
								while ((accounterDao.getAccount(
										company.getId(), str).getParent() != null)) {
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(), str)
											.getParent().getName(), company)] += tp
											.getWriteOff();
									str = accounterDao.getAccount(
											company.getId(), str).getParent()
											.getName();
								}
							}
						}
					}
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
					}
				}
			}
		}
	}

	public PayBill testCallCreatePayBill(long cm, String vendor, String date,
			String payFrom, String paymentMethod, double amount, PayBill p,
			List<Account> accounts) throws Exception {
		company = accounterDao.getCompany(1l);
		p.setCompany(company);
		p.setNumber(accounterGUIDao.getNextTransactionNumber(company.getId(),
				Transaction.TYPE_PAY_BILL));
		p.setDate(format.parse(date));
		p.setType(Transaction.TYPE_PAY_BILL);
		p.setPayBillType(PayBill.TYPE_VENDOR_PAYMENT);
		p.setPayFrom(accounterDao.getAccount(company.getId(), payFrom));// un
		// deposited
		// funds
		p.setPaymentMethod(paymentMethod);
		p.setTotal(amount);
		p.setEndingBalance(d[getAccountVariable(accounts, payFrom, company)]
				- amount);
		p.setVendor(accounterDao.getVendor(company.getId(), vendor));
		p.setUnusedAmount(amount);
		double vendorBalance = accounterDao.getVendor(company.getId(),
				p.getVendor().getId()).getBalance();
		accounter.createPayBill(p);
		if (checkTesting)
			assertEquals(vendorBalance - p.getTotal(), accounterDao.getVendor(
					company.getId(), p.getVendor().getId()).getBalance());
		return p;
	}

	public PayBill testCallCreatePayBill(long cm, String vendor, String date,
			String payFrom, String paymentMethod, double amount,
			List<TransactionPayBill> tp, PayBill p, List<Account> accounts)
			throws Exception {

		company = accounterDao.getCompany(1l);
		p.setCompany(company);
		p.setNumber(accounterGUIDao.getNextTransactionNumber(company.getId(),
				Transaction.TYPE_PAY_BILL));
		p.setDate(format.parse(date));
		p.setType(Transaction.TYPE_PAY_BILL);
		p.setPayBillType(PayBill.TYPE_PAYBILL);
		p.setPayFrom(accounterDao.getAccount(cm, payFrom));
		p.setPaymentMethod(paymentMethod);
		p.setTotal(getAmountPayment(tp));
		p.setEndingBalance(d[getAccountVariable(accounts, payFrom, company)]
				- p.getTotal());
		p.setVendor(accounterDao.getVendor(cm, vendor));
		p.setUnusedAmount(p.getTotal());
		p.setTransactionPayBill(tp);
		double vendorBalance = accounterDao
				.getVendor(cm, p.getVendor().getId()).getBalance();
		accounter.createPayBill(p);
		if (checkTesting)
			assertEquals(vendorBalance - p.getTotal()
					- getAmountTotalDiscount(tp), accounterDao.getVendor(cm,
					p.getVendor().getId()).getBalance());
		return p;
	}

	public double testCallCreateCreditsAndPayments(Vendor v,
			boolean useAllCredits, double amtToUse,
			List<TransactionCreditsAndPayments> tc) throws DAOException {
		double d = 0.0;
		boolean flag = true;
		company = v.getCompany();
		List<CreditsAndPayments> cpl = accounterGUIDao
				.getVendorCreditsAndPayments(company.getId(), v.getId());
		assertNotNull(cpl);
		Iterator<CreditsAndPayments> i = cpl.iterator();
		while (i.hasNext() && flag) {
			CreditsAndPayments cp = i.next();
			TransactionCreditsAndPayments cps = new TransactionCreditsAndPayments();
			cps.setMemo(cp.getMemo());
			cps.setCreditsAndPayments(cp);
			cps
					.setAmountToUse(useAllCredits ? cp.getCreditAmount()
							: (amtToUse > cp.getCreditAmount()) ? (amtToUse - (amtToUse -= cp
									.getCreditAmount()))
									: amtToUse);
			cps.setCompany(company);
			if ((amtToUse < cp.getCreditAmount()) && !useAllCredits)
				break;
			if (!i.hasNext() && amtToUse > cp.getCreditAmount())
				d = amtToUse - cp.getCreditAmount();
			tc.add(cps);
		}

		return d;
	}

	public TransactionPayBill testCallCreateTransactionPayBill(Company company,
			String vendor, String dueDate, String discountAccount,
			double discount, PayBill p, boolean useAllCredits, double amtToUse)
			throws Exception {

		List<PayBillTransactionList> pbl = accounterGUIDao
				.getTransactionPayBills(company.getId(), accounterDao
						.getVendor(company.getId(), vendor).getId());
		if (pbl.size() > 0) {
			PayBillTransactionList pb = (PayBillTransactionList) getLastTransaction(
					1, pbl.size(), pbl.iterator());

			List<TransactionCreditsAndPayments> tc = new ArrayList<TransactionCreditsAndPayments>();

			double d = 0d;
			if (amtToUse == 0.0)
				d = testCallCreateCreditsAndPayments(accounterDao.getVendor(
						company.getId(), vendor), useAllCredits, amtToUse, tc);

			TransactionPayBill tp = new TransactionPayBill();
			tp.setAmountDue(pb.getAmountDue());
			tp.setTransactionCreditsAndPayments(tc);
			tp.setAppliedCredits(amtToUse - d);
			tp.setCashDiscount(discount);
			tp.setDiscountAccount(accounterDao.getAccount(company.getId(),
					discountAccount));
			tp.setDiscountDate(pb.getDiscountDate());
			tp.setDueDate(format.parse(dueDate));
			tp.setOriginalAmount(pb.getOriginalAmount());
			tp.setPayment(tp.getAmountDue()
					- (tp.getAppliedCredits() + tp.getCashDiscount()));
			tp.setPayBill(p);
			if (pb.getType() == Transaction.TYPE_ENTER_BILL)
				tp.setEnterBill(accounterDao.getEnterBill(company.getId(), pb
						.getTransactionId()));
			else
				tp.setTransactionMakeDeposit(accounterGUIDao
						.getTransactionMakeDeposit(company.getId(), pb
								.getTransactionId()));

			return tp;
		} else
			return null;
	}

	public PayBillTransactionList getLastTransaction(int c, int n,
			Iterator<PayBillTransactionList> i) {

		PayBillTransactionList p = null;
		Iterator<PayBillTransactionList> k = i;

		while (k.hasNext()) {
			p = k.next();
			if (!k.hasNext())
				return p;
		}

		return p;
	}

	public void testCreatePayBill() throws Exception {

		company = accounterDao.getCompany(1L);
		// This function declares a double array where all the present total
		// balances of all the accounts in the company are stored.
		// For every instruction, this array will get updated and later we check
		// these values with the stored values in the database.

		List<Account> accounts = accounterDao.getAccounts(company.getId());

		setDefaultAccountVariables(accounts, company.getId());

		ArrayList<PayBill> paybills = new ArrayList<PayBill>();

		PayBill payBill1 = new PayBill();
		PayBill payBill2 = new PayBill();
		PayBill payBill3 = new PayBill();
		PayBill payBill4 = new PayBill();
		PayBill payBill5 = new PayBill();
		PayBill payBill6 = new PayBill();
		PayBill payBill7 = new PayBill();
		PayBill payBill8 = new PayBill();
		// bV 20SI 9966311341
		PayBill payBill9 = new PayBill();
		PayBill payBill10 = new PayBill();

		// * public PayBill testCallCreatePayBill( cm in long
		// vendor in String ,
		// date in String ,
		// payFrom in String,
		// paymentMethod in int ,
		// amount in double ,
		// PayBill object
		// ) RETURNS paybill object

		List<Account> payFromAccounts = Utility.getPayFromAccounts(company);
		System.out.println("acounts payable amount="
				+ accounterDao.getAccount(company.getId(), 3l)
						.getTotalBalance());

		// paybills.add(testCallCreatePayBill(1l, "Vendor1", today(), //
		// "Account Cash", AccounterConstants.PAYMENT_METHOD_CASH, 1500d,
		// payBill1, accounts));

		if (createVendorPayments) {
			paybills.add(testCallCreatePayBill(1l, "Vendor1", today(),
					getRandomPayFromAccount(payFromAccounts).getName(),
					AccounterConstants.PAYMENT_METHOD_CASH, 1500d, payBill1,
					accounts));
			paybills.add(testCallCreatePayBill(1l, "Vendor2", today(),
					getRandomPayFromAccount(payFromAccounts).getName(),
					AccounterConstants.PAYMENT_METHOD_CASH, 1000d, payBill2,
					accounts));
			paybills.add(testCallCreatePayBill(1l, "Vendor3", today(),
					getRandomPayFromAccount(payFromAccounts).getName(),
					AccounterConstants.PAYMENT_METHOD_CASH, 500d, payBill3,
					accounts));

			if (createMore) {
				paybills.add(testCallCreatePayBill(1l, "Vendor4", today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						AccounterConstants.PAYMENT_METHOD_CASH, 1000d,
						payBill4, accounts));
				paybills.add(testCallCreatePayBill(1l, "Vendor5", today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						AccounterConstants.PAYMENT_METHOD_CASH, 500d, payBill5,
						accounts));
			}
		}

		List<TransactionPayBill> transactionPayBill6 = new ArrayList<TransactionPayBill>();
		List<TransactionPayBill> transactionPayBill7 = new ArrayList<TransactionPayBill>();
		List<TransactionPayBill> transactionPayBill8 = new ArrayList<TransactionPayBill>();
		List<TransactionPayBill> transactionPayBill9 = new ArrayList<TransactionPayBill>();
		List<TransactionPayBill> transactionPayBill10 = new ArrayList<TransactionPayBill>();

		//transactionPayBill6.add(testCallCreateTransactionPayBill(company.getId
		// (),
		// //
		// "Vendor1", dueDate(2), "Account Credit Card", 100D, payBill6, false,
		// // 1000d));

		if (createPayBills && createEnterBills) {
			int count = 5;
			if (!createMore)
				count = 3;

			TransactionPayBill tp;
			// if (count != 0) {
			// tp = testCallCreateTransactionPayBill(company, "Vendor1",
			// dueDate(2), getRandomAccount(accounts).getName(), 100D, payBill6,
			// false, 1000d);
			// if (tp != null) {
			// transactionPayBill6.add(tp);
			// count--;
			// }
			// }
			//
			// if (count != 0) {
			// tp = testCallCreateTransactionPayBill(company, "Vendor2",
			// dueDate(2), getRandomAccount(accounts).getName(), 100D, payBill7,
			// false, 1000d);
			// if (tp != null) {
			// transactionPayBill7.add(tp);
			// count--;
			// }
			// }
			//
			if (count != 0) {
				tp = testCallCreateTransactionPayBill(company, "Vendor3",
						dueDate(2), getRandomAccount(accounts).getName(), 100D,
						payBill8, false, 1000d);
				if (tp != null) {
					transactionPayBill8.add(tp);
					count--;
				}
			}
			//
			// if (count != 0) {
			// tp = testCallCreateTransactionPayBill(company, "Vendor4",
			// dueDate(2), getRandomAccount(accounts).getName(), 100D, payBill9,
			// false, 1000d);
			// if (tp != null) {
			// transactionPayBill9.add(tp);
			// count--;
			// }
			// }
			//
			// if (count != 0) {
			// tp = testCallCreateTransactionPayBill(company, "Vendor5",
			// dueDate(2), getRandomAccount(accounts).getName(), 100D,
			// payBill10, false, 1000d);
			// if (tp != null) {
			// transactionPayBill10.add(tp);
			// count--;
			// }
			// }
		}

		// * public PayBill testCallCreatePayBill( cm in long
		// * vendor in String , // * date in String , // * payFrom in String, //
		// * paymentMethod in int , // * amount in double , // *
		// transactionPayBill Set
		// * PayBill object
		// * accounts in List
		// * ) RETURNS paybill object

		// paybills.add(testCallCreatePayBill(1l, "Vendor1", today(), //
		// "Account Cash", AccounterConstants.PAYMENT_METHOD_CASH, 100.0*(new
		// Random().nextInt(10)), transactionPayBill6, payBill6, accounts));

		if (createPayBills && createEnterBills) {
			if (transactionPayBill6.size() > 0)
				paybills.add(testCallCreatePayBill(1l, "Vendor1", today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						AccounterConstants.PAYMENT_METHOD_CASH,
						100.0 * (new Random().nextInt(10)),
						transactionPayBill6, payBill6, accounts));

			if (transactionPayBill7.size() > 0)
				paybills.add(testCallCreatePayBill(1l, "Vendor2", today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						AccounterConstants.PAYMENT_METHOD_CASH,
						100.0 * (new Random().nextInt(10)),
						transactionPayBill7, payBill7, accounts));

			if (transactionPayBill8.size() > 0)
				paybills.add(testCallCreatePayBill(1l, "Vendor3", today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						AccounterConstants.PAYMENT_METHOD_CASH,
						100.0 * (new Random().nextInt(10)),
						transactionPayBill8, payBill8, accounts));

			if (transactionPayBill9.size() > 0)
				paybills.add(testCallCreatePayBill(1l, "Vendor4", today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						AccounterConstants.PAYMENT_METHOD_CASH,
						100.0 * (new Random().nextInt(10)),
						transactionPayBill9, payBill9, accounts));

			if (transactionPayBill10.size() > 0)
				paybills.add(testCallCreatePayBill(1l, "Vendor5", today(),
						getRandomPayFromAccount(payFromAccounts).getName(),
						AccounterConstants.PAYMENT_METHOD_CASH,
						100.0 * (new Random().nextInt(10)),
						transactionPayBill10, payBill10, accounts));
		}

		if (checkTesting) {
			Iterator<PayBill> i = paybills.iterator();

			while (i.hasNext()) {
				PayBill r = i.next();
				if (r.getPayFrom().isIncrease()) {
					d[getAccountVariable(accounts, r.getPayFrom().getName(),
							company)] += r.getTotal();
					String str = r.getPayFrom().getName();
					while ((accounterDao.getAccount(company.getId(), str)
							.getParent() != null)) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] += r.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				} else {
					d[getAccountVariable(accounts, r.getPayFrom().getName(),
							company)] -= r.getTotal();
					String str = r.getPayFrom().getName();
					while ((accounterDao.getAccount(company.getId(), str)
							.getParent() != null)) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] -= r.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				}
				d[getAccountVariable(accounts, company
						.getAccountsPayableAccount().getName(), company)] -= (r
						.getTotal());

				if (r.getPayBillType() == PayBill.TYPE_PAYBILL) {
					d[getAccountVariable(accounts, company
							.getAccountsPayableAccount().getName(), company)] -= getAmountTotalDiscount(r
							.getTransactionPayBill());
					List<TransactionPayBill> trp = r.getTransactionPayBill();
					Iterator<TransactionPayBill> i2 = trp.iterator();
					while (i2.hasNext()) {
						TransactionPayBill tp = i2.next();
						if (tp.getDiscountAccount().isIncrease()) {
							d[getAccountVariable(accounts, tp
									.getDiscountAccount().getName(), company)] += tp
									.getCashDiscount();
							String str = tp.getDiscountAccount().getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += tp
										.getCashDiscount();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						} else {
							d[getAccountVariable(accounts, tp
									.getDiscountAccount().getName(), company)] -= tp
									.getCashDiscount();
							String str = tp.getDiscountAccount().getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= tp
										.getCashDiscount();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}
					}
				}
				if (!i.hasNext()) {
					for (long l1 = 0; l1 < accounts.size(); l1++)
						assertEquals(accounterDao.getAccount(company.getId(),
								(l1 + 1)).getName(), d[getAccountVariable(
								accounts, accounterDao.getAccount(
										company.getId(), (l1 + 1)).getName(),
								company)], accounterDao.getAccount(
								company.getId(), (l1 + 1)).getTotalBalance());
				}
			}
		}
	}

	public CreditCardCharge testCallCreateCreditCardCharge(CreditCardCharge cs,
			String cu, long cm, String date, String depIn,
			List<TransactionItem> ti) throws Exception {

		company = accounterDao.getCompany(1L);

		cs.setVendor(accounterDao.getVendor(cm, cu));
		cs.setCompany(company);
		cs.setType(Transaction.TYPE_CASH_SALES);
		cs.setNumber(accounterGUIDao.getNextTransactionNumber(company.getId(),
				Transaction.TYPE_CASH_SALES));
		cs.setDate(format.parse(date));
		cs.setPayFrom(accounterDao.getAccount(cm, depIn));
		cs.setTransactionItems(ti);
		cs.setTotal(getAmountAllLineTotal(cs.getTransactionItems()));
		double vendorBalance = accounterDao.getVendor(company.getId(),
				cs.getVendor().getId()).getBalance();
		accounter.createCreditCardCharge(cs);
		if (checkTesting)
			assertEquals(vendorBalance, accounterDao.getVendor(company.getId(),
					cs.getVendor().getId()).getBalance());
		return cs;
	}

	public TransactionItem testCallCreateTransactionItem(String cm, int type,
			double q, double up, String it, CreditCardCharge c)
			throws Exception {
		TransactionItem item = new TransactionItem();
		testCallSetValuesTransactionItem(item, cm, type, it, q, up);
		item.setTransaction(c);
		return item;
	}

	public void testCreateCreditCardCharges() throws Exception {
		if (createCreditCardCharges) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Item> items = accounterDao.getItems(company.getId());
			ListIterator<Item> its = items.listIterator();

			List<Account> accounts = accounterDao.getAccounts(company.getId());
			ListIterator<Account> acs = accounts.listIterator();

			setDefaultAccountVariables(accounts, company.getId());

			CreditCardCharge ccc1 = new CreditCardCharge();
			CreditCardCharge ccc2 = new CreditCardCharge();
			CreditCardCharge ccc3 = new CreditCardCharge();
			CreditCardCharge ccc4 = new CreditCardCharge();
			CreditCardCharge ccc5 = new CreditCardCharge();
			CreditCardCharge ccc6 = new CreditCardCharge();
			CreditCardCharge ccc7 = new CreditCardCharge();
			CreditCardCharge ccc8 = new CreditCardCharge();
			CreditCardCharge ccc9 = new CreditCardCharge();
			CreditCardCharge ccc10 = new CreditCardCharge();

			List<TransactionItem> transactionItems1 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems2 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems3 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems4 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems5 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems6 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems7 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems8 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems9 = new ArrayList<TransactionItem>();
			List<TransactionItem> transactionItems10 = new ArrayList<TransactionItem>();

			// testCallTransactionItem( Type in int, for item type entry
			// Quantity in Double, // UnitPrice in double, // Discount in
			// Double, // //LineTotal in Double, // itemtax in String, // item
			// in String, // CashSales ) returns TransactionItems object
			//
			// testCallTransactionItem( Type in int, for sales tax account type
			// entry
			// LineTotal in Double, // taxcode in String, // CashSales ) returns
			// TransactionItems object

			// testCallTransactionItem( Type in int, for tax account type entry
			// Quantity in double
			// Unit price in double
			// //LineTotal in Double, // itemTax in String, // Account in String
			// CashSales ) returns TransactionItems object

			//transactionItems1.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Write off", ccc1));
			//transactionItems2.add(testCallCreateTransactionItem(company.getName
			// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, "Bank Charge", //
			// ccc2));

			for (int n = 0; n < (items.size() / 2); n++) {
				Item item = nextItem(its);// (String cm, int type, double q, //
				// double up, String it, //
				// CreditCardCharge c) throws
				// Exception
				transactionItems1.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ITEM, 1D, item
						.getSalesPrice(), item.getName(), ccc1));
			}
			for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
				Item item = nextItem(its);
				transactionItems2.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ITEM, 1D, item
						.getSalesPrice(), item.getName(), ccc2));
			}
			for (int n = 0; n < (accounts.size() / 2); n++) {
				Account account = nextAccount(acs);
				transactionItems3.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
						account.getName(), ccc3));
			}
			for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++) {
				Account account = nextAccount(acs);
				transactionItems4.add(testCallCreateTransactionItem(company
						.getName(), TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
						account.getName(), ccc4));
			}

			if (createMore) {
				for (int n = 0; n < ((items.size() + accounts.size()) / 4); n++) {

					if (items.size() > 0) {
						Item item = items.get(new Random()
								.nextInt(items.size()));
						transactionItems6
								.add(testCallCreateTransactionItem(company
										.getName(), TransactionItem.TYPE_ITEM,
										1D, item.getSalesPrice(), item
												.getName(), ccc6));
						item = items.get(new Random().nextInt(items.size()));
						transactionItems7
								.add(testCallCreateTransactionItem(company
										.getName(), TransactionItem.TYPE_ITEM,
										1D, item.getSalesPrice(), item
												.getName(), ccc7));
						item = items.get(new Random().nextInt(items.size()));
						transactionItems8
								.add(testCallCreateTransactionItem(company
										.getName(), TransactionItem.TYPE_ITEM,
										1D, item.getSalesPrice(), item
												.getName(), ccc8));
						item = items.get(new Random().nextInt(items.size()));
						transactionItems9
								.add(testCallCreateTransactionItem(company
										.getName(), TransactionItem.TYPE_ITEM,
										1D, item.getSalesPrice(), item
												.getName(), ccc9));
						item = items.get(new Random().nextInt(items.size()));
						transactionItems10
								.add(testCallCreateTransactionItem(company
										.getName(), TransactionItem.TYPE_ITEM,
										1D, item.getSalesPrice(), item
												.getName(), ccc10));
					}

					if (accounts.size() > 0) {
						Account account = getRandomAccount(accounts);
						transactionItems6.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), ccc6));
						account = getRandomAccount(accounts);
						transactionItems7.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), ccc7));
						account = getRandomAccount(accounts);
						transactionItems8.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), ccc8));
						account = getRandomAccount(accounts);
						transactionItems9.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), ccc9));
						account = getRandomAccount(accounts);
						transactionItems10.add(testCallCreateTransactionItem(
								company.getName(),
								TransactionItem.TYPE_ACCOUNT, 1D, 1000D,
								account.getName(), ccc10));
					}
				}
			}

			// testCallCreateccc( ccc, // Customer in String, // Company in long
			// , // Date in string, // DepositIn Account in long, // TaxGroup in
			// Long, // TransactionItems in set, // ) returns ccc object

			ArrayList<CreditCardCharge> cccs = new ArrayList<CreditCardCharge>();
			List<Account> payFromAccounts = Utility.getPayFromAccounts(company);
			// cccs.add(testCallCreateCreditCardCharge(ccc1, "Vendor1", 1L, //
			// today(), "Account Cash", transactionItems1));
			// cccs.add(testCallCreateCreditCardCharge(ccc2, "Vendor2", 1L, //
			// today(), "Account Credit Card", transactionItems2));

			if (transactionItems1.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc1, "Vendor1", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems1));

			if (transactionItems2.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc2, "Vendor2", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems2));

			if (transactionItems3.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc3, "Vendor3", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems3));

			if (transactionItems4.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc4, "Vendor4", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems4));

			if (transactionItems5.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc5, "Vendor5", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems5));

			if (transactionItems6.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc6, "Vendor1", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems6));

			if (transactionItems7.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc7, "Vendor2", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems7));

			if (transactionItems8.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc8, "Vendor3", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems8));

			if (transactionItems9.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc9, "Vendor4", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems9));

			if (transactionItems10.size() > 0)
				cccs.add(testCallCreateCreditCardCharge(ccc10, "Vendor5", 1L,
						today(), getRandomPayFromAccount(payFromAccounts)
								.getName(), transactionItems10));

			if (checkTesting) {
				// From here testing starts
				// All the CreditCardCharge are stored in a ArrayList, and we
				// will
				// iterate them.
				// For every CashSale, we perform necessary operations on the
				// accounts array and
				// atlast we check them with the actual values that are stored
				// in
				// the database.

				Iterator<CreditCardCharge> i = cccs.iterator();
				CreditCardCharge c;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				while (i.hasNext()) {
					c = (CreditCardCharge) i.next();
					assertNotNull(c);
					assertNotNull(c.getVendor());
					assertNotNull(c.getVendor().getName());
					assertEquals(c.getVendor().getName(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getVendor().getName());
					assertEquals(c.getDate(), accounterDao.getCreditCardCharge(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getPayFrom());
					assertNotNull(c.getPayFrom().getName());
					assertEquals(c.getPayFrom().getName(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getPayFrom().getName());
					assertEquals(c.getType(), accounterDao.getCreditCardCharge(
							company.getId(), c.getId()).getType());
					assertEquals(c.getTotal(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getTotal());
					if (c.getPaymentMethod() != null)
						if (c.getPaymentMethod() != null)
							assertEquals(c.getPaymentMethod(), accounterDao
									.getCreditCardCharge(company.getId(),
											c.getId()).getPaymentMethod());
					assertEquals(c.getNumber(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getExpenseAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else
							// if(it.getType()==TransactionItem.TYPE_SALESTAX)
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase

					}// while there is a transaction item

					// checking whether the depositIn account is updated. If it
					// has
					// a parent it also need to be updated
					if (c.getPayFrom().isIncrease()) {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] += c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] -= c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();

						}

						// If the last transaction item of the last CashSale is
						// done, we need to check the accounts table whether all
						// the
						// accounts are updated perfectly.
						if (!i.hasNext()) {
							for (long l1 = 0; l1 < accounts.size(); l1++)
								assertEquals(accounterDao.getAccount(
										company.getId(), (l1 + 1)).getName(),
										d[getAccountVariable(accounts,
												accounterDao.getAccount(
														company.getId(),
														(l1 + 1)).getName(),
												company)], accounterDao
												.getAccount(company.getId(),
														(l1 + 1))
												.getTotalBalance());
						}
					}
				}
			}
		}
	}

	public TransferFund testCallCreateTransferFund(Company company,
			String date, double amount, String transferFrom, String transferTo,
			TransferFund t) throws Exception {

		t.setCompany(company);
		t.setDate(format.parse(date));
		t.setNumber(accounterGUIDao.getNextTransactionNumber(company.getId(),
				Transaction.TYPE_TRANSFER_FUND));
		t.setTotal(amount);
		t.setTransferFrom(accounterDao
				.getAccount(company.getId(), transferFrom));
		t.setTransferTo(accounterDao.getAccount(company.getId(), transferTo));
		t.setType(Transaction.TYPE_TRANSFER_FUND);
		accounter.createTransferFund(t);
		return t;
	}

	public Account getRandomTransferAcc(List<Account> acs) throws DAOException {
		Account a = null;
		while (true) {
			a = acs.get(new Random().nextInt(acs.size()));
			if (!getAccountIsTaxAgencyAccount(a))
				return a;
		}
	}

	public Account getRandomTransferAccount(List<Account> acs, Account ac)
			throws DAOException {
		Account a = null;
		while (true) {
			a = getRandomTransferAcc(acs);
			if (!a.getName().equals(ac.getName()))
				return a;
		}
	}

	public void testCreateTransferFund() throws Exception {

		if (createTransferFunds) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Account> accounts = accounterDao.getAccounts(company.getId());

			setDefaultAccountVariables(accounts, company.getId());

			TransferFund transferFund1 = new TransferFund();
			TransferFund transferFund2 = new TransferFund();
			TransferFund transferFund3 = new TransferFund();
			TransferFund transferFund4 = new TransferFund();
			TransferFund transferFund5 = new TransferFund();
			TransferFund transferFund6 = new TransferFund();
			TransferFund transferFund7 = new TransferFund();
			TransferFund transferFund8 = new TransferFund();
			TransferFund transferFund9 = new TransferFund();
			TransferFund transferFund10 = new TransferFund();

			ArrayList<TransferFund> tfs = new ArrayList<TransferFund>();

			List<Account> transferAccounts = Utility
					.getCashBackAccounts(company);

			// tfs.add(testCallCreateTransferFund(company.getId(), today(),
			// 500.0, // AccounterConstants.WRITE_OFF,
			// AccounterConstants.BANK_CHARGE, // transferFund1));
			Account a = getRandomTransferAccount(transferAccounts,
					transferAccounts.get(1));
			tfs.add(testCallCreateTransferFund(company, today(),
					500.0 * (new Random().nextInt(10)), a.getName(),
					getRandomTransferAccount(transferAccounts, a).getName(),
					transferFund1));

			a = getRandomTransferAccount(transferAccounts, transferAccounts
					.get(1));
			tfs.add(testCallCreateTransferFund(company, today(),
					500.0 * (new Random().nextInt(10)), a.getName(),
					getRandomTransferAccount(transferAccounts, a).getName(),
					transferFund2));

			a = getRandomTransferAccount(transferAccounts, transferAccounts
					.get(1));
			tfs.add(testCallCreateTransferFund(company, today(),
					500.0 * (new Random().nextInt(10)), a.getName(),
					getRandomTransferAccount(transferAccounts, a).getName(),
					transferFund3));

			a = getRandomTransferAccount(transferAccounts, transferAccounts
					.get(1));
			tfs.add(testCallCreateTransferFund(company, today(),
					500.0 * (new Random().nextInt(10)), a.getName(),
					getRandomTransferAccount(transferAccounts, a).getName(),
					transferFund4));

			a = getRandomTransferAccount(transferAccounts, transferAccounts
					.get(1));
			tfs.add(testCallCreateTransferFund(company, today(),
					500.0 * (new Random().nextInt(10)), a.getName(),
					getRandomTransferAccount(transferAccounts, a).getName(),
					transferFund5));

			if (createMore) {
				a = getRandomTransferAccount(transferAccounts, transferAccounts
						.get(1));
				tfs
						.add(testCallCreateTransferFund(company, today(),
								500.0 * (new Random().nextInt(10)),
								a.getName(), getRandomTransferAccount(
										transferAccounts, a).getName(),
								transferFund6));

				a = getRandomTransferAccount(transferAccounts, transferAccounts
						.get(1));
				tfs
						.add(testCallCreateTransferFund(company, today(),
								500.0 * (new Random().nextInt(10)),
								a.getName(), getRandomTransferAccount(
										transferAccounts, a).getName(),
								transferFund7));

				a = getRandomTransferAccount(transferAccounts, transferAccounts
						.get(1));
				tfs
						.add(testCallCreateTransferFund(company, today(),
								500.0 * (new Random().nextInt(10)),
								a.getName(), getRandomTransferAccount(
										transferAccounts, a).getName(),
								transferFund8));

				a = getRandomTransferAccount(transferAccounts, transferAccounts
						.get(1));
				tfs
						.add(testCallCreateTransferFund(company, today(),
								500.0 * (new Random().nextInt(10)),
								a.getName(), getRandomTransferAccount(
										transferAccounts, a).getName(),
								transferFund9));

				a = getRandomTransferAccount(transferAccounts, transferAccounts
						.get(1));
				tfs
						.add(testCallCreateTransferFund(company, today(),
								500.0 * (new Random().nextInt(10)),
								a.getName(), getRandomTransferAccount(
										transferAccounts, a).getName(),
								transferFund10));
			}

			if (checkTesting) {
				Iterator<TransferFund> i = tfs.iterator();

				while (i.hasNext()) {
					TransferFund t = i.next();
					if (t.getTransferFrom().isIncrease()) {
						d[getAccountVariable(accounts, t.getTransferFrom()
								.getName(), company)] += t.getTotal();
						String str = t.getTransferFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, t.getTransferFrom()
								.getName(), company)] -= t.getTotal();
						String str = t.getTransferFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					if (t.getTransferTo().isIncrease()) {
						d[getAccountVariable(accounts, t.getTransferTo()
								.getName(), company)] -= t.getTotal();
						String str = t.getTransferTo().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, t.getTransferTo()
								.getName(), company)] += t.getTotal();
						String str = t.getTransferTo().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());

					}
				}
			}
		}
	}

	public double getAmountTotalAmount(List<TransactionPaySalesTax> tps) {

		double amount = 0.0;
		Iterator<TransactionPaySalesTax> i = tps.iterator();
		while (i.hasNext()) {
			TransactionPaySalesTax tp = (TransactionPaySalesTax) i.next();
			amount += tp.getAmountToPay();
		}
		return amount;
	}

	public TransactionPaySalesTax testCallCreateTransactionPaySalesTax(
			PaySalesTaxEntries tpl, double amountToPay, PaySalesTax pt)
			throws DAOException {
		TransactionPaySalesTax tpt = new TransactionPaySalesTax();

		tpt.setTaxAgency(accounterDao.getTaxAgency(company.getId(), tpl
				.getTaxAgency().name));
		tpt.setTaxCode(accounterDao.getTaxCode(company.getId(), tpl
				.getTaxCode().name));
		if (tpl.balance < amountToPay)
			tpt.setAmountToPay(tpl.balance);
		else
			tpt.setAmountToPay(amountToPay);
		tpt.setTaxDue(tpl.balance - tpt.getAmountToPay());
		tpt.paySalesTaxEntry = tpl;
		tpt.setTransaction(pt);
		return tpt;
	}

	public PaySalesTax testCallCreatePaySalesTax(Company company, String date,
			Account account, String paymentMethod, boolean canVoidOrEdit,
			List<TransactionPaySalesTax> transactionItems1,
			PaySalesTax paySalesTax, List<TaxCode> codes) throws DAOException,
			ParseException {

		paySalesTax.setTransactionPaySalesTax(transactionItems1);
		paySalesTax.setTotal(getAmountTotalAmount(transactionItems1));
		// paySalesTax.setTotal(paySalesTax.getTotal());
		paySalesTax.setCanVoidOrEdit(canVoidOrEdit);
		paySalesTax.setCompany(company);
		paySalesTax.setDate(format.parse(today()));
		paySalesTax.billsDueOnOrBefore = format.parse(date);
		paySalesTax.setPayFrom(account);
		paySalesTax.setPaymentMethod(paymentMethod);
		paySalesTax.setNumber(accounterGUIDao.getNextTransactionNumber(company
				.getId(), Transaction.TYPE_PAY_SALES_TAX));
		accounter.createPaySalesTax(paySalesTax);
		List<TransactionPaySalesTax> tps = paySalesTax
				.getTransactionPaySalesTax();
		Iterator<TransactionPaySalesTax> i = tps.iterator();
		while (i.hasNext()) {
			TransactionPaySalesTax tp = (TransactionPaySalesTax) i.next();
			taxAgencies[getTaxAgencyVariable(codes,
					tp.getTaxAgency().getName(), company)] += tp
					.getAmountToPay();
		}
		// accounter.alterPaySalesTax(paySalesTax);
		return paySalesTax;
	}

	public void testCreatePaySalesTax() throws Exception {

		if (createPaySalesTaxes) {
			company = accounterDao.getCompany(1L);
			List<Account> payFromAccounts = Utility.getPayFromAccounts(company);
			List<Account> accounts = accounterDao.getAccounts(company.getId());
			setDefaultAccountVariables(accounts, company.getId());

			List<TaxCode> codes = accounterDao.getTaxCodes(company.getId());
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getName().equals("None"))
					codes.remove(i);
			}
			taxAgencies = new double[codes.size()];
			for (int i = 0; i < taxAgencies.length; i++)
				taxAgencies[getTaxAgencyVariable(codes, codes.get(i)
						.getTaxAgency().getName(), company)] = codes.get(i)
						.getTaxAgency().getBalance();

			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<PaySalesTaxEntries> tpsl = accounterGUIDao
					.getTransactionPaySalesTaxEntriesList(company.getId(),
							format.parse(dueDate(1)));

			PaySalesTax pt1 = new PaySalesTax();
			PaySalesTax pt2 = new PaySalesTax();
			PaySalesTax pt3 = new PaySalesTax();
			PaySalesTax pt4 = new PaySalesTax();
			PaySalesTax pt5 = new PaySalesTax();

			List<TransactionPaySalesTax> transactionItems1 = new ArrayList<TransactionPaySalesTax>();
			List<TransactionPaySalesTax> transactionItems2 = new ArrayList<TransactionPaySalesTax>();
			List<TransactionPaySalesTax> transactionItems3 = new ArrayList<TransactionPaySalesTax>();
			List<TransactionPaySalesTax> transactionItems4 = new ArrayList<TransactionPaySalesTax>();
			List<TransactionPaySalesTax> transactionItems5 = new ArrayList<TransactionPaySalesTax>();

			Iterator<PaySalesTaxEntries> i2 = tpsl.iterator();

			if (i2.hasNext())
				transactionItems1.add(testCallCreateTransactionPaySalesTax(i2
						.next(), 1000d, pt1));

			if (i2.hasNext())
				transactionItems2.add(testCallCreateTransactionPaySalesTax(i2
						.next(), 1000d, pt2));

			if (i2.hasNext())
				transactionItems3.add(testCallCreateTransactionPaySalesTax(i2
						.next(), 1000d, pt3));

			if (i2.hasNext())
				transactionItems4.add(testCallCreateTransactionPaySalesTax(i2
						.next(), 1000d, pt4));

			if (i2.hasNext())
				transactionItems5.add(testCallCreateTransactionPaySalesTax(i2
						.next(), 1000d, pt5));

			List<PaySalesTax> pts = new ArrayList<PaySalesTax>();

			if (transactionItems1.size() > 0)
				pts.add(testCallCreatePaySalesTax(company, today(),
						getRandomPayFromAccount(payFromAccounts),
						AccounterConstants.PAYMENT_METHOD_CASH, true,
						transactionItems1, pt1, codes));
			// pts.add(testCallCreatePaySalesTax(company, today(),
			// accounterDao.getAccount(company.getId(), "Account LL"),
			// AccounterConstants.PAYMENT_METHOD_CASH, true, transactionItems1,
			// pt1, codes));

			if (transactionItems2.size() > 0)
				pts.add(testCallCreatePaySalesTax(company, today(),
						getRandomPayFromAccount(payFromAccounts),
						AccounterConstants.PAYMENT_METHOD_CASH, true,
						transactionItems2, pt2, codes));

			if (transactionItems3.size() > 0)
				pts.add(testCallCreatePaySalesTax(company, today(),
						getRandomPayFromAccount(payFromAccounts),
						AccounterConstants.PAYMENT_METHOD_CASH, true,
						transactionItems3, pt3, codes));
			//
			// if (transactionItems4.size() > 0)
			// pts.add(testCallCreatePaySalesTax(company, today(),
			// getRandomPayFromAccount(payFromAccounts),
			// AccounterConstants.PAYMENT_METHOD_CASH, true, transactionItems4,
			// pt4, codes));
			//
			// if (transactionItems5.size() > 0)
			// pts.add(testCallCreatePaySalesTax(company, today(),
			// getRandomPayFromAccount(payFromAccounts),
			// AccounterConstants.PAYMENT_METHOD_CASH, true, transactionItems5,
			// pt5, codes));

			if (createMore) {
				PaySalesTax pt6 = new PaySalesTax();
				PaySalesTax pt7 = new PaySalesTax();
				PaySalesTax pt8 = new PaySalesTax();
				PaySalesTax pt9 = new PaySalesTax();
				PaySalesTax pt10 = new PaySalesTax();

				List<TransactionPaySalesTax> transactionItems6 = new ArrayList<TransactionPaySalesTax>();
				List<TransactionPaySalesTax> transactionItems7 = new ArrayList<TransactionPaySalesTax>();
				List<TransactionPaySalesTax> transactionItems8 = new ArrayList<TransactionPaySalesTax>();
				List<TransactionPaySalesTax> transactionItems9 = new ArrayList<TransactionPaySalesTax>();
				List<TransactionPaySalesTax> transactionItems10 = new ArrayList<TransactionPaySalesTax>();

				if (i2.hasNext())
					transactionItems6.add(testCallCreateTransactionPaySalesTax(
							i2.next(), 1000d, pt6));
				if (i2.hasNext())
					transactionItems7.add(testCallCreateTransactionPaySalesTax(
							i2.next(), 1000d, pt7));
				if (i2.hasNext())
					transactionItems8.add(testCallCreateTransactionPaySalesTax(
							i2.next(), 1000d, pt8));
				if (i2.hasNext())
					transactionItems9.add(testCallCreateTransactionPaySalesTax(
							i2.next(), 1000d, pt9));
				if (i2.hasNext())
					transactionItems10
							.add(testCallCreateTransactionPaySalesTax(
									i2.next(), 1000d, pt10));

				if (transactionItems6.size() > 0)
					pts.add(testCallCreatePaySalesTax(company, today(),
							getRandomPayFromAccount(payFromAccounts),
							AccounterConstants.PAYMENT_METHOD_CASH, true,
							transactionItems6, pt6, codes));
				if (transactionItems7.size() > 0)
					pts.add(testCallCreatePaySalesTax(company, today(),
							getRandomPayFromAccount(payFromAccounts),
							AccounterConstants.PAYMENT_METHOD_CASH, true,
							transactionItems7, pt7, codes));
				if (transactionItems8.size() > 0)
					pts.add(testCallCreatePaySalesTax(company, today(),
							getRandomPayFromAccount(payFromAccounts),
							AccounterConstants.PAYMENT_METHOD_CASH, true,
							transactionItems8, pt8, codes));
				if (transactionItems9.size() > 0)
					pts.add(testCallCreatePaySalesTax(company, today(),
							getRandomPayFromAccount(payFromAccounts),
							AccounterConstants.PAYMENT_METHOD_CASH, true,
							transactionItems9, pt9, codes));
				if (transactionItems10.size() > 0)
					pts.add(testCallCreatePaySalesTax(company, today(),
							getRandomPayFromAccount(payFromAccounts),
							AccounterConstants.PAYMENT_METHOD_CASH, true,
							transactionItems10, pt10, codes));
			}

			if (checkTesting) {
				Iterator<PaySalesTax> i = pts.iterator();

				PaySalesTax c;
				List<TransactionPaySalesTax> ti, ti2 = new ArrayList<TransactionPaySalesTax>();
				while (i.hasNext()) {
					c = (PaySalesTax) i.next();
					assertNotNull(c);
					assertNotNull(c.getTotal());
					assertEquals(c.getTotal(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getDate(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getDate());
					assertEquals(c.getTotal(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getType(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getType());
					assertEquals(c.getNumber(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionPaySalesTax();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionPaySalesTax> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionPaySalesTax it = (TransactionPaySalesTax) j
								.next();
						ti2.add(it);
						Account acc = null;
						acc = it.getTaxAgency().getLiabilityAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getAmountToPay();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getAmountToPay();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getAmountToPay();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getAmountToPay();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase

					}// while there is a transaction item

					// checking whether the Accounts Payable account is updated.
					if (c.getPayFrom().isIncrease()) {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] += c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] -= c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

					// If the last transaction item of the last EnterBill is
					// done, // // we need to check the accounts table whether
					// all the
					// accounts
					// are updated perfectly.
					if (!i.hasNext()) {
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
					}
				}
			}
		}
	}

	public void testCreateExpense() throws DAOException, ParseException {
		if (createExpense) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Account> accounts = accounterDao.getAccounts(company.getId());

			setDefaultAccountVariables(accounts, company.getId());

			Expense e = new Expense();
			e.setBillFrom("ABC");
			e.setBillDate(format.parse(dueDate(-1)));
			e.setCompany(company);
			e.setDate(format.parse(today()));
			e.setAmountDue(5000D);
			e.setPaidAmount(1000D);
			e.setTotal(e.getAmountDue() + e.getPaidAmount());
			List<TransactionItem> tis = new ArrayList<TransactionItem>();
			TransactionItem ti = new TransactionItem();
			ti.setAccount(accounterDao.getAccount(company.getId(),
					AccounterConstants.BANK_CHARGE));
			ti.setLineTotal(1000d);
			ti.setUnitPrice(1000D);
			ti.setTransaction(e);
			ti.setCompany(company);
			tis.add(ti);
			e.setTransactionItems(tis);

			accounter.createExpense(e);

		}
	}

	public void testCreatePayExpense() throws DAOException, ParseException {
		if (createPayExpense) {
			company = accounterDao.getCompany(1L);
			// This function declares a double array where all the present total
			// balances of all the accounts in the company are stored.
			// For every instruction, this array will get updated and later we
			// check these values with the stored values in the database.

			List<Expense> expenses = accounterDao.getUnPaidExpense(company
					.getId());

			Expense e = expenses.get(0);

			PayExpense p = new PayExpense();
			p.setTotal(500d);
			p.setPaidFrom(accounterDao.getAccount(company.getId(),
					AccounterConstants.UN_DEPOSITED_FUNDS));
			p.setCompany(company);

			List<TransactionPayExpense> transactionPayExpenses = new ArrayList<TransactionPayExpense>();

			TransactionPayExpense transactionPayExpense = new TransactionPayExpense();

			transactionPayExpense.setExpense(e);
			transactionPayExpense.setPayment(500);
			transactionPayExpense.setPayExpense(p);

			p.setTransactionPayExpenses(transactionPayExpenses);

			accounter.createPayExpense(p);
		}
	}

	public List<ReportList> testAddCustomersAndVendors(Company company)
			throws DAOException {

		List<ReportList> rls = new ArrayList<ReportList>();
		List<Customer> customers = accounterDao.getCustomers(company.getId());
		Iterator<Customer> i = customers.iterator();

		while (i.hasNext()) {
			Customer c = (Customer) i.next();
			ReportList rl = new ReportList();
			rl.setItemName(AccounterConstants.ACCOUNTS_RECEIVABLE);
			rl.setDate(c.getDate());
			rl.setMemo("Opening Balance");
			rl.setName("Opening Balance");
			rl.setTransactionId(-1L);
			rl.setGroupName(AccounterConstants.OPENING_BALANCE);
			rl.setFlag(true);
			rl.setAmount(c.getBalance());
			rls.add(rl);

			ReportList rl2 = new ReportList();
			rl2.setItemName(AccounterConstants.OPENING_BALANCE);
			rl2.setDate(c.getDate());
			rl2.setName("Opening Balance");
			rl2.setMemo(c.getName());
			rl2.setGroupName(AccounterConstants.ACCOUNTS_RECEIVABLE);
			rl2.setTransactionId(-1L);
			rl2.setFlag(true);
			rl2.setAmount(rl.getAmount());
			rls.add(rl2);

		}
		List<Vendor> vendors = accounterDao.getVendors(company.getId());
		Iterator<Vendor> j = vendors.iterator();

		while (j.hasNext()) {
			Vendor c = (Vendor) j.next();
			ReportList rl = new ReportList();
			rl.setItemName(AccounterConstants.ACCOUNTS_PAYABLE);
			rl.setDate(c.getDate());
			rl.setTransactionId(-1L);
			rl.setName("Opening Balance");
			rl.setGroupName(AccounterConstants.OPENING_BALANCE);
			rl.setFlag(true);
			rl.setMemo("Opening Balance");
			rl.setAmount(c.getBalance());
			rls.add(rl);

			ReportList rl2 = new ReportList();
			rl2.setItemName(AccounterConstants.OPENING_BALANCE);
			rl2.setDate(c.getDate());
			rl2.setName("Opening Balance");
			rl2.setTransactionId(-1L);
			rl2.setMemo(c.getName());
			rl2.setGroupName(AccounterConstants.ACCOUNTS_PAYABLE);
			rl2.setFlag(true);
			rl2.setAmount(-c.getBalance());
			rls.add(rl2);
		}
		return rls;
	}

	public void testAddAccounts(Company company, List<ReportList> rls)
			throws DAOException {

		List<Account> accounts = accounterDao.getAccounts(company.getId());
		Iterator<Account> i = accounts.iterator();
		while (i.hasNext()) {
			Account c = (Account) i.next();
			if (c.getOpeningBalance() != 0.0
					&& !c.getName().equals(
							AccounterConstants.ACCOUNTS_RECEIVABLE)
					&& !c.getName().equals(AccounterConstants.ACCOUNTS_PAYABLE)) {
				ReportList rl = new ReportList();
				rl.setItemName(c.getName());
				rl.setDate(c.getAsOf());
				rl.setTransactionId(c.getId());
				rl.setName("Opening Balance");
				rl.setMemo("Opening Balance");
				rl.setGroupName(AccounterConstants.OPENING_BALANCE);
				rl.setFlag(true);
				rl.setTransactionId(-1L);
				rl.setAmount(c.getOpeningBalance());
				rls.add(rl);

				ReportList rl2 = new ReportList();
				rl2.setItemName(AccounterConstants.OPENING_BALANCE);
				rl2.setDate(c.getAsOf());
				rl2.setName("Opening Balance");
				rl2.setMemo(c.getName());
				rl2.setGroupName(c.getName());
				rl2.setTransactionId(-1L);
				rl2.setAmount(rl.getAmount());
				rl2.setFlag(true);
				rl2.setAmount(c.isIncrease() ? -c.getOpeningBalance() : c
						.getOpeningBalance());
				rls.add(rl2);
			}
		}
	}

	public ReportList reportListObject(List<ReportList> rls, String name) {

		if (rls.size() > 0) {
			ListIterator<ReportList> i = rls.listIterator();
			while (i.hasNext()) {
				ReportList rl = (ReportList) i.next();
				if (rl.getItemName().equals(name))
					return rl;
			}
		}
		return (new ReportList());
	}

	public void setAccountRegisterCashSales(Company company,
			List<ReportList> rls) throws DAOException {

		List<CashSales> cashsales = accounterDao.getCashSales(company.getId());
		Iterator<CashSales> i = cashsales.iterator();

		CashSales c;
		List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = (CashSales) i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				ti2.add(it);
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else if (it.getType() == TransactionItem.TYPE_SALESTAX)
					acc = it.getTaxCode().getTaxAgency().getLiabilityAccount();
				else
					acc = it.getAccount();
				System.out.println("In AccountCashsales, account="
						+ acc.getName());

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				rl.setMemo("");
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease()) {// if account is increase
					rl.setAmount(rl.getAmount() + it.getLineTotal());
				}// if for account is increase

				else {// if for account is not increase
					rl.setAmount(rl.getAmount() - it.getLineTotal());
				}// if for account is not increase
				rls2.add(rl);

				{
					if (it.taxGroup != null) {
						Set<TaxCode> tcs = it.taxGroup.getTaxCodes();
						Iterator<TaxCode> k = tcs.iterator();

						while (k.hasNext()) {
							TaxCode tc = (TaxCode) k.next();
							double rate = getTaxRate(tc) * it.lineTotal;

							ReportList rl2 = reportListObject(rls2, tc
									.getTaxAgency().getLiabilityAccount()
									.getName());
							rl2.setItemName(tc.getTaxAgency()
									.getLiabilityAccount().getName());
							rl2.setDate(c.getDate());
							rl2
									.setName(Utility.getTransactionName(c
											.getType()));
							rl2.setGroupName("Multiple");
							rl2.setMemo("");
							rl2.setTransactionId(c.getId());
							rl2.setFlag(true);
							if (tc.getTaxAgency().getLiabilityAccount()
									.isIncrease()) {
								rl2.setAmount(rl2.getAmount() + rate);
							} else {
								rl2.setAmount(rl2.getAmount() - rate);
							}
							rls2.add(rl);
						}
					}
				}
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			if (c.getDepositIn().isIncrease()) {
				ReportList rl = reportListObject(rls2, c.getDepositIn()
						.getName());
				rl.setItemName(c.getDepositIn().getName());
				rl.setDate(c.getDate());
				rl.setMemo("");
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				rl.setAmount(rl.getAmount() - c.getTotal());
				rls2.add(rl);
			} else {
				ReportList rl = reportListObject(rls2, c.getDepositIn()
						.getName());
				rl.setItemName(c.getDepositIn().getName());
				rl.setDate(c.getDate());
				rl.setMemo("");
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setAmount(rl.getAmount() + c.getTotal());
				rl.setTransactionId(c.getId());
				rls2.add(rl);
			}

			Iterator<ReportList> l = rls2.iterator();

			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}

	}

	public void setAccountRegisterInvoices(Company company, List<ReportList> rls)
			throws DAOException {

		List<Invoice> invoices = accounterDao.getInvoices(company.getId());
		Iterator<Invoice> i = invoices.iterator();

		Invoice c;
		List<TransactionItem> ti;
		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = (Invoice) i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item

				TransactionItem it = (TransactionItem) j.next();
				{
					Account acc = null;
					if (it.getType() == TransactionItem.TYPE_ITEM)
						acc = it.getItem().getIncomeAccount();
					else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
						acc = it.getAccount();
					else if (it.getType() == TransactionItem.TYPE_SALESTAX)
						acc = it.getTaxCode().getTaxAgency()
								.getLiabilityAccount();
					else
						acc = it.getAccount();

					ReportList rl = reportListObject(rls2, acc.getName());

					rl.setItemName(acc.getName());
					rl.setDate(c.getDate());
					rl.setName(Utility.getTransactionName(c.getType()));
					rl.setGroupName("Multiple");
					rl.setFlag(true);
					rl.setTransactionId(c.getId());
					rl.setMemo("");

					// If the isIncrease() value is true, then the account value
					// is
					// to be increased, else decreased.
					if (acc.isIncrease()) {// if account is increase
						rl.setAmount(rl.getAmount() + it.getLineTotal());
					}// if for account is increase

					else {// if for account is not increase
						rl.setAmount(rl.getAmount() - it.getLineTotal());
					}// if for account is not increase
					rls2.add(rl);
				}
				{
					if (it.taxGroup != null) {
						Set<TaxCode> tcs = it.taxGroup.getTaxCodes();
						Iterator<TaxCode> k = tcs.iterator();

						while (k.hasNext()) {
							TaxCode tc = (TaxCode) k.next();
							double rate = getTaxRate(tc) * it.lineTotal;
							ReportList rl = reportListObject(rls2, tc
									.getTaxAgency().getLiabilityAccount()
									.getName());
							rl.setItemName(tc.getTaxAgency()
									.getLiabilityAccount().getName());
							rl.setDate(c.getDate());
							rl.setName(Utility.getTransactionName(c.getType()));
							rl.setMemo("");
							rl.setTransactionId(c.getId());
							rl.setGroupName("Multiple");
							rl.setFlag(true);
							if (tc.getTaxAgency().getLiabilityAccount()
									.isIncrease()) {
								rl.setAmount(rl.getAmount() + rate);
							} else {
								rl.setAmount(rl.getAmount() - rate);
							}
							rls2.add(rl);
						}
					}
				}
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2,
						AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setItemName(AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setMemo("");
				rl.setTransactionId(c.getId());
				rl.setFlag(true);
				rl.setAmount(rl.getAmount() + c.getTotal());
				rls2.add(rl);
			}

			Iterator l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterCustomerCreditMemos(Company company,
			List<ReportList> rls) throws DAOException {

		List<CustomerCreditMemo> ccms = accounterDao
				.getCustomerCreditMemos(company.getId());
		Iterator<CustomerCreditMemo> i = ccms.iterator();

		CustomerCreditMemo c;
		List<TransactionItem> ti;
		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = (CustomerCreditMemo) i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				{
					Account acc = null;
					if (it.getType() == TransactionItem.TYPE_ITEM)
						acc = it.getItem().getIncomeAccount();
					else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
						acc = it.getAccount();
					else if (it.getType() == TransactionItem.TYPE_SALESTAX)
						acc = it.getTaxCode().getTaxAgency()
								.getLiabilityAccount();
					else
						acc = it.getAccount();
					// System.out.println("In AccountCashsales, account="+acc.
					// getName
					// ());

					ReportList rl = reportListObject(rls2, acc.getName());
					rl.setItemName(acc.getName());
					rl.setDate(c.getDate());
					rl.setName(Utility.getTransactionName(c.getType()));
					rl.setGroupName("Multiple");
					rl.setFlag(true);
					rl.setMemo("");
					rl.setTransactionId(c.getId());
					// If the isIncrease() value is true, then the account value
					// is
					// to be increased, else decreased.
					if (acc.isIncrease()) {// if account is increase
						rl.setAmount(-it.getLineTotal() + rl.getAmount());
					}// if for account is increase

					else {// if for account is not increase
						rl.setAmount(it.getLineTotal() + rl.getAmount());
					}// if for account is not increase
					rls2.add(rl);
				}
				{
					if (it.taxGroup != null) {
						Set<TaxCode> tcs = it.taxGroup.getTaxCodes();
						Iterator<TaxCode> k = tcs.iterator();

						while (k.hasNext()) {
							TaxCode tc = (TaxCode) k.next();
							double rate = getTaxRate(tc) * it.lineTotal;
							ReportList rl = reportListObject(rls2, tc
									.getTaxAgency().getLiabilityAccount()
									.getName());
							rl.setItemName(tc.getTaxAgency()
									.getLiabilityAccount().getName());
							rl.setDate(c.getDate());
							rl.setName(Utility.getTransactionName(c.getType()));
							rl.setMemo("");
							rl.setGroupName("Multiple");
							rl.setFlag(true);
							rl.setTransactionId(c.getId());
							if (tc.getTaxAgency().getLiabilityAccount()
									.isIncrease()) {
								rl.setAmount(-rate + rl.getAmount());
							} else {
								rl.setAmount(rate + rl.getAmount());
							}
							rls2.add(rl);
						}
					}
				}
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2,
						AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setItemName(AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setMemo("");
				rl.setFlag(true);
				rl.setAmount(-c.getTotal() + rl.getAmount());
				rl.setTransactionId(c.getId());
				rls2.add(rl);
			}

			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}

	}

	public void setAccountRegisterCustomerRefunds(Company company,
			List<ReportList> rls) throws DAOException {

		List<CustomerRefund> ccms = accounterDao.getCustomerRefunds(company
				.getId());
		Iterator<CustomerRefund> i = ccms.iterator();

		CustomerRefund c;
		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = (CustomerRefund) i.next();
			{
				Account acc = c.getPayFrom();

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName(AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setFlag(true);
				// rl.setMemo("");
				rl.setTransactionId(c.getId());
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease()) {// if account is increase
					rl.setAmount(c.getTotal() + rl.getAmount());
				}// if for account is increase

				else {// if for account is not increase
					rl.setAmount(-c.getTotal() + rl.getAmount());
				}// if for account is not increase
				rls2.add(rl);
			}

			{
				ReportList rl = reportListObject(rls2,
						AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setItemName(AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setDate(c.getDate());
				rl.setTransactionId(c.getId());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setAmount(c.getTotal() + rl.getAmount());
				rls2.add(rl);
			}
			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterEnterBills(Company company,
			List<ReportList> rls) throws DAOException {

		List<EnterBill> ccms = accounterDao.getEnterBills(company.getId());
		Iterator<EnterBill> i = ccms.iterator();

		EnterBill c;
		List<TransactionItem> ti;
		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else
					acc = it.getAccount();

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setMemo("");
				rl.setTransactionId(c.getId());
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease()) {// if account is increase
					rl.setAmount(-it.getLineTotal() + rl.getAmount());
				}// if for account is increase

				else {// if for account is not increase
					rl.setAmount(it.getLineTotal() + rl.getAmount());
				}// if for account is not increase
				rls2.add(rl);
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2,
						AccounterConstants.ACCOUNTS_PAYABLE);
				rl.setItemName(AccounterConstants.ACCOUNTS_PAYABLE);
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				rl.setAmount(c.getTotal() + rl.getAmount());
				rls2.add(rl);
			}
			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterVendorCreditMemos(Company company,
			List<ReportList> rls) throws DAOException {

		List<VendorCreditMemo> ccms = accounterDao.getVendorCreditMemos(company
				.getId());
		Iterator<VendorCreditMemo> i = ccms.iterator();
		VendorCreditMemo c;
		List<TransactionItem> ti;

		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();

			c = i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else
					acc = it.getAccount();

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setMemo("");
				rl.setTransactionId(c.getId());
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease()) {// if account is increase
					rl.setAmount(it.getLineTotal() + rl.getAmount());
				}// if for account is increase

				else {// if for account is not increase
					rl.setAmount(-it.getLineTotal() + rl.getAmount());
				}// if for account is not increase
				rls2.add(rl);
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2,
						AccounterConstants.ACCOUNTS_PAYABLE);
				rl.setItemName(AccounterConstants.ACCOUNTS_PAYABLE);
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				rl.setAmount(-c.getTotal() + rl.getAmount());
				rls2.add(rl);
			}
			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterCashPurchases(Company company,
			List<ReportList> rls) throws DAOException {

		List<CashPurchase> ccms = accounterDao
				.getCashPurchases(company.getId());
		Iterator<CashPurchase> i = ccms.iterator();

		CashPurchase c;
		List<TransactionItem> ti;

		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else
					acc = it.getAccount();

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setMemo("");
				rl.setTransactionId(c.getId());
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease()) {// if account is increase
					rl.setAmount(-it.getLineTotal() + rl.getAmount());
				}// if for account is increase
				else {// if for account is not increase
					rl.setAmount(it.getLineTotal() + rl.getAmount());
				}// if for account is not increase
				rls2.add(rl);
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2, c.getPayFrom().getName());
				rl.setItemName(c.getPayFrom().getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				if (c.getPayFrom().isIncrease())
					rl.setAmount(c.getTotal() + rl.getAmount());
				else
					rl.setAmount(-c.getTotal() + rl.getAmount());
				rls2.add(rl);
			}
			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}

	}

	public void setAccountRegisterWriteChecks(Company company,
			List<ReportList> rls) throws DAOException {

		List<WriteCheck> wcs = accounterDao.getWriteChecks(company.getId());
		Iterator<WriteCheck> i = wcs.iterator();

		WriteCheck w;
		List<TransactionItem> ti;

		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			w = i.next();
			ti = w.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else if (it.getType() == TransactionItem.TYPE_SALESTAX)
					acc = it.getTaxCode().getTaxAgency().getLiabilityAccount();
				else
					acc = it.getAccount();

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(w.getDate());
				rl.setName(Utility.getTransactionName(w.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setMemo("");
				rl.setTransactionId(w.getId());
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease()) {// if account is increase
					rl.setAmount(-it.getLineTotal() + rl.getAmount());
				}// if for account is increase
				else {// if for account is not increase
					rl.setAmount(it.getLineTotal() + rl.getAmount());
				}// if for account is not increase
				rls2.add(rl);
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2, w.getBankAccount()
						.getName());
				rl.setItemName(w.getBankAccount().getName());
				rl.setDate(w.getDate());
				rl.setName(Utility.getTransactionName(w.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(w.getId());
				if (w.getBankAccount().isIncrease())
					rl.setAmount(w.getTotal() + rl.getAmount());
				else
					rl.setAmount(-w.getTotal() + rl.getAmount());
				rls2.add(rl);
			}
			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterMakeDeposits(Company company,
			List<ReportList> rls) throws DAOException {

		List<MakeDeposit> wcs = accounterDao.getMakeDeposits(company.getId());
		Iterator<MakeDeposit> i = wcs.iterator();

		MakeDeposit c;
		List<TransactionMakeDeposit> ti;

		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = (MakeDeposit) i.next();

			// Take all the transaction items that are involved in the
			// writecheck and store them in a List.
			ti = c.getTransactionMakeDeposit();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionMakeDeposit> j = ti.iterator();
			Account acc = null;

			while (j.hasNext()) {// while there is a transaction item
				TransactionMakeDeposit it = (TransactionMakeDeposit) j.next();
				if (it.getIsNewEntry()) {
					if (it.getType() == TransactionMakeDeposit.TYPE_CUSTOMER)
						acc = accounterDao.getAccount(company.getId(),
								"Accounts Receivable");
					else if (it.getType() == TransactionMakeDeposit.TYPE_VENDOR)
						acc = accounterDao.getAccount(company.getId(),
								"Accounts Payable");
					else
						acc = it.getAccount();
				} else
					acc = accounterDao.getAccount(company.getId(),
							"Un Deposited Funds");

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(c.getDate());
				rl.setName("Deposit");
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				if (acc.isIncrease()
						|| acc.getName().equals("Accounts Payable"))
					rl.setAmount(it.getAmount() + rl.getAmount());
				else
					rl.setAmount(-it.getAmount() + rl.getAmount());

				rls2.add(rl);
			}// while there is a transaction item

			{
				ReportList rl = reportListObject(rls2, c.getDepositIn()
						.getName());
				rl.setItemName(c.getDepositIn().getName());
				rl.setDate(c.getDate());
				rl.setName("Deposit");
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				rl.setAmount(c.getTotal() + rl.getAmount());
				rls2.add(rl);

			}

			acc = c.getCashBackAccount();
			ReportList rl = reportListObject(rls2, acc.getName());
			rl.setItemName(acc.getName());
			rl.setDate(c.getDate());
			rl.setName("Deposit");
			rl.setGroupName("Multiple");
			rl.setFlag(true);
			rl.setTransactionId(c.getId());
			if (acc.isIncrease())
				rl.setAmount(-c.getCashBackAmount() + rl.getAmount());
			else
				rl.setAmount(c.getCashBackAmount() + rl.getAmount());

			rls2.add(rl);
			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterReceivePayments(Company company,
			List<ReportList> rls) throws DAOException {

		List<ReceivePayment> rps = accounterDao.getReceivePayments(company
				.getId());
		Iterator<ReceivePayment> i = rps.iterator();

		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			ReceivePayment c = i.next();

			{
				ReportList rl = reportListObject(rls2,
						AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setItemName(AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setDate(c.getDate());
				rl.setName(AccounterConstants.TYPE_RECEIVE_PAYMENT);
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				rl.setAmount(-(c.getAmount() + c.getTotalCashDiscount() + c
						.getTotalWriteOff())
						+ rl.getAmount());
				rls2.add(rl);
			}

			List<TransactionReceivePayment> trp = c
					.getTransactionReceivePayment();
			Iterator<TransactionReceivePayment> i2 = trp.iterator();
			boolean flag = false;
			while (i2.hasNext()) {
				TransactionReceivePayment tp = i2.next();

				if (tp.getCashDiscount() != 0 && tp.getCashDiscount() > 0.0) {
					ReportList rl = reportListObject(rls2, tp
							.getDiscountAccount().getName());
					rl.setItemName(tp.getDiscountAccount().getName());
					rl.setDate(c.getDate());
					rl.setName(AccounterConstants.TYPE_RECEIVE_PAYMENT);
					rl.setGroupName("Multiple");
					rl.setFlag(true);
					rl.setTransactionId(c.getId());
					if (tp.getDiscountAccount().isIncrease()) {
						rl.setAmount(-tp.getCashDiscount() + rl.getAmount());
					} else {
						rl.setAmount(tp.getCashDiscount() + rl.getAmount());
					}
					flag = true;
					rls2.add(rl);
				}
				if (tp.getWriteOffAccount() != null && tp.getWriteOff() > 0.0) {
					ReportList rl = reportListObject(rls2, tp
							.getWriteOffAccount().getName());
					rl.setItemName(tp.getWriteOffAccount().getName());
					rl.setDate(c.getDate());
					rl.setName(AccounterConstants.TYPE_RECEIVE_PAYMENT);
					rl.setGroupName("Multiple");
					rl.setFlag(true);
					rl.setTransactionId(c.getId());
					if (tp.getWriteOffAccount().isIncrease()) {
						rl.setAmount(-tp.getWriteOff() + rl.getAmount());
					} else {
						rl.setAmount(tp.getWriteOff() + rl.getAmount());
					}
					flag = true;
					rls2.add(rl);
				}
			}

			{
				ReportList rl = reportListObject(rls2, c.getDepositIn()
						.getName());
				rl.setItemName(c.getDepositIn().getName());
				rl.setDate(c.getDate());
				rl.setName(AccounterConstants.TYPE_RECEIVE_PAYMENT);
				if (flag)
					rl.setGroupName("Multiple");
				else
					rl.setGroupName(AccounterConstants.ACCOUNTS_RECEIVABLE);
				rl.setTransactionId(c.getId());
				rl.setFlag(true);

				if (c.getDepositIn().isIncrease())
					rl.setAmount(-c.getAmount() + rl.getAmount());
				else
					rl.setAmount(c.getAmount() + rl.getAmount());

				rls2.add(rl);
			}

			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterPayBills(Company company, List<ReportList> rls)
			throws DAOException {

		List<PayBill> pbs = accounterDao.getPayBills(company.getId());
		Iterator<PayBill> i = pbs.iterator();

		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();

			PayBill r = i.next();
			if (r.getPayBillType() == PayBill.TYPE_PAYBILL) {
				{
					ReportList rl = reportListObject(rls2,
							AccounterConstants.ACCOUNTS_PAYABLE);
					rl.setItemName(AccounterConstants.ACCOUNTS_PAYABLE);
					rl.setDate(r.getDate());
					rl.setName(AccounterConstants.VENDOR_PAYMENT);
					rl.setGroupName("Multiple");
					rl.setFlag(true);
					rl.setTransactionId(r.getId());
					rl.setAmount(-(r.getTotal() + (getAmountTotalDiscount(r
							.getTransactionPayBill())))
							+ rl.getAmount());
					rls2.add(rl);
				}

				List<TransactionPayBill> trp = r.getTransactionPayBill();
				Iterator<TransactionPayBill> i2 = trp.iterator();
				boolean flag = false;
				while (i2.hasNext()) {
					TransactionPayBill tp = i2.next();
					if (tp.getDiscountAccount() != null
							&& tp.getCashDiscount() > 0.0) {
						ReportList rl = reportListObject(rls2, tp
								.getDiscountAccount().getName());
						rl.setItemName(tp.getDiscountAccount().getName());
						rl.setDate(r.getDate());
						rl.setName(AccounterConstants.VENDOR_PAYMENT);
						rl.setGroupName("Multiple");
						rl.setFlag(true);
						rl.setTransactionId(r.getId());
						if (tp.getDiscountAccount().isIncrease())
							rl.setAmount(tp.getCashDiscount() + rl.getAmount());
						else
							rl
									.setAmount(-tp.getCashDiscount()
											+ rl.getAmount());
						flag = true;
						rls2.add(rl);
					}

				}
				ReportList rl = reportListObject(rls2, r.getPayFrom().getName());
				rl.setItemName(r.getPayFrom().getName());
				rl.setDate(r.getDate());
				rl.setName(AccounterConstants.VENDOR_PAYMENT);
				if (flag)
					rl.setGroupName("Multiple");
				else
					rl.setGroupName(AccounterConstants.ACCOUNTS_PAYABLE);
				rl.setTransactionId(r.getId());
				rl.setFlag(true);
				if (r.getPayFrom().isIncrease())
					rl.setAmount(r.getTotal() + rl.getAmount());
				else
					rl.setAmount(-r.getTotal() + rl.getAmount());
				rls2.add(rl);

			} else {
				{
					ReportList rl = reportListObject(rls2,
							AccounterConstants.ACCOUNTS_PAYABLE);
					rl.setItemName(AccounterConstants.ACCOUNTS_PAYABLE);
					rl.setDate(r.getDate());
					rl.setTransactionId(r.getId());
					rl.setName(AccounterConstants.VENDOR_PAYMENT);
					rl.setGroupName("Multiple");
					rl.setFlag(true);
					rl.setAmount(-(r.getTotal()) + rl.getAmount());
					rls2.add(rl);
				}

				ReportList rl = reportListObject(rls2, r.getPayFrom().getName());
				rl.setItemName(r.getPayFrom().getName());
				rl.setDate(r.getDate());
				rl.setName(AccounterConstants.VENDOR_PAYMENT);
				rl.setGroupName(AccounterConstants.ACCOUNTS_PAYABLE);
				rl.setTransactionId(r.getId());
				rl.setFlag(true);
				if (r.getPayFrom().isIncrease())
					rl.setAmount(r.getTotal() + rl.getAmount());
				else
					rl.setAmount(-r.getTotal() + rl.getAmount());
				rls2.add(rl);
			}

			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterCreditCardCharges(Company company,
			List<ReportList> rls) throws DAOException {
		List<CreditCardCharge> ccms = accounterDao.getCreditCardCharges(company
				.getId());
		Iterator<CreditCardCharge> i = ccms.iterator();

		CreditCardCharge c;
		List<TransactionItem> ti;
		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else
					acc = it.getAccount();

				ReportList rl = reportListObject(rls2, acc.getName());
				rl.setItemName(acc.getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setMemo("");
				rl.setTransactionId(c.getId());
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease()) {// if account is increase
					rl.setAmount(-it.getLineTotal() + rl.getAmount());
				}// if for account is increase

				else {// if for account is not increase
					rl.setAmount(it.getLineTotal() + rl.getAmount());
				}// if for account is not increase
				rls2.add(rl);
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2, c.getPayFrom().getName());
				rl.setItemName(c.getPayFrom().getName());
				rl.setDate(c.getDate());
				rl.setName(Utility.getTransactionName(c.getType()));
				rl.setGroupName("Multiple");
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				if (c.getPayFrom().isIncrease())
					rl.setAmount(c.getTotal() + rl.getAmount());
				else
					rl.setAmount(-c.getTotal() + rl.getAmount());

				rls2.add(rl);
			}

			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterTransferFunds(Company company,
			List<ReportList> rls) throws DAOException {
		List<TransferFund> ccms = accounterDao
				.getTransferFunds(company.getId());
		Iterator<TransferFund> i = ccms.iterator();

		TransferFund c;
		while (i.hasNext()) {
			List<ReportList> rls2 = new ArrayList<ReportList>();
			c = i.next();

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			{
				ReportList rl = reportListObject(rls2, c.getTransferTo()
						.getName());
				rl.setItemName(c.getTransferTo().getName());
				rl.setDate(c.getDate());
				rl.setName("Fund Transfer");
				rl.setGroupName(c.getTransferFrom().getName());
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				if (c.getTransferTo().isIncrease())
					rl.setAmount(-c.getTotal() + rl.getAmount());
				else
					rl.setAmount(c.getTotal() + rl.getAmount());
				rls2.add(rl);
			}

			{
				ReportList rl = reportListObject(rls2, c.getTransferFrom()
						.getName());
				rl.setItemName(c.getTransferFrom().getName());
				rl.setDate(c.getDate());
				rl.setName("Fund Transfer");
				rl.setGroupName(c.getTransferTo().getName());
				rl.setFlag(true);
				rl.setTransactionId(c.getId());
				if (c.getTransferFrom().isIncrease())
					rl.setAmount(c.getTotal() + rl.getAmount());
				else
					rl.setAmount(-c.getTotal() + rl.getAmount());
				rls2.add(rl);
			}

			Iterator<ReportList> l = rls2.iterator();
			while (l.hasNext()) {
				rls.add((ReportList) l.next());
			}
		}
	}

	public void setAccountRegisterPaySalesTaxes(Company company,
			List<ReportList> rls) throws DAOException {

		List<PaySalesTax> ccms;
		try {
			ccms = accounterDao.getPaySalesTaxes(company.getId());

			Iterator<PaySalesTax> i = ccms.iterator();

			PaySalesTax c;
			List<TransactionPaySalesTax> ti;
			while (i.hasNext()) {
				List<ReportList> rls2 = new ArrayList<ReportList>();
				c = i.next();

				ti = c.getTransactionPaySalesTax();
				Iterator<TransactionPaySalesTax> j = ti.iterator();
				while (j.hasNext()) {
					TransactionPaySalesTax tp = (TransactionPaySalesTax) j
							.next();
					ReportList rl = reportListObject(rls2, tp.getTaxAgency()
							.getLiabilityAccount().getName());
					rl.setItemName(tp.getTaxAgency().getLiabilityAccount()
							.getName());
					rl.setDate(c.getDate());
					rl.setName("Tax Agent Payment");
					rl.setGroupName("Multiple");
					rl.setFlag(true);
					rl.setTransactionId(c.getId());
					if (tp.getTaxAgency().getLiabilityAccount().isIncrease())
						rl.setAmount(-tp.getAmountToPay() + rl.getAmount());
					else
						rl.setAmount(tp.getAmountToPay() + rl.getAmount());

					rls2.add(rl);
				}

				{
					ReportList rl = reportListObject(rls2, c.getPayFrom()
							.getName());
					rl.setItemName(c.getPayFrom().getName());
					rl.setDate(c.getDate());
					rl.setName("Tax Agent Payment");
					rl.setGroupName("Multiple");
					rl.setFlag(true);

					if (c.getPayFrom().isIncrease())
						rl.setAmount(-c.getTotal() + rl.getAmount());
					else
						rl.setAmount(c.getTotal() + rl.getAmount());
					rl.setTransactionId(c.getId());
					rls2.add(rl);
				}

				Iterator<ReportList> l = rls2.iterator();
				while (l.hasNext()) {
					rls.add((ReportList) l.next());
				}
			}
		} catch (Exception e) {

			System.out.println("No Sales Tax is paid in the company");
		}

	}

	public void testCallAccountRegister() throws Exception {

		if (checkAccountRegister) {
			company = accounterDao.getCompany(1L);
			List<Account> accounts = accounterDao.getAccounts(company.getId());
			// ReportList name-> type transaction
			// groupname-> Account(multiple/account name)
			// itemname -> account's account register.
			// memo -> account's memo -> Name

			List<ReportList> rls = testAddCustomersAndVendors(company);
			testAddAccounts(company, rls);
			try {
				if (createCashSales)
					setAccountRegisterCashSales(company, rls);
				if (createInvoices)
					setAccountRegisterInvoices(company, rls);
				if (createCustomerCreditMemos)
					setAccountRegisterCustomerCreditMemos(company, rls);
				if (createCustomerRefunds)
					setAccountRegisterCustomerRefunds(company, rls);
				if (createEnterBills)
					setAccountRegisterEnterBills(company, rls);
				if (createVendorCreditMemos)
					setAccountRegisterVendorCreditMemos(company, rls);
				if (createCashPurchases)
					setAccountRegisterCashPurchases(company, rls);
				if (createWriteChecks)
					setAccountRegisterWriteChecks(company, rls);
				if (createMakeDeposits)
					setAccountRegisterMakeDeposits(company, rls);
				if (createReceivePayments)
					setAccountRegisterReceivePayments(company, rls);
				if (createPayBills || createVendorPayments)
					setAccountRegisterPayBills(company, rls);
				if (createCreditCardCharges)
					setAccountRegisterCreditCardCharges(company, rls);
				if (createTransferFunds)
					setAccountRegisterTransferFunds(company, rls);
				if (createPaySalesTaxes)
					setAccountRegisterPaySalesTaxes(company, rls);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Iterator<Account> ai = accounts.iterator();

			while (ai.hasNext()) {
				Account a = ai.next();
				int count = 0;
				System.out.println("a=name" + a.getName());
				List<AccountRegister> ars = accounterReportDAOService
						.getAccountRegister(company.getId(), "2009-01-01",
								today(), a.getId());
				Iterator<AccountRegister> j = ars.iterator();
				while (j.hasNext()) {
					AccountRegister ar = (AccountRegister) j.next();
					if (ar != null) {
						for (int i = 0; i < rls.size(); i++) {
							ReportList rl = rls.get(i);
							if (rl.getTransactionId() == 24)
								System.out.println("Here");
							System.out.println("Rl.Id=" + rl.getTransactionId()
									+ " ar.Id=" + ar.getTransactionId());
							if (rl.getTransactionId() == ar.getTransactionId()
									||
									// ((rl.getItemName().equals( //
									// AccounterConstants.ACCOUNTS_RECEIVABLE)
									// ||
									//rl.getItemName().equals(AccounterConstants
									// .ACCOUNTS_PAYABLE) ||
									//rl.getItemName().equals(AccounterConstants
									// .OPENING_BALANCE)) &&
									(rl.getTransactionId() < 0 && rl.getName()
											.equals("Opening Balance"))) {
								// count2++;
								if (rl.getItemName().equals(a.getName())
										&& a
												.getName()
												.equals(
														AccounterConstants.CASH_DISCOUNT_GIVEN))
									System.out.println("Here");
								System.out
										.println(" here rl.getGroupName()="
												+ rl.getGroupName()
												+ " ar.getAccount()="
												+ ar.getAccount());
								if (rl.getGroupName().equals(ar.getAccount())) {
									System.out.println("rl.name= "
											+ rl.getName() + " ar.getType="
											+ ar.getType() + " rl.getFlag="
											+ rl.getFlag());
									if (rl.getName().equals(ar.getType())
											&& rl.getFlag()) {
										if (rl
												.getName()
												.equals(
														AccounterConstants.TYPE_INVOICE))
											System.out
													.println(" here rl.getname()="
															+ rl.getName());
										System.out.println("Rl.memo="
												+ rl.getMemo() + " ar.getmemo="
												+ ar.getMemo());
										if ((rl.getMemo() == null
												|| ar.getMemo() == null || (rl
												.getMemo().equals(ar.getMemo()))))
										// if(rl.getMemo().equals(ar.getMemo()))
										{
											System.out
													.println(" here rl.getItemname()="
															+ rl.getItemName()
															+ " ar.getname()="
															+ a.getName());
											if (rl.getItemName().equals(
													"Account Cash"))
												System.out
														.println(" Came here");
											if (rl.getItemName().equals(
													a.getName())) {
												assertEquals(
														rl.getItemName()
																+ ", "
																+ rl.getName()
																+ ", "
																+ rl
																		.getGroupName()
																+ ", "
																+ rl.getMemo()
																+ ar
																		.getTransactionId(),
														rl.getAmount(), ar
																.getAmount());
												rl.setFlag(false);
												count++;
												break;
											}
										}
									}
								}
							}
						}
					}
				}
				System.out.println("count= " + count + " ars.size= "
						+ ars.size() + " a.name=" + a.getName());
				// assertEquals(a.getName(), count2, ars.size());
				assertEquals(a.getName(), count, ars.size());
			}
		}
	}

	// List <SalesTaxLiability> stl=
	// accounterReportDAOService.getSalesTaxLiabilityReport(company,
	// dueDate(-5), dueDate(1));
	// System.out.println("sales Tax liability size="+ stl.size());
	//			  
	// List<TransactionDetailByAccount>
	// tds=accounterReportDAOService.getTransactionDetailByAccount(company,
	// dueDate(-5), dueDate(1));
	// System.out.println("transactin detail by Account  size="+ tds.size());
	//			
	// System.out.println("Hi");

	// public void testReportSalesByItemDetail() throws DAOException{
	//			
	// ArrayList <ReportList> itemDetails= new ArrayList<ReportList>();
	//			
	// //itemDetails.add()
	// testSetItemsCashSales(itemDetails);
	// testSetItemsInvoices(itemDetails);
	// testSetItemsCustomerCreditMemos(itemDetails);
	// //testSetItemsCustomerRefunds(itemDetails);
	// company=accounterDao.getCompany(1l);
	// List <SalesByCustomerDetail>
	// idl=accounterReportDAOService.getSalesByItemDetail(company, dueDate(-2),
	// today());
	// Iterator<SalesByCustomerDetail> i=idl.iterator();
	// assertEquals(itemDetails.size() , idl.size());
	// while(i.hasNext())
	// {
	// SalesByCustomerDetail scd=(SalesByCustomerDetail) i.next();
	// Iterator<ReportList> j=itemDetails.iterator();
	// while(j.hasNext())
	// {
	// ReportList sid=(ReportList) j.next();
	// if(sid.getFlag()&&(scd.getType()==sid.getTransactionType()))
	// {
	// if(scd.getItemName().equals(sid.getItemName()))
	// {
	// if(scd.getItemType()==sid.getItemType())
	// {
	// assertEquals(scd.getQuantity(), sid.getQuantity());
	// assertEquals(scd.getAmount(),sid.getAmount());
	// sid.setFlag(false);
	// break;
	// }
	// }
	// }
	// if(!j.hasNext())
	// assertNotNull("All items are not included here", null);
	// }
	// }
	// Iterator<ReportList> temp=itemDetails.iterator();
	//			
	// while(temp.hasNext())
	// {
	// temp.next().setFlag(true);
	// }
	// //Testing SelesByItemSummary
	// List <SalesByCustomerDetail>
	// isl=accounterReportDAOService.getSalesByItemSummary(company, dueDate(-2),
	// today());
	// Iterator<SalesByCustomerDetail> i2=isl.iterator();
	// while(i2.hasNext())
	// {
	// SalesByCustomerDetail scd=(SalesByCustomerDetail) i2.next();
	// assertEquals(scd.getQuantity(),getQuantity(itemDetails,
	// scd.getItemName()));
	// assertEquals(scd.getAmount(),getAmount(itemDetails,scd.getItemName()));
	// }
	// }
	//	 
	// public void testSalesByCustomerDetail() throws DAOException
	// {
	//		 		
	// ArrayList <ReportList> customerDetails= new ArrayList<ReportList>();
	//			
	// //itemDetails.add()
	// testSetAccountsCashSales(customerDetails);
	// testSetAccountsInvoices(customerDetails);
	// testSetAccountsCustomerCreditMemos(customerDetails);
	//		
	//			
	// List<SalesByCustomerDetail>
	//cdl=accounterReportDAOService.getSalesByCustomerDetailReport(company.getId
	// (),
	// dueDate(-2), today());
	// Iterator <SalesByCustomerDetail> i3=cdl.iterator();
	// assertEquals(cdl.size(),customerDetails.size());
	// while(i3.hasNext())
	// {
	// SalesByCustomerDetail scd=i3.next();
	//				
	// Iterator<ReportList> j3=customerDetails.iterator();
	// while(j3.hasNext())
	// {
	// ReportList sid=(ReportList) j3.next();
	// if(sid.getFlag()&&(scd.getType()==sid.getTransactionType()))
	// {
	// if(scd.getName().equals(sid.getName()))
	// {
	// if(scd.getItemName().equals(sid.getItemName()))
	// {
	// assertEquals(scd.getQuantity(), sid.getQuantity());
	// assertEquals(scd.getAmount(),sid.getAmount());
	// sid.setFlag(false);
	// break;
	// }
	// }
	// }
	// if(!j3.hasNext())
	// assertNotNull("All customers are not included here", null);
	// }
	//				
	// }
	// Iterator<ReportList> temp=customerDetails.iterator();
	// while(temp.hasNext())
	// {
	// temp.next().setFlag(true);
	// }
	// //Testing Sales By Customer Summary
	// List <SalesByCustomerDetail>
	// csl=accounterReportDAOService.getSalesByCustomerSummary(company.getId(),
	// dueDate(-2), today());
	// Iterator<SalesByCustomerDetail> i4=csl.iterator();
	//			
	// while(i4.hasNext())
	// {
	// SalesByCustomerDetail scd=(SalesByCustomerDetail) i4.next();
	// Iterator<ReportList> j4=customerDetails.iterator();
	// while(j4.hasNext())
	// {
	// ReportList sid=(ReportList) j4.next();
	// if(scd.getName()==sid.getName())
	// {
	// assertEquals(scd.getGroupName(), sid.getGroupName());
	// assertEquals(scd.getAmount(), getCustomerSummaryAmount(customerDetails,
	// scd.getName()));
	// break;
	// }
	// if(!j4.hasNext())
	// assertNotNull("** All Customers are not included in the list **",null);
	// }
	// }
	// }
	//	
	// public double getAmount(ArrayList<ReportList> itemDetails,
	// String itemName) {
	// double amount=0.0;
	// Iterator<ReportList> i=itemDetails.iterator();
	// while(i.hasNext())
	// {
	// ReportList s= i.next();
	// if(s.getItemName().equals(itemName))
	// amount+=s.getAmount();
	// }
	// return amount;
	// }
	//	
	// public double getCustomerSummaryAmount(ArrayList<ReportList> itemDetails,
	// String name) {
	//
	// double amount=0.0;
	// Iterator<ReportList> i=itemDetails.iterator();
	// while(i.hasNext())
	// {
	// ReportList s= i.next();
	// if(s.getName().equals(name))
	// amount+=s.getAmount();
	// }
	// return amount;
	// }
	//
	// public double getQuantity(ArrayList<ReportList> itemDetails, String
	// itemName) {
	//
	// double q=0.0;
	//		
	// Iterator<ReportList> i=itemDetails.iterator();
	// while(i.hasNext())
	// {
	// ReportList s= i.next();
	// if(s.getItemName().equals(itemName))
	// q+=s.getQuantity();
	// }
	// return q;
	// }
	//
	// public void testSetItemsCashSales(ArrayList<ReportList> itemDetails)
	// throws DAOException {
	//			
	// Company company=accounterDao.getCompany(1l);
	// List <CashSales>cashsales= accounterDao.getCashSales(company.getId());
	// Iterator<CashSales> itr=cashsales.iterator();
	// while(itr.hasNext())
	// {
	// CashSales c=(CashSales) itr.next();
	// List<TransactionItem> tis=c.getTransactionItems();
	// Iterator<TransactionItem> j=tis.iterator();
	// while(j.hasNext())
	// {
	// TransactionItem ti=(TransactionItem) j.next();
	// if(ti.getType()==TransactionItem.TYPE_ITEM)
	// {
	// ReportList i= new ReportList();
	// setItemValues(c.getId(),c.getType(), c.getCustomer(), ti, i);
	// itemDetails.add(i);
	// }
	// }
	// }
	// }
	//	
	// public void testSetAccountsCashSales(ArrayList<ReportList> itemDetails)
	// throws DAOException {
	//
	// Company company=accounterDao.getCompany(1l);
	// List <CashSales>cashsales= accounterDao.getCashSales(company.getId());
	// Iterator<CashSales> itr=cashsales.iterator();
	// while(itr.hasNext())
	// {
	// CashSales c=(CashSales) itr.next();
	// List<TransactionItem> tis=c.getTransactionItems();
	// Iterator<TransactionItem> j=tis.iterator();
	// while(j.hasNext())
	// {
	// TransactionItem ti=(TransactionItem) j.next();
	// if(ti.getType()!=TransactionItem.TYPE_SALESTAX)
	// {
	// ReportList i= new ReportList();
	// setItemValues(c.getId(),c.getType(), c.getCustomer(),ti, i);
	// itemDetails.add(i);
	// }
	// }
	// }
	// }
	//	
	// public void testSetItemsInvoices(ArrayList<ReportList> itemDetails)
	// throws DAOException {
	//			
	// Company company=accounterDao.getCompany(1l);
	// ArrayList <Invoice>invoices=(ArrayList<Invoice>)
	// accounterDao.getInvoices(company.getId());
	//			
	// Iterator<Invoice> itr=invoices.iterator();
	// while(itr.hasNext())
	// {
	// Invoice c=(Invoice) itr.next();
	//				
	// List<TransactionItem> tis=c.getTransactionItems();
	// Iterator<TransactionItem> j=tis.iterator();
	// while(j.hasNext())
	// {
	// TransactionItem ti=(TransactionItem) j.next();
	// if(ti.getType()==TransactionItem.TYPE_ITEM)
	// {
	// ReportList i= new ReportList();
	// setItemValues(c.getId(), c.getType(), c.getCustomer(),ti, i);
	// itemDetails.add(i);
	// }
	// }
	//				
	// }
	//			
	// }
	//	
	// public void testSetAccountsInvoices(ArrayList<ReportList> itemDetails)
	// throws DAOException {
	//		
	// Company company=accounterDao.getCompany(1l);
	// ArrayList <Invoice>invoices=(ArrayList<Invoice>)
	// accounterDao.getInvoices(company.getId());
	//		
	// Iterator<Invoice> itr=invoices.iterator();
	// while(itr.hasNext())
	// {
	// Invoice c=(Invoice) itr.next();
	// List<TransactionItem> tis=c.getTransactionItems();
	// Iterator<TransactionItem> j=tis.iterator();
	// while(j.hasNext())
	// {
	// TransactionItem ti=(TransactionItem) j.next();
	// if(ti.getType()!=TransactionItem.TYPE_SALESTAX)
	// {
	// ReportList i= new ReportList();
	// setItemValues(c.getId(), c.getType(), c.getCustomer(), ti, i);
	// itemDetails.add(i);
	// }
	// }
	//			
	// }
	//		
	// }
	//	
	// public void setItemValues( long id, int type, Customer c, TransactionItem
	// t, ReportList i){
	//			
	// i.setTransactionId(id);
	// i.setTransactionType(type);
	// if(t.getType()==TransactionItem.TYPE_SALESTAX)
	// System.out.println("HI");
	//i.setItemName(t.getType()==TransactionItem.TYPE_ITEM?t.getItem().getName()
	// :(t.getType()==TransactionItem.TYPE_ACCOUNT?t.getAccount().getName():t.
	// getTaxCode().getTaxAgency().getLiabilityAccount().getName()));
	// i.setQuantity(type==Transaction.TYPE_CUSTOMER_CREDIT_MEMO?(t.getType()==
	// TransactionItem
	// .TYPE_SALESTAX?1.0:-t.getQuantity()):(t.getType()==TransactionItem
	// .TYPE_SALESTAX?1.0:t.getQuantity()));
	// i.setFlag(true);
	//i.setItemType(t.getType()==TransactionItem.TYPE_ITEM?t.getItem().getType()
	// :(t.getType()==TransactionItem.TYPE_ACCOUNT?t.getAccount().getType():t.
	// getTaxCode().getTaxAgency().getLiabilityAccount().getType()));
	// i.setName(c.getName());
	// i.setGroupName(c.getCustomerGroup().getName());
	// i.setOpeningBalance(c.getCustomerOpeningBalance());
	// i.setBalance(c.getBalance());
	// i.setDiscount(i.getDiscount()+t.getType()==TransactionItem.TYPE_ITEM?t.
	// getDiscount():0.0);
	//i.setAmount(i.getQuantity()*(t.getType()==TransactionItem.TYPE_SALESTAX?t.
	// getLineTotal():t.getUnitPrice()));
	// }
	//	
	// public void testSetItemsCustomerCreditMemos(ArrayList<ReportList>
	// itemDetails) throws DAOException {
	//			
	// company=accounterDao.getCompany(1l);
	// ArrayList <CustomerCreditMemo>ccms=(ArrayList<CustomerCreditMemo>)
	// accounterDao.getCustomerCreditMemos(company.getId());
	// Iterator<CustomerCreditMemo> itr=ccms.iterator();
	// while(itr.hasNext())
	// {
	// CustomerCreditMemo c=(CustomerCreditMemo) itr.next();
	//			
	// List<TransactionItem> tis=c.getTransactionItems();
	// Iterator<TransactionItem> j=tis.iterator();
	// while(j.hasNext())
	// {
	// TransactionItem ti=(TransactionItem) j.next();
	// if(ti.getType()==TransactionItem.TYPE_ITEM)
	// {
	// ReportList i= new ReportList();
	// setItemValues(c.getId(), c.getType(), c.getCustomer(),ti, i);
	// itemDetails.add(i);
	// }
	// }
	// }
	// }
	//	
	// public void testSetAccountsCustomerCreditMemos(ArrayList<ReportList>
	// itemDetails) throws DAOException {
	//			
	// company=accounterDao.getCompany(1l);
	// ArrayList <CustomerCreditMemo>ccms=(ArrayList<CustomerCreditMemo>)
	// accounterDao.getCustomerCreditMemos(company.getId());
	// Iterator<CustomerCreditMemo> itr=ccms.iterator();
	// while(itr.hasNext())
	// {
	// CustomerCreditMemo c=(CustomerCreditMemo) itr.next();
	//			
	// List<TransactionItem> tis=c.getTransactionItems();
	// Iterator<TransactionItem> j=tis.iterator();
	// while(j.hasNext())
	// {
	// TransactionItem ti=(TransactionItem) j.next();
	// if(ti.getType()!=TransactionItem.TYPE_SALESTAX)
	// {
	// ReportList i= new ReportList();
	// setItemValues(c.getId(), c.getType(), c.getCustomer(), ti, i);
	// itemDetails.add(i);
	// }
	// }
	// }
	// }
	//	
	// public void testSetAllCashSales(ArrayList<ReportList> itemDetails) throws
	// DAOException {
	//		
	// company=accounterDao.getCompany(1l);
	// ArrayList <CashSales>ccms=(ArrayList<CashSales>)
	// accounterDao.getCashSales(company.getId());
	// Iterator<CashSales> itr=ccms.iterator();
	// while(itr.hasNext())
	// {
	// CashSales c=(CashSales) itr.next();
	//			
	// ReportList i= new ReportList();
	// i.setTransactionId(c.getId());
	// i.setName(c.getCustomer().getName());
	// i.setBalance(c.getCustomer().getBalance());
	// i.setDiscount(0.0);
	// i.setAmount(c.getTotal());
	// i.setFlag(true);
	// i.setTransactionType(c.getType());
	// i.setWriteOff(0.0);
	// i.setStatus(c.getStatus());
	// i.setDate(c.getDate());
	// itemDetails.add(i);
	//			
	// }
	// }
	//	
	// public void testSetAllInvoices(ArrayList<ReportList> itemDetails) throws
	// DAOException {
	//		
	// company=accounterDao.getCompany(1l);
	// ArrayList <Invoice>ccms=(ArrayList<Invoice>)
	// accounterDao.getInvoices(company.getId());
	// Iterator<Invoice> itr=ccms.iterator();
	// while(itr.hasNext())
	// {
	// Invoice c=(Invoice) itr.next();
	//			
	// ReportList i= new ReportList();
	// i.setTransactionId(c.getId());
	// i.setName(c.getCustomer().getName());
	// i.setBalance(c.getCustomer().getBalance());
	// i.setDiscount(0.0);
	// i.setAmount(c.getTotal());
	// i.setFlag(true);
	// i.setTransactionType(c.getType());
	// i.setWriteOff(0.0);
	// i.setStatus(c.getStatus());
	// i.setDate(c.getDate());
	// itemDetails.add(i);
	// }
	// }
	//	
	// public void testSetAllCustomerCreditMemos(ArrayList<ReportList>
	// itemDetails) throws DAOException {
	//		
	// company=accounterDao.getCompany(1l);
	// ArrayList <CustomerCreditMemo>ccms=(ArrayList<CustomerCreditMemo>)
	// accounterDao.getCustomerCreditMemos(company.getId());
	// Iterator<CustomerCreditMemo> itr=ccms.iterator();
	// while(itr.hasNext())
	// {
	// CustomerCreditMemo c=(CustomerCreditMemo) itr.next();
	// ReportList i= new ReportList();
	// i.setTransactionId(c.getId());
	// i.setName(c.getCustomer().getName());
	// i.setBalance(c.getCustomer().getBalance());
	// i.setDiscount(0.0);
	// i.setAmount(c.getTotal());
	// i.setFlag(true);
	// i.setTransactionType(c.getType());
	// i.setWriteOff(0.0);
	// i.setStatus(c.getStatus());
	// i.setDate(c.getDate());
	// itemDetails.add(i);
	//			
	// }
	// }
	//
	// public void testSetAllReceivePayments(ArrayList<ReportList> sd) throws
	// DAOException
	// {
	// List <ReceivePayment>
	// rps=accounterDao.getReceivePayments(company.getId());
	// Iterator<ReceivePayment> itr=rps.iterator();
	// while(itr.hasNext())
	// {
	// ReceivePayment rp=(ReceivePayment) itr.next();
	// ReportList i=new ReportList();
	// i.setAmount(rp.getAmount());
	// i.setDiscount(rp.getTotalCashDiscount());
	// i.setWriteOff(rp.getTotalWriteOff());
	// i.setStatus(rp.getStatus());
	// i.setTransactionType(rp.getType());
	// i.setNumber(rp.getNumber());
	// i.setTransactionId(rp.getId());
	// i.setDiscount(rp.getTotalCashDiscount());
	// i.setName(rp.getCustomer().getName());
	// i.setFlag(true);
	// i.setBalance(rp.getCustomerBalance());
	// sd.add(i);
	// }
	// }
	//	
	// public void testSetAllCustomerRefunds(ArrayList<ReportList> sd) throws
	// DAOException
	// {
	// List<CustomerRefundsList>
	// rps=accounterGUIDao.getCustomerRefundsList(company.getId());
	// Iterator<CustomerRefundsList> itr=rps.iterator();
	// while(itr.hasNext())
	// {
	// CustomerRefundsList rp=(CustomerRefundsList) itr.next();
	//			 
	// ReportList i=new ReportList();
	// // i.setName(rp.getType()==Transaction.TYPE_CUSTOMER_REFUNDS?rp.)
	// if(rp.getType()==Transaction.TYPE_CUSTOMER_REFUNDS)
	// {
	// i.setName(rp.getName());
	// i.setDate(rp.getIssueDate());
	// i.setStatus(rp.getStatus());
	// i.setAmount(rp.getAmountPaid());
	// i.setTransactionType(rp.getType());
	// i.setTransactionId(rp.getTransactionId());
	// i.setFlag(true);
	// sd.add(i);
	// }
	// }
	// }
	//	
	// public void testCustomerTransactionHistory() throws DAOException
	// {
	// company= accounterDao.getCompany(1l);
	// //setAllCustomerHistoryValues();
	// ArrayList <ReportList> itemDetails= new ArrayList<ReportList>();
	//			
	// //itemDetails.add()
	// testSetAllCashSales(itemDetails);
	// testSetAllInvoices(itemDetails);
	// testSetAllCustomerCreditMemos(itemDetails);
	// testSetAllCustomerRefunds(itemDetails);
	// testSetAllCustomerWriteChecks(itemDetails);
	// testSetAllCustomerDeposits(itemDetails);
	// testSetAllReceivePayments(itemDetails);
	//		 	
	//
	//		 	
	// //List <TransactionHistory>
	//ctl=accounterReportDAOService.getCustomerTransactionHistory(company.getId(
	// ),
	// dueDate(-3), dueDate(1));
	//
	// List <TransactionHistory>
	//ctl=accounterReportDAOService.getCustomerTransactionHistory(company.getId(
	// ),
	// "2009-01-01", "2009-09-11");
	// Iterator<TransactionHistory> i=ctl.iterator();
	// assertEquals(ctl.size(),itemDetails.size());
	// while(i.hasNext())
	// {
	// TransactionHistory th=i.next();
	// Iterator<ReportList> j=itemDetails.iterator();
	// while(j.hasNext())
	// {
	// ReportList sl= (ReportList) j.next();
	// if(sl.getFlag()&&(sl.getAmount()==(th.getInvoicedAmount()==0.0?
	// th.getType
	//()==Transaction.TYPE_CUSTOMER_REFUNDS?-th.getPaidAmount():th.getPaidAmount
	// () :((th.getType()==Transaction.TYPE_CUSTOMER_CREDIT_MEMO||th.getType()==
	// Transaction
	// .TYPE_WRITE_CHECK||th.getType()==Transaction.TYPE_MAKE_DEPOSIT)
	// ?-th.getInvoicedAmount():th.getInvoicedAmount()))))
	// {
	// if(th.getName().equals(sl.getName()));
	// {
	//						
	// if(th.getType()==sl.getTransactionType())
	// {
	// if(th.getName().equals("Customer3"))
	// System.out.println("Here");
	// if(sl.getTransactionType()==Transaction.TYPE_CASH_SALES||sl.
	// getTransactionType
	// ()==Transaction.TYPE_INVOICE||sl.getTransactionType()==Transaction
	// .TYPE_CUSTOMER_REFUNDS
	// ||sl.getTransactionType()==Transaction.TYPE_WRITE_CHECK)
	// {
	// assertEquals(th.getDebit(),
	// sl.getAmount());//sl.getType()==Transaction.TYPE_CUSTOMER_REFUNDS
	// ?(-(sl.getAmount())): sl.getAmount());
	// assertTrue((sl.getTransactionType()==Transaction.TYPE_CASH_SALES||sl.
	// getTransactionType()==Transaction.TYPE_WRITE_CHECK)?
	// th.getDebit().toString().equals(th.getCredit().toString()):true);
	// }
	// else
	// assertEquals(th.getName(),th.getCredit(),
	// sl.getAmount()+sl.getDiscount()+
	// sl.getWriteOff());//sl.getType()==Transaction
	// .TYPE_CUSTOMER_CREDIT_MEMO?(-(sl.getAmount())): sl.getAmount());
	// System.out.println("type =" +sl.getTransactionType()+" "+
	// th.getType()+"/ id="+th.getTransactionId());
	// sl.setFlag(false);
	// break;
	//							
	// }
	// }
	//					
	// }
	// if(!j.hasNext())
	// {
	// System.out.println(" th customer =" + th.getName()+" th type ="+
	// th.getType());
	// assertNotNull("All customers are not included here", null);
	// }
	// }
	// }
	// Iterator<ReportList> temp=itemDetails.iterator();
	// while(temp.hasNext())
	// temp.next().setFlag(true);
	// }
	//	
	// public void testVendorTransactionHistory() throws DAOException
	// {
	// company= accounterDao.getCompany(1l);
	// ArrayList <ReportList> itemDetails= new ArrayList<ReportList>();
	//			
	// testSetAllEnterBills(itemDetails);
	// testSetAllVendorCreditMemos(itemDetails);
	// testSetAllCashPurchases(itemDetails);
	// testSetAllVendorWriteChecks(itemDetails);
	// testSetAllVendorDeposits(itemDetails);
	// testSetAllPayBills(itemDetails);
	// testSetAllCreditCardCharges(itemDetails);
	//	 	 
	//	 
	// List <TransactionHistory>
	//ctl=accounterReportDAOService.getVendorTransactionHistory(company.getId(),
	// dueDate(-3), today());
	// Iterator<TransactionHistory> i=ctl.iterator();
	// assertEquals(ctl.size(),itemDetails.size());
	// while(i.hasNext())
	// {
	// TransactionHistory th=i.next();
	// Iterator<ReportList> j=itemDetails.iterator();
	// while(j.hasNext())
	// {
	// ReportList sl= (ReportList) j.next();
	// if(sl.getFlag()&&(sl.getAmount()==(th.getInvoicedAmount()==0.0?
	// th.getPaidAmount()
	// :(th.getType()==Transaction.TYPE_VENDOR_CREDIT_MEMO?-th
	// .getInvoicedAmount():th.getInvoicedAmount()))))
	// {//th.getType().equals(Utility.getTransactionName(Transaction.
	// TYPE_VENDOR_CREDIT_MEMO))?-th.getPaidAmount():
	//					
	// if(th.getName().equals(sl.getName()));
	// {
	// if(th.getType()==sl.getTransactionType())
	// {
	// if(sl.getTransactionType()==Transaction.TYPE_CASH_PURCHASE||sl.
	// getTransactionType
	// ()==Transaction.TYPE_VENDOR_CREDIT_MEMO||sl.getTransactionType
	// ()==Transaction
	// .TYPE_CREDIT_CARD_CHARGE||sl.getTransactionType()==Transaction
	// .TYPE_WRITE_CHECK||sl.getTransactionType()==Transaction.TYPE_PAY_BILL)
	// {
	// assertEquals(th.getDebit(),
	// sl.getAmount()+sl.getDiscount());//sl.getType(
	// )==Transaction.TYPE_CUSTOMER_REFUNDS?(-(sl.getAmount())):
	// sl.getAmount());
	// assertTrue((sl.getTransactionType()==Transaction.TYPE_CASH_PURCHASE||sl.
	// getTransactionType
	// ()==Transaction.TYPE_WRITE_CHECK||sl.getTransactionType(
	// )==Transaction.TYPE_CREDIT_CARD_CHARGE)?
	// th.getDebit().doubleValue()==th.getCredit().doubleValue():true);
	// }
	// else
	// assertEquals(th.getCredit(),
	// sl.getAmount()+sl.getDiscount());//sl.getType
	// ()==Transaction.TYPE_CUSTOMER_CREDIT_MEMO?(-(sl.getAmount())):
	// sl.getAmount());
	// sl.setFlag(false);
	// break;
	// }
	// }
	// }
	// if(!j.hasNext())
	// assertNotNull("All customers are not included here", null);
	// }
	// }
	// Iterator<ReportList> temp=itemDetails.iterator();
	// while(temp.hasNext())
	// {
	// temp.next().setFlag(true);
	// }
	//		
	// }

	public void testSetAllEnterBills(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<EnterBill> ebs = (ArrayList<EnterBill>) accounterDao
				.getEnterBills(company.getId());
		Iterator<EnterBill> itr = ebs.iterator();

		while (itr.hasNext()) {
			EnterBill c = (EnterBill) itr.next();
			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getVendor().getName());
			i.setBalance(c.getVendor().getBalance());
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setStatus(c.getStatus());
			i.setDate(c.getDate());
			itemDetails.add(i);

		}
	}

	public void testSetAllCashPurchases(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<CashPurchase> ebs = (ArrayList<CashPurchase>) accounterDao
				.getCashPurchases(company.getId());
		Iterator<CashPurchase> itr = ebs.iterator();

		while (itr.hasNext()) {
			CashPurchase c = (CashPurchase) itr.next();
			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getVendor().getName());
			i.setBalance(c.getVendor().getBalance());
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setStatus(c.getStatus());
			i.setDate(c.getDate());
			itemDetails.add(i);

		}
	}

	public void testSetAllPayBills(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<PayBill> pbs = (ArrayList<PayBill>) accounterDao
				.getPayBills(company.getId());
		Iterator<PayBill> itr = pbs.iterator();

		while (itr.hasNext()) {
			PayBill c = (PayBill) itr.next();
			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getVendor().getName());
			i.setBalance(c.getVendor().getBalance());
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setStatus(c.getStatus());
			i.setDiscount(getAmountTotalDiscount(c.getTransactionPayBill()));
			i.setDate(c.getDate());
			itemDetails.add(i);
		}
	}

	public void testSetAllCreditCardCharges(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<CreditCardCharge> cccs = (ArrayList<CreditCardCharge>) accounterDao
				.getCreditCardCharges(company.getId());
		Iterator<CreditCardCharge> itr = cccs.iterator();

		while (itr.hasNext()) {
			CreditCardCharge c = (CreditCardCharge) itr.next();
			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getVendor().getName());
			i.setBalance(c.getVendor().getBalance());
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setStatus(c.getStatus());
			i.setDate(c.getDate());
			itemDetails.add(i);
		}
	}

	public void testSetAllVendorCreditMemos(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<VendorCreditMemo> ccms = (ArrayList<VendorCreditMemo>) accounterDao
				.getVendorCreditMemos(company.getId());
		Iterator<VendorCreditMemo> itr = ccms.iterator();

		while (itr.hasNext()) {
			VendorCreditMemo c = (VendorCreditMemo) itr.next();
			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getVendor().getName());
			i.setBalance(c.getVendor().getBalance());
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setWriteOff(0.0);
			i.setStatus(c.getStatus());
			i.setDate(c.getDate());
			itemDetails.add(i);

		}
	}

	public void testSetAllCustomerDeposits(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<MakeDeposit> mds = (ArrayList<MakeDeposit>) accounterDao
				.getMakeDeposits(company.getId());
		Iterator<MakeDeposit> itr = mds.iterator();
		while (itr.hasNext()) {
			MakeDeposit m = (MakeDeposit) itr.next();
			List<TransactionMakeDeposit> tmd = m.getTransactionMakeDeposit();
			Iterator<TransactionMakeDeposit> i2 = tmd.iterator();
			while (i2.hasNext()) {
				TransactionMakeDeposit td = (TransactionMakeDeposit) i2.next();
				if (td.getIsNewEntry()) {
					if (td.getCustomer() != null) {
						ReportList i = new ReportList();
						i.setTransactionId(m.getId());
						i.setName(td.getCustomer().getName());
						i.setBalance(td.getCustomer().getBalance());
						i.setAmount(td.getAmount());
						i.setFlag(true);
						i.setTransactionType(m.getType());
						i.setStatus(m.getStatus());
						i.setDate(m.getDate());
						itemDetails.add(i);
					}
				}
			}
		}
	}

	public void testSetAllVendorDeposits(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<MakeDeposit> mds = (ArrayList<MakeDeposit>) accounterDao
				.getMakeDeposits(company.getId());
		Iterator<MakeDeposit> itr = mds.iterator();
		while (itr.hasNext()) {
			MakeDeposit m = (MakeDeposit) itr.next();
			List<TransactionMakeDeposit> tmd = m.getTransactionMakeDeposit();
			Iterator<TransactionMakeDeposit> i2 = tmd.iterator();
			while (i2.hasNext()) {
				TransactionMakeDeposit td = (TransactionMakeDeposit) i2.next();
				if (td.getIsNewEntry()) {
					if (td.getVendor() != null) {
						ReportList i = new ReportList();
						i.setTransactionId(m.getId());
						i.setName(td.getVendor().getName());
						i.setBalance(td.getVendor().getBalance());
						i.setAmount(td.getAmount());
						i.setFlag(true);
						i.setTransactionType(m.getType());
						i.setStatus(m.getStatus());
						i.setDate(m.getDate());
						itemDetails.add(i);
					}
				}
			}
		}
	}

	public void testSetAllCustomerWriteChecks(ArrayList<ReportList> ids)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<WriteCheck> mds = (ArrayList<WriteCheck>) accounterDao
				.getWriteChecks(company.getId());
		Iterator<WriteCheck> itr = mds.iterator();
		while (itr.hasNext()) {
			WriteCheck m = (WriteCheck) itr.next();
			if (m.getCustomer() != null) {
				ReportList i = new ReportList();
				i.setName(m.getCustomer().getName());
				i.setBalance(m.getCustomer().getBalance());
				i.setTransactionId(m.getId());
				i.setAmount(m.getAmount());
				i.setFlag(true);
				i.setTransactionType(m.getType());
				i.setStatus(m.getStatus());
				i.setDate(m.getDate());
				ids.add(i);
			}
		}
	}

	public void testSetAllVendorWriteChecks(ArrayList<ReportList> ids)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<WriteCheck> mds = (ArrayList<WriteCheck>) accounterDao
				.getWriteChecks(company.getId());
		Iterator<WriteCheck> itr = mds.iterator();
		while (itr.hasNext()) {
			WriteCheck m = (WriteCheck) itr.next();
			if (m.getVendor() != null) {
				ReportList i = new ReportList();
				i.setName(m.getVendor().getName());
				i.setBalance(m.getVendor().getBalance());
				i.setTransactionId(m.getId());
				i.setAmount(m.getAmount());
				i.setFlag(true);
				i.setTransactionType(m.getType());
				i.setStatus(m.getStatus());
				i.setDate(m.getDate());
				ids.add(i);
			}
		}
	}

	// public int getAccountVariable(List<Account> accounts, String s,Company
	// company) throws DAOException
	// {
	// //For an Account name we will return the corresponding index in the
	// account array.
	//
	// for(int i=0;i<accounts.size();i++)
	// if(s.equals(accounts.get(i).getName()))
	// return i;
	//		   	
	// System.out.println("s=="+s+"/");
	// return -1;
	// }

	// public void setTrialBalanceOpeningBalanceVariable(List<Account> accounts,
	// long c)throws DAOException
	// {
	// company
	// =accounterDao.getCompany(1L,c);
	//		  
	//    	
	// }
	public void setTrialBalanceVendors(List<Account> accounts, long c)
			throws DAOException {
		company = accounterDao.getCompany(1L);
		List<Vendor> vendors = accounterDao.getVendors(company.getId());
		for (int i = 0; i < vendors.size(); i++) {
			trialBalance[getAccountVariable(accounts, company
					.getAccountsPayableAccount().getName(), company)] -= vendors
					.get(i).getOpeningBalance();
			trialBalance[getAccountVariable(accounts, company
					.getOpeningBalancesAccount().getName(), company)] += vendors
					.get(i).getOpeningBalance();
		}
	}

	public void setTrialBalanceCustomers(List<Account> accounts, long c)
			throws DAOException {
		company = accounterDao.getCompany(1L);
		List<Customer> customers = accounterDao.getCustomers(company.getId());
		for (int i = 0; i < customers.size(); i++) {
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] += customers
					.get(i).getOpeningBalance();
			trialBalance[getAccountVariable(accounts, company
					.getOpeningBalancesAccount().getName(), company)] -= customers
					.get(i).getOpeningBalance();
		}
	}

	public void setTrialBalanceAccountVariables(List<Account> accounts, long c)
			throws DAOException {

		company = accounterDao.getCompany(1L);
		trialBalance = new double[accounts.size()];
		double bal = 0;
		for (int i = 0; i < accounts.size(); i++) {
			String str = accounts.get(i).getName();
			trialBalance[i] = setTrailBalanceAccountOpeningBalance(str);
			bal += trialBalance[i];
		}
		trialBalance[getAccountVariable(accounts, "Opening Balances", company)] = -bal;
		if (createCustomers)
			setTrialBalanceCustomers(accounts, c);
		if (createVendors)
			setTrialBalanceVendors(accounts, c);
	}

	public double setTrailBalanceAccountOpeningBalance(String s)
			throws DAOException {

		company = accounterDao.getCompany(1L);
		return (accounterDao.getAccount(company.getId(), s).isIncrease() ? -accounterDao
				.getAccount(company.getId(), s).getOpeningBalance()
				: accounterDao.getAccount(company.getId(), s)
						.getOpeningBalance());

	}

	public void setTrialAllCashSales(List<Account> accounts, Company company,
			boolean isVoiding) throws DAOException {

		List<CashSales> cashsales = accounterDao.getCashSales(company.getId());
		Iterator<CashSales> i = cashsales.iterator();
		while (i.hasNext()) {
			CashSales c = (CashSales) i.next();
			// if((!isVoiding) || (isVoiding && !c.isVoid()))
			{
				List<TransactionItem> items = c.getTransactionItems();
				Iterator<TransactionItem> j = items.iterator();
				while (j.hasNext()) {
					TransactionItem t = (TransactionItem) j.next();
					trialBalance[getAccountVariable(
							accounts,
							t.getType() == TransactionItem.TYPE_ITEM ? t
									.getItem().getIncomeAccount().getName()
									: (t.getType() == TransactionItem.TYPE_ACCOUNT ? t
											.getAccount().getName()
											: t.getTaxCode().getTaxAgency()
													.getLiabilityAccount()
													.getName()), company)] -= t
							.getLineTotal();

					if (t.taxGroup != null) {
						Set<TaxCode> tcs = t.taxGroup.getTaxCodes();
						Iterator<TaxCode> k = tcs.iterator();

						while (k.hasNext()) {
							TaxCode tc = (TaxCode) k.next();
							trialBalance[getAccountVariable(accounts, tc
									.getTaxAgency().getLiabilityAccount()
									.getName(), company)] -= getTaxRate(tc)
									* t.lineTotal;
						}
					}
				}
				trialBalance[getAccountVariable(accounts, c.getDepositIn()
						.getName(), company)] += c.getTotal();
			}
		}

	}

	public void setTrialVoidCashSales(List<Account> accounts, Company company,
			CashSales c) throws DAOException {
		if (c != null) {
			List<TransactionItem> items = c.getTransactionItems();
			Iterator<TransactionItem> j = items.iterator();
			while (j.hasNext()) {
				TransactionItem t = (TransactionItem) j.next();
				trialBalance[getAccountVariable(
						accounts,
						t.getType() == TransactionItem.TYPE_ITEM ? t.getItem()
								.getIncomeAccount().getName()
								: (t.getType() == TransactionItem.TYPE_ACCOUNT ? t
										.getAccount().getName()
										: t.getTaxCode().getTaxAgency()
												.getLiabilityAccount()
												.getName()), company)] += t
						.getLineTotal();

				if (t.taxGroup != null) {
					Set<TaxCode> tcs = t.taxGroup.getTaxCodes();
					Iterator<TaxCode> k = tcs.iterator();

					while (k.hasNext()) {
						TaxCode tc = (TaxCode) k.next();
						trialBalance[getAccountVariable(accounts,
								tc.getTaxAgency().getLiabilityAccount()
										.getName(), company)] += getTaxRate(tc)
								* t.lineTotal;
					}
				}
			}
			trialBalance[getAccountVariable(accounts, c.getDepositIn()
					.getName(), company)] -= c.getTotal();
		}
	}

	public void setTrialAllInvoices(List<Account> accounts, Company company,
			boolean isVoiding) throws DAOException {

		List<Invoice> invoices = accounterDao.getInvoices(company.getId());
		Iterator<Invoice> i = invoices.iterator();
		while (i.hasNext()) {
			Invoice c = (Invoice) i.next();
			List<TransactionItem> items = c.getTransactionItems();
			Iterator<TransactionItem> j = items.iterator();
			while (j.hasNext()) {
				TransactionItem t = (TransactionItem) j.next();
				trialBalance[getAccountVariable(
						accounts,
						t.getType() == TransactionItem.TYPE_ITEM ? t.getItem()
								.getIncomeAccount().getName()
								: (t.getType() == TransactionItem.TYPE_ACCOUNT ? t
										.getAccount().getName()
										: t.getTaxCode().getTaxAgency()
												.getLiabilityAccount()
												.getName()), company)] -= t
						.getLineTotal();

				Set<TaxCode> tcs = t.taxGroup.getTaxCodes();
				Iterator<TaxCode> k = tcs.iterator();

				while (k.hasNext()) {
					TaxCode tc = (TaxCode) k.next();
					trialBalance[getAccountVariable(accounts, tc.getTaxAgency()
							.getLiabilityAccount().getName(), company)] -= getTaxRate(tc)
							* t.lineTotal;
				}
			}
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] += c
					.getTotal();
		}

	}

	public void setTrialVoidInvoices(List<Account> accounts, Company company,
			Invoice c) throws DAOException {
		if (c != null) {
			List<TransactionItem> items = c.getTransactionItems();
			Iterator<TransactionItem> j = items.iterator();
			while (j.hasNext()) {
				TransactionItem t = (TransactionItem) j.next();
				trialBalance[getAccountVariable(
						accounts,
						t.getType() == TransactionItem.TYPE_ITEM ? t.getItem()
								.getIncomeAccount().getName()
								: (t.getType() == TransactionItem.TYPE_ACCOUNT ? t
										.getAccount().getName()
										: t.getTaxCode().getTaxAgency()
												.getLiabilityAccount()
												.getName()), company)] += t
						.getLineTotal();

				Set<TaxCode> tcs = t.taxGroup.getTaxCodes();
				Iterator<TaxCode> k = tcs.iterator();

				while (k.hasNext()) {
					TaxCode tc = (TaxCode) k.next();
					trialBalance[getAccountVariable(accounts, tc.getTaxAgency()
							.getLiabilityAccount().getName(), company)] += getTaxRate(tc)
							* t.lineTotal;
				}
			}
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] -= c
					.getTotal();
		}
	}

	public void setTrialAllCustomerCreditMemos(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {

		List<CustomerCreditMemo> ccms = accounterDao
				.getCustomerCreditMemos(company.getId());
		Iterator<CustomerCreditMemo> i = ccms.iterator();
		while (i.hasNext()) {
			CustomerCreditMemo c = (CustomerCreditMemo) i.next();
			List<TransactionItem> items = c.getTransactionItems();
			Iterator<TransactionItem> j = items.iterator();
			while (j.hasNext()) {
				TransactionItem t = (TransactionItem) j.next();
				trialBalance[getAccountVariable(
						accounts,
						t.getType() == TransactionItem.TYPE_ITEM ? t.getItem()
								.getIncomeAccount().getName()
								: (t.getType() == TransactionItem.TYPE_ACCOUNT ? t
										.getAccount().getName()
										: t.getTaxCode().getTaxAgency()
												.getLiabilityAccount()
												.getName()), company)] += t
						.getLineTotal();

				Set<TaxCode> tcs = t.taxGroup.getTaxCodes();
				Iterator<TaxCode> k = tcs.iterator();

				while (k.hasNext()) {
					TaxCode tc = (TaxCode) k.next();
					trialBalance[getAccountVariable(accounts, tc.getTaxAgency()
							.getLiabilityAccount().getName(), company)] += getTaxRate(tc)
							* t.lineTotal;
				}
			}
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] -= c
					.getTotal();
		}

	}

	public void setTrialVoidCustomerCreditMemos(List<Account> accounts,
			Company company, CustomerCreditMemo c) throws DAOException {

		if (c != null) {
			List<TransactionItem> items = c.getTransactionItems();
			Iterator<TransactionItem> j = items.iterator();
			while (j.hasNext()) {
				TransactionItem t = (TransactionItem) j.next();
				trialBalance[getAccountVariable(
						accounts,
						t.getType() == TransactionItem.TYPE_ITEM ? t.getItem()
								.getIncomeAccount().getName()
								: (t.getType() == TransactionItem.TYPE_ACCOUNT ? t
										.getAccount().getName()
										: t.getTaxCode().getTaxAgency()
												.getLiabilityAccount()
												.getName()), company)] -= t
						.getLineTotal();

				Set<TaxCode> tcs = t.taxGroup.getTaxCodes();
				Iterator<TaxCode> k = tcs.iterator();

				while (k.hasNext()) {
					TaxCode tc = (TaxCode) k.next();
					trialBalance[getAccountVariable(accounts, tc.getTaxAgency()
							.getLiabilityAccount().getName(), company)] -= getTaxRate(tc)
							* t.lineTotal;
				}
			}
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] += c
					.getTotal();
		}
	}

	public void setTrialAllCustomerRefunds(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {

		List<CustomerRefund> cfs = accounterDao.getCustomerRefunds(company
				.getId());
		Iterator<CustomerRefund> i = cfs.iterator();
		while (i.hasNext()) {
			CustomerRefund c = i.next();
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] += c
					.getTotal();
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] -= c.getTotal();
		}
	}

	public void setTrialVoidCustomerRefunds(List<Account> accounts,
			Company company, CustomerRefund c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] -= c
					.getTotal();
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] += c.getTotal();
		}
	}

	public void setTrialAllReceivePayments(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {

		List<ReceivePayment> ccms = accounterDao.getReceivePayments(company
				.getId());
		Iterator<ReceivePayment> i = ccms.iterator();

		while (i.hasNext()) {
			ReceivePayment c = (ReceivePayment) i.next();
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] -= c
					.getAmount()
					+ c.getTotalCashDiscount() + c.getTotalWriteOff();
			trialBalance[getAccountVariable(accounts, c.getDepositIn()
					.getName(), company)] += c.getAmount();
			List<TransactionReceivePayment> trps = c
					.getTransactionReceivePayment();
			Iterator<TransactionReceivePayment> j = trps.iterator();

			while (j.hasNext()) {
				TransactionReceivePayment tp = j.next();
				{
					if (tp.getDiscountAccount() != null
							&& tp.getCashDiscount() > 0.0)
						trialBalance[getAccountVariable(accounts, tp
								.getDiscountAccount().getName(), company)] += tp
								.getCashDiscount();
					if (tp.getWriteOffAccount() != null
							&& tp.getWriteOff() > 0.0)
						trialBalance[getAccountVariable(accounts, tp
								.getWriteOffAccount().getName(), company)] += tp
								.getWriteOff();
				}
			}
		}
	}

	public void setTrialVoidReceivePayments(List<Account> accounts,
			Company company, ReceivePayment c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, company
					.getAccountsReceivableAccount().getName(), company)] += c
					.getAmount()
					+ c.getTotalCashDiscount() + c.getTotalWriteOff();
			trialBalance[getAccountVariable(accounts, c.getDepositIn()
					.getName(), company)] -= c.getAmount();
			List<TransactionReceivePayment> trps = c
					.getTransactionReceivePayment();
			Iterator<TransactionReceivePayment> j = trps.iterator();

			while (j.hasNext()) {
				TransactionReceivePayment tp = j.next();
				{
					if (tp.getDiscountAccount() != null
							&& tp.getCashDiscount() > 0.0)
						trialBalance[getAccountVariable(accounts, tp
								.getDiscountAccount().getName(), company)] -= tp
								.getCashDiscount();
					if (tp.getWriteOffAccount() != null
							&& tp.getWriteOff() > 0.0)
						trialBalance[getAccountVariable(accounts, tp
								.getWriteOffAccount().getName(), company)] -= tp
								.getWriteOff();
				}
			}
		}
	}

	public void setTrialAllEnterBills(List<Account> accounts, Company company,
			boolean voiding) throws DAOException {

		List<EnterBill> ccms = accounterDao.getEnterBills(company.getId());
		Iterator<EnterBill> i = ccms.iterator();
		while (i.hasNext()) {
			EnterBill c = (EnterBill) i.next();
			trialBalance[getAccountVariable(accounts, company
					.getAccountsPayableAccount().getName(), company)] -= c
					.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem tp = j.next();
				trialBalance[getAccountVariable(accounts,
						tp.getType() == TransactionItem.TYPE_ITEM ? tp
								.getItem().getExpenseAccount().getName() : tp
								.getAccount().getName(), company)] += tp
						.getLineTotal();
			}
		}
	}

	public void setTrialVoidEnterBills(List<Account> accounts, Company company,
			EnterBill c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, company
					.getAccountsPayableAccount().getName(), company)] += c
					.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem tp = j.next();
				trialBalance[getAccountVariable(accounts,
						tp.getType() == TransactionItem.TYPE_ITEM ? tp
								.getItem().getExpenseAccount().getName() : tp
								.getAccount().getName(), company)] -= tp
						.getLineTotal();
			}
		}
	}

	public void setTrialAllCashPurchases(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {

		List<CashPurchase> ccms = accounterDao
				.getCashPurchases(company.getId());
		Iterator<CashPurchase> i = ccms.iterator();
		while (i.hasNext()) {
			CashPurchase c = (CashPurchase) i.next();
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] -= c.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem tp = j.next();
				trialBalance[getAccountVariable(accounts,
						tp.getType() == TransactionItem.TYPE_ITEM ? tp
								.getItem().getExpenseAccount().getName() : tp
								.getAccount().getName(), company)] += tp
						.getLineTotal();
			}
		}
	}

	public void setTrialVoidCashPurchases(List<Account> accounts,
			Company company, CashPurchase c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] += c.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem tp = j.next();
				trialBalance[getAccountVariable(accounts,
						tp.getType() == TransactionItem.TYPE_ITEM ? tp
								.getItem().getExpenseAccount().getName() : tp
								.getAccount().getName(), company)] -= tp
						.getLineTotal();
			}
		}
	}

	public void setTrialAllVendorCreditMemos(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {

		List<VendorCreditMemo> ccms = accounterDao.getVendorCreditMemos(company
				.getId());
		Iterator<VendorCreditMemo> i = ccms.iterator();
		while (i.hasNext()) {
			VendorCreditMemo c = (VendorCreditMemo) i.next();
			trialBalance[getAccountVariable(accounts, company
					.getAccountsPayableAccount().getName(), company)] += c
					.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem tp = j.next();
				trialBalance[getAccountVariable(accounts,
						tp.getType() == TransactionItem.TYPE_ITEM ? tp
								.getItem().getExpenseAccount().getName() : tp
								.getAccount().getName(), company)] -= tp
						.getLineTotal();
			}
		}
	}

	public void setTrialVoidVendorCreditMemos(List<Account> accounts,
			Company company, VendorCreditMemo c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, company
					.getAccountsPayableAccount().getName(), company)] -= c
					.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem tp = j.next();
				trialBalance[getAccountVariable(accounts,
						tp.getType() == TransactionItem.TYPE_ITEM ? tp
								.getItem().getExpenseAccount().getName() : tp
								.getAccount().getName(), company)] += tp
						.getLineTotal();
			}
		}
	}

	public void setTrialAllVendorPayments(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {
		List<PayBill> ccms = accounterDao.getPayBills(company.getId());
		Iterator<PayBill> i = ccms.iterator();
		while (i.hasNext()) {
			PayBill c = (PayBill) i.next();
			trialBalance[getAccountVariable(accounts, company
					.getAccountsPayableAccount().getName(), company)] += c
					.getTotal()
					+ getAmountTotalDiscount(c.getTransactionPayBill());
			List<TransactionPayBill> tps = c.getTransactionPayBill();
			Iterator<TransactionPayBill> j = tps.iterator();
			while (j.hasNext()) {
				TransactionPayBill tp = (TransactionPayBill) j.next();
				if (tp.getDiscountAccount() != null
						&& tp.getCashDiscount() > 0.0)
					trialBalance[getAccountVariable(accounts, tp
							.getDiscountAccount().getName(), company)] -= tp
							.getCashDiscount();
			}
			{
				if (c.getType() == PayBill.TYPE_PAYBILL)
					trialBalance[getAccountVariable(accounts, c.getPayFrom()
							.getName(), company)] -= getAmountPayment(c
							.getTransactionPayBill());
				else
					trialBalance[getAccountVariable(accounts, c.getPayFrom()
							.getName(), company)] -= c.getTotal();
			}
		}
	}

	public void setTrialVoidVendorPayments(List<Account> accounts,
			Company company, PayBill c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, company
					.getAccountsPayableAccount().getName(), company)] -= c
					.getTotal()
					+ getAmountTotalDiscount(c.getTransactionPayBill());
			List<TransactionPayBill> tps = c.getTransactionPayBill();
			Iterator<TransactionPayBill> j = tps.iterator();
			while (j.hasNext()) {
				TransactionPayBill tp = (TransactionPayBill) j.next();
				if (tp.getDiscountAccount() != null
						&& tp.getCashDiscount() > 0.0)
					trialBalance[getAccountVariable(accounts, tp
							.getDiscountAccount().getName(), company)] += tp
							.getCashDiscount();
			}
			{
				if (c.getType() == PayBill.TYPE_PAYBILL)
					trialBalance[getAccountVariable(accounts, c.getPayFrom()
							.getName(), company)] += getAmountPayment(c
							.getTransactionPayBill());
				else
					trialBalance[getAccountVariable(accounts, c.getPayFrom()
							.getName(), company)] += c.getTotal();
			}
		}
	}

	public void setTrialAllWriteChecks(List<Account> accounts, Company company,
			boolean voiding) throws DAOException {
		List<WriteCheck> ccms = accounterDao.getWriteChecks(company.getId());
		Iterator<WriteCheck> i = ccms.iterator();
		while (i.hasNext()) {
			WriteCheck c = (WriteCheck) i.next();
			trialBalance[getAccountVariable(accounts, c.getBankAccount()
					.getName(), company)] -= c.getAmount();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem ti = (TransactionItem) j.next();
				trialBalance[getAccountVariable(
						accounts,
						ti.getType() == TransactionItem.TYPE_ITEM ? (c
								.getPayToType() == WriteCheck.TYPE_CUSTOMER ? ti
								.getItem().getIncomeAccount().getName()
								: ti.getItem().getExpenseAccount().getName())
								: (ti.getType() == TransactionItem.TYPE_ACCOUNT ? ti
										.getAccount().getName()
										: ti.getTaxCode().getTaxAgency()
												.getLiabilityAccount()
												.getName()), company)] += ti
						.getLineTotal();
			}
		}
	}

	public void setTrialVoidWriteChecks(List<Account> accounts,
			Company company, WriteCheck c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, c.getBankAccount()
					.getName(), company)] += c.getAmount();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem ti = (TransactionItem) j.next();
				trialBalance[getAccountVariable(
						accounts,
						ti.getType() == TransactionItem.TYPE_ITEM ? (c
								.getPayToType() == WriteCheck.TYPE_CUSTOMER ? ti
								.getItem().getIncomeAccount().getName()
								: ti.getItem().getExpenseAccount().getName())
								: (ti.getType() == TransactionItem.TYPE_ACCOUNT ? ti
										.getAccount().getName()
										: ti.getTaxCode().getTaxAgency()
												.getLiabilityAccount()
												.getName()), company)] -= ti
						.getLineTotal();
			}
		}
	}

	public void setTrialAllCreditCardCharges(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {

		List<CreditCardCharge> ccms = accounterDao.getCreditCardCharges(company
				.getId());
		Iterator<CreditCardCharge> i = ccms.iterator();
		while (i.hasNext()) {
			CreditCardCharge c = (CreditCardCharge) i.next();
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] -= c.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem ti = (TransactionItem) j.next();
				trialBalance[getAccountVariable(accounts,
						ti.getType() == TransactionItem.TYPE_ITEM ? ti
								.getItem().getExpenseAccount().getName() : ti
								.getAccount().getName(), company)] += ti
						.getLineTotal();
			}
		}
	}

	public void setTrialVoidCreditCardCharges(List<Account> accounts,
			Company company, CreditCardCharge c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] += c.getTotal();
			List<TransactionItem> tis = c.getTransactionItems();
			Iterator<TransactionItem> j = tis.iterator();
			while (j.hasNext()) {
				TransactionItem ti = (TransactionItem) j.next();
				trialBalance[getAccountVariable(accounts,
						ti.getType() == TransactionItem.TYPE_ITEM ? ti
								.getItem().getExpenseAccount().getName() : ti
								.getAccount().getName(), company)] -= ti
						.getLineTotal();
			}
		}
	}

	public void setTrialAllMakeDeposits(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {
		List<MakeDeposit> ccms = accounterDao.getMakeDeposits(company.getId());
		Iterator<MakeDeposit> i = ccms.iterator();
		while (i.hasNext()) {
			MakeDeposit c = (MakeDeposit) i.next();
			{
				trialBalance[getAccountVariable(accounts, c.getDepositIn()
						.getName(), company)] += c.getTotal();
				trialBalance[getAccountVariable(accounts, c
						.getCashBackAccount().getName(), company)] += c
						.getCashBackAmount();
			}

			List<TransactionMakeDeposit> tis = c.getTransactionMakeDeposit();
			Iterator<TransactionMakeDeposit> j = tis.iterator();
			while (j.hasNext()) {
				TransactionMakeDeposit ti = (TransactionMakeDeposit) j.next();
				trialBalance[getAccountVariable(
						accounts,
						ti.getIsNewEntry() ? ((ti.getType() == TransactionMakeDeposit.TYPE_CUSTOMER) ? company
								.getAccountsReceivableAccount().getName()
								: (ti.getType() == TransactionMakeDeposit.TYPE_VENDOR) ? company
										.getAccountsPayableAccount().getName()
										: ti.getAccount().getName())
								: AccounterConstants.UN_DEPOSITED_FUNDS,
						company)] -= ti.getAmount();
			}
		}
	}

	public void setTrialVoidMakeDeposits(List<Account> accounts,
			Company company, MakeDeposit c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, c.getDepositIn()
					.getName(), company)] -= c.getTotal();
			trialBalance[getAccountVariable(accounts, c.getCashBackAccount()
					.getName(), company)] -= c.getCashBackAmount();
			List<TransactionMakeDeposit> tis = c.getTransactionMakeDeposit();
			Iterator<TransactionMakeDeposit> j = tis.iterator();
			while (j.hasNext()) {
				TransactionMakeDeposit ti = (TransactionMakeDeposit) j.next();
				trialBalance[getAccountVariable(
						accounts,
						ti.getIsNewEntry() ? ((ti.getType() == TransactionMakeDeposit.TYPE_CUSTOMER) ? company
								.getAccountsReceivableAccount().getName()
								: (ti.getType() == TransactionMakeDeposit.TYPE_VENDOR) ? company
										.getAccountsPayableAccount().getName()
										: ti.getAccount().getName())
								: AccounterConstants.UN_DEPOSITED_FUNDS,
						company)] += ti.getAmount();
			}
		}
	}

	public void setTrialAllTransferFunds(List<Account> accounts,
			Company company, boolean voiding) throws DAOException {

		List<TransferFund> ccms = accounterDao
				.getTransferFunds(company.getId());
		Iterator<TransferFund> i = ccms.iterator();
		while (i.hasNext()) {
			TransferFund c = (TransferFund) i.next();
			{
				trialBalance[getAccountVariable(accounts, c.getTransferFrom()
						.getName(), company)] -= c.getTotal();
				trialBalance[getAccountVariable(accounts, c.getTransferTo()
						.getName(), company)] += c.getTotal();
			}
		}
	}

	public void setTrialVoidTransferFunds(List<Account> accounts,
			Company company, TransferFund c) throws DAOException {

		if (c != null) {
			trialBalance[getAccountVariable(accounts, c.getTransferFrom()
					.getName(), company)] += c.getTotal();
			trialBalance[getAccountVariable(accounts, c.getTransferTo()
					.getName(), company)] -= c.getTotal();
		}
	}

	public void setTrialAllPaySalesTaxes(List<Account> accounts,
			Company company, boolean voiding) throws DAOException,
			ParseException {

		List<PaySalesTax> pts = accounterDao.getPaySalesTaxes(company.getId());
		Iterator<PaySalesTax> i = pts.iterator();
		while (i.hasNext()) {
			PaySalesTax c = (PaySalesTax) i.next();
			List<TransactionPaySalesTax> tps = c.getTransactionPaySalesTax();
			Iterator<TransactionPaySalesTax> j = tps.iterator();
			while (j.hasNext()) {
				TransactionPaySalesTax tp = (TransactionPaySalesTax) j.next();
				TaxAgency t = tp.getTaxAgency();
				trialBalance[getAccountVariable(accounts, t
						.getLiabilityAccount().getName(), company)] += tp
						.getAmountToPay();
			}
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] -= c.getTotal();
		}
	}

	public void setTrialVoidPaySalesTaxes(List<Account> accounts,
			Company company, PaySalesTax c) throws DAOException, ParseException {

		if (c != null) {
			List<TransactionPaySalesTax> tps = c.getTransactionPaySalesTax();
			Iterator<TransactionPaySalesTax> j = tps.iterator();
			while (j.hasNext()) {
				TransactionPaySalesTax tp = (TransactionPaySalesTax) j.next();
				TaxAgency t = tp.getTaxAgency();
				trialBalance[getAccountVariable(accounts, t
						.getLiabilityAccount().getName(), company)] -= tp
						.getAmountToPay();
			}
			trialBalance[getAccountVariable(accounts, c.getPayFrom().getName(),
					company)] += c.getTotal();
		}
	}

	public CashSales testVoidCashSales(CashSales cashSales,
			List<Account> accounts, List<TaxCode> codes) throws DAOException {
		company = accounterDao.getCompany(1l);
		if (!cashSales.isVoid() && cashSales.canVoidOrEdit) {
			cashSales.setVoid(true);
			accounter.alterCashSales(cashSales);
			{

				// From here testing starts
				// All the CashSales are stored in a ArrayList, and we will
				// iterate
				// them.
				// For every CashSale, we perform necessary operations on the
				// accounts array and
				// atlast we check them with the actual values that are stored
				// in
				// the database.

				CashSales c = cashSales;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				{
					// c = (CashSales) i.next();
					assertNotNull(c);
					assertNotNull(c.getCustomer());
					assertNotNull(c.getCustomer().getName());
					assertEquals(c.getCustomer().getName(), accounterDao
							.getCashSales(company.getId(), c.getId())
							.getCustomer().getName());
					assertEquals(c.getDate(), accounterDao.getCashSales(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getDepositIn());
					assertNotNull(c.getDepositIn().getName());
					assertEquals(c.getDepositIn().getName(), accounterDao
							.getCashSales(company.getId(), c.getId())
							.getDepositIn().getName());
					assertEquals(c.getDiscountTotal(), accounterDao
							.getCashSales(company.getId(), c.getId())
							.getDiscountTotal());
					assertEquals(c.getType(), accounterDao.getCashSales(
							company.getId(), c.getId()).getType());
					assertEquals(c.getTotal(), accounterDao.getCashSales(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getSalesTax(), accounterDao.getCashSales(
							company.getId(), c.getId()).getSalesTax());
					if (c.getSalesPerson() != null)
						if (c.getSalesPerson().getFirstName() != null)
							assertEquals(c.getSalesPerson().getFirstName(),
									accounterDao.getCashSales(company.getId(),
											c.getId()).getSalesPerson()
											.getFirstName());
					if (c.getPaymentMethod() != null)
						if (c.getPaymentMethod() != null)
							assertEquals(c.getPaymentMethod(), accounterDao
									.getCashSales(company.getId(), c.getId())
									.getPaymentMethod());
					assertEquals(c.getNumber(), accounterDao.getCashSales(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getIncomeAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else if (it.getType() == TransactionItem.TYPE_SALESTAX)
							acc = it.getTaxCode().getTaxAgency()
									.getLiabilityAccount();
						else
							// if(it.getType()==TransactionItem.TYPE_SALESTAX)
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase
						if (it.getType() == TransactionItem.TYPE_SALESTAX)
							taxAgencies[getTaxAgencyVariable(codes, it
									.getTaxCode().getTaxAgency().getName(),
									company)] -= it.getLineTotal();

						if (it.taxGroup != null
								&& it.taxGroup.getTaxCodes() != null) {// if
							// taxgroup
							// is not
							// null
							Set<TaxCode> taxcodes = it.taxGroup.getTaxCodes();
							Iterator<TaxCode> i2 = taxcodes.iterator();
							while (i2.hasNext()) {// i2.hasNext
								TaxCode tc = (TaxCode) i2.next();
								double rate = 0.0;

								if (tc.getTaxRates() != null) {// taxrate ==
									// null

									Set<TaxRates> taxrates = tc.getTaxRates();
									Iterator<TaxRates> i3 = taxrates.iterator();

									Date date;// =new Date();
									if (i3.hasNext()) {// if i3.hasNext
										TaxRates tr = (TaxRates) i3.next();
										date = (Date) tr.getAsOf();
										rate = tr.getRate();
										while (i3.hasNext()) {
											// The date which is latest but not
											// after
											// the today's date is to be taken
											// and its taxrate must be used in
											// the
											// calculations.
											if (date.after(tr.getAsOf())) {
												date = tr.getAsOf();
												rate = tr.getRate();
											}
											tr = (TaxRates) i3.next();
										}// while i3.hasNext()
									}// if i3.hasNext()
									rate = (rate / 100.0) * it.lineTotal;
									// changing the taxagency's liability
									// account as per
									// the 'isIncrease()' value of the account.
									if (tc.getTaxAgency().getLiabilityAccount()
											.isIncrease()) {// taxAgency account
										// isIncrease

										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] -= rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] -= rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}// taxAgency account isIncrease
									else {
										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] += rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] += rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}
								}// taxrate == null

								taxAgencies[getTaxAgencyVariable(codes, tc
										.getTaxAgency().getName(), company)] -= rate;

							}// i2.hasNext
						}// if Taxgroup is not null

					}// while there is a transaction item

					// checking whether the depositIn account is updated. If it
					// has
					// a parent it also need to be updated
					if (!c.getDepositIn().isIncrease()) {
						d[getAccountVariable(accounts, c.getDepositIn()
								.getName(), company)] -= c.getTotal();
						String str = c.getDepositIn().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, c.getDepositIn()
								.getName(), company)] += c.getTotal();
						String str = c.getDepositIn().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					// Now we need to calculate the tax amount that the customer
					// had
					// to be paid.
					// For that we need to take the taxGroup in the cashsale
					// transaction
					// and checking the taxrates that are related with taxcodes
					// in
					// the taxgroup
					// And finally the liability account in the corresponding
					// taxagency must be updated...

					// If the last transaction item of the last CashSale is
					// done, we
					// need to check the accounts table
					// whether all the accounts are updated perfectly.
					{
						for (long l1 = 0; l1 < accounts.size(); l1++) {
							String name = accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName();
							// if(accounterDao.getAccount(company.getId(), (l1 +
							// 1)).getName().equals("SubWO"))
							System.out.println("here - " + name);
							assertEquals(name, d[getAccountVariable(accounts,
									name, company)], accounterDao.getAccount(
									company.getId(), name).getTotalBalance());
						}
						for (long l1 = 0; l1 < codes.size(); l1++) {
							String name = codes.get((int) l1).getTaxAgency()
									.getName();
							assertEquals(name,
									taxAgencies[getTaxAgencyVariable(codes,
											name, company)],
									accounterDao.getTaxAgency(company.getId(),
											name).getBalance());
						}

					}
				}

			}
			return cashSales;
		}
		return null;
	}

	public Invoice testVoidInvoice(Invoice invoice, List<Account> accounts,
			List<TaxCode> codes) throws DAOException {
		company = accounterDao.getCompany(1l);
		if (!invoice.isVoid() && invoice.canVoidOrEdit) {
			List<TransactionReceivePayment> trpsBackUp = null;
			List<TransactionCreditsAndPayments> tcpsBackUp = null;
			List<CreditsAndPayments> cpsBackUp = null;
			Invoice invoiceBackUp = new Invoice();
			invoiceBackUp.setStatus(invoice.getStatus());
			double unUsedAmount = 0;
			if (invoice.getTransactionReceivePayments() != null) {
				cpsBackUp = new ArrayList<CreditsAndPayments>();
				{
					List<CreditsAndPayments> cps1 = accounterGUIDao
							.getCustomerCreditsAndPayments(company.getId(),
									invoice.getCustomer().getId());
					for (int i = 0; i < cps1.size(); i++) {
						CreditsAndPayments cp = new CreditsAndPayments();
						cp.setBalance(cps1.get(i).getBalance());
						cp.setCreditAmount(cps1.get(i).getCreditAmount());
						cp.setMemo(cps1.get(i).getMemo());
						cp.setTransaction(cps1.get(i).getTransaction());
						cpsBackUp.add(cp);
					}
				}
				trpsBackUp = new ArrayList<TransactionReceivePayment>();
				Set<TransactionReceivePayment> trps = invoice
						.getTransactionReceivePayments();
				for (Iterator<TransactionReceivePayment> i = trps.iterator(); i
						.hasNext();) {
					TransactionReceivePayment trp1 = (TransactionReceivePayment) i
							.next();
					TransactionReceivePayment trp2 = new TransactionReceivePayment();
					trp2.setAppliedCredits(trp1.getAppliedCredits());
					trp2.setCashDiscount(trp1.getCashDiscount());
					trp2.setWriteOff(trp1.getWriteOff());
					trp2.setPayment(trp2.getPayment());
					if (trp1.getAppliedCredits() != 0.0) {
						tcpsBackUp = new ArrayList<TransactionCreditsAndPayments>();
						List<TransactionCreditsAndPayments> tcps = trp1
								.getTransactionCreditsAndPayments();

						for (Iterator<TransactionCreditsAndPayments> j = tcps
								.iterator(); j.hasNext();) {
							TransactionCreditsAndPayments tcp1 = (TransactionCreditsAndPayments) j
									.next();
							TransactionCreditsAndPayments tcp2 = new TransactionCreditsAndPayments();
							tcp2.setAmountToUse(tcp1.getAmountToUse());
							CreditsAndPayments cp = new CreditsAndPayments();
							cp.balance = tcp1.creditsAndPayments.balance;
							cp.creditAmount = tcp1.creditsAndPayments.creditAmount;
							cp.id = tcp1.creditsAndPayments.id;
							tcp2.creditsAndPayments = cp;
							tcp2.setCompany(tcp1.getCompany());
							tcp2.setId(tcp1.getId());
							tcp2.memo = tcp1.memo;
							tcpsBackUp.add(tcp2);
						}
						trp2.setTransactionCreditsAndPayments(tcpsBackUp);
						unUsedAmount = trp1.getReceivePayment()
								.getUnUsedPayments();
					}

					{
						ReceivePayment rp = new ReceivePayment();
						rp.amount = trp1.receivePayment.amount;
						rp.unUsedCredits = trp1.receivePayment.unUsedCredits;
						rp.unUsedPayments = trp1.receivePayment.unUsedPayments;
						rp.depositIn = trp1.receivePayment.depositIn;
						trp2.receivePayment = rp;
					}
					trpsBackUp.add(trp2);
				}
			}

			invoice.setVoid(true);
			accounter.alterInvoice(invoice);

			Invoice inv = accounterDao.getInvoice(company.getId(), invoice
					.getId());
			assertEquals("Invoice Payment value", inv.getPayments(), inv
					.getTotal());
			assertEquals("Invoice Balance Due value", inv.getBalanceDue(), 0.0);
			assertEquals("Status of the invoice", inv.getStatus(),
					invoiceBackUp.getStatus());

			{
				Set<TransactionReceivePayment> trps = inv
						.getTransactionReceivePayments();
				for (Iterator<TransactionReceivePayment> i = trps.iterator(); i
						.hasNext();) {
					TransactionReceivePayment tp = i.next();
					assertEquals("Transaction Receive Payment" + tp.getId(), tp
							.getPayment(), 0.0);
					assertEquals("Transaction Receive Payment" + tp.getId(), tp
							.getWriteOff(), 0.0);
					assertEquals("Transaction Receive Payment" + tp.getId(), tp
							.getCashDiscount(), 0.0);
					// assertNull("UnUsedCredits"+tp.getReceivePayment().
					// getUnUsedCredits(),
					// tp.getReceivePayment().getUnUsedCredits());
					assertEquals("Unused Credits"
							+ tp.getReceivePayment().getUnUsedCredits(),
							trpsBackUp.get(0).getReceivePayment()
									.getUnUsedCredits()
									- tp.payment, tp.getReceivePayment()
									.getUnUsedCredits());
					assertEquals("UnUsedPayments", tp.getReceivePayment()
							.getUnUsedPayments(), trpsBackUp.get(0)
							.getReceivePayment().getUnUsedPayments()
							+ unUsedAmount);

					List<TransactionCreditsAndPayments> tcps = tp
							.getTransactionCreditsAndPayments();
					for (int j = 0; j < tcps.size(); j++) {
						TransactionCreditsAndPayments tcp1 = tcps.get(j);
						assertEquals(
								"TransactionCreditsAndPayments- Credit Amount to Use",
								tcp1.getAmountToUse(), 0.0);

						for (int k = 0; (tcpsBackUp != null && k < tcpsBackUp
								.size()); k++) {
							TransactionCreditsAndPayments tcp2 = tcpsBackUp
									.get(k);
							if (tcp1.getMemo().equals(tcp2.getMemo())
									&& (tcp1.getCreditsAndPayments().getId() == tcp2
											.getCreditsAndPayments().getId())) {
								assertEquals(
										"CreditsAndPayments- Credit Amount to Use",
										tcp1.getCreditsAndPayments()
												.getCreditAmount(), tcp2
												.getCreditsAndPayments()
												.getCreditAmount()
												+ tcp2.getAmountToUse());
								break;
							} else if ((k + 1) >= tcpsBackUp.size())
								throw (new DAOException(
										DAOException.INVALID_REQUEST_EXCEPTION,
										null));
						}
					}
				}
			}
			if (invoice.getTransactionReceivePayments() != null
					&& invoice.getTransactionReceivePayments().size() > 0) {
				List<CreditsAndPayments> cps2 = accounterDao
						.getCreditsAndPayments(company.getId());
				{
					for (Iterator<CreditsAndPayments> i = cpsBackUp.iterator(); i
							.hasNext();) {
						CreditsAndPayments cp1 = (CreditsAndPayments) i.next();
						if (cp1
								.getMemo()
								.equals(
										""
												+ ((TransactionReceivePayment) (((inv
														.getTransactionReceivePayments())
														.iterator()).next()))
														.getReceivePayment()
														.getNumber()
												+ AccounterConstants.TYPE_RECEIVE_PAYMENT)
								&& cp1.getId() == ((TransactionReceivePayment) (((inv
										.getTransactionReceivePayments())
										.iterator()).next()))
										.getTransactionCreditsAndPayments()
										.get(0).getCreditsAndPayments().getId()) {
							for (Iterator<CreditsAndPayments> j = cps2
									.iterator(); j.hasNext();) {
								CreditsAndPayments cp2 = (CreditsAndPayments) j
										.next();
								if (cp2.getMemo().equals(cp1.getMemo())) {
									assertEquals(
											"Credit Amount in CreditsAndPayments",
											cp2.getCreditAmount(), cp1
													.getCreditAmount()
													+ unUsedAmount);
									break;
								} else if (!j.hasNext())
									throw (new DAOException(
											DAOException.INVALID_REQUEST_EXCEPTION,
											null));
							}
							break;
						} else if (!i.hasNext()) {
							assertEquals("size of CreditsAndPayments", cps2
									.size(), cpsBackUp.size() + 1);
							for (Iterator<CreditsAndPayments> j = cps2
									.iterator(); j.hasNext();) {
								CreditsAndPayments cp2 = (CreditsAndPayments) j
										.next();
								if (cp2
										.getMemo()
										.equals(
												""
														+ ((TransactionReceivePayment) ((inv
																.getTransactionReceivePayments()
																.iterator())
																.next()))
																.getReceivePayment()
																.getNumber()
														+ AccounterConstants.TYPE_RECEIVE_PAYMENT)) {
									assertEquals(cp2.getCreditAmount(),
											unUsedAmount);
									break;
								} else if (!j.hasNext())
									throw (new DAOException(
											DAOException.INVALID_REQUEST_EXCEPTION,
											null));
							}
						}
					}
				}
			}
			{
				Invoice c = invoice;

				assertNotNull(c);
				assertNotNull(c.getCustomer());
				assertNotNull(c.getCustomer().getName());
				assertEquals(c.getCustomer().getName(), accounterDao
						.getInvoice(company.getId(), c.getId()).getCustomer()
						.getName());
				assertEquals(c.getDate(), accounterDao.getInvoice(
						company.getId(), c.getId()).getDate());
				assertNotNull(c.getDueDate());
				assertEquals(c.getDueDate(), accounterDao.getInvoice(
						company.getId(), c.getId()).getDueDate());
				assertEquals(c.getDiscountTotal(), accounterDao.getInvoice(
						company.getId(), c.getId()).getDiscountTotal());
				assertEquals(c.getType(), accounterDao.getInvoice(
						company.getId(), c.getId()).getType());
				assertEquals(c.getTotal(), accounterDao.getInvoice(
						company.getId(), c.getId()).getTotal());
				assertEquals(c.getSalesTaxAmount(), accounterDao.getInvoice(
						company.getId(), c.getId()).getSalesTaxAmount());
				if (c.getSalesPerson() != null)
					if (c.getSalesPerson().getFirstName() != null)
						assertEquals(c.getSalesPerson().getFirstName(),
								accounterDao.getInvoice(company.getId(),
										c.getId()).getSalesPerson()
										.getFirstName());
				if (c.getPayments() != 0)
					assertEquals(c.getPayments(), accounterDao.getInvoice(
							company.getId(), c.getId()).getPayments());
				assertEquals(c.getNumber(), accounterDao.getInvoice(
						company.getId(), c.getId()).getNumber());

				// Take all the transaction items that are involved in the
				// cashsale and store them in a List.
				List<TransactionItem> ti = c.getTransactionItems();
				// Now, we need to iterate all the items and make necessary
				// changes in the corresponding accounts.
				Iterator<TransactionItem> j = ti.iterator();

				while (j.hasNext()) {// while there is a transaction item
					TransactionItem it = (TransactionItem) j.next();
					// ti2.add(it);
					Account acc = null;
					if (it.getType() == TransactionItem.TYPE_ITEM)
						acc = it.getItem().getIncomeAccount();
					else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
						acc = it.getAccount();
					else if (it.getType() == TransactionItem.TYPE_SALESTAX)
						acc = it.getTaxCode().getTaxAgency()
								.getLiabilityAccount();
					else
						acc = it.getAccount();

					// If the isIncrease() value is true, then the account value
					// is to be increased, else decreased.
					if (acc.isIncrease()) {// if account is increase

						d[getAccountVariable(accounts, acc.getName(), company)] -= it
								.getLineTotal();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is increase

					else {// if for account is not increase
						d[getAccountVariable(accounts, acc.getName(), company)] += it
								.getLineTotal();
						String str = acc.getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is not increase
					if (it.getType() == TransactionItem.TYPE_SALESTAX)
						taxAgencies[getTaxAgencyVariable(codes, it.getTaxCode()
								.getTaxAgency().getName(), company)] -= it
								.getLineTotal();

					if (it.taxGroup != null
							&& it.taxGroup.getTaxCodes() != null) {// if
						// taxgroup
						// is not
						// null
						Set<TaxCode> taxcodes = it.taxGroup.getTaxCodes();
						Iterator<TaxCode> i2 = taxcodes.iterator();
						while (i2.hasNext()) {// i2.hasNext
							TaxCode tc = (TaxCode) i2.next();
							double rate = 0.0;// ,rate2=0.0;

							if (tc.getTaxRates() != null) {// taxrate == null

								Set<TaxRates> taxrates = tc.getTaxRates();
								Iterator<TaxRates> i3 = taxrates.iterator();
								Date date;// =new Date();
								if (i3.hasNext()) {// if i3.hasNext
									TaxRates tr = (TaxRates) i3.next();
									date = (Date) tr.getAsOf();
									rate = tr.getRate();
									while (i3.hasNext()) {
										// The date which is latest but not
										// after the today's date is to be taken
										// and its taxrate must be used in the
										// calculations.

										if (date.after(tr.getAsOf())) {
											date = tr.getAsOf();
											rate = tr.getRate();
										}
										tr = (TaxRates) i3.next();
									}// while i3.hasNext()

								}// if i3.hasNext()
								rate = (rate / 100.0) * it.lineTotal;
								// changing the taxagency's liability account as
								// per
								// the 'isIncrease()' value of the account.
								if (tc.getTaxAgency().getLiabilityAccount()
										.isIncrease()) {// taxAgency account
									// isIncrease

									d[getAccountVariable(accounts, tc
											.getTaxAgency()
											.getLiabilityAccount().getName(),
											company)] -= rate;
									String str = tc.getTaxAgency()
											.getLiabilityAccount().getName();
									while (accounterDao.getAccount(
											company.getId(), str).getParent() != null) {
										d[getAccountVariable(accounts,
												accounterDao.getAccount(
														company.getId(), str)
														.getParent().getName(),
												company)] -= rate;
										str = accounterDao.getAccount(
												company.getId(), str)
												.getParent().getName();
									}
								}// taxAgency account isIncrease
								else {
									d[getAccountVariable(accounts, tc
											.getTaxAgency()
											.getLiabilityAccount().getName(),
											company)] -= rate;
									String str = tc.getTaxAgency()
											.getLiabilityAccount().getName();
									while (accounterDao.getAccount(
											company.getId(), str).getParent() != null) {
										d[getAccountVariable(accounts,
												accounterDao.getAccount(
														company.getId(), str)
														.getParent().getName(),
												company)] -= rate;
										str = accounterDao.getAccount(
												company.getId(), str)
												.getParent().getName();
									}
								}
							}// taxrate == null

							taxAgencies[getTaxAgencyVariable(codes, tc
									.getTaxAgency().getName(), company)] -= rate;
						}// i2.hasNext
					}// if Taxgroup is not null

				}// while there is a transaction item

				// checking whether the Accounts Receivable account is updated.
				// If it has a parent it also need to be updated
				d[getAccountVariable(accounts, "Accounts Receivable", company)] -= c
						.getTotal();

				// Now we need to calculate the tax amount that the customer had
				// to be paid.
				// For that we need to take the taxGroup in the cashsale
				// transaction
				// and checking the taxrates that are related with taxcodes in
				// the taxgroup
				// And finally the liability account in the corresponding
				// taxagency must be updated...

				// If the last transaction item of the last CashSale is done, we
				// need to check the accounts table whether all the accounts are
				// updated perfectly.

				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), d[getAccountVariable(accounts,
							accounterDao.getAccount(company.getId(), (l1 + 1))
									.getName(), company)], accounterDao
							.getAccount(company.getId(), (l1 + 1))
							.getTotalBalance());
				for (long l1 = 0; l1 < codes.size(); l1++) {
					String name = codes.get((int) l1).getTaxAgency().getName();
					assertEquals(name, taxAgencies[getTaxAgencyVariable(codes,
							name, company)], accounterDao.getTaxAgency(
							company.getId(), name).getBalance());
				}
			}
			return invoice;
		}
		return null;
	}

	public CustomerCreditMemo testVoidCustomerCreditMemo(
			CustomerCreditMemo customerCreditMemo, List<Account> accounts,
			List<TaxCode> codes) throws DAOException {
		company = accounterDao.getCompany(1l);
		if (!customerCreditMemo.isVoid() && customerCreditMemo.canVoidOrEdit) {
			double[][] creditAmounts = null;
			List<Invoice> invoicesBackUp = new ArrayList<Invoice>();
			CreditsAndPayments cpsBackUp = null;
			{
				boolean flag = false;
				List<CreditsAndPayments> cps1 = accounterGUIDao
						.getCustomerCreditsAndPayments(company.getId(),
								customerCreditMemo.getCustomer().getId());
				for (int i = 0; i < cps1.size(); i++) {
					CreditsAndPayments cp = new CreditsAndPayments();
					cp.setBalance(cps1.get(i).getBalance());
					cp.setCreditAmount(cps1.get(i).getCreditAmount());
					cp.setMemo(cps1.get(i).getMemo());
					cp.setTransaction(cps1.get(i).getTransaction());
					cp.id = cps1.get(i).id;
					// cpsBackUp.add(cp);
					if (cp
							.getMemo()
							.equals(
									""
											+ customerCreditMemo.getNumber()
											+ AccounterConstants.TYPE_CUSTOMER_CREDIT_MEMO))

					{
						cpsBackUp = cp;
						Set<TransactionCreditsAndPayments> tps = cps1.get(i)
								.getTransactionCreditsAndPayments();
						creditAmounts = new double[cps1.size()][3];
						int count = 0;
						for (Iterator<TransactionCreditsAndPayments> itr = tps
								.iterator(); itr.hasNext();) {
							TransactionCreditsAndPayments tcp = itr.next();
							TransactionReceivePayment trp = tcp
									.getTransactionReceivePayment();
							creditAmounts[++count][0] = trp.getReceivePayment()
									.getId();
							creditAmounts[++count][1] = trp.getId();
							creditAmounts[++count][2] = tcp.getAmountToUse();
							if (trp.getInvoice() != null)
								invoicesBackUp.add(trp.getInvoice());
						}
						flag = true;
					}
				}
				if (!flag)
					throw (new DAOException(
							DAOException.INVALID_REQUEST_EXCEPTION, null));
			}
			customerCreditMemo.setVoid(true);
			accounter.alterCustomerCreditMemo(customerCreditMemo);

			{
				boolean flag = false;
				List<CreditsAndPayments> cps1 = accounterGUIDao
						.getCustomerCreditsAndPayments(company.getId(),
								customerCreditMemo.getCustomer().getId());
				for (int i = 0; i < cps1.size(); i++) {
					if (cps1
							.get(i)
							.getMemo()
							.equals(
									""
											+ customerCreditMemo.getNumber()
											+ AccounterConstants.TYPE_CUSTOMER_CREDIT_MEMO)
							&& (((cps1.get(i)
									.getTransactionCreditsAndPayments()
									.iterator()).next())
									.getCreditsAndPayments().getId() == ((cpsBackUp
									.getTransactionCreditsAndPayments()
									.iterator()).next())
									.getCreditsAndPayments().getId())) {
						flag = true;
						break;
					}
				}

				{
					Set<TransactionCreditsAndPayments> trps = cpsBackUp
							.getTransactionCreditsAndPayments();
					Iterator<TransactionCreditsAndPayments> i = trps.iterator();
					while (i.hasNext()) {
						TransactionCreditsAndPayments tp = i.next();
						for (int j = 0; j < creditAmounts.length; j++) {
							if (creditAmounts[j][0] == tp
									.getTransactionReceivePayment()
									.getReceivePayment().getId()
									&& creditAmounts[j][1] == tp
											.getTransactionReceivePayment()
											.getId()) {
								if (tp.getTransactionReceivePayment()
										.getInvoice() != null) {
									Invoice invoice = tp
											.getTransactionReceivePayment()
											.getInvoice();
									assertEquals(invoice.getPayments()
											- creditAmounts[j][2], accounterDao
											.getInvoice(company.getId(),
													invoice.getId())
											.getPayments());
									assertEquals(invoice.getBalanceDue()
											+ creditAmounts[j][2], accounterDao
											.getInvoice(company.getId(),
													invoice.getId())
											.getBalanceDue());
								}
							}
						}
					}

				}
				if (flag)
					throw (new DAOException(
							DAOException.INVALID_REQUEST_EXCEPTION, null));
			}
			{
				CustomerCreditMemo c = customerCreditMemo;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				{
					assertNotNull(c);
					assertNotNull(c.getCustomer());
					assertNotNull(c.getCustomer().getName());
					assertEquals(c.getCustomer().getName(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getCustomer().getName());
					assertEquals(c.getDate(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getDate());
					assertEquals(c.getDiscountTotal(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getDiscountTotal());
					assertEquals(c.getType(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getType());
					assertEquals(c.getTotal(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getTotal());
					assertEquals(c.getSalesTax(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getSalesTax());
					if (c.getSalesPerson() != null)
						if (c.getSalesPerson().getFirstName() != null)
							assertEquals(c.getSalesPerson().getFirstName(),
									accounterDao.getCustomerCreditMemo(
											company.getId(), c.getId())
											.getSalesPerson().getFirstName());
					assertEquals(c.getNumber(), accounterDao
							.getCustomerCreditMemo(company.getId(), c.getId())
							.getNumber());

					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getIncomeAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else if (it.getType() == TransactionItem.TYPE_SALESTAX)
							acc = it.getTaxCode().getTaxAgency()
									.getLiabilityAccount();
						else
							acc = it.getAccount();
						if (acc.getType() == Account.TYPE_ACCOUNT_PAYABLE)
							System.out.println("here");

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase
						if (it.getType() == TransactionItem.TYPE_SALESTAX)
							taxAgencies[getTaxAgencyVariable(codes, it
									.getTaxCode().getTaxAgency().getName(),
									company)] += it.getLineTotal();

						if (it.taxGroup != null
								&& it.taxGroup.getTaxCodes() != null) {// if
							// taxgroup
							// is
							// not
							// null
							Set<TaxCode> taxcodes = it.taxGroup.getTaxCodes();
							Iterator<TaxCode> i2 = taxcodes.iterator();
							while (i2.hasNext()) {// i2.hasNext
								TaxCode tc = (TaxCode) i2.next();
								double rate = 0.0;

								if (tc.getTaxRates() != null) {// taxrate ==
									// null

									Set<TaxRates> taxrates = tc.getTaxRates();
									Iterator<TaxRates> i3 = taxrates.iterator();
									Date date;
									if (i3.hasNext()) {// if i3.hasNext
										TaxRates tr = (TaxRates) i3.next();
										date = (Date) tr.getAsOf();
										rate = tr.getRate();
										while (i3.hasNext()) {
											// The date which is latest but not
											// after
											// the today's date is to be taken
											// and its
											// taxrate must be used in the
											// calculations.
											if (date.after(tr.getAsOf())) {
												date = tr.getAsOf();
												rate = tr.getRate();
											}
											tr = (TaxRates) i3.next();
										}// while i3.hasNext()
									}// if i3.hasNext()
									rate = (rate / 100.0) * it.lineTotal;
									// changing the taxagency's liability
									// account as per
									// the 'isIncrease()' value of the account.
									if (tc.getTaxAgency().getLiabilityAccount()
											.isIncrease()) {// taxAgency account
										// isIncrease
										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] += rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] += rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}// taxAgency account isIncrease
									else {
										d[getAccountVariable(accounts, tc
												.getTaxAgency()
												.getLiabilityAccount()
												.getName(), company)] -= rate;
										String str = tc.getTaxAgency()
												.getLiabilityAccount()
												.getName();
										while (accounterDao.getAccount(
												company.getId(), str)
												.getParent() != null) {
											d[getAccountVariable(accounts,
													accounterDao.getAccount(
															company.getId(),
															str).getParent()
															.getName(), company)] -= rate;
											str = accounterDao.getAccount(
													company.getId(), str)
													.getParent().getName();
										}
									}
								}// taxrate == null
								taxAgencies[getTaxAgencyVariable(codes, tc
										.getTaxAgency().getName(), company)] += rate;
							}// i2.hasNext
						}// if Taxgroup is not null

					}// while there is a transaction item

					// checking whether the AccountsReceivable account is
					// updated. If it has
					// a parent it also need to be updated
					d[getAccountVariable(accounts, "Accounts Receivable",
							company)] += c.getTotal();

					// Now we need to calculate the tax amount that the customer
					// had to be paid.
					// For that we need to take the taxGroup in the
					// Customercrediememo transaction
					// and checking the taxrates that are related with taxcodes
					// in the taxgroup
					// And finally the liability account in the corresponding
					// taxagency must be updated...

					// If the last transaction item of the last
					// CustomerCreditMemo is done, we
					// need to check the accounts table whether all the accounts
					// are updated perfectly.
					// if (!i.hasNext())
					{
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());

						for (long l1 = 0; l1 < codes.size(); l1++) {
							String name = codes.get((int) l1).getTaxAgency()
									.getName();
							assertEquals(name,
									taxAgencies[getTaxAgencyVariable(codes,
											name, company)],
									accounterDao.getTaxAgency(company.getId(),
											name).getBalance());
						}
					}
				}
			}
			return customerCreditMemo;
		}
		return null;
	}

	public CustomerRefund testVoidCustomerRefund(CustomerRefund customerRefund,
			List<Account> accounts, List<TaxCode> codes) throws DAOException {
		company = accounterDao.getCompany(1l);
		if (!customerRefund.isVoid() && customerRefund.canVoidOrEdit) {
			double[][] credits = null;
			List<TransactionReceivePayment> trpsBack;
			List<TransactionCreditsAndPayments> tcpsBack = null;
			List<CreditsAndPayments> cpsBack = null;
			if (customerRefund.transactionReceivePayments != null) {
				cpsBack = accounterDao.getCreditsAndPayments(company.getId());
				Set<TransactionReceivePayment> trps1 = customerRefund.transactionReceivePayments;

				trpsBack = new ArrayList<TransactionReceivePayment>();
				for (Iterator<TransactionReceivePayment> i = trps1.iterator(); i
						.hasNext();) {
					TransactionReceivePayment trp = new TransactionReceivePayment();
					TransactionReceivePayment trp2 = (TransactionReceivePayment) i
							.next();
					trp.setAppliedCredits(trp2.getAppliedCredits());
					trp.setPayment(trp2.getPayment());
					List<TransactionCreditsAndPayments> tcps1 = trp2
							.getTransactionCreditsAndPayments();
					tcpsBack = new ArrayList<TransactionCreditsAndPayments>();
					for (int j = 0; j < tcps1.size(); j++) {
						TransactionCreditsAndPayments tcp = new TransactionCreditsAndPayments();
						tcp.setAmountToUse(tcps1.get(j).getAmountToUse());
						tcp.setCreditsAndPayments(tcps1.get(j)
								.getCreditsAndPayments());
						tcpsBack.add(tcp);
					}
					trp.setTransactionCreditsAndPayments(tcpsBack);
					trpsBack.add(trp);
				}

			}
			customerRefund.setVoid(true);
			accounter.alterCustomerRefunds(customerRefund);

			{
				Set<TransactionReceivePayment> cps = customerRefund
						.getTransactionReceivePayments();
				if (cps != null) {
					for (Iterator<TransactionReceivePayment> i = cps.iterator(); i
							.hasNext();) {
						TransactionReceivePayment trp = (TransactionReceivePayment) i
								.next();
						assertEquals("Payment", 0.0, trp.getPayment());
						assertEquals("Applied Credits", 0.0, trp
								.getAppliedCredits());
						assertEquals("Cash Discount", 0.0, trp
								.getCashDiscount());
						assertEquals("Write off", 0.0, trp.getWriteOff());
						List<TransactionCreditsAndPayments> tcps = trp
								.getTransactionCreditsAndPayments();
						for (int j = 0; j < tcps.size(); j++) {
							assertEquals(tcps.get(j).getAmountToUse(), 0.0);

							CreditsAndPayments cp = tcps.get(j)
									.getCreditsAndPayments();

							for (int k = 0; k < tcpsBack.size(); k++) {
								if (cp.getMemo().equals(
										tcpsBack.get(k).getMemo())) {
									assertEquals(tcps.get(j)
											.getCreditsAndPayments()
											.getCreditAmount(), tcpsBack.get(k)
											.getCreditsAndPayments()
											.getCreditAmount()
											+ tcpsBack.get(k).getAmountToUse());
									break;
								} else if ((k + 1) >= tcpsBack.size())
									throw (new DAOException(
											DAOException.INVALID_REQUEST_EXCEPTION,
											null));
							}
						}
					}
					List<CreditsAndPayments> cpsNew = accounterDao
							.getCreditsAndPayments(company.getId());
					for (int j = 0; j < cpsNew.size(); j++) {
						if (cpsNew
								.get(j)
								.getMemo()
								.equals(
										""
												+ customerRefund.getNumber()
												+ AccounterConstants.TYPE_RECEIVE_PAYMENT)) {
							for (int k = 0; k < cpsBack.size(); k++) {
								if (cpsNew.get(j).getMemo().equals(
										cpsBack.get(k).getMemo())) {
									assertEquals(
											cpsNew.get(j).getCreditAmount(),
											cpsBack.get(k).getCreditAmount()
													+ getAmountTotalPayments(customerRefund
															.getTransactionReceivePayments()));
									break;
								} else if ((k + 1) >= cpsBack.size())
									throw (new DAOException(
											DAOException.INVALID_REQUEST_EXCEPTION,
											null));
							}
						}
					}
				}
			}
			{
				CustomerRefund c = customerRefund;
				assertNotNull(c);
				assertNotNull(c.getPayTo());
				assertNotNull(c.getPayTo().getName());
				assertEquals(c.getPayTo().getName(), accounterDao
						.getCustomerRefunds(company.getId(), c.getId())
						.getPayTo().getName());
				assertNotNull(c.getPayFrom());
				assertNotNull(c.getPayFrom().getName());
				assertEquals(c.getPayFrom().getName(), accounterDao
						.getCustomerRefunds(company.getId(), c.getId())
						.getPayFrom().getName());
				assertNotNull(c.getPaymentMethod());
				assertNotNull(c.getPaymentMethod());
				assertEquals(c.getPaymentMethod(), accounterDao
						.getCustomerRefunds(company.getId(), c.getId())
						.getPaymentMethod());
				assertEquals(c.getDate(), accounterDao.getCustomerRefunds(
						company.getId(), c.getId()).getDate());
				if (c.getDate() != null)
					assertEquals(c.getDate(), accounterDao.getCustomerRefunds(
							company.getId(), c.getId()).getDate());
				assertEquals(c.getType(), accounterDao.getCustomerRefunds(
						company.getId(), c.getId()).getType());
				assertEquals(c.getTotal(), accounterDao.getCustomerRefunds(
						company.getId(), c.getId()).getTotal());
				if (c.getPayments() != 0)
					assertEquals(c.getPayments(), accounterDao
							.getCustomerRefunds(company.getId(), c.getId())
							.getPayments());
				assertEquals(c.getNumber(), accounterDao.getCustomerRefunds(
						company.getId(), c.getId()).getNumber());

				// checking whether the depositIn account is updated. If it has
				// a parent it also need to be updated
				d[getAccountVariable(accounts, "Accounts Receivable", company)] -= c
						.getTotal();

				if (c.getPayFrom().isIncrease()) {
					d[getAccountVariable(accounts, c.getPayFrom().getName(),
							company)] -= c.getTotal();
					String str = c.getPayFrom().getName();
					while ((accounterDao.getAccount(company.getId(), str)
							.getParent() != null)) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] -= c.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				} else {
					d[getAccountVariable(accounts, c.getPayFrom().getName(),
							company)] += c.getTotal();
					String str = c.getPayFrom().getName();
					while ((accounterDao.getAccount(company.getId(), str)
							.getParent() != null)) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] += c.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				}
				// If the last transaction item of the last CashSale is done, we
				// need to check the accounts table whether all the accounts are
				// updated perfectly.
				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), accounterDao.getAccount(
							company.getId(), (l1 + 1)).getTotalBalance(),
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), (l1 + 1))
									.getName(), company)]);
			}
			return customerRefund;
		}
		return null;
	}

	public EnterBill testVoidEnterBills(EnterBill enterBill,
			List<Account> accounts, List<TaxCode> codes) throws DAOException {
		if (!enterBill.isVoid() && enterBill.canVoidOrEdit) {
			company = accounterDao.getCompany(1l);
			List<TransactionPayBill> tpsBackUp = null;
			List<TransactionCreditsAndPayments> tcpsBackUp = null;
			List<CreditsAndPayments> vcpsBackUp = null;
			EnterBill enterBillBackUp = new EnterBill();
			enterBillBackUp.setStatus(enterBill.getStatus());

			if (enterBill.transactionPayBills != null) {
				vcpsBackUp = new ArrayList<CreditsAndPayments>();
				{
					List<CreditsAndPayments> cps1 = accounterGUIDao
							.getVendorCreditsAndPayments(company.getId(),
									enterBill.getVendor().getId());
					for (int i = 0; i < cps1.size(); i++) {

						CreditsAndPayments cp = new CreditsAndPayments();
						cp.setBalance(cps1.get(i).getBalance());
						cp.setCreditAmount(cps1.get(i).getCreditAmount());
						cp.setMemo(cps1.get(i).getMemo());
						cp.setTransaction(cps1.get(i).getTransaction());
						vcpsBackUp.add(cp);
					}
				}
				tpsBackUp = new ArrayList<TransactionPayBill>();
				Set<TransactionPayBill> trps = enterBill.transactionPayBills;
				for (Iterator<TransactionPayBill> i = trps.iterator(); i
						.hasNext();) {
					TransactionPayBill trp1 = i.next();
					TransactionPayBill trp2 = new TransactionPayBill();
					trp2.setAppliedCredits(trp1.getAppliedCredits());
					trp2.setCashDiscount(trp1.getCashDiscount());
					trp2.setPayment(trp1.getPayment());
					trp2.id = trp1.id;
					if (trp1.getAppliedCredits() != 0.0) {
						tcpsBackUp = new ArrayList<TransactionCreditsAndPayments>();
						List<TransactionCreditsAndPayments> tcps = trp1
								.getTransactionCreditsAndPayments();

						for (int j = 0; j < tcps.size(); j++) {
							TransactionCreditsAndPayments tcp1 = tcps.get(j);
							TransactionCreditsAndPayments tcp2 = new TransactionCreditsAndPayments();
							tcp2.amountToUse = tcp1.amountToUse;
							tcp2.transactionPayBill = tcp1.transactionPayBill;
							tcp2.memo = tcp1.memo;
							tcp2.id = tcp1.id;
							tcp2.company = tcp1.company;
							tcpsBackUp.add(tcp2);
						}
						trp2.setTransactionCreditsAndPayments(tcpsBackUp);
						// unUsedAmount=trp1.getPayBill().getUnusedAmount();
					}
					tpsBackUp.add(trp2);
				}
			}
			enterBill.setVoid(true);
			accounter.alterEnterBill(enterBill);

			{
				if (enterBill.transactionPayBills != null) {
					List<CreditsAndPayments> cps1 = accounterGUIDao
							.getVendorCreditsAndPayments(company.getId(),
									enterBill.getVendor().getId());
					for (int i = 0; i < cps1.size(); i++) {
						assertEquals("New CreditsAndPayments size=", cps1
								.size(), vcpsBackUp.size() + 1);
						CreditsAndPayments cp = cps1.get(i);
						Set<TransactionPayBill> tps = enterBill.transactionPayBills;
						for (Iterator<TransactionPayBill> itr = tps.iterator(); itr
								.hasNext();) {
							TransactionPayBill tp = itr.next();
							if (cp.memo.equals("" + tp.payBill.number
									+ AccounterConstants.TYPE_PAY_BILL)) {
								assertEquals(
										"Credit Amount in CreditsAndPayments for VendorPayment",
										cp.creditAmount, tp.payment);
							}
							assertEquals(
									"CashDiscount in TransactionPayBill, id="
											+ tp.id, tp.cashDiscount, 0.0);
							assertEquals(
									"Applied Credits in TransactionPayBill, id="
											+ tp.id, tp.appliedCredits, 0.0);
							assertEquals("Payments in TransactionPayBill, id="
									+ tp.id, tp.payment, 0.0);
						}
					}
				}
			}

			{// testing void of Enterbill

				// testing of this transaction starts from here........

				EnterBill c = enterBill;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				assertNotNull(c);
				assertNotNull(c.getVendor());
				assertNotNull(c.getVendor().getName());
				assertEquals(c.getVendor().getName(), accounterDao
						.getEnterBill(company.getId(), c.getId()).getVendor()
						.getName());
				assertEquals(c.getDate(), accounterDao.getEnterBill(
						company.getId(), c.getId()).getDate());
				assertNotNull(c.getDueDate());
				assertEquals(c.getDueDate(), accounterDao.getEnterBill(
						company.getId(), c.getId()).getDueDate());
				assertEquals(c.getTotal(), accounterDao.getEnterBill(
						company.getId(), c.getId()).getTotal());
				assertEquals(c.getType(), accounterDao.getEnterBill(
						company.getId(), c.getId()).getType());
				if (c.getPayments() != 0)
					assertEquals(c.getPayments(), accounterDao.getEnterBill(
							company.getId(), c.getId()).getPayments());
				if (c.getPaymentTerm() != null)
					assertEquals(c.getPaymentTerm(), accounterDao.getEnterBill(
							company.getId(), c.getId()).getPaymentTerm());
				assertEquals(c.getNumber(), accounterDao.getEnterBill(
						company.getId(), c.getId()).getNumber());

				// Take all the transaction items that are involved in the
				// cashsale and store them in a List.
				ti = c.getTransactionItems();
				// Now, we need to iterate all the items and make necessary
				// changes in the corresponding accounts.
				Iterator<TransactionItem> j = ti.iterator();

				while (j.hasNext()) {// while there is a transaction item
					TransactionItem it = (TransactionItem) j.next();
					ti2.add(it);
					Account acc = null;
					if (it.getType() == TransactionItem.TYPE_ITEM)
						acc = it.getItem().getExpenseAccount();
					else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
						acc = it.getAccount();

					// If the isIncrease() value is true, then the account value
					// is to be increased, else decreased.
					if (acc.isIncrease()) {
						d[getAccountVariable(accounts, acc.getName(), company)] += it
								.getLineTotal();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

					else {
						d[getAccountVariable(accounts, acc.getName(), company)] -= it
								.getLineTotal();
						String str = acc.getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

				}// while there is a transaction item

				// checking whether the Accounts Payable account is updated.

				d[getAccountVariable(accounts, "Accounts Payable", company)] -= c
						.getTotal();

				// If the last transaction item of the last EnterBill is done,
				// // we need to check the accounts table whether all the
				// accounts are updated perfectly.
				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), d[getAccountVariable(accounts,
							accounterDao.getAccount(company.getId(), (l1 + 1))
									.getName(), company)], accounterDao
							.getAccount(company.getId(), (l1 + 1))
							.getTotalBalance());
			}
			return enterBill;
		}
		return null;
	}

	public VendorCreditMemo testVoidVendorCreditMemo(
			VendorCreditMemo vendorCreditMemo, List<Account> accounts,
			List<TaxCode> codes) throws DAOException {
		if (!vendorCreditMemo.isVoid() && vendorCreditMemo.canVoidOrEdit) {
			company = accounterDao.getCompany(1l);
			List<TransactionPayBill> tpsBackUp = null;
			List<TransactionCreditsAndPayments> tcpsBackUp = null;
			List<CreditsAndPayments> vcpsBackUp = null;
			VendorCreditMemo vcmBackUp = new VendorCreditMemo();
			CreditsAndPayments vcpBackUp = null;
			vcmBackUp.setStatus(vendorCreditMemo.getStatus());
			{
				vcpsBackUp = accounterGUIDao.getVendorCreditsAndPayments(
						company.getId(), vendorCreditMemo.getVendor().getId());

				if (vcpsBackUp.size() > 0) {
					for (int i = 0; i < vcpsBackUp.size(); i++) {
						if (vcpsBackUp.get(i).memo.equals(""
								+ vendorCreditMemo.number
								+ AccounterConstants.TYPE_VENDOR_CREDIT_MEMO)) {
							CreditsAndPayments cp = new CreditsAndPayments();
							cp.balance = vcpsBackUp.get(i).balance;
							cp.creditAmount = vcpsBackUp.get(i).creditAmount;
							cp.id = vcpsBackUp.get(i).id;
							cp.memo = vcpsBackUp.get(i).memo;
							Set<TransactionCreditsAndPayments> tcps1 = new HashSet<TransactionCreditsAndPayments>();
							Set<TransactionCreditsAndPayments> tcps = vcpsBackUp
									.get(i).getTransactionCreditsAndPayments();
							for (Iterator<TransactionCreditsAndPayments> itr = tcps
									.iterator(); itr.hasNext();) {
								TransactionCreditsAndPayments tcp1 = (TransactionCreditsAndPayments) itr
										.next();
								TransactionCreditsAndPayments tcp2 = new TransactionCreditsAndPayments();
								tcp2.amountToUse = tcp1.amountToUse;
								tcp2.memo = tcp1.memo;
								tcp2.id = tcp1.id;
								tcp2.creditsAndPayments = cp;
								tcps1.add(tcp1);
							}
							cp.transactionCreditsAndPayments = tcps1;
							vcpBackUp = cp;
						}
						tcpsBackUp = new ArrayList<TransactionCreditsAndPayments>();
						Set<TransactionCreditsAndPayments> tcps = vcpsBackUp
								.get(i).getTransactionCreditsAndPayments();
						for (Iterator<TransactionCreditsAndPayments> itr = tcps
								.iterator(); itr.hasNext();) {
							TransactionCreditsAndPayments tcp1 = (TransactionCreditsAndPayments) itr
									.next();
							TransactionCreditsAndPayments tcp2 = new TransactionCreditsAndPayments();
							tcp2.amountToUse = tcp1.amountToUse;
							CreditsAndPayments cp = new CreditsAndPayments();
							cp.balance = tcp1.creditsAndPayments.balance;
							cp.creditAmount = tcp1.creditsAndPayments.creditAmount;
							cp.id = tcp1.creditsAndPayments.id;
							cp.memo = tcp1.creditsAndPayments.memo;
							tcp2.creditsAndPayments = cp;
							tcpsBackUp.add(tcp2);
						}
					}
					for (Iterator<TransactionCreditsAndPayments> i = tcpsBackUp
							.iterator(); i.hasNext();) {
						tpsBackUp = new ArrayList<TransactionPayBill>();
						TransactionPayBill trp1 = (i.next()).transactionPayBill;
						TransactionPayBill trp2 = new TransactionPayBill();
						trp2.setAppliedCredits(trp1.getAppliedCredits());
						trp2.setCashDiscount(trp1.getCashDiscount());
						trp2.setPayment(trp1.getPayment());
						trp2.id = trp1.id;
						tpsBackUp.add(trp2);
					}
				}
			}

			vendorCreditMemo.setVoid(true);
			accounter.alterVendorCreditMemo(vendorCreditMemo);

			{
				List<CreditsAndPayments> cps = accounterGUIDao
						.getVendorCreditsAndPayments(company.getId(),
								vendorCreditMemo.getVendor().getId());
				assertEquals("new CreditsAndPayments size", cps.size(),
						vcpsBackUp.size() - 1);
				for (int i = 0; i < cps.size(); i++) {
					if (cps.get(i).memo.equals(vcpBackUp.memo)
							&& cps.get(i).id == vcpBackUp.id)
						throw (new DAOException(
								DAOException.INVALID_REQUEST_EXCEPTION, null));

					Set<TransactionCreditsAndPayments> tcps = vendorCreditMemo.creditsAndPayments.transactionCreditsAndPayments;

					Set<TransactionCreditsAndPayments> tcps2 = vcpBackUp.transactionCreditsAndPayments;

					// assertEquals(vendorCreditMemo.creditsAndPayments.
					// creditAmount,
					// vcpBackUp.creditAmount+getamount)

					for (Iterator<TransactionCreditsAndPayments> itr = tcps
							.iterator(); itr.hasNext();) {
						TransactionCreditsAndPayments tcp = itr.next();
						assertEquals("TransactionCreditsAndPayments",
								tcp.amountToUse, 0.0);
						// assertEquals("CreditsAndPayments",
						// tcp.creditsAndPayments.creditAmount, )
						assertEquals("TransactionPayBill",
								tcp.transactionPayBill.cashDiscount, 0.0);
						assertEquals("TransactionPayBill",
								tcp.transactionPayBill.appliedCredits, 0.0);
						assertEquals("TransactionPayBill",
								tcp.transactionPayBill.amountDue, 0.0);
						assertEquals("TransactionPayBill",
								tcp.transactionPayBill.payment, 0.0);
					}
				}
			}

			{// voiding of vendorCreditMemo
				VendorCreditMemo c = vendorCreditMemo;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				assertNotNull(c);
				assertNotNull(c.getVendor());
				assertNotNull(c.getVendor().getName());
				assertEquals(c.getVendor().getName(), accounterDao
						.getVendorCreditMemo(company.getId(), c.getId())
						.getVendor().getName());
				assertEquals(c.getDate(), accounterDao.getVendorCreditMemo(
						company.getId(), c.getId()).getDate());
				assertEquals(c.getTotal(), accounterDao.getVendorCreditMemo(
						company.getId(), c.getId()).getTotal());
				assertEquals(c.getType(), accounterDao.getVendorCreditMemo(
						company.getId(), c.getId()).getType());
				assertEquals(c.getNumber(), accounterDao.getVendorCreditMemo(
						company.getId(), c.getId()).getNumber());

				// Take all the transaction items that are involved in the
				// cashsale and store them in a List.
				ti = c.getTransactionItems();
				// Now, we need to iterate all the items and make necessary
				// changes in the corresponding accounts.
				Iterator<TransactionItem> j = ti.iterator();

				while (j.hasNext()) {// while there is a transaction item
					TransactionItem it = (TransactionItem) j.next();
					ti2.add(it);
					Account acc = null;
					if (it.getType() == TransactionItem.TYPE_ITEM)
						acc = it.getItem().getExpenseAccount();
					else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
						acc = it.getAccount();

					// If the isIncrease() value is true, then the account value
					// is to be increased, else decreased.
					if (acc.isIncrease()) {// if account is increase

						d[getAccountVariable(accounts, acc.getName(), company)] -= it
								.getLineTotal();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is increase

					else {// if for account is not increase
						d[getAccountVariable(accounts, acc.getName(), company)] += it
								.getLineTotal();
						String str = acc.getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is not increase

				}// while there is a transaction item

				// checking whether the Accounts Payable account is updated.
				d[getAccountVariable(accounts, company
						.getAccountsPayableAccount().getName(), company)] += c
						.getTotal();

				// If the last transaction item of the last vendorCreditMemo is
				// done, we need to check the accounts table whether all the
				// accounts are updated perfectly.
				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), d[getAccountVariable(accounts,
							accounterDao.getAccount(company.getId(), (l1 + 1))
									.getName(), company)], accounterDao
							.getAccount(company.getId(), (l1 + 1))
							.getTotalBalance());

			}// voiding of vendorCreditMemo
			return vendorCreditMemo;
		}
		return null;
	}

	public CashPurchase testVoidCashPurchase(CashPurchase cashPurchase,
			List<Account> accounts, List<TaxCode> codes) throws DAOException {
		if (!cashPurchase.isVoid() && cashPurchase.canVoidOrEdit) {
			cashPurchase.setVoid(true);
			accounter.alterCashPurchase(cashPurchase);

			{// voiding of CashPurchase

				CashPurchase c = cashPurchase;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				assertNotNull(c);
				assertNotNull(c.getVendor());
				assertNotNull(c.getVendor().getName());
				assertEquals(c.getVendor().getName(), accounterDao
						.getCashPurchase(company.getId(), c.getId())
						.getVendor().getName());
				assertEquals(c.getDate(), accounterDao.getCashPurchase(
						company.getId(), c.getId()).getDate());
				assertNotNull(c.getDate());
				assertEquals(c.getDate(), accounterDao.getCashPurchase(
						company.getId(), c.getId()).getDate());
				assertEquals(c.getTotal(), accounterDao.getCashPurchase(
						company.getId(), c.getId()).getTotal());
				assertEquals(c.getType(), accounterDao.getCashPurchase(
						company.getId(), c.getId()).getType());
				assertEquals(c.getPaymentMethod(), accounterDao
						.getCashPurchase(company.getId(), c.getId())
						.getPaymentMethod());
				if (c.getDeliveryDate() != null)
					assertEquals(c.getDeliveryDate(), accounterDao
							.getCashPurchase(company.getId(), c.getId())
							.getDeliveryDate());
				if (c.getPayFrom() != null)
					assertEquals(c.getPayFrom().getName(), accounterDao
							.getCashPurchase(company.getId(), c.getId())
							.getPayFrom().getName());
				assertEquals(c.getNumber(), accounterDao.getCashPurchase(
						company.getId(), c.getId()).getNumber());

				// Take all the transaction items that are involved in the
				// cashsale and store them in a List.
				ti = c.getTransactionItems();
				// Now, we need to iterate all the items and make necessary
				// changes in the corresponding accounts.
				Iterator<TransactionItem> j = ti.iterator();

				while (j.hasNext()) {// while there is a transaction item
					TransactionItem it = (TransactionItem) j.next();
					ti2.add(it);
					Account acc = null;
					if (it.getType() == TransactionItem.TYPE_ITEM)
						acc = it.getItem().getExpenseAccount();
					else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
						acc = it.getAccount();

					// If the isIncrease() value is true, then the account value
					// is to be increased, else decreased.
					if (acc.isIncrease()) {// if account is increase

						d[getAccountVariable(accounts, acc.getName(), company)] += it
								.getLineTotal();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is increase

					else {// if for account is not increase
						d[getAccountVariable(accounts, acc.getName(), company)] -= it
								.getLineTotal();
						String str = acc.getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is not increase

				}// while there is a transaction item

				// checking whether the payFrom account is updated. If it has a
				// parent it also need to be updated
				if (c.getPayFrom().isIncrease()) {
					d[getAccountVariable(accounts, c.getPayFrom().getName(),
							company)] -= c.getTotal();
					String str = c.getPayFrom().getName();
					while (accounterDao.getAccount(company.getId(), str)
							.getParent() != null) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] -= c.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				} else {
					d[getAccountVariable(accounts, c.getPayFrom().getName(),
							company)] += c.getTotal();
					String str = c.getPayFrom().getName();
					while (accounterDao.getAccount(company.getId(), str)
							.getParent() != null) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] += c.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				}
				// If the last transaction item of the last CashPurchase is
				// done, we need to check the accounts table whether all the
				// accounts are updated perfectly.
				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), d[getAccountVariable(accounts,
							accounterDao.getAccount(company.getId(), (l1 + 1))
									.getName(), company)], accounterDao
							.getAccount(company.getId(), (l1 + 1))
							.getTotalBalance());

			}// voiding of CashPurchase
			return cashPurchase;
		}
		return null;
	}

	public WriteCheck testVoidWriteCheck(WriteCheck writeCheck,
			List<Account> accounts, List<TaxCode> codes) throws DAOException {
		if (!writeCheck.isVoid() && writeCheck.canVoidOrEdit) {
			writeCheck.setVoid(true);
			accounter.alterWriteCheck(writeCheck);

			{// voiding of writechecks

				WriteCheck c = writeCheck;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				assertNotNull(c);
				assertEquals(c.getType(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getType());
				assertEquals(c.getPayToType(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getPayToType());
				if (c.getPayToType() == WriteCheck.TYPE_VENDOR) {
					assertNotNull(c.getVendor());
					assertNotNull(c.getVendor().getName());
					assertEquals(c.getVendor().getName(), accounterDao
							.getwriterCheck(company.getId(), c.getId())
							.getVendor().getName());
				} else if (c.getPayToType() == WriteCheck.TYPE_CUSTOMER) {
					assertNotNull(c.getCustomer());
					assertNotNull(c.getCustomer().getName());
					assertEquals(c.getCustomer().getName(), accounterDao
							.getwriterCheck(company.getId(), c.getId())
							.getCustomer().getName());
				} else {
					assertNotNull(c.getTaxAgency());
					assertNotNull(c.getTaxAgency().getName());
					assertEquals(c.getTaxAgency().getName(), accounterDao
							.getwriterCheck(company.getId(), c.getId())
							.getTaxAgency().getName());
				}
				assertEquals(c.getDate(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getDate());
				assertNotNull(c.getDate());
				assertEquals(c.getDate(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getDate());
				assertEquals(c.getTotal(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getTotal());
				assertEquals(c.getType(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getType());
				assertEquals(c.getBankAccount().getName(), accounterDao
						.getwriterCheck(company.getId(), c.getId())
						.getBankAccount().getName());
				assertEquals(c.getBalance(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getBalance());
				assertEquals(c.getNumber(), accounterDao.getwriterCheck(
						company.getId(), c.getId()).getNumber());

				// Take all the transaction items that are involved in the
				// writecheck and store them in a List.
				ti = c.getTransactionItems();
				// Now, we need to iterate all the items and make necessary
				// changes in the corresponding accounts.
				Iterator<TransactionItem> j = ti.iterator();

				while (j.hasNext()) {// while there is a transaction item
					TransactionItem it = (TransactionItem) j.next();
					ti2.add(it);
					Account acc = null;
					if (it.getType() == TransactionItem.TYPE_ITEM) {
						if (c.getPayToType() == WriteCheck.TYPE_VENDOR)
							acc = it.getItem().getExpenseAccount();
						if (c.getPayToType() == WriteCheck.TYPE_CUSTOMER)
							acc = it.getItem().getIncomeAccount();
					} else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
						acc = it.getAccount();
					else if (it.getType() == TransactionItem.TYPE_SALESTAX)
						acc = it.getTaxCode().getTaxAgency()
								.getLiabilityAccount();

					// If the isIncrease() value is true, then the account value
					// is to be increased, else decreased.
					if (acc.isIncrease()) {// if account is increase

						d[getAccountVariable(accounts, acc.getName(), company)] += it
								.getLineTotal();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is increase

					else {// if for account is not increase
						d[getAccountVariable(accounts, acc.getName(), company)] -= it
								.getLineTotal();
						String str = acc.getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= it
									.getLineTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}// if for account is not increase
					if (it.getType() == TransactionItem.TYPE_SALESTAX
							&& c.getPayToType() == WriteCheck.TYPE_CUSTOMER)
						taxAgencies[getTaxAgencyVariable(codes, it.getTaxCode()
								.getTaxAgency().getName(), company)] += it
								.getLineTotal();

				}// while there is a transaction item

				if (c.getPayToType() == WriteCheck.TYPE_TAX_AGENCY)
					taxAgencies[getTaxAgencyVariable(codes, c.getTaxAgency()
							.getName(), company)] += c.getTotal();

				// checking whether the Bank account is updated. If it has a
				// parent it also need to be updated
				if (c.getBankAccount().isIncrease()) {
					d[getAccountVariable(accounts,
							c.getBankAccount().getName(), company)] -= c
							.getTotal();
					String str = c.getBankAccount().getName();
					while (accounterDao.getAccount(company.getId(), str)
							.getParent() != null) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] -= c.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				} else {
					d[getAccountVariable(accounts,
							c.getBankAccount().getName(), company)] += c
							.getTotal();
					String str = c.getBankAccount().getName();
					while (accounterDao.getAccount(company.getId(), str)
							.getParent() != null) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] += c.getTotal();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				}

				// If the last transaction item of the last CashSale is done, we
				// need to check the accounts table whether all the accounts are
				// updated perfectly.
				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), d[getAccountVariable(accounts,
							accounterDao.getAccount(company.getId(), (l1 + 1))
									.getName(), company)], accounterDao
							.getAccount(company.getId(), (l1 + 1))
							.getTotalBalance());

				for (long l1 = 0; l1 < codes.size(); l1++) {
					String name = codes.get((int) l1).getTaxAgency().getName();
					assertEquals(name, taxAgencies[getTaxAgencyVariable(codes,
							name, company)], accounterDao.getTaxAgency(
							company.getId(), name).getBalance());
				}
			}// voiding of writechecks
			return writeCheck;
		}
		return null;
	}

	public MakeDeposit testVoidMakeDeposit(MakeDeposit makeDeposit,
			List<Account> accounts, List<TaxCode> codes) throws DAOException {
		if (!makeDeposit.isVoid() && makeDeposit.canVoidOrEdit) {
			makeDeposit.setVoid(true);
			accounter.alterMakeDeposit(makeDeposit);

			{// VOIDING MAKEDEPOSIT

				MakeDeposit c = makeDeposit;
				List<TransactionMakeDeposit> ti;
				List<TransactionMakeDeposit> ti2 = new ArrayList<TransactionMakeDeposit>();
				assertNotNull(c);
				assertEquals(c.getType(), accounterDao.getMakeDeposit(
						company.getId(), c.getId()).getType());
				assertEquals(c.getCashBackAccount().getName(), accounterDao
						.getMakeDeposit(company.getId(), c.getId())
						.getCashBackAccount().getName());
				assertEquals(c.getCashBackAmount(), accounterDao
						.getMakeDeposit(company.getId(), c.getId())
						.getCashBackAmount());
				assertEquals(c.getDate(), accounterDao.getMakeDeposit(
						company.getId(), c.getId()).getDate());
				assertEquals(c.getTotal(), accounterDao.getMakeDeposit(
						company.getId(), c.getId()).getTotal());
				assertEquals(c.getDepositIn().getName(), accounterDao
						.getMakeDeposit(company.getId(), c.getId())
						.getDepositIn().getName());
				assertEquals(c.getTotal(), accounterDao.getMakeDeposit(
						company.getId(), c.getId()).getTotal());
				assertEquals(c.getNumber(), accounterDao.getMakeDeposit(
						company.getId(), c.getId()).getNumber());

				// Take all the transaction items that are involved in the
				// writecheck and store them in a List.
				ti = c.getTransactionMakeDeposit();
				// Now, we need to iterate all the items and make necessary
				// changes in the corresponding accounts.
				Iterator<TransactionMakeDeposit> j = ti.iterator();

				Account acc = null;
				while (j.hasNext()) {// while there is a transaction item
					TransactionMakeDeposit it = (TransactionMakeDeposit) j
							.next();
					ti2.add(it);
					if (it.getIsNewEntry()) {
						if (it.getType() == TransactionMakeDeposit.TYPE_CUSTOMER)
							acc = accounterDao.getAccount(company.getId(),
									"Accounts Receivable");
						else if (it.getType() == TransactionMakeDeposit.TYPE_VENDOR)
							acc = accounterDao.getAccount(company.getId(),
									"Accounts Payable");
						else
							acc = it.getAccount();
					} else
						acc = accounterDao.getAccount(company.getId(),
								"Un Deposited Funds");

					if (acc.isIncrease()
							|| acc.getName().equals("Accounts Payable")) {
						d[getAccountVariable(accounts, acc.getName(), company)] -= it
								.getAmount();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= it
									.getAmount();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, acc.getName(), company)] += it
								.getAmount();
						String str = acc.getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += it
									.getAmount();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

				}// while there is a transaction item

				d[getAccountVariable(accounts, c.getDepositIn().getName(),
						company)] -= c.getTotal();
				// checking whether the depositIn account is updated. If it has
				// a parent it also need to be updated
				acc = c.getCashBackAccount();
				if (acc.isIncrease()) {

					d[getAccountVariable(accounts, acc.getName(), company)] += c
							.getCashBackAmount();
					String str = acc.getName();
					while ((accounterDao.getAccount(company.getId(), str)
							.getParent() != null)) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] += c.getCashBackAmount();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				}

				else {
					d[getAccountVariable(accounts, acc.getName(), company)] -= c
							.getCashBackAmount();
					String str = acc.getName();
					while (accounterDao.getAccount(company.getId(), str)
							.getParent() != null) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] -= c.getCashBackAmount();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				}

				// If the last transaction item of the last CashSale is done, we
				// need to check the accounts table whether all the accounts are
				// updated perfectly.
				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), d[getAccountVariable(accounts,
							accounterDao.getAccount(company.getId(), (l1 + 1))
									.getName(), company)], accounterDao
							.getAccount(company.getId(), (l1 + 1))
							.getTotalBalance());

			}// VOIDING MAKEDEPOSIT
			return makeDeposit;
		}
		return null;
	}

	public ReceivePayment testVoidReceivePayment(ReceivePayment receivePayment,
			List<Account> accounts, List<TaxCode> codes) throws DAOException {
		company = accounterDao.getCompany(1l);
		if (!receivePayment.isVoid() && receivePayment.canVoidOrEdit) {
			List<CreditsAndPayments> cps = accounterDao
					.getCreditsAndPayments(receivePayment.company.id);
			ReceivePayment rpBackUp = new ReceivePayment();
			{
				rpBackUp.amount = receivePayment.amount;
				rpBackUp.id = receivePayment.id;
				rpBackUp.status = receivePayment.status;
				rpBackUp.total = receivePayment.total;
				rpBackUp.totalAppliedCredits = receivePayment.totalAppliedCredits;
				rpBackUp.totalCashDiscount = receivePayment.totalCashDiscount;
				rpBackUp.totalWriteOff = receivePayment.totalWriteOff;
				rpBackUp.unUsedPayments = receivePayment.unUsedPayments;
				rpBackUp.unUsedCredits = receivePayment.unUsedCredits;
				{
					List<TransactionReceivePayment> trps = receivePayment.transactionReceivePayment;
					for (int i = 0; i < trps.size(); i++) {
						TransactionReceivePayment trp = new TransactionReceivePayment();
						trp.appliedCredits = trps.get(i).appliedCredits;
						trp.cashDiscount = trps.get(i).cashDiscount;
						trp.id = trps.get(i).id;
						trp.invoiceAmount = trps.get(i).invoiceAmount;
						trp.payment = trps.get(i).payment;
						trp.writeOff = trps.get(i).writeOff;
						{
							List<TransactionCreditsAndPayments> tcps = new ArrayList<TransactionCreditsAndPayments>();
							for (int j = 0; j < trp.transactionCreditsAndPayments
									.size(); j++) {
								TransactionCreditsAndPayments tcp = new TransactionCreditsAndPayments();
								tcp.amountToUse = trp.transactionCreditsAndPayments
										.get(j).amountToUse;
								tcp.memo = trp.transactionCreditsAndPayments
										.get(j).memo;
								tcp.id = trp.transactionCreditsAndPayments
										.get(j).id;
								{
									CreditsAndPayments cp = new CreditsAndPayments();
									cp.balance = trp.transactionCreditsAndPayments
											.get(j).creditsAndPayments.balance;
									cp.creditAmount = trp.transactionCreditsAndPayments
											.get(j).creditsAndPayments.creditAmount;
									cp.id = trp.transactionCreditsAndPayments
											.get(j).creditsAndPayments.id;
									tcp.creditsAndPayments = cp;
								}
								tcps.add(tcp);
							}
							trp.transactionCreditsAndPayments = tcps;
						}
						if (trps.get(i).customerRefund != null) {
							CustomerRefund cr = new CustomerRefund();
							cr.balanceDue = trps.get(i).customerRefund.balanceDue;
							cr.total = trps.get(i).customerRefund.total;
							cr.id = trps.get(i).customerRefund.id;
							cr.payments = trps.get(i).customerRefund.payments;
							cr.status = trps.get(i).customerRefund.status;
							trp.customerRefund = cr;
						}
						if (trps.get(i).invoice != null) {
							Invoice inv = new Invoice();
							inv.balanceDue = trps.get(i).invoice.balanceDue;
							inv.total = trps.get(i).invoice.total;
							inv.id = trps.get(i).invoice.id;
							inv.payments = trps.get(i).invoice.payments;
							inv.status = trps.get(i).invoice.status;
							trp.invoice = inv;
						}
						trps.add(trp);
					}
					rpBackUp.transactionReceivePayment = trps;
					if (receivePayment.creditsAndPayments != null) {

						CreditsAndPayments cp = new CreditsAndPayments();
						cp.balance = receivePayment.creditsAndPayments.balance;
						cp.creditAmount = receivePayment.creditsAndPayments.creditAmount;
						cp.id = receivePayment.creditsAndPayments.id;
						cp.memo = receivePayment.creditsAndPayments.memo;
						rpBackUp.creditsAndPayments = cp;
					}
				}
			}
			receivePayment.setVoid(true);
			accounter.alterReceivePayment(receivePayment);

			{
				assertEquals("CashDiscount", receivePayment.totalCashDiscount,
						0.0);
				assertEquals("Total Write off", receivePayment.totalWriteOff,
						0.0);
				assertEquals("Total  Applied Credits",
						receivePayment.totalAppliedCredits, 0.0);
				assertEquals("Total", receivePayment.total, 0.0);
				assertEquals("Amount", receivePayment.amount, rpBackUp.amount);
				assertEquals("Unused Payments", receivePayment.unUsedPayments,
						receivePayment.amount);
				assertEquals("Unused Credits", receivePayment.unUsedCredits,
						0.0);
				{
					List<TransactionReceivePayment> trps = receivePayment.transactionReceivePayment;
					for (int i = 0; i < trps.size(); i++) {
						assertEquals("credits", trps.get(i).appliedCredits, 0.0);
						assertEquals("Write off", trps.get(i).writeOff, 0.0);
						assertEquals("Cash Discount", trps.get(i).cashDiscount,
								0.0);
						List<TransactionCreditsAndPayments> tcps = trps.get(i).transactionCreditsAndPayments;
						for (int j = 0; j < tcps.size(); j++) {
							assertEquals("Amount to use",
									tcps.get(j).amountToUse, 0.0);
							CreditsAndPayments cp1 = null;// =accounterDao.
															// getCreditAndPayment
															// (receivePayment.
															// company.id,
							// tcps.get(j).creditsAndPayments.id);
							CreditsAndPayments cp2 = tcps.get(j).creditsAndPayments;
							double amount = 0;
							for (int k = 0; k < rpBackUp.transactionReceivePayment
									.size(); k++) {
								for (int l = 0; l < rpBackUp.transactionReceivePayment
										.get(k).transactionCreditsAndPayments
										.size(); l++) {

									TransactionCreditsAndPayments tcp = rpBackUp.transactionReceivePayment
											.get(k).transactionCreditsAndPayments
											.get(l);
									if (tcp.creditsAndPayments.id == cp2.id
											&& tcp.creditsAndPayments.memo
													.equals(cp2.memo)) {
										amount += tcp.amountToUse;
										cp1 = tcp.creditsAndPayments;
									}
								}
							}
							assertEquals("CreditsAndPayments- Credit Amount",
									cp1.creditAmount + amount, cp2.creditAmount);
						}
					}
				}
				{
					List<TransactionReceivePayment> trps = rpBackUp.transactionReceivePayment;
					for (int i = 0; i < trps.size(); i++) {
						if (trps.get(i).customerRefund != null) {
							CustomerRefund cr = accounterDao
									.getCustomerRefunds(
											receivePayment.company.id, trps
													.get(i).customerRefund.id);
							assertEquals("Customer Refund Balance due",
									cr.balanceDue, cr.total);
							assertEquals("Customer Refund Payments",
									cr.payments, 0.0);
							assertEquals("Customer Refund status", cr.status,
									trps.get(i).customerRefund.status);
						}
						if (trps.get(i).invoice != null) {
							Invoice in = accounterDao.getInvoice(
									receivePayment.company.id,
									trps.get(i).invoice.id);
							assertEquals("InvoiceBalance due", in.balanceDue,
									in.total);
							assertEquals("Invoice Payments", in.payments, 0.0);
							assertEquals("Invoice Status", in.status, trps
									.get(i).invoice.status);
						}
					}
				}
			}

			{// voiding receive payment
				ReceivePayment r = receivePayment;

				if (r.getDepositIn().isIncrease()) {
					d[getAccountVariable(accounts, r.getDepositIn().getName(),
							company)] += r.getAmount();
					String str = r.getDepositIn().getName();
					while ((accounterDao.getAccount(company.getId(), str)
							.getParent() != null)) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] += r.getAmount();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				} else {
					d[getAccountVariable(accounts, r.getDepositIn().getName(),
							company)] -= r.getAmount();
					String str = r.getDepositIn().getName();
					while ((accounterDao.getAccount(company.getId(), str)
							.getParent() != null)) {
						d[getAccountVariable(accounts, accounterDao.getAccount(
								company.getId(), str).getParent().getName(),
								company)] -= r.getAmount();
						str = accounterDao.getAccount(company.getId(), str)
								.getParent().getName();
					}
				}

				d[getAccountVariable(accounts, company
						.getAccountsReceivableAccount().getName(), company)] += (r
						.getAmount()
						+ r.getTotalCashDiscount() + r.getTotalWriteOff());

				List<TransactionReceivePayment> trp = r
						.getTransactionReceivePayment();
				Iterator<TransactionReceivePayment> i2 = trp.iterator();
				while (i2.hasNext()) {
					TransactionReceivePayment tp = i2.next();
					if (tp.getDiscountAccount() != null) {
						if (tp.getDiscountAccount().isIncrease()) {
							d[getAccountVariable(accounts, tp
									.getDiscountAccount().getName(), company)] += tp
									.getCashDiscount();
							String str = tp.getDiscountAccount().getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += tp
										.getCashDiscount();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						} else {
							d[getAccountVariable(accounts, tp
									.getDiscountAccount().getName(), company)] -= tp
									.getCashDiscount();
							String str = tp.getDiscountAccount().getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= tp
										.getCashDiscount();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}
					}
					if (tp.getWriteOffAccount() != null) {
						if (tp.getWriteOffAccount().isIncrease()) {
							d[getAccountVariable(accounts, tp
									.getWriteOffAccount().getName(), company)] += tp
									.getWriteOff();
							String str = tp.getWriteOffAccount().getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += tp
										.getWriteOff();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						} else {
							d[getAccountVariable(accounts, tp
									.getWriteOffAccount().getName(), company)] -= tp
									.getWriteOff();
							String str = tp.getWriteOffAccount().getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= tp
										.getWriteOff();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}
					}
				}
				for (long l1 = 0; l1 < accounts.size(); l1++)
					assertEquals(accounterDao.getAccount(company.getId(),
							(l1 + 1)).getName(), d[getAccountVariable(accounts,
							accounterDao.getAccount(company.getId(), (l1 + 1))
									.getName(), company)], accounterDao
							.getAccount(company.getId(), (l1 + 1))
							.getTotalBalance());

			}// voiding receive payment
			return receivePayment;
		}
		return null;
	}

	public PayBill testVoidVendorPayment(PayBill vendorPayment,
			List<Account> accounts) throws DAOException {
		if (!vendorPayment.isVoid() && vendorPayment.canVoidOrEdit) {
			List<CreditsAndPayments> cps = accounterDao
					.getCreditsAndPayments(vendorPayment.company.id);
			PayBill vpBackUp = new PayBill();
			{
				vpBackUp.id = vendorPayment.id;
				vpBackUp.status = vendorPayment.status;
				vpBackUp.total = vendorPayment.total;
				vpBackUp.unusedAmount = vendorPayment.unusedAmount;
				if (vendorPayment.transactionPayBill != null) {
					List<TransactionPayBill> tps = vendorPayment.transactionPayBill;
					for (int i = 0; i < tps.size(); i++) {
						TransactionPayBill tp = new TransactionPayBill();
						tp.appliedCredits = tps.get(i).appliedCredits;
						tp.cashDiscount = tps.get(i).cashDiscount;
						tp.id = tps.get(i).id;
						tp.originalAmount = tps.get(i).originalAmount;
						tp.payment = tps.get(i).payment;
						{
							List<TransactionCreditsAndPayments> tcps = new ArrayList<TransactionCreditsAndPayments>();
							for (int j = 0; j < tp.transactionCreditsAndPayments
									.size(); j++) {
								TransactionCreditsAndPayments tcp = new TransactionCreditsAndPayments();
								tcp.amountToUse = tp.transactionCreditsAndPayments
										.get(j).amountToUse;
								tcp.memo = tp.transactionCreditsAndPayments
										.get(j).memo;
								tcp.id = tp.transactionCreditsAndPayments
										.get(j).id;
								{
									CreditsAndPayments cp = new CreditsAndPayments();
									cp.balance = tp.transactionCreditsAndPayments
											.get(j).creditsAndPayments.balance;
									cp.creditAmount = tp.transactionCreditsAndPayments
											.get(j).creditsAndPayments.creditAmount;
									cp.id = tp.transactionCreditsAndPayments
											.get(j).creditsAndPayments.id;
									tcp.creditsAndPayments = cp;
								}
								tcps.add(tcp);
							}
							tp.transactionCreditsAndPayments = tcps;
						}
						if (tps.get(i).enterBill != null) {
							EnterBill cr = new EnterBill();
							cr.balanceDue = tps.get(i).enterBill.balanceDue;
							cr.total = tps.get(i).enterBill.total;
							cr.id = tps.get(i).enterBill.id;
							cr.payments = tps.get(i).enterBill.payments;
							cr.status = tps.get(i).enterBill.status;
							tp.enterBill = cr;
						}
						if (tps.get(i).transactionMakeDeposit != null) {
							TransactionMakeDeposit inv = new TransactionMakeDeposit();
							inv.balanceDue = tps.get(i).transactionMakeDeposit.balanceDue;
							inv.amount = tps.get(i).transactionMakeDeposit.amount;
							inv.id = tps.get(i).transactionMakeDeposit.id;
							inv.payments = tps.get(i).transactionMakeDeposit.payments;
							tp.transactionMakeDeposit = inv;
						}
						tps.add(tp);
					}
					vpBackUp.transactionPayBill = tps;
					if (vendorPayment.creditsAndPayments != null) {

						CreditsAndPayments cp = new CreditsAndPayments();
						cp.balance = vendorPayment.creditsAndPayments.balance;
						cp.creditAmount = vendorPayment.creditsAndPayments.creditAmount;
						cp.id = vendorPayment.creditsAndPayments.id;
						cp.memo = vendorPayment.creditsAndPayments.memo;
						vpBackUp.creditsAndPayments = cp;
					}
				}
			}
			vendorPayment.setVoid(true);
			accounter.alterPayBill(vendorPayment);
			{
				assertEquals("Total", vendorPayment.total, 0.0);
				assertEquals("Unused Credits", vendorPayment.unusedAmount, 0.0);
				if (vendorPayment.transactionPayBill != null) {
					{
						List<TransactionPayBill> trps = vendorPayment.transactionPayBill;
						for (int i = 0; i < trps.size(); i++) {
							assertEquals("credits", trps.get(i).appliedCredits,
									0.0);
							assertEquals("Cash Discount",
									trps.get(i).cashDiscount, 0.0);
							List<TransactionCreditsAndPayments> tcps = trps
									.get(i).transactionCreditsAndPayments;
							for (int j = 0; j < tcps.size(); j++) {
								assertEquals("Amount to use",
										tcps.get(j).amountToUse, 0.0);
								CreditsAndPayments cp1 = null;// =accounterDao.
																// getCreditAndPayment
																// (
																// receivePayment
																// .company.id,
								// tcps.get(j).creditsAndPayments.id);
								CreditsAndPayments cp2 = tcps.get(j).creditsAndPayments;
								double amount = 0;
								for (int k = 0; k < vpBackUp.transactionPayBill
										.size(); k++) {
									for (int l = 0; l < vpBackUp.transactionPayBill
											.get(k).transactionCreditsAndPayments
											.size(); l++) {
										TransactionCreditsAndPayments tcp = vpBackUp.transactionPayBill
												.get(k).transactionCreditsAndPayments
												.get(l);
										if (tcp.creditsAndPayments.id == cp2.id
												&& tcp.creditsAndPayments.memo
														.equals(cp2.memo)) {
											amount += tcp.amountToUse;
											cp1 = tcp.creditsAndPayments;
										}
									}
								}
								assertEquals(
										"CreditsAndPayments- Credit Amount",
										cp1.creditAmount + amount,
										cp2.creditAmount);
							}
						}
					}
					{
						List<TransactionPayBill> trps = vpBackUp.transactionPayBill;
						for (int i = 0; i < trps.size(); i++) {
							if (trps.get(i).enterBill != null) {
								EnterBill cr = accounterDao.getEnterBill(
										vendorPayment.company.id,
										trps.get(i).enterBill.id);
								assertEquals("EnterBill Balance due",
										cr.balanceDue, cr.total);
								assertEquals("EnterBill Payments", cr.payments,
										0.0);
								assertEquals("EnterBill status", cr.status,
										trps.get(i).enterBill.status);
							}
							if (trps.get(i).transactionMakeDeposit != null) {
								TransactionMakeDeposit t = trps.get(i).transactionMakeDeposit;
								MakeDeposit m = accounterDao.getMakeDeposit(
										vendorPayment.company.id,
										t.makeDeposit.id);
								List<TransactionMakeDeposit> tmds = m.transactionMakeDeposit;
								for (int j = 0; j < tmds.size(); j++) {
									if (tmds.get(j).id == t.id) {
										TransactionMakeDeposit in = m.transactionMakeDeposit
												.get(0);
										assertEquals("InvoiceBalance due",
												in.balanceDue, in.amount);
										assertEquals("Invoice Payments",
												in.payments, 0.0);
									}
								}
							}
						}
					}
				}
			}

			{

				{
					PayBill r = vendorPayment;
					if (r.getPayFrom().isIncrease()) {
						d[getAccountVariable(accounts,
								r.getPayFrom().getName(), company)] -= r
								.getTotal();
						String str = r.getPayFrom().getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= r
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts,
								r.getPayFrom().getName(), company)] += r
								.getTotal();
						String str = r.getPayFrom().getName();
						while ((accounterDao.getAccount(company.getId(), str)
								.getParent() != null)) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += r
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					d[getAccountVariable(accounts, company
							.getAccountsPayableAccount().getName(), company)] += (r
							.getTotal());

					if (r.getPayBillType() == PayBill.TYPE_PAYBILL) {
						d[getAccountVariable(accounts, company
								.getAccountsPayableAccount().getName(), company)] -= getAmountTotalDiscount(r
								.getTransactionPayBill());
						List<TransactionPayBill> trp = r
								.getTransactionPayBill();
						Iterator<TransactionPayBill> i2 = trp.iterator();
						while (i2.hasNext()) {
							TransactionPayBill tp = i2.next();
							if (tp.getDiscountAccount().isIncrease()) {
								d[getAccountVariable(accounts, tp
										.getDiscountAccount().getName(),
										company)] -= tp.getCashDiscount();
								String str = tp.getDiscountAccount().getName();
								while ((accounterDao.getAccount(
										company.getId(), str).getParent() != null)) {
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(), str)
											.getParent().getName(), company)] -= tp
											.getCashDiscount();
									str = accounterDao.getAccount(
											company.getId(), str).getParent()
											.getName();
								}
							} else {
								d[getAccountVariable(accounts, tp
										.getDiscountAccount().getName(),
										company)] += tp.getCashDiscount();
								String str = tp.getDiscountAccount().getName();
								while ((accounterDao.getAccount(
										company.getId(), str).getParent() != null)) {
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(), str)
											.getParent().getName(), company)] += tp
											.getCashDiscount();
									str = accounterDao.getAccount(
											company.getId(), str).getParent()
											.getName();
								}
							}
						}
					}
					for (long l1 = 0; l1 < accounts.size(); l1++)
						assertEquals(accounterDao.getAccount(company.getId(),
								(l1 + 1)).getName(), d[getAccountVariable(
								accounts, accounterDao.getAccount(
										company.getId(), (l1 + 1)).getName(),
								company)], accounterDao.getAccount(
								company.getId(), (l1 + 1)).getTotalBalance());
				}

			}
			return vendorPayment;
		}
		return null;
	}

	public CreditCardCharge testVoidCreditCardCharge(
			CreditCardCharge creditCardCharge, List<Account> accounts)
			throws DAOException {
		if (!creditCardCharge.isVoid() && creditCardCharge.canVoidOrEdit) {
			creditCardCharge.setVoid(true);
			accounter.alterCreditCardCharge(creditCardCharge);
			{

				// From here testing starts
				// All the CreditCardCharge are stored in a ArrayList, and we
				// will
				// iterate them.
				// For every CashSale, we perform necessary operations on the
				// accounts array and atlast we check them with the actual
				// values that are stored in
				// the database.
				CreditCardCharge c;
				List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
				{
					c = creditCardCharge;
					assertNotNull(c);
					assertNotNull(c.getVendor());
					assertNotNull(c.getVendor().getName());
					assertEquals(c.getVendor().getName(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getVendor().getName());
					assertEquals(c.getDate(), accounterDao.getCreditCardCharge(
							company.getId(), c.getId()).getDate());
					assertNotNull(c.getPayFrom());
					assertNotNull(c.getPayFrom().getName());
					assertEquals(c.getPayFrom().getName(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getPayFrom().getName());
					assertEquals(c.getType(), accounterDao.getCreditCardCharge(
							company.getId(), c.getId()).getType());
					assertEquals(c.getTotal(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getTotal());
					if (c.getPaymentMethod() != null)
						if (c.getPaymentMethod() != null)
							assertEquals(c.getPaymentMethod(), accounterDao
									.getCreditCardCharge(company.getId(),
											c.getId()).getPaymentMethod());
					assertEquals(c.getNumber(), accounterDao
							.getCreditCardCharge(company.getId(), c.getId())
							.getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionItems();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionItem> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionItem it = (TransactionItem) j.next();
						ti2.add(it);
						Account acc = null;
						if (it.getType() == TransactionItem.TYPE_ITEM)
							acc = it.getItem().getExpenseAccount();
						else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
							acc = it.getAccount();
						else
							// if(it.getType()==TransactionItem.TYPE_SALESTAX)
							acc = it.getAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase

							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getLineTotal();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getLineTotal();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getLineTotal();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase

					}// while there is a transaction item

					// checking whether the depositIn account is updated. If it
					// has
					// a parent it also need to be updated
					if (c.getPayFrom().isIncrease()) {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] -= c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] += c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();

						}

						// If the last transaction item of the last CashSale is
						// done, we need to check the accounts table whether all
						// the
						// accounts are updated perfectly.
						for (long l1 = 0; l1 < accounts.size(); l1++)
							assertEquals(accounterDao.getAccount(
									company.getId(), (l1 + 1)).getName(),
									d[getAccountVariable(accounts, accounterDao
											.getAccount(company.getId(),
													(l1 + 1)).getName(),
											company)], accounterDao.getAccount(
											company.getId(), (l1 + 1))
											.getTotalBalance());
					}
				}

			}
			return creditCardCharge;
		}
		return null;
	}

	public TransferFund testVoidTransferFund(TransferFund transferFund,
			List<Account> accounts) throws DAOException {
		if (!transferFund.isVoid() && transferFund.canVoidOrEdit) {
			transferFund.setVoid(true);
			accounter.alterTransferFund(transferFund);
			{
				{
					TransferFund t = transferFund;
					if (t.getTransferFrom().isIncrease()) {
						d[getAccountVariable(accounts, t.getTransferFrom()
								.getName(), company)] -= t.getTotal();
						String str = t.getTransferFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, t.getTransferFrom()
								.getName(), company)] += t.getTotal();
						String str = t.getTransferFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					if (t.getTransferTo().isIncrease()) {
						d[getAccountVariable(accounts, t.getTransferTo()
								.getName(), company)] += t.getTotal();
						String str = t.getTransferTo().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts, t.getTransferTo()
								.getName(), company)] -= t.getTotal();
						String str = t.getTransferTo().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= t
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}
					for (long l1 = 0; l1 < accounts.size(); l1++)
						assertEquals(accounterDao.getAccount(company.getId(),
								(l1 + 1)).getName(), d[getAccountVariable(
								accounts, accounterDao.getAccount(
										company.getId(), (l1 + 1)).getName(),
								company)], accounterDao.getAccount(
								company.getId(), (l1 + 1)).getTotalBalance());
				}
			}
			return transferFund;
		}
		return null;
	}

	public PaySalesTax testVoidPaySalesTax(PaySalesTax paySalesTax,
			List<Account> accounts) throws DAOException {
		if (!paySalesTax.isVoid() && paySalesTax.canVoidOrEdit) {
			paySalesTax.setVoid(true);
			accounter.alterPaySalesTax(paySalesTax);
			{
				PaySalesTax c;
				List<TransactionPaySalesTax> ti, ti2 = new ArrayList<TransactionPaySalesTax>();
				{
					c = paySalesTax;
					assertNotNull(c);
					assertNotNull(c.getTotal());
					assertEquals(c.getTotal(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getTotal());
					// assertEquals(c.getTaxAgency().getName(), //
					// accounterDao.getTaxAgency(company.getId(), //
					// c.getTaxAgency().getId()));
					assertEquals(c.getDate(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getDate());
					assertEquals(c.getTotal(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getTotal());
					assertEquals(c.getType(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getType());
					assertEquals(c.getNumber(), accounterDao.getPaySalesTax(
							company.getId(), c.getId()).getNumber());

					// Take all the transaction items that are involved in the
					// cashsale and store them in a List.
					ti = c.getTransactionPaySalesTax();
					// Now, we need to iterate all the items and make necessary
					// changes in the corresponding accounts.
					Iterator<TransactionPaySalesTax> j = ti.iterator();

					while (j.hasNext()) {// while there is a transaction item
						TransactionPaySalesTax it = (TransactionPaySalesTax) j
								.next();
						ti2.add(it);
						Account acc = null;
						acc = it.getTaxAgency().getLiabilityAccount();

						// If the isIncrease() value is true, then the account
						// value
						// is to be increased, else decreased.
						if (acc.isIncrease()) {// if account is increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] += it.getAmountToPay();
							String str = acc.getName();
							while ((accounterDao.getAccount(company.getId(),
									str).getParent() != null)) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] += it
										.getAmountToPay();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is increase

						else {// if for account is not increase
							d[getAccountVariable(accounts, acc.getName(),
									company)] -= it.getAmountToPay();
							String str = acc.getName();
							while (accounterDao
									.getAccount(company.getId(), str)
									.getParent() != null) {
								d[getAccountVariable(accounts, accounterDao
										.getAccount(company.getId(), str)
										.getParent().getName(), company)] -= it
										.getAmountToPay();
								str = accounterDao.getAccount(company.getId(),
										str).getParent().getName();
							}
						}// if for account is not increase

					}// while there is a transaction item

					// checking whether the Accounts Payable account is updated.
					if (c.getPayFrom().isIncrease()) {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] -= c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] -= c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					} else {
						d[getAccountVariable(accounts,
								c.getPayFrom().getName(), company)] += c
								.getTotal();
						String str = c.getPayFrom().getName();
						while (accounterDao.getAccount(company.getId(), str)
								.getParent() != null) {
							d[getAccountVariable(accounts, accounterDao
									.getAccount(company.getId(), str)
									.getParent().getName(), company)] += c
									.getTotal();
							str = accounterDao.getAccount(company.getId(), str)
									.getParent().getName();
						}
					}

					// If the last transaction item of the last EnterBill is
					// done, // // we need to check the accounts table whether
					// all the
					// accounts are updated perfectly.
					for (long l1 = 0; l1 < accounts.size(); l1++)
						assertEquals(accounterDao.getAccount(company.getId(),
								(l1 + 1)).getName(), d[getAccountVariable(
								accounts, accounterDao.getAccount(
										company.getId(), (l1 + 1)).getName(),
								company)], accounterDao.getAccount(
								company.getId(), (l1 + 1)).getTotalBalance());
				}
			}
			return paySalesTax;
		}
		return null;
	}

	public void testTrailBalance() throws DAOException, ParseException {
		company = accounterDao.getCompany(1l);
		List<Account> accounts = accounterDao.getAccounts(company.getId());
		List<TaxCode> codes = accounterDao.getTaxCodes(company.getId());

		if (checkTrialBalance) {
			setTrialBalanceAccountVariables(accounts, company.getId());
			setDefaultAccountVariables(accounts, company.getId());
			taxAgencies = new double[codes.size()];
			for (int i = 0; i < taxAgencies.length; i++)
				taxAgencies[getTaxAgencyVariable(codes, codes.get(i)
						.getTaxAgency().getName(), company)] = codes.get(i)
						.getTaxAgency().getBalance();

			double balance = 0.0;
			for (int n = 0; n < trialBalance.length; n++) {
				balance += trialBalance[n];
			}
			if (createCashSales)
				setTrialAllCashSales(accounts, company, false);
			if (createInvoices)
				setTrialAllInvoices(accounts, company, false);
			if (createCustomerCreditMemos)
				setTrialAllCustomerCreditMemos(accounts, company, false);
			if (createCustomerRefunds)
				setTrialAllCustomerRefunds(accounts, company, false);
			if (createEnterBills)
				setTrialAllEnterBills(accounts, company, false);
			if (createVendorCreditMemos)
				setTrialAllVendorCreditMemos(accounts, company, false);
			if (createCashPurchases)
				setTrialAllCashPurchases(accounts, company, false);
			if (createWriteChecks)
				setTrialAllWriteChecks(accounts, company, false);
			if (createMakeDeposits)
				setTrialAllMakeDeposits(accounts, company, false);
			if (createReceivePayments)
				setTrialAllReceivePayments(accounts, company, false);
			if (createVendorPayments || createPayBills)
				setTrialAllVendorPayments(accounts, company, false);
			if (createCreditCardCharges)
				setTrialAllCreditCardCharges(accounts, company, false);
			if (createTransferFunds)
				setTrialAllTransferFunds(accounts, company, false);
			if (createPaySalesTaxes)
				setTrialAllPaySalesTaxes(accounts, company, false);

			List<TrialBalance> tbs = accounterReportDAOService.getTrialBalance(
					company.getId(), "2009-01-01", dueDate(1));
			trialBalanceList = tbs;
			balance = 0.0;
			for (int n = 0; n < trialBalance.length; n++) {
				balance += trialBalance[n];
			}
			assertEquals(balance, 0.0);
			Iterator<TrialBalance> i = tbs.iterator();
			while (i.hasNext()) {
				TrialBalance tb = (TrialBalance) i.next();
				System.out.println("Account =" + tb.getAccountName() + " id ="
						+ tb.getAccountId() + "a_number="
						+ tb.getAccountNumber() + "account type="
						+ tb.getAccountType() + "Parent id="
						+ tb.getParentAccount() + "amount =" + tb.getAmount());
				if (tb.getAmount() != 0.0)
					assertEquals(tb.getAccountName(),
							trialBalance[getAccountVariable(accounts, tb
									.getAccountName(), company)], tb
									.getAmount());
			}
		}
		if (checkVoid) {
			company = accounterDao.getCompany(1l);
			if (voidCashSales) {
				List<CashSales> cashSales = accounterDao.getCashSales(company
						.getId());
				for (int i = 0; i < (1 + (cashSales.size() / 2)); i++) {
					if (cashSales.size() > 0)
						setTrialVoidCashSales(accounts, company,
								testVoidCashSales(cashSales.get(i), accounts,
										codes));
				}
			}
			if (voidInvoices) {
				List<Invoice> invoices = accounterDao.getInvoices(company
						.getId());
				for (int i = 0; i < (invoices.size() / 2 + 1); i++) {
					if (invoices.size() > 0)
						setTrialVoidInvoices(accounts, company,
								testVoidInvoice(invoices.get(i), accounts,
										codes));
				}
			}
			if (voidCustomerCreditMemos) {
				List<CustomerCreditMemo> customerCreditsMemos = accounterDao
						.getCustomerCreditMemos(company.getId());
				for (int i = 0; i < (customerCreditsMemos.size() / 2) + 1; i++) {
					if (customerCreditsMemos.size() > 0)
						setTrialVoidCustomerCreditMemos(accounts, company,
								testVoidCustomerCreditMemo(customerCreditsMemos
										.get(i), accounts, codes));
				}
			}
			if (voidCustomerRefunds) {
				List<CustomerRefund> customerRefunds = accounterDao
						.getCustomerRefunds(company.getId());
				for (int i = 0; i < (customerRefunds.size() / 2) + 1; i++) {
					if (customerRefunds.size() > 0)
						setTrialVoidCustomerRefunds(accounts, company,
								testVoidCustomerRefund(customerRefunds.get(i),
										accounts, codes));
				}
			}
			if (voidEnterBills) {
				List<EnterBill> enterBills = accounterDao.getEnterBills(company
						.getId());
				for (int i = 0; i < (enterBills.size() / 2) + 1; i++) {
					if (enterBills.size() > 0)
						setTrialVoidEnterBills(accounts, company,
								testVoidEnterBills(enterBills.get(i), accounts,
										codes));
				}
			}
			if (voidVendorCreditMemos) {
				List<VendorCreditMemo> vendorCreditMemos = accounterDao
						.getVendorCreditMemos(company.getId());
				for (int i = 0; i < (vendorCreditMemos.size() / 2) + 1; i++) {
					if (vendorCreditMemos.size() > 0)
						setTrialVoidVendorCreditMemos(accounts, company,
								testVoidVendorCreditMemo(vendorCreditMemos
										.get(i), accounts, codes));
				}
			}
			if (voidCashPurchases) {
				List<CashPurchase> cashPurchases = accounterDao
						.getCashPurchases(company.getId());
				for (int i = 0; i < (cashPurchases.size() / 2) + 1; i++) {
					if (cashPurchases.size() > 0)
						setTrialVoidCashPurchases(accounts, company,
								testVoidCashPurchase(cashPurchases.get(i),
										accounts, codes));
				}
			}
			if (voidWriteChecks) {
				List<WriteCheck> writeChecks = accounterDao
						.getWriteChecks(company.getId());
				for (int i = 0; i < (writeChecks.size() / 2) + 1; i++) {
					if (writeChecks.size() > 0)
						setTrialVoidWriteChecks(accounts, company,
								testVoidWriteCheck(writeChecks.get(i),
										accounts, codes));
				}
			}
			if (voidMakeDeposits) {
				List<MakeDeposit> makeDeposits = accounterDao
						.getMakeDeposits(company.getId());
				for (int i = 0; i < (makeDeposits.size() / 2) + 1; i++) {
					if (makeDeposits.size() > 0)
						setTrialVoidMakeDeposits(accounts, company,
								testVoidMakeDeposit(makeDeposits.get(i),
										accounts, codes));
				}
			}
			if (voidReceivePayments) {
				List<ReceivePayment> receivePayments = accounterDao
						.getReceivePayments(company.getId());
				for (int i = 0; i < (receivePayments.size() / 2) + 1; i++) {
					if (receivePayments.size() > 0)
						setTrialVoidReceivePayments(accounts, company,
								testVoidReceivePayment(receivePayments.get(i),
										accounts, codes));
				}
			}
			if (voidPayBills) {
				List<PayBill> payBills = accounterDao.getPayBills(company
						.getId());
				for (int i = 0; i < (payBills.size() / 2) + 1; i++) {
					if (payBills.size() > 0)
						setTrialVoidVendorPayments(
								accounts,
								company,
								testVoidVendorPayment(payBills.get(i), accounts));
				}
			}
			if (voidCreditCardCharges) {
				List<CreditCardCharge> creditCardCharges = accounterDao
						.getCreditCardCharges(company.getId());
				for (int i = 0; i < (creditCardCharges.size() / 2) + 1; i++) {
					if (creditCardCharges.size() > 0)
						setTrialVoidCreditCardCharges(accounts, company,
								testVoidCreditCardCharge(creditCardCharges
										.get(i), accounts));
				}
			}
			if (voidTransferFunds) {
				List<TransferFund> transferFunds = accounterDao
						.getTransferFunds(company.getId());
				for (int i = 0; i < (transferFunds.size() / 2) + 1; i++) {
					if (transferFunds.size() > 0)
						setTrialVoidTransferFunds(accounts, company,
								testVoidTransferFund(transferFunds.get(i),
										accounts));
				}
			}
			if (voidPaySalesTaxes) {
				List<PaySalesTax> paySalesTaxes = accounterDao
						.getPaySalesTaxes(company.getId());
				for (int i = 0; i < (paySalesTaxes.size() / 2) + 1; i++) {
					if (paySalesTaxes.size() > 0)
						setTrialVoidPaySalesTaxes(accounts, company,
								testVoidPaySalesTax(paySalesTaxes.get(i),
										accounts));
				}
			}
			double balance = 0.0;
			for (int n = 0; n < trialBalance.length; n++)
				balance += trialBalance[n];
			assertEquals(balance, 0.0);
			List<TrialBalance> tbs = accounterReportDAOService.getTrialBalance(
					company.getId(), "2009-01-01", dueDate(1));

			for (Iterator<TrialBalance> i = tbs.iterator(); i.hasNext();) {
				TrialBalance tb1 = i.next();
				if (tb1.getAmount() != 0.0)
					assertEquals(tb1.getAccountName(),
							trialBalance[getAccountVariable(accounts, tb1
									.getAccountName(), company)], tb1
									.getAmount());
			}
		}
	}

	public void setBalanceSheetCustomers(List<Account> accounts,
			Company company, List<BalanceSheetReport> bsl) throws DAOException {
		List<Customer> customers = accounterDao.getCustomers(company.id);
		for (int i = 0; i < customers.size(); i++) {
			Customer c = customers.get(i);
			// BalanceSheetReport bs=
		}
	}

	public void setBalanceSheetVendors(List<Account> accounts, Company company,
			List<BalanceSheetReport> bsl) {

	}

	// Balance sheet Report starts here
	public void setBalanceSheetAccountVariables(List<Account> accounts, long c)
			throws DAOException {
		company = accounterDao.getCompany(1L);
		trialBalance = new double[accounts.size()];
		List<BalanceSheetReport> bsl = new ArrayList<BalanceSheetReport>();

		double bal = 0;
		for (int i = 0; i < accounts.size(); i++) {
			BalanceSheetReport bs = new BalanceSheetReport();
			Account a = accounts.get(i);
			bs.accountName = a.name;
			if (a.type == Account.TYPE_EQUITY) {
				bs.baseType = BalanceSheetReport.BASETYPE_EQUITY;
			} else if (a.type == Account.TYPE_OTHER_ASSET
					|| a.type == Account.TYPE_FIXED_ASSET
					|| accounts.get(i).type == Account.TYPE_OTHER_CURRENT_ASSET
					|| accounts.get(i).type == Account.TYPE_INVENTORY_ASSET) {
				if (a.type == Account.TYPE_OTHER_CURRENT_ASSET)
					bs.subBaseType = BalanceSheetReport.SUBBASETYPE_CURRENT_ASSET;
				if (a.type == Account.TYPE_OTHER_ASSET)
					bs.subBaseType = BalanceSheetReport.SUBBASETYPE_OTHER_ASSET;
				if (a.type == Account.TYPE_FIXED_ASSET)
					bs.subBaseType = BalanceSheetReport.SUBBASETYPE_FIXED_ASSET;

				bs.baseType = BalanceSheetReport.BASETYPE_ASSET;
			} else if (a.type == Account.TYPE_LONG_TERM_LIABILITY
					|| a.type == Account.TYPE_OTHER_CURRENT_LIABILITY
					|| a.type == Account.TYPE_PAYROLL_LIABILITY) {
				if (a.type == Account.TYPE_LONG_TERM_LIABILITY)
					bs.subBaseType = BalanceSheetReport.SUBBASETYPE_LONG_TERM_LIABILITY;
				if (a.type == Account.TYPE_OTHER_CURRENT_LIABILITY
						|| a.type == Account.TYPE_PAYROLL_LIABILITY)
					bs.subBaseType = BalanceSheetReport.SUBBASETYPE_CURRENT_LIABILITY;

				bs.baseType = BalanceSheetReport.BASETYPE_LIABILITY;
			}
			bs.amount = a.openingBalance;
			bal += a.isIncrease ? bs.amount : -1 * bs.amount;

			bsl.add(bs);
		}
		for (int i = 0; i < bsl.size(); i++) {
			if (bsl.get(i).accountName
					.equals(AccounterConstants.OPENING_BALANCE))
				bsl.get(i).amount = -bal;
		}

		// trialBalance[getAccountVariable(accounts, "Opening Balances",
		// company)] = -bal;
		if (createCustomers)
			setBalanceSheetCustomers(accounts, company, bsl);
		if (createVendors)
			setBalanceSheetVendors(accounts, company, bsl);
	}

	public double setBalanceSheetAccountOpeningBalance(String s)
			throws DAOException {
		company = accounterDao.getCompany(1l);
		return (accounterDao.getAccount(company.id, s).isIncrease() ? accounterDao
				.getAccount(company.id, s).getOpeningBalance()
				: -1
						* accounterDao.getAccount(company.id, s)
								.getOpeningBalance());
	}

	public void setBalanceSheetCashSales(List<Account> accounts, Company company)
			throws DAOException {
		List<CashSales> cashsales = accounterDao.getCashSales(company.id);
		Iterator<CashSales> i = cashsales.iterator();

		CashSales c;
		List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
		while (i.hasNext()) {
			c = (CashSales) i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				ti2.add(it);
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else if (it.getType() == TransactionItem.TYPE_SALESTAX)
					acc = it.getTaxCode().getTaxAgency().getLiabilityAccount();
				else
					acc = it.getAccount();
				System.out.println("In Balancesheet Cashsales, account="
						+ acc.getName());
				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();

				if (it.taxGroup != null) {
					Set<TaxCode> tcs = it.taxGroup.getTaxCodes();
					Iterator<TaxCode> k = tcs.iterator();

					while (k.hasNext()) {
						TaxCode tc = (TaxCode) k.next();
						double rate = getTaxRate(tc) * it.lineTotal;
						if (tc.getTaxAgency().getLiabilityAccount()
								.isIncrease())
							d[getAccountVariable(accounts, tc.getTaxAgency()
									.getLiabilityAccount().getName(), company)] += rate;
						else
							d[getAccountVariable(accounts, tc.getTaxAgency()
									.getLiabilityAccount().getName(), company)] -= rate;
					}
				}
			}// while there is a transaction item
			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			if (!c.getDepositIn().isIncrease())
				d[getAccountVariable(accounts, c.getDepositIn().getName(),
						company)] += c.getTotal();
			else
				d[getAccountVariable(accounts, c.getDepositIn().getName(),
						company)] -= c.getTotal();

		}
	}

	public void setBalanceSheetInvoices(List<Account> accounts, Company company)
			throws DAOException {
		List<Invoice> invoices = accounterDao.getInvoices(company.id);
		Iterator<Invoice> i = invoices.iterator();

		Invoice c;
		List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
		while (i.hasNext()) {
			c = (Invoice) i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				ti2.add(it);
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else if (it.getType() == TransactionItem.TYPE_SALESTAX)
					acc = it.getTaxCode().getTaxAgency().getLiabilityAccount();
				else
					// if(it.getType()==TransactionItem.TYPE_SALESTAX)
					acc = it.getAccount();

				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();

				Set<TaxCode> tcs = it.taxGroup.getTaxCodes();
				Iterator<TaxCode> k = tcs.iterator();

				while (k.hasNext()) {
					TaxCode tc = (TaxCode) k.next();
					double rate = getTaxRate(tc) * it.lineTotal;
					if (tc.getTaxAgency().getLiabilityAccount().isIncrease())
						d[getAccountVariable(accounts, tc.getTaxAgency()
								.getLiabilityAccount().getName(), company)] += rate;
					else
						d[getAccountVariable(accounts, tc.getTaxAgency()
								.getLiabilityAccount().getName(), company)] -= rate;
				}
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			d[getAccountVariable(accounts, "Accounts Receivable", company)] += c
					.getTotal();

		}
	}

	public void setBalanceSheetCustomerCreditMemos(List<Account> accounts,
			Company company) throws DAOException {
		List<CustomerCreditMemo> ccms = accounterDao
				.getCustomerCreditMemos(company.id);
		Iterator<CustomerCreditMemo> i = ccms.iterator();

		CustomerCreditMemo c;
		List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
		while (i.hasNext()) {
			c = (CustomerCreditMemo) i.next();
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				ti2.add(it);
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else if (it.getType() == TransactionItem.TYPE_SALESTAX)
					acc = it.getTaxCode().getTaxAgency().getLiabilityAccount();
				else
					// if(it.getType()==TransactionItem.TYPE_SALESTAX)
					acc = it.getAccount();

				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();

				Set<TaxCode> tcs = it.taxGroup.getTaxCodes();
				Iterator<TaxCode> k = tcs.iterator();

				while (k.hasNext()) {
					TaxCode tc = (TaxCode) k.next();
					double rate = getTaxRate(tc) * it.lineTotal;
					if (tc.getTaxAgency().getLiabilityAccount().isIncrease())
						d[getAccountVariable(accounts, tc.getTaxAgency()
								.getLiabilityAccount().getName(), company)] -= rate;
					else
						d[getAccountVariable(accounts, tc.getTaxAgency()
								.getLiabilityAccount().getName(), company)] += rate;
				}
			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			d[getAccountVariable(accounts, "Accounts Receivable", company)] -= c
					.getTotal();

		}
	}

	public void setBalanceSheetCustomerRefunds(List<Account> accounts,
			Company company) throws DAOException {
		List<CustomerRefund> ccms = accounterDao.getCustomerRefunds(company.id);
		Iterator<CustomerRefund> i = ccms.iterator();

		CustomerRefund c;
		while (i.hasNext()) {
			c = (CustomerRefund) i.next();
			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			d[getAccountVariable(accounts, "Accounts Receivable", company)] += c.total;

			if (c.getPayFrom().isIncrease())
				d[getAccountVariable(accounts, c.getPayFrom().getName(),
						company)] += c.total;
			else
				d[getAccountVariable(accounts, c.getPayFrom().getName(),
						company)] -= c.total;
		}
	}

	public void setBalanceSheetEnterBills(List<Account> accounts,
			Company company) throws DAOException {
		List<EnterBill> ebs = accounterDao.getEnterBills(company.id);
		Iterator<EnterBill> i = ebs.iterator();
		EnterBill c;
		List<TransactionItem> ti;
		while (i.hasNext()) {
			c = (EnterBill) i.next();
			// Take all the transaction items that are involved in the cashsale
			// and store them in a List.
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();

				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();

			}// while there is a transaction item

			// checking whether the Accounts Payable account is updated.
			d[getAccountVariable(accounts, "Accounts Payable", company)] += c
					.getTotal();
		}
	}

	public void setBalanceSheetVendorCreditMemos(List<Account> accounts,
			Company company) throws DAOException {
		List<VendorCreditMemo> vcms = accounterDao
				.getVendorCreditMemos(company.id);
		Iterator<VendorCreditMemo> i = vcms.iterator();
		VendorCreditMemo c;
		List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();
		while (i.hasNext()) {
			c = (VendorCreditMemo) i.next();

			// Take all the transaction items that are involved in the cashsale
			// and store them in a List.
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				ti2.add(it);
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();

				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();
			}// while there is a transaction item

			// checking whether the Accounts Payable account is updated.
			d[getAccountVariable(accounts, "Accounts Payable", company)] -= c
					.getTotal();
		}
	}

	public void setBalanceSheetCashPurchases(List<Account> accounts,
			Company company) throws DAOException {
		List<CashPurchase> cps = accounterDao.getCashPurchases(company.id);
		Iterator<CashPurchase> i = cps.iterator();
		CashPurchase c;
		List<TransactionItem> ti, ti2 = new ArrayList<TransactionItem>();

		while (i.hasNext()) {
			c = (CashPurchase) i.next();

			// Take all the transaction items that are involved in the cashsale
			// and store them in a List.
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				ti2.add(it);
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();

				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();

			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			if (c.getPayFrom().isIncrease())
				d[getAccountVariable(accounts, c.getPayFrom().getName(),
						company)] += c.getTotal();
			else
				d[getAccountVariable(accounts, c.getPayFrom().getName(),
						company)] -= c.getTotal();
		}
	}

	public void setBalanceSheetWriteChecks(List<Account> accounts,
			Company company) throws DAOException {
		List<WriteCheck> cps = accounterDao.getWriteChecks(company.id);
		Iterator<WriteCheck> i = cps.iterator();
		WriteCheck c;
		List<TransactionItem> ti;

		while (i.hasNext()) {
			c = (WriteCheck) i.next();

			// Take all the transaction items that are involved in the
			// writecheck and store them in a List.
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else if (it.getType() == TransactionItem.TYPE_SALESTAX)
					acc = it.getTaxCode().getTaxAgency().getLiabilityAccount();

				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();

			}// while there is a transaction item

			// checking whether the Bank account is updated. If it has a parent
			// it also need to be updated
			if (c.getBankAccount().isIncrease())
				d[getAccountVariable(accounts, c.getBankAccount().getName(),
						company)] += c.getTotal();
			else
				d[getAccountVariable(accounts, c.getBankAccount().getName(),
						company)] -= c.getTotal();
		}
	}

	public void setBalanceSheetMakeDeposits(List<Account> accounts,
			Company company) throws DAOException {
		List<MakeDeposit> cps = accounterDao.getMakeDeposits(company.id);
		Iterator<MakeDeposit> i = cps.iterator();

		MakeDeposit c;
		List<TransactionMakeDeposit> ti;

		while (i.hasNext()) {
			c = (MakeDeposit) i.next();
			// Take all the transaction items that are involved in the
			// writecheck and store them in a List.
			ti = c.getTransactionMakeDeposit();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionMakeDeposit> j = ti.iterator();

			Account acc = null;
			while (j.hasNext()) {// while there is a transaction item
				TransactionMakeDeposit it = (TransactionMakeDeposit) j.next();
				if (it.getIsNewEntry()) {
					if (it.getType() == TransactionMakeDeposit.TYPE_CUSTOMER)
						acc = accounterDao.getAccount(company.id,
								"Accounts Receivable");
					else if (it.getType() == TransactionMakeDeposit.TYPE_VENDOR)
						acc = accounterDao.getAccount(company.id,
								"Accounts Payable");
					else
						acc = it.getAccount();
				} else
					acc = accounterDao.getAccount(company.id,
							"Un Deposited Funds");

				if (acc.isIncrease()
						|| acc.getName().equals("Accounts Payable"))
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getAmount();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getAmount();
			}// while there is a transaction item

			d[getAccountVariable(accounts, c.getDepositIn().getName(), company)] += c
					.getTotal();

			acc = c.getCashBackAccount();
			if (acc.isIncrease())
				d[getAccountVariable(accounts, acc.getName(), company)] -= c
						.getCashBackAmount();
			else
				d[getAccountVariable(accounts, acc.getName(), company)] += c
						.getCashBackAmount();
		}
	}

	public void setBalanceSheetReceivePayments(List<Account> accounts,
			Company company) throws DAOException {
		List<ReceivePayment> cps = accounterDao.getReceivePayments(company.id);
		Iterator<ReceivePayment> i = cps.iterator();

		while (i.hasNext()) {
			ReceivePayment r = i.next();

			if (r.getDepositIn().isIncrease())
				d[getAccountVariable(accounts, r.getDepositIn().getName(),
						company)] -= r.getAmount();
			else
				d[getAccountVariable(accounts, r.getDepositIn().getName(),
						company)] += r.getAmount();

			d[getAccountVariable(accounts,
					r.company.accountsReceivableAccount.name, company)] -= (r
					.getAmount()
					+ r.getTotalCashDiscount() + r.getTotalWriteOff());

			List<TransactionReceivePayment> trp = r
					.getTransactionReceivePayment();
			Iterator<TransactionReceivePayment> i2 = trp.iterator();
			while (i2.hasNext()) {
				TransactionReceivePayment tp = i2.next();
				if (tp.getDiscountAccount().isIncrease())
					d[getAccountVariable(accounts, tp.getDiscountAccount()
							.getName(), company)] -= tp.getCashDiscount();
				else
					d[getAccountVariable(accounts, tp.getDiscountAccount()
							.getName(), company)] += tp.getCashDiscount();

				if (tp.getWriteOffAccount().isIncrease())
					d[getAccountVariable(accounts, tp.getWriteOffAccount()
							.getName(), company)] -= tp.getWriteOff();
				else
					d[getAccountVariable(accounts, tp.getWriteOffAccount()
							.getName(), company)] += tp.getWriteOff();
			}
		}
	}

	public void setBalanceSheetVendorPayments(List<Account> accounts,
			Company company) throws DAOException {
		List<PayBill> cps = accounterDao.getPayBills(company.id);
		Iterator<PayBill> i = cps.iterator();

		while (i.hasNext()) {
			PayBill r = i.next();
			if (r.getPayFrom().isIncrease())
				d[getAccountVariable(accounts, r.getPayFrom().getName(),
						company)] += r.total;
			else
				d[getAccountVariable(accounts, r.getPayFrom().getName(),
						company)] -= r.total;

			d[getAccountVariable(accounts,
					r.company.accountsPayableAccount.name, company)] -= (r.total + ((r
					.getPayBillType() == PayBill.TYPE_PAYBILL) ? getAmountTotalDiscount(r
					.getTransactionPayBill())
					: 0.0));

			if (r.getPayBillType() == PayBill.TYPE_PAYBILL) {
				List<TransactionPayBill> trp = r.getTransactionPayBill();
				Iterator<TransactionPayBill> i2 = trp.iterator();
				while (i2.hasNext()) {
					TransactionPayBill tp = i2.next();
					if (tp.getDiscountAccount().isIncrease())
						d[getAccountVariable(accounts, tp.getDiscountAccount()
								.getName(), company)] += tp.getCashDiscount();
					else
						d[getAccountVariable(accounts, tp.getDiscountAccount()
								.getName(), company)] -= tp.getCashDiscount();
				}
			}
		}
	}

	public void setBalanceSheetCreditCardCharges(List<Account> accounts,
			Company company) throws DAOException {
		List<CreditCardCharge> cps = accounterDao
				.getCreditCardCharges(company.id);
		Iterator<CreditCardCharge> i = cps.iterator();

		CreditCardCharge c;
		List<TransactionItem> ti;
		while (i.hasNext()) {
			c = (CreditCardCharge) i.next();

			// Take all the transaction items that are involved in the cashsale
			// and store them in a List.
			ti = c.getTransactionItems();
			// Now, we need to iterate all the items and make necessary changes
			// in the corresponding accounts.
			Iterator<TransactionItem> j = ti.iterator();

			while (j.hasNext()) {// while there is a transaction item
				TransactionItem it = (TransactionItem) j.next();
				Account acc = null;
				if (it.getType() == TransactionItem.TYPE_ITEM)
					acc = it.getItem().getIncomeAccount();
				else if (it.getType() == TransactionItem.TYPE_ACCOUNT)
					acc = it.getAccount();
				else
					// if(it.getType()==TransactionItem.TYPE_SALESTAX)
					acc = it.getAccount();

				// If the isIncrease() value is true, then the account value is
				// to be increased, else decreased.
				if (acc.isIncrease())
					d[getAccountVariable(accounts, acc.getName(), company)] -= it
							.getLineTotal();
				else
					d[getAccountVariable(accounts, acc.getName(), company)] += it
							.getLineTotal();

			}// while there is a transaction item

			// checking whether the depositIn account is updated. If it has a
			// parent it also need to be updated
			if (c.getPayFrom().isIncrease())
				d[getAccountVariable(accounts, c.getPayFrom().getName(),
						company)] += c.getTotal();
			else
				d[getAccountVariable(accounts, c.getPayFrom().getName(),
						company)] -= c.getTotal();
		}
	}

	public void setBalanceSheetTransferFunds(List<Account> accounts,
			Company company) throws DAOException {
		List<TransferFund> cps = accounterDao.getTransferFunds(company.id);
		Iterator<TransferFund> i = cps.iterator();

		while (i.hasNext()) {
			TransferFund t = i.next();

			if (t.getTransferFrom().isIncrease())
				d[getAccountVariable(accounts, t.getTransferFrom().getName(),
						company)] += t.total;
			else
				d[getAccountVariable(accounts, t.getTransferFrom().getName(),
						company)] -= t.total;

			if (t.getTransferTo().isIncrease())
				d[getAccountVariable(accounts, t.getTransferTo().getName(),
						company)] -= t.total;
			else
				d[getAccountVariable(accounts, t.getTransferTo().getName(),
						company)] += t.total;
		}
	}

	public void testBalanceSheet() throws DAOException {
		if (checkBalanceSheet) {
			company = accounterDao.getCompany(1l);
			List<Account> accounts = accounterDao.getAccounts(company.id);

			List<TrialBalance> tbs = accounterReportDAOService
					.getBalanceSheetReport(company.id, dueDate(-2), dueDate(2));
			for (TrialBalance tb : tbs) {
				System.out.println("BaseType:  " + tb.getBaseType()
						+ "   SubBaseType: " + tb.getSubBaseType()
						+ "   GroupType:  " + tb.getGroupType() + "   Type: "
						+ tb.getAccountType() + "   Flow:  "
						+ tb.getAccountFlow());
			}

			setBalanceSheetAccountVariables(accounts, company.getId());

			if (createCashSales)
				setBalanceSheetCashSales(accounts, company);
			if (createInvoices)
				setBalanceSheetInvoices(accounts, company);
			if (createCustomerCreditMemos)
				setBalanceSheetCustomerCreditMemos(accounts, company);
			if (createCustomerRefunds)
				setBalanceSheetCustomerRefunds(accounts, company);
			if (createEnterBills)
				setBalanceSheetEnterBills(accounts, company);
			if (createVendorCreditMemos)
				setBalanceSheetVendorCreditMemos(accounts, company);
			if (createCashPurchases)
				setBalanceSheetCashPurchases(accounts, company);
			if (createWriteChecks)
				setBalanceSheetWriteChecks(accounts, company);
			if (createMakeDeposits)
				setBalanceSheetMakeDeposits(accounts, company);
			if (createReceivePayments)
				setBalanceSheetReceivePayments(accounts, company);
			if (createPayBills || createVendorPayments)
				setBalanceSheetVendorPayments(accounts, company);
			if (createCreditCardCharges)
				setBalanceSheetCreditCardCharges(accounts, company);
			if (createTransferFunds)
				setBalanceSheetTransferFunds(accounts, company);

			// List<TrialBalance> tbs=
			// accounterReportDAOService.
			// getBalanceSheetProfitAndLossCashFlowReport(company.id,
			// dueDate(-2), dueDate(2));
			double liabilities = 0, assets = 0;
			Iterator<TrialBalance> i = tbs.iterator();
			while (i.hasNext()) {
				TrialBalance tb = (TrialBalance) i.next();
				if (tb.getAmount() != 0.0 || tb.getAmount() != -0.0)
					assertEquals(tb.getAccountName(), tb.getAmount(),
							d[getAccountVariable(accounts, tb.getAccountName(),
									company)]);

				System.out.println("account name="
						+ tb.getAccountName()
						+ "d[] value="
						+ d[getAccountVariable(accounts, tb.getAccountName(),
								company)]);
				if (tb.getAccountName().equals("Accounts Payable")
						|| tb.getAccountName().equals("Opening Balances")
						|| accounterDao.getAccount(company.id,
								tb.getAccountId()).isIncrease()
						|| tb.getAccountType() == Account.TYPE_EXPENSE) {
					if (tb.getAccountType() == Account.TYPE_EXPENSE)
						liabilities -= d[getAccountVariable(accounts, tb
								.getAccountName(), company)];
					else
						liabilities += d[getAccountVariable(accounts, tb
								.getAccountName(), company)];
				} else
					liabilities -= d[getAccountVariable(accounts, tb
							.getAccountName(), company)];
			}
			System.out.println("assets=" + assets + "liabilities="
					+ liabilities);
			assertEquals(0.0, liabilities);
		}
	}

	public void testSetAllCashSales(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<CashSales> ccms = (ArrayList<CashSales>) accounterDao
				.getCashSales(company.id);
		Iterator<CashSales> itr = ccms.iterator();
		while (itr.hasNext()) {
			CashSales c = (CashSales) itr.next();

			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getCustomer().getName());
			i.setBalance(c.getCustomer().getBalance());
			i.setDiscount(0.0);
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setWriteOff(0.0);
			i.setStatus(c.getStatus());
			i.setDate(c.getDate());
			itemDetails.add(i);

		}
	}

	public void testSetAllInvoices(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<Invoice> ccms = (ArrayList<Invoice>) accounterDao
				.getInvoices(company.id);
		Iterator<Invoice> itr = ccms.iterator();
		while (itr.hasNext()) {
			Invoice c = (Invoice) itr.next();

			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getCustomer().getName());
			i.setBalance(c.getCustomer().getBalance());
			i.setDiscount(0.0);
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setWriteOff(0.0);
			i.setStatus(c.getStatus());
			i.setDate(c.getDate());
			itemDetails.add(i);
		}
	}

	public void testSetAllCustomerCreditMemos(ArrayList<ReportList> itemDetails)
			throws DAOException {

		company = accounterDao.getCompany(1l);
		ArrayList<CustomerCreditMemo> ccms = (ArrayList<CustomerCreditMemo>) accounterDao
				.getCustomerCreditMemos(company.id);
		Iterator<CustomerCreditMemo> itr = ccms.iterator();
		while (itr.hasNext()) {
			CustomerCreditMemo c = (CustomerCreditMemo) itr.next();
			ReportList i = new ReportList();
			i.setTransactionId(c.getId());
			i.setName(c.getCustomer().getName());
			i.setBalance(c.getCustomer().getBalance());
			i.setDiscount(0.0);
			i.setAmount(c.getTotal());
			i.setFlag(true);
			i.setTransactionType(c.getType());
			i.setWriteOff(0.0);
			i.setStatus(c.getStatus());
			i.setDate(c.getDate());
			itemDetails.add(i);

		}
	}

	public void testSetAllReceivePayments(ArrayList<ReportList> sd)
			throws DAOException {
		List<ReceivePayment> rps = accounterDao.getReceivePayments(company.id);
		Iterator<ReceivePayment> itr = rps.iterator();
		while (itr.hasNext()) {
			ReceivePayment rp = (ReceivePayment) itr.next();
			ReportList i = new ReportList();
			i.setAmount(rp.getAmount());
			i.setDiscount(rp.getTotalCashDiscount());
			i.setWriteOff(rp.getTotalWriteOff());
			i.setStatus(rp.getStatus());
			i.setTransactionType(rp.getType());
			i.setNumber(rp.getNumber());
			i.setTransactionId(rp.getId());
			i.setDiscount(rp.getTotalCashDiscount());
			i.setName(rp.getCustomer().getName());
			i.setFlag(true);
			i.setBalance(rp.getCustomerBalance());
			sd.add(i);
		}
	}

	public void testSetAllCustomerRefunds(ArrayList<ReportList> sd)
			throws DAOException {
		List<CustomerRefund> rps = accounterDao.getCustomerRefunds(company.id);
		Iterator<CustomerRefund> itr = rps.iterator();
		while (itr.hasNext()) {
			CustomerRefund rp = (CustomerRefund) itr.next();

			ReportList i = new ReportList();
			if (rp.getType() == Transaction.TYPE_CUSTOMER_REFUNDS) {
				i.setName(rp.getPayee().name);
				i.setDate(rp.getDate());
				i.setStatus(rp.getStatus());
				i.setAmount(rp.total);
				i.setTransactionType(rp.getType());
				i.setTransactionId(rp.id);
				i.setFlag(true);
				sd.add(i);
			}
		}
	}

	public void testCustomerTransactionHistory() throws DAOException {
		if (checkCustomerTransactionHistory) {
			company = accounterDao.getCompany(1l);
			// setAllCustomerHistoryValues();
			ArrayList<ReportList> itemDetails = new ArrayList<ReportList>();

			// itemDetails.add()
			testSetAllCashSales(itemDetails);
			testSetAllInvoices(itemDetails);
			testSetAllCustomerCreditMemos(itemDetails);
			testSetAllCustomerRefunds(itemDetails);
			testSetAllCustomerWriteChecks(itemDetails);
			testSetAllCustomerDeposits(itemDetails);
			testSetAllReceivePayments(itemDetails);

			// List <TransactionHistory>
			//ctl=accounterReportDAOService.getCustomerTransactionHistory(company
			// ,
			// dueDate(-3), dueDate(1));

			List<TransactionHistory> ctl = accounterReportDAOService
					.getCustomerTransactionHistory(company.id, "2009-01-01",
							"2009-09-11");
			Iterator<TransactionHistory> i = ctl.iterator();
			assertEquals(ctl.size(), itemDetails.size());
			while (i.hasNext()) {
				TransactionHistory th = i.next();
				Iterator<ReportList> j = itemDetails.iterator();
				while (j.hasNext()) {
					ReportList sl = (ReportList) j.next();
					if (sl.getFlag()
							&& (sl.getAmount() == (th.getInvoicedAmount() == 0.0 ? th
									.getType() == Transaction.TYPE_CUSTOMER_REFUNDS ? -th
									.getPaidAmount()
									: th.getPaidAmount()
									: ((th.getType() == Transaction.TYPE_CUSTOMER_CREDIT_MEMO
											|| th.getType() == Transaction.TYPE_WRITE_CHECK || th
											.getType() == Transaction.TYPE_MAKE_DEPOSIT) ? -th
											.getInvoicedAmount()
											: th.getInvoicedAmount())))) {
						if (th.getName().equals(sl.getName()))
							;
						{

							if (th.getType() == sl.getTransactionType()) {
								if (th.getName().equals("Customer3"))
									System.out.println("Here");
								if (sl.getTransactionType() == Transaction.TYPE_CASH_SALES
										|| sl.getTransactionType() == Transaction.TYPE_INVOICE
										|| sl.getTransactionType() == Transaction.TYPE_CUSTOMER_REFUNDS
										|| sl.getTransactionType() == Transaction.TYPE_WRITE_CHECK) {
									assertEquals(th.getDebit(), sl.getAmount());// sl
																				// .
																				// getType
																				// (
																				// )
																				// ==
																				// Transaction
																				// .
																				// TYPE_CUSTOMER_REFUNDS
																				// ?
																				// (
																				// -
																				// (
																				// sl
																				// .
																				// getAmount
																				// (
																				// )
																				// )
																				// )
																				// :
									// sl.getAmount());
									assertTrue((sl.getTransactionType() == Transaction.TYPE_CASH_SALES || sl
											.getTransactionType() == Transaction.TYPE_WRITE_CHECK) ? th
											.getDebit() == th.getCredit()
											: true);
								} else
									assertEquals(th.getName(), th.getCredit(),
											sl.getAmount() + sl.getDiscount()
													+ sl.getWriteOff());// sl.
																		// getType
																		// ()==
																		// Transaction
																		// .
																		// TYPE_CUSTOMER_CREDIT_MEMO
																		// ?
																		// (-(sl
																		// .
																		// getAmount
																		// ())):
								// sl.getAmount());
								System.out.println("type ="
										+ sl.getTransactionType() + " "
										+ th.getType() + "/ id="
										+ th.getTransactionId());
								sl.setFlag(false);
								break;

							}
						}

					}
					if (!j.hasNext()) {
						System.out.println(" th customer =" + th.getName()
								+ " th type =" + th.getType());
						assertNotNull("All customers are not included here",
								null);
					}
				}
			}
			Iterator<ReportList> temp = itemDetails.iterator();
			while (temp.hasNext())
				temp.next().setFlag(true);
		}
	}

	public void testVendorTransactionHistory() throws DAOException {
		if (checkVendorTransactionHistory) {
			company = accounterDao.getCompany(1l);
			ArrayList<ReportList> itemDetails = new ArrayList<ReportList>();

			testSetAllEnterBills(itemDetails);
			testSetAllVendorCreditMemos(itemDetails);
			testSetAllCashPurchases(itemDetails);
			testSetAllVendorWriteChecks(itemDetails);
			testSetAllVendorDeposits(itemDetails);
			testSetAllPayBills(itemDetails);
			testSetAllCreditCardCharges(itemDetails);

			List<TransactionHistory> ctl = accounterReportDAOService
					.getVendorTransactionHistory(company.id, dueDate(-3),
							today());
			Iterator<TransactionHistory> i = ctl.iterator();
			assertEquals(ctl.size(), itemDetails.size());
			while (i.hasNext()) {
				TransactionHistory th = i.next();
				Iterator<ReportList> j = itemDetails.iterator();
				while (j.hasNext()) {
					ReportList sl = (ReportList) j.next();
					if (sl.getFlag()
							&& (sl.getAmount() == (th.getInvoicedAmount() == 0.0 ? th
									.getPaidAmount()
									: (th.getType() == Transaction.TYPE_VENDOR_CREDIT_MEMO ? -th
											.getInvoicedAmount()
											: th.getInvoicedAmount())))) {// th.
																			// getType
																			// (
																			// )
																			// .
																			// equals
																			// (
																			// Utility
																			// .
																			// getTransactionName
																			// (
																			// Transaction
																			// .
																			// TYPE_VENDOR_CREDIT_MEMO
																			// )
																			// )
																			// ?
																			// -
																			// th
																			// .
																			// getPaidAmount
																			// (
																			// )
																			// :

						if (th.getName().equals(sl.getName()))
							;
						{
							if (th.getType() == sl.getTransactionType()) {
								if (sl.getTransactionType() == Transaction.TYPE_CASH_PURCHASE
										|| sl.getTransactionType() == Transaction.TYPE_VENDOR_CREDIT_MEMO
										|| sl.getTransactionType() == Transaction.TYPE_CREDIT_CARD_CHARGE
										|| sl.getTransactionType() == Transaction.TYPE_WRITE_CHECK
										|| sl.getTransactionType() == Transaction.TYPE_PAY_BILL) {
									assertEquals(th.getDebit(), sl.getAmount()
											+ sl.getDiscount());//sl.getType()==
																// Transaction.
																// TYPE_CUSTOMER_REFUNDS
																// ?(-(sl.
																// getAmount
																// ())):
									// sl.getAmount());
									assertTrue((sl.getTransactionType() == Transaction.TYPE_CASH_PURCHASE
											|| sl.getTransactionType() == Transaction.TYPE_WRITE_CHECK || sl
											.getTransactionType() == Transaction.TYPE_CREDIT_CARD_CHARGE) ? th
											.getDebit() == th.getCredit()
											: true);
								} else
									assertEquals(th.getCredit(), sl.getAmount()
											+ sl.getDiscount());//sl.getType()==
																// Transaction.
																// TYPE_CUSTOMER_CREDIT_MEMO
																// ?(-(sl.
																// getAmount
																// ())):
								// sl.getAmount());
								sl.setFlag(false);
								break;
							}
						}
					}
					if (!j.hasNext())
						assertNotNull("All customers are not included here",
								null);
				}
			}
			Iterator<ReportList> temp = itemDetails.iterator();
			while (temp.hasNext()) {
				temp.next().setFlag(true);
			}
		}
	}
}
