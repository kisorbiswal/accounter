package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class SalesPersonsListCommand extends AbstractCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(ACTIVE, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createSalesPersonsList(context);
		return result;
	}

	private Result createSalesPersonsList(Context context) {
		Result result = null;
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		result = isActiveRequirement(context, selection);
		if (result != null) {
			return result;
		}

		Boolean isActive = (Boolean) get(ACTIVE).getValue();

		result = salesPersonsList(context, isActive);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result salesPersonsList(Context context, Boolean isActive) {
		ResultList list = new ResultList("values");
		Object last = context.getLast(RequirementType.TAXITEM_GROUP);
		if (last != null) {
			list.add(createSalesPersonRecord((SalesPerson) last));
		}

		List<SalesPerson> salesPersons = getSalesPersons(
				context.getHibernateSession(), isActive);
		for (int i = 0; i < VALUES_TO_SHOW && i < salesPersons.size(); i++) {
			SalesPerson salesPerson = salesPersons.get(i);
			if (salesPerson != last) {
				list.add(createSalesPersonRecord((SalesPerson) salesPerson));
			}
		}
		Result result = new Result();

		int size = list.size();
		StringBuilder message = new StringBuilder();
		if (size == 0) {
			message.append("No records to show.");
			result.add(message.toString());
			return result;
		}

		String activeString = "";
		if (isActive) {
			activeString = "Active Sales Persons";
		} else {
			activeString = "InActive Sales Persons";
		}
		result.add(activeString);
		result.add(list);

		return result;
	}

	private List<SalesPerson> getSalesPersons(Session hibernateSession,
			Boolean isActive) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createSalesPersonRecord(SalesPerson salesPerson) {
		Record record = new Record(salesPerson);
		record.add("Active", salesPerson.isActive());
		record.add("Sales Person", salesPerson.getName());
		Address address = salesPerson.getAddress();
		record.add("Address", address != null ? address.getAddress1() : "");
		record.add("City", address != null ? address.getCity() : "");
		record.add("State", address != null ? address.getStateOrProvinence()
				: "");
		record.add("Zip Code", address != null ? address.getZipOrPostalCode()
				: "");
		record.add("Phone", salesPerson.getPhoneNo());
		record.add("Fax", salesPerson.getFaxNo());
		return record;
	}

}
