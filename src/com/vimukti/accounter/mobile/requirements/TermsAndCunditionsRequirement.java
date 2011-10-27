package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class TermsAndCunditionsRequirement extends AbstractRequirement<Boolean> {

	public TermsAndCunditionsRequirement() {
		super("TermsAndConditions", null, null, true, true);
		setDefaultValue(false);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String attribute = context.getSelection(VALUES);
		if (attribute == getName()) {
			Boolean isActive = getValue();
			setValue(!isActive);
		}

		Boolean isActive = getValue();
		Record isActiveRecord = new Record(getName());
		isActiveRecord
				.add("",
						isActive ? "I Accepted Terms and conditions"
								: "Please read the Terms and Conditions (https://www.accounterlive.com/site/termsandconditions). And Accept");
		list.add(isActiveRecord);
		return null;
	}
}
