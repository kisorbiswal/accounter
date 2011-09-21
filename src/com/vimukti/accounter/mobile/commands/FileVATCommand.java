package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class FileVATCommand extends AbstractVATCommand {

	private static final String FROM_DATE = null;
	private static final String TO_DATE = null;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(TAX_AGENCY, false, true));
		list.add(new Requirement(FROM_DATE, false, true));
		list.add(new Requirement(TO_DATE, false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = taxAgencyRequirement(context);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result dateRequirement(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
