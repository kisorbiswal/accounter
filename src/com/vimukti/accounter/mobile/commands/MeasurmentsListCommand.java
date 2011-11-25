package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class MeasurmentsListCommand extends NewAbstractCommand {

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

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<Measurement>(getMessages()
				.measurement(), "", 20) {

			@Override
			protected String onSelection(Measurement value) {
				return "update Measurement #" + value.getName();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().payeeList(getMessages().measurement());
			}

			@Override
			protected String getEmptyString() {

				return getMessages()
						.youDontHaveAny(getMessages().measurement());

			}

			@Override
			protected Record createRecord(Measurement value) {
				Record classRec = new Record(value);
				classRec.add(getMessages().name(), value.getName());
				classRec.add(getMessages().description(),
						value.getDesctiption());
				classRec.add(getMessages().type(), value.getDefaultUnit()
						.getType());
				classRec.add(getMessages().factor(), value.getDefaultUnit()
						.getFactor());

				return classRec;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getMessages().measurement()));

			}

			@Override
			protected boolean filter(Measurement e, String name) {
				return false;

			}

			@Override
			protected List<Measurement> getLists(Context context) {
				return getMeasurements(context);
			}

		});

	}

	private List<Measurement> getMeasurements(Context context) {
		Set<Measurement> measurement = context.getCompany().getMeasurements();
		List<Measurement> result = new ArrayList<Measurement>(measurement);
		return result;
	}
}
