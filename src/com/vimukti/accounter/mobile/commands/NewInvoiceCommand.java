package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewInvoiceCommand extends AbstractTransactionCommand {

	private static final String ESTIMATEANDSALESORDER = "estimateAndSalesOrder";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("customer", false, true));
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

		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement(BILL_TO, true, true));

		list.add(new Requirement("DueDate", true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(MEMO, true, true));
		list.add(new Requirement(ESTIMATEANDSALESORDER, true, true));
		list.add(new Requirement(TAXCODE, false, true));
	}

	@Override
	public Result run(Context context) {
		setDetaultValues(context);
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
			}
		}
		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult
				.add("New Invoice  is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		setTransactionType(CUSTOMER_TRANSACTION);
		result = customerRequirement(context, list, "customer");
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context, makeResult, actions);
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

		result = new Result();
		result.add("Invoice  created successfully");
		return result;
	}

	private void setDetaultValues(Context context) {
		get(DATE).setDefaultValue(new Date(System.currentTimeMillis()));
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(CONTACT).setDefaultValue(new ClientContact());
		ArrayList<ClientPaymentTerms> paymentTerms = getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get("DueDate").setDefaultValue(new Date(System.currentTimeMillis()));

		get(MEMO).setDefaultValue(" ");
		get(BILL_TO).setDefaultValue(new ClientAddress());
	}

	private void completeProcess(Context context) {

		ClientInvoice invoice = new ClientInvoice();

		Date date = get(DATE).getValue();
		invoice.setDate(new ClientFinanceDate(date).getDate());

		invoice.setType(Transaction.TYPE_INVOICE);

		String number = get(NUMBER).getValue();
		invoice.setNumber(number);

		List<ClientTransactionItem> items = get(ITEMS).getValue();

		if (context.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			TAXCode taxCode = get("tax").getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}

		}

		ClientCustomer customer = get("customer").getValue();
		invoice.setCustomer(customer.getID());

		Date dueDate = get("DueDate").getValue();
		invoice.setDueDate(new ClientFinanceDate(dueDate).getDate());

		ClientContact contact = get(CONTACT).getValue();
		invoice.setContact(contact);

		ClientAddress billTo = get(BILL_TO).getValue();
		invoice.setBillingAddress(billTo);

		ClientPaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		invoice.setPaymentTerm(paymentTerm.getID());

		String orderNo = get(ORDER_NO).getValue();
		invoice.setOrderNum(orderNo);

		String memo = get(MEMO).getValue();
		invoice.setMemo(memo);
		invoice.setStatus(Invoice.STATUS_OPEN);

		// Adding selecting estimate or salesOrder to Invoice

		EstimatesAndSalesOrdersList e = get(ESTIMATEANDSALESORDER).getValue();
		ClientEstimate cct = null;
		ClientSalesOrder cSalesOrder = null;
		if (e != null) {
			if (e.getType() == ClientTransaction.TYPE_ESTIMATE) {
				invoice.setEstimate(e.getTransactionId());
				cct = getEstimate(e.getTransactionId(), context);
				addEstimate(cct, items);
			} else {
				invoice.setSalesOrder(e.getTransactionId());
				cSalesOrder = getSalesOrder(e.getTransactionId(), context);
				addSalesOrder(cSalesOrder, items);
			}
		}
		invoice.setTransactionItems(items);

		create(invoice, context);
	}

	private void addSalesOrder(ClientSalesOrder cSalesOrder,
			List<ClientTransactionItem> items) {
		for (ClientTransactionItem item : cSalesOrder.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (item.getLineTotal() != 0.0) {
				clientItem.setDescription(item.getDescription());
				clientItem.setType(item.getType());
				clientItem.setAccount(item.getAccount());
				clientItem.setItem(item.getItem());
				clientItem.setVatItem(item.getVatItem());
				clientItem.setVATfraction(item.getVATfraction());
				clientItem.setTaxCode(item.getTaxCode());
				clientItem.setDescription(item.getDescription());
				clientItem.setQuantity(item.getQuantity());
				clientItem.setUnitPrice(item.getUnitPrice());
				clientItem.setDiscount(item.getDiscount());
				clientItem.setLineTotal(item.getLineTotal()
						- item.getInvoiced());
				clientItem.setTaxable(item.isTaxable());
				clientItem.setReferringTransactionItem(item.getID());
				items.add(clientItem);
			}
		}

	}

	private void addEstimate(ClientEstimate cct,
			List<ClientTransactionItem> items) {
		for (ClientTransactionItem cst : cct.getTransactionItems()) {
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (cst.getLineTotal() != 0.0) {
				clientItem.setDescription(cst.getDescription());
				clientItem.setType(cst.getType());
				clientItem.setAccount(cst.getAccount());
				clientItem.setItem(cst.getItem());
				clientItem.setVATfraction(cst.getVATfraction());
				clientItem.setTaxCode(cst.getTaxCode());
				clientItem.setDescription(cst.getDescription());
				clientItem.setQuantity(cst.getQuantity());
				clientItem.setUnitPrice(cst.getUnitPrice());
				clientItem.setDiscount(cst.getDiscount());
				clientItem.setLineTotal(cst.getLineTotal() - cst.getInvoiced());
				clientItem.setTaxable(cst.isTaxable());
				clientItem.setReferringTransactionItem(cst.getID());

				items.add(clientItem);
			}
		}

	}

	private ClientEstimate getEstimate(long transactionId, Context context) {
		ClientEstimate cEstimate = null;
		try {
			cEstimate = new FinanceTool().getManager().getObjectById(
					AccounterCoreType.ESTIMATE, transactionId,
					Accounter.getCompany().getAccountingType(),
					context.getCompany().getID());
		} catch (DAOException e1) {
			e1.printStackTrace();
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		return cEstimate;
	}

	private ClientSalesOrder getSalesOrder(long transactionId, Context context) {
		ClientSalesOrder cSalesOrder = null;
		try {
			cSalesOrder = new FinanceTool().getManager().getObjectById(
					AccounterCoreType.SALESORDER, transactionId,
					Accounter.getCompany().getAccountingType(),
					context.getCompany().getID());
		} catch (DAOException e1) {
			e1.printStackTrace();
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		return cSalesOrder;
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
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Requirement custmerReq = get("customer");
		ClientCustomer customer = (ClientCustomer) custmerReq.getValue();

		Result result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = estimateAndSalesOrderRequirement(context, list, selection,
				customer);

		if (result != null) {
			return result;
		}

		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = invoiceNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "DueDate",
				"Enter Due Date", selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				"Enter Invoice Numer");

		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Invoice.");
		actions.add(finish);

		return makeResult;

	}

	private Result estimateAndSalesOrderRequirement(Context context,
			ResultList list, Object selection, ClientCustomer customer) {
		Object est = context.getSelection(ESTIMATEANDSALESORDER);
		Requirement estimateReq = get(ESTIMATEANDSALESORDER);

		if (est != null) {
			estimateReq.setValue(est);
		}

		EstimatesAndSalesOrdersList estimates = estimateReq.getValue();
		if (selection != null)
			if (selection == ESTIMATEANDSALESORDER) {
				context.setAttribute(INPUT_ATTR, ESTIMATEANDSALESORDER);
				return estimates(context, estimates);

			}

		Record paymentTermRecord = new Record(ESTIMATEANDSALESORDER);
		paymentTermRecord.add("Name", ESTIMATEANDSALESORDER);
		paymentTermRecord.add("Value",
				estimates == null ? "" : estimates.getCustomerName());

		list.add(paymentTermRecord);
		return null;
	}

	private Result estimates(Context context,
			EstimatesAndSalesOrdersList estimates) {

		ClientCustomer c = get("customer").getValue();

		ArrayList<EstimatesAndSalesOrdersList> estimatesAndSalesOrdersList = null;
		try {
			estimatesAndSalesOrdersList = new FinanceTool()
					.getCustomerManager().getEstimatesAndSalesOrdersList(
							c.getID(), context.getCompany().getID());
		} catch (DAOException e) {

			e.printStackTrace();
		}

		Result result = context.makeResult();
		result.add("Select estimate");

		ResultList list = new ResultList(ESTIMATEANDSALESORDER);
		List<EstimatesAndSalesOrdersList> skip = new ArrayList<EstimatesAndSalesOrdersList>();
		if (estimates != null) {
			list.add(createEstimateRecord(estimates));
			skip.add(estimates);
		}

		ActionNames selection = context.getSelection(ESTIMATEANDSALESORDER);

		List<Record> actions = new ArrayList<Record>();

		List<EstimatesAndSalesOrdersList> pagination = pagination(context,
				selection, actions, estimatesAndSalesOrdersList, skip, 5);

		for (EstimatesAndSalesOrdersList s : pagination) {
			list.add(createEstimateRecord(s));
		}

		for (Record record : actions) {
			list.add(record);
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create estimate");

		result.add(commandList);
		return result;
	}

	private Record createEstimateRecord(EstimatesAndSalesOrdersList estimates) {
		Record rec = new Record(estimates);
		rec.add("", estimates.getCustomerName());
		if (estimates.getType() == ClientTransaction.TYPE_ESTIMATE)
			rec.add("", "Estimate");
		else
			rec.add("", "SalesOrder");
		return rec;
	}

	private Result invoiceNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("number");
		String invoiceNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ORDER_NO)) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			invoiceNo = order;
			req.setValue(invoiceNo);
		}

		if (selection == invoiceNo) {
			context.setAttribute(INPUT_ATTR, ORDER_NO);
			return number(context, "Enter Invoice number", invoiceNo);
		}

		Record invoiceNoRec = new Record(invoiceNo);
		invoiceNoRec.add("Name", "Invoice Number");
		invoiceNoRec.add("Value", invoiceNo);
		list.add(invoiceNoRec);
		return null;
	}

}