package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.web.client.core.ClientBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.server.FinanceTool;

public class FileVATCommand extends NewAbstractTransactionCommand {

	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String BOXES = "boxes";
	private static final String TAX_AGENCY = "taxAgency";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnterName(getConstants().taxAgency()), getConstants()
				.taxAgency(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().taxAgency());
			}

			@Override
			protected List<ClientTAXAgency> getLists(Context context) {
				return context.getClientCompany().gettaxAgencies();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().taxAgency());
			}

			@Override
			protected boolean filter(ClientTAXAgency e, String name) {
				return e.getName().toLowerCase().startsWith(name);
			}
		});

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getConstants().fromDate()), getConstants().fromDate(), true,
				true));

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getConstants().toDate()), getConstants().toDate(), true, true));

		list.add(new Requirement(BOXES, false, true));
		list.add(new ShowListRequirement<ClientBox>(BOXES, "", 15) {

			@Override
			protected String onSelection(ClientBox value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getConstants()
						.norecordstoshowinbetweentheselecteddates();
			}

			@Override
			protected Record createRecord(ClientBox value) {
				Record record = new Record(value);
				record.add("", getConstants().vatLine());
				record.add("", value.getName());
				record.add("", getConstants().amount());
				record.add("", value.getAmount());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {

			}

			@Override
			protected boolean filter(ClientBox e, String name) {
				return false;
			}

			@Override
			protected List<ClientBox> getLists(Context context) {
				return getBoxes(context);
			}
		});
	}

	private List<ClientBox> getBoxes(Context context) {
		ClientTAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		ClientFinanceDate toDate = get(TO_DATE).getValue();
		TAXAgency serverVatAgency;
		try {
			serverVatAgency = new ServerConvertUtil().toServerObject(
					new TAXAgency(), taxAgency, context.getHibernateSession());
			ClientTAXReturn vatReturn = new ClientConvertUtil().toClientObject(
					new FinanceTool().getTaxManager().getVATReturnDetails(
							serverVatAgency, new FinanceDate(fromDate),
							new FinanceDate(toDate),
							context.getClientCompany().getID()),
					ClientTAXReturn.class);
			return vatReturn.getBoxes();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ClientBox>();
		}

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientTAXReturn clientVATReturn = new ClientTAXReturn();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		clientVATReturn.setPeriodStartDate(fromDate.getDate());

		ClientFinanceDate toDate = get(TO_DATE).getValue();
		clientVATReturn.setPeriodStartDate(toDate.getDate());

		ClientTAXAgency taxAgency = get(TAX_AGENCY).getValue();
		clientVATReturn.setTAXAgency(taxAgency.getID());

		List<ClientBox> boxes = get(BOXES).getValue();
		clientVATReturn.setBoxes(boxes);

		create(clientVATReturn, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().fileVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().fileVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		ClientFinanceDate date = context.getClientCompany()
				.getCurrentFiscalYearStartDate();
		get(FROM_DATE).setDefaultValue(date);
		get(TO_DATE).setDefaultValue(new ClientFinanceDate());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().fileVAT());
	}
}
