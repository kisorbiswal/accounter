package com.vimukti.accounter.mobile.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.servlets.CompaniesServlet;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.server.FinanceTool;

public class CommandUtils {

	public static ClientPayee getPayeeByName(Company company, String vendorName) {
		Set<Payee> vendors = company.getPayees();
		for (Payee vendor : vendors) {
			if (vendor.getName().equals(vendorName)) {
				if (vendor instanceof Vendor) {
					return (ClientPayee) getClientObjectById(vendor.getID(),
							AccounterCoreType.VENDOR, company.getId());
				} else if (vendor instanceof Customer) {
					return (ClientPayee) getClientObjectById(vendor.getID(),
							AccounterCoreType.CUSTOMER, company.getId());
				} else if (vendor instanceof TAXAgency) {
					return (ClientPayee) getClientObjectById(vendor.getID(),
							AccounterCoreType.TAXAGENCY, company.getId());
				}
			}
		}
		return null;
	}

	public static IAccounterCore getClientObjectById(long id,
			AccounterCoreType type, long companyId) {
		try {
			return new FinanceTool().getManager().getObjectById(type, id,
					companyId);
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ClientItemGroup getItemGroupByName(Company company,
			String string) {
		Set<ItemGroup> itemGroups = company.getItemGroups();
		for (ItemGroup itemGroup : itemGroups) {
			if (itemGroup.getName().equals(string)) {
				return (ClientItemGroup) getClientObjectById(itemGroup.getID(),
						AccounterCoreType.ITEM_GROUP, company.getId());
			}
		}
		return null;
	}

	public static ClientFinanceDate getCurrentFiscalYearStartDate(
			ClientCompanyPreferences preferences) {
		ClientFinanceDate startDate = new ClientFinanceDate();
		startDate.setMonth(preferences.getFiscalYearFirstMonth() + 1);
		startDate.setDay(1);
		return startDate;
	}

	public static ClientFinanceDate getCurrentFiscalYearEndDate(
			ClientCompanyPreferences preferences) {

		ClientFinanceDate startDate = new ClientFinanceDate();
		startDate.setMonth(preferences.getFiscalYearFirstMonth() + 1);
		startDate.setDay(1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.getDateAsObject());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		ClientFinanceDate endDate = new ClientFinanceDate(calendar.getTime());
		return endDate;
	}

	public static ClientAccount getAccountByName(Company company, String string) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getName().equals(string)) {
				return (ClientAccount) getClientObjectById(account.getID(),
						AccounterCoreType.ACCOUNT, company.getId());
			}
		}
		return null;
	}

	public static ClientAccount getAccountByNumber(Company company,
			String numberFromString) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getNumber().equals(numberFromString)) {
				return (ClientAccount) getClientObjectById(account.getID(),
						AccounterCoreType.ACCOUNT, company.getId());
			}
		}
		return null;
	}

	public static List<String> getPaymentMethods() {
		List<String> paymentMethods = new ArrayList<String>();
		paymentMethods.add("Cash");
		paymentMethods.add("Check");
		paymentMethods.add("Credit Card");
		return paymentMethods;
	}

	public static IAccounterServerCore getServerObjectById(long id,
			AccounterCoreType account) {
		return (IAccounterServerCore) new FinanceTool().getManager()
				.getServerObjectForid(account, id);
	}

	public static List<ClientTAXItem> getClientTaxItems(long companyId) {
		try {
			return new FinanceTool().getManager().getObjects(
					AccounterCoreType.TAXITEM, companyId);
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ClientCustomer getCustomerByNumber(Company company,
			String customerNumber) {
		Set<Customer> customers = company.getCustomers();
		for (Customer customer : customers) {
			if (customer.getNumber().equals(customerNumber)) {
				return (ClientCustomer) getClientObjectById(customer.getID(),
						AccounterCoreType.CUSTOMER, company.getId());
			}
		}
		return null;
	}

	public static ClientVendor getVendorByNumber(Company company,
			String vendorNumber) {
		Set<Vendor> vendors = company.getVendors();
		for (Vendor vendor : vendors) {
			if (vendor.getVendorNumber().equals(vendorNumber)) {
				return (ClientVendor) getClientObjectById(vendor.getID(),
						AccounterCoreType.VENDOR, company.getId());
			}
		}
		return null;
	}

	public static ClientSalesPerson getSalesPersonByName(Company company,
			String string) {
		Set<SalesPerson> salesPersons = company.getSalesPersons();
		for (SalesPerson salesPerson : salesPersons) {
			if (salesPerson.getFirstName().equals(string)
					|| (salesPerson.getLastName() != null && salesPerson
							.getLastName().equals(string))
					|| (salesPerson.getMiddleName1() != null && salesPerson
							.getMiddleName1().equals(string))
					|| (salesPerson.getMiddleName2() != null && salesPerson
							.getMiddleName2().equals(string))
					|| (salesPerson.getMiddleName3() != null && salesPerson
							.getMiddleName3().equals(string))) {
				return (ClientSalesPerson) getClientObjectById(
						salesPerson.getID(), AccounterCoreType.SALES_PERSON,
						company.getId());
			}
		}
		return null;
	}

	public static <T> boolean contains(List<T> values, T value) {
		long id = 0;
		if (value instanceof IAccounterCore) {
			id = ((IAccounterCore) value).getID();
			for (T t : values) {
				long id2 = ((IAccounterCore) t).getID();
				if (id2 == id) {
					return true;
				}
			}
		} else if (value instanceof IAccounterServerCore) {
			id = ((IAccounterServerCore) value).getID();
			for (T t : values) {
				long id2 = ((IAccounterServerCore) t).getID();
				if (id2 == id) {
					return true;
				}
			}
		} else if (value instanceof IssuePaymentTransactionsList) {
			id = ((IssuePaymentTransactionsList) value).getTransactionId();
			for (T t : values) {
				long id2 = ((IssuePaymentTransactionsList) t)
						.getTransactionId();
				if (id2 == id) {
					return true;
				}
			}
		}
		return false;
	}

	public static ClientCustomerGroup getCustomerGroupByName(Company company,
			String string) {
		Set<CustomerGroup> customerGroups = company.getCustomerGroups();
		for (CustomerGroup customerGroup : customerGroups) {
			if (customerGroup.getName().equals(string)) {
				return (ClientCustomerGroup) getClientObjectById(
						customerGroup.getID(),
						AccounterCoreType.CUSTOMER_GROUP, company.getId());
			}
		}
		return null;
	}

	public static ClientVendorGroup getVendorGroupByName(Company company,
			String string) {
		Set<VendorGroup> vendorGroups = company.getVendorGroups();
		for (VendorGroup vendorGroup : vendorGroups) {
			if (vendorGroup.getName().equals(string)) {
				return (ClientVendorGroup) getClientObjectById(
						vendorGroup.getID(), AccounterCoreType.VENDOR_GROUP,
						company.getId());
			}
		}
		return null;
	}

	public static ClientPaymentTerms getPaymentTermByName(Company company,
			String string) {
		Set<PaymentTerms> paymentTerms = company.getPaymentTerms();
		for (PaymentTerms paymentTerm : paymentTerms) {
			if (paymentTerm.getName().equals(string)) {
				return (ClientPaymentTerms) getClientObjectById(
						paymentTerm.getID(), AccounterCoreType.PAYMENT_TERM,
						company.getId());
			}
		}
		return null;
	}

	public static ClientShippingTerms getshippingTermsByNameByName(
			Company company, String string) {
		Set<ShippingTerms> shippingTerms = company.getShippingTerms();
		for (ShippingTerms shippingTerm : shippingTerms) {
			if (shippingTerm.getName().equals(string)) {
				return (ClientShippingTerms) getClientObjectById(
						shippingTerm.getID(), AccounterCoreType.SHIPPING_TERM,
						company.getId());
			}
		}
		return null;
	}

	public static ClientTransaction getClientTransactionByNumber(
			Company company, String number, AccounterCoreType type) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Class<?> serverClass = getClientEquivalentServerClass(type);
		if (serverClass == null) {
			return null;
		}
		Transaction uniqueResult = (Transaction) currentSession
				.createQuery(
						"from "
								+ serverClass.getName()
								+ " transaction where transaction.number=:number and transaction.company=:company")
				.setParameter("number", number)
				.setParameter("company", company).uniqueResult();
		if (uniqueResult != null) {
			ClientTransaction transaction = (ClientTransaction) getClientObjectById(
					uniqueResult.getID(), type, company.getId());
			return transaction;
		}
		return null;
	}

	public static Item getItemByName(Company company, String string) {
		Set<Item> items = company.getItems();
		for (Item item : items) {
			if (item.getName().equals(string)) {
				return item;
			}
		}
		return null;
	}

	public static ClientFiscalYear getFiscalYearByDate(long string,
			Integer integer, Company company) {
		Set<FiscalYear> fiscalYears = company.getFiscalYears();
		for (FiscalYear fiscalYear : fiscalYears) {
			if (fiscalYear.getStartDate().getDate() == string) {
				return (ClientFiscalYear) getClientObjectById(
						fiscalYear.getID(), AccounterCoreType.FISCALYEAR,
						company.getId());
			} else if (fiscalYear.getEndDate().getDate() == string) {
				return (ClientFiscalYear) getClientObjectById(
						fiscalYear.getID(), AccounterCoreType.FISCALYEAR,
						company.getId());
			} else if (fiscalYear.getStatus() == integer) {
				return (ClientFiscalYear) getClientObjectById(
						fiscalYear.getID(), AccounterCoreType.FISCALYEAR,
						company.getId());
			}
		}
		return null;
	}

	public static Location getLocationByName(Company company, String string) {
		Set<Location> locations = company.getLocations();
		for (Location location : locations) {
			if (location.getName().equals(string)) {
				return location;
			}
		}
		return null;
	}

	public static AccounterClass getClassByName(Company company, String string) {
		Set<AccounterClass> accounterClasses = company.getAccounterClasses();
		for (AccounterClass accounterClass : accounterClasses) {
			if (accounterClass.getName().equals(string)) {
				return accounterClass;
			}
		}
		return null;
	}

	public static ClientMeasurement getMeasurement(Company company,
			String string) {
		Set<Measurement> measurements = company.getMeasurements();
		for (Measurement measurement : measurements) {
			if (measurement.getName().equals(string)) {
				return (ClientMeasurement) getClientObjectById(
						measurement.getID(), AccounterCoreType.MEASUREMENT,
						company.getId());
			} else if (measurement.getDesctiption().equals(string)) {
				return (ClientMeasurement) getClientObjectById(
						measurement.getID(), AccounterCoreType.MEASUREMENT,
						company.getId());
			} else if (measurement.getDefaultUnit().getType().equals(string)) {
				return (ClientMeasurement) getClientObjectById(
						measurement.getID(), AccounterCoreType.MEASUREMENT,
						company.getId());
			} else {
				Set<Unit> units = measurement.getUnits();
				for (Unit unit : units) {
					if (unit.getType().equals(string)) {
						return (ClientMeasurement) getClientObjectById(
								measurement.getID(),
								AccounterCoreType.MEASUREMENT, company.getId());
					}
				}
			}
		}
		return null;
	}

	private static Class<?> getClientEquivalentServerClass(
			AccounterCoreType type) {

		String clientClassName = type.getClientClassSimpleName();

		clientClassName = clientClassName.replaceAll("Client", "");

		Class<?> clazz = null;

		// FIXME if Class class1 if of another package other than,
		// com.vimukti.accounter.core

		try {
			String qualifiedName = "com.vimukti.accounter.core."
					+ clientClassName;
			clazz = Class.forName(qualifiedName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazz;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @return
	 */
	public static FinanceDate[] getMinimumAndMaximumDates(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		List<ClientFinanceDate> dates = getMinimumAndMaximumTransactionDate(companyId);
		ClientFinanceDate startDate1 = dates.get(0) == null ? new ClientFinanceDate()
				: dates.get(0);
		ClientFinanceDate endDate2 = dates.get(1) == null ? new ClientFinanceDate()
				: dates.get(1);

		FinanceDate transtartDate;
		if (startDate == null || startDate.isEmpty())
			transtartDate = new FinanceDate(startDate1);
		else
			transtartDate = new FinanceDate(startDate.getDate());
		FinanceDate tranendDate;
		if (endDate == null || endDate.isEmpty())
			tranendDate = new FinanceDate(endDate2);
		else
			tranendDate = new FinanceDate(endDate.getDate());

		return new FinanceDate[] { transtartDate, tranendDate };
	}

	/**
	 * 
	 * @param companyId
	 * @return
	 */
	private static List<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
			long companyId) {
		ArrayList<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = new FinanceTool().getManager()
					.getMinimumAndMaximumTransactionDate(companyId);
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDates;
	}

	/**
	 * get transaction name by the Type.
	 * 
	 * @param transactionType
	 * @return Transaction Name
	 */
	public static String getTransactionName(int transactionType) {

		String transactionName = null;
		switch (transactionType) {
		case 0:
			transactionName = AccounterClientConstants.MEMO_OPENING_BALANCE;
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			transactionName = AccounterClientConstants.TYPE_CASH_SALES;
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			transactionName = AccounterClientConstants.TYPE_CASH_PURCHASE;
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			transactionName = AccounterClientConstants.TYPE_CREDIT_CARD_CHARGE;
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			transactionName = AccounterClientConstants.TYPE_CUSTOMER_CREDIT_MEMO;
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = AccounterClientConstants.TYPE_CUSTOMER_REFUNDS;
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			transactionName = AccounterClientConstants.TYPE_ENTER_BILL;
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			transactionName = AccounterClientConstants.TYPE_ESTIMATE;
			break;
		case ClientTransaction.TYPE_INVOICE:
			transactionName = AccounterClientConstants.TYPE_INVOICE;
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			transactionName = AccounterClientConstants.TYPE_ISSUE_PAYMENT;
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			transactionName = AccounterClientConstants.TYPE_MAKE_DEPOSIT;
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			transactionName = AccounterClientConstants.TYPE_PAY_BILL;
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			transactionName = AccounterClientConstants.TYPE_VENDOR_PAYMENT;
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			transactionName = AccounterClientConstants.TYPE_RECEIVE_PAYMENT;
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			transactionName = AccounterClientConstants.TYPE_TRANSFER_FUND;
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = AccounterClientConstants.TYPE_VENDOR_CREDIT_MEMO;
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			transactionName = AccounterClientConstants.TYPE_WRITE_CHECK;
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			transactionName = AccounterClientConstants.TYPE_JOURNAL_ENTRY;
			break;
		case ClientTransaction.TYPE_PAY_TAX:
			transactionName = AccounterClientConstants.TYPE_PAY_TAX;
			break;
		case ClientTransaction.TYPE_RECEIVE_TAX:
			transactionName = AccounterClientConstants.TYPE_RECEIVE_TAX;
			break;
		case ClientTransaction.TYPE_SALES_ORDER:
			transactionName = AccounterClientConstants.TYPE_SALES_ORDER;
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			transactionName = AccounterClientConstants.TYPE_PURCHASE_ORDER;
			break;
		case ClientTransaction.TYPE_ITEM_RECEIPT:
			transactionName = AccounterClientConstants.TYPE_ITEM_RECEIPT;
			break;
		case ClientTransaction.TYPE_CASH_EXPENSE:
			transactionName = AccounterClientConstants.TYPE_CASH_EXPENSE;
			break;
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			transactionName = AccounterClientConstants.TYPE_EMPLOYEE_EXPENSE;
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			transactionName = AccounterClientConstants.TYPE_CREDIT_CARD_EXPENSE;
			break;
		case ClientTransaction.TYPE_TAX_RETURN:
			transactionName = AccounterClientConstants.TYPE_VAT_RETURN;
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			transactionName = AccounterClientConstants.TYPE_CUSTOMER_PREPAYMENT;
			break;
		case ClientTransaction.TYPE_ADJUST_SALES_TAX:
		case ClientTransaction.TYPE_ADJUST_VAT_RETURN:
			transactionName = Accounter.messages().taxAdjustment();
			break;
		}
		return transactionName;
	}

	/**
	 * 
	 * @param company
	 * @param string
	 * @return
	 */
	public static Account getaccount(Company company, String string) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getName().equals(string)) {
				return account;
			}
		}
		return null;
	}

	/**
	 * get taxCode
	 * 
	 * @param taxCodeId
	 * @param company
	 * @return
	 */
	public static TAXCode getTaxCode(long taxCodeId, Company company) {
		Set<TAXCode> taxCodes = company.getTaxCodes();
		for (TAXCode taxCode : taxCodes) {
			if (taxCode.getID() == taxCodeId) {
				return taxCode;
			}
		}
		return null;
	}
}
