package com.vimukti.accounter.web.client.ui.customers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ReconcileCreateDialog extends BaseDialog {

	private ClientStatementRecord statementRecord;

	public ReconcileCreateDialog(ClientStatementRecord statementRecord) {
		this.statementRecord = statementRecord;
		setText("Create");
		this.getElement().setId("ReconcileCreateDialog");
		createControls();
//		setWidth("500px");
		center();
	}

	/**
	 * Creating the controls
	 */
	private void createControls() {
		FlexTable linksTable = new FlexTable();
		final Map<String, Action> map;
		// Checking statementRecord amount is Spent or received amount.
		if (statementRecord.getSpentAmount() > 0) {
			map = getSpentTransactionNames();
		} else {
			map = getReceivedTransactionNames();
		}
		int j = 0, i = 0;
		for (String linkName : map.keySet()) {
			final Anchor anchor = new Anchor(linkName);
			anchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					map.get(anchor.getText()).run();
					removeFromParent();
				}
			});
			linksTable.setWidget(i, j, anchor);
			i++;
		}
		add(linksTable);
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Action> getReceivedTransactionNames() {
		Map<String, Action> map = new HashMap<String, Action>();
		map.put(messages.invoice(), ActionFactory.getNewInvoiceAction());
		map.put(messages.cashSale(), ActionFactory.getNewCashSaleAction());
		map.put(messages.payeePrePayment(Global.get().customer()),
				ActionFactory.getNewCustomerPaymentAction());
		map.put(messages.receivePayment(),
				ActionFactory.getReceivePaymentAction());
		return map;
	}

	/**
	 * 
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	private Map<String, Action> getSpentTransactionNames() {
		Map<String, Action> map = new HashMap<String, Action>();
		map.put(messages.enterBill(), ActionFactory.getEnterBillsAction());
		map.put(messages.cashPurchase(),
				ActionFactory.getNewCashPurchaseAction());
		map.put(messages.payeePrePayment(Global.get().Vendor()),
				ActionFactory.getVendorPaymentsAction());
		map.put(messages.cashExpense(), ActionFactory.CashExpenseAction());
		map.put(messages.creditCardExpense(),
				ActionFactory.CreditCardExpenseAction());
		map.put(messages.writeCheck(), ActionFactory.getWriteChecksAction());
		map.put(messages.payBill(), ActionFactory.getPayBillsAction());
		return map;
	}

	@Override
	protected boolean onOK() {
		return false;
	}

	@Override
	public void setFocus() {
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
