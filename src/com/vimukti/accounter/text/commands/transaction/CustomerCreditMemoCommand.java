package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * Number,TransactionDate,CustomerName,ItemName,Description,UnitPrice,Quantity,
 * Tax
 * ==========================================================================
 * Number,TransactionDate,CustomerName,AccountName,Description,Amount,Tax
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerCreditMemoCommand extends AbstractTransactionCommand {

	private String number;
	private String memo;
	private String customerName;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// Transaction Date
		boolean parseTransactionDate = parseTransactionDate(data, respnse);
		if (!parseTransactionDate) {
			return false;
		}
		// customer
		customerName = data.nextString("");
		// Transaction Item
		boolean parseTransactionItem = parseTransactionItem(data, respnse);
		if (!parseTransactionItem) {
			return false;
		}
		// memo
		memo = data.nextString(null);
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		CustomerCreditMemo creditMemo = getObject(CustomerCreditMemo.class,
				"number", number);
		if (creditMemo == null) {
			creditMemo = new CustomerCreditMemo();
		}
		creditMemo.setNumber(number);
		creditMemo.setDate(transactionDate);
		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			session.save(customer);
		}
		creditMemo.setCustomer(customer);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processTransactionItem();
		creditMemo.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		double transactionTotal = getTransactionTotal(processTransactionItem);
		creditMemo.setTotal(transactionTotal);

		creditMemo.setMemo(memo);

		saveOrUpdate(creditMemo);
	}

}
