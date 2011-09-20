package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

/**
 * 
 * @author Sai Prasad N
 *
 */
public class NewJournalEntryCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("TransactionDate", false, true));
		list.add(new ObjectListRequirement("voucher", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("VoucherNumber", false, true));
				list.add(new Requirement("Date", false, true));
				list.add(new Requirement("Accont", false, true));
				list.add(new Requirement("Credit", false, true));
				list.add(new Requirement("Debit", false, true));

			}
		});
		list.add(new ObjectListRequirement("voucher", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("VoucherNumber", false, true));
				list.add(new Requirement("Date", false, true));
				list.add(new Requirement("Accont", false, true));
				list.add(new Requirement("Credit", false, true));
				list.add(new Requirement("Debit", false, true));

			}
		});

		list.add(new Requirement("Memo", true, true));

	}

	@Override
	public Result run(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
