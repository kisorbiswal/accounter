package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionPayEmployee;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public abstract class PayEmployeeTable extends
		EditTable<ClientTransactionPayEmployee> {
	private final boolean canEdit;
	private final List<Integer> selectedValues = new ArrayList<Integer>();

	public boolean isAlreadyOpened;
	private final ICurrencyProvider currencyProvider;
	private long transactionId;

	public PayEmployeeTable(boolean canEdit, ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		this.canEdit = canEdit;
	}

	public void setTranactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionPayEmployee>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionPayEmployee row) {
				// if (!getPreferences().isCreditsApplyAutomaticEnable()) {
				// row.setPayment(row.getAmountDue() - row.getAppliedCredits());
				// }
				onSelectionChanged(row, value);
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientTransactionPayEmployee> context) {
				super.render(widget, context);
				if (isInViewMode()) {
					((CheckBox) widget).setValue(true);
				}
			}
		});

		if (canEdit) {
			TextEditColumn<ClientTransactionPayEmployee> dueDate = new TextEditColumn<ClientTransactionPayEmployee>() {

				@Override
				protected String getValue(ClientTransactionPayEmployee row) {
					return DateUtills.getDateAsString(new ClientFinanceDate(row
							.getDate()).getDateAsObject());
				}

				@Override
				protected void setValue(ClientTransactionPayEmployee row,
						String value) {
					// No need
				}

				@Override
				public int getWidth() {
					return 90;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.dueDate();
				}
			};
			this.addColumn(dueDate);
		}

		TextEditColumn<ClientTransactionPayEmployee> billNo = new TextEditColumn<ClientTransactionPayEmployee>() {

			@Override
			protected String getValue(ClientTransactionPayEmployee row) {
				return row.getPayRunNumber();
			}

			@Override
			protected void setValue(ClientTransactionPayEmployee row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.billNo();
			}
		};
		this.addColumn(billNo);

		if (canEdit) {
			this.addColumn(new AmountColumn<ClientTransactionPayEmployee>(
					currencyProvider, false) {

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return getColumnNameWithCurrency(messages.originalAmount());
				}

				@Override
				protected Double getAmount(ClientTransactionPayEmployee row) {
					return row.getOriginalAmount();
				}

				@Override
				protected void setAmount(ClientTransactionPayEmployee row,
						Double value) {

				}
			});

			this.addColumn(new AmountColumn<ClientTransactionPayEmployee>(
					currencyProvider, false) {

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return getColumnNameWithCurrency(messages.amountDue());
				}

				@Override
				protected Double getAmount(ClientTransactionPayEmployee row) {
					return row.getAmountDue();
				}

				@Override
				protected void setAmount(ClientTransactionPayEmployee row,
						Double value) {

				}
			});
		} else {
			this.addColumn(new AmountColumn<ClientTransactionPayEmployee>(
					currencyProvider, false) {

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				protected String getColumnName() {
					return messages.billAmount();
				}

				@Override
				protected Double getAmount(ClientTransactionPayEmployee row) {
					return row.getOriginalAmount();
				}

				@Override
				protected void setAmount(ClientTransactionPayEmployee row,
						Double value) {
					//

				}
			});
		}

		if (canEdit) {

			this.addColumn(new AmountColumn<ClientTransactionPayEmployee>(
					currencyProvider, true) {

				@Override
				public void render(IsWidget widget,
						RenderContext<ClientTransactionPayEmployee> context) {
					TextBoxBase box = (TextBoxBase) widget;
					String value = getValue(context.getRow());
					box.setEnabled(isEnable(context.getRow())
							&& !context.isDesable());
					box.setText(value);
				}

				private boolean isEnable(ClientTransactionPayEmployee row) {
					return selectedValues.contains(indexOf(row));
				}

				@Override
				protected String getColumnName() {
					return getColumnNameWithCurrency(messages.payments());
				}

				@Override
				public int getWidth() {
					return 110;
				}

				@Override
				protected Double getAmount(ClientTransactionPayEmployee row) {
					return row.getPayment();
				}

				@Override
				protected void setAmount(ClientTransactionPayEmployee row,
						Double value) {
					double previous = row.getPayment();
					row.setPayment(value);
					double totalValue = getTotalValue(row);
					if (DecimalUtil.isGreaterThan(totalValue,
							row.getAmountDue())) {
						Accounter.showError(messages
								.totalPaymentNotExceedDueForSelectedRecords());
						row.setPayment(previous);
					}
					updateValue(row);
					adjustAmountAndEndingBalance();
					updateFootervalues(row, canEdit);
					update(row);
				}
			});

		}

		if (!canEdit) {
			this.addColumn(new TextEditColumn<ClientTransactionPayEmployee>() {

				@Override
				protected String getValue(ClientTransactionPayEmployee row) {
					return "";
				}

				@Override
				protected void setValue(ClientTransactionPayEmployee row,
						String value) {
					// No Need
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.referenceNo();
				}
			});
			this.addColumn(new AmountColumn<ClientTransactionPayEmployee>(
					currencyProvider, false) {

				@Override
				protected String getValue(ClientTransactionPayEmployee row) {
					return String.valueOf(row.getPayment());
					// return DataUtils.getAmountAsStringInPrimaryCurrency(row
					// .getPayment());

				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.amountPaid();
				}

				@Override
				protected Double getAmount(ClientTransactionPayEmployee row) {
					return row.getPayment();
				}

				@Override
				protected void setAmount(ClientTransactionPayEmployee row,
						Double value) {

				}
			});
		}

	}

	protected void selectAllRows(boolean value) {
		List<ClientTransactionPayEmployee> allRows = getAllRows();
		for (ClientTransactionPayEmployee row : allRows) {
			row.setPayment(row.getAmountDue());
			onSelectionChanged(row, value);
		}
	}

	private void onSelectionChanged(ClientTransactionPayEmployee obj,
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
	public void updateValue(ClientTransactionPayEmployee obj) {
		// obj.setPayment(obj.getAmountDue());
		updateTotalPayment(obj);
		adjustAmountAndEndingBalance();
		update(obj);
	}

	protected abstract void updateTotalPayment(ClientTransactionPayEmployee obj);

	public int indexOf(ClientTransactionPayEmployee selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public List<ClientTransactionPayEmployee> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}

	protected abstract void adjustPaymentValue(
			ClientTransactionPayEmployee selectedObject);

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	protected abstract void updateFootervalues(
			ClientTransactionPayEmployee row, boolean canEdit);

	protected abstract void adjustAmountAndEndingBalance();

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getSelectedRecords().size() == 0) {
			result.addError(this,
					messages.pleaseSelectAnyOneOfTheTransactions());
		}

		// validates receive payment amount excesses due amount or not
		for (ClientTransactionPayEmployee transactionPayBill : this
				.getSelectedRecords()) {

			double totalValue = getTotalValue(transactionPayBill);
			if (DecimalUtil.isEquals(totalValue, 0)) {
				result.addError(this,
						messages.totalPaymentNotZeroForSelectedRecords());
			} else if (DecimalUtil.isGreaterThan(totalValue,
					transactionPayBill.getAmountDue())) {
				result.addError(this,
						messages.totalPaymentNotExceedDueForSelectedRecords());
			}
		}

		return result;
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

	public void resetValues() {
		/* Revert all credits to its original state */
		for (ClientTransactionPayEmployee obj : this.getAllRows()) {

			obj.setPayment(0.0d);
			selectedValues.remove((Integer) indexOf(obj));
			update(obj);
		}
		updateFootervalues(null, canEdit);
		resetTotlas();
	}

	protected abstract void resetTotlas();

	private void resetValue(ClientTransactionPayEmployee obj) {

		deleteTotalPayment(obj);
		obj.setPayment(0.0d);
		update(obj);
		adjustAmountAndEndingBalance();
		updateFootervalues(obj, canEdit);
	}

	protected abstract void deleteTotalPayment(ClientTransactionPayEmployee obj);

	private double getTotalValue(ClientTransactionPayEmployee payment) {
		double totalValue = payment.getPayment();
		return totalValue;
	}

	public void setRecords(List<ClientTransactionPayEmployee> records) {
		setAllRows(records);
	}

	public void removeAllRecords() {
		clear();
	}

	public void addLoadingImagePanel() {
		// TODO Auto-generated method stub

	}

	public void selectRow(int count) {
		onSelectionChanged(getRecords().get(count), true);
	}

	public List<ClientTransactionPayEmployee> getRecords() {
		return getAllRows();
	}

	@Override
	protected abstract boolean isInViewMode();

	private void updatesAmounts(ClientTransactionPayEmployee bill) {
		updateValue(bill);
	}

	public void updateAmountsFromGUI() {
		for (ClientTransactionPayEmployee item : this.getAllRows()) {
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}

}
