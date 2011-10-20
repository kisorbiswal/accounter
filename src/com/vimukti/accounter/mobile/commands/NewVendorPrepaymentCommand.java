package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewVendorPrepaymentCommand extends AbstractTransactionCommand {

	private static final String AMOUNT = "amount";
	private static final String BILL_TO = "address";
	private static final String TO_BE_PRINTED = "toBePrinted";
	private static final String CHEQUE_NO = "chequeNo";
	private static final String MEMO = "memo";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, true));
		list.add(new Requirement(SUPPLIER, false, true));
		list.add(new Requirement(PAY_FROM, false, true));
		list.add(new Requirement(BILL_TO, true, true));
		list.add(new Requirement(AMOUNT, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(TO_BE_PRINTED, true, true));
		list.add(new Requirement(CHEQUE_NO, true, true));
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues(context);
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();
		String process = (String) context.getAttribute(PROCESS_ATTR);
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
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);
		makeResult.add(getMessages().readyToCreate(
				Global.get().vendor() + getConstants().payment()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		setTransactionType(VENDOR_TRANSACTION);

		result = createSupplierRequirement(context, list, SUPPLIER);
		if (result != null) {
			return result;
		}
		result = accountRequirement(context, list, PAY_FROM,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				});
		if (result != null) {
			return result;
		}
		result = amountRequirement(context, list, AMOUNT, getMessages()
				.pleaseEnter(getConstants().amount()));
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_METHOD);
		if (result != null) {
			return result;
		}
		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		result = new Result();
		result.add(getMessages().createSuccessfully(Global.get().vendor())
				+ getConstants().prePayment());
		return result;
	}

	private void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(
				new ClientFinanceDate(System.currentTimeMillis()));

		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_RECEIVE_PAYMENT,
						context.getCompany()));

		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(TO_BE_PRINTED).setDefaultValue(Boolean.FALSE);
		get(CHEQUE_NO).setDefaultValue(" ");
		get(MEMO).setDefaultValue("");
	}

	private void completeProcess(Context context) {

		ClientPayBill paybill = new ClientPayBill();
		ClientVendor vendor = (ClientVendor) get(SUPPLIER).getValue();

		vendor = (ClientVendor) context.getHibernateSession().merge(vendor);
		ClientAddress billTo = (ClientAddress) get(BILL_TO).getValue();
		ClientAccount pay = (ClientAccount) get(PAY_FROM).getValue();
		pay = (ClientAccount) context.getHibernateSession().merge(pay);
		String amount = (String) get(AMOUNT).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		Boolean toBePrinted = (Boolean) get(TO_BE_PRINTED).getValue();
		String memo = get(MEMO).getValue();
		String chequeNumber = get(CHEQUE_NO).getValue();

		ClientFinanceDate transactionDate = get(DATE).getValue();
		paybill.setDate(transactionDate.getDate());
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setVendor(vendor);
		paybill.setAddress(billTo);
		paybill.setPayFrom(pay);
		paybill.setUnusedAmount(Double.parseDouble(amount));
		paybill.setPaymentMethod(paymentMethod);
		paybill.setMemo(memo);
		paybill.setToBePrinted(toBePrinted);
		paybill.setCheckNumber(chequeNumber);
		create(paybill, context);

	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Result result = null;
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		result = dateOptionalRequirement(context, list, DATE, getMessages()
				.pleaseEnter(getConstants().transactionDate()), getConstants()
				.transactionDate(), selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, NUMBER,
				getMessages().pleaseEnter(getConstants().number()));
		if (result != null) {
			return result;
		}
		result = addressOptionalRequirement(context, list, selection, BILL_TO,
				getMessages().pleaseEnter(getConstants().billTo()));
		if (result != null) {
			return result;
		}

		result = toBePrintedOptionalRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, CHEQUE_NO,
				getMessages().pleaseEnter(getConstants().checkNo()));
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				getMessages().pleaseEnter(getConstants().memo()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add(
				"",
				getMessages().finishToCreate(
						Global.get().vendor() + getConstants().prePayment()));
		actions.add(finish);
		return makeResult;
	}

	private Result toBePrintedOptionalRequirement(Context context,
			ResultList list, Object selection) {
		Requirement isActiveReq = get(TO_BE_PRINTED);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == TO_BE_PRINTED) {
			context.setAttribute(INPUT_ATTR, TO_BE_PRINTED);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This is To be printed";
		} else {
			activeString = "Not";
		}
		Record isActiveRecord = new Record(TO_BE_PRINTED);
		isActiveRecord.add("", TO_BE_PRINTED);
		isActiveRecord.add("", activeString);
		list.add(isActiveRecord);
		return null;
	}
}
