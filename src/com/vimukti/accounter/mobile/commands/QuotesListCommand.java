package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class QuotesListCommand extends NewAbstractCommand {
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
		list.add(new ActionRequirement(VIEW_BY, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimateType == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().open());
				list.add(getMessages().rejected());
				list.add(getMessages().accepted());
				list.add(getMessages().expired());
				list.add(getMessages().all());
				return list;
			}
		});

		list.add(new ShowListRequirement<Estimate>("Estimates",
				"Please select.", 20) {

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// Estimate value) {
			//
			// if (estimateType == ClientEstimate.QUOTES) {
			// commandList.add(new UserCommand("Void transaction", value
			// .getType() + " " + value.getID()));
			// commandList.add(new UserCommand("Update Estimate ", value
			// .getNumber()));
			// } else if (estimateType == ClientEstimate.CREDITS) {
			// commandList.add(new UserCommand("Update Credit", value
			// .getNumber()));
			// commandList.add(new UserCommand("Delete Credit", value
			// .getID()));
			// } else if (estimateType == ClientEstimate.CHARGES) {
			// commandList.add(new UserCommand("Delete transaction", value
			// .getNumber()));
			// commandList.add(new UserCommand("Update Charge", value
			// .getID()));
			// }
			// }

			@Override
			protected String onSelection(Estimate value) {
				if (estimateType == ClientEstimate.QUOTES) {
					return "Edit Transaction " + value.getID();
				} else if (estimateType == ClientEstimate.CREDITS) {
					return "Edit Transaction " + value.getID();
				} else if (estimateType == ClientEstimate.CHARGES) {
					return "Edit Transaction " + value.getID();
				}
				return "";
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
				estrecord.add("", value.getDate());
				estrecord.add("", value.getNumber());
				estrecord.add("", value.getCustomer().getName() != null ? value
						.getCustomer().getName() : "");
				estrecord.add("", value.getExpirationDate().toString());
				estrecord.add("", value.getTotal());
				return estrecord;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				if (estimateType == ClientEstimate.QUOTES) {
					list.add("Create Quote");
				} else if (estimateType == ClientEstimate.CREDITS) {
					list.add("Create Charge");
				} else if (estimateType == ClientEstimate.CHARGES) {
					list.add("Create Credit");
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
					context.getCompany().getID(), estimateType);
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
}