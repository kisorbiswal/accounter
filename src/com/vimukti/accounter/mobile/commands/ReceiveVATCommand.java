package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.ReceiveVAT;
import com.vimukti.accounter.core.ReceiveVATEntries;
import com.vimukti.accounter.core.TransactionReceiveVAT;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionReceiveVAT;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

public class ReceiveVATCommand extends AbstractVATCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_RECEIVE = "billToReceive";
	private static final String BILLS_TO_RECEIVE_LIST = "billToReceiveList";
	private static final String DEPOSIT_TO = "depositTo";
	private static final String RECEIVAT_TRANSACTION_ITEM_PROCESS = "receivevattransactionitemprocess";
	private static final String OLD_RECEIVAT_TRANSACTION_ITEM_PROCESS = "oldreceivevattransactionitemprocess";
	private static final String RECEIVAT_TRANSACTION_ITEM_ATTR = "receivevattransactionitemprocess";
	private static final String RECEIVAT_TRANSACTION_ITEM_DETAILS = "receivevattransactionitemdetails";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(DEPOSIT_TO, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(VAT_RETURN_END_DATE, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(BILLS_TO_RECEIVE, false, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		Result result = context.makeResult();
		Result makeResult = context.makeResult();
		makeResult
				.add(getMessages().readyToCreate(getConstants().receiveVAT()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		final long accountsReceivableAccountId = getClientCompany()
				.getAccountsReceivableAccountId();
		result = accountRequirement(context, list, DEPOSIT_TO, getConstants()
				.account(), new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount account) {
				return Arrays.asList(ClientAccount.TYPE_BANK,
						ClientAccount.TYPE_CREDIT_CARD,
						ClientAccount.TYPE_OTHER_CURRENT_ASSET,
						ClientAccount.TYPE_FIXED_ASSET).contains(
						account.getType())
						&& account.getID() != accountsReceivableAccountId;
			}
		});
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context, list, PAYMENT_METHOD,
				getConstants().payMethod());
		if (result != null) {
			return result;
		}

		result = billsToReceiveRequirement(context, list);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createPayVat(context);
	}

	private void setDefaultValues() {
		get(PAYMENT_METHOD).setDefaultValue("Cash");
		get(VAT_RETURN_END_DATE).setDefaultValue(new FinanceDate());
		get(DATE).setDefaultValue(new FinanceDate());
		get(ORDER_NO).setDefaultValue("1");
	}

	private Result createPayVat(Context context) {
		ReceiveVAT receiveVAT = new ReceiveVAT();

		Account depositTo = get(DEPOSIT_TO).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<TransactionReceiveVAT> billsToReceive = get(BILLS_TO_RECEIVE)
				.getValue();
		FinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		FinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		receiveVAT.setDepositIn(depositTo);
		receiveVAT.setPaymentMethod(paymentMethod);
		receiveVAT.setTransactionReceiveVAT(billsToReceive);
		receiveVAT.setReturnsDueOnOrBefore(vatReturnDate);
		receiveVAT.setDate(transactionDate);
		receiveVAT.setNumber(orderNo);

		create(receiveVAT, context);

		markDone();
		Result result = new Result();
		result.add(getMessages()
				.createSuccessfully(getConstants().receiveVAT()));

		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_BILLS:
				return getBillsToReceiveResult(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Requirement paymentMethodReq = get(PAYMENT_METHOD);
		String paymentMethod = (String) paymentMethodReq.getValue();
		if (paymentMethod == selection) {
			context.setAttribute(INPUT_ATTR, PAYMENT_METHOD);
			return paymentMethod(context, paymentMethod);
		}

		Requirement billsToReceiveReq = get(BILLS_TO_RECEIVE);
		List<TransactionReceiveVAT> transReceiveVats = billsToReceiveReq
				.getValue();

		selection = context.getSelection("transactionReceiveVats");
		if (selection != null) {
			Result result = getBillsToReceiveResult(context);
			if (result != null) {
				return result;
			}
		}

		// ResultList list = new ResultList("values");

		Record paymentMethodRecord = new Record(paymentMethod);
		paymentMethodRecord.add("Name", "Pay Method");
		paymentMethodRecord.add("Value", paymentMethod);
		list.add(paymentMethodRecord);

		// Result result = dateOptionalRequirement(context, list,
		// VAT_RETURN_END_DATE, "Filter by VAT return end date", selection);
		// if (result != null) {
		// return result;
		// }
		//
		// result = dateOptionalRequirement(context, list, DATE,
		// "Enter the date",
		// selection);
		// if (result != null) {
		// return result;
		// }
		//
		// result = numberOptionalRequirement(context, list, selection,
		// ORDER_NO,
		// "Enter Receive Vat Number");
		// if (result != null) {
		// return result;
		// }

		Result result = context.makeResult();
		result.add("Receive Vat is ready to create with following values.");
		result.add(list);
		result.add("Bill To Receive:-");
		ResultList payVats = new ResultList("transactionReceiveVats");
		for (TransactionReceiveVAT payVat : transReceiveVats) {
			// Record itemRec = createTransactionReceiveVatRecord(payVat);
			// payVats.add(itemRec);
		}
		result.add(payVats);

		// ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_BILLS);
		moreItems.add("", "Add more bills");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to Receive vat.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result billsToReceiveRequirement(Context context, ResultList list) {
		Requirement billsToReceiveReq = get(BILLS_TO_RECEIVE);
		List<ClientTransactionReceiveVAT> transactionReceiveVatBills = context
				.getSelections(BILLS_TO_RECEIVE_LIST);
		List<ClientTransactionReceiveVAT> transactionItems = billsToReceiveReq
				.getValue();
		if (transactionReceiveVatBills != null
				&& transactionReceiveVatBills.size() > 0) {
			for (ClientTransactionReceiveVAT account : transactionReceiveVatBills) {
				if (account.getAmountToReceive() == 0) {
					Result result = receiveVatItem(context, account);
					if (result != null) {
						return result;
					}
				}
			}
		}

		if (!billsToReceiveReq.isDone()) {
			if (transactionReceiveVatBills != null
					&& transactionReceiveVatBills.size() > 0) {
				billsToReceiveReq.setValue(transactionReceiveVatBills);
			} else {
				return getBillsToReceiveResult(context);
			}
		}
		if (transactionReceiveVatBills != null
				&& transactionReceiveVatBills.size() > 0) {
			List<ClientTransactionReceiveVAT> receiveVats = billsToReceiveReq
					.getValue();
			receiveVats.addAll(transactionReceiveVatBills);
		}
		return null;
	}

	private Result receiveVatItem(Context context,
			ClientTransactionReceiveVAT transactionItem) {
		context.setAttribute(PROCESS_ATTR, RECEIVAT_TRANSACTION_ITEM_PROCESS);
		context.setAttribute(OLD_RECEIVAT_TRANSACTION_ITEM_PROCESS,
				transactionItem);

		String lineAttr = (String) context
				.getAttribute(RECEIVAT_TRANSACTION_ITEM_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(RECEIVAT_TRANSACTION_ITEM_ATTR);
			if (lineAttr.equals("amount")) {
				if (context.getDouble() != null) {
					transactionItem.setAmountToReceive(context.getDouble());
				} else {
					transactionItem.setAmountToReceive(context.getInteger()
							.doubleValue());
				}
			}
		} else {
			Object selection = context
					.getSelection(RECEIVAT_TRANSACTION_ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals("amount")) {
					context.setAttribute(RECEIVAT_TRANSACTION_ITEM_ATTR,
							"amount");
					return amount(context,
							getMessages().pleaseEnter(getConstants().amount()),
							transactionItem.getAmountToReceive());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getAmountToReceive() == 0) {
						context.setAttribute(RECEIVAT_TRANSACTION_ITEM_ATTR,
								"amount");
						return amount(
								context,
								getMessages().pleaseEnter(
										getConstants().amount()),
								transactionItem.getAmountToReceive());
					}
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_RECEIVAT_TRANSACTION_ITEM_PROCESS);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}
		ResultList list = new ResultList(RECEIVAT_TRANSACTION_ITEM_DETAILS);
		Record record = new Record(getConstants().amount());
		record.add("", getConstants().taxAgency());
		record.add("", transactionItem.getTaxAgency());
		record.add("", getConstants().amount());
		record.add("", transactionItem.getAmountToReceive());
		record.add("", getConstants().taxDue());
		record.add("", transactionItem.getTaxDue());
		list.add(record);

		Result result = context.makeResult();

		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", getConstants().finish());
		actions.add(record);
		result.add(actions);
		return result;
	}

	private Result getBillsToReceiveResult(Context context) {
		Result result = context.makeResult();
		List<ClientTransactionReceiveVAT> transactionPayVats = getTransactionReceiveVatBills(context);
		ResultList list = new ResultList(BILLS_TO_RECEIVE_LIST);
		Object last = context.getLast(RequirementType.TRANSACTION_PAY_VAT);

		List<ClientTransactionReceiveVAT> skipRecords = new ArrayList<ClientTransactionReceiveVAT>();
		int num = 0;
		if (last != null) {
			list.add(createTransactionReceiveVatRecord((ClientTransactionReceiveVAT) last));
			skipRecords.add((ClientTransactionReceiveVAT) last);
		}
		Requirement payBillsReq = get(BILLS_TO_RECEIVE_LIST);
		// List<TransactionReceiveVAT> transPayVats = payBillsReq.getValue();
		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientTransactionReceiveVAT> pagination = pagination(context,
				selection, actions, transactionPayVats, skipRecords,
				VALUES_TO_SHOW);
		for (ClientTransactionReceiveVAT clientTransactionReceiveVAT : pagination) {
			list.add(createTransactionReceiveVatRecord(clientTransactionReceiveVAT));
		}
		// for (ClientTransactionReceiveVAT transactionPayVat :
		// transactionPayVats) {
		//
		// if (transactionPayVat != last
		// || !availablePayVats.contains(transactionPayVat)) {
		// .add(createTransactionReceiveVatRecord(transactionPayVat));
		// num++;
		// }
		// if (num == VALUES_TO_SHOW) {
		// break;
		// }
		// }
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add("Slect Bill to pay.");
		} else {
			result.add("You don't have Bills.");
		}

		result.add(list);
		return result;
	}

	private List<ClientTransactionReceiveVAT> getTransactionReceiveVatBills(
			Context contex) {

		ArrayList<ClientReceiveVATEntries> clientEntries = new ArrayList<ClientReceiveVATEntries>();

		FinanceTool tool = new FinanceTool();
		List<ReceiveVATEntries> entries = tool.getTaxManager()
				.getReceiveVATEntries(contex.getCompany().getId());
		for (ReceiveVATEntries entry : entries) {
			ClientReceiveVATEntries clientEntry = new ClientReceiveVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setVatReturn(entry.getTransaction().getID());
			clientEntry.setVatAgency(entry.getTaxAgency() != null ? entry
					.getTaxAgency().getID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}

		return getClientTransactionReceiveVATRecords(clientEntries);
	}

	private List<ClientTransactionReceiveVAT> getClientTransactionReceiveVATRecords(
			ArrayList<ClientReceiveVATEntries> clientEntries) {
		List<ClientTransactionReceiveVAT> records = new ArrayList<ClientTransactionReceiveVAT>();
		for (ClientReceiveVATEntries entry : clientEntries) {
			ClientTransactionReceiveVAT clientEntry = new ClientTransactionReceiveVAT();

			clientEntry.setTaxAgency(entry.getVatAgency());
			clientEntry.setVatReturn(entry.getVatReturn());
			// clientEntry.setAmountToReceive(entry.getAmount())
			double total = entry.getAmount();
			double balance = entry.getBalance();
			// clientEntry
			// .setTaxDue(total - balance > 0.0 ? total - balance : 0.0);
			clientEntry.setTaxDue(balance);

			records.add(clientEntry);
		}

		return records;
	}

	private Record createTransactionReceiveVatRecord(
			ClientTransactionReceiveVAT receiveVat) {
		Record record = new Record(receiveVat);
		ClientTAXAgency taxAgency = getClientCompany().getTaxAgency(
				receiveVat.getTaxAgency());
		record.add("Vat Agency", taxAgency != null ? taxAgency.getName() : "");
		record.add("Tax Due", receiveVat.getTaxDue());
		record.add("Amount to Receive", receiveVat.getAmountToReceive());
		return record;
	}

}
