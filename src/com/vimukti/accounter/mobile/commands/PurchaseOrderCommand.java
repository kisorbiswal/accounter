package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class PurchaseOrderCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("vendor", false, true));
		list.add(new Requirement("status", false, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("total", true, true));
			}
		});

		list.add(new Requirement("contact", true, false));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("billto", true, false));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("orderno", true, true));
		list.add(new Requirement("orderorderno", true, true));
		list.add(new Requirement("paymentterms", true, true));
		list.add(new Requirement("duedate", true, true));
		list.add(new Requirement("dispatchdate", true, true));
		list.add(new Requirement("receiveddate", true, true));

	}

	@Override
	public Result run(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
