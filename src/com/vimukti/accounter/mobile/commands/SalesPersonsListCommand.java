package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientVendor;

public class SalesPersonsListCommand extends AbstractCommand {
	private static final String SALESPERSON_TYPE = "salespersonType";
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createSalesPersonsList(context);
		return result;
	}

	private Result createSalesPersonsList(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case ACTIVE:
				context.setAttribute(SALESPERSON_TYPE, true);
				break;
			case IN_ACTIVE:
				context.setAttribute(SALESPERSON_TYPE, false);
				break;
			case ALL:
				context.setAttribute(SALESPERSON_TYPE, null);
				break;
			default:
				break;
			}
		}
		Result result = salesPersonsList(context, selection);
		return result;
	}

	private Result salesPersonsList(Context context,  ActionNames selection) {
		Result result = context.makeResult();
		ResultList salesPersonsList = new ResultList("salespersonsList");
		result.add("Sales Persons List");

		Boolean salesPersonsType = (Boolean) context.getAttribute(SALESPERSON_TYPE);
		List<ClientSalesPerson> salesPersons = getSalesPersons(salesPersonsType);

		ResultList actions = new ResultList("actions");

		List<ClientSalesPerson> pagination = pagination(context, selection, actions,
				salesPersons, new ArrayList<ClientSalesPerson>(), VALUES_TO_SHOW);

		for (ClientSalesPerson salesPrson : pagination) {
			salesPersonsList.add(createSalesPersonRecord(salesPrson));
		}

		StringBuilder message = new StringBuilder();
		if (salesPersonsList.size() > 0) {
			message.append(getMessages().pleaseSelect(getConstants().salesPerson()));
		}

		result.add(message.toString());
		result.add(salesPersonsList);

		Record inActiveRec = new Record(ActionNames.ACTIVE);
		inActiveRec.add("", getConstants().active());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.IN_ACTIVE);
		inActiveRec.add("",  getConstants().inActive());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("",  getConstants().all());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().add()+" " +getConstants().salesPerson());
		result.add(commandList);
		return result;

	}

	private List<ClientSalesPerson> getSalesPersons(
			Boolean isActive) {
		ArrayList<ClientSalesPerson> salesPersons = getClientCompany().getSalesPersons();
		if (isActive == null) {
			return salesPersons;
		}
		ArrayList<ClientSalesPerson> result = new ArrayList<ClientSalesPerson>();
		for (ClientSalesPerson salesPerson : salesPersons) {
			if (salesPerson.isActive() == isActive) {
				result.add(salesPerson);
			}
		}
		return result;
	}

	private Record createSalesPersonRecord(ClientSalesPerson salesPrson) {
		Record record = new Record(salesPrson);
		record.add("Active", salesPrson.isActive() ?getConstants().active():getConstants().inActive());
		record.add("Sales Person", salesPrson.getName());
		ClientAddress address = salesPrson.getAddress();
		record.add("Address", address != null ? address.getAddress1() : "");
		record.add("City", address != null ? address.getCity() : "");
		record.add("State", address != null ? address.getStateOrProvinence()
				: "");
		record.add("Zip Code", address != null ? address.getZipOrPostalCode()
				: "");
		record.add("Phone", salesPrson.getPhoneNo());
		record.add("Fax", salesPrson.getFaxNo());
		return record;
	}

}
