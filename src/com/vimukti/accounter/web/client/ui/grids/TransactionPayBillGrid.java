package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CashDiscountDialog;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.customers.CustomerCreditsAndPaymentsDialiog;
import com.vimukti.accounter.web.client.ui.vendors.PayBillView;

public class TransactionPayBillGrid extends
		AbstractTransactionGrid<ClientTransactionPayBill> {

	private boolean canEdit;
	public boolean isAlreadyOpened;
	AccounterConstants vendorConstants = Accounter.constants();
	PayBillView paybillView;
	ClientVendor vendor;
	List<Integer> selectedValues = new ArrayList<Integer>();
	AccounterConstants accounterConstants = Accounter.constants();
	protected boolean gotCreditsAndPayments;
	private CashDiscountDialog cashDiscountDialog;
	public CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialiog;
	public List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments;

	/* This stack tracks the recently applied credits */
	public Stack<Map<Integer, Object>> creditsStack;
	public Stack<Map<Integer, Object>> revertedCreditsStack;

	private boolean hasRecords;
	private Stack<Map<Integer, Map<Integer, Object>>> tobeReverCredittStk = new Stack<Map<Integer, Map<Integer, Object>>>();
	private ArrayList<Map<Integer, Object>> pendingRevertedCredit = new ArrayList<Map<Integer, Object>>();

	public TransactionPayBillGrid(boolean isEdit) {
		super(isEdit, true);
		this.canEdit = isEdit;
	}

	public TransactionPayBillGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	@Override
	public void init() {
		super.init();
		this.header.setStyleName("gridHeader HEADER_GRID");
		// this.addFooterValue(FinanceApplication.constants().subTotal(),
		// canEdit ? 1 : 0);
		// // OriginalAmount total
		// this.addFooterValue(amountAsString(0.00), canEdit ? 2 :
		// 1);
		// // Total AmountDue
		// if (canEdit)
		// this.addFooterValue(amountAsString(0.00), 3);
		// // Total CashDiscount amount
		// this.addFooterValue(amountAsString(0.00), canEdit ? 5 :
		// 3);
		// // Total Payment
		// this.addFooterValue(amountAsString(0.00), canEdit ? 7 :
		// 6);
	}

	@Override
	protected int getColumnType(int col) {
		if (canEdit) {
			if (col == 5 || col == 6)
				return COLUMN_TYPE_LINK;
			if (col == 7)
				return COLUMN_TYPE_DECIMAL_TEXTBOX;
			if (col == 2 || col == 3 || col == 8)
				return COLUMN_TYPE_DECIMAL_TEXT;

		} else {
			if (col == 3 || col == 4)
				return COLUMN_TYPE_LINK;
			if (col == 1 || col == 6)
				return COLUMN_TYPE_DECIMAL_TEXT;

		}

		return COLUMN_TYPE_TEXT;
	}

	@Override
	protected Object getColumnValue(ClientTransactionPayBill paybill, int col) {
		ClientTAXItem taxItem = getCompany()
				.getTAXItem(vendor.getTaxItemCode());
		if (canEdit) {
			switch (col) {
			case 0:
				return paybill.getDueDate() != 0 ? UIUtils
						.getDateByCompanyType(new ClientFinanceDate(paybill
								.getDueDate())) : "";
			case 1:
				return paybill.getBillNumber();

			case 2:
				return amountAsString(paybill.getOriginalAmount());
			case 3:
				return amountAsString(paybill.getDummyDue());
			case 4:
				return paybill.getDiscountDate() != 0 ? UIUtils
						.getDateByCompanyType(new ClientFinanceDate(paybill
								.getDiscountDate())) : "";
			case 5:
				return amountAsString(paybill.getCashDiscount());
			case 6:
				return amountAsString(paybill.getAppliedCredits());
			case 7:
				if (taxItem != null)
					return amountAsString(paybill.getOriginalAmount()
							* (taxItem.getTaxRate() / 100));
				else
					return amountAsString(0.0);

			case 8:
				if(paybill.getPayment()!=0)
					return amountAsString(paybill.getPayment()
							- paybill.getOriginalAmount()
							* (taxItem.getTaxRate()) / 100);
				else
					return amountAsString(0.0);
				
			default:
				return "";
			}
		} else {
			switch (col) {
			case 0:
				return paybill.getBillNumber();
			case 1:
				return amountAsString(paybill.getOriginalAmount());
			case 2:
				return paybill.getDiscountDate() != 0 ? UIUtils
						.getDateByCompanyType(new ClientFinanceDate(paybill
								.getDiscountDate())) : "";
			case 3:
				return amountAsString(paybill.getCashDiscount());
			case 4:
				return amountAsString(paybill.getAppliedCredits());
			case 5:
				// FIXME-->backend people need to set it
				return "";
			case 6:
				return amountAsString(paybill.getPayment());
			default:
				return "";
			}
		}

	}

	@Override
	protected String[] getColumns() {
		if (canEdit) {

			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA)

			{
				return new String[] { vendorConstants.dueDate(),

				vendorConstants.billNo(), vendorConstants.originalAmount(),
						vendorConstants.amountDue(),
						vendorConstants.discountDate(),
						vendorConstants.cashDiscount(),
						vendorConstants.credits(), vendorConstants.tds(),
						vendorConstants.payments() };
			} else {
				return new String[] { vendorConstants.dueDate(),

				vendorConstants.billNo(), vendorConstants.originalAmount(),
						vendorConstants.amountDue(),
						vendorConstants.discountDate(),
						vendorConstants.cashDiscount(),
						vendorConstants.credits(), vendorConstants.payments() };
			}

		} else {
			return new String[] { vendorConstants.billNo(),
					vendorConstants.billAmount(),
					vendorConstants.discountDate(),
					vendorConstants.cashDiscount(), vendorConstants.tds(),
					vendorConstants.credits(), vendorConstants.referenceNo(),
					vendorConstants.amountPaid() };

		}

	}

	public void setPaymentView(PayBillView paybillView) {
		this.paybillView = paybillView;
	}

	@Override
	protected boolean isEditable(ClientTransactionPayBill obj, int row, int col) {

		if ((canEdit && col == 7) || (!canEdit && col == 6)) {
			if (!isSelected(obj)) {
				selectRow(row);
				currentRow = row;
				currentCol = col;
			}
			return true;
		}
		return false;
	}

	@Override
	protected void onClick(ClientTransactionPayBill obj, int row, int index) {
		if (!canEdit) {
			if (index == 3)// || isSelected(obj))
				openCashDiscountDialog();
		} else {
			switch (index) {
			case 5:
				// if (isSelected(obj))
				openCashDiscountDialog();
				break;
			case 6:
				// if (isSelected(obj))
				openCreditsDialog();
				break;

			default:
				// List<Integer> tt = new ArrayList<Integer>();
				// tt.add(5);
				// tt.add(6);
				// tt.add(7);
				// if (!(tt.contains(index)))
				// selectRow(row);
				break;
			}

		}
	}

	@Override
	public void selectRow(int row) {
		if (getWidget(row, 0) != null)
			if (((CheckBox) getWidget(row, 0)).getValue() != true)
				super.selectRow(row);
	}

	@Override
	public void editComplete(ClientTransactionPayBill editingRecord,
			Object value, int col) {

		try {

			// if (value != null
			// && value.toString().startsWith(
			// "" + UIUtils.getCurrencySymbol() + "")) {
			// value = value.toString().replaceAll(
			// "" + UIUtils.getCurrencySymbol() + "", "");
			// }
			double payment = Double.parseDouble(DataUtils
					.getReformatedAmount(value.toString()) + "");
			editingRecord.setPayment(payment);
			updateAmountDue(editingRecord);
			updateData(editingRecord);
			paybillView.adjustAmountAndEndingBalance();
			updateFootervalues(editingRecord);

		} catch (Exception e) {
			Accounter.showError(accounterConstants.invalidateEntry());
		}
		super.editComplete(editingRecord, value, col);
	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getSelectedRecords().size() == 0) {
			result.addError(this, Accounter.constants()
					.pleaseSelectAnyOneOfTheTransactions());
		}

		// validates receive payment amount excesses due amount or not
		for (ClientTransactionPayBill transactionPayBill : this
				.getSelectedRecords()) {

			double totalValue = getTotalValue(transactionPayBill);
			if (!AccounterValidator.isValidReceive_Payment(transactionPayBill
					.getAmountDue(), totalValue, Accounter.constants()
					.receivePaymentExcessDue())) {
				// FIXME
				result.addError(transactionPayBill.getBillNumber(), Accounter
						.constants().receivePaymentExcessDue());
			}
		}

		return result;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 1)
			return 108;
		else
			return 109;
	}

	public CustomCombo getCustomCombo(int colIndex) {
		return null;
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionPayBill obj,
			int colIndex) {

		return null;
	}

	public void initCreditsAndPayments(final ClientVendor vendor) {

		this.vendor = vendor;
		Accounter
				.createHomeService()
				.getVendorCreditsAndPayments(
						vendor.getID(),
						new AccounterAsyncCallback<ArrayList<ClientCreditsAndPayments>>() {

							public void onException(AccounterException caught) {
								Accounter.showInformation(Accounter.messages()
										.failedTogetCreditsListAndPayments(
												vendor.getName()));

								gotCreditsAndPayments = false;
								return;

							}

							public void onResultSuccess(
									ArrayList<ClientCreditsAndPayments> result) {
								if (result == null)
									onFailure(null);

								updatedCustomerCreditsAndPayments = result;
								creditsStack = new Stack<Map<Integer, Object>>();

								paybillView.adjustAmountAndEndingBalance();
								paybillView.calculateUnusedCredits();

								gotCreditsAndPayments = true;

							}

						});

	}

	public void openCashDiscountDialog() {
		// if (cashDiscountDialog == null) {
		// ClientAccount discountAccount = null;
		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// discountAccount = getCompany().getAccountByName(
		// companyConstants.cashDiscountTaken());
		// } else if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// discountAccount = getCompany().getAccountByName(
		// companyConstants.discounts());
		// }
		cashDiscountDialog = new CashDiscountDialog(canEdit,
				selectedObject.getCashDiscount(), getCompany().getAccount(
						selectedObject.getDiscountAccount()));
		// } else {
		// cashDiscountDialog.setCanEdit(canEdit);
		// cashDiscountDialog.setCashDiscountValue(selectedObject
		// .getCashDiscount());
		// cashDiscountDialog.
		// }
		cashDiscountDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public void onCancel() {

			}

			@Override
			public boolean onOK() {
				try {
					selectedObject.setCashDiscount(cashDiscountDialog
							.getCashDiscount());
					setAttribute("cashAccount",
							cashDiscountDialog.selectedDiscountAccount
									.getName(), currentRow);
					updateRecord(selectedObject, currentRow, 3);
					paybillView.adjustPaymentValue(selectedObject);
					updateFootervalues(selectedObject);
				} catch (Exception e) {
					Accounter.showError(e.getMessage());
					return false;

				}

				paybillView.adjustAmountAndEndingBalance();
				return true;
			}
		});
		cashDiscountDialog.show();
	}

	public void openCreditsDialog() {
		if (gotCreditsAndPayments) {
			if (creditsAndPaymentsDialiog == null) {
				initCreditsDialogInstance();
			} else {
				if (selectedObject.isCreditsApplied()) {

					Map<Integer, Object> appliedCredits = selectedObject
							.getTempCredits();

					int size = updatedCustomerCreditsAndPayments.size();
					/* 'i' is index of credit record in creditsGrid */
					for (int i = 0; i < size; i++) {

						if (appliedCredits.containsKey(i)) {
							if (tobeReverCredittStk != null
									&& tobeReverCredittStk.size() != 0) {
								updatePendingCredits(i);
							} else {// end of toBeRevertdStk's if

								/*
								 * Get credits from payBill obj and apply them
								 * to creditRecords to display in creditsGrid
								 */
								ClientCreditsAndPayments selectdCredit = updatedCustomerCreditsAndPayments
										.get(i);
								TempCredit tmpCr = (TempCredit) appliedCredits
										.get(i);
								selectdCredit.setAmtTouse(tmpCr
										.getAmountToUse());
								selectdCredit.setBalance(tmpCr
										.getRemainingBalance());
							}
						} else {// END of appliedCredits.containsKey(i)

							if (tobeReverCredittStk != null
									&& tobeReverCredittStk.size() != 0) {
								updatePendingCredits(i);
							} else {
								ClientCreditsAndPayments unSelectdCredit = updatedCustomerCreditsAndPayments
										.get(i);
								unSelectdCredit.setAmtTouse(0);
							}

						}
					}
					Iterator<Map<Integer, Map<Integer, Object>>> pendingCreditsItrt = tobeReverCredittStk
							.iterator();
					for (; pendingCreditsItrt.hasNext();) {
						Map<Integer, Map<Integer, Object>> pMap = pendingCreditsItrt
								.next();
						if (pMap.containsKey(indexOf(selectedObject)))
							pMap.remove(indexOf(selectedObject));
					}
				} else { // End of --- if (selectedObject.isCreditsApplied())
					if (revertedCreditsStack != null
							&& revertedCreditsStack.size() != 0) {
						/*
						 * Update the stack credits by adding recently reverted
						 * credits
						 */
						Map<Integer, Object> stkCredit = revertedCreditsStack
								.peek();

						for (Integer indx : stkCredit.keySet()) {

							TempCredit tempCrt = (TempCredit) stkCredit
									.get(indx);
							ClientCreditsAndPayments rec = updatedCustomerCreditsAndPayments
									.get(indx.intValue());
							rec.setBalance(tempCrt.getRemainingBalance()
									+ tempCrt.getAmountToUse());
							rec.setRemaoningBalance(rec.getBalance());
							rec.setAmtTouse(0);
						}
						revertedCreditsStack.clear();
					} else if (creditsStack != null && creditsStack.size() != 0) {

						/* Update the stack with pending credits */
						Map<Integer, Object> stkCredit = creditsStack.peek();

						/*
						 * Get the credit from stack n set it to credit record
						 * for display
						 */
						for (Integer indx : stkCredit.keySet()) {

							TempCredit stkCrt = (TempCredit) stkCredit
									.get(indx);
							ClientCreditsAndPayments rec = updatedCustomerCreditsAndPayments
									.get(indx.intValue());
							rec.setBalance(stkCrt.getRemainingBalance());
							rec.setAmtTouse(0);
						}
					}
				}
				creditsAndPaymentsDialiog
						.setUpdatedCreditsAndPayments(updatedCustomerCreditsAndPayments);
				creditsAndPaymentsDialiog.setCanEdit(canEdit);
				creditsAndPaymentsDialiog.setRecord(selectedObject);
				creditsAndPaymentsDialiog.setVendor(vendor);
				creditsAndPaymentsDialiog.updateFields();

			}

			creditsAndPaymentsDialiog
					.addInputDialogHandler(new InputDialogHandler() {

						@Override
						public void onCancel() {
							creditsAndPaymentsDialiog.cancelClicked = true;
							// selectedObject
							// .setAppliedCredits(creditsAndPaymentsDialiog
							// .getTotalCreditAmount());
							// updateData(selectedObject);
						}

						@Override
						public boolean onOK() {
							List<ClientCreditsAndPayments> appliedCreditsForThisRec = creditsAndPaymentsDialiog.grid
									.getSelectedRecords();
							Map<Integer, Object> appliedCredits = new HashMap<Integer, Object>();
							TempCredit creditRec = null;

							for (ClientCreditsAndPayments rec : appliedCreditsForThisRec) {
								try {
									checkBalance(rec.getAmtTouse());
								} catch (Exception e) {
									Accounter.showError(e.getMessage());
									return false;
								}
								Integer recordIndx = creditsAndPaymentsDialiog.grid
										.indexOf(rec);
								creditRec = new TempCredit();
								for (ClientTransactionPayBill pb : getSelectedRecords()) {
									if (pb.isCreditsApplied()) {
										for (Integer idx : pb.getTempCredits()
												.keySet()) {
											if (recordIndx == idx)
												((TempCredit) pb
														.getTempCredits().get(
																idx))
														.setRemainingBalance(rec
																.getBalance());
										}
									}
								}
								creditRec.setRemainingBalance(rec.getBalance());
								creditRec.setAmountToUse(rec.getAmtTouse());
								appliedCredits.put(recordIndx, creditRec);
							}

							try {

								creditsAndPaymentsDialiog.okClicked = true;

								// creditsAndPaymentsDialiog.validateTransaction();

							} catch (Exception e) {

								// if (e instanceof Payment)
								// Accounter
								// .showError(((PaymentExcessException) e)
								// .getMessage());
								// else
								Accounter.showError(e.getMessage());
								return false;

							}

							selectedObject.setTempCredits(appliedCredits);
							selectedObject.setCreditsApplied(true);

							creditsStack.push(appliedCredits);

							selectedObject
									.setAppliedCredits(creditsAndPaymentsDialiog
											.getTotalCreditAmount());

							// selectedObject.setPayment(selectedObject
							// .getAmountDue()
							// - selectedObject.getAppliedCredits());

							// setAttribute("creditsAndPayments",
							// creditsAndPaymentsDialiog
							// .getAppliedCredits(), currentRow);

							paybillView.adjustPaymentValue(selectedObject);
							updateRecord(selectedObject, currentRow, 4);
							updateFootervalues(selectedObject);
							paybillView.unUsedCreditsText
									.setAmount(creditsAndPaymentsDialiog.totalBalances);
							return true;
						}
					});
			creditsAndPaymentsDialiog.show();

		} else {
			Accounter.showInformation(Accounter.messages()
					.noCreditsForThisVendor(Global.get().vendor()));
		}

	}

	private void updatePendingCredits(int i) {
		int curntPayBillRecIndx = indexOf(selectedObject);
		Iterator<Map<Integer, Map<Integer, Object>>> pendingCredits = tobeReverCredittStk
				.iterator();
		Map<Integer, Map<Integer, Object>> pek = tobeReverCredittStk.peek();
		/*
		 * find the size of credits map whose size is bigger than all other
		 * creditmaps
		 */
		int siz = 0;
		for (Integer ky : pek.keySet()) {
			if (siz < pek.get(ky).size()) {
				siz = pek.get(ky).size();
			}
		}
		for (int s = 0; pendingCredits.hasNext(); s++) {
			Map<Integer, Map<Integer, Object>> pendingCrs = pendingCredits
					.next();
			if (pendingCrs.containsKey(curntPayBillRecIndx)) {
				Map<Integer, Object> toBeAdCrsMap = pendingCrs
						.get(curntPayBillRecIndx);
				if (toBeAdCrsMap != null && toBeAdCrsMap.containsKey(i)) {
					for (Integer k : toBeAdCrsMap.keySet()) {
						if (k.intValue() == i) {
							TempCredit addCr = (TempCredit) toBeAdCrsMap.get(i);
							ClientCreditsAndPayments selectdCredit = updatedCustomerCreditsAndPayments
									.get(i);
							TempCredit tmpCr = (TempCredit) selectedObject
									.getTempCredits().get(i);
							tmpCr.setRemainingBalance(tmpCr
									.getRemainingBalance()
									+ addCr.getAmountToUse());
							selectdCredit.setAmtTouse(tmpCr.getAmountToUse());
							selectdCredit.setBalance(tmpCr
									.getRemainingBalance());
						}
					}
				}
			}
		}

	}

	private void initCreditsDialogInstance() {
		for (ClientCreditsAndPayments rec : updatedCustomerCreditsAndPayments) {
			rec.setActualAmt(rec.getBalance());
			rec.setRemaoningBalance(rec.getBalance());
		}
		creditsAndPaymentsDialiog = new CustomerCreditsAndPaymentsDialiog(
				this.vendor, updatedCustomerCreditsAndPayments, canEdit,
				selectedObject);

	}

	public void checkBalance(double amount) throws Exception {
		if (DecimalUtil.isEquals(amount, 0))
			throw new Exception(Accounter.constants()
					.youdnthaveBalToApplyCredits());
	}

	/* A POJO class which holds the credits of a paybill record temporarly */
	public class TempCredit {
		double remainingBalance;
		double amountToUse;

		public double getRemainingBalance() {
			return remainingBalance;
		}

		public void setRemainingBalance(double remainingBalance) {
			this.remainingBalance = remainingBalance;
		}

		public double getAmountToUse() {
			return amountToUse;
		}

		public void setAmountToUse(double amountToUse) {
			this.amountToUse = amountToUse;
		}

	}

	@Override
	public void setCanEdit(boolean enabled) {
		this.canEdit = enabled;
		super.setCanEdit(enabled);
	}

	/*
	 * This method inkvoked when headercheckbox value is true.And calls the
	 * methods to update the non editable fileds and record's payment value.
	 */
	public void selectAllRows() {
		for (ClientTransactionPayBill obj : this.getRecords()) {
			// if (!isSelected(obj)) {
			((CheckBox) this.body.getWidget(indexOf(obj), 0)).setValue(true);
			selectedValues.add(indexOf(obj));
			this.rowFormatter.addStyleName(indexOf(obj), "selected");
			// updateValue(obj);
			// }
		}
		paybillView.adjustAmountAndEndingBalance();
	}

	/*
	 * This method invoked each time when record(s) get selected & it updates
	 * the footer values
	 */
	public void updateValue(ClientTransactionPayBill obj) {
		obj.setPayment(obj.getAmountDue());
		updateTotalPayment(obj);
		updateRecord(obj, indexOf(obj), canEdit ? 7 : 6);
	}

	public void updateTotalPayment(ClientTransactionPayBill obj) {
		ClientTransaction transactionObject = paybillView
				.getTransactionObject();
		// paybillView.gettrantransactionTotal = 0.0;
		transactionObject.setTotal(0.0);
		for (ClientTransactionPayBill rec : getSelectedRecords()) {
			// paybillView.transactionTotal += rec.getPayment();
			transactionObject.setTotal(transactionObject.getTotal()
					+ rec.getPayment());
			paybillView.totalCashDiscount += rec.getCashDiscount();
		}
		/* updating payment(or PaidAmount) */
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paybillView.transactionTotal), canEdit ? 7
		// : 6);
		paybillView.adjustPaymentValue(obj);
		updateAmountDue(obj);
	}

	/*
	 * This method invoked each time when record values/its selection status
	 * changed.It updates the footervalues in editmode & in creationmodeF
	 */
	public void updateFootervalues(ClientTransactionPayBill obj) {
		ClientTransaction transactionObject = paybillView
				.getTransactionObject();
		if (canEdit) {
			// paybillView.transactionTotal = 0.0;
			transactionObject.setTotal(0.0);
			paybillView.totalCashDiscount = 0.0;
			for (ClientTransactionPayBill rec : getSelectedRecords()) {
				// paybillView.transactionTotal += rec.getPayment();
				transactionObject.setTotal(transactionObject.getTotal()
						+ rec.getPayment());
				paybillView.totalCashDiscount += rec.getCashDiscount();
			}
			// this.updateFooterValues(DataUtils
			// .getAmountAsString(paybillView.transactionTotal), 7);
			// this.updateFooterValues(DataUtils
			// .getAmountAsString(paybillView.totalCashDiscount), 5);
		} else {
			// paybillView.transactionTotal = 0.0;
			transactionObject.setTotal(0.0);
			paybillView.totalCashDiscount = 0.0;
			paybillView.totalOriginalAmount = 0.0;
			for (ClientTransactionPayBill rec : getRecords()) {
				paybillView.totalOriginalAmount += rec.getOriginalAmount();
				// paybillView.transactionTotal += rec.getPayment();
				transactionObject.setTotal(transactionObject.getTotal()
						+ rec.getPayment());
				paybillView.totalCashDiscount += rec.getCashDiscount();
			}
			/* */
			// this.updateFooterValues(DataUtils
			// .getAmountAsString(paybillView.totalOriginalAmount),
			// canEdit ? 2 : 1);
			// this.updateFooterValues(DataUtils
			// .getAmountAsString(paybillView.transactionTotal),
			// canEdit ? 7 : 6);
			// this.updateFooterValues(DataUtils
			// .getAmountAsString(paybillView.totalCashDiscount),
			// canEdit ? 5 : 3);
		}
	}

	public void deleteTotalPayment(ClientTransactionPayBill rec) {
		ClientTransaction transactionObject = paybillView
				.getTransactionObject();
		// paybillView.transactionTotal -= rec.getPayment();
		transactionObject.setTotal(transactionObject.getTotal()
				- rec.getPayment());
		paybillView.totalCashDiscount -= rec.getCashDiscount();

		/* updating payment column's footer */
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paybillView.transactionTotal), canEdit ? 7
		// : 6);
		// if (!DecimalUtil.isLessThan(paybillView.totalCashDiscount, 0))
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paybillView.totalCashDiscount),
		// canEdit ? 5 : 3);
	}

	/*
	 * This method invoked when header checkbox value is false(unchecked)
	 */
	public void resetValues() {
		/* Revert all credits to its original state */
		for (ClientCreditsAndPayments crdt : updatedCustomerCreditsAndPayments) {
			crdt.setBalance(crdt.getActualAmt());
			crdt.setRemaoningBalance(0);
			crdt.setAmtTouse(0);
		}
		for (ClientTransactionPayBill obj : this.getRecords()) {
			obj.setTempCredits(null);
			if (creditsAndPaymentsDialiog != null
					&& creditsAndPaymentsDialiog.grid.getRecords().size() == 0)
				creditsStack.clear();

			obj.setPayment(0.0);
			obj.setCashDiscount(0);
			obj.setAppliedCredits(0);
			this.rowFormatter.removeStyleName((Integer) indexOf(obj),
					"selected");
			selectedValues.remove((Integer) indexOf(obj));
			updateData(obj);
		}
		updateFootervalues(null);
		paybillView.resetTotlas();
		creditsAndPaymentsDialiog = null;
		cashDiscountDialog = null;
	}

	/*
	 * This method invoked when record is unselected and resets the record's
	 * amounts to its default values
	 */
	public void resetValue(ClientTransactionPayBill obj) {
		if (obj.isCreditsApplied()) {
			int size = updatedCustomerCreditsAndPayments.size();
			Map<Integer, Object> toBeRvrtMap = obj.getTempCredits();
			for (Map<Integer, Object> rMp : pendingRevertedCredit) {
				for (Integer k : toBeRvrtMap.keySet()) {
					if (rMp.containsKey(k)) {
						TempCredit tc = (TempCredit) rMp.get(k);
						TempCredit toBeAdC = (TempCredit) toBeRvrtMap.get(k);
						tc.setRemainingBalance(tc.getRemainingBalance()
								+ toBeAdC.getAmountToUse());
					}
				}
			}
			pendingRevertedCredit.add(toBeRvrtMap);
			/*
			 * 'i' is creditRecord(in creditGrid) index.Update all the
			 * appliedCredit records(in transactionPaybills) with the unselected
			 * record's credits
			 */
			for (int i = 0; i < size; i++) {
				if (toBeRvrtMap.containsKey(i)) {
					TempCredit toBeAddCr = (TempCredit) toBeRvrtMap.get(i);
					/*
					 * search for this revertedCreditRecord in all selected
					 * payBill record's credits
					 */
					// tobeReverCredittStk.clear();
					if (getSelectedRecords().size() != 0) {
						for (int j = 0; j < getSelectedRecords().size(); j++) {
							Map<Integer, Object> pbCrsMap = getSelectedRecords()
									.get(j).getTempCredits();
							if (pbCrsMap != null && pbCrsMap.containsKey(i)) {
								TempCredit chngCrd = (TempCredit) pbCrsMap
										.get(i);
								chngCrd.setRemainingBalance(chngCrd
										.getRemainingBalance()
										+ toBeAddCr.getAmountToUse());
							} else {
								/*
								 * place the pendingcredit in a map n place this
								 * map in another map suchthat the paybillrecord
								 * number as key for this map
								 */
								// if (pbCrsMap != null) {
								Map<Integer, Object> pendingCredit = new HashMap<Integer, Object>();
								pendingCredit.put(i, toBeAddCr);
								Map<Integer, Map<Integer, Object>> unSelectedpbCredits = new HashMap<Integer, Map<Integer, Object>>();
								unSelectedpbCredits.put(
										indexOf(getSelectedRecords().get(j)),
										pendingCredit);
								tobeReverCredittStk.push(unSelectedpbCredits);
								// }
							}
						}
					} else {
						/*
						 * If the there are no selected records,then save these
						 * reverted credits in a stack.And apply these credits
						 * when any record selected
						 */
						// revertedCreditsStack = new Stack<Map<Integer,
						// Object>>();
						// revertedCreditsStack.push(toBeRvrtMap);
						for (ClientCreditsAndPayments crdt : updatedCustomerCreditsAndPayments) {
							crdt.setBalance(crdt.getActualAmt());
							crdt.setRemaoningBalance(0);
							crdt.setAmtTouse(0);
							creditsAndPaymentsDialiog.grid.updateData(crdt);
						}
					}
				}
			}
			obj.setTempCredits(null);
			obj.setCreditsApplied(false);
		}
		if (creditsAndPaymentsDialiog != null
				&& creditsAndPaymentsDialiog.grid.getRecords().size() == 0)
			creditsStack.clear();
		updateAmountDue(obj);
		deleteTotalPayment(obj);
		obj.setPayment(0.0d);
		obj.setCashDiscount(0.0d);
		obj.setAppliedCredits(0.0d);
		updateData(obj);
		paybillView.adjustAmountAndEndingBalance();
		updateFootervalues(obj);
	}

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		if (isChecked) {
			selectAllRows();
		} else {
			resetValues();
		}
		super.onHeaderCheckBoxClick(isChecked);
	}

	public boolean isSelected(ClientTransactionPayBill transactionList) {
		return canEdit ? ((CheckBox) getWidget(indexOf(transactionList), 0))
				.getValue() : true;

	}

	@Override
	protected void onSelectionChanged(ClientTransactionPayBill obj, int row,
			boolean isChecked) {
		if (isChecked && !selectedValues.contains(row)) {
			this.rowFormatter.addStyleName(row, "selected");
			selectedValues.add(row);
			updateValue(obj);
		} else {
			this.rowFormatter.removeStyleName(row, "selected");
			selectedValues.remove((Integer) row);
			resetValue(obj);
		}
		super.onSelectionChanged(obj, row, isChecked);
	}

	public boolean isEmptyGrid() {
		for (ClientTransactionPayBill bill : getRecords()) {
			if (isSelected(bill)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {

		return null;
	}

	public void updateAmountDue(ClientTransactionPayBill item) {
		double totalValue = item.getCashDiscount() + item.getAppliedCredits()
				+ item.getPayment();

		if (!DecimalUtil.isGreaterThan(totalValue, item.getAmountDue())) {
			item.setDummyDue(item.getAmountDue() - totalValue);
		} else {
			item.setDummyDue(0.0);
		}
	}

	private double getTotalValue(ClientTransactionPayBill payment) {
		double totalValue = payment.getCashDiscount()
				+ payment.getAppliedCredits() + payment.getPayment();
		return totalValue;
	}

	@Override
	public void setTaxCode(long taxCode) {

	}
}
