package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CreatePartialCompanyCommand extends AbstractCompanyCommad {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		list.add(new Requirement(COMPANY_NAME, false, true));
		list.add(new Requirement(LEGAL_NAME, true, true));
		list.add(new Requirement(TAX_ID, true, true));
		list.add(new Requirement(COUNTRY, true, true));
		list.add(new Requirement(STATE, true, true));
		list.add(new Requirement(CITY, true, true));
		list.add(new Requirement(ZIPCODE, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EMAIL, true, true));
		list.add(new Requirement(WEB_SITE, true, true));
		list.add(new Requirement(TIME_ZONE, true, true));
		// Second Page
		list.add(new Requirement(INDUSTRY, false, true));
		list.add(new Requirement(ORGANIZATION_REFER, true, true));
		list.add(new Requirement(FISCAL_YEAR, true, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result makeResult = context.makeResult();
		makeResult
				.add("Company is ready to be created with following details:");
		ResultList list = new ResultList(VALUES);
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		Result result = nameRequirement(context, list, COMPANY_NAME,
				getConstants().companyName(),
				getMessages().pleaseEnter(getConstants().companyName()));
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context, getConstants().industry(),
				getIndustryList(), INDUSTRIES, get(INDUSTRY), list);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context, list, actions, makeResult);
		makeResult.add(actions);
		if (result != null) {
			return result;
		}

		return null;
	}

	private void setDefaultValues() {
		// TODO Auto-generated method stub

	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			// case FINISH_FIRST_PAGE:
			// context.removeAttribute(INPUT_ATTR);
			// return null;
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection(VALUES);

		Result result = nameRequirement(context, list, LEGAL_NAME,
				getConstants().legalCompanyName(),
				getMessages().pleaseEnter(getConstants().legalCompanyName()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, TAX_ID, getConstants().taxId(),
				getMessages().pleaseEnter(getConstants().taxId()));
		if (result != null) {
			return result;
		}

		result = listRequirement(context, getConstants().state(),
				getStatesList(), COUNTRIES, get(COUNTRY), list);
		if (result != null) {
			return result;
		}

		result = listRequirement(context, getConstants().state(),
				getCountryList(), STATES, get(STATE), list);
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, CITY, getConstants().city(),
				getMessages().pleaseEnter(getConstants().city()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, ZIPCODE, getConstants()
				.zipCode(), getMessages().pleaseEnter(getConstants().zipCode()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, PHONE, getConstants()
				.phoneNumber(),
				getMessages().pleaseEnter(getConstants().phoneNumber()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, FAX,
				getConstants().faxNumber(),
				getMessages().pleaseEnter(getConstants().faxNumber()));
		if (result != null) {
			return result;
		}

		result = emailRequirement(context, list, EMAIL, getConstants().email(),
				getMessages().pleaseEnter(getConstants().email()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(
				context,
				list,
				WEB_SITE,
				getConstants().webSite(),
				getMessages().pleaseEnter(
						getMessages().name(getConstants().webSite())));
		if (result != null) {
			return result;
		}

		result = listRequirement(context, getConstants().timezone(),
				getTimeZonesList(), TIME_ZONES, get(TIME_ZONE), list);
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context, getConstants().organisation(),
				getOrganizationTypes(), ORGANIZATION_TYPES,
				get(ORGANIZATION_REFER), list);
		if (result != null) {
			return result;
		}

		result = listRequirement(context, getConstants().fiscalYear(),
				getFiscalYearMonths(), FISCAL_YEAR_LIST, get(FISCAL_YEAR), list);
		if (result != null) {
			return result;
		}
		Record finish = new Record(ActionNames.FINISH);
		finish.add(
				"",
				getMessages().finishToCreate(
						getConstants().companyPreferences()));
		actions.add(finish);
		return makeResult;
	}

}
