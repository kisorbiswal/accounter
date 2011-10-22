package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class NewVendorGroupCommand extends AbstractTransactionCommand {
	private static final String VENDORGROUP_NAME = "VendorGroup Name";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VENDORGROUP_NAME, false, true));

	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getMessages().vendorGroup(Global.get().Vendor())));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		Result result = nameRequirement(
				context,
				list,
				VENDORGROUP_NAME,
				getMessages().customerGroup(Global.get().Vendor()),
				getMessages().pleaseEnter(
						getMessages().vendorGroup(Global.get().Vendor())));
		if (result != null) {
			return result;
		}
		markDone();
		return createvendorGroupObject(context);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createvendorGroupObject(Context context) {
		ClientVendorGroup vendorGroup = new ClientVendorGroup();
		vendorGroup.setName((String) get(VENDORGROUP_NAME).getValue());
		create(vendorGroup, context);
		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getMessages().vendorGroup(Global.get().Vendor())));

		return result;
	}

}
