package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * This VoidCommand class for the void command and void transaction also
 * 
 * @author vimukti8
 * 
 */
public class VoidTransactionCommand extends Command {

	private static final int TRANSACTIONS_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("transaction", false, true));
	}

	@Override
	public Result run(Context context) {
		Requirement transactionReq = get("transaction");
		Transaction seleTransaction = context.getSelection("Transaction");
		if (seleTransaction != null) {
			transactionReq.setValue(seleTransaction);
		}
		if (!transactionReq.isDone()) {
			return getTransactionResult(context);
		} else {
			voidTransaction(context, transactionReq);
			markDone();
		}
		return null;
	}

	private void voidTransaction(Context context, Requirement transactionReq) {
		// TODO
		Transaction transaction = (Transaction) transactionReq.getValue();
		// Need to void the transaction and have to save transaction.
	}

	private Result getTransactionResult(Context context) {
		Result result = context.makeResult();
		ResultList transactionList = new ResultList("Transaction");
		Transaction lastTransaction = (Transaction) context
				.getLast(RequirementType.TRANSACTION);
		if (lastTransaction != null && !lastTransaction.isVoid()) {
			transactionList
					.add(createTransactionRecord((Transaction) lastTransaction));
		}
		List<Transaction> transactions = getTransactionsList(context
				.getHibernateSession());
		for (int i = 0; i < TRANSACTIONS_TO_SHOW || i < transactions.size(); i++) {
			Transaction transaction = transactions.get(i);
			if (transaction != lastTransaction) {
				transactionList.add(createTransactionRecord(transaction));
			}
		}
		int size = transactionList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Transaction to void");
		}
		result.add(message.toString());
		result.add(transactionList);
		result.add("Type Transaction number for Transaction");
		return result;
	}

	private List<Transaction> getTransactionsList(Session session) {
		// TODO Auto-generated method stub
		// Get 5 transactions from the data base based on the dates and
		// which are not voided
		return null;
	}

	private Record createTransactionRecord(Transaction transaction) {
		// TODO Auto-generated method stub
		// Need to convert transaction to record.
		return null;
	}

}
