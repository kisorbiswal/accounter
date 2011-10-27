package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ItemGroupRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public abstract class AbstractItemCreateCommand extends NewAbstractCommand {

	private static final String NAME = "name";
	private static final String I_SELL_THIS = "iSellthis";
	private static final String SALES_DESCRIPTION = "salesDescription";
	private static final String SALES_PRICE = "salesPrice";
	private static final String INCOME_ACCOUNT = "incomeAccount";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String IS_COMMISION_ITEM = "isCommisionItem";
	private static final String STANDARD_COST = "standardCost";
	private static final String ITEM_GROUP = "itemGroup";
	private static final String IS_ACTIVE = "isActive";
	private static final String I_BUY_THIS = "iBuyService";
	private static final String PURCHASE_DESCRIPTION = "purchaseDescription";
	private static final String PURCHASE_PRICE = "purchasePrice";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String PREFERRED_SUPPLIER = "preferredSupplier";
	private static final String SERVICE_NO = "supplierServiceNo";
	protected static final String WEIGHT = "weight";
	private static final String TAXCODE = "taxCode";

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(NAME, getMessages().pleaseEnter(
				getConstants().itemName()), getConstants().name(), false, true));
		list.add(new BooleanRequirement(I_SELL_THIS, true) {

			@Override
			protected String getTrueString() {
				return getConstants().isellthisservice();
			}

			@Override
			protected String getFalseString() {
				return getConstants().idontSellThisService();
			}
		});

		list.add(new NameRequirement(SALES_DESCRIPTION, getMessages()
				.pleaseEnter(getConstants().salesDescription()), getConstants()
				.salesDescription(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AmountRequirement(SALES_PRICE, getMessages().pleaseEnter(
				getConstants().salesPrice()), getConstants().salesPrice(),
				true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AccountRequirement(INCOME_ACCOUNT, getMessages()
				.pleaseEnter(
						getMessages().incomeAccount(Global.get().Account())),
				getMessages().incomeAccount(Global.get().Account()), false,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& e.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& e.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& e.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
								&& e.getType() != ClientAccount.TYPE_OTHER_EXPENSE
								&& e.getType() != ClientAccount.TYPE_EXPENSE
								&& e.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& e.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& e.getType() != ClientAccount.TYPE_FIXED_ASSET
								&& e.getType() != ClientAccount.TYPE_CASH
								&& e.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
								&& e.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& e.getType() != ClientAccount.TYPE_EQUITY) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Account());
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}

			@Override
			protected String getSetMessage() {
				return "Income Account has been selected";
			}
		});

		list.add(new BooleanRequirement(IS_TAXABLE, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getConstants().taxable();
			}

			@Override
			protected String getFalseString() {
				return getConstants().taxExempt();
			}
		});

		list.add(new BooleanRequirement(IS_COMMISION_ITEM, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getConstants().thisIsCommisionItem();
			}

			@Override
			protected String getFalseString() {
				return getConstants().thisIsNoCommisionItem();
			}
		});

		list.add(new AmountRequirement(STANDARD_COST, getMessages()
				.pleaseEnter(getConstants().standardCost()), getConstants()
				.standardCost(), true, true));

		list.add(new ItemGroupRequirement(ITEM_GROUP,
				"Enter the item group name", getConstants().itemGroup(), true,
				true, null) {

			@Override
			protected String getSetMessage() {
				return "Item group has been selected";
			}

			@Override
			protected List<ClientItemGroup> getLists(Context context) {
				return getClientCompany().getItemGroups();
			}

			@Override
			protected boolean filter(ClientItemGroup e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, "Enter the Tax Code name",
				getConstants().taxCode(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getCompany().getPreferences()
						.isClassOnePerTransaction()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return getClientCompany().getTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getConstants().active();
			}

			@Override
			protected String getFalseString() {
				return getConstants().inActive();
			}
		});

		list.add(new BooleanRequirement(I_BUY_THIS, true) {

			@Override
			protected String getTrueString() {
				return getConstants().iBuyThisItem();
			}

			@Override
			protected String getFalseString() {
				return getConstants().idontBuyThisService();
			}
		});

		list.add(new NameRequirement(PURCHASE_DESCRIPTION, getMessages()
				.pleaseEnter(getConstants().purchaseDescription()),
				getConstants().purchaseDescription(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AmountRequirement(PURCHASE_PRICE, getMessages()
				.pleaseEnter(getConstants().purchasePrice()), getConstants()
				.purchasePrice(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new AccountRequirement(EXPENSE_ACCOUNT, getMessages()
				.pleaseEnter(
						getMessages().expenseAccount(Global.get().Account())),
				getMessages().expenseAccount(Global.get().Account()), false,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& e.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& e.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& e.getType() != ClientAccount.TYPE_INCOME
								&& e.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& e.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& e.getType() != ClientAccount.TYPE_FIXED_ASSET
								&& e.getType() != ClientAccount.TYPE_CASH
								&& e.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
								&& e.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& e.getType() != ClientAccount.TYPE_EQUITY) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Account());
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}

			@Override
			protected String getSetMessage() {
				return "Expense Account has been selected";
			}
		});

		list.add(new VendorRequirement(PREFERRED_SUPPLIER,
				"enter the supplier name or number", getMessages()
						.preferredVendor(Global.get().Vendor()), true, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return "Preferred Supplier has been selected";
			}

			@Override
			protected List<ClientVendor> getLists(Context context) {
				return getClientCompany().getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(ClientVendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(SERVICE_NO, getMessages().pleaseEnter(
				getMessages().vendorServiceNo(Global.get().Vendor())),
				getMessages().vendorServiceNo(Global.get().Vendor()), true,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

	}

	@Override
	public String getWelcomeMessage() {
		return "Create Item Command is activated.";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(I_SELL_THIS).setDefaultValue(Boolean.TRUE);
		get(IS_TAXABLE).setDefaultValue(Boolean.TRUE);
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(SERVICE_NO).setDefaultValue("1");
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientItem item = new ClientItem();

		String name = (String) get(NAME).getValue();

		// TODO:check weather it is product or service item
		Integer weight = 0;
		Requirement weightReq = get(WEIGHT);
		if (weightReq != null) {
			String s = weightReq.getValue();
			weight = s != null ? Integer.parseInt(s) : 0;
		}
		Boolean iSellthis = (Boolean) get(I_SELL_THIS).getValue();
		String description = (String) get(SALES_DESCRIPTION).getValue();
		double price = (Double) get(SALES_PRICE).getValue();
		ClientAccount incomeAccount = (ClientAccount) get(INCOME_ACCOUNT)
				.getValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getValue();
		Boolean isCommisionItem = (Boolean) get(IS_COMMISION_ITEM).getValue();
		double cost = (Double) get(STANDARD_COST).getValue();
		ClientItemGroup itemGroup = (ClientItemGroup) get(ITEM_GROUP)
				.getValue();
		ClientTAXCode vatcode = (ClientTAXCode) get(TAXCODE).getValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		Boolean isBuyservice = (Boolean) get(I_BUY_THIS).getValue();
		String purchaseDescription = (String) get(PURCHASE_DESCRIPTION)
				.getValue();
		double purchasePrice = (Double) get(PURCHASE_PRICE).getValue();
		ClientAccount expenseAccount = (ClientAccount) get(EXPENSE_ACCOUNT)
				.getValue();
		ClientVendor preferedSupplier = (ClientVendor) get(PREFERRED_SUPPLIER)
				.getValue();
		String supplierServiceNo = (String) get(SERVICE_NO).getValue();

		item.setName(name);
		item.setWeight(weight);
		item.setISellThisItem(iSellthis);
		if (iSellthis) {
			item.setSalesDescription(description);
			item.setSalesPrice(price);
			item.setIncomeAccount(incomeAccount.getID());
			item.setTaxable(isTaxable);
			item.setCommissionItem(isCommisionItem);
		}
		item.setStandardCost(cost);
		if (context.getCompany().getPreferences().isClassOnePerTransaction()) {
			item.setTaxCode(vatcode.getID());
		}
		item.setActive(isActive);
		item.setIBuyThisItem(isBuyservice);
		if (itemGroup != null)
			item.setItemGroup(itemGroup.getID());
		if (isBuyservice) {
			item.setPurchaseDescription(purchaseDescription);
			item.setPurchasePrice(purchasePrice);
			item.setExpenseAccount(expenseAccount.getID());
			if (preferedSupplier != null)
				item.setPreferredVendor(preferedSupplier.getID());
			item.setVendorItemNumber(supplierServiceNo);
		}
		create(item, context);

		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().item());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().item());
	}

}
