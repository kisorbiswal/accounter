package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.TransactionItem;
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
public class CashExpenseCommand extends AbstractTransactionCommand {

	private FinanceDate transactionDate;
	private String number;
	private String paymentMethod;
	private String vendorName;
	private String payfrom;
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}

		if (!parseTransactionDate(data, respnse)) {
			return true;
		}
		paymentMethod = data.nextString("");

		vendorName = data.nextString("");

		payfrom = data.nextString("");

		if (!parseTransactionItem(data, respnse)) {
			return true;
		}
		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Criteria query = session.createCriteria(Invoice.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("number", number));
		CashPurchase expense = getObject(Invoice.class, "number", number);
		if (expense == null) {
			expense = new CashPurchase();
		}

		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setName(this.vendorName);
			session.save(vendor);
		}
		expense.setNumber(number);
		expense.setDate(transactionDate);
		expense.setPaymentMethod(paymentMethod);
		ArrayList<TransactionItem> processTransactionItem = processTransactionItem();
		expense.setTransactionItems(processTransactionItem);
		if (memo != null) {
			expense.setMemo(memo);
		}
		// getting Transaction
		double total = getTransactionTotal(processTransactionItem);
		expense.setTotal(total);

		saveOrUpdate(expense);
	}

}
