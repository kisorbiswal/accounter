package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.IGenericCallback;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CreditsandPaymentsGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionPayBillGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionReceivePaymentGrid;

/**
 * 
 * @author kumar kasimala
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class CustomerCreditsAndPaymentsDialiog extends BaseDialog {

	AmountField amtDueText, totCredAmtText, cashDiscText, totBalText,
			adjPayText, totAmtUseText;

	public CreditsandPaymentsGrid grid;

	@SuppressWarnings("unused")
	private String amountDue;
	@SuppressWarnings("unused")
	private String cashDiscount;

	@SuppressWarnings("unused")
	private int key;
	@SuppressWarnings("unused")
	private ClientCustomer customer;

	public Double totalBalances = 0.0D;
	public Double totalAmountToUse = 0.0D;

	private CustomersMessages customerConstants = GWT
			.create(CustomersMessages.class);

	public ClientTransactionReceivePayment record;

	public boolean okClicked;

	public boolean cancelClicked;

	private boolean canEdit;

	public double adjustPayment = 0.0D;

	private double initialAdjustPayment = 0.0D;

	private List<ClientCreditsAndPayments> updatedCreditsAndPayments;

	@SuppressWarnings("unused")
	private ClientVendor vendor;

	private ClientTransactionPayBill transactionPaybill;

	public double totalCreditApplied = 0.0;
	public double totalUnusedCreditAmount = 0.0;

	public CustomerCreditsAndPaymentsDialiog(ClientCustomer customer,
			List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments,
			boolean canEdit, ClientTransactionReceivePayment record) {
		super(FinanceApplication.getCustomersMessages()
				.applyCreditsAndPaymentsFor(),
		// + (customer != null ? customer.getName() : ""),
				FinanceApplication.getCustomersMessages()
						.applyCreditsAndPayments());
		this.customer = customer;
		this.canEdit = canEdit;
		updatedCreditsAndPayments = updatedCustomerCreditsAndPayments;
		this.record = record;
		createControls();
		grid.initialCreditsAndPayments(updatedCustomerCreditsAndPayments);
		addGridRecordsToListGrid(updatedCreditsAndPayments);

	}

	public CustomerCreditsAndPaymentsDialiog(ClientVendor venddor,
			List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments,
			boolean canEdit, ClientTransactionPayBill record) {
		super(FinanceApplication.getCustomersMessages()
				.applyCreditsAndPaymentsFor()
				+ (venddor != null ? venddor.getName() : ""),
				FinanceApplication.getCustomersMessages()
						.applyCreditsAndPayments());
		this.vendor = venddor;
		this.canEdit = canEdit;
		updatedCreditsAndPayments = updatedCustomerCreditsAndPayments;
		this.transactionPaybill = record;
		createControls();
		grid.initialCreditsAndPayments(updatedCustomerCreditsAndPayments);
		addGridRecordsToListGrid(updatedCreditsAndPayments);
	}

	public CustomerCreditsAndPaymentsDialiog(
			String amountDue,
			String cashDiscount,
			List<ClientCreditsAndPayments> creditsAndPayments,
			int key,
			LinkedHashMap<String, List<ClientTransactionCreditsAndPayments>> creditsAndPaymentsMap,
			IGenericCallback<String> callback) {
		super(FinanceApplication.getCustomersMessages()
				.applyCreditsAndPaymentsFor(), "");
		this.key = key;
		// this.creditsAndPaymentsMap = creditsAndPaymentsMap;

		this.amountDue = amountDue;
		this.cashDiscount = cashDiscount;
		this.updatedCreditsAndPayments = creditsAndPayments;
		// this.callback = callback;
		createControls();

	}

	public double getTotalCreditAmount() {
		totalCreditApplied = 0.0;
		for (ClientCreditsAndPayments crd : grid.getSelectedRecords()) {
			totalCreditApplied += crd.getAmtTouse();
		}
		return totalCreditApplied;
	}

	public double getTotalUnuseCreditAmount() {
		totalUnusedCreditAmount = 0.0;
		for (ClientCreditsAndPayments crd : grid.getSelectedRecords()) {
			totalUnusedCreditAmount += crd.getBalance();
		}
		return totalUnusedCreditAmount;
	}

	public void setRecord(ClientTransactionReceivePayment record) {
		this.record = record;
		if (cashDiscText != null)
			cashDiscText.setValue(DataUtils.getAmountAsString(record
					.getCashDiscount()));
	}

	public void setRecord(ClientTransactionPayBill record) {
		this.transactionPaybill = record;
		if (cashDiscText != null)
			cashDiscText.setValue(DataUtils
					.getAmountAsString(transactionPaybill.getCashDiscount()));
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
		setTotalAmountToUse(totalAmountToUse);

		setTotalBalance(totalBalances);

		setAdjustPaymentText(totalAmountToUse);

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
			grid.addEmptyMessage(FinanceApplication.getCustomersMessages()
					.therearenocreditstoshow());
		}
		for (ClientCreditsAndPayments cr : creditsAndPayments) {
			int row = grid.indexOf(cr);
			if (!DecimalUtil.isEquals(cr.getAmtTouse(), 0.0))
				((CheckBox) grid.getWidget(row, 0)).setValue(true);
		}

		double toBeSetTotalBalance = 0.0;
		double toBeSetTotalCredtAmt = 0.0d;
		double toBeSetTotalAmtTouse = 0.0d;
		for (ClientCreditsAndPayments cpRecord : creditsAndPayments) {
			toBeSetTotalBalance += cpRecord.getBalance();
			toBeSetTotalCredtAmt += cpRecord.getCreditAmount();
			// toBeSetTotalAmtTouse += cpRecord.getAmtTouse();
		}

		totBalText.setValue(DataUtils.getAmountAsString(toBeSetTotalBalance));
		totalBalances = toBeSetTotalBalance;
		totCredAmtText.setAmount(toBeSetTotalCredtAmt);

		if (!canEdit) {
			totalAmountToUse = toBeSetTotalAmtTouse;
			totalBalances = toBeSetTotalBalance;
			updateFields();
		}

	}

	private void createControls() {
		mainPanel.setSpacing(5);

		if (canEdit) {
			amtDueText = new AmountField(customerConstants.amountDue());
			amtDueText.setColSpan(1);
			if (transactionPaybill != null)
				amtDueText.setValue(DataUtils
						.getAmountAsString(transactionPaybill.getAmountDue()));
			else if (record != null)
				amtDueText.setValue(DataUtils.getAmountAsString(record
						.getAmountDue()));
			amtDueText.setDisabled(true);
		}

		totCredAmtText = new AmountField(customerConstants.totalCreditMemo());
		totCredAmtText.setColSpan(1);
		totCredAmtText.setDisabled(true);

		cashDiscText = new AmountField(customerConstants.cashDiscount());
		cashDiscText.setColSpan(1);
		if (transactionPaybill != null)
			cashDiscText.setValue(DataUtils
					.getAmountAsString(transactionPaybill.getCashDiscount()));
		else if (record != null)
			cashDiscText.setValue(DataUtils.getAmountAsString(record
					.getCashDiscount()));
		cashDiscText.setDisabled(true);

		totBalText = new AmountField(customerConstants.totalBalance());
		totBalText.setColSpan(1);
		totBalText.setDisabled(true);

		adjPayText = new AmountField(customerConstants.adjustPayment());
		adjPayText.setColSpan(1);
		adjPayText.setDisabled(true);

		if (canEdit) {
			if (transactionPaybill != null)
				adjustPayment = transactionPaybill.getAmountDue()
						- transactionPaybill.getCashDiscount();
			else if (record != null)
				adjustPayment = record.getAmountDue()
						- (record.getCashDiscount() + record.getWriteOff());

			initialAdjustPayment = adjustPayment;

			adjPayText.setValue(DataUtils
					.getAmountAsString(initialAdjustPayment));
		} else {
			if (transactionPaybill != null)
				adjPayText.setValue(DataUtils
						.getAmountAsString(transactionPaybill.getPayment()));
			else if (record != null)
				adjPayText.setValue(DataUtils.getAmountAsString(record
						.getPayment()));
		}
		totAmtUseText = new AmountField(customerConstants.totalAmountToUse());
		totAmtUseText.setColSpan(1);
		totAmtUseText.setDisabled(true);
		totAmtUseText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");

		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		form.setWidth("100%");
		if (canEdit)
			form.setFields(amtDueText, totCredAmtText, cashDiscText,
					totBalText, adjPayText, totAmtUseText);
		else
			form.setFields(totCredAmtText, cashDiscText, totBalText,
					adjPayText, totAmtUseText);

		grid = new CreditsandPaymentsGrid(true, this, this.record);
		grid.isEnable = false;
		grid.init();
		grid.setWidth("100%");
		grid.setHeight("200px");
		grid.setCanEdit(canEdit);
		grid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		if (canEdit) {
			okbtn.setTitle(customerConstants.adjust());
		} else {
			cancelBtn.setTitle(customerConstants.close());
			okbtn.setVisible(false);
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setWidth("100%");
		mainVLay.add(form);
		mainVLay.add(grid);

		setBodyLayout(mainVLay);
		setWidth("600");
		center();
	}

	public void validateTransaction() throws Exception {
		if (!grid.validateGrid()) {
			throw new Exception(
					"Select atleast on record.Or click on Cancel...");
		}
	}

	private void setAdjustPaymentText(double totalAmountToUse) {
		adjustPayment = initialAdjustPayment - totalAmountToUse;
		if (adjPayText == null)
			return;
		if (!DecimalUtil.isLessThan(adjustPayment, 0))
			this.adjPayText
					.setValue(DataUtils.getAmountAsString(adjustPayment));
		else
			this.adjPayText.setValue("0.0");

	}

	private void setTotalBalance(Double totalBalances) {
		if (totalBalances == null)
			totalBalances = 0.0D;

		totBalText.setValue(DataUtils.getAmountAsString(totalBalances));

		this.totalBalances = totalBalances;

	}

	private void setTotalAmountToUse(Double totalAmountToUse) {
		if (totalAmountToUse == null)
			totalAmountToUse = 0.0;

		totAmtUseText.setValue(DataUtils
				.getAmountAsString(getTotalCreditAmount()));

		this.totalAmountToUse = totalAmountToUse;

	}

	// public void refreshValues() {
	// grid.refreshValues();
	// }

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
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
							.setAmtTouse(((TransactionPayBillGrid.TempCredit) trPayBill
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
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
