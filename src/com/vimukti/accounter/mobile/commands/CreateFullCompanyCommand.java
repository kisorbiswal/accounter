//package com.vimukti.accounter.mobile.commands;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.vimukti.accounter.main.ServerLocal;
//import com.vimukti.accounter.mobile.ActionNames;
//import com.vimukti.accounter.mobile.Context;
//import com.vimukti.accounter.mobile.Record;
//import com.vimukti.accounter.mobile.Requirement;
//import com.vimukti.accounter.mobile.Result;
//import com.vimukti.accounter.mobile.ResultList;
//import com.vimukti.accounter.web.client.core.AccountsTemplate;
//import com.vimukti.accounter.web.server.AccountsTemplateManager;
//
//public class CreateFullCompanyCommand extends AbstractCommand {
//	private static final String COMPANY_NAME = "companyName";
//	private static final String LEGAL_NAME = "legalName";
//	private static final String TAX_ID = "taxId";
//	private static final String STATE = "state";
//	private static final String CITY = "city";
//	private static final String ZIPCODE = "zipcode";
//	private static final String FAX = "fax";
//	private static final String WEB_SITE = "website";
//	private static final String TIME_ZONE = "timezone";
//	private static final String INDUSTRY = "industry";
//	private static final String ORGANIZATION_REFER = "organizationrefer";
//	private static final String CUSTOMER_TERMINOLOGY = "customerterm";
//	private static final String SUPPLIER_TERMINOLOGY = "vendorterm";
//	private static final String ACCOUNT_TERMINOLOGY = "accountterm";
//	private static final String SERVICE_PRODUCTS_BOTH = "serprobothlist";
//	private static final String TRACK_TAX = "trackTax";
//	private static final String CREATE_ESTIMATES = "createestimates";
//	private static final String SELECT_CURRENCY = "selectcurrency";
//	private static final String MANAGE_BILLS_OWE = "managebills";
//	private static final String FISCAL_YEAR = "fiscalyear";
//	private static final String ACCOUNTS = "defaultaccounts";
//
//	private static final String VALUES = "values";
//
//	@Override
//	public String getId() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected void addRequirements(List<Requirement> list) {
//		// First page
//		list.add(new Requirement(COMPANY_NAME, false, true));
//		list.add(new Requirement(LEGAL_NAME, true, true));
//		list.add(new Requirement(TAX_ID, true, true));
//		list.add(new Requirement(COUNTRY, true, true));
//		list.add(new Requirement(STATE, true, true));
//		list.add(new Requirement(CITY, true, true));
//		list.add(new Requirement(ZIPCODE, true, true));
//		list.add(new Requirement(PHONE, true, true));
//		list.add(new Requirement(FAX, true, true));
//		list.add(new Requirement(EMAIL, true, true));
//		list.add(new Requirement(WEB_SITE, true, true));
//		list.add(new Requirement(TIME_ZONE, true, true));
//
//		// Second Page
//		list.add(new Requirement(INDUSTRY, false, true));
//		list.add(new Requirement(ORGANIZATION_REFER, true, true));
//		list.add(new Requirement(CUSTOMER_TERMINOLOGY, true, true));
//		list.add(new Requirement(SUPPLIER_TERMINOLOGY, true, true));
//		list.add(new Requirement(ACCOUNT_TERMINOLOGY, true, true));
//		list.add(new Requirement(SERVICE_PRODUCTS_BOTH, true, true));
//		list.add(new Requirement(TRACK_TAX, true, true));
//		list.add(new Requirement(CREATE_ESTIMATES, true, true));
//		list.add(new Requirement(SELECT_CURRENCY, true, true));
//		list.add(new Requirement(MANAGE_BILLS_OWE, true, true));
//		list.add(new Requirement(FISCAL_YEAR, true, true));
//		list.add(new Requirement(ACCOUNTS, true, true));
//	}
//
//	public Result test() {
//		Context context = null;
//		Result makeResult = null;
//		ResultList list = null;
//		ResultList actions = null;
//
//		List<Requirement> allRequirements = new ArrayList<Requirement>();
//		for (Requirement req : allRequirements) {
//			Result result = req.run(context, makeResult, list, actions);
//			if (result != null) {
//				return result;
//			}
//		}
//		return makeResult;
//	}
//
//	@Override
//	public Result run(Context context) {
//		setDefaultValues();
//		if (context.getAttribute(INPUT_ATTR) == null) {
//			context.setAttribute(INPUT_ATTR, "optional");
//		}
//
//		Result makeResult = context.makeResult();
//		makeResult
//				.add("Company is ready to be created with following details:");
//		ResultList list = new ResultList(VALUES);
//		makeResult.add(list);
//		ResultList actions = new ResultList(ACTIONS);
//
//		Result result = nameRequirement(context, list, COMPANY_NAME,
//				getConstants().companyName(),
//				getMessages().pleaseEnter(getConstants().companyName()));
//		if (result != null) {
//			return result;
//		}
//
//		result = createOptionalRequirement(context, list, actions, makeResult);
//		makeResult.add(actions);
//		if (result != null) {
//			return result;
//		}
//
//		return null;
//	}
//
//	private Result createOptionalRequirement(Context context, ResultList list,
//			ResultList actions, Result makeResult) {
//		Object selection = context.getSelection(ACTIONS);
//		if (selection != null) {
//			ActionNames actionName = (ActionNames) selection;
//			switch (actionName) {
//			case FINISH_FIRST_PAGE:
//				context.removeAttribute(INPUT_ATTR);
//				return null;
//			case FINISH:
//				context.removeAttribute(INPUT_ATTR);
//				return null;
//			default:
//				break;
//			}
//		}
//		selection = context.getSelection(VALUES);
//
//		Result result = nameRequirement(context, list, LEGAL_NAME,
//				getConstants().legalCompanyName(),
//				getMessages().pleaseEnter(getConstants().legalCompanyName()));
//		if (result != null) {
//			return result;
//		}
//
//		result = nameRequirement(context, list, TAX_ID, getConstants().taxId(),
//				getMessages().pleaseEnter(getConstants().taxId()));
//		if (result != null) {
//			return result;
//		}
//
//		result = listRequirement(context, getConstants().state(),
//				getStatesList(), COUNTRIES, get(COUNTRY), list);
//		if (result != null) {
//			return result;
//		}
//
//		result = listRequirement(context, getConstants().state(),
//				getCountryList(), STATES, get(STATE), list);
//		if (result != null) {
//			return result;
//		}
//
//		result = nameRequirement(context, list, CITY, getConstants().city(),
//				getMessages().pleaseEnter(getConstants().city()));
//		if (result != null) {
//			return result;
//		}
//
//		result = nameRequirement(context, list, ZIPCODE, getConstants()
//				.zipCode(), getMessages().pleaseEnter(getConstants().zipCode()));
//		if (result != null) {
//			return result;
//		}
//
//		result = nameRequirement(context, list, PHONE, getConstants()
//				.phoneNumber(),
//				getMessages().pleaseEnter(getConstants().phoneNumber()));
//		if (result != null) {
//			return result;
//		}
//
//		result = nameRequirement(context, list, FAX,
//				getConstants().faxNumber(),
//				getMessages().pleaseEnter(getConstants().faxNumber()));
//		if (result != null) {
//			return result;
//		}
//
//		result = emailRequirement(context, list, EMAIL, getConstants().email(),
//				getMessages().pleaseEnter(getConstants().email()));
//		if (result != null) {
//			return result;
//		}
//
//		result = nameRequirement(
//				context,
//				list,
//				WEB_SITE,
//				getConstants().webSite(),
//				getMessages().pleaseEnter(
//						getMessages().name(getConstants().webSite())));
//		if (result != null) {
//			return result;
//		}
//
//		result = listRequirement(context, getConstants().timezone(),
//				getTimeZonesList(), TIME_ZONES, get(TIME_ZONE), list);
//		if (result != null) {
//			return result;
//		}
//
//		result = numberListRequirement(context, getConstants().organisation(),
//				getOrganizationTypes(), ORGANIZATION_TYPES,
//				get(ORGANIZATION_REFER), list);
//		if (result != null) {
//			return result;
//		}
//
//		result = numberListRequirement(context,
//				getMessages().terminology(getConstants().Customer()),
//				getCustomerTerminologies(), CUSTOMER_TERMINOLOGIES,
//				get(CUSTOMER_TERMINOLOGY), list);
//		if (result != null) {
//			return result;
//		}
//
//		result = numberListRequirement(context,
//				getMessages().terminology(getConstants().Supplier()),
//				getSupplierTerminologies(), SUPPLIER_TERMINOLOGIES,
//				get(SUPPLIER_TERMINOLOGY), list);
//		if (result != null) {
//			return result;
//		}
//
//		result = numberListRequirement(context,
//				getMessages().terminology(getConstants().account()),
//				getAccountTerminologies(), ACCOUNT_TERMINOLOGIES,
//				get(ACCOUNT_TERMINOLOGY), list);
//		if (result != null) {
//			return result;
//		}
//
//		result = numberListRequirement(context, "Services/Products",
//				getServiceProductBothList(), SERVICE_PRODUCTS_BOTH,
//				get(SERVICE_PRODUCTS), list);
//		if (result != null) {
//			return result;
//		}
//		selection = context.getSelection(VALUES);
//		booleanOptionalRequirement(context, selection, list, TRACK_TAX,
//				"Track Tax Enabled", "Track Tax Disabled");
//
//		Boolean isEnabled = get(TRACK_TAX).getValue();
//		if (isEnabled) {
//			booleanOptionalRequirement(context, selection, list,
//					ONE_PER_TRANSACTION, "One per transaction",
//					"One per detail line");
//			booleanOptionalRequirement(context, selection, list, TRACK_TAX_PAD,
//					"Enabled Tracking Tax Paid", "Disabled Tracking Tax Paid");
//		}
//
//		booleanOptionalRequirement(context, selection, list, CREATE_ESTIMATES,
//				"Yes,want to create estimates",
//				"No,don't want to create estimates");
//
//		result = currencyRequirement(context, list);
//		if (result != null) {
//			return result;
//		}
//
//		booleanOptionalRequirement(context, selection, list, MANAGE_BILLS_OWE,
//				"Manage Bills", "Don't Manage Bills");
//
//		result = listRequirement(context, getConstants().fiscalYear(),
//				getFiscalYearMonths(), FISCAL_YEAR_LIST, get(FISCAL_YEAR), list);
//		if (result != null) {
//			return result;
//		}
//
//		if (get(INDUSTRY).isDone()) {
//			result = defaultAccountsRequirement(context, actions, makeResult);
//			if (result != null) {
//				return result;
//			}
//		}
//		Record finish = new Record(ActionNames.FINISH);
//		finish.add(
//				"",
//				getMessages().finishToCreate(
//						getConstants().companyPreferences()));
//		actions.add(finish);
//		return makeResult;
//	}
//
//	private Result listRequirement(Context context, String name,
//			List<String> displayList, String selectionName,
//			Requirement requirement, ResultList list) {
//		String input = (String) context.getSelection(selectionName);
//		if (input != null) {
//			requirement.setValue(input);
//		}
//		Object selection = context.getSelection("values");
//		if (!requirement.isDone()) {
//			return showList(context, name, null, displayList, selectionName,
//					requirement);
//		} else {
//			if (requirement.getName() == INDUSTRY) {
//				get(ACCOUNTS).setDefaultValue(getDefaultTemplateAccounts());
//			}
//		}
//
//		String requirementvalue = requirement.getValue();
//		if (selection != null && selection.equals(requirement.getName())) {
//			return showList(context, name, requirementvalue, displayList,
//					selectionName, requirement);
//		}
//
//		Record nameRecord = new Record(requirement.getName());
//		nameRecord.add("", name);
//		nameRecord.add("", requirementvalue);
//		list.add(nameRecord);
//		return null;
//	}
//
//	private void setDefaultValues() {
//		// TODO Auto-generated method stub
//
//	}
//
// }
