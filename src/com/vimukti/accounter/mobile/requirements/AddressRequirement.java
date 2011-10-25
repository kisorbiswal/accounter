package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAddress;

public class AddressRequirement extends AbstractRequirement {

	private static final String ADDRESS_LINE_ATTR = "addressLine";
	private static final String ADDRESS_ACTIONS = "addressAction";

	public AddressRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
		setDefaultValue(new ClientAddress());
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		boolean show = false;

		Object attribute = context.getAttribute(INPUT_ATTR);
		Object object = context.getSelection(VALUES);
		if (object == getName()) {
			show = true;
		}
		if (attribute.equals(getName())) {
			show = true;
		}
		if (!isDone()) {
			show = true;
		}

		if (show) {
			context.setAttribute(INPUT_ATTR, getName());
			Result result = address(context);
			if (result != null) {
				return result;
			}
		}
		ClientAddress address = getValue();
		Record billToRecord = new Record(getName());
		billToRecord.add("", getRecordName());
		billToRecord.add("", address.getAddressString());
		list.add(billToRecord);
		return null;
	}

	protected Result address(Context context) {
		ClientAddress oldAddress = getValue();
		String lineAttr = (String) context.getAttribute(ADDRESS_LINE_ATTR);
		if (lineAttr != null) {
			String input = context.getString();
			context.removeAttribute(ADDRESS_LINE_ATTR);
			if (lineAttr.equals("address1")) {
				oldAddress.setAddress1(input);
			} else if (lineAttr.equals("city")) {
				oldAddress.setCity(input);
			} else if (lineAttr.equals("street")) {
				oldAddress.setStreet(input);
			} else if (lineAttr.equals("stateOrProvinence")) {
				oldAddress.setStateOrProvinence(input);
			} else if (lineAttr.equals("countryOrRegion")) {
				oldAddress.setCountryOrRegion(input);
			}
		} else {
			Object selection = context.getSelection(getName());
			if (selection != null) {
				if (selection == "Address1") {
					context.setAttribute(ADDRESS_LINE_ATTR, "address1");
					return show(context, "Enter Address1",
							oldAddress.getAddress1());
				} else if (selection == "City") {
					context.setAttribute(ADDRESS_LINE_ATTR, "city");
					return show(context, "Enter City", oldAddress.getCity());
				} else if (selection == "Street") {
					context.setAttribute(ADDRESS_LINE_ATTR, "street");
					return show(context, "Enter Street", oldAddress.getStreet());
				} else if (selection == "State/Provinence") {
					context.setAttribute(ADDRESS_LINE_ATTR, "stateOrProvinence");
					return show(context, "Enter State/Provinence",
							oldAddress.getStateOrProvinence());
				} else if (selection == "Country/Region") {
					context.setAttribute(ADDRESS_LINE_ATTR, "countryOrRegion");
					return show(context, "Enter Country/Region",
							oldAddress.getCountryOrRegion());
				}
			} else {
				selection = context.getSelection(ADDRESS_ACTIONS);
				if (selection == ActionNames.FINISH) {
					context.setAttribute(INPUT_ATTR, "");
					context.removeAttribute(ADDRESS_LINE_ATTR);// No need
					return null;
				}
			}
		}

		ResultList list = new ResultList(getName());
		Record record = new Record("Address1");
		record.add("", getConstants().address1());
		record.add("", oldAddress.getAddress1());
		list.add(record);

		record = new Record("Street");
		record.add("", getConstants().streetName());
		record.add("", oldAddress.getStreet());
		list.add(record);

		record = new Record("City");
		record.add("", getConstants().city());
		record.add("", oldAddress.getCity());
		list.add(record);

		record = new Record("State/Provinence");
		record.add("", getConstants().stateOrProvince());
		record.add("", oldAddress.getStateOrProvinence());
		list.add(record);

		record = new Record("Country/Region");
		record.add("", getConstants().countryRegion());
		record.add("", oldAddress.getCountryOrRegion());
		list.add(record);

		Result result = context.makeResult();
		result.add(getEnterString());

		result.add(list);
		result.add(getConstants().select());

		ResultList finish = new ResultList(ADDRESS_ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		finish.add(record);
		result.add(finish);
		return result;
	}
}
