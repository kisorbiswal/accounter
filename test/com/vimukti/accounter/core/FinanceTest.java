package com.vimukti.accounter.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.Query;
import org.hibernate.Session;

import com.bizantra.server.utils.SecureUtils;
import com.vimukti.accounter.services.AccounterGUIDAOService;
import com.vimukti.accounter.services.AccounterReportDAOService;
import com.vimukti.accounter.services.AccounterService;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.services.IAccounterGUIDAOService;
import com.vimukti.accounter.services.IAccounterReportDAOService;
import com.vimukti.accounter.services.IAccounterService;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.core.reports.VATDetailReport;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATSummary;

public class FinanceTest extends TestCase {

	private IAccounterService accounter = null;
	IAccounterGUIDAOService accounterGUIDao;
	IAccounterReportDAOService accounterReportDAOService;
	Company company = null;

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	boolean createCustomers = true, checkTesting = false, createVendors = true;

	@Override
	protected void setUp() throws Exception {

		// ====================================
		super.setUp();
		init();
		// =====================================

	}

	private void init() {
		Utility.openSession();
		accounter = new AccounterService();
		accounterGUIDao = new AccounterGUIDAOService();
		accounterReportDAOService = new AccounterReportDAOService();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
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
		int y = (m > 12 ? 1 : (m < 1 ? -1 : 0));

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

	public void testQuery() throws Exception {

		Session session = Utility.getCurrentSession();
		Query query = session
				.createQuery("from com.vimukti.accounter.core.Transaction where type in (4,5,8,10)");

		List<Transaction> list = query.list();

		for (int i = 0; i < list.size(); i++) {
			System.out.println((JournalEntry) list.get(i));
		}

	}

	public void testSample() throws Exception {

		Company company = new Company(Company.ACCOUNTING_TYPE_UK, Utility
				.getCurrentSession());
		company.setName("Company1");
		company.setID(SecureUtils.createID());
		// session.save(company);
		accounter.createObject(company);
		Company cp = accounter.getObjectById(Company.class, company
				.getID());
		System.out.println(cp.getID());
		// Account account = accounterService.getObjectByName(Account.class,
		// AccounterConstants.PENDING_ITEM_RECEIPTS);
		// account.setOpeningBalance(1000);
		// account.setNumber("2222222222222");
		// company.setName("Vimukti");
		// accounterService.updateObject(company);
		// Account account = accounterService.getObjectByName(Account.class,
		// AccounterConstants.SALES_TAX_PAYABLE);
		// accounterService.deleteObject(account);
		// System.out.println("HI");

		// Account acc= new Account();
		// acc.setName("Account Cash");
		// acc.setType(Account.TYPE_CASH);
		// acc.setOpeningBalance(10000d);
		// accounterService.createObject(acc);
		//
		// Customer customer = new Customer();
		// customer.setType(Payee.TYPE_CUSTOMER);
		// customer.setName("Customer1");
		// customer.setOpeningBalance(20000);
		// customer.paymentTerm = accounterService.getObjectByName(
		// PaymentTerms.class, "Monthly");
		// accounterService.createObject(customer);

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
		customer.setEmails(em);
		customer.setAddress(a);
		customer.setContacts(c);

		try {
			// customer.setPayeeSince(format.parse(since));
			// customer.setBalanceAsOf(format.parse(asof));

			customer.setPayeeSince(format.parse(dueDate(0)));
			customer.setBalanceAsOf(format.parse(dueDate(0)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		customer.setOpeningBalance(bal);
		accounter.createObject(customer);
		return customer;
	}

	public void testCreateCustomers() throws DAOException {
		accounterReportDAOService
				.createTaxes(VATAgency.RETURN_TYPE_IRELAND_VAT);

		if (createCustomers) {

			company = accounter.getObjectByName(Company.class, "Company1");

			double amt1 = 0.0, amt2 = 0.0;
			double OBCurrentBalance = ((Account) accounter.getObjectByName(
					Account.class, AccounterConstants.OPENING_BALANCE))
					.getCurrentBalance(), OBTotalBalance = ((Account) accounter
					.getObjectByName(Account.class,
							AccounterConstants.OPENING_BALANCE))
					.getTotalBalance();

			ArrayList<Customer> custs = new ArrayList<Customer>();
			try {

				// amt1 = accounterDao.getAccount(company.getID(),
				// "Accounts Receivable").getOpeningBalance();

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

			// if (checkTesting) {
			// // //Testing starts from here
			// amt2 = 0.0;
			//
			// List<IAccounterServerCore> l = (List<IAccounterServerCore>)(
			// accounter.getObjects(Customer.class));
			// ArrayList<Customer> l2 = custs;
			// assertNotNull(l);
			// assertNotNull(l2);
			// Iterator<IAccounterServerCore> i = l.iterator();
			// Iterator j = l2.iterator();
			//
			// while (i.hasNext() && j.hasNext()) {
			// Customer cg = (Customer) i.next();// This returns the
			// // customer from
			// // starting of the customer list
			// Customer c1 = (Customer) j.next();
			// // check every field in the customer object whether it is
			// // saved correctly in the database check name
			// assertNotNull(cg.getName());
			// assertNotNull(c1.getName());
			// assertEquals(c1.getName(), cg.getName());
			// // check File As
			// assertNotNull(cg.getFileAs());
			// assertNotNull(c1.getFileAs());
			// assertEquals(c1.getFileAs(), cg.getFileAs());
			// // check Addresses as address field consists of several entries,
			// we need to
			// // take an iterator to iterate aLL the values
			// assertNotNull(cg.getAddress());
			// assertNotNull(c1.getAddress());
			//
			// Iterator i2 = cg.getAddress().iterator();
			// Iterator j2 = c1.getAddress().iterator();
			//
			// while (i2.hasNext())// &&j2.hasNext())
			// {
			// Address a = (Address) i2.next();
			// Address b = a;
			// while (j2.hasNext()) {
			// b = (Address) j2.next();
			// assertNotNull(b);
			// assertNotNull(a.getCity());
			// assertNotNull(b.getCity());
			// if (a.getCity() == b.getCity()) {
			// // check the city
			// assertEquals(a.getCity(), b.getCity());
			// // check street
			// assertNotNull(a.getStreet());
			// assertNotNull(b.getStreet());
			// assertEquals(a.getStreet(), b.getStreet());
			// // check country
			// assertNotNull(a.getCountryOrRegion());
			// assertNotNull(b.getCountryOrRegion());
			// assertEquals(a.getCountryOrRegion(), b
			// .getCountryOrRegion());
			// // check state
			// assertNotNull(a.getStateOrProvinence());
			// assertNotNull(b.getStateOrProvinence());
			// assertEquals(a.getStateOrProvinence(), b
			// .getStateOrProvinence());
			// break;
			// }
			// }
			// if (!i2.hasNext())
			// assertEquals(a.getCity(), b.getCity());
			// }
			//
			// assertNotNull(cg.getEmails());
			// assertNotNull(c1.getEmails());
			//
			// Iterator i3 = cg.getEmails().iterator();
			// Iterator j3 = cg.getEmails().iterator();
			//
			// while (i3.hasNext())// &&j3.hasNext())
			// {
			// // checking Mails
			// Email a = (Email) i3.next();
			// Email b = a;
			//
			// while (j3.hasNext()) {
			// b = (Email) j3.next();
			// assertNotNull(b);
			// assertNotNull(a.getEmail());
			// assertNotNull(b.getEmail());
			// // check the email one by one
			// if (a.getEmail() == b.getEmail()) {
			// assertEquals(a.getEmail(), b.getEmail());
			// break;
			// }
			// }
			// if (!i3.hasNext())
			// assertEquals(a.getEmail(), b.getEmail());
			// }
			//
			// assertEquals(cg.getPayeeSince(), c1.getPayeeSince());
			// assertEquals(cg.getBalanceAsOf(), c1.getBalanceAsOf());
			// assertEquals(cg.getBalance(), c1.getBalance());
			// assertEquals(cg.getOpeningBalance(), c1.getBalance());
			// assertEquals(cg.getBalance(), c1.getOpeningBalance());
			// assertEquals(cg.getOpeningBalance(), c1.getOpeningBalance());
			//
			// amt2 += cg.getBalance();
			//
			// assertNotNull(cg.getContacts());
			// assertNotNull(c1.getContacts());
			//
			// Iterator i4 = cg.getContacts().iterator();
			// Iterator j4 = cg.getContacts().iterator();
			//
			// while (i4.hasNext() && j4.hasNext()) {
			// Contact a = (Contact) i4.next();
			// Contact b = a;
			// // checking Contacts
			// while (j4.hasNext()) {
			// b = (Contact) j4.next();
			// assertNotNull(b);
			// assertNotNull(a.getTitle());
			// assertNotNull(b.getTitle());
			// // checking contacts one by one
			// if (a.getTitle() == b.getTitle()) {
			// // check the title in the contacts
			// assertEquals(a.getTitle(), b.getTitle());
			// // checking email in the contacts
			// assertNotNull(a.getEmail());
			// assertNotNull(b.getEmail());
			//
			// assertEquals(a.getEmail(), b.getEmail());
			// break;
			// }
			// }
			// if (!i4.hasNext())
			// assertEquals(a.getEmail(), b.getEmail());
			// }
			// // Now checking the AccountsReceivable accounts
			// }
			// assertNotNull(amt1);
			// assertNotNull(amt2);
			//
			// Account acc = accounter.getObjectByName(Account.class,
			// "Accounts Receivable");
			// assertEquals(AccounterConstants.ACCOUNTS_RECEIVABLE,
			// (amt1 + amt2), acc.getCurrentBalance());
			// assertEquals(AccounterConstants.ACCOUNTS_RECEIVABLE,
			// (amt1 + amt2), acc.getTotalBalance());
			//
			// acc = accounter.getObjectByName(Account.class,
			// AccounterConstants.OPENING_BALANCE);
			// assertEquals(AccounterConstants.OPENING_BALANCE,
			// (OBCurrentBalance + amt1 + amt2), acc
			// .getCurrentBalance());
			// assertEquals(AccounterConstants.OPENING_BALANCE,
			// (OBTotalBalance + amt1 + amt2), acc.getTotalBalance());
			// }
		}
		// try {
		// Customer cust = accounterService.getObjectByName(Customer.class,
		// "Customer1");
		// cust.paymentTerm = accounterService.getObjectByName(
		// PaymentTerms.class, "Quarterly");
		//
		// accounterService.updateObject(cust);
		// } catch (DAOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
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
			// v.setPayeeSince(format.parse(since));
			// v.setBalanceAsOf(format.parse(asof));

			v.setPayeeSince(format.parse(dueDate(0)));
			v.setBalanceAsOf(format.parse(dueDate(0)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		v.setOpeningBalance(bal);
		accounter.createObject(v);
		return v;
	}

	public void testCreateVendor() throws DAOException {

		if (createVendors) {
			double amt1 = 0.0, amt2 = 0.0;
			double OBCurrentBalance = ((Account) accounter.getObjectByName(
					Account.class, AccounterConstants.OPENING_BALANCE))
					.getCurrentBalance(), OBTotalBalance = ((Account) accounter
					.getObjectByName(Account.class,
							AccounterConstants.OPENING_BALANCE))
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

				List<Vendor> l = accounter.getObjects(Vendor.class);
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

				// Account acc = accounterDao.getAccount(company.getID(),
				// "Accounts Payable");
				// assertEquals(AccounterConstants.ACCOUNTS_PAYABLE,
				// (amt1 + amt2), acc.getCurrentBalance());
				// assertEquals(AccounterConstants.ACCOUNTS_PAYABLE,
				// (amt1 + amt2), acc.getTotalBalance());
				//
				// acc = accounterDao.getAccount(company.getID(),
				// AccounterConstants.OPENING_BALANCE);
				// assertEquals(AccounterConstants.OPENING_BALANCE,
				// (OBCurrentBalance - amt1 - amt2), acc
				// .getCurrentBalance());
				// assertEquals(AccounterConstants.OPENING_BALANCE,
				// (OBTotalBalance - amt1 - amt2), acc.getTotalBalance());

			}
		}
	}

	public TransactionItem testAddTransactionItem(int type, String itemName,
			double unitPrice, double quantity, boolean taxable, String vatCode)
			throws DAOException {

		TransactionItem ti = new TransactionItem();
		if (quantity == 0)
			quantity++;

		ti.setTaxable(taxable);
		ti.setType(type);
		if (vatCode != null) {
			ti.setVatCode(((VATCode) accounter.getObjectByName(VATCode.class,
					vatCode)));
		}
		if (type == TransactionItem.TYPE_ACCOUNT) {
			ti.setAccount((Account) accounter.getObjectByName(Account.class,
					itemName));
			ti.setUnitPrice(unitPrice);
			ti.setQuantity(1);
			ti.setLineTotal(1 * unitPrice);
		} else if (type == TransactionItem.TYPE_ITEM) {
			ti.setItem((Item) accounter.getObjectByName(Item.class, itemName));
			ti.setUnitPrice(unitPrice);
			ti.setQuantity(quantity);
			ti.setLineTotal(quantity * unitPrice);
		}
		// else if (type == TransactionItem.TYPE_SALESTAX) {
		// ti.setTaxCode((TaxCode) accounter.getObjectByName(TaxCode.class,
		// itemName));
		// ti.setLineTotal(unitPrice);
		// }

		return ti;
	}

	public void testQuotes() throws DAOException {

		Estimate e1 = new Estimate();
		e1.setCustomer((Customer) accounter.getObjectByName(Customer.class,
				"Customer1"));
		e1.setType(Transaction.TYPE_ESTIMATE);

		List<TransactionItem> tis = new ArrayList<TransactionItem>();
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.BANK_CHARGE)).getName(), 2000d, 1d,
				false, null));
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.UN_DEPOSITED_FUNDS)).getName(),
				3000d, 1d, false, null));
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.RETAINED_EARNINGS)).getName(),
				4000d, 1d, false, null));

		e1.setTransactionItems(tis);
		// accounter.createObject(e1);

		Estimate e2 = new Estimate();
		e2.setCustomer((Customer) accounter.getObjectByName(Customer.class,
				"Customer2"));
		e2.setType(Transaction.TYPE_ESTIMATE);

		List<TransactionItem> tis2 = new ArrayList<TransactionItem>();
		tis2.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.WRITE_OFF)).getName(), 2000d, 1d,
				false, null));
		tis2.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.UN_DEPOSITED_FUNDS)).getName(),
				3000d, 1d, false, null));

		e2.setTransactionItems(tis2);
		// accounter.createObject(e2);

		if (checkTesting) {
			List<Estimate> l = accounter.getObjects(Estimate.class);

			Iterator<Estimate> i = l.iterator();
			while (i.hasNext()) {
				Estimate e = i.next();
				assertEquals("Estimate No " + e.getID() + "'s status", e
						.getStatus(),
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED);
			}
		}
	}

	public void testSalesOrder() throws DAOException {

		SalesOrder e1 = new SalesOrder();
		e1.setCustomer((Customer) accounter.getObjectByName(Customer.class,
				"Customer1"));
		e1.setType(Transaction.TYPE_SALES_ORDER);

		List<TransactionItem> tis = new ArrayList<TransactionItem>();
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.BANK_CHARGE)).getName(), 4000d, 1d,
				false, null));
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.UN_DEPOSITED_FUNDS)).getName(),
				5000d, 1d, false, null));
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.RETAINED_EARNINGS)).getName(),
				6000d, 1d, false, null));

		e1.setTransactionItems(tis);
		accounter.createObject(e1);

		SalesOrder e2 = new SalesOrder();
		e2.setCustomer((Customer) accounter.getObjectByName(Customer.class,
				"Customer2"));
		e2.setType(Transaction.TYPE_SALES_ORDER);

		List<TransactionItem> tis2 = new ArrayList<TransactionItem>();
		tis2.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.WRITE_OFF)).getName(), 7000d, 1d,
				false, null));
		tis2.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.UN_DEPOSITED_FUNDS)).getName(),
				8000d, 1d, false, null));

		e2.setTransactionItems(tis2);
		// accounter.createObject(e2);

		// if(checkTesting)
		// {
		// List<SalesOrder> l=accounter.getObjects(SalesOrder.class);
		// Iterator<SalesOrder> i=l.iterator();
		// while(i.hasNext())
		// {
		// SalesOrder e=i.next();
		// assertEquals("SalesOrder No "+ e.getID()+"'s status", e.getStatus(),
		// Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED);
		// }
		// }
		// /////////////////////////////////////////////////////
		// ////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////

		// SalesOrder s= new SalesOrder();
		Customer c1 = accounter.getObjectByName(Customer.class, "Customer1");
		List<EstimatesAndSalesOrdersList> sl = accounterGUIDao
				.getEstimatesAndSalesOrdersList(c1.getID());
		System.out.println("sl size=" + sl.size());
		List<SalesOrdersList> sol = accounterGUIDao.getSalesOrdersList(c1
				.getID());
		System.out.println("SalesOrders list size=" + sol.size());

		Vendor v1 = accounter.getObjectByName(Vendor.class, "Vendor1");

		List<PurchaseOrdersAndItemReceiptsList> poil = accounterGUIDao
				.getPurchasesAndItemReceiptsList(v1.getID());
		System.out.println("PurchaseOrdersAndItemReceiptsList size="
				+ poil.size());

		List<PurchaseOrdersList> pol = accounterGUIDao.getPurchaseOrdersList(v1
				.getID());
		System.out.println("PurchaseOrdersList size=" + pol.size());

		List<PurchaseOrdersList> pol2 = accounterGUIDao
				.getNotReceivedPurchaseOrdersList(v1.getID());
		System.out.println("NotReceivedPurchaseOrdersList size=" + pol2.size());

	}

	public double getAmountSalesTax(Company company, String tg,
			List<TransactionItem> ti) throws DAOException {
		TaxGroup tg1 = accounter.getObjectByName(TaxGroup.class, tg);
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

	public double getAmountAllLineTotal(List<TransactionItem> ti) {
		double total = 0.0;
		Iterator<TransactionItem> i = ti.iterator();

		while (i.hasNext()) {
			TransactionItem tr = (TransactionItem) i.next();
			total += tr.getLineTotal();
		}

		return total;
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

	public Invoice testCallCreateInvoice(String d, long cm, String cu,
			String sp, String tg, List<TransactionItem> ti, String dd,
			double p, Invoice inv) throws Exception {

		Customer customer = accounter.getObjectByName(Customer.class, cu);

		inv.setNumber(accounterGUIDao
				.getNextTransactionNumber(Transaction.TYPE_INVOICE));
		inv.setCustomer(customer);
		inv.setType(Transaction.TYPE_INVOICE);
		TaxGroup t = accounter.getObjectByName(TaxGroup.class, tg);
		for (int i = 0; i < ti.size(); i++) {
			if (ti.get(i).isTaxable)
				ti.get(i).taxGroup = t;
		}
		inv.setDate(format.parse(d));
		inv.setSalesPerson(((SalesPerson) accounter.getObjectByName(
				SalesPerson.class, sp)));
		inv.setTransactionItems(ti);
		inv.setSalesTaxAmount(getAmountSalesTax(Company.getCompany(), tg, ti));
		inv.setDueDate(format.parse(dd));
		inv.setAllLineTotal(getAmountAllLineTotal(ti));
		inv.setDiscountTotal(getAmountDiscountTotal(ti));
		inv
				.setBalanceDue((inv.getAllLineTotal() + inv.getSalesTaxAmount())
						- p);
		inv.setTotal(inv.getAllLineTotal() + inv.getSalesTaxAmount());
		inv.setPayments(p);
		// inv.setAllTaxableLineTotal(getAmountAllTaxableLineTotal(ti));
		double customerBalance = customer.getBalance();

		accounter.createObject(inv);
		if (checkTesting) {
			customer = accounter.getObjectById(Customer.class, inv
					.getCustomer().getID());
			assertEquals(customerBalance + inv.getTotal(), customer
					.getBalance());
		}
		return inv;
	}

	public void testInvoices() throws Exception {

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

		// transactionItems1.add(testCallCreateTransactionItem(company.getName
		// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true, //
		// "Retained Earnings", invoice1));
		// transactionItems2.add(testCallCreateTransactionItem(company.getName
		// (), TransactionItem.TYPE_ACCOUNT, 1D, 1000D, true, //
		// "Employee Payroll Liabilities", invoice2));

		// if (invoice1 != null) {
		// for (int n = 0; n < (items.size() / 2); n++) {
		// Item item = nextItem(its);
		// transactionItems1.add(testCallCreateTransactionItem(company
		// .getName(), TransactionItem.TYPE_ITEM, 1D, item
		// .getSalesPrice(), 0D, true, item.getName(),
		// invoice1));
		// }
		// }
		//
		// if (invoice2 != null) {
		// for (int n = (items.size() / 2) + 1; n <= items.size(); n++) {
		// Item item = nextItem(its);
		// transactionItems2.add(testCallCreateTransactionItem(company
		// .getName(), TransactionItem.TYPE_ITEM, 1D, item
		// .getSalesPrice(), 0D, true, item.getName(),
		// invoice2));
		// }
		// }
		//
		// if (invoice3 != null) {
		// for (int n = 0; n < (accounts.size() / 2); n++)
		// transactionItems3.add(testCallCreateTransactionItem(company
		// .getName(), TransactionItem.TYPE_ACCOUNT, 1D,
		// 1000D, true, nextAccount(acs).getName(), invoice3));
		// }
		//
		// if (invoice4 != null) {
		// for (int n = (accounts.size() / 2) + 1; n <= accounts.size(); n++)
		// transactionItems4.add(testCallCreateTransactionItem(company
		// .getName(), TransactionItem.TYPE_ACCOUNT, 1D, 100D,
		// true, nextAccount(acs).getName(), invoice4));
		// }
		//
		// if (invoice5 != null) {
		// for (int n = 0; n < (codes.size() * 5); n++)
		// transactionItems5.add(testCallCreateTransactionItem(company
		// .getName(), TransactionItem.TYPE_SALESTAX, 1000D,
		// nextTaxCode(tcs).getName(), invoice5));
		// }
		//
		// if (createMore) {
		// for (int n = 0; n < ((items.size() + accounts.size() + codes
		// .size()) / 4); n += 3) {
		// if (items.size() > 0) {
		// Item item = items.get(new Random()
		// .nextInt(items.size()));
		// if (invoice6 != null)
		// transactionItems6
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ITEM, 1D, item
		// .getSalesPrice(), 0D, true,
		// item.getName(), invoice6));
		//
		// item = items.get(new Random().nextInt(items.size()));
		// if (invoice7 != null)
		// transactionItems7
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ITEM, 1D, item
		// .getSalesPrice(), 0D, true,
		// item.getName(), invoice7));
		//
		// item = items.get(new Random().nextInt(items.size()));
		// if (invoice8 != null)
		// transactionItems8
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ITEM, 1D, item
		// .getSalesPrice(), 0D, true,
		// item.getName(), invoice8));
		//
		// item = items.get(new Random().nextInt(items.size()));
		// if (invoice9 != null)
		// transactionItems9
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ITEM, 1D, item
		// .getSalesPrice(), 0D, true,
		// item.getName(), invoice9));
		//
		// item = items.get(new Random().nextInt(items.size()));
		// if (invoice10 != null)
		// transactionItems10
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ITEM, 1D, item
		// .getSalesPrice(), 0D, true,
		// item.getName(), invoice10));
		// }
		//
		// if (accounts.size() > 0) {
		// Account account = getRandomAccount(accounts);
		// if (invoice6 != null)
		// transactionItems6
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ACCOUNT, 1D,
		// 1000D, true, account.getName(),
		// invoice6));
		//
		// account = getRandomAccount(accounts);
		// if (invoice7 != null)
		// transactionItems7
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ACCOUNT, 1D,
		// 1000D, true, account.getName(),
		// invoice7));
		//
		// account = getRandomAccount(accounts);
		// if (invoice8 != null)
		// transactionItems8
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ACCOUNT, 1D,
		// 1000D, true, account.getName(),
		// invoice8));
		//
		// account = getRandomAccount(accounts);
		// if (invoice9 != null)
		// transactionItems9
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ACCOUNT, 1D,
		// 1000D, true, account.getName(),
		// invoice9));
		//
		// account = getRandomAccount(accounts);
		// if (invoice10 != null)
		// transactionItems10
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_ACCOUNT, 1D,
		// 1000D, true, account.getName(),
		// invoice10));
		// }
		//
		// if (codes.size() > 0) {
		// TaxCode tc = codes.get(new Random().nextInt(codes
		// .size()));
		// if (invoice6 != null)
		// transactionItems6
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_SALESTAX,
		// 1000D, tc.getName(), invoice6));
		//
		// tc = codes.get(new Random().nextInt(codes.size()));
		// if (invoice7 != null)
		// transactionItems7
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_SALESTAX,
		// 1000D, tc.getName(), invoice7));
		//
		// tc = codes.get(new Random().nextInt(codes.size()));
		// if (invoice8 != null)
		// transactionItems8
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_SALESTAX,
		// 1000D, tc.getName(), invoice8));
		//
		// tc = codes.get(new Random().nextInt(codes.size()));
		// if (invoice9 != null)
		// transactionItems9
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_SALESTAX,
		// 1000D, tc.getName(), invoice9));
		//
		// tc = codes.get(new Random().nextInt(codes.size()));
		// if (invoice10 != null)
		// transactionItems10
		// .add(testCallCreateTransactionItem(company
		// .getName(),
		// TransactionItem.TYPE_SALESTAX,
		// 1000D, tc.getName(), invoice10));
		// }
		// }
		// }

		List<TransactionItem> tis = new ArrayList<TransactionItem>();
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.BANK_CHARGE)).getName(), 2000d, 1d,
				false, "S"));
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.UN_DEPOSITED_FUNDS)).getName(),
				3000d, 1d, false, "R"));
		tis.add(testAddTransactionItem(TransactionItem.TYPE_ACCOUNT,
				((Account) accounter.getObjectByName(Account.class,
						AccounterConstants.RETAINED_EARNINGS)).getName(),
				4000d, 1d, false, "S"));
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

		if (tis.size() > 0)
			Invoices.add(testCallCreateInvoice(today(), 1l, "Customer1",
					"SalesPerson1", "TG12", tis, dueDate(1), 0d, invoice1));

		// if (transactionItems2.size() > 0)
		// Invoices.add(testCallCreateInvoice(today(), 1l, "Customer2",
		// "SalesPerson1", "TG23", transactionItems2, dueDate(2),
		// 0d, invoice2));
		//
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

	public void testVATReturn() throws DAOException, ParseException {

		List<VATAgency> vatAgencis = accounter.getObjects(VATAgency.class);

		VATAgency vatAgency = vatAgencis.get(0);

		VATReturn vatReturn = accounterGUIDao.getVATReturn(vatAgency, format
				.parse(dueDate(-1)), format.parse(dueDate(1)));

		accounter.createObject(vatReturn);

		VATSummary vs = accounterReportDAOService.getPriorReturnVATSummary(
				vatAgency, format.parse(today()));

		VATDetailReport vdr = accounterReportDAOService
				.getPriorVATReturnVATDetailReport(vatAgency, format
						.parse(today()));

		List<VATItemDetail> vis = accounterReportDAOService
				.getVATItemDetailReport(dueDate(-1), dueDate(2));
	}

}
