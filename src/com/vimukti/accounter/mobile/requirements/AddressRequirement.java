package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAddress;

public class AddressRequirement extends AbstractRequirement<ClientAddress> {

	private static final String ADDRESS_LINE_ATTR = "addressLine";
	private static final String ADDRESS_ACTIONS = "addressAction";
	private static final Object ADDRESS1 = "address1";
	private static final Object CITY = "city";
	private static final Object STREET = "street";
	private static final Object STATE = "stateOrProvinence";
	private static final Object COUNTRY = "countryOrRegion";

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
			if (isEditable()) {
				context.setAttribute(INPUT_ATTR, getName());
				Result result = address(context);
				if (result != null) {
					return result;
				}
			} else {
				addFirstMessage(context,
						getMessages().youCantEdit(getRecordName()));
			}
		}
		ClientAddress address = getValue();
		Record billToRecord = new Record(getName());
		billToRecord.add(getRecordName(), address.getAddressString());
		list.add(billToRecord);
		return null;
	}

	protected Result address(Context context) {
		ClientAddress oldAddress = getValue();
		String lineAttr = (String) context.getAttribute(ADDRESS_LINE_ATTR);
		if (lineAttr != null) {
			String input = context.getString();
			context.removeAttribute(ADDRESS_LINE_ATTR);
			if (lineAttr.equals(ADDRESS1)) {
				oldAddress.setAddress1(input);
			} else if (lineAttr.equals(CITY)) {
				oldAddress.setCity(input);
			} else if (lineAttr.equals(STREET)) {
				oldAddress.setStreet(input);
			} else if (lineAttr.equals(STATE)) {
				oldAddress.setStateOrProvinence(input);
			} else if (lineAttr.equals(COUNTRY)) {
				oldAddress.setCountryOrRegion(input);
			}
			setValue(oldAddress);
		} else {
			Object selection = context.getSelection(getName());
			if (selection != null) {
				if (selection == ADDRESS1) {
					context.setAttribute(ADDRESS_LINE_ATTR, ADDRESS1);
					return show(
							context,
							getMessages().pleaseEnter(getMessages().address1()),
							oldAddress.getAddress1(), oldAddress.getAddress1());
				} else if (selection == CITY) {
					context.setAttribute(ADDRESS_LINE_ATTR, CITY);
					return show(context,
							getMessages().pleaseEnter(getMessages().city()),
							oldAddress.getCity(), oldAddress.getCity());
				} else if (selection == STREET) {
					context.setAttribute(ADDRESS_LINE_ATTR, STREET);
					return show(
							context,
							getMessages().pleaseEnter(
									getMessages().streetName()),
							oldAddress.getStreet(), oldAddress.getStreet());
				} else if (selection == STATE) {
					context.setAttribute(ADDRESS_LINE_ATTR, STATE);
					return show(
							context,
							getMessages().pleaseEnter(
									getMessages().stateOrProvince()),
							oldAddress.getStateOrProvinence(),
							oldAddress.getStateOrProvinence());
				} else if (selection == COUNTRY) {
					context.setAttribute(ADDRESS_LINE_ATTR, COUNTRY);
					return show(
							context,
							getMessages().pleaseEnter(
									getMessages().countryRegion()),
							oldAddress.getCountryOrRegion(),
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
		Record record = new Record(ADDRESS1);
		record.add("", getMessages().address1());
		record.add("", oldAddress.getAddress1());
		list.add(record);

		record = new Record(STREET);
		record.add("", getMessages().streetName());
		record.add("", oldAddress.getStreet());
		list.add(record);

		record = new Record(CITY);
		record.add("", getMessages().city());
		record.add("", oldAddress.getCity());
		list.add(record);

		record = new Record(STATE);
		record.add("", getMessages().stateOrProvince());
		record.add("", oldAddress.getStateOrProvinence());
		list.add(record);

		record = new Record(COUNTRY);
		record.add("", getMessages().countryRegion());
		record.add("", oldAddress.getCountryOrRegion());
		list.add(record);

		Result result = context.makeResult();
		result.add(getEnterString());

		result.add(list);
		result.add(getMessages().select());

		ResultList finish = new ResultList(ADDRESS_ACTIONS);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		finish.add(record);
		result.add(finish);
		return result;
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_STRING);
	}
}
