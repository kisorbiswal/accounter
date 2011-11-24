package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTAXReturnEntry;
import com.vimukti.accounter.web.client.ui.widgets.DateUtil;
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
				.pleaseEnterName(getMessages().taxAgency()), getMessages()
				.taxAgency(), false, true, new ChangeListner<TAXAgency>() {

			@Override
			public void onSelection(TAXAgency value) {
				taxAgencySelected(value);
			}
		}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().taxAgency());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(context.getCompany()
						.getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().taxAgency());
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().toLowerCase().startsWith(name);
			}
		});

		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (getPreferences().isEnableMultiCurrency()) { return
		 * super.run(context, makeResult, list, actions); } else { return null;
		 * } }
		 * 
		 * @Override protected List<Currency> getLists(Context context) { return
		 * new ArrayList<Currency>(context.getCompany() .getCurrencies()); } });
		 * 
		 * list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
		 * .pleaseSelect(getConstants().currency()), getConstants() .currency(),
		 * false, true) {
		 * 
		 * @Override protected String getDisplayValue(Double value) {
		 * ClientCurrency primaryCurrency = getPreferences()
		 * .getPrimaryCurrency(); Currency selc = get(CURRENCY).getValue();
		 * return "1 " + selc.getFormalName() + " = " + value + " " +
		 * primaryCurrency.getFormalName(); }
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if (get(CURRENCY).getValue()
		 * != null) { if (getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue()) .equals(getPreferences()
		 * .getPrimaryCurrency())) { return super.run(context, makeResult, list,
		 * actions); } } return null;
		 * 
		 * } });
		 */

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().fromDate()), getMessages().fromDate(), true, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				fromDateChanged((ClientFinanceDate) value);
			}
		});

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().toDate()), getMessages().toDate(), true, true));

		list.add(new ShowListRequirement<ClientTAXReturnEntry>(BOXES, "", 15) {

			@Override
			protected String onSelection(ClientTAXReturnEntry value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().norecordstoshowinbetweentheselecteddates();
			}

			@Override
			protected Record createRecord(ClientTAXReturnEntry value) {
				Record record = new Record(value);
				record.add("", getMessages().vatLine());
				record.add("", value.getName());
				record.add("", getMessages().amount());
				record.add("", value.getTaxAmount());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {

			}

			@Override
			protected boolean filter(ClientTAXReturnEntry e, String name) {
				return false;
			}

			@Override
			protected List<ClientTAXReturnEntry> getLists(Context context) {
				return getClientTAXReturnEntries(context);
			}
		});
	}

	protected void taxAgencySelected(TAXAgency taxAgency) {
		get(BOXES).setValue(new ArrayList<ClientTAXReturnEntry>());
		long lastTaxReturnEndDate = new FinanceTool().getTaxManager()
				.getLastTaxReturnEndDate(taxAgency.getID(), getCompanyId());
		ClientFinanceDate clientFinanceDate;
		if (lastTaxReturnEndDate != 0) {
			clientFinanceDate = new ClientFinanceDate(lastTaxReturnEndDate);
		} else {
			clientFinanceDate = new ClientFinanceDate(
					DateUtil.getCurrentMonthFirstDate());
		}
		get(FROM_DATE).setValue(clientFinanceDate);
	}

	protected void fromDateChanged(ClientFinanceDate value) {
		Date date = value.getDateAsObject();
		int frequency = ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY;
		TAXAgency selectedTaxAgency = get(TAX_AGENCY).getValue();
		if (selectedTaxAgency != null) {
			frequency = selectedTaxAgency.getTAXFilingFrequency();
		}
		date = DateUtil.getMonthFirstDay(date);
		switch (frequency) {
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY:
			date.setMonth(date.getMonth() + 1);
			break;
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_QUARTERLY:
			date.setMonth(date.getMonth() + 3);
			break;
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_HALF_YEARLY:
			date.setMonth(date.getMonth() + 6);
			break;
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_YEARLY:
			date.setMonth(date.getMonth() + 12);
			break;
		}
		date.setDate(date.getDate() - 1);
		get(TO_DATE).setValue(new ClientFinanceDate(date));
	}

	private List<ClientTAXReturnEntry> getClientTAXReturnEntries(Context context) {
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		ClientFinanceDate toDate = get(TO_DATE).getValue();
		try {
			List<ClientTAXReturnEntry> taxReturnEntries = new FinanceTool()
					.getTaxManager().getTAXReturnEntries(getCompanyId(),
							taxAgency.getID(), fromDate.getDate(),
							toDate.getDate());
			get(BOXES).setValue(taxReturnEntries);
			return taxReturnEntries;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ClientTAXReturnEntry>();
		}
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientTAXReturn clientVATReturn = new ClientTAXReturn();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		clientVATReturn.setPeriodStartDate(fromDate.getDate());

		ClientFinanceDate toDate = get(TO_DATE).getValue();
		clientVATReturn.setPeriodEndDate(toDate.getDate());
		clientVATReturn.setTransactionDate(new ClientFinanceDate().getDate());
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		clientVATReturn.setTAXAgency(taxAgency.getID());

		List<ClientTAXReturnEntry> taxReturnEntries = get(BOXES).getValue();
		clientVATReturn.setTaxReturnEntries(taxReturnEntries);

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * clientVATReturn.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * clientVATReturn.setCurrencyFactor(factor); }
		 */
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
		return getMessages().creating(getMessages().fileVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().fileVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		Date currentMonthFirstDate = DateUtil.getCurrentMonthFirstDate();
		get(FROM_DATE).setDefaultValue(
				new ClientFinanceDate(currentMonthFirstDate));
		get(TO_DATE).setDefaultValue(new ClientFinanceDate());
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().fileVAT());
	}
}
