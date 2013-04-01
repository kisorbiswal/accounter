package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.CreateOrUpdateCommand;
import com.vimukti.accounter.utils.HibernateUtil;

//pay cheque,date,vendor,invoice number,amount,chequeno,bankname,account number
public class WriteCheckCommand extends CreateOrUpdateCommand {

	private FinanceDate transactionDate;
	private String number;
	private String payTo;
	private String bankAccount;
	private String checkNumber;
	private ArrayList<TransctionItem> items = new ArrayList<TransctionItem>();
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
		}
		// if next date is null,then set the default present date
		transactionDate = data.nextDate(new FinanceDate());
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		payTo = data.nextString("");
		bankAccount = data.nextString("");
		checkNumber = data.nextString("");
		String accountName = data.nextString("");
		TransctionItem item = new TransctionItem();
		item.setAccount(accountName);
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Amount field");
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
		Criteria query = session.createCriteria(WriteCheck.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("number", number));
		WriteCheck writeCheck = (WriteCheck) query.uniqueResult();
		if (writeCheck == null) {
			writeCheck = new WriteCheck();
		}

		Criteria payeeQuery = session.createCriteria(Payee.class);
		payeeQuery.add(Restrictions.eq("company", getCompany()));
		payeeQuery.add(Restrictions.eq("name", payTo));
		Payee payee = (Payee) query.uniqueResult();
		if (payee == null) {
			payee = new Vendor();
			payee.setName(payTo);
			session.save(payee);
		}
		writeCheck.setNumber(number);
		writeCheck.setDate(transactionDate);
		Criteria bankAccountQuery = session.createCriteria(BankAccount.class);
		bankAccountQuery.add(Restrictions.eq("company", getCompany()));
		bankAccountQuery.add(Restrictions.eq("name", bankAccount));
		BankAccount bankAccount = (BankAccount) query.uniqueResult();
		if (bankAccount == null) {
			bankAccount = new BankAccount();
			bankAccount.setName(this.bankAccount);
			session.save(bankAccount);
		}
		writeCheck.setBankAccount(bankAccount);
		writeCheck.setCheckNumber(checkNumber);
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
		writeCheck.setTransactionItems(transactionItems);
		if (memo != null) {
			writeCheck.setMemo(memo);
		}
		// getting Transaction Total
		double total = getTransactionTotal(transactionItems);
		writeCheck.setTotal(total);
	}

}
