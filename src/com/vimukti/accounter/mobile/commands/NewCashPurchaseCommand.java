package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCashPurchaseCommand extends AbstractTransactionCommand {

	private static final String CHEQUE_NO = "chequeNo";
	private static final String DELIVERY_DATE = "deliveryDate";

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
				list.add(new Requirement("amount", false, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement(BILL_TO, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(MEMO, true, true));
		list.add(new Requirement(DEPOSIT_OR_TRANSFER_TO, false, true));
		list.add(new Requirement(CHEQUE_NO, true, true));
		list.add(new Requirement(DELIVERY_DATE, true, true));
	}

	@Override
	public Result run(Context context) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(TRANSACTION_ITEM_PROCESS)) {
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

		setDefaultValues();

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().cashPurchase()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = context.makeResult();

		result = createSupplierRequirement(context, list, SUPPLIER, Global
				.get().Vendor());
		if (result != null) {
			return result;
		}

		result = itemsAndAccountsRequirement(context, makeResult, actions,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return e.getIsActive();
					}
				});
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context, list, PAYMENT_METHOD,
				getConstants().paymentMethod());
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, DEPOSIT_OR_TRANSFER_TO,
				getConstants().account(), new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET)
								.contains(e.getType());
					}
				});
		if (result != null) {
			return result;
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return completeProcess(context);
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(PAYMENT_METHOD).setDefaultValue("cash");
	}

	private Result completeProcess(Context context) {
		ClientCashPurchase cashPurchase = new ClientCashPurchase();
		ClientFinanceDate date = get(DATE).getValue();
		cashPurchase.setDate(date.getDate());

		cashPurchase.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		String number = get(NUMBER).getValue();
		cashPurchase.setNumber(number);

		// FIXME
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		for (ClientTransactionItem item : items) {
			ClientItem clientItem = getClientCompany().getItem(item.getItem());
			ClientAccount account = getClientCompany().getAccount(
					clientItem.getIncomeAccount());
			if (account != null) {
				clientItem.setIncomeAccount(account.getID());
			} else {
				account = getClientCompany().getAccount(
						clientItem.getExpenseAccount());
				clientItem.setExpenseAccount(account.getID());
			}
		}

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		for (ClientTransactionItem item : accounts) {
			ClientAccount account = getClientCompany().getAccount(
					item.getAccount());
			item.setAccount(account.getID());
		}

		accounts.addAll(items);
		cashPurchase.setTransactionItems(accounts);

		// TODO Location
		// TODO Class

		if (getClientCompany().getPreferences().isTrackTax()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		ClientVendor vendor = get(SUPPLIER).getValue();
		cashPurchase.setVendor(vendor.getID());

		ClientContact contact = get(CONTACT).getValue();
		cashPurchase.setContact(contact);

		// TODO Payments

		String phone = get(PHONE).getValue();
		cashPurchase.setPhone(phone);

		String memo = get(MEMO).getValue();
		cashPurchase.setMemo(memo);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashPurchase.setPaymentMethod(paymentMethod);

		ClientAccount account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		cashPurchase.setPayFrom(account.getID());

		String chequeNo = get(CHEQUE_NO).getValue();
		if (paymentMethod.equals(US_CHECK) || paymentMethod.equals(UK_CHECK)) {
			cashPurchase.setCheckNumber(chequeNo);
		}

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		cashPurchase.setDeliveryDate(deliveryDate.getDate());

		updateTotals(cashPurchase);
		create(cashPurchase, context);
		markDone();

		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().cashPurchase()));
		return result;
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
			case ADD_MORE_ITEMS:
				return items(context);
			case ADD_MORE_ACCOUNTS:
				return accountItems(context, ACCOUNTS,
						new ListFilter<ClientAccount>() {
							@Override
							public boolean filter(ClientAccount e) {
								return e.getIsActive();
							}
						});
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Requirement supplierReq = get(SUPPLIER);
		ClientVendor supplier = (ClientVendor) supplierReq.getValue();

		Result result = dateOptionalRequirement(context, list, DELIVERY_DATE,
				getConstants().deliveryDate(),
				getMessages().pleaseEnter(getConstants().date()), selection);
		if (result != null) {
			return result;
		}

		result = contactRequirement(context, list, selection, supplier);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				getConstants().phoneNumber(),
				getMessages().pleaseEnter(getConstants().purchaseNumber()));
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, DATE, getConstants()
				.date(), getMessages().pleaseEnter(getConstants().date()),
				selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, PHONE,
				getConstants().phoneNumber(),
				getMessages().pleaseEnter(getConstants().phone()));
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, CHEQUE_NO, getMessages()
				.pleaseEnter(getConstants().checkNo()), getConstants()
				.checkNo());
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				getConstants().memo(), getConstants().addMemo());
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().cashPurchase()));
		actions.add(finish);

		return makeResult;
	}

}
