package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.PaySalesTaxEntries;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientPaySalesTaxEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPaySalesTax;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

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
		list.add(new Requirement(TAX_AGENCY, true, true));
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

		result = accountRequirement(context, list, PAY_FROM, getConstants()
				.payFrom(), new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				if (e.getType() == ClientAccount.TYPE_BANK)
					return true;
				else
					return false;
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
		result = taxAgencyRequirement(context, list, TAX_AGENCY);
		if (result != null) {
			return result;
		}
		result = billsToPayRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createPaySalesTax(context);
	}

	private void setDefaultValues() {
		get(BILLS_DUE_ONBEFORE).setValue(new ClientFinanceDate());
		get(DATE).setValue(new ClientFinanceDate());
		get(ORDER_NO).setValue("1");
	}

	private Result createPaySalesTax(Context context) {
		ClientPaySalesTax paySalesTax = new ClientPaySalesTax();

		ClientAccount payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<com.vimukti.accounter.web.client.core.ClientTransactionPaySalesTax> billsToPay = get(
				BILLS_TO_PAY).getValue();
		ClientFinanceDate billsDueOnBefore = get(BILLS_DUE_ONBEFORE).getValue();
		ClientFinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		for (ClientTransactionPaySalesTax c : billsToPay) {
			c.setTransaction(paySalesTax);

		}
		paySalesTax.setPayFrom(payFrom.getID());
		paySalesTax.setPaymentMethod(paymentMethod);
		paySalesTax.setTransactionPaySalesTax(billsToPay);
		paySalesTax.setBillsDueOnOrBefore(billsDueOnBefore.getDate());
		paySalesTax.setDate(transactionDate.getDate());
		paySalesTax.setNumber(orderNo);
		paySalesTax.setType(ClientTransaction.TYPE_PAY_SALES_TAX);
		ClientTAXAgency agency = get(TAX_AGENCY).getValue();
		paySalesTax.setTaxAgency(agency.getID());

		create(paySalesTax, context);

		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(getConstants().payTax()));

		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
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

		Result result = dateOptionalRequirement(context, list, DATE,
				getConstants().transactionDate(),
				getMessages().pleaseEnter(getConstants().transactionDate()),
				selection);
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, BILLS_DUE_ONBEFORE,
				getConstants().billsDueOnOrBefore(),
				getMessages().pleaseEnter(getConstants().billsDueOnOrBefore()),
				selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, ORDER_NO,
				getConstants().no(),
				getMessages().pleaseEnter(getConstants().number()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().payTax()));
		actions.add(finish);

		return makeResult;
	}

	private Result billsToPayRequirement(Context context, Result result,
			ResultList actions) {
		Requirement billsToPayReq = get(BILLS_TO_PAY);
		List<ClientTransactionPaySalesTax> transactionPaySalesTaxBills = context
				.getSelections(BILLS_TO_PAY_LIST);
		if (!billsToPayReq.isDone()) {
			if (transactionPaySalesTaxBills != null
					&& transactionPaySalesTaxBills.size() > 0) {
				billsToPayReq.setValue(transactionPaySalesTaxBills);
			} else {
				return getBillsToPayResult(context);
			}
		}
		List<ClientTransactionPaySalesTax> transPaySalesTaxes = billsToPayReq
				.getValue();

		ClientTAXAgency agency = get(TAX_AGENCY).getValue();

		result.add(getConstants().payTax());
		ResultList paySalesTaxs = new ResultList("transactionPayTaxes");
		for (ClientTransactionPaySalesTax paySalesTax : transPaySalesTaxes) {
			if (paySalesTax.getTaxAgency() == agency.getID()) {
				Record itemRec = createTransactionPaySalesTaxRecord(paySalesTax);
				paySalesTaxs.add(itemRec);
			}

		}
		result.add(paySalesTaxs);
		return null;
	}

	private Result getBillsToPayResult(Context context) {
		Result result = context.makeResult();
		List<ClientTransactionPaySalesTax> transactionPayTaxes = getTransactionPaySalesTaxBills(
				context, context.getHibernateSession());
		ResultList list = new ResultList(BILLS_TO_PAY_LIST);
		Object last = context
				.getLast(RequirementType.TRANSACTION_PAY_SALES_TAX);
		int num = 0;

		if (last != null) {
			list.add(createTransactionPaySalesTaxRecord((ClientTransactionPaySalesTax) last));
			num++;
		}
		Requirement payBillsReq = get(BILLS_TO_PAY);
		List<ClientTransactionPaySalesTax> transPayTaxes = new ArrayList<ClientTransactionPaySalesTax>();
		transPayTaxes = payBillsReq.getValue();
		List<ClientTransactionPaySalesTax> availablePayTaxes = new ArrayList<ClientTransactionPaySalesTax>();
		if (transPayTaxes != null)
			for (ClientTransactionPaySalesTax transactionItem : transPayTaxes) {
				availablePayTaxes.add(transactionItem);
			}
		for (ClientTransactionPaySalesTax transactionPaySalesTax : transactionPayTaxes) {
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
			result.add(getMessages().selectTypeOfThis(getConstants().tax()));
		} else {
			result.add(getMessages().youDontHaveAny(getConstants().taxes()));
		}

		result.add(list);
		return result;
	}

	private List<ClientTransactionPaySalesTax> getTransactionPaySalesTaxBills(
			Context context, Session hibernateSession) {
		ClientFinanceDate date = (ClientFinanceDate) get(BILLS_DUE_ONBEFORE)
				.getValue();
		ArrayList<PaySalesTaxEntries> transactionPaySalesTaxEntriesList = null;
		try {
			transactionPaySalesTaxEntriesList = new FinanceTool()
					.getTaxManager().getTransactionPaySalesTaxEntriesList(
							date.getDate(), context.getCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		ClientTAXAgency taxAGency = get(TAX_AGENCY).getValue();
		List<ClientTransactionPaySalesTax> result = new ArrayList<ClientTransactionPaySalesTax>();

		for (PaySalesTaxEntries salesTaxEntry : transactionPaySalesTaxEntriesList) {

			if (salesTaxEntry.getTaxAgency().getName()
					.equals(taxAGency.getName())) {
				ClientPaySalesTaxEntries paySalesTxEntry = new ClientPaySalesTaxEntries();
				paySalesTxEntry.setID(salesTaxEntry.getID());
				paySalesTxEntry.setAmount(salesTaxEntry.getAmount());
				paySalesTxEntry.setBalance(salesTaxEntry.getBalance());
				// paySalesTxEntry.setStatus(salesTaxEntry.getTransaction()
				// .getStatus());
				paySalesTxEntry.setTaxAgency(salesTaxEntry.getTaxAgency()
						.getID());
				if (salesTaxEntry.getTaxRateCalculation() != null)
					paySalesTxEntry.setTaxRateCalculation(salesTaxEntry
							.getTaxRateCalculation().getID());
				if (salesTaxEntry.getTaxItem() != null)
					paySalesTxEntry.setTaxItem(salesTaxEntry.getTaxItem()
							.getID());
				if (salesTaxEntry.getTaxAdjustment() != null)
					paySalesTxEntry.setTaxAdjustment(salesTaxEntry
							.getTaxAdjustment().getID());

				// paySalesTxEntry.setTransaction(salesTaxEntry.getTransaction()
				// .getID());
				paySalesTxEntry.setTransactionDate(salesTaxEntry
						.getTransactionDate().getDate());

				ClientTransactionPaySalesTax c = new ClientTransactionPaySalesTax();
				c.setPaySalesTaxEntry(paySalesTxEntry);
				c.setTaxAgency(salesTaxEntry.getTaxAgency().getID());
				c.setAmountToPay(salesTaxEntry.getAmount());
				c.setTaxItem(salesTaxEntry.getTaxItem().getID());
				c.setTaxDue(salesTaxEntry.getAmount());
				c.setTaxRateCalculation(salesTaxEntry.getTaxRateCalculation()
						.getID());

				result.add(c);
			}

		}

		return result;
	}

	private Record createTransactionPaySalesTaxRecord(
			ClientTransactionPaySalesTax payTaxBill) {
		Record record = new Record(payTaxBill);
		long taxAgency = payTaxBill.getTaxAgency();
		record.add("", "TaxAgency");
		record.add("Tax Agency", getClientCompany().getTaxAgency(taxAgency)
				.getName());

		record.add("", "Tax Item");
		record.add("", getClientCompany().getTAXItem(payTaxBill.getTaxItem())
				.getName());
		record.add("", "Tax Due");
		record.add("", payTaxBill.getTaxDue());
		record.add("", "Amount to pay");
		record.add("", payTaxBill.getAmountToPay());
		return record;
	}
}
