package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionItemItemsRequirement extends
		AbstractTransactionItemsRequirement<ClientItem> {
	private static final String ITEM_PROPERTY_ATTR = "itemPropertyAttr";
	private static final Object QUANTITY = "quantity";
	private static final Object UNIT_PRICE = "unitPrice";
	private static final Object DISCOUNT = "discount";
	private static final String TAXCODE = "taxcode";
	private static final Object DESCRIPTION = "description";
	private static final String ITEM_DETAILS = "itemDetails";
	private static final Object TAX = "tax";
	private boolean isSales;

	public TransactionItemItemsRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext, boolean isSales) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
		this.isSales = isSales;
	}

	@Override
	protected Result checkItemToEdit(Context context,
			ClientTransactionItem transactionItem) {
		if (transactionItem.getUnitPrice() == 0) {
			context.putSelection(ITEM_DETAILS, UNIT_PRICE);
			Result transactionItemResult = transactionItem(context,
					transactionItem);
			if (transactionItemResult != null) {
				return transactionItemResult;
			}
		} else if (context.getCompany().getPreferences().isTrackTax()
				&& context.getCompany().getPreferences().isTaxPerDetailLine()
				&& transactionItem.getTaxCode() == 0) {
			context.putSelection(ITEM_DETAILS, TAXCODE);
			Result transactionItemResult = transactionItem(context,
					transactionItem);
			if (transactionItemResult != null) {
				return transactionItemResult;
			}
		}
		return null;
	}

	@Override
	protected Result transactionItem(Context context,
			ClientTransactionItem transactionItem) {
		context.setAttribute(PROCESS_ATTR, getName());
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR, transactionItem);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals(QUANTITY)) {
				if (context.getDouble() != null) {
					transactionItem.getQuantity().setValue(context.getDouble());
				} else {
					transactionItem.getQuantity().setValue(
							context.getInteger().doubleValue());
				}
			} else if (lineAttr.equals(UNIT_PRICE)) {
				if (context.getDouble() != null) {
					transactionItem.setUnitPrice(context.getDouble());
				} else {
					transactionItem.setUnitPrice(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(DISCOUNT)) {
				if (context.getDouble() != null) {
					transactionItem.setDiscount(context.getDouble());
				} else {
					transactionItem.setDiscount(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals(TAXCODE)) {
				ClientTAXCode taxCode = context.getSelection(TAXCODE);
				transactionItem.setTaxCode(taxCode.getID());
			} else if (lineAttr.equals(DESCRIPTION)) {
				transactionItem.setDescription(context.getString());
			}
		} else {
			Object selection = context.getSelection(ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals(QUANTITY)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, QUANTITY);
					return amount(context, "Enter Quantity", transactionItem
							.getQuantity().getValue());
				} else if (selection.equals(UNIT_PRICE)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, UNIT_PRICE);
					return amount(context, "Enter Unit Price",
							transactionItem.getUnitPrice());
				} else if (selection.equals(DISCOUNT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DISCOUNT);
					return amount(context, "Enter Discount",
							transactionItem.getDiscount());
				} else if (selection.equals(TAXCODE)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
					return taxCode(context, "Enter TaxCode", null);
				} else if (selection.equals(TAX)) {
					transactionItem.setTaxable(!transactionItem.isTaxable());
				} else if (selection.equals(DESCRIPTION)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DESCRIPTION);
					return number(context, "Enter Discription",
							transactionItem.getDescription());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getUnitPrice() == 0) {
						context.setAttribute(ITEM_PROPERTY_ATTR, UNIT_PRICE);
						return amount(context, "",
								transactionItem.getUnitPrice());
					} else if (context.getCompany().getPreferences()
							.isTrackTax()
							&& context.getCompany().getPreferences()
									.isTaxPerDetailLine()
							&& transactionItem.getTaxCode() == 0) {
						context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
						return taxCode(context, "", null);
					}
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}
		ResultList list = new ResultList(ITEM_DETAILS);
		Record record = new Record(QUANTITY);
		record.add("", "Quantity:");
		record.add("", transactionItem.getQuantity());
		list.add(record);

		record = new Record(UNIT_PRICE);
		record.add("", "Unit Price:");
		record.add("", transactionItem.getUnitPrice());
		list.add(record);

		record = new Record(DISCOUNT);
		record.add("", "Discount:");
		record.add("", transactionItem.getDiscount());
		list.add(record);

		// if (getClientCompany().getPreferences().isTrackTax()
		// && getClientCompany().getPreferences().isTaxPerDetailLine()) {
		// record = new Record(TAXCODE);
		// record.add("", getConstants().taxCode());
		// if (transactionItem.getTaxCode() != 0) {
		// record.add(
		// "",
		// getClientCompany().getTAXCode(
		// transactionItem.getTaxCode()).getName());
		// } else {
		// record.add("", "");
		// }
		// list.add(record);
		// } else {
		// record = new Record(TAX);
		// record.add("", getConstants().isTaxable());
		// if (transactionItem.isTaxable()) {
		// record.add("", getConstants().taxable());
		// } else {
		// record.add("", getConstants().taxExempt());
		// }
		// list.add(record);
		// }

		record = new Record(DESCRIPTION);
		record.add("", "Description:");
		record.add("", transactionItem.getDescription());
		list.add(record);

		Result result = context.makeResult();
		result.add("Item Details:");
		result.add("Name : " + getItemDisplayValue(transactionItem));
		result.add(list);

		// if (getClientCompany().getPreferences().isTaxPerDetailLine()) {
		// result.add(getConstants().vat() + transactionItem.getVATfraction());
		// }

		double lt = transactionItem.getQuantity().getValue()
				* transactionItem.getUnitPrice();
		double disc = transactionItem.getDiscount();
		transactionItem
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		result.add("Total" + transactionItem.getLineTotal());

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", "Delete");
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", "Finish");
		actions.add(record);
		result.add(actions);
		return result;
	}

	@Override
	protected String getAddMoreString() {
		return "Add More Items";
	}

	@Override
	protected String getEmptyString() {
		return "There are no Items";
	}

	@Override
	protected void setPrice(ClientTransactionItem transactionItem,
			ClientItem item) {
		transactionItem.setItem(item.getID());
	}

	@Override
	protected String getItemDisplayValue(ClientTransactionItem item) {
		return getClientCompany().getItem(item.getItem()).getDisplayName();
	}

	@Override
	protected void setItem(ClientTransactionItem transactionItem,
			ClientItem item) {
		if (isSales) {
			transactionItem.setUnitPrice(item.getSalesPrice());
		} else {
			transactionItem.setUnitPrice(item.getPurchasePrice());
		}
	}

	@Override
	protected Record createRecord(ClientItem value) {
		Record record = new Record(value);
		record.add("", value.getName());
		ClientTAXCode taxCode = getClientCompany().getTAXCode(
				value.getTaxCode());
		if (taxCode != null) {
			record.add("", taxCode.getName());
		}
		return record;
	}

	@Override
	protected String getDisplayValue(ClientItem value) {
		return value.getDisplayName();
	}

	@Override
	protected String getCreateCommandString() {
		return "Create Item";
	}

	@Override
	protected String getSelectString() {
		return "Slect an Item";
	}

	@Override
	protected ClientItem getTransactionItem(ClientTransactionItem item) {
		return getClientCompany().getItem(item.getItem());
	}

}
