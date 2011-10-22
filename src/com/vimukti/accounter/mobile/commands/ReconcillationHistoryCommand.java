package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class ReconcillationHistoryCommand extends AbstractCommand {

	private static final String BANKACCOUNT = "BankAccount";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(BANKACCOUNT, false, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().account()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = accountRequirement(context, list, BANKACCOUNT, getConstants()
				.bankAccount(), new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				if (e.getType() == ClientAccount.TYPE_BANK) {
					return true;
				} else {
					return false;
				}
			}
		});

		if (result != null) {
			return result;
		}

		result = getReconcillationHistory(context);
		return result;
	}

	/**
	 * 
	 * @param context
	 */

	private Result getReconcillationHistory(Context context) {
		Result result = context.makeResult();
		ResultList accountsList = new ResultList("reconlist");
		result.add(getConstants().ReconciliationsList());
		List<ClientReconciliation> reconciliationsByBankAccountID = null;
		ClientAccount bankAccouont = (ClientAccount) get(BANKACCOUNT)
				.getValue();
		try {
			reconciliationsByBankAccountID = new FinanceTool()
					.getReconciliationsByBankAccountID(bankAccouont.getID(),
							context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		ResultList actions = new ResultList("actions");

		List<ClientReconciliation> pagination = pagination(context, null,
				actions, reconciliationsByBankAccountID,
				new ArrayList<ClientReconciliation>(), 5);

		for (ClientReconciliation recon : pagination) {
			accountsList.add(createReconcillationRecord(recon));
		}

		result.add(accountsList);
		return result;
	}

	private Record createReconcillationRecord(ClientReconciliation recon) {

		Record rec = new Record(recon);
		rec.add("", getConstants().ReconciliationDate());
		rec.add("", recon.getReconcilationDate());
		rec.add("", getConstants().openingBalance());
		rec.add("", recon.getOpeningBalance());
		rec.add("", getConstants().ClosingBalance());
		rec.add("", recon.getClosingBalance());
		return rec;

	}
}
