package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class QuotesListCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
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
		get(VIEW_BY).setDefaultValue(getConstants().open());

	}

	@Override
	public String getSuccessMessage() {

		return "Success" + getConstants().quotesList();

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ActionRequirement(VIEW_BY, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().open());
				list.add(getConstants().rejected());
				list.add(getConstants().accepted());
				list.add(getConstants().expired());
				list.add(getConstants().all());
				return list;
			}
		});

		list.add(new ShowListRequirement<Estimate>("Estimates",
				"Please select.", 10) {

			@Override
			protected String onSelection(Estimate value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getShowMessage() {

				return getConstants().quotesList();
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(getConstants().quotes());
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
				list.add("Create Quote");

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
					context.getClientCompany().getID(), 1);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		for (Estimate e : data) {
			if (viewType.equals(getConstants().open())) {
				if (e.getStatus() == Estimate.STATUS_OPEN)
					result.add(e);

			} else if (viewType.equals(getConstants().accepted())) {
				if (e.getStatus() == Estimate.STATUS_ACCECPTED) {
					result.add(e);
				}
			} else if (viewType.equals(getConstants().rejected())) {
				if (e.getStatus() == Estimate.STATUS_REJECTED) {
					result.add(e);
				}
			} else if (viewType.equals(getConstants().all())) {
				result.add(e);

			}
		}

		return result;

	}
}