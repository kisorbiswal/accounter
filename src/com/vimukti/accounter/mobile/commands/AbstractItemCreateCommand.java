package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
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

public abstract class AbstractItemCreateCommand extends AbstractCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("name", false, true));
		list.add(new Requirement("iSellthis", true, true));
		list.add(new Requirement("description", true, true));
		list.add(new Requirement("price", true, true));
		list.add(new Requirement("incomeaccount", false, true));
		list.add(new Requirement("isTaxable", true, true));
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
	public Result run(Context context) {
		Result result = context.makeResult();
		result =itemNameRequirement(context);
		if (result != null) {
			return result;
		}
		setDefaultValues();
		 Boolean iSellThis = get("iSellthis").getValue();
		 if (iSellThis) {
		 result = incomeorExpenseAccountRequirement(context, true);
		 if (result != null) {
		 return result;
		 }
		 }

		if (context.getCompany().getPreferences().isClassOnePerTransaction()) {
			result = vatCodeRequirment(context);
			if (result != null) {
				return result;
			}
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

		return createNewItem(context);
	}

	private void setDefaultValues() {
		get("iSellthis").setDefaultValue(Boolean.TRUE);
		get("description").setDefaultValue(" ");
		get("price").setDefaultValue(0.0D);
		get("isTaxable").setDefaultValue(Boolean.TRUE);
		get("isCommisionItem").setDefaultValue(Boolean.TRUE);
		get("cost").setDefaultValue(0.0D);
		get("itemgroup").setDefaultValue("");
		get("active").setDefaultValue(Boolean.TRUE);
		get("buyservice").setDefaultValue(Boolean.FALSE);
		get("purchaseDescription").setDefaultValue(" ");
		get("purchasePrice").setDefaultValue(0.0D);
		get("preferedSupplier").setDefaultValue("");
		get("supplierServiceNo").setDefaultValue("");
	}

	/**
	 * for checking of optionals
	 * 
	 * @param context
	 * @return
	 */

	private Result createOptionalResult(Context context) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

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

		Requirement accNameReq = get("name");
		String name = (String) accNameReq.getValue();
		if (selection != null) {
			if (selection == "accountName") {
				context.setAttribute(INPUT_ATTR, "name");
				return text(context, "Enter Account Name", name);
			} 
		}
		Record nameRecord = new Record("accountName");
		nameRecord.add("", "Name");
		nameRecord.add("", name);
		list.add(nameRecord);
		

		Result result = isSellthisRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		// TODO :check weather it is product or service item
		result = weightRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		// Boolean iSellThis = get("iSellthis").getValue();
		boolean iSellThis = true;
		if (iSellThis) {
			Requirement incomeAccountReq = get("incomeaccount");
			Account inAccount = (Account) incomeAccountReq.getValue();
			if (inAccount == selection) {
				context.setAttribute(INPUT_ATTR, "incomeaccount");
				Result incomeorExpenseAccountRequirement = incomeorExpenseAccountRequirement(
						context, true);
				if (incomeorExpenseAccountRequirement != null) {
					return incomeorExpenseAccountRequirement;
				}
			}

			result = descriptionRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = amountOptionalRequirement(context, list, selection,
					"price", "Please enter the Price");
			if (result != null) {
				return result;
			}

			// result = isTaxableRequirement(context, list, selection);
			// if (result != null) {
			// return result;
			// }
			// TODO: check box requirment
			// result = isCommisionitemRequirement(context, list, selection);
			// if (result != null) {
			// return result;
			// }
		}
		result = amountOptionalRequirement(context, list, selection, "cost", "please enter the cost");
		if (result != null) {
			return result;
		}

		result = itemGroupRequirement(context);
		if(result != null){
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

	private Result isTaxableRequirement(Context context, ResultList list,
			Object selection) {
		Requirement TaxableReq = get("isTaxable");
		Boolean isTaxable = (Boolean) TaxableReq.getValue();
		if (selection == isTaxable) {
			context.setAttribute(INPUT_ATTR, "isTaxable");
			isTaxable = !isTaxable;
			TaxableReq.setValue(isTaxable);
		}
		String TaxableString = "";
		if (isTaxable) {
			TaxableString = "Taxable is Active";
		} else {
			TaxableString = "Taxable is InActive";
		}
		Record TaxableRecord = new Record("isTaxable");
		TaxableRecord.add("Name", "Taxable");
		TaxableRecord.add("Value", TaxableString);
		list.add(TaxableRecord);
		return null;
	}

	private Result isSellthisRequirement(Context context, ResultList list,
			Object selection) {
		Requirement iSellthisReq = get("iSellthis");
		Boolean isSellthis = (Boolean) iSellthisReq.getValue() == null ? false
				: true;
		if (selection == isSellthis) {
			context.setAttribute(INPUT_ATTR, "iSellthis");
			isSellthis = !isSellthis;
			iSellthisReq.setValue(isSellthis);
		}

		String isSellthisString = "";
		if (isSellthis) {
			isSellthisString = "I sell this Service is Active";
		} else {
			isSellthisString = "I sell this Service is InActive";
		}
		Record issellThisRecord = new Record("iSellthis");
		issellThisRecord.add("Name", "I sell this item");
		issellThisRecord.add("Value", isSellthisString);
		list.add(issellThisRecord);
		return null;
	}

	protected Result weightRequirement(Context context, ResultList list,
			Object selection) {
		return null;
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
		Requirement req = get("price");
		Double price = (Double) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);

		if (attribute.equals("price")) {
			String selectedPrice = context.getSelection("price");
			if (selectedPrice == null) {
				selectedPrice = context.getNumber();
			}
			price = Double.parseDouble(selectedPrice);
			req.setValue(price);
		}
		if (selection == price) {
			if (selection == "price") {
				context.setAttribute(INPUT_ATTR, "price");
				return amount(context, "Plese enter the price", price);
			}
		}
		Record priceRecord = new Record("price");
		priceRecord.add("Name", "price");
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
		isActiveRecord.add("Name", "Active");
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
		costRecord.add("Name", "Cost");
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
		iscommRecord.add("Name", "Commision Item");
		iscommRecord.add("Value", commString);
		list.add(iscommRecord);

		return null;
	}

	private Result descriptionRequirement(Context context, ResultList list,
			Object selection) {
		Requirement requirement = get("description");
		String desc = (String) requirement.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("description")) {
			String selectedDesc = context.getSelection("description");
			if (selectedDesc == null) {
				selectedDesc = context.getString();
			}
			desc = selectedDesc;
			requirement.setValue(desc);
		}

		if (selection == desc) {
			context.setAttribute(INPUT_ATTR, "description");
			return text(context, "Please enter the description", null);
		}
		Record descRecord = new Record(desc);
		descRecord.add("Name", "description");
		descRecord.add("Value", desc);
		list.add(descRecord);
		return null;

	}

	/**
	 * checking for mandatory field item name
	 * 
	 * @param context
	 * @return
	 */

	private Result itemNameRequirement(Context context) {
		Requirement requirement = get("name");
		if (!requirement.isDone()) {
			String name = context.getString();
			if (name.trim().length() != 0) {
				requirement.setValue(name);
			} else {
				context.setAttribute(INPUT_ATTR, "name");
				return text(context, "Please enter the Name", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals("name")) {
			requirement.setValue(input);
		}
		return null;
	}

	private Result vatCodeRequirment(Context context) {
		Requirement taxCodeRequirement = get("vatCode");
		TAXCode taxCode = context.getSelection("vatCode");
		if (taxCode != null) {
			taxCodeRequirement.setValue(taxCode);
		}
		if (!taxCodeRequirement.isDone()) {
			return taxCode(context, null);
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
		List<Account> accounts = getAccountsByType(context, accountType);
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
	private List<Account> getAccountsByType(Context context,
			boolean TypeofAccounts) {
		List<Account> accountslist = null;
		Set<Account> allAccounts = context.getCompany().getAccounts();
		List<Account> accounts = new ArrayList<Account>(allAccounts);
		accountslist = new ArrayList<Account>();
		for (Account account : accounts) {
			if (TypeofAccounts) {
				if (account.getType() == 14)
					accountslist.add(account);
			}
			if (account.getType() == 16) {
				accountslist.add(account);
			}
		}
		return accountslist;
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
		List<ItemGroup> itemgroups = getItemGroups(context
				.getHibernateSession());
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

	private Result createNewItem(Context context) {
		Item item = null;

		String name = (String) get("name").getValue();

		// TODO:check weather it is product or service item
		Integer weight = (Integer) get("weight").getValue();
		Boolean iSellthis = (Boolean) get("iSellthis").getValue();
		String description = (String) get("description").getValue();
		double price = (Double) get("price").getValue();
		Account incomeAccount = (Account) get("incomeaccount").getValue();
		Boolean isTaxable = (Boolean) get("isTaxable").getValue();
		Boolean isCommisionItem = (Boolean) get("isCommisionItem").getValue();
		double cost = (Double) get("cost").getValue();
		ItemGroup itemGroup = (ItemGroup) get("itemgroup").getValue();
		TAXCode vatcode = (TAXCode) get("vatCode").getValue();
		Boolean isActive = (Boolean) get("active").getValue();
		Boolean isBuyservice = (Boolean) get("buyservice").getValue();
		String purchaseDescription = (String) get("purchaseDescription")
				.getValue();
		double purchasePrice = (Double) get("purchasePrice").getValue();
		Account expenseAccount = (Account) get("expenseAccount").getValue();
		Vendor preferedSupplier = (Vendor) get("preferedSupplier").getValue();
		String supplierServiceNo = (String) get("supplierServiceNo").getValue();

		item.setName(name);
		item.setWeight(weight);
		item.setISellThisItem(iSellthis);
		if (iSellthis) {
			item.setSalesDescription(description);
			item.setSalesPrice(price);
			item.setIncomeAccount(incomeAccount);
			item.setTaxable(isTaxable);
			item.setCommissionItem(isCommisionItem);
		}
		item.setStandardCost(cost);
		item.setTaxCode(vatcode);
		item.setActive(isActive);
		item.setIBuyThisItem(isBuyservice);
		if (isBuyservice) {
			item.setPurchaseDescription(purchaseDescription);
			item.setPurchasePrice(purchasePrice);
			item.setExpenseAccount(expenseAccount);
			item.setPreferredVendor(preferedSupplier);
			item.setVendorItemNumber(supplierServiceNo);
		}
		create(item, context);

		markDone();

		Result result = new Result();
		result.add(" Item was created successfully.");

		return result;

	}

}
