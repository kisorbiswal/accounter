package com.vimukti.accounter.mobile.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.server.FinanceTool;

public class CommandUtils {

	public static ClientPayee getPayeeByName(Company company, String vendorName) {
		Set<Payee> vendors = company.getPayees();
		for (Payee vendor : vendors) {
			if (vendor.getName().equalsIgnoreCase(vendorName)) {
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
		} else if (value instanceof String) {
			String val = (String) value;
			for (T t : values) {
				if (val.equals(t)) {
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
	public static List<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
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

	public static Customer getCustomerByName(Company company,
			String customerName) {
		Set<Customer> customers = company.getCustomers();
		for (Customer customer : customers) {
			if (customer.getName().equals(customerName)) {
				return customer;
			}
		}
		return null;
	}

	public static Map<String, Object> dateRangeChanged(
			AccounterMessages messages, String dateRange,
			String selectedDateRange, ClientCompanyPreferences preferences,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			ClientFinanceDate transactionStartDate) {
		Map<String, Object> returnedMap = new HashMap<String, Object>();
		returnedMap.put("isDateChanges", false);
		try {
			ClientFinanceDate date = new ClientFinanceDate();
			if (!selectedDateRange.equals(messages.all())
					&& dateRange.equals(messages.all())) {
				selectedDateRange = messages.all();
				// startDate = FinanceApplication.getStartDate();
				// endDate = Utility.getLastandOpenedFiscalYearEndDate();
				// if (endDate == null)
				// endDate = new ClientFinanceDate();
				startDate = new ClientFinanceDate(0);
				endDate = new ClientFinanceDate(0);

			} else if (!selectedDateRange.equals(messages.today())
					&& dateRange.equals(messages.today())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.today();
			} else if (!selectedDateRange.equals(messages.endThisWeek())
					&& dateRange.equals(messages.endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
				selectedDateRange = messages.endThisWeek();
			} else if (!selectedDateRange.equals(messages.endThisWeekToDate())
					&& dateRange.equals(messages.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				selectedDateRange = messages.endThisWeekToDate();
			} else if (!selectedDateRange.equals(messages.endThisMonth())
					&& dateRange.equals(messages.endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
				selectedDateRange = messages.endThisMonth();
			} else if (!selectedDateRange.equals(messages.endThisMonthToDate())
					&& dateRange.equals(messages.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.endThisMonthToDate();
			} else if (!selectedDateRange.equals(messages
					.endThisFiscalQuarter())
					&& dateRange.equals(messages.endThisFiscalQuarter())) {
				// changes are needed for calculating Fiscal Quarter,
				// according
				// to user preferences.
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				selectedDateRange = messages.endThisFiscalQuarter();
			} else if (!selectedDateRange.equals(messages
					.endThisFiscalQuarterToDate())
					&& dateRange.equals(messages.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;

				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.endThisFiscalQuarterToDate();
			} else if (!selectedDateRange.equals(messages
					.endThisCalanderQuarter())
					&& dateRange.equals(messages.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				selectedDateRange = messages.endThisCalanderQuarter();
			} else if (!selectedDateRange.equals(messages
					.endThisCalanderQuarterToDate())
					&& dateRange
							.equals(messages.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.endThisCalanderQuarterToDate();
			} else if (!selectedDateRange.equals(messages.endThisFiscalYear())
					&& dateRange.equals(messages.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				selectedDateRange = messages.endThisFiscalYear();
			} else if (!selectedDateRange.equals(messages
					.endThisFiscalYearToDate())
					&& dateRange.equals(messages.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.endThisFiscalYearToDate();
			} else if (!selectedDateRange
					.equals(messages.endThisCalanderYear())
					&& dateRange.equals(messages.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				selectedDateRange = messages.endThisCalanderYear();
			} else if (!selectedDateRange.equals(messages
					.endThisCalanderYearToDate())
					&& dateRange.equals(messages.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.endThisCalanderYearToDate();
				// changeDates(startDate, endDate);
				returnedMap.put("isDateChanges", true);
			} else if (!selectedDateRange.equals(messages.endYesterday())
					&& dateRange.equals(messages.endYesterday())) {
				// startDate = new ClientFinanceDate(date.getYear(),
				// date.getMonth(), date
				// .getDate() - 1);
				startDate = transactionStartDate;
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDay(day - 1);
				selectedDateRange = messages.endYesterday();
			} else if (!selectedDateRange.equals(messages
					.endPreviousFiscalQuarter())
					&& dateRange.equals(messages.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.endPreviousFiscalQuarter();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (!selectedDateRange.equals(messages
					.endLastCalendarQuarter())
					&& dateRange.equals(messages.endLastCalendarQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.endLastCalendarQuarter();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (!selectedDateRange.equals(messages
					.previousFiscalYearSameDates())
					&& dateRange.equals(messages.previousFiscalYearSameDates())) {
				selectedDateRange = messages.previousFiscalYearSameDates();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (!selectedDateRange.equals(messages
					.previousFiscalYearSameDates())
					&& dateRange.equals(messages.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(startDate.getYear() - 1,
						startDate.getMonth(), startDate.getDay());
				endDate = new ClientFinanceDate(endDate.getYear() - 1,
						endDate.getMonth(), endDate.getDay());
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				selectedDateRange = messages.previousFiscalYearSameDates();
			} else if (!selectedDateRange.equals(messages.lastCalenderYear())
					&& dateRange.equals(messages.lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

				selectedDateRange = messages.lastCalenderYear();
			} else if (!selectedDateRange.equals(messages
					.previousCalenderYear())
					&& dateRange.equals(messages.previousCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				// startDate = new ClientFinanceDate(startDate.getYear() -
				// 1,
				// startDate
				// .getMonth(), startDate.getDate());
				// startDate = new ClientFinanceDate(endDate.getYear() - 1,
				// endDate
				// .getMonth(), endDate.getDate());
				// startDate.setYear(startDate.getYear() - 1);
				// endDate.setYear(endDate.getYear() - 1);
				selectedDateRange = messages.previousCalenderYear();
			} else if (!selectedDateRange.equals(messages.lastMonth())
					&& dateRange.equals(messages.lastMonth())) {
				int day;
				if (date.getMonth() == 0) {
					day = getMonthLastDate(11, date.getYear() - 1);
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else {
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, 1);
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				selectedDateRange = messages.lastMonth();
			} else if (!selectedDateRange.equals(messages.last3Months())
					&& dateRange.equals(messages.last3Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 3, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				selectedDateRange = messages.last3Months();
			} else if (!selectedDateRange.equals(messages.last6Months())
					&& dateRange.equals(messages.last6Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 6, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 7, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 8, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 3) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 4) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 5) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 6, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				selectedDateRange = messages.last6Months();
			} else if (!selectedDateRange.equals(messages.lastYear())
					&& dateRange.equals(messages.lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				selectedDateRange = messages.lastYear();
			} else if (!selectedDateRange.equals(messages.present())
					&& dateRange.equals(messages.present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();
				selectedDateRange = messages.present();

			} else if (!selectedDateRange.equals(messages.untilEndOfYear())
					&& dateRange.equals(messages.untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);
				selectedDateRange = messages.untilEndOfYear();

			} else if (!selectedDateRange.equals(messages.thisWeek())
					&& dateRange.equals(messages.thisWeek())) {
				startDate = getWeekStartDate();
				endDate.setDay(startDate.getDay() + 6);
				endDate.setMonth(startDate.getMonth());
				endDate.setYear(startDate.getYear());
				selectedDateRange = messages.thisWeek();
			} else if (!selectedDateRange.equals(messages.thisMonth())
					&& dateRange.equals(messages.thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(new ClientFinanceDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				selectedDateRange = messages.thisMonth();
			} else if (!selectedDateRange.equals(messages.lastWeek())
					&& dateRange.equals(messages.lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDay(endDate.getDay() - 1);

				Calendar startCal = Calendar.getInstance();
				startCal.setTime(endDate.getDateAsObject());
				startCal.set(Calendar.DAY_OF_MONTH,
						startCal.get(Calendar.DAY_OF_MONTH) - 6);
				startDate = new ClientFinanceDate(startCal.getTime());

				selectedDateRange = messages.lastWeek();

			} else if (!selectedDateRange.equals(messages.thisFinancialYear())
					&& dateRange.equals(messages.thisFinancialYear())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = getCurrentFiscalYearEndDate(preferences);
				selectedDateRange = messages.thisFinancialYear();
			} else if (!selectedDateRange.equals(messages.lastFinancialYear())
					&& dateRange.equals(messages.lastFinancialYear())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				startDate.setYear(startDate.getYear() - 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(getCurrentFiscalYearEndDate(preferences)
						.getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				endDate.setYear(endDate.getYear() - 1);
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				selectedDateRange = messages.lastFinancialYear();
			} else if (!selectedDateRange.equals(messages
					.thisFinancialQuarter())
					&& dateRange.equals(messages.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				selectedDateRange = messages.thisFinancialQuarter();
				ClientFinanceDate[] currentFiscalYearQuarter = getCurrentFiscalYearQuarter(
						startDate, endDate, preferences);
				startDate = currentFiscalYearQuarter[0];
				endDate = currentFiscalYearQuarter[1];
			} else if (!selectedDateRange.equals(messages
					.lastFinancialQuarter())
					&& dateRange.equals(messages.lastFinancialQuarter())) {
				ClientFinanceDate[] currentFiscalYearQuarter = getCurrentFiscalYearQuarter(
						startDate, endDate, preferences);
				startDate = currentFiscalYearQuarter[0];
				endDate = currentFiscalYearQuarter[1];
				Calendar startDateCal = Calendar.getInstance();
				startDateCal.setTime(startDate.getDateAsObject());
				startDateCal.set(Calendar.MONTH,
						startDateCal.get(Calendar.MONTH) - 3);
				Calendar endDateCal = Calendar.getInstance();
				endDateCal.setTime(endDate.getDateAsObject());
				endDateCal.set(Calendar.MONTH,
						endDateCal.get(Calendar.MONTH) - 3);
				startDate = new ClientFinanceDate(startDateCal.getTime());
				endDate = new ClientFinanceDate(endDateCal.getTime());
				selectedDateRange = messages.lastFinancialQuarter();
				// getCurrentQuarter();
				// startDate.setYear(startDate.getYear() - 1);
				// endDate.setYear(endDate.getYear() - 1);

			} else if (!selectedDateRange
					.equals(messages.financialYearToDate())
					&& dateRange.equals(messages.financialYearToDate())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				selectedDateRange = messages.financialYearToDate();
			} else if (!selectedDateRange.equals(messages.thisVATQuarter())
					&& dateRange.equals(messages.thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				selectedDateRange = messages.thisVATQuarter();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (!selectedDateRange.equals(messages
					.thisVATQuarterToDate())
					&& dateRange.equals(messages.thisVATQuarterToDate())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				selectedDateRange = messages.thisVATQuarterToDate();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
				endDate = new ClientFinanceDate();

			} else if (!selectedDateRange.equals(messages.lastVATQuarter())
					&& dateRange.equals(messages.lastVATQuarter())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				selectedDateRange = messages.lastVATQuarter();
				ClientFinanceDate[] previousQuarter = getPreviousQuarter(
						startDate, endDate);
				startDate = previousQuarter[0];
				endDate = previousQuarter[1];

			} else if (!selectedDateRange.equals(messages
					.lastVATQuarterToDate())
					&& dateRange.equals(messages.lastVATQuarterToDate())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				selectedDateRange = messages.lastVATQuarterToDate();
				ClientFinanceDate[] previousQuarter = getPreviousQuarter(
						startDate, endDate);
				startDate = previousQuarter[0];
				endDate = previousQuarter[1];
				endDate = new ClientFinanceDate();
			} else if (!selectedDateRange.equals(messages.nextVATQuarter())
					&& dateRange.equals(messages.nextVATQuarter())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				selectedDateRange = messages.nextVATQuarter();
				ClientFinanceDate[] nextQuarter = getNextQuarter(startDate,
						endDate);
				startDate = nextQuarter[0];
				endDate = nextQuarter[1];
			} else if (!selectedDateRange.equals(messages.custom())
					&& dateRange.equals(messages.custom())) {
				startDate = startDate != null && startDate.getDate() == 0 ? new ClientFinanceDate()
						: startDate;
				endDate = endDate != null && endDate.getDate() == 0 ? new ClientFinanceDate()
						: endDate;
				selectedDateRange = messages.custom();
			}
			// changeDates(startDate, endDate);
			returnedMap.put("isDateChanges", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnedMap.put("startDate", startDate);
		returnedMap.put("endDate", endDate);
		returnedMap.put("selectedDateRange", selectedDateRange);
		return returnedMap;
	}

	private static ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}

		return newDate;
	}

	private static native double getWeekEndDate()/*-{
		var date = new ClientFinanceDate();
		var day = date.getDay();
		var remainingDays = 6 - day;
		var newDate = new ClientFinanceDate();
		newDate.setDate(date.getDate() + remainingDays);
		var tmp = newDate.getTime();
		return tmp;
	}-*/;

	private static int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	private static ClientFinanceDate[] getCurrentQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}

		return new ClientFinanceDate[] { startDate, endDate };
	}

	private static ClientFinanceDate[] getCurrentFiscalYearQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			ClientCompanyPreferences preferences) {

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate start = getCurrentFiscalYearStartDate(preferences);
		ClientFinanceDate end = getCurrentFiscalYearEndDate(preferences);

		int currentQuarter = 0;
		ClientFinanceDate quarterStart = getCurrentFiscalYearStartDate(preferences);
		ClientFinanceDate quarterEnd;

		Calendar quarterEndCal = Calendar.getInstance();
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());

		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 1;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 2;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 3;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 4;
		}
		switch (currentQuarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getDateAsObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getDateAsObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getDateAsObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			endDate = end;
			break;
		}

		return new ClientFinanceDate[] { startDate, endDate };
	}

	private static ClientFinanceDate[] getNextQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear() + 1, 0, 1);
			endDate = new ClientFinanceDate(date.getYear() + 1, 2, 31);
			break;
		}
		return new ClientFinanceDate[] { startDate, endDate };
	}

	private static ClientFinanceDate[] getPreviousQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		}

		return new ClientFinanceDate[] { startDate, endDate };
	}

	public static String getAccountTypeString(int accountType) {

		String accountTypeName = null;
		switch (accountType) {
		case ClientAccount.TYPE_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_INCOME;
			break;
		case ClientAccount.TYPE_OTHER_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_INCOME;
			break;
		case ClientAccount.TYPE_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_EXPENSE;
			break;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_EXPENSE;
			break;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			accountTypeName = AccounterClientConstants.TYPE_COST_OF_GOODS_SOLD;
			break;
		case ClientAccount.TYPE_CASH:
			accountTypeName = AccounterClientConstants.TYPE_CASH;
			break;
		case ClientAccount.TYPE_BANK:
			accountTypeName = AccounterClientConstants.TYPE_BANK;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_ASSET;
			break;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_INVENTORY_ASSET;
			break;
		case ClientAccount.TYPE_OTHER_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_ASSET;
			break;
		case ClientAccount.TYPE_FIXED_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_FIXED_ASSET;
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			accountTypeName = AccounterClientConstants.TYPE_CREDIT_CARD;
			break;
		case ClientAccount.TYPE_PAYPAL:
			accountTypeName = AccounterClientConstants.TYPE_PAYPAL;
			break;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_PAYROLL_LIABILITY;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_LIABILITY;
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_LONG_TERM_LIABILITY;
			break;
		case ClientAccount.TYPE_EQUITY:
			accountTypeName = AccounterClientConstants.TYPE_EQUITY;
			break;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_RECEIVABLE;
			break;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_PAYABLE;
			break;

		}
		return accountTypeName;
	}

	/**
	 * 
	 * @param transactionType
	 * @return
	 */
	public static AccounterCoreType getAccounterCoreType(int transactionType) {

		switch (transactionType) {

		case ClientTransaction.TYPE_PAY_BILL:
			return AccounterCoreType.PAYBILL;

		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			return AccounterCoreType.MAKEDEPOSIT;

		case ClientTransaction.TYPE_ENTER_BILL:
			return AccounterCoreType.ENTERBILL;

		case ClientTransaction.TYPE_CASH_PURCHASE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_CASH_SALES:
			return AccounterCoreType.CASHSALES;

		case ClientTransaction.TYPE_WRITE_CHECK:
			return AccounterCoreType.WRITECHECK;

		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			return AccounterCoreType.CUSTOMERREFUND;

		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			return AccounterCoreType.CUSTOMERCREDITMEMO;

		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			return AccounterCoreType.RECEIVEPAYMENT;

		case ClientTransaction.TYPE_INVOICE:
			return AccounterCoreType.INVOICE;

		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			return AccounterCoreType.CREDITCARDCHARGE;

		case ClientTransaction.TYPE_ESTIMATE:
			return AccounterCoreType.ESTIMATE;

		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			return AccounterCoreType.ISSUEPAYMENT;

		case ClientTransaction.TYPE_TRANSFER_FUND:
			return AccounterCoreType.TRANSFERFUND;

		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			return AccounterCoreType.VENDORCREDITMEMO;

		case ClientTransaction.TYPE_PAY_TAX:
			return AccounterCoreType.PAY_TAX;

		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			return AccounterCoreType.JOURNALENTRY;

		case ClientTransaction.TYPE_SALES_ORDER:
			return AccounterCoreType.SALESORDER;

		case ClientTransaction.TYPE_PURCHASE_ORDER:
			return AccounterCoreType.PURCHASEORDER;

		case ClientTransaction.TYPE_ITEM_RECEIPT:
			return AccounterCoreType.ITEMRECEIPT;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			return AccounterCoreType.CREDITCARDCHARGE;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			return AccounterCoreType.CUSTOMERPREPAYMENT;

		case ClientTransaction.TYPE_RECEIVE_TAX:
			return AccounterCoreType.RECEIVEVAT;

		}
		return null;

	}
}
