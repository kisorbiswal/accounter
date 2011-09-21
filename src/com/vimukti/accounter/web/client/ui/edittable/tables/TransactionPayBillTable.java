package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CashDiscountDialog;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.customers.CustomerCreditsAndPaymentsDialiog;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.AnchorEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public abstract class TransactionPayBillTable extends
		EditTable<ClientTransactionPayBill> {
	private boolean canEdit;
	private ClientVendor vendor;
	private List<Integer> selectedValues = new ArrayList<Integer>();
	private boolean gotCreditsAndPayments;
	private CashDiscountDialog cashDiscountDialog;
	private CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialiog;
	private List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments;

	/* This stack tracks the recently applied credits */
	private Stack<Map<Integer, Object>> creditsStack;
	private Stack<Map<Integer, Object>> revertedCreditsStack;

	private Stack<Map<Integer, Map<Integer, Object>>> tobeReverCredittStk = new Stack<Map<Integer, Map<Integer, Object>>>();
	private ArrayList<Map<Integer, Object>> pendingRevertedCredit = new ArrayList<Map<Integer, Object>>();
	public boolean isAlreadyOpened;

	public TransactionPayBillTable(boolean canEdit) {
		this.canEdit = canEdit;
		initColumn();
	}

	private void initColumn() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionPayBill>() {

			@Override
			protected void onHeaderValueChanged(boolean value) {
				onHeaderCheckBoxClick(value);
				selectAllRows(value);
			}

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionPayBill row) {
				onSelectionChanged(row, value);
			}

		});

		if (canEdit) {
			TextEditColumn<ClientTransactionPayBill> dueDate = new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return DateUtills.getDateAsString(new ClientFinanceDate(row
							.getDueDate()).getDateAsObject());
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No need
				}

				@Override
				public int getWidth() {
					return 108;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().dueDate();
				}
			};
			this.addColumn(dueDate);
		}

		TextEditColumn<ClientTransactionPayBill> billNo = new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return row.getBillNumber();
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 108;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().billNo();
			}
		};
		this.addColumn(billNo);

		if (canEdit) {
			this.addColumn(new AmountColumn<ClientTransactionPayBill>() {

				@Override
				public int getWidth() {
					return 108;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().originalAmount();
				}

				@Override
				protected double getAmount(ClientTransactionPayBill row) {
					return row.getOriginalAmount();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						double value) {

				}
			});

			this.addColumn(new AmountColumn<ClientTransactionPayBill>() {

				@Override
				public int getWidth() {
					return 108;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().amountDue();
				}

				@Override
				protected double getAmount(ClientTransactionPayBill row) {
					return row.getDummyDue();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						double value) {

				}
			});
		} else {
			this.addColumn(new AmountColumn<ClientTransactionPayBill>() {

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				public int getWidth() {
					return 108;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().billAmount();
				}

				@Override
				protected double getAmount(ClientTransactionPayBill row) {
					return row.getOriginalAmount();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						double value) {
					//

				}
			});
		}

		this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return DateUtills.getDateAsString(row.getDiscountDate());
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
				// No Need

			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 108;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().discountDate();
			}
		});

		this.addColumn(new AnchorEditColumn<ClientTransactionPayBill>() {

			@Override
			protected void onClick(ClientTransactionPayBill row) {
				openCashDiscountDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return DataUtils.getAmountAsString(row.getCashDiscount());
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().cashDiscount();
			}
		});

		if (!canEdit) {

			addTdsColumn();

		}
		if (canEdit) {
			this.addColumn(new AnchorEditColumn<ClientTransactionPayBill>() {

				@Override
				protected void onClick(ClientTransactionPayBill row) {
					openCreditsDialog(row);
				}

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return DataUtils.getAmountAsString(row.getAppliedCredits());
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().credits();
				}
			});

			if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
				addTdsColumn();
			}

			this.addColumn(new AmountColumn<ClientTransactionPayBill>() {

				@Override
				protected String getColumnName() {
					return Accounter.constants().payments();
				}

				@Override
				protected double getAmount(ClientTransactionPayBill row) {
					return row.getPayment();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						double value) {
					onSelectionChanged(row, true);
					row.setPayment(value);
					updateAmountDue(row);
					adjustAmountAndEndingBalance();
					updateFootervalues(row, canEdit);
					update(row);
				}
			});
		}

		if (!canEdit) {
			this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return "";
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No Need
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().referenceNo();
				}
			});
			this.addColumn(new AmountColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return DataUtils.getAmountAsString(row.getPayment());
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().amountPaid();
				}

				@Override
				protected double getAmount(ClientTransactionPayBill row) {
					return row.getPayment();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						double value) {

				}
			});
		}
	}

	protected void selectAllRows(boolean value) {
		List<ClientTransactionPayBill> allRows = getAllRows();
		for (ClientTransactionPayBill row : allRows) {
			onSelectionChanged(row, value);
		}
	}

	private void onSelectionChanged(ClientTransactionPayBill obj,
			boolean isChecked) {
		int row = indexOf(obj);
		if (isChecked && !selectedValues.contains(row)) {
			selectedValues.add(row);
			updateValue(obj);
		} else {
			selectedValues.remove((Integer) row);
			resetValue(obj);
		}
		super.checkColumn(row, 0, isChecked);
	}

	/*
	 * This method invoked each time when record(s) get selected & it updates
	 * the footer values
	 */
	public void updateValue(ClientTransactionPayBill obj) {
		obj.setPayment(obj.getAmountDue());
		updateAmountDue(obj);
		updateTotalPayment(obj);
		update(obj);
	}

	protected abstract void updateTotalPayment(ClientTransactionPayBill obj);

	private void initCreditsDialogInstance(
			ClientTransactionPayBill selectedObject) {
		for (ClientCreditsAndPayments rec : updatedCustomerCreditsAndPayments) {
			rec.setActualAmt(rec.getBalance());
			rec.setRemaoningBalance(rec.getBalance());
		}
		setCreditsAndPaymentsDialiog(new CustomerCreditsAndPaymentsDialiog(
				this.vendor, updatedCustomerCreditsAndPayments, canEdit,
				selectedObject));

	}

	private void updatePendingCredits(int i,
			ClientTransactionPayBill selectedObject) {
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
		for (; pendingCredits.hasNext();) {
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

	public int indexOf(ClientTransactionPayBill selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	private void openCreditsDialog(final ClientTransactionPayBill selectedObject) {
		if (gotCreditsAndPayments) {
			if (getCreditsAndPaymentsDialiog() == null) {
				initCreditsDialogInstance(selectedObject);
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
								updatePendingCredits(i, selectedObject);
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
								updatePendingCredits(i, selectedObject);
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
				getCreditsAndPaymentsDialiog().setUpdatedCreditsAndPayments(
						updatedCustomerCreditsAndPayments);
				getCreditsAndPaymentsDialiog().setCanEdit(canEdit);
				getCreditsAndPaymentsDialiog().setRecord(selectedObject);
				getCreditsAndPaymentsDialiog().setVendor(vendor);
				getCreditsAndPaymentsDialiog().updateFields();

			}

			getCreditsAndPaymentsDialiog().addInputDialogHandler(
					new InputDialogHandler() {

						@Override
						public void onCancel() {
							getCreditsAndPaymentsDialiog().cancelClicked = true;
							// selectedObject
							// .setAppliedCredits(creditsAndPaymentsDialiog
							// .getTotalCreditAmount());
							// updateData(selectedObject);
						}

						@Override
						public boolean onOK() {
							List<ClientCreditsAndPayments> appliedCreditsForThisRec = getCreditsAndPaymentsDialiog().grid
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
								Integer recordIndx = getCreditsAndPaymentsDialiog().grid
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

								getCreditsAndPaymentsDialiog().okClicked = true;

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
									.setAppliedCredits(getCreditsAndPaymentsDialiog()
											.getTotalCreditAmount());

							// selectedObject.setPayment(selectedObject
							// .getAmountDue()
							// - selectedObject.getAppliedCredits());

							// setAttribute("creditsAndPayments",
							// creditsAndPaymentsDialiog
							// .getAppliedCredits(), currentRow);

							adjustPaymentValue(selectedObject);

							update(selectedObject);

							updateFootervalues(selectedObject, canEdit);
							setUnUsedCreditsTextAmount(getCreditsAndPaymentsDialiog().totalBalances);
							return true;
						}
					});
			getCreditsAndPaymentsDialiog().show();

		} else {
			Accounter.showInformation(Accounter.messages()
					.noCreditsForThisVendor(Global.get().vendor()));
		}

	}

	protected abstract void setUnUsedCreditsTextAmount(Double totalBalances);

	public List<ClientTransactionPayBill> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}

	private void checkBalance(double amount) throws Exception {
		if (DecimalUtil.isEquals(amount, 0))
			throw new Exception(Accounter.constants()
					.youdnthaveBalToApplyCredits());
	}

	private void openCashDiscountDialog(
			final ClientTransactionPayBill selectedObject) {
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
		cashDiscountDialog = new CashDiscountDialog(canEdit, selectedObject
				.getCashDiscount(), getCompany().getAccount(
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

					// TODO setAttribute("cashAccount",
					// cashDiscountDialog.selectedDiscountAccount
					// .getName(), currentRow);

					update(selectedObject);

					adjustPaymentValue(selectedObject);
					updateFootervalues(selectedObject, canEdit);
				} catch (Exception e) {
					Accounter.showError(e.getMessage());
					return false;
				}
				adjustAmountAndEndingBalance();
				return true;
			}
		});
		cashDiscountDialog.show();
	}

	protected abstract void adjustPaymentValue(
			ClientTransactionPayBill selectedObject);

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	protected abstract void updateFootervalues(ClientTransactionPayBill row,
			boolean canEdit);

	protected abstract void adjustAmountAndEndingBalance();

	private void addTdsColumn() {

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA
				&& getCompany().getPreferences().isTDSEnabled()) {
			this.addColumn(new AmountColumn<ClientTransactionPayBill>() {

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().tds();
				}

				@Override
				protected double getAmount(ClientTransactionPayBill row) {
					ClientTAXItem taxItem = Accounter.getCompany().getTAXItem(
							vendor.getTaxItemCode());
					if (taxItem != null)
						return row.getPayment() * (taxItem.getTaxRate() / 100);
					else
						return 0;
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						double value) {
					// No Need

				}
			});
		}
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getSelectedRecords().size() == 0) {
			result.addError(this, Accounter.constants()
					.pleaseSelectAnyOneOfTheTransactions());
		}

		// // validates receive payment amount excesses due amount or not
		// for (ClientTransactionPayBill transactionPayBill : this
		// .getSelectedRecords()) {
		//
		// double totalValue = getTotalValue(transactionPayBill);
		// if (!AccounterValidator.isValidReceive_Payment(transactionPayBill
		// .getAmountDue(), totalValue, Accounter.constants()
		// .receivePaymentExcessDue())) {
		// result.addError(transactionPayBill.getBillNumber(), Accounter
		// .constants().receivePaymentExcessDue());
		// }
		// }

		return result;
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

								adjustAmountAndEndingBalance();
								calculateUnusedCredits();

								gotCreditsAndPayments = true;

							}

						});

	}

	protected abstract void calculateUnusedCredits();

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

	private void resetValues() {
		/* Revert all credits to its original state */
		if (updatedCustomerCreditsAndPayments == null) {
			return;
		}
		for (ClientCreditsAndPayments crdt : updatedCustomerCreditsAndPayments) {
			crdt.setBalance(crdt.getActualAmt());
			crdt.setRemaoningBalance(0);
			crdt.setAmtTouse(0);
		}
		for (ClientTransactionPayBill obj : this.getAllRows()) {
			obj.setTempCredits(null);
			if (getCreditsAndPaymentsDialiog() != null
					&& getCreditsAndPaymentsDialiog().grid.getRecords().size() == 0)
				creditsStack.clear();

			obj.setPayment(0.0);
			obj.setCashDiscount(0);
			obj.setAppliedCredits(0);
			selectedValues.remove((Integer) indexOf(obj));
			update(obj);
		}
		updateFootervalues(null, canEdit);
		resetTotlas();
		setCreditsAndPaymentsDialiog(null);
		cashDiscountDialog = null;
	}

	protected abstract void resetTotlas();

	private void resetValue(ClientTransactionPayBill obj) {
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
							getCreditsAndPaymentsDialiog().grid
									.updateData(crdt);
						}
					}
				}
			}
			obj.setTempCredits(null);
			obj.setCreditsApplied(false);
		}
		if (getCreditsAndPaymentsDialiog() != null
				&& getCreditsAndPaymentsDialiog().grid.getRecords().size() == 0)
			creditsStack.clear();
		deleteTotalPayment(obj);
		obj.setPayment(0.0d);
		obj.setCashDiscount(0.0d);
		obj.setAppliedCredits(0.0d);
		updateAmountDue(obj);
		update(obj);
		adjustAmountAndEndingBalance();
		updateFootervalues(obj, canEdit);
	}

	protected abstract void deleteTotalPayment(ClientTransactionPayBill obj);

	private void onHeaderCheckBoxClick(boolean isChecked) {
		if (isChecked) {
			adjustAmountAndEndingBalance();
		} else {
			resetValues();
		}
	}

	public void updateAmountDue(ClientTransactionPayBill item) {
		double totalValue = item.getCashDiscount() + item.getAppliedCredits()
				+ item.getPayment();

		if (!DecimalUtil.isGreaterThan(totalValue, item.getAmountDue())) {
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA
					&& getCompany().getPreferences().isTDSEnabled()) {

				ClientTAXItem taxItem = Accounter.getCompany().getTAXItem(
						vendor.getTaxItemCode());
				if (taxItem != null)
					item.setDummyDue(item.getAmountDue()
							- (totalValue + (taxItem.getTaxRate() / 100 * item
									.getOriginalAmount())));
				// item.setDummyDue(item.getAmountDue() - totalValue);
			} else {
				item.setDummyDue(item.getAmountDue() - totalValue);
			}
		} else {
			item.setDummyDue(0.0);
		}
	}

	private double getTotalValue(ClientTransactionPayBill payment) {
		double totalValue = payment.getCashDiscount()
				+ payment.getAppliedCredits() + payment.getPayment();
		return totalValue;
	}

	public CustomerCreditsAndPaymentsDialiog getCreditsAndPaymentsDialiog() {
		return creditsAndPaymentsDialiog;
	}

	public void setCreditsAndPaymentsDialiog(
			CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialiog) {
		this.creditsAndPaymentsDialiog = creditsAndPaymentsDialiog;
	}

	public void setRecords(List<ClientTransactionPayBill> records) {
		setAllRows(records);
	}

	public List<ClientCreditsAndPayments> getUpdatedCustomerCreditsAndPayments() {
		return updatedCustomerCreditsAndPayments;
	}

	public Stack<Map<Integer, Object>> getCreditsStack() {
		return creditsStack;
	}

	public void setCreditsStack(Stack<Map<Integer, Object>> creditsStack) {
		this.creditsStack = creditsStack;
	}

	public void removeAllRecords() {
		clear();
	}

	public void addEmptyMessage(String noRecordsToShow) {
		// TODO Auto-generated method stub

	}

	public void addLoadingImagePanel() {
		// TODO Auto-generated method stub

	}

	public void selectRow(int count) {
		onSelectionChanged(getRecords().get(count), true);
	}

	public List<ClientTransactionPayBill> getRecords() {
		return getAllRows();
	}
}
