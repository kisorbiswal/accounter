package com.vimukti.accounter.text.commands.transaction;

import org.hibernate.Session;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */
public class PayBillCommand extends AbstractTransactionCommand {

	private String number;
	private String vendorName;
	private String paymentMethod;
	private String payFromAccountName;
	private String enterBillNumber;
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
		vendorName = data.nextString("");
		// Payment Method
		paymentMethod = data.nextString("Cash");
		// checkNumber
		checkNumber = data.nextString("");
		// amount
		amount = data.nextDouble(0);
		// depositIn
		payFromAccountName = data.nextString("");
		// Invoice Number
		enterBillNumber = data.nextString("");
		// memo
		memo = data.nextString("");

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			respnse.addError("Invalid Vendor Name");
			return;
		}
		// getting the invoice Based On Number
		EnterBill enterBill = getObject(EnterBill.class, "invoiceNumber",
				enterBillNumber);
		if (enterBill == null) {
			respnse.addError("Invalid Enter Bill Number for this Vendor");
			return;
		}
		PayBill payBill = getObject(PayBill.class, "number", number);
		if (payBill == null) {
			payBill = new PayBill();
		}
		payBill.setDate(transactionDate);
		payBill.setNumber(number);

		payBill.setVendor(vendor);

		BankAccount payFrom = getObject(BankAccount.class, "name",
				payFromAccountName);
		if (payFrom == null) {
			payFrom = new BankAccount();
			payFrom.setName(payFromAccountName);
			session.save(payFrom);
		}
		payBill.setPayFrom(payFrom);
		payBill.setPaymentMethod(paymentMethod);
		payBill.setCheckNumber(checkNumber);
		// Preparing the ReceivePayment Item
		TransactionPayBill payment = new TransactionPayBill();
		payment.setEnterBill(enterBill);
		payment.setBillNumber(enterBill.getNumber());
		payment.setOriginalAmount(enterBill.getTotal());
		payment.setDueDate(enterBill.getDueDate());
		payment.setPayment(amount);

		payBill.getTransactionPayBill().add(payment);

		payBill.setMemo(memo);

		saveOrUpdate(payBill);

	}
}
