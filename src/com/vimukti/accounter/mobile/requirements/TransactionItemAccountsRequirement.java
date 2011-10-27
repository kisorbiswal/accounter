package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionItemAccountsRequirement extends
		AbstractTransactionItemsRequirement<ClientAccount> {

	private static final String ITEM_DETAILS = "itemDetails";
	private static final String AMOUNT = "amount";
	private static final String TAXCODE = "taxCode";
	private static final String ITEM_PROPERTY_ATTR = "itemPropertyAttr";
	private static final String DISCOUNT = "discount";
	private static final String DESCRIPTION = "description";
	private static final String TAX = "tax";

	public TransactionItemAccountsRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
	}

	@Override
	protected String getSetMessage() {
		return "Account is selected.";
	}

	@Override
	protected ClientAccount getTransactionItem(ClientTransactionItem item) {
		return getClientCompany().getAccount(item.getAccount());
	}

	@Override
	protected void setPrice(ClientTransactionItem transactionItem,
			ClientAccount item) {
	}

	@Override
	protected void setItem(ClientTransactionItem transactionItem,
			ClientAccount item) {
		transactionItem.setAccount(item.getID());
	}

	@Override
	protected String getEmptyString() {
		return "There are no accounts";
	}

	@Override
	protected Result transactionItem(Context context,
			ClientTransactionItem transactionItem) {
		context.setAttribute(PROCESS_ATTR, getName());
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR, transactionItem);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals(AMOUNT)) {
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
				if (selection.equals(AMOUNT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, AMOUNT);
					return amount(
							context,
							getMessages().pleaseEnterThe(
									getItemName(transactionItem),
									getConstants().amount()),
							transactionItem.getUnitPrice());
				} else if (selection.equals(DISCOUNT)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DISCOUNT);
					return amount(
							context,
							getMessages().pleaseEnterThe(
									getItemName(transactionItem),
									getConstants().discount()),
							transactionItem.getDiscount());
				} else if (selection.equals(TAXCODE)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
					return taxCode(
							context,
							getMessages().pleaseEnterThe(
									getItemName(transactionItem),
									getConstants().taxCode()),
							getClientCompany().getTAXCode(
									transactionItem.getTaxCode()));
				} else if (selection.equals(TAX)) {
					transactionItem.setTaxable(!transactionItem.isTaxable());
				} else if (selection.equals(DESCRIPTION)) {
					context.setAttribute(ITEM_PROPERTY_ATTR, DESCRIPTION);
					return show(
							context,
							getMessages().pleaseEnterThe(
									getItemName(transactionItem),
									getConstants().description()),
							transactionItem.getDescription());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getUnitPrice() == 0) {
						context.setAttribute(ITEM_PROPERTY_ATTR, AMOUNT);
						return amount(
								context,
								getMessages().pleaseEnterThe(
										getItemName(transactionItem),
										getConstants().amount()),
								transactionItem.getUnitPrice());
					} else if (context.getCompany().getPreferences()
							.isTrackTax()
							&& context.getCompany().getPreferences()
									.isTaxPerDetailLine()
							&& transactionItem.getTaxCode() == 0) {
						context.setAttribute(ITEM_PROPERTY_ATTR, TAXCODE);
						return taxCode(
								context,
								getMessages().pleaseEnterThe(
										getItemName(transactionItem),
										getConstants().taxCode()),
								getClientCompany().getTAXCode(
										transactionItem.getTaxCode()));
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
		Record record = new Record(AMOUNT);
		record.add("", getConstants().amount());
		record.add("", transactionItem.getUnitPrice());
		list.add(record);

		record = new Record(DISCOUNT);
		record.add("", getConstants().discount());
		record.add("", transactionItem.getDiscount());
		list.add(record);
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			record = new Record(TAXCODE);
			record.add("", getConstants().taxCode());
			if (transactionItem.getTaxCode() != 0) {
				ClientTAXCode code = getClientCompany().getTAXCode(
						transactionItem.getTaxCode());
				if (code != null) {
					record.add("", code.getName());
				}
			} else {
				record.add("", "");
			}
			list.add(record);
		} else {
			record = new Record(TAX);
			record.add("", getConstants().tax());
			if (transactionItem.isTaxable()) {
				record.add("", getConstants().taxable());
			} else {
				record.add("", getConstants().nonTaxable());
			}
			list.add(record);
		}

		record = new Record(DESCRIPTION);
		record.add("", getConstants().description());
		record.add("", transactionItem.getDescription());
		list.add(record);

		// TODO NEED TO CALCULATE LINE TOTAL
		double lineTotal = transactionItem.getUnitPrice()
				- ((transactionItem.getUnitPrice() * transactionItem
						.getDiscount()) / 100);
		transactionItem.setLineTotal(lineTotal);
		Result result = context.makeResult();
		result.add(getMessages().details(Global.get().Account()));

		ClientAccount account = getClientCompany().getAccount(
				transactionItem.getAccount());

		result.add(getMessages().accountName(Global.get().Account()) + ": "
				+ account.getName());

		double lt = transactionItem.getUnitPrice();
		double disc = transactionItem.getDiscount();
		transactionItem
				.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
		result.add(getConstants().total() + transactionItem.getLineTotal());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", getConstants().delete());
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", getConstants().finish());
		actions.add(record);
		result.add(actions);
		return result;
	}

	@Override
	protected Result checkItemToEdit(Context context,
			ClientTransactionItem transactionItem) {
		if (transactionItem.getUnitPrice() == 0) {
			context.putSelection(ITEM_DETAILS, AMOUNT);
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
	protected String getAddMoreString() {
		return "Add More Accounts";
	}

	@Override
	protected String getItemDisplayValue(ClientTransactionItem item) {
		return getTransactionItem(item).getDisplayName();
	}

	@Override
	protected Record createRecord(ClientAccount value) {
		Record record = new Record(value);
		record.add("", getMessages().accountNumber(Global.get().Account()));
		record.add("", value.getNumber());
		record.add("", getMessages().accountName(Global.get().Account()));
		record.add("", value.getName());
		record.add("", getMessages().accountType(Global.get().Account()));
		record.add("", getAccountTypeString(value.getType()));
		record.add("", getConstants().balance());
		record.add("", value.getTotalBalance());
		return record;
	}

	protected String getAccountTypeString(int accountType) {

		String accountTypeName = null;
		switch (accountType) {
		case ClientAccount.TYPE_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_INCOME;
			break;
		case ClientAccount.TYPE_OTHER_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_INCOME;
			break;
		case ClientAccount.TYPE_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_EXPENSE;
			break;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_EXPENSE;
			break;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			accountTypeName = AccounterClientConstants.TYPE_COST_OF_GOODS_SOLD;
			break;
		case ClientAccount.TYPE_CASH:
			accountTypeName = AccounterClientConstants.TYPE_CASH;
			break;
		case ClientAccount.TYPE_BANK:
			accountTypeName = AccounterClientConstants.TYPE_BANK;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_ASSET;
			break;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_INVENTORY_ASSET;
			break;
		case ClientAccount.TYPE_OTHER_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_ASSET;
			break;
		case ClientAccount.TYPE_FIXED_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_FIXED_ASSET;
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			accountTypeName = AccounterClientConstants.TYPE_CREDIT_CARD;
			break;
		case ClientAccount.TYPE_PAYPAL:
			accountTypeName = AccounterClientConstants.TYPE_PAYPAL;
			break;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_PAYROLL_LIABILITY;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_LIABILITY;
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_LONG_TERM_LIABILITY;
			break;
		case ClientAccount.TYPE_EQUITY:
			accountTypeName = AccounterClientConstants.TYPE_EQUITY;
			break;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_RECEIVABLE;
			break;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_PAYABLE;
			break;

		}
		return accountTypeName;
	}

	@Override
	protected boolean filter(ClientAccount e, String name) {
		return e.getName().contains(name) || e.getNumber().equals(name);
	}

	@Override
	protected String getDisplayValue(ClientAccount value) {
		return value.getDisplayName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("Create Acount");
	}

	@Override
	protected String getSelectString() {
		return "Select an account";
	}

}
