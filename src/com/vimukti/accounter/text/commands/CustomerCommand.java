package com.vimukti.accounter.text.commands;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * name,customerSince,openingBalance,address,webaddress,email,phone,fax
 * 
 * @author Umasree
 * 
 */
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
	public boolean parse(ITextData data, ITextResponse respnse) {
		name = data.nextString("");
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
		}
		customerSince = data.nextDate(new FinanceDate());
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Opening Balance");
		}
		openingBalance = data.nextDouble(0);
		address = data.nextAddress(null);
		webAddress = data.nextString("");
		email = data.nextString("");
		phone = data.nextString("");
		fax = data.nextString("");

		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		Session session = HibernateUtil.getCurrentSession();
		Criteria query = session.createCriteria(Customer.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("name", name));
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
