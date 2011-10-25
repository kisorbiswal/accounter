package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EstimatesAndSalesOrderListRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemAccountsRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewInvoiceCommand extends NewAbstractTransactionCommand {

	private static final String ESTIMATEANDSALESORDER = "estimateAndSalesOrder";
	private static final String CUSTOMER = "customer";
	private static final String ITEMS = "items";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String DUE_DATE = "duedate";
	private static final String CONTACT = "contact";
	private static final String BILL_TO = "billto";
	private static final String DATE = "date";
	private static final String NUMBER = "number";
	private static final String ORDER_NO = "orderNo";
	private static final String MEMO = "memo";
	private static final String TAXCODE = "taxCode";
	private static final String ACCOUNT_ITEMS = "accountItems";

	private ClientCustomer customer;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER,
				"Please Eneter Customer name or number to set InvoiceCustomer",
				"Customer", false, true, new ChangeListner<ClientCustomer>() {

					@Override
					public void onSelection(ClientCustomer value) {
						if (customer != value) {
							customer = value;
							NewInvoiceCommand.this.get(CONTACT).setValue(null);
						}
					}
				}) {

			@Override
			protected List<ClientCustomer> getLists(Context context) {
				return getClientCompany().getCustomers();
			}
		});

		list.add(new TransactionItemItemsRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
				false, true, true) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getItems();
			}

			@Override
			protected ClientCompany getClientCompany() {
				return NewInvoiceCommand.this.getClientCompany();
			}
		});

		list.add(new TransactionItemAccountsRequirement(ACCOUNT_ITEMS,
				"Please Enter Account name or number", getConstants().items(),
				false, true) {

			@Override
			protected ClientCompany getClientCompany() {
				return NewInvoiceCommand.this.getClientCompany();
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return getClientCompany().getAccounts();
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return e.getName().contains(name) || e.getNumber().equals(name);
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, true));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getConstants().paymentTerm()), getConstants()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<ClientPaymentTerms> getLists(Context context) {
				return getClientCompany().getPaymentsTerms();
			}
		});

		list.add(new ContactRequirement(CONTACT, "Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected List<ClientContact> getLists(Context context) {
				return new ArrayList<ClientContact>(customer.getContacts());
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getConstants().dueDate()), getConstants().dueDate(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getConstants().orderNo()), getConstants().orderNo(), true, true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new EstimatesAndSalesOrderListRequirement(
				ESTIMATEANDSALESORDER, getMessages().selectTypeOfThis(
						getConstants().quote()), getConstants()
						.quoteAndSalesOrderList(), true, true, null) {

			@Override
			protected List<EstimatesAndSalesOrdersList> getLists(Context context) {
				try {
					return new FinanceTool().getCustomerManager()
							.getEstimatesAndSalesOrdersList(customer.getID(),
									context.getCompany().getID());
				} catch (DAOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected boolean filter(EstimatesAndSalesOrdersList e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return getClientCompany().getTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				return e.getName().contains(name);
			}
		});
	}

	// @Override
	// public Result run(Context context) {
	// setDetaultValues(context);
	// String process = (String) context.getAttribute(PROCESS_ATTR);
	// Result result = context.makeResult();
	// if (process != null) {
	// if (process.equals(ADDRESS_PROCESS)) {
	// result = addressProcess(context);
	// if (result != null) {
	// return result;
	// }
	// } else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
	// result = transactionItemProcess(context);
	// if (result != null) {
	// return result;
	// }
	// }
	// }
	// // Preparing Result
	// Result makeResult = context.makeResult();
	// makeResult
	// .add(getMessages().readyToCreate(getConstants().newInvoice()));
	// ResultList list = new ResultList("values");
	// makeResult.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	//
	// result = customerRequirement(context, list, "customer", Global.get()
	// .customer());
	// if (result != null) {
	// return result;
	// }
	// result = itemsRequirement(context, makeResult, actions, true);
	// if (result != null) {
	// return result;
	// }
	// makeResult.add(actions);
	// ClientCompanyPreferences preferences = getClientCompany()
	// .getPreferences();
	// if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
	// result = taxCodeRequirement(context, list);
	// if (result != null) {
	// return result;
	// }
	// }
	// result = createOptionalResult(context, list, actions, makeResult);
	// if (result != null) {
	// return result;
	// }
	// completeProcess(context);
	// markDone();
	//
	// result = new Result();
	// result.add(getMessages().createSuccessfully(getConstants().invoice()));
	// return result;
	// }

	// private void setDetaultValues(Context context) {
	// get(DATE).setDefaultValue(new ClientFinanceDate());
	// get(NUMBER).setDefaultValue(
	// NumberUtils.getNextTransactionNumber(
	// ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
	// get(CONTACT).setDefaultValue(null);
	// ArrayList<ClientPaymentTerms> paymentTerms = getClientCompany()
	// .getPaymentsTerms();
	// for (ClientPaymentTerms p : paymentTerms) {
	// if (p.getName().equals("Due on Receipt")) {
	// get(PAYMENT_TERMS).setDefaultValue(p);
	// }
	// }
	//
	// get("DueDate").setDefaultValue(new ClientFinanceDate());
	//
	// get(MEMO).setDefaultValue(" ");
	// get(BILL_TO).setDefaultValue(new ClientAddress());
	// }

	private void completeProcess(Context context) {

		ClientInvoice invoice = new ClientInvoice();

		ClientFinanceDate date = get(DATE).getValue();
		invoice.setDate(date.getDate());

		invoice.setType(Transaction.TYPE_INVOICE);

		String number = get(NUMBER).getValue();
		invoice.setNumber(number);

		List<ClientTransactionItem> items = get(ITEMS).getValue();

		ClientCustomer customer = get("customer").getValue();
		invoice.setCustomer(customer.getID());

		ClientFinanceDate dueDate = get("DueDate").getValue();
		invoice.setDueDate(dueDate.getDate());

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

		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		invoice.setTransactionItems(items);
		updateTotals(invoice, true);

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
					context.getCompany().getID());
		} catch (DAOException e1) {
			e1.printStackTrace();
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		return cSalesOrder;
	}

	// private Result createOptionalResult(Context context, ResultList list,
	// ResultList actions, Result makeResult) {
	// if (context.getAttribute(INPUT_ATTR) == null) {
	// context.setAttribute(INPUT_ATTR, "optional");
	// }
	//
	// Object selection = context.getSelection(ACTIONS);
	// if (selection != null) {
	// ActionNames actionName = (ActionNames) selection;
	// switch (actionName) {
	// case ADD_MORE_ITEMS:
	// return items(context);
	// case FINISH:
	// context.removeAttribute(INPUT_ATTR);
	// return null;
	// default:
	// break;
	// }
	// }
	//
	// selection = context.getSelection("values");
	//
	// Requirement custmerReq = get("customer");
	// ClientCustomer customer = (ClientCustomer) custmerReq.getValue();
	//
	// Result result = dateRequirement(context, list, selection, DATE,
	// getMessages().pleaseEnter(getConstants().transactionDate()),
	// getConstants().transactionDate());
	// if (result != null) {
	// return result;
	// }
	//
	// result = estimateAndSalesOrderRequirement(context, list, selection,
	// customer);
	//
	// if (result != null) {
	// return result;
	// }
	//
	// result = contactRequirement(context, list, selection, customer);
	// if (result != null) {
	// return result;
	// }
	//
	// result = paymentTermRequirement(context, list, selection);
	// if (result != null) {
	// return result;
	// }
	//
	// result = invoiceNoRequirement(context, list, selection);
	// if (result != null) {
	// return result;
	// }
	//
	// result = addressOptionalRequirement(context, list, selection, BILL_TO,
	// getMessages().pleaseEnter(getConstants().billTo()),
	// getConstants().billTo());
	// if (result != null) {
	// return result;
	// }
	// result = dateOptionalRequirement(context, list, "DueDate",
	// getConstants().dueDate(),
	// getMessages().pleaseEnter(getConstants().dueDate()), selection);
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberOptionalRequirement(context, list, selection, NUMBER,
	// getConstants().invoiceNo(),
	// getMessages().pleaseEnter(getConstants().invoiceNo()));
	//
	// if (result != null) {
	// return result;
	// }
	//
	// result = stringOptionalRequirement(context, list, selection, MEMO,
	// getConstants().memo(),
	// getMessages().pleaseEnter(getConstants().memo()));
	// if (result != null) {
	// return result;
	// }
	//
	// Record finish = new Record(ActionNames.FINISH);
	// finish.add("", getMessages().finishToCreate(getConstants().invoice()));
	// actions.add(finish);
	//
	// return makeResult;
	//
	// }

	// private Result estimateAndSalesOrderRequirement(Context context,
	// ResultList list, Object selection, ClientCustomer customer) {
	// Object est = context.getSelection(ESTIMATEANDSALESORDER);
	// Requirement estimateReq = get(ESTIMATEANDSALESORDER);
	//
	// if (est != null) {
	// estimateReq.setValue(est);
	// }
	//
	// EstimatesAndSalesOrdersList estimates = estimateReq.getValue();
	// // if (est instanceof ActionNames) {
	// // estimates = null;
	// // } else {
	// // estimates = (EstimatesAndSalesOrdersList) est;
	// // }
	//
	// if (selection != null)
	// if (selection == ESTIMATEANDSALESORDER) {
	// context.setAttribute(INPUT_ATTR, ESTIMATEANDSALESORDER);
	// return estimates(context, estimates);
	//
	// }
	// Record paymentTermRecord = new Record(ESTIMATEANDSALESORDER);
	// paymentTermRecord.add("Name", getConstants().quoteAndSalesOrderList());
	//
	// if (estimates != null) {
	// if (estimates.getType() == ClientEstimate.TYPE_ESTIMATE) {
	// paymentTermRecord.add("", getConstants().quote());
	// } else {
	// paymentTermRecord.add("", getConstants().salesOrder());
	// }
	// }
	//
	// list.add(paymentTermRecord);
	//
	// return null;
	// }

	// private Result estimates(Context context,
	// EstimatesAndSalesOrdersList estimates) {
	//
	// ClientCustomer c = get("customer").getValue();
	//
	// ArrayList<EstimatesAndSalesOrdersList> estimatesAndSalesOrdersList =
	// null;
	// try {
	// estimatesAndSalesOrdersList = new FinanceTool()
	// .getCustomerManager().getEstimatesAndSalesOrdersList(
	// c.getID(), context.getCompany().getID());
	// } catch (DAOException e) {
	//
	// e.printStackTrace();
	// }
	//
	// Result result = context.makeResult();
	// result.add(getMessages().selectTypeOfThis(getConstants().quote()));
	//
	// ResultList list = new ResultList(ESTIMATEANDSALESORDER);
	// List<EstimatesAndSalesOrdersList> skip = new
	// ArrayList<EstimatesAndSalesOrdersList>();
	// if (estimates != null) {
	// list.add(createEstimateRecord(estimates));
	// skip.add(estimates);
	// }
	//
	// ActionNames selection = context.getSelection(ESTIMATEANDSALESORDER);
	//
	// List<Record> actions = new ArrayList<Record>();
	//
	// List<EstimatesAndSalesOrdersList> pagination = pagination(context,
	// selection, actions, estimatesAndSalesOrdersList, skip,
	// ESTIMATESANDSALESORDERS_TO_SHOW);
	//
	// for (EstimatesAndSalesOrdersList s : pagination) {
	// list.add(createEstimateRecord(s));
	// }
	//
	// for (Record record : actions) {
	// list.add(record);
	// }
	// result.add(list);
	//
	// CommandList commandList = new CommandList();
	// commandList.add("Create" + getConstants().quote());
	//
	// result.add(commandList);
	// return result;
	// }

	private Record createEstimateRecord(EstimatesAndSalesOrdersList estimates) {
		Record rec = new Record(estimates);
		if (estimates.getType() == ClientTransaction.TYPE_ESTIMATE)
			rec.add("", getConstants().quote());
		else
			rec.add("", getConstants().salesOrder());

		rec.add("", estimates.getTotal());

		return rec;
	}

	// private Result invoiceNoRequirement(Context context, ResultList list,
	// Object selection) {
	// Requirement req = get("number");
	// String invoiceNo = (String) req.getValue();
	//
	// String attribute = (String) context.getAttribute(INPUT_ATTR);
	// if (attribute.equals(ORDER_NO)) {
	// String order = context.getSelection(NUMBER);
	// if (order == null) {
	// order = context.getString();
	// }
	// invoiceNo = order;
	// req.setValue(invoiceNo);
	// }
	//
	// if (selection == invoiceNo) {
	// context.setAttribute(INPUT_ATTR, ORDER_NO);
	// return number(context,
	// getMessages().pleaseEnter(getConstants().invoiceNumber()),
	// invoiceNo);
	// }
	//
	// Record invoiceNoRec = new Record(invoiceNo);
	// invoiceNoRec.add("Name", getConstants().invoiceNumber());
	// invoiceNoRec.add("Value", invoiceNo);
	// list.add(invoiceNoRec);
	// return null;
	// }

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientInvoice invoice = new ClientInvoice();

		ClientFinanceDate date = get(DATE).getValue();
		invoice.setDate(date.getDate());

		invoice.setType(Transaction.TYPE_INVOICE);

		String number = get(NUMBER).getValue();
		invoice.setNumber(number);

		List<ClientTransactionItem> items = get(ITEMS).getValue();

		ClientCustomer customer = get(CUSTOMER).getValue();
		invoice.setCustomer(customer.getID());

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		invoice.setDueDate(dueDate.getDate());

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

		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		invoice.setTransactionItems(items);
		updateTotals(invoice, true);

		create(invoice, context);

		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().invoice());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		ArrayList<ClientPaymentTerms> paymentTerms = getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().invoice());
	}

}