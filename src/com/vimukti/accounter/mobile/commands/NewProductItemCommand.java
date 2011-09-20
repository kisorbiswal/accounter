package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewProductItemCommand extends AbstractItemCreateCommand {
	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub
		super.addRequirements(list);
		list.add(new Requirement("weight", true, true));
	}

	@Override
	protected Result weightRequirement(Context context, ResultList list,
			Object selection) {

		Requirement weightReq = get("weight");
		String weight = (String) weightReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("weight")) {
			String weigh = context.getSelection(TEXT);
			if (weigh == null) {
				weigh = context.getString();
			}
			weight = weigh;
			weightReq.setValue(weight);
		}
		if (selection == weight) {
			context.setAttribute(INPUT_ATTR, "price");
			return text(context, "Enter Weight", weight);
		}

		Record weightRecord = new Record(weight);
		weightRecord.add("Name", "Weight");
		weightRecord.add("Value", weight);
		list.add(weightRecord);

		return super.weightRequirement(context, list, selection);
	}
}
