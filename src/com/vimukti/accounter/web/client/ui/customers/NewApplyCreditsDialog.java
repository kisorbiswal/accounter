package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.IGenericCallback;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionPayBillTable;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CreditsandPaymentsGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionReceivePaymentGrid;

public class NewApplyCreditsDialog extends BaseDialog<ClientCustomer> {

	AmountField amtDueText, totAmtUseText;

	public CreditsandPaymentsGrid grid;

	private double amountDue;

	private int key;

	private ClientCustomer customer;

	public Double totalBalances = 0.0D;
	public Double totalAmountToUse = 0.0D;

	private AccounterConstants customerConstants = Accounter.constants();

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

	public NewApplyCreditsDialog(ClientCustomer customer,
			List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments,
			boolean canEdit, ClientTransactionReceivePayment record) {
		super(Accounter.constants().applyCreditsAndPaymentsFor(),
		// + (customer != null ? customer.getName() : ""),
				Accounter.constants().applyCreditsandPayments());
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
			boolean canEdit, ClientTransactionPayBill record) {
		super(Accounter.constants().applyCreditsAndPaymentsFor()
				+ (venddor != null ? venddor.getName() : ""), Accounter
				.constants().applyCreditsandPayments());
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
			IGenericCallback<String> callback) {
		super(Accounter.constants().applyCreditsAndPaymentsFor(), "");
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
		for (ClientCreditsAndPayments crd : grid.getActualRecords()) {
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
			grid.addEmptyMessage(Accounter.constants()
					.therearenocreditstoshow());
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
			amtDueText = new AmountField(customerConstants.amountDue(), this);
			amtDueText.setColSpan(1);
			if (transactionPaybill != null)
				amtDueText.setValue(amountAsString(transactionPaybill
						.getAmountDue()));
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

		grid = new CreditsandPaymentsGrid(false, this, this.record);
		grid.isEnable = false;
		grid.init();
		grid.setWidth("100%");
		grid.setHeight("200px");
		grid.setCanEdit(canEdit);
		grid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		totAmtUseText = new AmountField(customerConstants.totalAmountToUse(),
				this);
		totAmtUseText.setColSpan(1);
		totAmtUseText.setValue("");
		totAmtUseText.setBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				checkTotalAmount(true);

			}
		});

		if (canEdit) {
			okbtn.setTitle(customerConstants.adjust());
		} else {
			cancelBtn.setTitle(customerConstants.close());
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
		amtDueText.setAmount(amountDue);
		checkTotalAmount(false);
	}

	protected void checkTotalAmount(boolean isBlur) {
		double totalAmount = totAmtUseText.getAmount();
		if (totalAmount <= amountDue) {
			if (isBlur) {
				resetRecords();
			}
			for (ClientCreditsAndPayments credit : updatedCreditsAndPayments) {
				credit.setAmtTouse(0.0D);
				if (totalAmount > 0) {
					double balance = credit.getBalance();
					if (totalAmount <= balance) {
						credit.setAmtTouse(totalAmount);
						totalAmount = 0;
					} else {
						totalAmount -= balance;
						credit.setAmtTouse(balance);
					}
					grid.updateData(credit);
					grid.updateAmountValues();
				}
			}
		} else {
			showError();
		}

	}

	private void resetRecords() {
		for (ClientCreditsAndPayments clientCreditsAndPayments : updatedCreditsAndPayments) {
			clientCreditsAndPayments.setAmtTouse(0.0D);
			grid.updateData(clientCreditsAndPayments);
			grid.updateAmountValues();
		}

	}

	private void showError() {
		Accounter.showError("Total Amount to use must be lessthan amount due.");
		resetRecords();
		record.setAppliedCredits(0.0d);
		if (getTotalUnuseCreditAmount() > amountDue) {
			totAmtUseText.setAmount(amountDue);
		} else {
			totAmtUseText.setAmount(getTotalCreditAmount());
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
	public List<ClientTransactionCreditsAndPayments> getAppliedCredits() {
		List<ClientTransactionCreditsAndPayments> clientTransactionCreditsAndPayments = new ArrayList<ClientTransactionCreditsAndPayments>();
		for (IsSerializable obj : grid.getSelectedRecords()) {
			ClientCreditsAndPayments crdPayment = (ClientCreditsAndPayments) obj;
			crdPayment.setBalance(crdPayment.getBalance());
			ClientTransactionCreditsAndPayments creditsAndPayments = new ClientTransactionCreditsAndPayments();
			try {
				creditsAndPayments.setAmountToUse(DataUtils
						.getAmountStringAsDouble(totAmtUseText.getValue()
								.toString()));
			} catch (Exception e) {
			}
			creditsAndPayments.setCreditsAndPayments(crdPayment);
			clientTransactionCreditsAndPayments.add(creditsAndPayments);
		}
		return clientTransactionCreditsAndPayments;
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
					ClientCreditsAndPayments crdPayment = grid.getRecords()
							.get(indx);
					crdPayment.setBalance(crdPayment.getActualAmt());
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
				crdPayment
						.setAmtTouse(((TransactionReceivePaymentGrid.TempCredit) (rcvPaymnt
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
		if (totalAmount > amountDue) {
			showError();
			return false;
		} else {
			return true;
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
