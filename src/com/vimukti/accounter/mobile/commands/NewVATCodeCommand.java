package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;

public class NewVATCodeCommand extends AbstractVATCommand {

	private static final String DESCRIPTION = "description";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String VATITEM_FOR_SALES = "vatItemForSales";
	private static final String VATITEM_FOR_PURCHASE = "vatItemForPurchase";
	private static final String IS_ACTIVE = "isActive";
	private static final String SALES_VAT_ITEMS = "salesVatItems";
	private static final String PURCHASE_VAT_ITEMS = "purchaseVatItems";

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
		makeResult.add("VatAgency  is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		setOptionalValues();

		result = nameRequirement(context, list, NAME, "Enter Vatcode");
		if (result != null) {
			return result;
		}

		result = vatItemForSalesRequirement(context, list, VATITEM_FOR_SALES,
				"Please Select the Sales Vat Item.");
		if (result != null) {
			return result;
		}

		result = vatItemForPurchaseRequirement(context, list,
				VATITEM_FOR_PURCHASE, "Please Select the Purchase Vat Item.");
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
		result.add("VAT Code was created successfully.");

		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param vatitemForPurchase
	 * @param dispalyname
	 * @return
	 */
	private Result vatItemForPurchaseRequirement(Context context,
			ResultList list, String vatitemForPurchase, String dispalyname) {
		Requirement vatItemForPurchaseReq = get(vatitemForPurchase);
		ClientTAXItem vatItemPurchase = context
				.getSelection(PURCHASE_VAT_ITEMS);
		if (vatItemPurchase != null) {
			vatItemForPurchaseReq.setValue(vatItemPurchase);
		}
		ClientTAXItem value = vatItemForPurchaseReq.getValue();
		Object selection = context.getSelection("values");
		if (!vatItemForPurchaseReq.isDone() || value == selection) {
			return getVatItemForPurchseResult(context);
		}

		Record record = new Record(vatitemForPurchase);
		record.add("", vatitemForPurchase);
		record.add("", value.getName());
		list.add(record);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result getVatItemForPurchseResult(Context context) {
		Result result = context.makeResult();
		ResultList vatItemGroupsList = new ResultList(PURCHASE_VAT_ITEMS);

		Object last = context.getLast(RequirementType.TAXITEM_GROUP);
		List<ClientTAXItemGroup> clientTAXItemGroup = new ArrayList<ClientTAXItemGroup>();
		if (last != null) {
			vatItemGroupsList.add(createTaxItemRecord((ClientTAXItem) last));
			clientTAXItemGroup.add((ClientTAXItemGroup) last);
		}

		List<ClientTAXItemGroup> vatItemGroups = getPurchaseVatItemGroups();

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientTAXItemGroup> pagination = pagination(context, selection,
				actions, vatItemGroups, clientTAXItemGroup, VALUES_TO_SHOW);

		for (ClientTAXItemGroup terms : pagination) {
			vatItemGroupsList.add(createTaxItemRecord((ClientTAXItem) terms));
		}

		int size = vatItemGroupsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Purchase Vat Item.");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(vatItemGroupsList);
		result.add(commandList);
		result.add("Select the Purchase Vat Item");

		return result;
	}

	/**
	 * 
	 * @return
	 */
	private List<ClientTAXItemGroup> getPurchaseVatItemGroups() {
		List<ClientTAXItemGroup> vatItmsList = new ArrayList<ClientTAXItemGroup>();
		for (ClientTAXItemGroup vatItem : getFilteredVATItems()) {
			if (!vatItem.isSalesType()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
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
				DESCRIPTION, "Enter Description");
		if (result != null) {
			return result;
		}
		booleanOptionalRequirement(context, selection, list, IS_TAXABLE,
				"Taxable", "Tax exempt");

		booleanOptionalRequirement(context, selection, list, IS_ACTIVE,
				"This Item is Active", "This Item is InActive");

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Vat Code.");
		actions.add(finish);

		return makeResult;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param vatitemForSales
	 * @param dispalyName
	 * @return
	 */
	private Result vatItemForSalesRequirement(Context context, ResultList list,
			String vatitemForSales, String dispalyName) {
		Requirement vatItemForSalesReq = get(vatitemForSales);
		ClientTAXItem vatItemSale = context.getSelection(SALES_VAT_ITEMS);
		if (vatItemSale != null) {
			vatItemForSalesReq.setValue(vatItemSale);
		}

		ClientTAXItem value = vatItemForSalesReq.getValue();
		Object selection = context.getSelection("values");
		if (!vatItemForSalesReq.isDone() || value == selection) {
			return getVatItemForSaleResult(context);
		}

		Record record = new Record(vatitemForSales);
		record.add("", vatitemForSales);
		record.add("", value.getName());
		list.add(record);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result getVatItemForSaleResult(Context context) {
		Result result = context.makeResult();
		ResultList vatItemGroupsList = new ResultList(SALES_VAT_ITEMS);

		Object last = context.getLast(RequirementType.TAXITEM_GROUP);
		List<ClientTAXItemGroup> vatItemGroup = getPurchaseVatItemGroups();
		if (last != null) {
			vatItemGroupsList.add(createTaxItemRecord((ClientTAXItem) last));
			vatItemGroup.add((ClientTAXItemGroup) last);
		}

		List<ClientTAXItemGroup> vatItemGroups = getSalesVatItemGroups();

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientTAXItemGroup> pagination = pagination(context, selection,
				actions, vatItemGroups, vatItemGroup, VALUES_TO_SHOW);

		for (ClientTAXItemGroup terms : pagination) {
			vatItemGroupsList.add(createTaxItemRecord((ClientTAXItem) terms));
		}
		int size = vatItemGroupsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Sales Vat Item.");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(vatItemGroupsList);
		result.add(commandList);
		result.add("Select the Sales Vat Item");

		return result;
	}

	/**
	 * 
	 * @return
	 */
	private List<ClientTAXItemGroup> getFilteredVATItems() {
		List<ClientTAXItemGroup> vatItmsList = new ArrayList<ClientTAXItemGroup>();
		ArrayList<ClientTAXItemGroup> taxItemGroups = new ArrayList<ClientTAXItemGroup>(
				getClientCompany().getTaxItemGroups());
		taxItemGroups.addAll(getClientCompany().getTaxItems());
		for (ClientTAXItemGroup vatItem : getClientCompany().getTaxItems()) {
			if (vatItem.isPercentage()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	private List<ClientTAXItemGroup> getSalesVatItemGroups() {
		List<ClientTAXItemGroup> vatItmsList = new ArrayList<ClientTAXItemGroup>();
		for (ClientTAXItemGroup vatItem : getFilteredVATItems()) {
			if (vatItem.isSalesType()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

}
