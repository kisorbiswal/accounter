package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionItemTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {
	private static final String QUANITY = "Quantity";
	private static final String ITEM = "Item";
	private static final String UNITPTICE = "UnitPrice";
	private static final String DISCOUNT = "Discount";
	private static final String TAXCODE = "TaxCode";
	private static final String TAX = "Tax";
	private static final String DESCRIPTION = "Description";

	public TransactionItemTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
	}

	public abstract boolean isSales();

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM,
				"Please Select an Item for Transaction", "Item", false, true,
				null, isSales()) {

			@Override
			protected List<Item> getLists(Context context) {
				return getItems(context);
			}

			@Override
			public void setValue(Object value) {
				Item item = (Item) value;
				super.setValue(value);
				if (item != null) {
					if (isSales()) {
						get(UNITPTICE).setValue(item.getSalesPrice());
					} else {
						get(UNITPTICE).setValue(item.getPurchasePrice());
					}
					get(TAXCODE).setValue(
							get(TAXCODE).getValue() == null ? item.getTaxCode()
									: get(TAXCODE).getValue());
					get(TAX).setValue(item.isTaxable());
				}
			}
		});

		list.add(new AmountRequirement(QUANITY, "Please Enter Quantity",
				"Quantity", true, true));

		list.add(new AmountRequirement(UNITPTICE, "Please Enter Unit Price",
				"Unit Price", false, true));

		list.add(new AmountRequirement(DISCOUNT, "Please Enter Discount",
				"Discount", true, true));

		list.add(new TaxCodeRequirement(TAXCODE, "Please Select TaxCode",
				"Tax Code", false, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				Set<TAXCode> taxCodes = context.getCompany().getTaxCodes();
				List<TAXCode> clientcodes = new ArrayList<TAXCode>();
				for (TAXCode taxCode : taxCodes) {
					if (taxCode.isActive()) {
						clientcodes.add(taxCode);
					}
				}
				return clientcodes;
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(TAX, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& getPreferences().isTaxPerDetailLine()) {
					return null;
				} else {
					return super.run(context, makeResult, list, actions);
				}
			}

			@Override
			protected String getTrueString() {
				return getMessages().taxable();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxExempt();
			}
		});

		list.add(new StringRequirement(DESCRIPTION, "Please Enter Description",
				"Description", true, true));
	}

	public abstract List<Item> getItems(Context context);

	@Override
	protected String getEmptyString() {
		return "There are no Transaction Items";
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		Item clientItem = get(ITEM).getValue();
		obj.setItem(clientItem.getID());
		obj.getQuantity().setValue((Double) get(QUANITY).getValue());
		obj.setUnitPrice((Double) get(UNITPTICE).getValue());
		obj.setDiscount((Double) get(DISCOUNT).getValue());
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			if (taxCode != null) {
				obj.setTaxCode(taxCode.getID());
			}
		} else {
			obj.setTaxable((Boolean) get(TAX).getValue());
		}
		obj.setDescription((String) get(DESCRIPTION).getValue());
		double lt = obj.getQuantity().getValue() * obj.getUnitPrice();
		double disc = obj.getDiscount();
		obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(obj.getItem(),
				AccounterCoreType.ITEM);
		get(ITEM).setValue(item);
		get(QUANITY).setDefaultValue(obj.getQuantity().getValue());
		get(UNITPTICE).setValue(obj.getUnitPrice());

		get(DISCOUNT).setDefaultValue(obj.getDiscount());
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			get(TAXCODE).setDefaultValue(
					CommandUtils.getServerObjectById(obj.getTaxCode(),
							AccounterCoreType.TAX_CODE));
			get(TAX).setDefaultValue(false);
		} else {
			get(TAX).setDefaultValue(obj.isTaxable());
		}
		get(DESCRIPTION).setDefaultValue(obj.getDescription());
	}

	@Override
	protected ClientTransactionItem getNewObject() {
		ClientTransactionItem clientTransactionItem = new ClientTransactionItem();
		clientTransactionItem.setType(ClientTransactionItem.TYPE_ITEM);
		return clientTransactionItem;
	}

	@Override
	protected Record createFullRecord(ClientTransactionItem t) {
		Record record = new Record(t);
		ClientItem iAccounterCore = (ClientItem) CommandUtils
				.getClientObjectById(t.getItem(), AccounterCoreType.ITEM,
						getCompanyId());
		if (iAccounterCore != null) {
			record.add("", iAccounterCore.getDisplayName());
		}
		record.add("", t.getQuantity());
		record.add("", t.getUnitPrice());
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			record.add("",
					((ClientTAXCode) (CommandUtils.getClientObjectById(
							t.getTaxCode(), AccounterCoreType.TAX_CODE,
							getCompanyId()))).getDisplayName());
		} else {
			if (t.isTaxable()) {
				record.add("", getMessages().taxable());
			} else {
				record.add("", getMessages().taxExempt());
			}
		}
		record.add("", t.getDescription());
		return record;
	}

	@Override
	protected List<ClientTransactionItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientTransactionItem t) {
		return createFullRecord(t);
	}

	protected String getAddMoreString() {
		List<ClientTransactionItem> items = getValue();
		return items.isEmpty() ? "Add Items" : getMessages().addMore(
				getMessages().items());
	}
}
