package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class CreditRatingListCommand extends NewAbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<CreditRating>("creditRatingList",
				getMessages().pleaseSelect(getConstants().creditRating()), 5) {
			@Override
			protected Record createRecord(CreditRating value) {
				Record record = new Record(value);
				record.add("", value.getName());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create CreditRating");
			}

			@Override
			protected boolean filter(CreditRating e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<CreditRating> getLists(Context context) {
				return getCreditRatingList(context);
			}

			@Override
			protected String getShowMessage() {
				return getConstants().creditRatingList();
			}

			@Override
			protected String getEmptyString() {
				return getConstants().noRecordsToShow();
			}

			@Override
			protected String onSelection(CreditRating value) {
				return null;
			}
		});
	}
	
	private List<CreditRating> getCreditRatingList(Context context) {
		return new ArrayList<CreditRating>(context.getCompany()
				.getCreditRatings());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

}
