package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewServiceItemCommand extends AbstractCommand {

	@Override
	protected Company getCompany() {
		return super.getCompany();
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("name", false, true));
		list.add(new Requirement("incomeaccount", false, true));
		list.add(new Requirement("isservice", true, true));
		list.add(new Requirement("description", true, true));
		list.add(new Requirement("price", true, true));
		list.add(new Requirement("isCommisionItem", true, true));
		list.add(new Requirement("cost", true, true));
		list.add(new Requirement("itemgroup", true, true));
		list.add(new Requirement("vatCode", false, true));
		list.add(new Requirement("active", true, true));
		list.add(new Requirement("buyservice", true, true));
		list.add(new Requirement("purchaseDescription", true, true));
		list.add(new Requirement("purchasePrice", true, true));
		list.add(new Requirement("expenseAccount", false, true));
		list.add(new Requirement("preferedSupplier", true, true));
		list.add(new Requirement("supplierServiceNo", true, true));

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = itemNameRequirement(context);
		if (result != null) {
			return result;
		}

		result = incomeorExpenseAccountRequirement(context, true);
		if (result != null) {
			return result;
		}

		result = vatCodeRequirment(context);
		if (result != null) {
			return result;
		}
		Boolean buyService = get("buyservice").getValue();
		if (buyService) {
			result = incomeorExpenseAccountRequirement(context, false);
			if (result != null) {
				return result;
			}
		}
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}

		return createItem(context);
	}

	/**
	 * for checking of optionals
	 * 
	 * @param context
	 * @return
	 */

	private Result createOptionalResult(Context context) {
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
		ResultList list = new ResultList("values");

		Requirement nameReq = get("name");
		Record nameRecord = new Record(nameReq);
		nameRecord.add("Name", "Name");
		nameRecord.add("Value", nameReq.getName());
		list.add(nameRecord);

		// Result result = isServiceRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

		Requirement itemGroupReq = get("itemgroup");
		ItemGroup itemGroup = (ItemGroup) itemGroupReq.getValue();
		if (itemGroup == selection) {
			context.setAttribute(INPUT_ATTR, "itemgroup");
			return itemgroups(context);
		}

		Requirement vatCodeReq = get("vatCode");
		TAXCode taxCode = (TAXCode) vatCodeReq.getValue();
		if (taxCode == selection) {
			context.setAttribute(INPUT_ATTR, "vatCode");
			return taxCode(context, null);
		}

		Requirement incomeAccountReq = get("incomeaccount");
		Account inAccount = (Account) incomeAccountReq.getValue();
		if (inAccount == selection) {
			context.setAttribute(INPUT_ATTR, "incomeaccount");
			return incomeorExpenseAccountRequirement(context, true);
		}

		Result result = descriptionRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = priceRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = isCommisionitemRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = costRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = activeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = buyserviceRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		Boolean buyService = get("buyservice").getValue();
		if (buyService) {
			Requirement expenseAccountReq = get("expenseAccount");
			Account exAccount = (Account) expenseAccountReq.getValue();
			if (exAccount == selection) {
				context.setAttribute(INPUT_ATTR, "expenseAccount");
				return incomeorExpenseAccountRequirement(context, false);
			}
			result = purchaseDescriptionRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = purchasepriceRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			// for prefferedSupplier
			result = vendorRequirement(context);
			if (result != null) {
				return result;
			}

			result = supplierServiceNoRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}
		result = itemGroupRequirement(context);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add(" Item is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Item.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result supplierServiceNoRequirement(Context context,
			ResultList list, Object selection) {

		Requirement supplierServiceReq = get("supplierServiceNo");
		Integer supplierService = (Integer) supplierServiceReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("supplierServiceNo")) {
			Integer supSer = context.getSelection(TEXT);
			if (supSer == null) {
				supSer = context.getInteger();
			}
			supplierService = supSer;
			supplierServiceReq.setValue(supplierService);
		}
		if (selection == supplierService) {
			context.setAttribute(INPUT_ATTR, "supplierServiceNo");
			return text(context, "Enter Supplier Service No.", supplierService
					.toString());
		}

		Long supplierServiceNo = (Long) get("supplierServiceNo").getValue();
		Record supplierServiceNoRec = new Record(supplierServiceNo);
		supplierServiceNoRec.add("Name", "Supplier Service No.");
		supplierServiceNoRec.add("Value", supplierServiceNo);
		list.add(supplierServiceNoRec);
		return null;
	}

	private Result purchasepriceRequirement(Context context, ResultList list,
			Object selection) {

		Requirement pPriceReq = get("purchasePrice");
		String pPrice = (String) pPriceReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("purchasePrice")) {
			String pric = context.getSelection(TEXT);
			if (pric == null) {
				pric = context.getString();
			}
			pPrice = pric;
			pPriceReq.setValue(pPrice);
		}
		if (selection == pPrice) {
			context.setAttribute(INPUT_ATTR, "purchasePrice");
			return text(context, "Enter Purchase Price", pPrice);
		}

		Record pPriceRecord = new Record(pPrice);
		pPriceRecord.add("Name", "purchasePrice");
		pPriceRecord.add("Value", pPrice);
		list.add(pPriceRecord);
		return null;
	}

	private Result priceRequirement(Context context, ResultList list,
			Object selection) {

		Requirement priceReq = get("price");
		String price = (String) priceReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("price")) {
			String pric = context.getSelection(TEXT);
			if (pric == null) {
				pric = context.getString();
			}
			price = pric;
			priceReq.setValue(price);
		}
		if (selection == price) {
			context.setAttribute(INPUT_ATTR, "price");
			return text(context, "Enter Price", price);
		}

		Record priceRecord = new Record(price);
		priceRecord.add("Name", "Price");
		priceRecord.add("Value", price);
		list.add(priceRecord);
		return null;
	}

	private Result purchaseDescriptionRequirement(Context context,
			ResultList list, Object selection) {
		Requirement pDescriptionReq = get("purchaseDescription");
		String pDescription = (String) pDescriptionReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("purchaseDescription")) {
			String desc = context.getSelection(TEXT);
			if (desc == null) {
				desc = context.getString();
			}
			pDescription = desc;
			pDescriptionReq.setValue(pDescription);
		}
		if (selection == pDescription) {
			context.setAttribute(INPUT_ATTR, "purchaseDescription");
			return text(context, "Enter Purchase Description", pDescription);
		}

		Record pDescRecord = new Record(pDescription);
		pDescRecord.add("Name", "Purchase Description");
		pDescRecord.add("Value", pDescription);
		list.add(pDescRecord);
		return null;
	}

	private Result buyserviceRequirement(Context context, ResultList list,
			Object selection) {
		Requirement buyserviceReq = get("buyservice");
		Boolean isbuyservice = (Boolean) buyserviceReq.getValue();
		if (selection == isbuyservice) {
			context.setAttribute(INPUT_ATTR, "buyservice");
			isbuyservice = !isbuyservice;
			buyserviceReq.setValue(isbuyservice);
		}
		String isbuyserviceString = "";
		if (isbuyservice) {
			isbuyserviceString = "I buy this Service is Active";
		} else {
			isbuyserviceString = "I buy this Service InActive";
		}
		Record isbuyserviceRecord = new Record("buyservice");
		isbuyserviceRecord.add("Name", "");
		isbuyserviceRecord.add("Value", isbuyserviceString);
		list.add(isbuyserviceRecord);
		return null;
	}

	private Result activeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement isActiveReq = get("active");
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, "active");
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This Item is Active";
		} else {
			activeString = "This Item is InActive";
		}
		Record isActiveRecord = new Record("active");
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);
		return null;
	}

	private Result costRequirement(Context context, ResultList list,
			Object selection) {

		Requirement costReq = get("cost");
		String cost = (String) costReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("cost")) {
			String cos = context.getSelection(TEXT);
			if (cos == null) {
				cos = context.getString();
			}
			cost = cos;
			costReq.setValue(cost);
		}
		if (selection == cost) {
			context.setAttribute(INPUT_ATTR, "cost");
			return text(context, "Enter cost", cost);
		}

		Record costRecord = new Record(cost);
		costRecord.add("Name", "cost");
		costRecord.add("Value", cost);
		list.add(costRecord);
		return null;
	}

	private Result isCommisionitemRequirement(Context context, ResultList list,
			Object selection) {

		Requirement isCommReq = get("isCommisionItem");
		Boolean isCommisionItem = (Boolean) isCommReq.getValue();
		if (selection == isCommisionItem) {
			context.setAttribute(INPUT_ATTR, "isCommisionItem");
			isCommisionItem = !isCommisionItem;
			isCommReq.setValue(isCommisionItem);
		}
		String commString = "";
		if (isCommisionItem) {
			commString = "This is Commision Item";
		} else {
			commString = "This Item is not Commision Item";
		}
		Record iscommRecord = new Record("isCommisionItem");
		iscommRecord.add("Name", "");
		iscommRecord.add("Value", commString);
		list.add(iscommRecord);

		return null;
	}

	private Result descriptionRequirement(Context context, ResultList list,
			Object selection) {
		Requirement descriptionReq = get("description");
		String description = (String) descriptionReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("description")) {
			String desc = context.getSelection(TEXT);
			if (desc == null) {
				desc = context.getString();
			}
			description = desc;
			descriptionReq.setValue(description);
		}
		if (selection == description) {
			context.setAttribute(INPUT_ATTR, "description");
			return text(context, "Enter Description", description);
		}

		Record descRecord = new Record(description);
		descRecord.add("Name", "Description");
		descRecord.add("Value", description);
		list.add(descRecord);
		return null;
	}

	private Result isServiceRequirement(Context context, ResultList list,
			Object selection) {
		Boolean isService = (Boolean) get("isservice").getValue();
		Record serviceRec = new Record(isService);
		serviceRec.add("Name", "Is Service");
		serviceRec.add("Value", isService.toString());
		list.add(serviceRec);
		return null;
	}

	/**
	 * checking for mandatory field item name
	 * 
	 * @param context
	 * @return
	 */

	private Result itemNameRequirement(Context context) {
		Requirement nameReq = get("name");
		if (!nameReq.isDone()) {
			String string = context.getString();
			if (string != null) {
				nameReq.setValue(string);
			} else {
				return text(context, "Please Enter the name", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals("name")) {
			nameReq.setValue(input);
		}
		return null;
	}

	/**
	 * cheking for vatcode
	 * 
	 * @param context
	 * @return
	 */
	private Result vatCodeRequirment(Context context) {
		Requirement taxReq = get("vatCode");
		TAXCode taxcode = context.getSelection(TAXCODE);
		if (!taxReq.isDone()) {
			if (taxcode != null) {
				taxReq.setValue(taxcode);
			} else {
				return taxCode(context, null);
			}
		}
		if (taxcode != null) {
			taxReq.setValue(taxcode);
		}
		return null;
	}

	/**
	 * checking for mandatory field income accounts by type (income or expense)
	 * 
	 * @param context
	 * @param typeOfaccounts
	 * @return
	 */

	protected Result incomeorExpenseAccountRequirement(Context context,
			boolean typeOfaccounts) {
		Requirement incomeAccountReq = get("incomeaccount");
		Account incomeAccount = context.getSelection("incomeaccounts");
		if (incomeAccount != null) {
			incomeAccountReq.setValue(incomeAccount);
		}
		if (!incomeAccountReq.isDone()) {
			return accountsByType(context, typeOfaccounts);
		}
		return null;
	}

	/**
	 * to get the result of accounts by type (income or expense)
	 * 
	 * @param context
	 * @param accountType
	 * @return
	 */
	protected Result accountsByType(Context context, boolean accountType) {
		Result result = context.makeResult();
		ResultList incomeaccountsList = new ResultList("incomeaccounts");

		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			incomeaccountsList.add(createincomeAccountRecord((Account) last));
			num++;
		}
		List<Account> accounts = getAccountsByType(context.getSession(),
				accountType);
		for (Account account : accounts) {
			if (account != last) {
				incomeaccountsList.add(createincomeAccountRecord(account));
				num++;
			}
			if (num == INCOMEACCOUNTS_TO_SHOW) {
				break;
			}
		}
		int size = incomeaccountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select an Account");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(incomeaccountsList);
		result.add(commandList);
		result.add("Type for Account");
		return result;
	}

	protected Record createincomeAccountRecord(Account last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		record.add("Account Number", last.getNumber());
		return record;
	}

	/**
	 * 
	 * @param session
	 * @param TypeofAccounts
	 *            (income or expense)
	 * @return
	 */
	private List<Account> getAccountsByType(Session session,
			boolean TypeofAccounts) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * requirement for a vendor from combo
	 * 
	 * @param context
	 * @return
	 */
	private Result vendorRequirement(Context context) {
		Requirement vendorReq = get("preferedSupplier");
		Vendor vendor = context.getSelection("vendors");
		if (vendor != null) {
			vendorReq.setValue(vendor);
		}
		if (!vendorReq.isDone()) {
			return vendors(context);
		}
		return null;
	}

	private Result vendors(Context context) {
		Result result = context.makeResult();
		ResultList vendorsList = new ResultList("vendors");

		Object last = context.getLast(RequirementType.VENDOR);
		int num = 0;
		if (last != null) {
			vendorsList.add(createVendorRecord((Vendor) last));
			num++;
		}
		List<Vendor> vendors = getVendors(context.getSession());
		for (Vendor vendor : vendors) {
			if (vendor != last) {
				vendorsList.add(createVendorRecord(vendor));
				num++;
			}
			if (num == VENDORS_TO_SHOW) {
				break;
			}
		}
		int size = vendorsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_UK)
				message.append("Select an Suplier");
			else
				message.append("Select an Vendor");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(vendorsList);
		result.add(commandList);
		if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_UK)
			result.add("Type for Supplier");
		else
			result.add("Type for Vendor");
		return result;
	}

	private Record createVendorRecord(Vendor last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		record.add("Balance", last.getBalance());
		return record;
	}

	private List<Vendor> getVendors(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result itemGroupRequirement(Context context) {
		Requirement itemgroupReq = get("itemgroup");
		ItemGroup itemgroup = context.getSelection("itemgroups");
		if (itemgroup != null) {
			itemgroupReq.setValue(itemgroup);
		}
		if (!itemgroupReq.isDone()) {
			return itemgroups(context);
		}
		return null;

	}

	private Result itemgroups(Context context) {
		Result result = context.makeResult();
		ResultList itemgroupsList = new ResultList("itemgroups");

		Object last = context.getLast(RequirementType.ITEM_GROUP);
		int num = 0;
		if (last != null) {
			itemgroupsList.add(createitemGroupRecord((ItemGroup) last));
			num++;
		}
		List<ItemGroup> itemgroups = getItemGroups(context.getSession());
		for (ItemGroup itemGroup : itemgroups) {
			if (itemGroup != last) {
				itemgroupsList.add(createitemGroupRecord(itemGroup));
				num++;
			}
			if (num == ITEMGROUPS_TO_SHOW) {
				break;
			}
		}
		int size = itemgroupsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select an ItemGroup");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(itemgroupsList);
		result.add(commandList);
		result.add("Type for ItemGroup");
		return result;

	}

	private List<ItemGroup> getItemGroups(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createitemGroupRecord(ItemGroup last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		return record;
	}

	private Result createItem(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
