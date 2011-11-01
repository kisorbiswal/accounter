package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewFiscalYearCommand extends NewAbstractCommand {

	private static final String START_DATE = "start date";
	private static final String END_DATE = "end date";
	private static final String STATUS = "status";

	private static final String INPUT_ATTR = "input";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new DateRequirement(START_DATE, getMessages().pleaseEnter(
				getConstants().startDate()), getConstants().startDate(), false,
				true));

		list.add(new DateRequirement(END_DATE, getMessages().pleaseEnter(
				getConstants().endDate()), getConstants().endDate(), false,
				true));

		list.add(new NumberRequirement(STATUS, getMessages().pleaseEnter(
				getConstants().status()), getConstants().status(), false, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientFiscalYear fiscalYear = new ClientFiscalYear();

		ClientFinanceDate startDate = (ClientFinanceDate) get(START_DATE)
				.getValue();
		ClientFinanceDate endDate = (ClientFinanceDate) get(END_DATE)
				.getValue();
		Integer status = Integer.parseInt((String) get(STATUS).getValue());

		fiscalYear.setStartDate(startDate.getDate());
		fiscalYear.setEndDate(startDate.getDate());
		fiscalYear.setStatus(status.intValue());
		create(fiscalYear, context);

		markDone();

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Fiscal year commond is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "Fiscal year is ready with the following values";
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "new fiscal year is created successfully";
	}

}
