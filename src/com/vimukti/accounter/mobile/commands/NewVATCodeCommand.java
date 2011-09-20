package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewVATCodeCommand extends AbstractCommand {

	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String VATITEM_FOR_SALES = "vatItemForSales";
	private static final String VATITEM_FOR_PURCHASE = "vatItemForPurchase";
	private static final String IS_ACTIVE = "isActive";
	private static final String SALES_VAT_ITEMS = "salesVatItems";
	private static final int VALUES_TO_SHOW = 5;
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

		result = nameRequirement(context);
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

	private Result createTaxCode(Context context) {
		TAXCode taxCode = new TAXCode();

		String name = get(NAME).getValue();
		String description = (String) get(DESCRIPTION).getDefaultValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getDefaultValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getDefaultValue();

		taxCode.setName(name);
		taxCode.setDescription(description);
		taxCode.setTaxable(isTaxable);
		taxCode.setActive(isActive);
		if (isTaxable) {
			TAXItem salesVatItem = get(VATITEM_FOR_SALES).getValue();
			TAXItem purchaseVatItem = get(VATITEM_FOR_PURCHASE).getValue();
			taxCode.setTAXItemGrpForSales(salesVatItem);
			taxCode.setTAXItemGrpForPurchases(purchaseVatItem);
		}

		Session session = context.getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(taxCode);
		transaction.commit();

		markDone();
		Result result = new Result();
		result.add("VAT Code was created successfully.");

		return result;
	}

	private Result vatItemForPurchaseRequirement(Context context) {
		Requirement vatItemForPurchaseReq = get(VATITEM_FOR_PURCHASE);
		TAXAgency vatItemPurchase = context.getSelection(VATITEM_FOR_PURCHASE);
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
			vatItemGroupsList.add(createVatItemRecord((TAXItem) last));
		}

		List<TAXItem> vatItemGroups = getPurchaseVatItemGroups(context
				.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < vatItemGroups.size(); i++) {
			TAXItem vatItem = vatItemGroups.get(i);
			if (vatItem != last) {
				vatItemGroupsList.add(createVatItemRecord((TAXItem) vatItem));
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

	private List<TAXItem> getPurchaseVatItemGroups(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result createOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

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
		if (name == selection) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the VAT Code Name.", name);
		}

		ResultList list = new ResultList("values");

		Record nameRecord = new Record(name);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

		Result result = descriptionRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Requirement isTaxableReq = get(IS_TAXABLE);
		Boolean isTaxable = (Boolean) isTaxableReq.getDefaultValue();
		if (selection == isTaxable) {
			context.setAttribute(INPUT_ATTR, IS_TAXABLE);
			isTaxable = !isTaxable;
			isTaxableReq.setDefaultValue(isTaxable);
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
			TAXItem salesTaxItem = (TAXItem) salesVatItemReq.getValue();
			if (salesTaxItem == selection) {
				context.setAttribute(INPUT_ATTR, VATITEM_FOR_SALES);
				return getVatItemForSaleResult(context);
			}
			Record salesTaxItemRecord = new Record(salesTaxItem);
			salesTaxItemRecord.add("Name", "Sales Vat Item");
			salesTaxItemRecord.add("Value", salesTaxItem.getName());
			list.add(salesTaxItemRecord);

			Requirement purchaseVatItemReq = get(VATITEM_FOR_SALES);
			TAXItem purchaseTaxItem = (TAXItem) purchaseVatItemReq.getValue();
			if (purchaseTaxItem == selection) {
				context.setAttribute(INPUT_ATTR, VATITEM_FOR_SALES);
				return getVatItemForPurchseResult(context);
			}
			Record purchaseTaxItemRecord = new Record(purchaseTaxItem);
			purchaseTaxItemRecord.add("Name", "Purchase Vat Item");
			purchaseTaxItemRecord.add("Value", purchaseTaxItem.getName());
			list.add(purchaseTaxItemRecord);
		}

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getDefaultValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setDefaultValue(isActive);
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
		TAXAgency vatItemSale = context.getSelection(VATITEM_FOR_SALES);
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
			vatItemGroupsList.add(createVatItemRecord((TAXItem) last));
		}

		List<TAXItem> vatItemGroups = getSalesVatItemGroups(context
				.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < vatItemGroups.size(); i++) {
			TAXItem vatItemGroup = vatItemGroups.get(i);
			if (vatItemGroup != last) {
				vatItemGroupsList
						.add(createVatItemRecord((TAXItem) vatItemGroup));
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

	private List<TAXItem> getSalesVatItemGroups(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createVatItemRecord(TAXItem taxItem) {
		Record record = new Record(taxItem);
		record.add("Name", taxItem.getName());
		return record;
	}

	private Result descriptionRequirement(Context context, ResultList list,
			Object selection) {
		Requirement descriptionReq = get(DESCRIPTION);
		String description = (String) descriptionReq.getDefaultValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(DESCRIPTION)) {
			String desc = context.getSelection(TEXT);
			if (desc == null) {
				desc = context.getString();
			}
			description = desc;
			descriptionReq.setDefaultValue(description);
		}
		if (selection == description) {
			context.setAttribute(INPUT_ATTR, DESCRIPTION);
			return text(context, "Description", description);
		}

		Record descRecord = new Record(description);
		descRecord.add("Name", "Description");
		descRecord.add("Value", description);
		list.add(descRecord);
		return null;
	}

	private Result nameRequirement(Context context) {
		Requirement nameReq = get(NAME);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(NAME)) {
			input = context.getString();
			nameReq.setValue(input);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!nameReq.isDone()) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the VAT Code Name.", null);
		}
		return null;
	}

}
