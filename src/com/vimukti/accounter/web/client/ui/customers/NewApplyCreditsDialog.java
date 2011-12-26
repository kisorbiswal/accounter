package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.core.IGenericCallback;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionPayBillTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionReceivePaymentTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionReceivePaymentTable.TempCredit;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CreditsandPaymentsGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class NewApplyCreditsDialog extends BaseDialog<ClientCustomer> {

	AmountField amtDueText, totAmtUseText;

	public CreditsandPaymentsGrid grid;

	private double amountDue;

	private int key;

	private ClientCustomer customer;

	public Double totalBalances = 0.0D;
	public Double totalAmountToUse = 0.0D;

	public ClientTransactionReceivePayment record;

	public boolean okClicked;

	public boolean cancelClicked;

	private boolean canEdit;

	public double adjustPayment = 0.0D;

	private double initialAdjustPayment = 0.0D;

	private List<ClientCreditsAndPayments> updatedCreditsAndPayments;

	private ClientVendor vendor;

	private ClientTransactionPayBill transactionPaybill;

	public double totalCreditApplied = 0.0;
	public double totalUnusedCreditAmount = 0.0;
	private ICurrencyProvider currencyProvider;

	public NewApplyCreditsDialog(ClientCustomer customer,
			List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments,
			boolean canEdit, ClientTransactionReceivePayment record,
			ICurrencyProvider currencyProvider) {
		super(Accounter.messages().applyCreditsandPayments());
		this.currencyProvider = currencyProvider;
		this.customer = customer;
		this.canEdit = canEdit;
		updatedCreditsAndPayments = updatedCustomerCreditsAndPayments;
		this.record = record;
		createControls();
		grid.initialCreditsAndPayments(updatedCustomerCreditsAndPayments);
		getInitialAmountUse();
		addGridRecordsToListGrid(updatedCreditsAndPayments);

	}

	public NewApplyCreditsDialog(ClientVendor venddor,
			List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments,
			boolean canEdit, ClientTransactionPayBill record,
			ICurrencyProvider currencyProvider) {
		super(Accounter.messages().applyCreditsandPayments());
		this.currencyProvider = currencyProvider;
		this.vendor = venddor;
		this.canEdit = canEdit;
		updatedCreditsAndPayments = updatedCustomerCreditsAndPayments;
		this.transactionPaybill = record;
		createControls();
		grid.initialCreditsAndPayments(updatedCustomerCreditsAndPayments);
		getInitialAmountUse();
		addGridRecordsToListGrid(updatedCreditsAndPayments);
	}

	public NewApplyCreditsDialog(
			String amountDue,
			List<ClientCreditsAndPayments> creditsAndPayments,
			int key,
			LinkedHashMap<String, List<ClientTransactionCreditsAndPayments>> creditsAndPaymentsMap,
			IGenericCallback<String> callback,
			ICurrencyProvider currencyProvider) {
		super(Accounter.messages().applyCreditsandPayments(), "");
		this.currencyProvider = currencyProvider;
		this.key = key;
		// this.creditsAndPaymentsMap = creditsAndPaymentsMap;

		// this.amountDue = amountDue;
		this.updatedCreditsAndPayments = creditsAndPayments;
		// this.callback = callback;
		createControls();
		getInitialAmountUse();
	}

	public double getTotalCreditAmount() {
		totalCreditApplied = 0.0;
		for (ClientCreditsAndPayments crd : grid.getSelectedRecords()) {
			totalCreditApplied += crd.getAmtTouse();
		}
		return totAmtUseText.getAmount();
	}

	public double getTotalUnuseCreditAmount() {
		totalUnusedCreditAmount = 0.0;
		List<ClientCreditsAndPayments> records = grid.getUpdatedRecords();
		if (records.isEmpty()) {
			records = grid.getActualRecords();
		}
		for (ClientCreditsAndPayments crd : records) {
			totalUnusedCreditAmount += crd.getBalance();
		}
		return totalUnusedCreditAmount;
	}

	public double getActualUnusedCreditAmount() {
		totalUnusedCreditAmount = 0.0;
		List<ClientCreditsAndPayments> records = grid.getActualRecords();
		for (ClientCreditsAndPayments crd : records) {
			totalUnusedCreditAmount += crd.getBalance();
		}
		return totalUnusedCreditAmount;
	}

	public void setRecord(ClientTransactionReceivePayment record) {
		this.record = record;
		getInitialAmountUse();
	}

	public void setRecord(ClientTransactionPayBill record) {
		this.transactionPaybill = record;
		getInitialAmountUse();
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public void setUpdatedCreditsAndPayments(
			List<ClientCreditsAndPayments> updatedCreditsAndPayments) {
		this.updatedCreditsAndPayments = updatedCreditsAndPayments;
		grid.initialCreditsAndPayments(updatedCreditsAndPayments);
		addGridRecordsToListGrid(updatedCreditsAndPayments);

	}

	/*
	 * This method invoked when record values are changed.It updates the
	 * non-editable fields
	 */
	public void updateFields() {
		setTotalBalance(totalBalances);

	}

	/*
	 * This method invoked to set the records to grid and it also updates the
	 * non-editable fileds
	 */
	public void addGridRecordsToListGrid(
			List<ClientCreditsAndPayments> creditsAndPayments) {
		if (creditsAndPayments != null && creditsAndPayments.size() != 0) {
			grid.removeAllRecords();
			grid.setRecords(creditsAndPayments);
		} else {
			grid.addEmptyMessage(Accounter.messages().therearenocreditstoshow());
		}
		for (ClientCreditsAndPayments cr : creditsAndPayments) {
			int row = grid.indexOf(cr);
			// if (!DecimalUtil.isEquals(cr.getAmtTouse(), 0.0))
			// ((CheckBox) grid.getWidget(row, 0)).setValue(true);
		}

		double toBeSetTotalBalance = 0.0;
		double toBeSetTotalCredtAmt = 0.0d;
		double toBeSetTotalAmtTouse = 0.0d;
		for (ClientCreditsAndPayments cpRecord : creditsAndPayments) {
			toBeSetTotalBalance += cpRecord.getBalance();
			toBeSetTotalCredtAmt += cpRecord.getCreditAmount();
			// toBeSetTotalAmtTouse += cpRecord.getAmtTouse();
		}

		totalBalances = toBeSetTotalBalance;

		if (!canEdit) {
			totalAmountToUse = toBeSetTotalAmtTouse;
			totalBalances = toBeSetTotalBalance;
			updateFields();
		}

	}

	private void createControls() {
		mainPanel.setSpacing(5);

		if (canEdit) {
			amtDueText = new AmountField(messages.amountDue(), this,
					currencyProvider.getTransactionCurrency());
			amtDueText.setColSpan(1);
			if (transactionPaybill != null)
				amtDueText
						.setValue(amountAsString(transactionPaybill
								.getAmountDue()
								- transactionPaybill.getCashDiscount()));
			else if (record != null)
				amtDueText.setValue(amountAsString(record.getAmountDue()
						- (record.getCashDiscount() + record.getWriteOff())));
			amtDueText.setDisabled(true);
		}

		if (canEdit) {
			if (transactionPaybill != null)
				adjustPayment = transactionPaybill.getAmountDue()
						- transactionPaybill.getCashDiscount();
			else if (record != null)
				adjustPayment = record.getAmountDue()
						- (record.getCashDiscount() + record.getWriteOff());

			initialAdjustPayment = adjustPayment;
		}

		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		form.setWidth("100%");
		if (canEdit)
			form.setFields(amtDueText);

		grid = new CreditsandPaymentsGrid(false, this,
				currencyProvider.getTransactionCurrency(), this.record);
		grid.isEnable = false;
		grid.init();
		grid.setWidth("100%");
		grid.setHeight("200px");
		grid.setCanEdit(canEdit);
		grid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		totAmtUseText = new AmountField(messages.totalAmountToUse(), this,
				currencyProvider.getTransactionCurrency());
		totAmtUseText.setColSpan(1);
		totAmtUseText.setValue("");
		totAmtUseText.setBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				checkTotalAmount(true);

			}
		});

		if (canEdit) {
			okbtn.setTitle(messages.adjust());
		} else {
			cancelBtn.setTitle(messages.close());
			okbtn.setVisible(false);
		}

		DynamicForm amountUseForm = new DynamicForm();
		amountUseForm.setFields(totAmtUseText);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setWidth("100%");
		mainVLay.add(form);
		mainVLay.add(grid);
		mainVLay.add(amountUseForm);

		setBodyLayout(mainVLay);
		setWidth("600px");
		center();
	}

	private void getInitialAmountUse() {
		double amount = 0.0d;
		double totalCreditAmount = getTotalUnuseCreditAmount();
		double appliedCredits = 0.0d;
		if (record != null) {
			appliedCredits = record.getAppliedCredits();
			amountDue = record.getAmountDue()
					- (record.getCashDiscount() + record.getWriteOff());
		} else if (transactionPaybill != null) {
			appliedCredits = transactionPaybill.getAppliedCredits();
			amountDue = transactionPaybill.getAmountDue()
					- transactionPaybill.getCashDiscount();
		}
		if (totalCreditAmount > amountDue) {
			amount = amountDue;
		} else {
			amount = totalCreditAmount;
		}
		if (appliedCredits == 0) {
			totAmtUseText.setAmount(amount);
		} else {
			totAmtUseText.setAmount(appliedCredits);
		}
		totalAmountToUse = totAmtUseText.getAmount();
		amtDueText.setAmount(amountDue);
		if (!updatedCreditsAndPayments.isEmpty()
				&& DecimalUtil.isEquals(appliedCredits, 0)) {
			checkTotalAmount(false);
		}else
		{
			checkTotalAmount(false);
		}
	}

	protected void checkTotalAmount(boolean isBlur) {
		double totalAmount = totAmtUseText.getAmount();
		if (totalAmount <= amountDue) {
			// if (isBlur) {
			// resetRecords();
			// }
			for (ClientCreditsAndPayments credit : updatedCreditsAndPayments) {
				// credit.setAmtTouse(0.0D);
				// credit.setBalance(credit.getBalance() +
				// credit.getAmtTouse());
				grid.resetValue(credit);
				if (totalAmount > 0) {
					double balance = credit.getBalance();
					if (totalAmount <= balance) {
						credit.setAmtTouse(totalAmount);
						totalAmount = 0;
					} else {
						totalAmount -= balance;
						credit.setAmtTouse(balance);
					}
					credit.setBalance(credit.getBalance()
							- credit.getAmtTouse());
					credit.setRemaoningBalance(credit.getBalance());
					grid.updateData(credit);
					grid.updateAmountValues();
				}
			}
		}
		if (totalAmount > amountDue) {
			Accounter.showError(Accounter.messages()
					.amountToUseMustLessthanTotal());
			showError();
		}
	}

	private void resetRecords() {
		for (ClientCreditsAndPayments clientCreditsAndPayments : updatedCreditsAndPayments) {
			grid.resetValue(clientCreditsAndPayments);
		}

	}

	private void showError() {

		resetRecords();
		if (record != null) {
			record.setAppliedCredits(0.0d);
		} else if (transactionPaybill != null) {
			transactionPaybill.setAppliedCredits(0.0d);
		}
		if (updatedCreditsAndPayments.isEmpty()) {
			totAmtUseText.setAmount(0.0d);
		} else if (getTotalUnuseCreditAmount() > amountDue) {
			totAmtUseText.setAmount(amountDue);
		} else {
			totAmtUseText.setAmount(getTotalUnuseCreditAmount());
		}
		double totalAmount = totAmtUseText.getAmount();
		for (ClientCreditsAndPayments credit : updatedCreditsAndPayments) {
			grid.resetValue(credit);
			if (totalAmount > 0) {
				double balance = credit.getBalance();
				if (totalAmount <= balance) {
					credit.setAmtTouse(totalAmount);
					totalAmount = 0;
				}
				credit.setBalance(credit.getBalance() - credit.getAmtTouse());
				credit.setRemaoningBalance(credit.getBalance());
				grid.updateData(credit);
				grid.updateAmountValues();
			}
		}
	}

	private void setTotalBalance(Double totalBalances) {
		if (totalBalances == null)
			totalBalances = 0.0D;

		this.totalBalances = totalBalances;

	}

	@Override
	public Object getGridColumnValue(ClientCustomer obj, int index) {
		return null;
	}

	/*
	 * This method invoked when "ok" button clicked.It'll set all the applied
	 * credits to the record using setAttribute() in PayBill/RecievPayment Grid
	 */
	public List<ClientCreditsAndPayments> getAppliedCredits() {
		List<ClientCreditsAndPayments> clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments crdPayment : grid.getRecords()) {
			if (!DecimalUtil.isEquals(crdPayment.getAmtTouse(), 0)) {
				clientCreditsAndPayments.add(crdPayment);
			}
		}
		return clientCreditsAndPayments;
	}

	/*
	 * This method returns final records for saving and is called from the
	 * transaction when it is going to save
	 */

	public List<ClientTransactionCreditsAndPayments> getTransactionCredits(
			IAccounterCore transctn) {

		List<ClientTransactionCreditsAndPayments> clientTransactionCreditsAndPayments = new ArrayList<ClientTransactionCreditsAndPayments>();
		if (transctn instanceof ClientTransactionPayBill) {
			ClientTransactionPayBill trPayBill = (ClientTransactionPayBill) transctn;
			if (trPayBill.getTempCredits() != null) {
				for (Integer indx : trPayBill.getTempCredits().keySet()) {
					ClientCreditsAndPayments crdPayment = grid
							.getRecordByIndex(indx);
					crdPayment.setBalance(crdPayment.getActualAmt());
					crdPayment.setRemaoningBalance(crdPayment.getBalance());
					crdPayment
							.setAmtTouse(((TransactionPayBillTable.TempCredit) trPayBill
									.getTempCredits().get(indx))
									.getAmountToUse());
					// }
					// for (IsSerializable obj : grid.getSelectedRecords()) {
					// ClientCreditsAndPayments crdPayment =
					// (ClientCreditsAndPayments)
					// obj;
					/*
					 * For backnd purpose,they need the original dueamount(the
					 * calculations(decreasing balance by 'amountToUse') are
					 * done at backend side)
					 */
					// crdPayment.setBalance(crdPayment.getActualAmt());
					ClientTransactionCreditsAndPayments creditsAndPayments = new ClientTransactionCreditsAndPayments();
					try {
						creditsAndPayments.setAmountToUse(crdPayment
								.getAmtTouse());
					} catch (Exception e) {
					}
					creditsAndPayments.setDate(crdPayment.getTransaction()
							.getTransactionDate());
					creditsAndPayments.setMemo(crdPayment.getMemo());
					creditsAndPayments.setCreditsAndPayments(crdPayment);
					clientTransactionCreditsAndPayments.add(creditsAndPayments);
				}
			}
		} else {
			ClientTransactionReceivePayment rcvPaymnt = (ClientTransactionReceivePayment) transctn;
			for (Integer indx : rcvPaymnt.getTempCredits().keySet()) {
				ClientCreditsAndPayments crdPayment = grid.getRecords().get(
						indx);
				crdPayment.setBalance(crdPayment.getActualAmt());
				crdPayment.setRemaoningBalance(crdPayment.getBalance());
				crdPayment
						.setAmtTouse(((TransactionReceivePaymentTable.TempCredit) (rcvPaymnt
								.getTempCredits().get(indx))).getAmountToUse());
				// }
				// for (IsSerializable obj : grid.getSelectedRecords()) {
				// ClientCreditsAndPayments crdPayment =
				// (ClientCreditsAndPayments)
				// obj;
				/*
				 * For backnd purpose,they need the original dueamount(the
				 * calculations(decreasing balance by 'amountToUse') are done at
				 * backend side)
				 */
				// crdPayment.setBalance(crdPayment.getActualAmt());
				ClientTransactionCreditsAndPayments creditsAndPayments = new ClientTransactionCreditsAndPayments();
				try {
					creditsAndPayments.setAmountToUse(crdPayment.getAmtTouse());
				} catch (Exception e) {
				}
				creditsAndPayments.setDate(crdPayment.getTransaction()
						.getTransactionDate());
				creditsAndPayments.setMemo(crdPayment.getMemo());
				creditsAndPayments.setCreditsAndPayments(crdPayment);
				clientTransactionCreditsAndPayments.add(creditsAndPayments);
			}
		}
		return clientTransactionCreditsAndPayments;
	}

	@Override
	protected boolean onOK() {
		double totalAmount = totAmtUseText.getAmount();
		if (updatedCreditsAndPayments.isEmpty() && totalAmount > 0) {
			Accounter.showError(Accounter.messages().noCreditsToApply());
			showError();
			return false;
		} else if (totalAmount > amountDue) {
			Accounter.showError(Accounter.messages()
					.amountToUseMustLessthanTotal());
			showError();
			return false;
		} else {
			return true;
		}

	}

	@Override
	protected boolean onCancel() {
		if (record != null && record.isCreditsApplied()) {
			Map<Integer, Object> appliedCredits = record.getTempCredits();
			int size = updatedCreditsAndPayments.size();
			for (int i = 0; i < size; i++) {
				if (appliedCredits.containsKey(i)) {
					ClientCreditsAndPayments selectdCredit = updatedCreditsAndPayments
							.get(i);
					TempCredit tmpCr = (TempCredit) appliedCredits.get(i);
					selectdCredit.setBalance(selectdCredit.getBalance()
							+ (selectdCredit.getAmtTouse() - tmpCr
									.getAmountToUse()));
				} else {
					ClientCreditsAndPayments unSelectdCredit = updatedCreditsAndPayments
							.get(i);
					unSelectdCredit.setAmtTouse(0);
				}
			}
		} else if (transactionPaybill != null
				&& transactionPaybill.isCreditsApplied()) {
			Map<Integer, Object> appliedCredits = transactionPaybill
					.getTempCredits();
			int size = updatedCreditsAndPayments.size();
			for (int i = 0; i < size; i++) {
				if (appliedCredits.containsKey(i)) {
					ClientCreditsAndPayments selectdCredit = updatedCreditsAndPayments
							.get(i);
					com.vimukti.accounter.web.client.ui.edittable.tables.TransactionPayBillTable.TempCredit tmpCr = (com.vimukti.accounter.web.client.ui.edittable.tables.TransactionPayBillTable.TempCredit) appliedCredits
							.get(i);
					selectdCredit.setBalance(selectdCredit.getBalance()
							+ (selectdCredit.getAmtTouse() - tmpCr
									.getAmountToUse()));
				} else {
					ClientCreditsAndPayments unSelectdCredit = updatedCreditsAndPayments
							.get(i);
					unSelectdCredit.setAmtTouse(0);
				}
			}
		} else {
			resetRecords();
		}
		return super.onCancel();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public boolean validTotalAmountUse() {
		if (DecimalUtil.isGreaterThan(totalAmountToUse, amountDue)) {
			return false;
		} else {
			totAmtUseText.setAmount(totalAmountToUse);
		}
		return true;
	}
}
