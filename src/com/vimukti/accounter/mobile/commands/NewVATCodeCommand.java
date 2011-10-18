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
		Result result = null;

		setOptionalValues();

		result = nameRequirement(context, null, null, null);
		if (result != null) {
			return result;
		}

		result = vatItemForSalesRequirement(context);
		if (result != null) {
			return result;
		}

		result = vatItemForPurchaseRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
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

	private Result vatItemForPurchaseRequirement(Context context) {
		Requirement vatItemForPurchaseReq = get(VATITEM_FOR_PURCHASE);
		ClientTAXItem vatItemPurchase = context
				.getSelection(PURCHASE_VAT_ITEMS);
		if (vatItemPurchase != null) {
			vatItemForPurchaseReq.setValue(vatItemPurchase);
		}
		if (!vatItemForPurchaseReq.isDone()) {
			return getVatItemForPurchseResult(context);
		}
		return null;
	}

	private Result getVatItemForPurchseResult(Context context) {
		Result result = context.makeResult();
		ResultList vatItemGroupsList = new ResultList(PURCHASE_VAT_ITEMS);

		Object last = context.getLast(RequirementType.TAXITEM_GROUP);
		if (last != null) {
			vatItemGroupsList.add(createTaxItemRecord((ClientTAXItem) last));
		}

		List<ClientTAXItemGroup> vatItemGroups = getPurchaseVatItemGroups();
		for (int i = 0; i < VALUES_TO_SHOW && i < vatItemGroups.size(); i++) {
			ClientTAXItemGroup vatItem = vatItemGroups.get(i);
			if (vatItem != last) {
				vatItemGroupsList
						.add(createTaxItemRecord((ClientTAXItem) vatItem));
			}
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

	private List<ClientTAXItemGroup> getPurchaseVatItemGroups() {
		List<ClientTAXItemGroup> vatItmsList = new ArrayList<ClientTAXItemGroup>();
		for (ClientTAXItemGroup vatItem : getFilteredVATItems()) {
			if (!vatItem.isSalesType()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	private Result createOptionalRequirement(Context context) {
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

		Requirement nameReq = get(NAME);
		String name = (String) nameReq.getValue();
		if (NAME == selection) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the VAT Code Name.", name);
		}

		ResultList list = new ResultList("values");

		Record nameRecord = new Record(NAME);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, "Enter Description");
		if (result != null) {
			return result;
		}

		Requirement isTaxableReq = get(IS_TAXABLE);
		Boolean isTaxable = (Boolean) isTaxableReq.getValue();

		if (selection == IS_TAXABLE) {
			context.setAttribute(INPUT_ATTR, IS_TAXABLE);
			isTaxable = !isTaxable;
			isTaxableReq.setValue(isTaxable);
		}
		String taxableString = "";
		if (isTaxable) {
			taxableString = "Taxable";
		} else {
			taxableString = "Tax exempt";
		}

		Record isTaxableRecord = new Record(IS_TAXABLE);
		isTaxableRecord.add("Name", "Tax");
		isTaxableRecord.add("Value", taxableString);
		list.add(isTaxableRecord);

		if (isTaxable) {
			Requirement salesVatItemReq = get(VATITEM_FOR_SALES);
			ClientTAXItem salesTaxItem = (ClientTAXItem) salesVatItemReq
					.getValue();
			if (VATITEM_FOR_SALES == selection) {
				context.setAttribute(INPUT_ATTR, VATITEM_FOR_SALES);
				return getVatItemForSaleResult(context);
			}
			Record salesTaxItemRecord = new Record(VATITEM_FOR_SALES);
			salesTaxItemRecord.add("Name", "Sales Vat Item");
			salesTaxItemRecord.add("Value", salesTaxItem.getName());
			list.add(salesTaxItemRecord);

			Requirement purchaseVatItemReq = get(VATITEM_FOR_PURCHASE);
			ClientTAXItem purchaseTaxItem = (ClientTAXItem) purchaseVatItemReq
					.getValue();
			if (VATITEM_FOR_PURCHASE == selection) {
				context.setAttribute(INPUT_ATTR, VATITEM_FOR_PURCHASE);
				return getVatItemForPurchseResult(context);
			}
			Record purchaseTaxItemRecord = new Record(VATITEM_FOR_PURCHASE);
			purchaseTaxItemRecord.add("Name", "Purchase Vat Item");
			purchaseTaxItemRecord.add("Value", purchaseTaxItem.getName());
			list.add(purchaseTaxItemRecord);
		}

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();

		if (selection == IS_ACTIVE) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This Item is Active";
		} else {
			activeString = "This Item is InActive";
		}

		Record isActiveRecord = new Record(IS_ACTIVE);
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);

		result = context.makeResult();
		result.add("Vat Code is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Vat Code.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result vatItemForSalesRequirement(Context context) {
		Requirement vatItemForSalesReq = get(VATITEM_FOR_SALES);
		ClientTAXItem vatItemSale = context.getSelection(SALES_VAT_ITEMS);
		if (vatItemSale != null) {
			vatItemForSalesReq.setValue(vatItemSale);
		}
		if (!vatItemForSalesReq.isDone()) {
			return getVatItemForSaleResult(context);
		}
		return null;
	}

	private Result getVatItemForSaleResult(Context context) {
		Result result = context.makeResult();
		ResultList vatItemGroupsList = new ResultList(SALES_VAT_ITEMS);

		Object last = context.getLast(RequirementType.TAXITEM_GROUP);
		if (last != null) {
			vatItemGroupsList.add(createTaxItemRecord((ClientTAXItem) last));
		}

		List<ClientTAXItemGroup> vatItemGroups = getSalesVatItemGroups();
		for (int i = 0; i < VALUES_TO_SHOW && i < vatItemGroups.size(); i++) {
			ClientTAXItemGroup vatItemGroup = vatItemGroups.get(i);
			if (vatItemGroup != last) {
				vatItemGroupsList
						.add(createTaxItemRecord((ClientTAXItem) vatItemGroup));
			}
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
