package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CashSaleCommand extends AbstractTransactionCommand {

	private String number;
	private String customerName;
	private String memo;
	private String paymentmethod;
	private String despositIn;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// customer
		customerName = data.nextString("");

		if (!parseTransactionDate(data, respnse)) {
			return true;
		}

		// Transaction Item
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		// paymentMethod
		paymentmethod = data.nextString("");
		// desposit in Account
		despositIn = data.nextString("");
		// memo
		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		CashSales cashSales = getObject(Invoice.class, "number", number);
		if (cashSales == null) {
			cashSales = new CashSales();
		}

		cashSales.setNumber(number);
		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			session.save(customer);
		}
		cashSales.setCustomer(customer);

		cashSales.setDate(transactionDate);

		BankAccount bankAccount = getObject(BankAccount.class, "name",
				despositIn);
		if (bankAccount == null) {
			bankAccount = new BankAccount();
			bankAccount.setName(this.despositIn);
			session.save(bankAccount);
		}

		cashSales.setDepositIn(bankAccount);
		// Processed Transaction Items
		ArrayList<TransactionItem> processTransactionItems = processCustomerTransactionItem();

		cashSales.setTransactionItems(processTransactionItems);
		// geting the transaction Total
		double transactionTotal = getTransactionTotal(processTransactionItems);

		cashSales.setTotal(transactionTotal);

		cashSales.setMemo(memo);

		cashSales.setPaymentMethod(paymentmethod);

		saveOrUpdate(cashSales);
	}
}
