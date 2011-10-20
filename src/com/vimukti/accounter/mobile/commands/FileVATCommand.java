package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class FileVATCommand extends AbstractVATCommand {

	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String BOXES = "boxes";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(TAX_AGENCY, false, true));
		list.add(new Requirement(FROM_DATE, true, true));
		list.add(new Requirement(TO_DATE, true, true));
		list.add(new Requirement(BOXES, false, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();

		setDefaultValues();

		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().fileVAT()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = taxAgencyRequirement(context, list, TAX_AGENCY);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createFileVat(context);
	}

	private Result createFileVat(Context context) {
		ClientVATReturn clientVATReturn = new ClientVATReturn();
		Date fromDate = get(FROM_DATE).getValue();
		clientVATReturn.setVATperiodStartDate(fromDate.getTime());

		Date toDate = get(TO_DATE).getValue();
		clientVATReturn.setVATperiodStartDate(toDate.getTime());

		ClientTAXAgency taxAgency = get(TAX_AGENCY).getValue();
		clientVATReturn.setTaxAgency(taxAgency.getID());

		List<ClientBox> boxes = get(BOXES).getValue();
		clientVATReturn.setBoxes(boxes);

		create(clientVATReturn, context);

		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().fileVATReturn()));

		return result;
	}

	private void setDefaultValues() {
		ClientFinanceDate date = getClientCompany()
				.getCurrentFiscalYearStartDate();
		get(FROM_DATE).setDefaultValue(date.getDateAsObject());
		get(TO_DATE).setDefaultValue(new Date());
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Result result = dateOptionalRequirement(context, list, FROM_DATE,
				getMessages().pleaseEnter(getConstants().fromDate()), selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, TO_DATE, getMessages()
				.pleaseEnter(getConstants().endDate()), selection);
		if (result != null) {
			return result;
		}

		ClientTAXAgency taxAgency = get(TAX_AGENCY).getValue();
		Date fromDate = get(FROM_DATE).getValue();
		Date toDate = get(TO_DATE).getValue();

		makeResult.add("VAT Line:-");
		List<ClientBox> boxes2 = new ArrayList<ClientBox>();
		try {
			boxes2 = getBoxes(taxAgency, fromDate, toDate, context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ClientBox box : boxes2) {
			makeResult.add(createBoxRecord(box));
		}

		Requirement boxesReq = get(BOXES);
		boxesReq.setValue(boxes2);

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to File vat.");
		actions.add(finish);

		return makeResult;
	}

	private String createBoxRecord(ClientBox box) {
		StringBuffer record = new StringBuffer();
		record.append(box.getName() + "  ");
		record.append(box.getAmount());
		return record.toString();
	}

	private List<ClientBox> getBoxes(ClientTAXAgency taxAgency, Date fromDate,
			Date toDate, Context context) throws Exception {
		TAXAgency serverVatAgency = new ServerConvertUtil().toServerObject(
				new TAXAgency(), taxAgency, context.getHibernateSession());

		ClientVATReturn vatReturn = new ClientConvertUtil().toClientObject(
				new FinanceTool().getTaxManager().getVATReturnDetails(
						serverVatAgency, new FinanceDate(fromDate),
						new FinanceDate(toDate), getClientCompany().getID()),
				ClientVATReturn.class);
		return vatReturn.getBoxes();
	}

}
