package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionItemTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {
	private static final String QUANITY = "Quantity";
	private static final String ITEM = "Item";
	private static final String UNITPTICE = "UnitPrice";
	private static final String DISCOUNT = "Discount";
	private static final String TAXCODE = "TaxCode";
	private static final String TAX = "Tax";
	private static final String DESCRIPTION = "Description";
	private boolean isSales;

	public TransactionItemTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext, boolean isSales) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
		this.isSales = isSales;
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM,
				"Please Select an Item for Transaction", "Item", false, true,
				null) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getServiceItems();
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
				if (getClientCompany().getPreferences().isTrackTax()
						&& getClientCompany().getPreferences()
								.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return getClientCompany().getActiveTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		list.add(new BooleanRequirement(TAX, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& getClientCompany().getPreferences()
								.isTaxPerDetailLine()) {
					return null;
				} else {
					return super.run(context, makeResult, list, actions);
				}
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

		list.add(new StringRequirement(DESCRIPTION, "Please Enter Description",
				"Description", true, true));
	}

	@Override
	protected String getEmptyString() {
		return "There are no Transaction Items";
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		ClientItem clientItem = get(ITEM).getValue();
		obj.setItem(clientItem.getID());
		obj.getQuantity().setValue((Double) get(QUANITY).getValue());
		obj.setUnitPrice((Double) get(UNITPTICE).getValue());
		obj.setDiscount((Double) get(DISCOUNT).getValue());
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
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
		ClientItem item = getClientCompany().getItem(obj.getItem());
		get(ITEM).setValue(item);
		get(QUANITY).setDefaultValue(obj.getQuantity().getValue());
		if (item != null) {
			if (isSales) {
				get(UNITPTICE).setValue(item.getSalesPrice());
			} else {
				get(UNITPTICE).setValue(item.getPurchasePrice());
			}
		} else {
			get(UNITPTICE).setValue(obj.getUnitPrice());
		}

		get(DISCOUNT).setDefaultValue(obj.getDiscount());
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			get(TAXCODE).setDefaultValue(
					getClientCompany().getTAXCode(obj.getTaxCode()));
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
		record.add("", getClientCompany().getItem(t.getItem()).getDisplayName());
		record.add("", t.getQuantity());
		record.add("", t.getUnitPrice());
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			record.add("", getClientCompany().getTAXCode(t.getTaxCode())
					.getDisplayName());
		} else {
			if (t.isTaxable()) {
				record.add("", getConstants().taxable());
			} else {
				record.add("", getConstants().taxExempt());
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

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().items());
	}

}
