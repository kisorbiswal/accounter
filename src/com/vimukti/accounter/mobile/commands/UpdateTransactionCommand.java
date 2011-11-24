package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ForwardRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;

public class UpdateTransactionCommand extends NewAbstractCommand {

	private static final String TRANSACTION = "transaction";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long transactionId = Long.valueOf(context.getString());
		Session session = context.getHibernateSession();
		Transaction transaction = (Transaction) session.get(Transaction.class,
				transactionId);
		get(TRANSACTION).setValue(transaction);
		context.setString("");
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ListRequirement<Transaction>(TRANSACTION, getMessages()
				.pleaseEnter("transaction name"), "Transaction", false, true,
				null) {

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected Record createRecord(Transaction value) {
				Record record = new Record(value);

				record.add("", value.getType());
				record.add("", value.getDate());
				record.add("", value.getNetAmount());
				record.add("", value.getTotal());

				return null;
			}

			@Override
			protected String getDisplayValue(Transaction value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getSelectString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected boolean filter(Transaction e, String name) {
				// TODO Auto-generated method stub
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
				String transactionName = CommandUtils
						.getTransactionName(transaction.getType());
				return "Update " + transactionName + " #"
						+ transaction.getNumber();
			}
		});
	}
}
