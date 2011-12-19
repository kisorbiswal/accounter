package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class QuotesListCommand extends AbstractTransactionListCommand {
	int estimateType;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		estimateType = ClientEstimate.QUOTES;
		String commandString = context.getCommandString().toLowerCase();
		if (commandString.contains("charges")) {
			estimateType = ClientEstimate.CHARGES;
		} else if (commandString.contains("credits")) {
			estimateType = ClientEstimate.CREDITS;
		}

		if (commandString.contains("quote")) {
			if (!context.getPreferences().isDelayedchargesEnabled()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		} else {
			if (!context.getPreferences().isDoyouwantEstimates()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		}

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
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		if (estimateType == ClientEstimate.QUOTES) {
			return "Success" + getMessages().quotesList();
		} else if (estimateType == ClientEstimate.CREDITS) {
			return "Success" + getMessages().creditsList();
		} else if (estimateType == ClientEstimate.CHARGES) {
			return "Success" + getMessages().chargesList();
		}
		return "";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new ShowListRequirement<Estimate>("Estimates",
				"Please select.", 20) {

			@Override
			protected String onSelection(Estimate value) {
				return "editTransaction " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				if (estimateType == ClientEstimate.QUOTES) {
					return getMessages().quotesList();
				} else if (estimateType == ClientEstimate.CREDITS) {
					return getMessages().creditsList();
				} else if (estimateType == ClientEstimate.CHARGES) {
					return getMessages().chargesList();
				}
				return "";
			}

			@Override
			protected String getEmptyString() {
				if (estimateType == ClientEstimate.QUOTES) {
					return getMessages().youDontHaveAny(getMessages().quotes());
				} else if (estimateType == ClientEstimate.CREDITS) {
					return getMessages()
							.youDontHaveAny(getMessages().credits());
				} else if (estimateType == ClientEstimate.CHARGES) {
					return getMessages()
							.youDontHaveAny(getMessages().Charges());
				}
				return "";
			}

			@Override
			protected Record createRecord(Estimate value) {
				Record estrecord = new Record(value);
				estrecord.add(getMessages().date(), value.getDate());
				estrecord.add(getMessages().number(), value.getNumber());
				estrecord.add("Name",
						value.getCustomer().getName() != null ? value
								.getCustomer().getName() : "");
				estrecord.add(getMessages().expirationDate(), value
						.getExpirationDate().toString());
				estrecord.add(getMessages().total(), value.getCurrency()
						.getSymbol() + " " + value.getTotal());
				return estrecord;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				if (estimateType == ClientEstimate.QUOTES) {
					list.add("createQuote");
				} else if (estimateType == ClientEstimate.CHARGES) {
					list.add("newCharge");
				} else if (estimateType == ClientEstimate.CREDITS) {
					list.add("newCredit");
				}

			}

			@Override
			protected boolean filter(Estimate e, String name) {
				return e.getCustomer().getName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Estimate> getLists(Context context) {
				return getEstimates(context);
			}

		});

	}

	private List<Estimate> getEstimates(Context context) {
		String viewType = get(VIEW_BY).getValue();
		List<Estimate> result = new ArrayList<Estimate>();
		List<Estimate> data = null;
		try {
			data = new FinanceTool().getCustomerManager().getEstimates(
					context.getCompany().getID(), estimateType,
					new FinanceDate(getStartDate()),
					new FinanceDate(getEndDate()));
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (estimateType == ClientEstimate.QUOTES) {
			for (Estimate e : data) {
				if (viewType.equals(getMessages().open())) {
					if (e.getStatus() == Estimate.STATUS_OPEN)
						result.add(e);

				} else if (viewType.equals(getMessages().accepted())) {
					if (e.getStatus() == Estimate.STATUS_ACCECPTED) {
						result.add(e);
					}
				} else if (viewType.equals(getMessages().rejected())) {
					if (e.getStatus() == Estimate.STATUS_REJECTED) {
						result.add(e);
					}
				} else if (viewType.equals(getMessages().all())) {
					result.add(e);

				}
			}
		} else {
			result = data;
		}
		return result;

	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().rejected());
		list.add(getMessages().accepted());
		list.add(getMessages().expired());
		list.add(getMessages().all());
		return list;
	}
}