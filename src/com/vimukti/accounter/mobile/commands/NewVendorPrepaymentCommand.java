package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.Transaction;
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
		list.add(new Requirement(VENDOR, false, true));
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

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
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
		setDefaultValues(context);
		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return result;

	}

	private void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new Date());

		get(NUMBER)
				.setDefaultValue(
						NumberUtils.getNextTransactionNumber(
								Transaction.TYPE_RECEIVE_PAYMENT,
								context.getCompany()));

		get(BILL_TO).setDefaultValue(new Address());
		get(TO_BE_PRINTED).setDefaultValue(Boolean.FALSE);
		get(CHEQUE_NO).setDefaultValue(" ");
		get(MEMO).setDefaultValue("");
	}

	private void completeProcess(Context context) {

		PayBill paybill = new PayBill();
		Vendor vendor = (Vendor) get(VENDOR).getValue();

		vendor = (Vendor) context.getHibernateSession().merge(vendor);
		paybill.setCompany(context.getCompany());
		Address billTo = (Address) get(BILL_TO).getValue();
		Account pay = (Account) get(PAY_FROM).getValue();
		pay = (Account) context.getHibernateSession().merge(pay);
		String amount = (String) get(AMOUNT).getValue();
		String paymentMethod = get(PAYMENT_MENTHOD).getValue();
		Boolean toBePrinted = (Boolean) get(TO_BE_PRINTED).getValue();
		String memo = get(MEMO).getValue();
		String chequeNumber = get(CHEQUE_NO).getValue();

		Date transactionDate = (Date) get(DATE).getValue();
		paybill.setDate(new FinanceDate(transactionDate));
		paybill.setType(Transaction.TYPE_PAY_BILL);
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

		Requirement vendorReq = get(VENDOR);
		Vendor vendor = (Vendor) vendorReq.getValue();
		Record vendorRecord = new Record(vendor);
		vendorRecord.add("Name", VENDOR);
		vendorRecord.add("Value", vendor.getName());

		list.add(vendorRecord);

		Result result = dateOptionalRequirement(context, list, DATE,
				"Enter TransactionDate", selection);
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

		String amount = (String) amountReq.getValue();
		list.add(createAmountRecord(amount));

		String method = get(PAYMENT_MENTHOD).getValue();

		Record m = new Record(method);
		m.add("", PAYMENT_MENTHOD);
		m.add("", method);
		list.add(m);

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

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Bill.");
		ResultList actions = new ResultList(ACTIONS);
		actions.add(finish);
		result.add(actions);

		return result;
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

	private Record createAmountRecord(String amount2) {
		Record amountRec = new Record(amount2);
		amountRec.add("", AMOUNT);

		amountRec.add("", amount2);

		return amountRec;
	}

	private Result amountRequirement(Context context) {
		Requirement numberReq = get(AMOUNT);
		if (numberReq.isDone())
			return null;
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(AMOUNT)) {
			numberReq.setValue(input);
			String num = context.getString();
			if (num != null) {
				numberReq.setValue(num);
			}
		} else {
			context.setAttribute(INPUT_ATTR, AMOUNT);
			return text(context, "Please Enter amount ", "");
		}

		return null;
	}
}
