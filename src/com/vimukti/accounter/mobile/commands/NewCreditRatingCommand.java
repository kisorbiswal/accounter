package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.web.client.core.ClientCreditRating;

public class NewCreditRatingCommand extends NewAbstractCommand {

	private static final String CREDIT_RATING_NAME = "CreditRationgName";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(CREDIT_RATING_NAME, getMessages()
				.pleaseEnter(getConstants().creditRatingName()), getConstants()
				.creditRatingName(), false, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientCreditRating creditRating = new ClientCreditRating();
		creditRating.setName(get(CREDIT_RATING_NAME).getValue().toString());
		create(creditRating, context);
		return null;

	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().creditRating());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().creditRating());
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().creditRating());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}
}
