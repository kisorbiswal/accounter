package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class CreateRecurringTemplateDialog extends
		BaseDialog<ClientRecurringTransaction> {

	public static String ALL = Accounter.messages().all();
	public static String BILL = Accounter.messages().bill();
	public static String CASH_EXPENSE = Accounter.messages().cashExpense();
	public static String CASH_SALES = Accounter.messages().cashSale();
	public static String CASH_PURCHASE = Accounter.messages().cashPurchase();
	public static String CREDIT_CARD_EXPENSE = Accounter.messages()
			.creditCardExpense();
	public static String CUSTOMER_CREDIT_MEMO = Accounter.messages()
			.customerCreditNote(Global.get().Customer());
	public static String DEPOSIT_TRANSFER_FUNDS = Accounter.messages()
			.depositTransferFunds();
	public static String INVOICE = Accounter.messages().invoice();
	public static String QUOTE = Accounter.messages().quote();
	public static String VENDOR_CREDIT_MEMO = Accounter.messages()
			.vendorCreditMemo();
	public static String WRITE_CHECK = Accounter.messages().writeCheck();

	Map<String, Integer> typesMap = new HashMap<String, Integer>();

	private DynamicForm form;
	private SelectCombo transactionTypeCombo;
	ClientLocation clientLocation;

	public CreateRecurringTemplateDialog() {
		super(messages.selectTransactionType(), "");
		setWidth("400px");
		createControls();
		center();
	}

	private void createControls() {

		initTypesMap();

		form = new DynamicForm();
		form.setWidth("100%");
		transactionTypeCombo = new SelectCombo(messages.transactionType());
		transactionTypeCombo
				.initCombo(new ArrayList<String>(typesMap.keySet()));
		transactionTypeCombo.setHelpInformation(true);
		transactionTypeCombo.setRequired(true);

		VerticalPanel layout = new VerticalPanel();
		form.setFields(transactionTypeCombo);
		layout.add(form);

		setBodyLayout(layout);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		return result;
	}

	@Override
	protected boolean onOK() {
		int type = typesMap.get(transactionTypeCombo.getSelectedValue());
		
		ClientTransaction transaction = UIUtils.getTransactionObject(type);
		transaction.setSaveStatus(ClientTransaction.STATUS_TEMPLATE);
		transaction.setType(type);

		ReportsRPC.openTransactionView(transaction);
		return true;
	}

	@Override
	public void setFocus() {
		transactionTypeCombo.setFocus();
	}

	private void initTypesMap() {
		typesMap.put(BILL, ClientTransaction.TYPE_ENTER_BILL);
		typesMap.put(CASH_EXPENSE, ClientTransaction.TYPE_CASH_EXPENSE);
		typesMap.put(CASH_SALES, ClientTransaction.TYPE_CASH_SALES);
		typesMap.put(CASH_PURCHASE, ClientTransaction.TYPE_CASH_PURCHASE);
		typesMap.put(CREDIT_CARD_EXPENSE,
				ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		typesMap.put(CUSTOMER_CREDIT_MEMO,
				ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		typesMap.put(DEPOSIT_TRANSFER_FUNDS,
				ClientTransaction.TYPE_MAKE_DEPOSIT);
		typesMap.put(INVOICE, ClientTransaction.TYPE_INVOICE);
		typesMap.put(QUOTE, ClientTransaction.TYPE_ESTIMATE);
		typesMap.put(VENDOR_CREDIT_MEMO,
				ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		typesMap.put(WRITE_CHECK, ClientTransaction.TYPE_WRITE_CHECK);
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
