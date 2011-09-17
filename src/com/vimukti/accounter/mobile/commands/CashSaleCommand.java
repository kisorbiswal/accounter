package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class CashSaleCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("customer", false, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement("paymentMethod", false, true));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("memo", true, true));
		list.add(new ObjectListRequirement("transferTo", false, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("accountNo", false, true));
				list.add(new Requirement("accountName", false, true));
			}

		});
	}

	@Override
	public Result run(Context context) {
		Result result = customerRequirement(context);
		if (result == null) {
			// TODO
		}

		result = itemsRequirement(context);
		if (result == null) {
			// TODO
		}

		depositeOrTransferTo(context);

		result = createOptionalResult(context);
		return null;
	}

	private Result depositeOrTransferTo(Context context) {
		Requirement transferedReq = get("transferTo");
		if (!transferedReq.isDone()) {
			// transferedAccount = context.getSelections();
			// if (transferedAccountList != null
			// && transferedAccountList.size() > 0) {

			// }
		}
		return null;
	}

	private Result createOptionalResult(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
