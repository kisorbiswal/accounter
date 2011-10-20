package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCreditCardChargeCommond extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(SUPPLIER, false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new ObjectListRequirement(ACCOUNTS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", false, true));
				list.add(new Requirement("price", false, true));
			}
		});
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(MEMO, true, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement("payFrom", false, true));
		list.add(new Requirement("deliveryDate", true, true));
		list.add(new Requirement(TAXCODE, false, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = context.makeResult();
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ACCOUNT_ITEM_PROCESS)) {
				result = transactionAccountProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().creditCardCharge()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		setTransactionType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
		result = createSupplierRequirement(context, list, SUPPLIER);
		if (result != null) {
			return result;
		}

		result = itemsAndAccountsRequirement(context, makeResult, actions,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				});
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, "payFrom",
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return account.getIsActive()
								&& Arrays.asList(ClientAccount.TYPE_BANK,
										ClientAccount.TYPE_OTHER_CURRENT_ASSET)
										.contains(account.getType());
					}
				});
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			result = taxCodeRequirement(context, list);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(PHONE).setDefaultValue("");
		ClientContact contact = new ClientContact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue("");
		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get(PAYMENT_METHOD).setDefaultValue(getConstants().creditCard());
	}

	private void completeProcess(Context context) {

		ClientCreditCardCharge creditCardCharge = new ClientCreditCardCharge();

		ClientVendor supplier = get(SUPPLIER).getValue();
		creditCardCharge.setVendor(supplier.getID());

		ClientContact contact = get(CONTACT).getValue();
		creditCardCharge.setContact(contact);

		Date date = get(DATE).getValue();
		creditCardCharge.setDate(new FinanceDate(date).getDate());

		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

		String number = get(NUMBER).getValue();
		creditCardCharge.setNumber(number);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		creditCardCharge.setPaymentMethod(paymentMethod);

		String phone = get(PHONE).getValue();
		creditCardCharge.setPhone(phone);

		ClientAccount account = get("payFrom").getValue();
		creditCardCharge.setPayFrom(account.getID());

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		items.addAll(accounts);
		creditCardCharge.setTransactionItems(items);
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		String memo = get(MEMO).getValue();
		creditCardCharge.setMemo(memo);
		updateTotals(creditCardCharge);
		create(creditCardCharge, context);
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Result result = dateOptionalRequirement(context, list, DATE,
				getMessages().pleaseEnter(getConstants().date()), selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(
				context,
				list,
				selection,
				NUMBER,
				getMessages().pleaseEnter(
						getConstants().cashSale() + getConstants().number()));
		if (result != null) {
			return result;
		}
		Requirement vendorReq = get(SUPPLIER);
		ClientVendor vendor = vendorReq.getValue();
		result = contactRequirement(context, list, selection, vendor);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, PHONE,
				getMessages().pleaseEnter(getConstants().phoneNumber()));
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, "deliveryDate",
				getMessages().pleaseEnter(getConstants().deliveryDate()),
				selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				getMessages().pleaseEnter(getConstants().memo()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().creditCardCharge()));
		actions.add(finish);
		return makeResult;
	}

}
