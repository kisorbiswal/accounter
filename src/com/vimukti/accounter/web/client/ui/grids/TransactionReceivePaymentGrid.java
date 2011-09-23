package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CashDiscountDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.customers.CustomerCreditsAndPaymentsDialiog;
import com.vimukti.accounter.web.client.ui.customers.NewApplyCreditsDialog;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentView;
import com.vimukti.accounter.web.client.ui.customers.WriteOffDialog;

public class TransactionReceivePaymentGrid extends
		AbstractTransactionGrid<ClientTransactionReceivePayment> {
	AccounterConstants customerConstants = Accounter.constants();
	AccounterMessages accounterMessages = Accounter.messages();

	ReceivePaymentView paymentView;
	ClientCustomer customer;
	List<Integer> selectedValues = new ArrayList<Integer>();
	protected boolean gotCreditsAndPayments;
	private boolean canEdit;
	private CashDiscountDialog cashDiscountDialog;
	private WriteOffDialog writeOffDialog;

	public NewApplyCreditsDialog creditsAndPaymentsDialiog;
	public List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
	public Map<ClientTransactionReceivePayment, List<ClientCreditsAndPayments>> value;

	/* This stack tracks the recently applied credits */
	public Stack<Map<Integer, Object>> creditsStack;
	public Map<Integer, Object> revrtedCreditsMap;
	private Stack<Map<Integer, Object>> revertedCreditsStack;

	List<ClientTransactionReceivePayment> tranReceivePayments = new ArrayList<ClientTransactionReceivePayment>();
	private int[] columns = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_LINK,
			ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX };
	private int[] columnsinEdit = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_LINK,
			ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX };

	@Override
	public void init() {
		super.init();
		this.header.setStyleName("gridHeader HEADER_GRID");
		// this.addFooterValue(FinanceApplication.constants().subTotal(),
		// 1);
		// this.addFooterValue("" + UIUtils.getCurrencySymbol() + "0.0", 2);
		// if (!paymentView.isEditMode())
		// this.addFooterValue("" + UIUtils.getCurrencySymbol() + "0.0", 3);
		// this.addFooterValue(FinanceApplication.constants().totalcolan()
		// + UIUtils.getCurrencySymbol() + " 0.0", 8);
	}

	public void setPaymentView(ReceivePaymentView paymentView) {
		this.paymentView = paymentView;
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}

	public TransactionReceivePaymentGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	@Override
	protected int getColumnType(int col) {
		if (canEdit)
			return columns[col];
		else
			return columnsinEdit[col];
	}

	@Override
	protected String[] getColumns() {
		if (canEdit) {
			return new String[] { customerConstants.dueDate(),
					customerConstants.invoice(),
					customerConstants.invoiceAmount(),
					customerConstants.amountDue(),
					customerConstants.discountDate(),
					customerConstants.cashDiscount(),
					customerConstants.writeOff(),
					customerConstants.appliedCredits(),
					customerConstants.payment() };
		} else
			return new String[] { customerConstants.invoice(),
					customerConstants.invoiceAmount(),
					customerConstants.discountDate(),
					customerConstants.cashDiscount(),
					customerConstants.writeOff(),
					customerConstants.appliedCredits(),
					customerConstants.payment() };
	}

	@Override
	protected Object getColumnValue(
			ClientTransactionReceivePayment receivePayment, int col) {

		if (canEdit) {
			switch (col) {
			case 0:
				if (receivePayment.getDueDate() != 0)
					return UIUtils.getDateByCompanyType(new ClientFinanceDate(
							receivePayment.getDueDate()));
				return "";
			case 1:
				return receivePayment.getNumber();
			case 2:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getInvoiceAmount()));
			case 3:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getDummyDue()));
			case 4:
				return UIUtils.getDateByCompanyType(new ClientFinanceDate(
						receivePayment.getDiscountDate()));
			case 5:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getCashDiscount()));
			case 6:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getWriteOff()));
			case 7:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getAppliedCredits()));

			case 8:

				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getPayment()));

			default:
				break;
			}
		} else {
			switch (col) {
			case 0:
				return receivePayment.getNumber();
			case 1:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getInvoiceAmount()));
			case 2:
				return UIUtils.getDateByCompanyType(new ClientFinanceDate(
						receivePayment.getDiscountDate()));
			case 3:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getCashDiscount()));
			case 4:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getWriteOff()));
			case 5:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getAppliedCredits()));
			case 6:
				return amountAsString(getAmountInForeignCurrency(receivePayment
						.getPayment()));
			default:
				break;
			}
		}
		return null;
	}

	@Override
	public void editComplete(ClientTransactionReceivePayment item,
			Object value, int col) {

		if ((canEdit && col == 8) || (!canEdit && col == 6)) {
			double amt, originalPayment;
			try {
				originalPayment = item.getPayment();
				amt = getAmountInBaseCurrency(Double.valueOf(value.toString()));
				item.setPayment(amt);
				updateAmountDue(item);
				double totalValue = item.getCashDiscount() + item.getWriteOff()
						+ item.getAppliedCredits() + item.getPayment();
				if (AccounterValidator.isValidReceive_Payment(item
						.getAmountDue(), totalValue, Accounter.constants()
						.receivePaymentExcessDue())) {
					paymentView.recalculateGridAmounts();
					updateTotalPayment(0.0);
				} else {
					item.setPayment(originalPayment);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		updateData(item);
		super.editComplete(item, value, col);
	}

	@Override
	protected boolean isEditable(ClientTransactionReceivePayment obj, int row,
			int col) {
		if ((canEdit && col == 8) || (!canEdit && col == 6)) {
			if (!isSelected(obj)) {
				selectRow(row);
				currentCol = col;
			}
			return true;
		}
		return false;
	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validates receive amount exceeds due amount
		for (ClientTransactionReceivePayment transactionReceivePayment : this
				.getSelectedRecords()) {
			double totalValue = getTotalValue(transactionReceivePayment);
			if (DecimalUtil.isLessThan(totalValue, 0.00)) {
				result.addError(this, accounterMessages
						.valueCannotBe0orlessthan0(customerConstants.amount()));
			} else if (DecimalUtil.isGreaterThan(totalValue,
					transactionReceivePayment.getAmountDue())
					|| DecimalUtil.isEquals(totalValue, 0)) {
				result.addError(this, Accounter.constants()
						.receivePaymentExcessDue());
			}
		}
		return result;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
		case 1:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			return 80;
		case 2:
		case 3:
			return 85;

		default:
			break;
		}
		return -1;
	}

	public CustomCombo getCustomCombo(int colIndex) {
		return null;
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(
			ClientTransactionReceivePayment obj, int colIndex) {

		return null;
	}

	@Override
	protected void onClick(ClientTransactionReceivePayment obj, int row,
			int index) {
		if (canEdit) {
			switch (index) {
			case 5:
				// if (isSelected(obj))
				openCashDiscountDialog();
				break;
			case 6:
				// if (isSelected(obj))
				openWriteOffDialog();
				break;
			case 7:
				// if (isSelected(obj)) {
				openCreditsDialog();
				// }
				break;

			default:
				// List<Integer> tt = new ArrayList<Integer>();
				// tt.add(5);
				// tt.add(6);
				// tt.add(7);
				// tt.add(8);
				// if (!(tt.contains(index)))
				// selectRow(row);
				break;
			}
		} else {
			switch (index) {
			case 3:
				openCashDiscountDialog();
				break;
			case 4:
				openWriteOffDialog();
				break;
			case 5:
				// openCreditsDialog();
				break;

			default:
				// List<Integer> tt = new ArrayList<Integer>();
				// tt.add(3);
				// tt.add(4);
				// tt.add(5);
				// // tt.add(6);
				// if (!(tt.contains(index)))
				// selectRow(row);
				break;
			}
		}
	}

	@Override
	public void selectRow(int row) {
		if (((CheckBox) getWidget(row, 0)).getValue() != true)
			super.selectRow(row);
	}

	public void initCreditsAndPayments(final ClientCustomer customer) {

		Accounter
				.createHomeService()
				.getCustomerCreditsAndPayments(
						customer.getID(),
						new AccounterAsyncCallback<ArrayList<ClientCreditsAndPayments>>() {

							public void onException(AccounterException caught) {
								Accounter.showInformation(Accounter.messages()
										.failedTogetCreditsListAndPayments(
												customer.getName()));

								gotCreditsAndPayments = false;
								return;

							}

							public void onResultSuccess(
									ArrayList<ClientCreditsAndPayments> result) {
								if (result == null)
									onFailure(null);

								updatedCustomerCreditsAndPayments = result;
								creditsStack = new Stack<Map<Integer, Object>>();
								paymentView.calculateUnusedCredits();

								gotCreditsAndPayments = true;

							}

						});

	}

	public void openCashDiscountDialog() {
		cashDiscountDialog = new CashDiscountDialog(canEdit, selectedObject
				.getCashDiscount(), getCashDiscountAccount());
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
				if (canEdit) {
					if (cashDiscountDialog.getSelectedDiscountAccount() != null) {
						selectedObject.setPayment(0.0);
						selectedObject.setCashDiscount(cashDiscountDialog
								.getCashDiscount());
						selectedObject.setDiscountAccount(cashDiscountDialog
								.getSelectedDiscountAccount().getID());
						if (validatePaymentValue()) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setCashDiscount(0.0D);
							updatePayment(selectedObject);
						}

						updateData(selectedObject);
						paymentView.recalculateGridAmounts();
					} else
						return false;
					// paymentView.updateFooterValues();
					paymentView.recalculateGridAmounts();
				}
				return true;
			}
		});
		cashDiscountDialog.show();
	}

	private ClientAccount getCashDiscountAccount() {

		ClientAccount cashDiscountAccount = getCompany().getAccount(
				selectedObject.getDiscountAccount());
		return cashDiscountAccount;
	}

	private ClientAccount getWriteOffAccount() {

		ClientAccount writeOffAccount = getCompany().getAccount(
				selectedObject.getWriteOffAccount());
		return writeOffAccount;
	}

	public void openCreditsDialog() {
		if (gotCreditsAndPayments) {
			if (creditsAndPaymentsDialiog == null) {
				for (ClientCreditsAndPayments rec : updatedCustomerCreditsAndPayments) {
					rec.setActualAmt(rec.getBalance());
					rec.setRemaoningBalance(rec.getBalance());
				}
				creditsAndPaymentsDialiog = new NewApplyCreditsDialog(
						this.customer, updatedCustomerCreditsAndPayments,
						canEdit, selectedObject);
			} else {
				if (selectedObject.isCreditsApplied()) {
					Map<Integer, Object> appliedCredits = selectedObject
							.getTempCredits();
					int size = updatedCustomerCreditsAndPayments.size();
					for (int i = 0; i < size; i++) {
						if (appliedCredits.containsKey(i)) {
							ClientCreditsAndPayments selectdCredit = updatedCustomerCreditsAndPayments
									.get(i);
							TempCredit tmpCr = (TempCredit) appliedCredits
									.get(i);
							selectdCredit.setAmtTouse(tmpCr.getAmountToUse());
							selectdCredit.setBalance(tmpCr
									.getRemainingBalance());
						} else {
							ClientCreditsAndPayments unSelectdCredit = updatedCustomerCreditsAndPayments
									.get(i);
							unSelectdCredit.setAmtTouse(0);
						}
					}
				} else {
					if (revertedCreditsStack != null
							&& revertedCreditsStack.size() != 0) {
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
						Map<Integer, Object> stkCredit = creditsStack.peek();

						for (Integer indx : stkCredit.keySet()) {

							TempCredit tempCrt = (TempCredit) stkCredit
									.get(indx);
							ClientCreditsAndPayments rec = updatedCustomerCreditsAndPayments
									.get(indx.intValue());
							rec.setBalance(tempCrt.getRemainingBalance());
							rec.setAmtTouse(0);
						}
					}
				}
				creditsAndPaymentsDialiog
						.setUpdatedCreditsAndPayments(updatedCustomerCreditsAndPayments);
				creditsAndPaymentsDialiog.setCanEdit(canEdit);
				creditsAndPaymentsDialiog.setRecord(selectedObject);
				creditsAndPaymentsDialiog.setCustomer(customer);
				creditsAndPaymentsDialiog.updateFields();
			}
		} else if (!gotCreditsAndPayments && canEdit) {
			Accounter.showInformation(Accounter.messages()
					.noCreditsforthiscustomer(Global.get().customer()));
		}
		if (!canEdit) {
			creditsAndPaymentsDialiog = new NewApplyCreditsDialog(
					this.customer,
					getSelectedCreditsAndPayments(selectedObject), canEdit,
					selectedObject);
		}

		if (creditsAndPaymentsDialiog == null)
			return;
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
							for (ClientTransactionReceivePayment rcvp : getSelectedRecords()) {
								if (rcvp.isCreditsApplied()) {
									for (Integer idx : rcvp.getTempCredits()
											.keySet()) {
										if (recordIndx == idx)
											((TempCredit) rcvp.getTempCredits()
													.get(idx))
													.setRemainingBalance(rec
															.getBalance());
									}
								}
							}
							creditRec.setRemainingBalance(rec.getBalance());
							creditRec.setAmountToUse(rec.getAmtTouse());
							appliedCredits.put(recordIndx, creditRec);
						}
						selectedObject.setTempCredits(appliedCredits);
						selectedObject.setCreditsApplied(true);

						creditsStack.push(appliedCredits);

						try {

							creditsAndPaymentsDialiog.okClicked = true;

							// creditsAndPaymentsDialiog.validateTransaction();

						} catch (Exception e) {
							Accounter.showError(e.getMessage());
							return false;
						}

						selectedObject
								.setAppliedCredits(creditsAndPaymentsDialiog
										.getTotalCreditAmount());
						paymentView.recalculateGridAmounts();
						updateRecord(selectedObject, currentRow, 4);
						updatePayment(selectedObject);
						// paymentView.updateFooterValues();
						paymentView.recalculateGridAmounts();
						paymentView.unUsedCreditsText
								.setAmount(creditsAndPaymentsDialiog.totalBalances);
						updateData(selectedObject);
						return true;
					}
				});
		creditsAndPaymentsDialiog.show();

	}

	public void checkBalance(double amount) throws Exception {
		if (DecimalUtil.isEquals(amount, 0))
			throw new Exception(Accounter.constants()
					.youdnthaveBalToApplyCredits());
	}

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

	private List<ClientCreditsAndPayments> getSelectedCreditsAndPayments(
			ClientTransactionReceivePayment selectedObject) {
		List<ClientCreditsAndPayments> createdCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		ClientTransactionReceivePayment tranReceivePayment;
		tranReceivePayment = this.tranReceivePayments
				.get(indexOf(selectedObject));
		for (ClientTransactionCreditsAndPayments trancreditsAndPaymnets : tranReceivePayment
				.getTransactionCreditsAndPayments()) {
			ClientCreditsAndPayments creditsAmdPayment = trancreditsAndPaymnets
					.getCreditsAndPayments();
			creditsAmdPayment.setAmtTouse(trancreditsAndPaymnets
					.getAmountToUse());
			createdCreditsAndPayments.add(creditsAmdPayment);
		}
		return createdCreditsAndPayments;
	}

	public void setTranReceivePayments(
			List<ClientTransactionReceivePayment> recievePayments) {
		this.tranReceivePayments = recievePayments;
	}

	protected boolean validatePaymentValue() {
		double totalValue = getTotalValue(selectedObject);
		if (AccounterValidator.isValidReceive_Payment(selectedObject
				.getAmountDue(), totalValue, Accounter.constants()
				.receiveAmountPayDue())) {
			return true;
		} else
			return false;

	}

	private void updatePayment(ClientTransactionReceivePayment payment) {
		double paymentValue = payment.getAmountDue() - getTotalValue(payment);
		payment.setPayment(paymentValue);
		updateAmountDue(payment);
	}

	private double getTotalValue(ClientTransactionReceivePayment payment) {
		double totalValue = payment.getCashDiscount() + payment.getWriteOff()
				+ payment.getAppliedCredits() + payment.getPayment();
		return totalValue;
	}

	public void openWriteOffDialog() {
		writeOffDialog = new WriteOffDialog(getCompany().getActiveAccounts(),
				selectedObject, canEdit, getWriteOffAccount());
		writeOffDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public void onCancel() {

			}

			@Override
			public boolean onOK() {
				if (canEdit) {
					if (writeOffDialog.getSelectedWriteOffAccount() != null) {
						selectedObject.setPayment(0.0);
						selectedObject.setWriteOff(writeOffDialog
								.getCashDiscountValue());
						selectedObject.setWriteOffAccount(writeOffDialog
								.getSelectedWriteOffAccount().getID());
						if (validatePaymentValue()) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setWriteOff(0.0D);
							updatePayment(selectedObject);
						}
						updateData(selectedObject);
						paymentView.recalculateGridAmounts();
						// paymentView.updateFooterValues();
					}

				}
				return true;
			}
		});
		writeOffDialog.show();
	}

	@Override
	public void setCanEdit(boolean enabled) {
		this.canEdit = enabled;
		super.setCanEdit(enabled);
	}

	public void selectAllRows() {
		for (ClientTransactionReceivePayment obj : this.getRecords()) {
			// if (!isSelected(obj)) {
			((CheckBox) this.body.getWidget(indexOf(obj), 0)).setValue(true);
			selectedValues.add(indexOf(obj));
			this.rowFormatter.addStyleName(indexOf(obj), "selected");
			updateValue(obj);
			// }
		}
		updateAmountReceived();
		paymentView.recalculateGridAmounts();
	}

	public void updateValue(ClientTransactionReceivePayment obj) {
		// setAccountDefaultValues(obj);
		obj.setPayment(obj.getAmountDue());
		// updatePayment(obj);
		updateTotalPayment(obj.getPayment());
		obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
		updateRecord(obj, indexOf(obj), canEdit ? 8 : 6);
		updateData(obj);
		// currentCol = canEdit ? 8 : 6;
		// currentRow = indexOf(obj);

	}

	// private void setAccountDefaultValues(ClientTransactionReceivePayment obj)
	// {
	// ClientAccount cashDiscountAccount, writeOffAccount;
	// int accountType = getCompany().getAccountingType();
	// switch (accountType) {
	// case ClientCompany.ACCOUNTING_TYPE_UK:
	// cashDiscountAccount = getCompany().getAccountByName(
	// AccounterClientConstants.DISCOUNTS);
	//
	// writeOffAccount = getCompany().getAccountByName(
	// AccounterClientConstants.DISCOUNTS_TAKEN);
	// obj.setDiscountAccount(cashDiscountAccount.getID());
	// obj.setWriteOffAccount(writeOffAccount.getID());
	// break;
	// case ClientCompany.ACCOUNTING_TYPE_US:
	// cashDiscountAccount = getCompany().getAccountByName(
	// AccounterClientConstants.CASH_DISCOUNT_TAKEN);
	// writeOffAccount = getCompany().getAccountByName(
	// AccounterClientConstants.DISCOUNTS_TAKEN);
	// obj.setDiscountAccount(cashDiscountAccount.getID());
	// obj.setWriteOffAccount(writeOffAccount.getID());
	// break;
	// }
	// }

	public void updateTotalPayment(Double payment) {
		paymentView.transactionTotal += payment;
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paymentView.transactionTotal), canEdit ? 8
		// : 6);
	}

	public void deleteTotalPayment(Double payment) {
		paymentView.transactionTotal -= payment;
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paymentView.transactionTotal), canEdit ? 8
		// : 6);
	}

	public void resetValues() {
		for (ClientCreditsAndPayments crdt : updatedCustomerCreditsAndPayments) {
			crdt.setBalance(crdt.getActualAmt());
			crdt.setRemaoningBalance(0);
			crdt.setAmtTouse(0);
		}
		for (ClientTransactionReceivePayment obj : this.getRecords()) {
			resetValue(obj);
			paymentView.recalculateGridAmounts();
			if (creditsAndPaymentsDialiog != null
					&& creditsAndPaymentsDialiog.grid.getRecords().size() == 0)
				creditsStack.clear();
			this.rowFormatter.removeStyleName(indexOf(obj), "selected");
			selectedValues.remove((Integer) indexOf(obj));
		}
		creditsAndPaymentsDialiog = null;
		cashDiscountDialog = null;
		writeOffDialog = null;
	}

	public void resetValue(ClientTransactionReceivePayment obj) {
		if (obj.isCreditsApplied()) {
			int size = updatedCustomerCreditsAndPayments.size();
			Map<Integer, Object> toBeRvrtMap = obj.getTempCredits();
			/* 'i' is creditRecord(in creditGrid) index */
			for (int i = 0; i < size; i++) {
				if (toBeRvrtMap.containsKey(i)) {
					TempCredit toBeAddCr = (TempCredit) toBeRvrtMap.get(i);
					/*
					 * search for this revertedCreditRecord in all selected
					 * payBill record's credits
					 */
					if (getSelectedRecords().size() != 0) {
						for (int j = 0; j < getSelectedRecords().size(); j++) {
							Map<Integer, Object> rcvCrsMap = getSelectedRecords()
									.get(j).getTempCredits();
							if (rcvCrsMap.containsKey(i)) {
								TempCredit chngCrd = (TempCredit) rcvCrsMap
										.get(i);
								chngCrd.setRemainingBalance(chngCrd
										.getRemainingBalance()
										+ toBeAddCr.getAmountToUse());
							}
						}
					} else {
						revertedCreditsStack = new Stack<Map<Integer, Object>>();
						revertedCreditsStack.push(toBeRvrtMap);
					}
				}
			}

			obj.setCreditsApplied(false);
		}
		if (creditsAndPaymentsDialiog != null
				&& creditsAndPaymentsDialiog.grid.getRecords().size() == 0)
			creditsStack.clear();

		// setAccountDefaultValues(obj);
		deleteTotalPayment(obj.getPayment());
		obj.setPayment(0.0d);
		obj.setCashDiscount(0.0d);
		obj.setWriteOff(0.0d);
		obj.setAppliedCredits(0.0d);
		obj.setDummyDue(obj.getAmountDue());
		updateData(obj);
		updateUnuseAmt();
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

	public boolean isSelected(ClientTransactionReceivePayment transactionList) {
		return canEdit ? ((CheckBox) getWidget(indexOf(transactionList), 0))
				.getValue() : true;
	}

	@Override
	protected void onSelectionChanged(ClientTransactionReceivePayment obj,
			int row, boolean isChecked) {
		if (isChecked && !selectedValues.contains(row)) {
			this.rowFormatter.addStyleName(row, "selected");
			selectedValues.add(row);
			updateValue(obj);
			// updateUnuseAmt();
		} else {
			selectedValues.remove((Integer) row);
			this.rowFormatter.removeStyleName(row, "selected");
			resetValue(obj);
		}
		super.onSelectionChanged(obj, row, isChecked);

		updateAmountReceived();
	}

	public void updateUnuseAmt() {
		paymentView.unUsedPayments = (paymentView.amountRecieved - paymentView.transactionTotal);
		paymentView.setUnusedPayments(paymentView.unUsedPayments);
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		return null;
	}

	public Double getTotal() {
		double total = 0.0;
		for (ClientTransactionReceivePayment payment : this.getRecords()) {
			total += payment.getPayment();
		}
		return total;
	}

	private void updateAmountReceived() {
		double toBeSetAmount = 0.0;
		for (ClientTransactionReceivePayment receivePayment : getSelectedRecords()) {
			toBeSetAmount += receivePayment.getPayment();
		}
		paymentView.setAmountRecieved(toBeSetAmount);
	}

	public void updateAmountDue(ClientTransactionReceivePayment item) {
		double totalValue = item.getCashDiscount() + item.getWriteOff()
				+ item.getAppliedCredits() + item.getPayment();

		if (!DecimalUtil.isGreaterThan(totalValue, item.getAmountDue())) {
			if (!DecimalUtil.isLessThan(item.getPayment(), 0.00))
				item.setDummyDue(item.getAmountDue() - totalValue);
			else
				item.setDummyDue(item.getAmountDue() + item.getPayment()
						- totalValue);

		}
	}

	@Override
	public void setTaxCode(long taxCode) {
		// currently not using anywhere in the project.

	}
}
