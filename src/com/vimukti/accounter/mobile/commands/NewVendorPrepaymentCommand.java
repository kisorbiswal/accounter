package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewVendorPrepaymentCommand extends AbstractTransactionCommand {

	private static final String PAY_TO = "vendors";
	private static final String AMOUNT = "amount";
	private static final String BILL_TO = "billTo";
	private static final String TO_BE_PRINTED = "toBePrinted";
	private static final String CHEQUE_NO = "chequeNumber";
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
		list.add(new Requirement(PAY_TO, false, true));
		list.add(new Requirement(PAY_FROM, false, true));
		list.add(new Requirement(BILL_TO, true, true));
		list.add(new Requirement(AMOUNT, false, true));
		list.add(new Requirement(PAYMENT_MENTHOD, false, true));
		list.add(new Requirement(TO_BE_PRINTED, true, true));
		list.add(new Requirement(CHEQUE_NO, true, true));
		list.add(new Requirement(MEMO, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;
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
			}
		}
		result = vendorRequirement(context);
		if (result != null) {
			return result;
		}
		result = payFromRequirement(context);
		if (result != null) {
			return result;
		}

		result = amountRequirement(context);
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context);
		if (result != null) {
			return result;
		}
		result = createOptionalRequirement(context);

		completeProcess(context);
		markDone();
		return result;

	}

	private void completeProcess(Context context) {

		PayBill paybill = new PayBill();
		Vendor vendor = (Vendor) get(PAY_TO).getValue();

		Address billTo = (Address) get(BILL_TO).getValue();
		Account pay = (Account) get(PAY_FROM).getValue();
		Double amount = (Double) get(AMOUNT).getValue();
		String paymentMethod = get(PAYMENT_MENTHOD).getValue();
		Boolean toBePrinted = (Boolean) get(TO_BE_PRINTED).getValue();
		String memo = get(MEMO).getValue();
		String chequeNumber = get(CHEQUE_NO).getValue();

		paybill.setVendor(vendor);
		paybill.setAddress(billTo);
		paybill.setPayFrom(pay);
		paybill.setUnusedAmount(amount);
		paybill.setPaymentMethod(paymentMethod);
		paybill.setMemo(memo);
		paybill.setToBePrinted(toBePrinted);
		paybill.setCheckNumber(chequeNumber);
		create(paybill, context);

	}

	private Result createOptionalRequirement(Context context) {

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
		ResultList list = new ResultList("values");

		Result result = null;
		Requirement vendorReq = get(PAY_TO);
		Vendor vendor = (Vendor) vendorReq.getValue();
		Record vendorRecord = new Record(vendor);
		vendorRecord.add("Name", PAY_TO);
		vendorRecord.add("Value", vendor.getName());

		list.add(vendorRecord);

		result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, NUMBER,
				"TtransactionNumber");
		if (result != null) {
			return result;
		}
		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		Requirement bankAccountReeq = get(PAY_FROM);
		Account account = (Account) bankAccountReeq.getValue();
		list.add(createAccountRecord(account));
		Requirement amountReq = get(AMOUNT);

		Double amount = (Double) amountReq.getValue();
		list.add(createAmountRecord(amount));

		result = paymentMethodRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = toBePrintedOptionalRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = chequeNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}
		result = context.makeResult();
		result.add("Vendor prepayment is ready to creating...");
		result.add(list);

		return result;
	}

	private Result toBePrintedOptionalRequirement(Context context,
			ResultList list, Object selection) {
		Requirement isActiveReq = get(TO_BE_PRINTED);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == isActive) {
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
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);
		return null;
	}

	private Record createAmountRecord(Double amount2) {
		Record amountRec = new Record(amount2);
		amountRec.add("Name", "");
		amountRec.add("Amount", amount2);

		return amountRec;
	}

	private Result amountRequirement(Context context) {
		Requirement numberReq = get(AMOUNT);
		if (!numberReq.isDone()) {
			String num = context.getString();
			if (num != null) {
				numberReq.setValue(num);
			} else {
				return text(context, "Please Enter amount ", "" + num);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(AMOUNT)) {
			numberReq.setValue(input);
		}
		return null;
	}
}
