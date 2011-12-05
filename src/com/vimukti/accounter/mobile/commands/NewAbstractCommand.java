package com.vimukti.accounter.mobile.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public abstract class NewAbstractCommand extends NewCommand {
	public static final String FIRST_MESSAGE = "firstMessage";
	private IGlobal global;
	private AccounterMessages messages;
	protected static final String AMOUNTS_INCLUDE_TAX = "Amounts Include tax";
	protected static final String COUNTRY = "country";
	protected static final String PHONE = "phone";
	protected static final String EMAIL = "email";
	protected static final String ACTIONS = "actions";
	protected static final int VALUES_TO_SHOW = 20;
	protected static final int COUNTRIES_TO_SHOW = 10;
	protected static final String VIEW_BY = "viewBy";
	protected static final String FROM_DATE = "fromDate";
	protected static final String TO_DATE = "to_date";
	protected static final String DATE_RANGE = "dateRange";

	public NewAbstractCommand() {

	}

	@Override
	public void init() {
		try {
			global = new ServerGlobal();
		} catch (IOException e) {
			e.printStackTrace();
		}
		messages = global.messages();
		super.init();
	}

	protected AccounterMessages getMessages() {
		return messages;
	}

	protected void create(IAccounterCore coreObject, Context context) {
		try {
			if (coreObject.getID() == 0) {
				String clientClassSimpleName = coreObject.getObjectType()
						.getClientClassSimpleName();
				OperationContext opContext = new OperationContext(context
						.getCompany().getID(), coreObject, context
						.getIOSession().getUserEmail());

				opContext.setArg2(clientClassSimpleName);

				new FinanceTool().create(opContext);
			} else {
				String serverClassFullyQualifiedName = coreObject
						.getObjectType().getServerClassFullyQualifiedName();

				OperationContext opContext = new OperationContext(context
						.getCompany().getID(), coreObject, context
						.getIOSession().getUserEmail(),
						String.valueOf(coreObject.getID()),
						serverClassFullyQualifiedName);

				new FinanceTool().update(opContext);
			}
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	protected long getNumberFromString(String string) {
		if (string.isEmpty()) {
			return 0;
		}
		if (string.charAt(0) != '#') {
			return 0;
		}
		string = string.substring(1);
		if (string.isEmpty()) {
			return 0;
		}
		return Long.parseLong(string);
	}

	@SuppressWarnings("unchecked")
	public void addFirstMessage(Context context, String string) {
		((List<String>) context.getAttribute(FIRST_MESSAGE)).add(string);
	}

	protected List<String> getPaymentMethods() {
		List<String> listOfPaymentMethods = new ArrayList<String>();
		listOfPaymentMethods.add(getMessages().cash());
		listOfPaymentMethods.add(getMessages().cheque());
		listOfPaymentMethods.add(getMessages().creditCard());
		listOfPaymentMethods.add(getMessages().directDebit());
		listOfPaymentMethods.add(getMessages().masterCard());
		listOfPaymentMethods.add(getMessages().onlineBanking());
		listOfPaymentMethods.add(getMessages().standingOrder());
		listOfPaymentMethods.add(getMessages().switchMaestro());
		return listOfPaymentMethods;
	}

	/**
	 * set address Type
	 * 
	 * @param type
	 * @return
	 */
	public int getAddressType(String type) {
		if (type.equalsIgnoreCase("1"))
			return ClientAddress.TYPE_BUSINESS;
		else if (type.equalsIgnoreCase(getMessages().billTo()))
			return ClientAddress.TYPE_BILL_TO;
		else if (type.equalsIgnoreCase(getMessages().shipTo()))
			return ClientAddress.TYPE_SHIP_TO;
		else if (type.equalsIgnoreCase("2"))
			return ClientAddress.TYPE_WAREHOUSE;
		else if (type.equalsIgnoreCase("3"))
			return ClientAddress.TYPE_LEGAL;
		else if (type.equalsIgnoreCase("4"))
			return ClientAddress.TYPE_POSTAL;
		else if (type.equalsIgnoreCase("5"))
			return ClientAddress.TYPE_HOME;
		else if (type.equalsIgnoreCase(Accounter.messages().company()))
			return ClientAddress.TYPE_COMPANY;
		else if (type.equalsIgnoreCase(Accounter.messages()
				.companyregistration()))
			return ClientAddress.TYPE_COMPANY_REGISTRATION;

		return ClientAddress.TYPE_OTHER;
	}

	protected ClientContact toClientContact(Contact contact) {
		if (contact == null) {
			return null;
		}
		ClientContact clientContact = new ClientContact();
		clientContact.setBusinessPhone(contact.getBusinessPhone());
		clientContact.setEmail(contact.getEmail());
		clientContact.setID(contact.getID());
		clientContact.setName(contact.getName());
		clientContact.setPrimary(contact.isPrimary());
		clientContact.setTitle(contact.getTitle());
		clientContact.setVersion(contact.getVersion());
		return clientContact;
	}

	protected Contact toServerContact(ClientContact contact) {
		if (contact == null) {
			return null;
		}
		Contact clientContact = new Contact();
		clientContact.setBusinessPhone(contact.getBusinessPhone());
		clientContact.setEmail(contact.getEmail());
		clientContact.setName(contact.getName());
		clientContact.setPrimary(contact.isPrimary());
		clientContact.setTitle(contact.getTitle());
		clientContact.setVersion(contact.getVersion());
		return clientContact;
	}

	protected ClientAddress toClientAddress(Address address) {
		if (address == null) {
			return null;
		}
		ClientAddress clientAddress = new ClientAddress();
		clientAddress.setAddress1(address.getAddress1());
		clientAddress.setCity(address.getCity());
		clientAddress.setCountryOrRegion(address.getCountryOrRegion());
		clientAddress.setID(address.getID());
		clientAddress.setIsSelected(address.getIsSelected());
		clientAddress.setStateOrProvinence(address.getStateOrProvinence());
		clientAddress.setStreet(address.getStreet());
		clientAddress.setType(address.getType());
		clientAddress.setVersion(address.getVersion());
		clientAddress.setZipOrPostalCode(address.getZipOrPostalCode());
		return clientAddress;
	}

	protected List<Customer> getCustomers() {
		Session currentSession = HibernateUtil.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Customer> customers = currentSession
				.getNamedQuery("getCustomersOrderByName")
				.setParameter("company", getCompany()).list();
		return new ArrayList<Customer>(customers);
	}

	protected List<Vendor> getVendors() {
		Session currentSession = HibernateUtil.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Vendor> customers = currentSession
				.getNamedQuery("getVendorsOrderByName")
				.setParameter("company", getCompany()).list();
		return new ArrayList<Vendor>(customers);
	}

	public long autoGenerateAccountnumber(int range1, int range2,
			Company company) {
		// TODO::: add a filter to filter the accounts based on the account type
		Set<Account> accounts = company.getAccounts();
		Long number = null;
		if (number == null) {
			number = (long) range1;
			for (Account account : accounts) {
				while (number.toString().equals(account.getNumber())) {
					number++;
					if (number >= range2) {
						number = (long) range1;
					}
				}
			}
		}
		return number;
	}
}
