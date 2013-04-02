package com.vimukti.accounter.text.commands.transaction;

import org.hibernate.Session;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionReceivePayment;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Number,TransactionDate,customer,paymentMethod,CheckNumber,DepositIn,Invoice
 * Number,Memo
 * 
 * @author vimukti10
 * 
 */
public class ReceivePaymentCommand extends AbstractTransactionCommand {

	private String number;
	private String customerName;
	private String paymentMethod;
	private String depositInAccounName;
	private String invoiceNumber;
	private String memo;
	private String checkNumber;
	private double amount;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// Transaction date
		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		// customer
		customerName = data.nextString("");
		// Payment Method
		paymentMethod = data.nextString("Cash");
		// checkNumber
		checkNumber = data.nextString("");
		// amount
		amount = data.nextDouble(0);
		// depositIn
		depositInAccounName = data.nextString("");
		// Invoice Number
		invoiceNumber = data.nextString("");
		// memo
		memo = data.nextString("");

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		Customer customer = getObject(Customer.class, "name", customerName);
		if (customer == null) {
			respnse.addError("Invalid Customer Name");
			return;
		}
		// getting the invoice Based On Number
		Invoice invoice = getObject(Invoice.class, "invoiceNumber",
				invoiceNumber);
		if (invoice == null) {
			respnse.addError("Invalid Invoice Number for this Customer");
			return;
		}
		ReceivePayment receivePayment = getObject(ReceivePayment.class,
				"number", number);
		if (receivePayment == null) {
			receivePayment = new ReceivePayment();
		}
		receivePayment.setDate(transactionDate);
		receivePayment.setNumber(number);

		receivePayment.setCustomer(customer);

		BankAccount depositIn = getObject(BankAccount.class, "name",
				depositInAccounName);
		if (depositIn == null) {
			depositIn = new BankAccount();
			depositIn.setName(depositInAccounName);
			session.save(depositIn);
		}
		receivePayment.setDepositIn(depositIn);
		receivePayment.setPaymentMethod(paymentMethod);
		receivePayment.setCheckNumber(checkNumber);
		// Preparing the ReceivePayment Item
		TransactionReceivePayment payment = new TransactionReceivePayment();
		payment.setInvoice(invoice);
		payment.setNumber(invoice.getNumber());
		payment.setInvoiceAmount(invoice.getTotal());
		payment.setDueDate(invoice.getDueDate());
		payment.setPayment(amount);

		receivePayment.getTransactionReceivePayment().add(payment);

		receivePayment.setMemo(memo);
		saveOrUpdate(receivePayment);

	}
}
