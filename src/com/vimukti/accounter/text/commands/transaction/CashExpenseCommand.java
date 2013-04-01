package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.CreateOrUpdateCommand;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * 
 * @author vimukti10
 * 
 */
public class CashExpenseCommand extends CreateOrUpdateCommand {

	private FinanceDate transactionDate;
	private String number;
	private String paymentMethod;
	private String vendor;
	private String payfrom;
	private ArrayList<TransctionItem> items = new ArrayList<TransctionItem>();
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		transactionDate = data.nextDate(new FinanceDate());

		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		paymentMethod = data.nextString("");
		vendor = data.nextString("");
		payfrom = data.nextString("");

		String itemName = data.nextString("");
		TransctionItem item = new TransctionItem();
		item.setAccount(itemName);
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Amount field");
			return false;
		}
		item.setAmount(data.nextDouble(0));
		item.setTax(data.nextString(null));
		item.setDescription(data.nextString(null));
		items.add(item);

		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		Session session = HibernateUtil.getCurrentSession();
		Criteria query = session.createCriteria(Invoice.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("number", number));
		CashPurchase expense = (CashPurchase) query.uniqueResult();
		if (expense == null) {
			expense = new CashPurchase();
		}

		Criteria customerQuery = session.createCriteria(Customer.class);
		customerQuery.add(Restrictions.eq("company", getCompany()));
		customerQuery.add(Restrictions.eq("name", vendor));
		Vendor vendor = (Vendor) query.uniqueResult();
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(this.vendor);
			session.save(vendor);
		}
		expense.setNumber(number);
		expense.setDate(transactionDate);
		expense.setPaymentMethod(paymentMethod);
		ArrayList<TransactionItem> transactionItems = new ArrayList<TransactionItem>();
		for (TransctionItem titem : items) {
			TransactionItem transcItem = new TransactionItem();

			String accountName = titem.getAccount();
			Criteria accountQuery = session.createCriteria(Account.class);
			accountQuery.add(Restrictions.eq("company", getCompany()));
			accountQuery.add(Restrictions.eq("name", accountName));
			Account account = (Account) accountQuery.uniqueResult();
			if (account == null) {
				account = new Account();
				account.setName(accountName);
				session.save(account);
			}
			transcItem.setAccount(account);
			transcItem.setUnitPrice(titem.getAmount());
			// getting Line Total
			Double lineTotal = getLineTotal(titem.getAmount(), titem.getTax());
			transcItem.setLineTotal(lineTotal);

			String desc = titem.getDescription();
			if (desc != null) {
				transcItem.setDescription(desc);
			}
			transactionItems.add(transcItem);
		}
		expense.setTransactionItems(transactionItems);
		if (memo != null) {
			expense.setMemo(memo);
		}
		// getting Transaction
		double total = getTransactionTotal(transactionItems);
		expense.setTotal(total);

		session.save(expense);

	}

}
