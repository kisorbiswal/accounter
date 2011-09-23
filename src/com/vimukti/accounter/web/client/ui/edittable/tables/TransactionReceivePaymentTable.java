package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CashDiscountDialog;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.customers.CustomerCreditsAndPaymentsDialiog;
import com.vimukti.accounter.web.client.ui.customers.WriteOffDialog;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.AnchorEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public abstract class TransactionReceivePaymentTable extends
		EditTable<ClientTransactionReceivePayment> {
	private ClientCompany company;
	private boolean canEdit;

	AccounterConstants customerConstants = Accounter.constants();
	AccounterMessages accounterMessages = Accounter.messages();

	ClientCustomer customer;
	List<Integer> selectedValues = new ArrayList<Integer>();
	protected boolean gotCreditsAndPayments;
	private CashDiscountDialog cashDiscountDialog;
	private WriteOffDialog writeOffDialog;

	public CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialiog;
	public List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
	public Map<ClientTransactionReceivePayment, List<ClientCreditsAndPayments>> value;

	/* This stack tracks the recently applied credits */
	public Stack<Map<Integer, Object>> creditsStack;
	public Map<Integer, Object> revrtedCreditsMap;
	private Stack<Map<Integer, Object>> revertedCreditsStack;

	List<ClientTransactionReceivePayment> tranReceivePayments = new ArrayList<ClientTransactionReceivePayment>();

	public TransactionReceivePaymentTable(boolean canEdit) {
		this.canEdit = canEdit;
		this.company = Accounter.getCompany();
	}

	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionReceivePayment row) {
				onSelectionChanged(row, value);
			}

		});
		if (canEdit) {
			TextEditColumn<ClientTransactionReceivePayment> dateCoulmn = new TextEditColumn<ClientTransactionReceivePayment>() {

				@Override
				protected void setValue(ClientTransactionReceivePayment row,
						String value) {
				}

				@Override
				protected String getValue(ClientTransactionReceivePayment row) {
					return DateUtills.getDateAsString(new ClientFinanceDate(row
							.getDueDate()).getDateAsObject());
				}

				@Override
				public int getWidth() {
					return 80;
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
			this.addColumn(dateCoulmn);
		}

		TextEditColumn<ClientTransactionReceivePayment> invoiceNumber = new TextEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return row.getNumber();
			}

			@Override
			protected void setValue(ClientTransactionReceivePayment row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 85;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().invoice();
			}
		};
		this.addColumn(invoiceNumber);

		AmountColumn<ClientTransactionReceivePayment> invoiceAmountColumn = new AmountColumn<ClientTransactionReceivePayment>() {

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 85;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().invoiceAmount();
			}

			@Override
			protected double getAmount(ClientTransactionReceivePayment row) {
				return row.getInvoiceAmount();
			}

			@Override
			protected void setAmount(ClientTransactionReceivePayment row,
					double value) {

			}
		};
		this.addColumn(invoiceAmountColumn);

		if (canEdit) {
			AmountColumn<ClientTransactionReceivePayment> amountDueColumn = new AmountColumn<ClientTransactionReceivePayment>() {

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				public int getWidth() {
					return 80;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().amountDue();
				}

				@Override
				protected double getAmount(ClientTransactionReceivePayment row) {
					return row.getDummyDue();
				}

				@Override
				protected void setAmount(ClientTransactionReceivePayment row,
						double value) {

				}
			};
			this.addColumn(amountDueColumn);
		}

		TextEditColumn<ClientTransactionReceivePayment> discountDateColumn = new TextEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void setValue(ClientTransactionReceivePayment row,
					String value) {
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return "";// row.getDiscountDate();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 80;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().discountDate();
			}
		};
		this.addColumn(discountDateColumn);

		AnchorEditColumn<ClientTransactionReceivePayment> cashDiscountColumn = new AnchorEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openCashDiscountDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsString(row.getCashDiscount());
			}

			@Override
			public int getWidth() {
				return 83;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().cashDiscount();
			}
		};
		this.addColumn(cashDiscountColumn);

		AnchorEditColumn<ClientTransactionReceivePayment> writeOffColumn = new AnchorEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openWriteOffDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsString(row.getWriteOff());
			}

			@Override
			public int getWidth() {
				return 80;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().writeOff();
			}
		};
		this.addColumn(writeOffColumn);

		AnchorEditColumn<ClientTransactionReceivePayment> appliedCreditsColumn = new AnchorEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openCreditsDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsString(row.getAppliedCredits());
			}

			@Override
			public int getWidth() {
				return 85;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().appliedCredits();
			}
		};
		this.addColumn(appliedCreditsColumn);

		TextEditColumn<ClientTransactionReceivePayment> paymentColumn = new AmountColumn<ClientTransactionReceivePayment>() {

			@Override
			protected String getColumnName() {
				return Accounter.constants().payment();
			}

			@Override
			protected double getAmount(ClientTransactionReceivePayment row) {
				return row.getPayment();
			}

			@Override
			protected void setAmount(ClientTransactionReceivePayment item,
					double value) {
				double amt, originalPayment;
				try {
					originalPayment = item.getPayment();
					amt = value;
					if (!isSelected(item)) {
						item.setPayment(item.getAmountDue());
						onSelectionChanged(item, true);
					}
					item.setPayment(amt);
					updateAmountDue(item);

					double totalValue = item.getCashDiscount()
							+ item.getWriteOff() + item.getAppliedCredits()
							+ item.getPayment();
					if (AccounterValidator.isValidReceive_Payment(item
							.getAmountDue(), totalValue, Accounter.constants()
							.receivePaymentExcessDue())) {
						recalculateGridAmounts();
						updateTotalPayment(0.0);
					} else {
						item.setPayment(originalPayment);
					}
					update(item);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		this.addColumn(paymentColumn);
	}

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

	private double getTotalValue(ClientTransactionReceivePayment payment) {
		double totalValue = payment.getCashDiscount() + payment.getWriteOff()
				+ payment.getAppliedCredits() + payment.getPayment();
		return totalValue;
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
								gotCreditsAndPayments = true;
							}

						});

	}

	public void openCashDiscountDialog(
			final ClientTransactionReceivePayment selectedObject) {
		cashDiscountDialog = new CashDiscountDialog(canEdit,
				selectedObject.getCashDiscount(),
				getCashDiscountAccount(selectedObject));
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
						if (validatePaymentValue(selectedObject)) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setCashDiscount(0.0D);
							updatePayment(selectedObject);
						}

						update(selectedObject);
					} else
						return false;
				}
				return true;
			}
		});
		cashDiscountDialog.show();
	}

	private ClientAccount getCashDiscountAccount(
			ClientTransactionReceivePayment selectedObject) {
		ClientAccount cashDiscountAccount = this.company
				.getAccount(selectedObject.getDiscountAccount());
		return cashDiscountAccount;
	}

	private ClientAccount getWriteOffAccount(
			ClientTransactionReceivePayment selectedObject) {
		ClientAccount writeOffAccount = this.company.getAccount(selectedObject
				.getWriteOffAccount());
		return writeOffAccount;
	}

	public void openCreditsDialog(
			final ClientTransactionReceivePayment selectedObject) {
		if (gotCreditsAndPayments) {
			if (creditsAndPaymentsDialiog == null) {
				for (ClientCreditsAndPayments rec : updatedCustomerCreditsAndPayments) {
					rec.setActualAmt(rec.getBalance());
					rec.setRemaoningBalance(rec.getBalance());
				}
				creditsAndPaymentsDialiog = new CustomerCreditsAndPaymentsDialiog(
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
			creditsAndPaymentsDialiog = new CustomerCreditsAndPaymentsDialiog(
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
						updatePayment(selectedObject);
						update(selectedObject);
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

	protected boolean validatePaymentValue(
			ClientTransactionReceivePayment selectedObject) {
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
		updateValue(payment);
		updateAmountReceived();
	}

	public void openWriteOffDialog(
			final ClientTransactionReceivePayment selectedObject) {
		writeOffDialog = new WriteOffDialog(this.company.getActiveAccounts(),
				selectedObject, canEdit, getWriteOffAccount(selectedObject));
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
						if (validatePaymentValue(selectedObject)) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setWriteOff(0.0D);
							updatePayment(selectedObject);
						}
						update(selectedObject);
					}

				}
				return true;
			}
		});
		writeOffDialog.show();
	}

	public void updateValue(ClientTransactionReceivePayment obj) {
		updateTotalPayment(obj.getPayment());
		obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
		update(obj);
	}

	public abstract void updateTotalPayment(Double payment);

	private void onHeaderCheckBoxClick(boolean isChecked) {
		resetValues();
		if (isChecked) {
			List<ClientTransactionReceivePayment> allRows = getAllRows();
			for (ClientTransactionReceivePayment row : allRows) {
				onSelectionChanged(row, true);
			}
		}
	}

	private void resetValues() {
		for (ClientCreditsAndPayments crdt : updatedCustomerCreditsAndPayments) {
			crdt.setBalance(crdt.getActualAmt());
			crdt.setRemaoningBalance(0);
			crdt.setAmtTouse(0);
		}
		for (ClientTransactionReceivePayment obj : this.getRecords()) {
			resetValue(obj);
			recalculateGridAmounts();
			if (creditsAndPaymentsDialiog != null
					&& creditsAndPaymentsDialiog.grid.getRecords().size() == 0)
				creditsStack.clear();
			selectedValues.remove((Integer) indexOf(obj));
		}
		creditsAndPaymentsDialiog = null;
		cashDiscountDialog = null;
		writeOffDialog = null;
	}

	protected abstract void recalculateGridAmounts();

	public int indexOf(ClientTransactionReceivePayment selectedObject) {
		return getAllRows().indexOf(selectedObject);
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
	}

	protected abstract void deleteTotalPayment(double d);

	public List<ClientTransactionReceivePayment> getSelectedRecords() {
		return super.getSelectedRecords(0);
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

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}

	public void removeAllRecords() {
		this.clear();
	}

	public void selectRow(int count) {
		onSelectionChanged(getAllRows().get(count), true);
	}

	private void onSelectionChanged(ClientTransactionReceivePayment obj,
			boolean isChecked) {

		int row = indexOf(obj);
		if (isChecked) {
			selectedValues.add(row);
			updatePayment(obj);
		} else {
			selectedValues.remove((Integer) row);
			resetValue(obj);
		}
		update(obj);
		super.checkColumn(row, 0, isChecked);
		updateAmountReceived();
		recalculateGridAmounts();
	}

	private void updateAmountReceived() {
		double toBeSetAmount = 0.0;
		for (ClientTransactionReceivePayment receivePayment : getSelectedRecords()) {
			toBeSetAmount += receivePayment.getPayment();
		}
		setAmountRecieved(toBeSetAmount);
	}

	protected abstract void setAmountRecieved(double toBeSetAmount);

	public void addLoadingImagePanel() {
		// TODO Auto-generated method stub

	}

	public void addEmptyMessage(String noRecordsToShow) {
		// TODO Auto-generated method stub

	}

	public List<ClientTransactionReceivePayment> getRecords() {
		return getAllRows();
	}

	public boolean isSelected(ClientTransactionReceivePayment trprecord) {
		return super.isChecked(trprecord, 0);
	}
}
