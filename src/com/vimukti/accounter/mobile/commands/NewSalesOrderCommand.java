package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EstimateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewSalesOrderCommand extends NewAbstractTransactionCommand {

	public static final String STATUS = "status";
	public static final String CUSTOMER_ORDER_NO = "customerOrderNumber";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getConstants().salesOrder());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().salesOrder());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_SALES_ORDER,
						context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		ArrayList<ClientPaymentTerms> paymentTerms = context.getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(STATUS).setDefaultValue(getConstants().open());

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().quote());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseSelect(
				Global.get().Customer()), Global.get().Customer(), false, true,
				new ChangeListner<ClientCustomer>() {

					@Override
					public void onSelection(ClientCustomer value) {
						NewSalesOrderCommand.this.get(CONTACT).setValue(null);
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
				return new ArrayList<ClientContact>(
						((ClientCustomer) NewSalesOrderCommand.this.get(
								CUSTOMER).getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientCustomer) get(CUSTOMER).getValue())
						.getDisplayName();
			}
		});
		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getConstants().orderNo()), getConstants().orderNo(), true, true));

		list.add(new NumberRequirement(
				CUSTOMER_ORDER_NO,
				getMessages().pleaseEnter(
						Global.get().Customer() + getConstants().orderNumber()),
				Global.get().Customer() + getConstants().orderNumber(), true,
				true));
		list.add(new EstimateRequirement(ESTIMATE, getMessages().pleaseSelect(
				getConstants().quote()), getConstants().quote(), true, true,
				null) {

			@Override
			protected List<Estimate> getLists(Context context) {
				try {
					return new FinanceTool().getCustomerManager()
							.getEstimates(
									((ClientCustomer) get(CUSTOMER).getValue())
											.getID(),
									context.getCompany().getID());
				} catch (DAOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected boolean filter(Estimate e, String name) {
				return false;
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getConstants().dueDate()), getConstants().dueDate(), true, true));

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phoneNumber(),
				true, true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));
		list.add(new StringListRequirement(STATUS, getMessages().pleaseSelect(
				getConstants().status()), getConstants().status(), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSelectString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().open());
				list.add(getConstants().completed());
				list.add(getConstants().cancelled());
				return list;
			}

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& !getClientCompany().getPreferences()
								.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

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

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientSalesOrder newSalesOrder = new ClientSalesOrder();

		ClientCustomer customer = get(CUSTOMER).getValue();
		newSalesOrder.setCustomer(customer.getID());
		newSalesOrder.setType(ClientTransaction.TYPE_SALES_ORDER);
		newSalesOrder.setPhone((String) get(PHONE).getValue());
		int statusNumber = 0;
		if (get(STATUS).getValue().equals(getConstants().open())) {
			statusNumber = 1;
		} else if (get(STATUS).getValue().equals(getConstants().completed())) {
			statusNumber = 2;
		} else if (get(STATUS).getValue().equals(getConstants().cancelled())) {
			statusNumber = 3;
		}
		ClientAddress billTo = get(BILL_TO).getValue();
		newSalesOrder.setBillingAddress(billTo);
		newSalesOrder.setStatus(statusNumber);
		ClientFinanceDate value = get(DATE).getValue();
		newSalesOrder.setDate(value.getDate());
		newSalesOrder.setNumber((String) get(NUMBER).getValue());

		newSalesOrder.setCustomerOrderNumber((String) get(ORDER_NO).getValue());

		ClientPaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		if (newPaymentTerms != null)
			newSalesOrder.setPaymentTerm(newPaymentTerms.getID());

		CompanyPreferences preferences = context.getCompany().getPreferences();

		ClientFinanceDate date = get(DUE_DATE).getValue();
		newSalesOrder.setDate(date.getDate());

		Estimate e = get(ESTIMATE).getValue();
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		ClientConvertUtil c = new ClientConvertUtil();
		ClientEstimate cEstimate = null;
		try {
			cEstimate = c.toClientObject(e, ClientEstimate.class);
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		if (e != null) {
			newSalesOrder.setEstimate(cEstimate.getID());
			addEstimate(cEstimate, items);
		}
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		newSalesOrder.setTransactionItems(items);
		updateTotals(context, newSalesOrder, true);
		String memo = get(MEMO).getValue();
		newSalesOrder.setMemo(memo);

		create(newSalesOrder, context);
		return null;
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
}
