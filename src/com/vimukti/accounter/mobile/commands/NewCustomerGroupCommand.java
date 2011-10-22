package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class NewCustomerGroupCommand extends AbstractTransactionCommand {
	private static final String CUSTPMERGROUP_NAME = "CustomerGroup Name";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CUSTPMERGROUP_NAME, false, true));

	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getMessages().customerGroup(Global.get().Customer())));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		Result result = nameRequirement(
				context,
				list,
				CUSTPMERGROUP_NAME,
				getMessages().customerGroup(Global.get().Customer()),
				getMessages().pleaseEnter(
						getMessages().customerGroup(Global.get().Customer())));
		if (result != null) {
			return result;
		}
		markDone();
		return createClassObject(context);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createClassObject(Context context) {
		ClientCustomerGroup customerGroup = new ClientCustomerGroup();
		customerGroup.setName((String) get(CUSTPMERGROUP_NAME).getValue());
		create(customerGroup, context);
		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getMessages().customerGroup(Global.get().Customer())));

		return result;
	}

}
