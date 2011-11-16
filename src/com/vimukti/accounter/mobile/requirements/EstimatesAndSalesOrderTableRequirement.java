package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;

public abstract class EstimatesAndSalesOrderTableRequirement extends
		AbstractTableRequirement<EstimatesAndSalesOrdersList> {

	public EstimatesAndSalesOrderTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
		// setDefaultValue(new ArrayList<EstimatesAndSalesOrdersList>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {

	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getConstants().estimate());
	}

	@Override
	protected void getRequirementsValues(EstimatesAndSalesOrdersList obj) {

	}

	@Override
	protected void setRequirementsDefaultValues(EstimatesAndSalesOrdersList obj) {

	}

	@Override
	protected boolean getIsCreatableObject() {
		return true;
	}

	@Override
	protected void addCreateCommands(CommandList commandList) {
		commandList.add("create new quote");
	}

	@Override
	protected EstimatesAndSalesOrdersList getNewObject() {
		return new EstimatesAndSalesOrdersList();
	}

	@Override
	protected Record createFullRecord(EstimatesAndSalesOrdersList value) {
		Record rec = new Record(value);
		if (value.getType() == ClientTransaction.TYPE_ESTIMATE)
			rec.add("", getConstants().quote());
		else
			rec.add("", getConstants().salesOrder());

		rec.add("", value.getTotal());

		return rec;
	}

	//
	// protected abstract Customer getCustomer();

	@Override
	protected Record createRecord(EstimatesAndSalesOrdersList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return "Add More Estimates";
	}
}