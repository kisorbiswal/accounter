package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewVATCodeCommand extends AbstractVATCommand {

	private static final String DESCRIPTION = "description";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String VATITEM_FOR_SALES = "vatItemForSales";
	private static final String VATITEM_FOR_PURCHASE = "vatItemForPurchase";
	private static final String IS_ACTIVE = "isActive";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(DESCRIPTION, true, true));
		list.add(new Requirement(IS_TAXABLE, true, true));
		list.add(new Requirement(VATITEM_FOR_SALES, false, true));
		list.add(new Requirement(VATITEM_FOR_PURCHASE, false, true));
		list.add(new Requirement(IS_ACTIVE, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().vatAgency()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		setOptionalValues();

		result = nameRequirement(context, list, NAME, getConstants().vatCode(),
				getMessages().pleaseEnter(getConstants().vatCode()));
		if (result != null) {
			return result;
		}

		result = taxItemRequirement(context, list, VATITEM_FOR_SALES,
				getMessages().pleaseSelect(getConstants().salesTaxItem()),
				getConstants().salesTaxItem(), new ListFilter<ClientTAXItem>() {

					@Override
					public boolean filter(ClientTAXItem e) {
						return e.isSalesType();
					}
				});

		if (result != null) {
			return result;
		}

		result = taxItemRequirement(context, list, VATITEM_FOR_PURCHASE,
				getMessages().pleaseSelect(getConstants().purchaseTaxItem()),
				getConstants().purchaseTaxItem(),
				new ListFilter<ClientTAXItem>() {

					@Override
					public boolean filter(ClientTAXItem e) {
						return !e.isSalesType();
					}
				});
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createTaxCode(context);
	}

	private void setOptionalValues() {
		Requirement descReq = get(DESCRIPTION);
		if (descReq.getDefaultValue() == null) {
			descReq.setDefaultValue(new String());
		}

		Requirement isActiveReq = get(IS_ACTIVE);
		if (isActiveReq.getDefaultValue() == null) {
			isActiveReq.setDefaultValue(true);
		}

		Requirement isTaxableReq = get(IS_TAXABLE);
		if (isTaxableReq.getDefaultValue() == null) {
			isTaxableReq.setDefaultValue(true);
		}

	}

	/**
	 * 
	 * @param context
	 * @return
	 */

	private Result createTaxCode(Context context) {
		ClientTAXCode taxCode = new ClientTAXCode();

		String name = get(NAME).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getValue();

		taxCode.setName(name);
		taxCode.setDescription(description);
		taxCode.setTaxable(isTaxable);
		taxCode.setActive(isActive);
		if (isTaxable) {
			ClientTAXItem salesVatItem = get(VATITEM_FOR_SALES).getValue();
			ClientTAXItem purchaseVatItem = get(VATITEM_FOR_PURCHASE)
					.getValue();
			taxCode.setTAXItemGrpForSales(salesVatItem.getID());
			taxCode.setTAXItemGrpForPurchases(purchaseVatItem.getID());
		}

		create(taxCode, context);

		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(getConstants().vatCode()));

		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param actions
	 * @param makeResult
	 * @return
	 */
	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		// context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, getConstants().description(), getMessages()
						.pleaseEnter(getConstants().description()));
		if (result != null) {
			return result;
		}
		booleanOptionalRequirement(context, selection, list, IS_TAXABLE,
				getConstants().taxable(), getConstants().taxExempt());

		booleanOptionalRequirement(context, selection, list, IS_ACTIVE,
				getConstants().itemIsActive(), getConstants().itemIsInactive());

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().vatCode()));
		actions.add(finish);

		return makeResult;
	}
}
