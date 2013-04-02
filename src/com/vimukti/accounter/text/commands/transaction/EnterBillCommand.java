package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Session;

import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * Number,TransactionDate,vendorName,dueDate,deliveryDate,ItemName,Description,
 * UnitPrice,Quantity, Tax
 * ==========================================================================
 * Number
 * ,TransactionDate,vendorName,dueDate,deliveryDate,AccountName,Description
 * ,Amount,Tax
 * 
 * @author Lingarao.R
 * 
 */
public class EnterBillCommand extends AbstractTransactionCommand {

	private String number;
	private String vendorName;
	private String memo;
	private FinanceDate dueDate;
	private FinanceDate deliveryDate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		// Number
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		// customer
		vendorName = data.nextString("");

		if (!parseTransactionDate(data, respnse)) {
			return true;
		}

		// Transaction Item
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		// Due Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		dueDate = data.nextDate(new FinanceDate());
		// Delivery Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		deliveryDate = data.nextDate(new FinanceDate());
		// Transaction Item
		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		// memo
		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		EnterBill enterBill = getObject(EnterBill.class, "number", number);
		if (enterBill == null) {
			enterBill = new EnterBill();
		}
		enterBill.setNumber(number);
		enterBill.setDate(transactionDate);
		enterBill.setDueDate(dueDate);
		enterBill.setDeliveryDate(deliveryDate);
		// TODO status
		// TODO payment Terms
		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(vendorName);
			session.save(vendor);
		}
		enterBill.setVendor(vendor);
		// setting the Transaction iTems to Transaction
		ArrayList<TransactionItem> processTransactionItem = processTransactionItem();
		enterBill.setTransactionItems(processTransactionItem);
		// setting the Transaction Total
		enterBill.setTotal(getTransactionTotal(processTransactionItem));

		enterBill.setMemo(memo);

		saveOrUpdate(enterBill);
	}

}
