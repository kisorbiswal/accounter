package com.vimukti.accounter.text.commands;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;

//name,customerSince,openingBalance,address,webaddress,email,phone,fax
public class CustomerCommand extends CreateOrUpdateCommand {

	private String name;
	private FinanceDate customerSince;
	private double openingBalance;
	private Address address;
	private String webAddress;
	private String email;
	private String phone;
	private String fax;

	@Override
	public void parse(ITextData data, ITextResponse respnse) {
		name = data.nextString();
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
		}
		customerSince = data.nextDate();
		openingBalance = data.nextDouble();
		address = data.nextAddress();
		webAddress = data.nextString();
		email = data.nextString();
		phone = data.nextString();
		fax = data.nextString();
	}

	@Override
	public void process(ITextResponse respnse) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getCustomerByname")
				.setParameter("company", getCompany())
				.setParameter("name", name);
		Customer customer = (Customer) query.uniqueResult();
		if (customer == null) {
			customer = new Customer();
		}
		customer.setName(name);
		customer.setPayeeSince(customerSince);
		customer.setOpeningBalance(openingBalance);
		customer.getAddress().add(address);
		customer.setWebPageAddress(webAddress);
		customer.setEmail(email);
		customer.setPhoneNo(phone);
		customer.setFaxNo(fax);

		session.save(customer);
	}

}
