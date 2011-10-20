package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaySalesTax;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TransactionPaySalesTax;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ListFilter;

public class PaySalesTaxCommand extends AbstractVATCommand {

	private static final String BILLS_DUE_ONBEFORE = "billsDueOnOrBefore";
	private static final String BILLS_TO_PAY = "billToPay";
	private static final String BILLS_TO_PAY_LIST = "billsToPayList";
	private static final String PAY_FROM = "payFrom";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(PAY_FROM, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(BILLS_DUE_ONBEFORE, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(BILLS_TO_PAY, false, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();

		setDefaultValues();

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().payTax()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		// result = accountRequirement(context, list, PAY_FROM,
		// new ListFilter<ClientAccount>() {
		//
		// @Override
		// public boolean filter(ClientAccount e) {
		// return Arrays.asList(ClientAccount.TYPE_BANK,
		// ClientAccount.TYPE_OTHER_CURRENT_ASSET)
		// .contains(e.getType());
		// }
		// });
		// if (result != null) {
		// return result;
		// }

		// result = paymentMethodRequirement(context, list, PAYMENT_METHOD);
		// if (result != null) {
		// return result;
		// }

		result = billsToPayRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createPaySalesTax(context);
	}

	private void setDefaultValues() {
		get(BILLS_DUE_ONBEFORE).setValue(new Date());
		get(DATE).setValue(new Date());
		get(ORDER_NO).setValue("1");
	}

	private Result createPaySalesTax(Context context) {
		PaySalesTax paySalesTax = new PaySalesTax();

		Account payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<TransactionPaySalesTax> billsToPay = get(BILLS_TO_PAY).getValue();
		FinanceDate billsDueOnBefore = get(BILLS_DUE_ONBEFORE).getValue();
		FinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		paySalesTax.setPayFrom(payFrom);
		paySalesTax.setPaymentMethod(paymentMethod);
		paySalesTax.setTransactionPaySalesTax(billsToPay);
		paySalesTax.setBillsDueOnOrBefore(billsDueOnBefore);
		paySalesTax.setDate(transactionDate);
		paySalesTax.setNumber(orderNo);

		create(paySalesTax, context);

		markDone();
		Result result = new Result();
		result.add("Pay SalesTax created successfully.");

		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_BILLS:
				return getBillsToPayResult(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		selection = context.getSelection("transactionPaySalesTaxs");
		if (selection != null) {
			Result result = getBillsToPayResult(context);
			if (result != null) {
				return result;
			}
		}

		// Result result = dateOptionalRequirement(context, list,
		// BILLS_DUE_ONBEFORE, "Filter by Sales Tax return end date",
		// selection);
		// if (result != null) {
		// return result;
		// }

		// result = dateRequirement(context, list, selection, DATE,
		// "Enter the date");
		// if (result != null) {
		// return result;
		// }
		//
		// result = orderNoRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to Pay Sales Tax.");
		actions.add(finish);

		return makeResult;
	}

	private Result billsToPayRequirement(Context context, Result result,
			ResultList actions) {
		Requirement billsToPayReq = get(BILLS_TO_PAY);
		List<TransactionPaySalesTax> transactionPaySalesTaxBills = context
				.getSelections(BILLS_TO_PAY_LIST);
		if (!billsToPayReq.isDone()) {
			if (transactionPaySalesTaxBills != null
					&& transactionPaySalesTaxBills.size() > 0) {
				billsToPayReq.setValue(transactionPaySalesTaxBills);
			} else {
				return getBillsToPayResult(context);
			}
		}
		if (transactionPaySalesTaxBills != null
				&& transactionPaySalesTaxBills.size() > 0) {
			List<TransactionPaySalesTax> items = billsToPayReq.getValue();
			items.addAll(transactionPaySalesTaxBills);
		}

		List<TransactionPaySalesTax> transPaySalesTaxes = billsToPayReq
				.getValue();

		result.add("Bill To Pay:-");
		ResultList paySalesTaxs = new ResultList("transactionPayTaxes");
		for (TransactionPaySalesTax paySalesTax : transPaySalesTaxes) {
			Record itemRec = createTransactionPaySalesTaxRecord(paySalesTax);
			paySalesTaxs.add(itemRec);
		}
		result.add(paySalesTaxs);
		return null;
	}

	private Result getBillsToPayResult(Context context) {
		Result result = context.makeResult();
		List<TransactionPaySalesTax> transactionPayTaxes = getTransactionPaySalesTaxBills(context
				.getHibernateSession());
		ResultList list = new ResultList(BILLS_TO_PAY_LIST);
		Object last = context
				.getLast(RequirementType.TRANSACTION_PAY_SALES_TAX);
		int num = 0;
		if (last != null) {
			list.add(createTransactionPaySalesTaxRecord((TransactionPaySalesTax) last));
			num++;
		}
		Requirement payBillsReq = get(BILLS_TO_PAY_LIST);
		List<TransactionPaySalesTax> transPayTaxes = payBillsReq.getValue();
		List<TransactionPaySalesTax> availablePayTaxes = new ArrayList<TransactionPaySalesTax>();
		for (TransactionPaySalesTax transactionItem : transPayTaxes) {
			availablePayTaxes.add(transactionItem);
		}
		for (TransactionPaySalesTax transactionPaySalesTax : transactionPayTaxes) {
			if (transactionPaySalesTax != last
					|| !availablePayTaxes.contains(transactionPaySalesTax)) {
				list.add(createTransactionPaySalesTaxRecord(transactionPaySalesTax));
				num++;
			}
			if (num == VALUES_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add("Slect Bill to pay.");
		} else {
			result.add("You don't have Bills.");
		}

		result.add(list);
		return result;
	}

	private List<TransactionPaySalesTax> getTransactionPaySalesTaxBills(
			Session hibernateSession) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createTransactionPaySalesTaxRecord(
			TransactionPaySalesTax payTaxBill) {
		Record record = new Record(payTaxBill);
		TAXAgency taxAgency = payTaxBill.getTaxAgency();
		record.add("Tax Agency", taxAgency != null ? taxAgency.getName() : "");
		record.add("Tax Due", payTaxBill.getTaxDue());
		record.add("Amount to pay", payTaxBill.getAmountToPay());
		return record;
	}

}
