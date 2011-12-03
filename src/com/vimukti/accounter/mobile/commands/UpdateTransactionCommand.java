package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ForwardRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class UpdateTransactionCommand extends NewAbstractCommand {

	private static final String TRANSACTION = "transaction";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long transactionId = Long.valueOf(context.getString());
		Session session = context.getHibernateSession();
		Transaction transaction = (Transaction) session.get(Transaction.class,
				transactionId);
		try {
			transaction.canEdit(transaction);
			get(TRANSACTION).setValue(transaction);
			context.setString("");
		} catch (AccounterException e) {
			return e.getMessage();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ListRequirement<Transaction>(TRANSACTION, getMessages()
				.pleaseEnter("transaction name"), "Transaction", false, true,
				null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected Record createRecord(Transaction value) {
				Record record = new Record(value);
				record.add(getMessages().type(), value.getType());
				record.add(getMessages().date(), value.getDate());
				record.add(getMessages().netAmount(), value.getNetAmount());
				record.add(getMessages().total(), value.getTotal());
				return null;
			}

			@Override
			protected String getDisplayValue(Transaction value) {
				return null;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected boolean filter(Transaction e, String name) {
				return false;
			}

			@Override
			protected List<Transaction> getLists(Context context) {
				return null;
			}
		});
		list.add(new ForwardRequirement() {

			@Override
			public String getNextCommand() {
				Transaction transaction = get(TRANSACTION).getValue();
				String transactionName = Utility.getTransactionName(transaction
						.getType());
				return "Update " + transactionName + " #"
						+ transaction.getNumber();
			}
		});
	}
}
