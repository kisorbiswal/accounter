package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
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
		list.add(new Requirement(BILLS_TO_PAY, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = accountRequirement(context, PAY_FROM);
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context,null, null);
		if (result != null) {
			return result;
		}

		result = billsToPayRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		return createPaySalesTax(context);
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

	private Result createOptionalRequirement(Context context) {
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

		Requirement payFromReq = get(PAY_FROM);
		Account payFrom = (Account) payFromReq.getValue();
		if (payFrom == selection) {
			context.setAttribute(INPUT_ATTR, PAY_FROM);
			return getAccountResult(context);
		}

		Requirement paymentMethodReq = get(PAYMENT_METHOD);
		String paymentMethod = (String) paymentMethodReq.getValue();
		if (paymentMethod == selection) {
			context.setAttribute(INPUT_ATTR, PAYMENT_METHOD);
			return paymentMethod(context, paymentMethod);
		}

		Requirement billsToPayReq = get(BILLS_TO_PAY);
		List<TransactionPaySalesTax> transPaySalesTaxes = billsToPayReq
				.getValue();

		selection = context.getSelection("transactionPaySalesTaxs");
		if (selection != null) {
			Result result = getBillsToPayResult(context);
			if (result != null) {
				return result;
			}
		}

		ResultList list = new ResultList("values");

		Record payFromRecord = new Record(payFrom);
		payFromRecord.add("Name", "Pay From");
		payFromRecord.add("Value", payFrom.getName());
		list.add(payFromRecord);

		Record paymentMethodRecord = new Record(paymentMethod);
		paymentMethodRecord.add("Name", "Pay Method");
		paymentMethodRecord.add("Value", paymentMethod);
		list.add(paymentMethodRecord);

		Result result = dateOptionalRequirement(context, list,
				BILLS_DUE_ONBEFORE, "Filter by Sales Tax return end date",
				selection);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Pay SalesTax is ready to create with following values.");
		result.add(list);
		result.add("Bill To Pay:-");
		ResultList paySalesTaxs = new ResultList("transactionPayTaxes");
		for (TransactionPaySalesTax paySalesTax : transPaySalesTaxes) {
			Record itemRec = createTransactionPaySalesTaxRecord(paySalesTax);
			paySalesTaxs.add(itemRec);
		}
		result.add(paySalesTaxs);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_BILLS);
		moreItems.add("", "Add more bills");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to Pay Sales Tax.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result billsToPayRequirement(Context context) {
		Requirement billsToPayReq = get(BILLS_TO_PAY);
		List<TransactionPaySalesTax> transactionPaySalesTaxBills = context
				.getSelections(BILLS_TO_PAY);
		if (!billsToPayReq.isDone()) {
			if (transactionPaySalesTaxBills.size() > 0) {
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

	@Override
	protected List<ClientAccount> getAccounts() {
		// TODO Auto-generated method stub
		return super.getAccounts();
	}

}
