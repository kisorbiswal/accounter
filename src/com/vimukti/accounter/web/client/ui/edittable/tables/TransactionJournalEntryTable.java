package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AccountDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class TransactionJournalEntryTable extends
		EditTable<ClientTransactionItem> {

	/*
	 * holds all records debit n credit totals
	 */
	private double creditTotal;
	private double debitTotal;
	private ICurrencyProvider currencyProvider;

	public TransactionJournalEntryTable(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	protected void initColumns() {
		// TextEditColumn<ClientEntry> voucherNumber = new
		// TextEditColumn<ClientEntry>() {
		//
		// @Override
		// protected String getValue(ClientEntry row) {
		// return row.getVoucherNumber();
		// }
		//
		// @Override
		// protected boolean isEnable() {
		// return false;
		// }
		//
		// @Override
		// public int getWidth() {
		// return 85;
		// }
		//
		// @Override
		// protected String getColumnName() {
		// return Accounter.constants().voucherNo();
		// }
		//
		// @Override
		// protected void setValue(ClientEntry row, String value) {
		// row.setVoucherNumber(value);
		// }
		// };
		// this.addColumn(voucherNumber);

		// DateColumn<ClientTransactionItem> dateColumn = new
		// DateColumn<ClientTransactionItem>() {
		//
		// @Override
		// protected ClientFinanceDate getValue(ClientTransactionItem row) {
		// return new ClientFinanceDate(row.getEntryDate());
		// }
		//
		// @Override
		// public int getWidth() {
		// return 132;
		// }
		//
		// @Override
		// protected String getColumnName() {
		// return Accounter.constants().date();
		// }
		//
		// @Override
		// protected void setValue(ClientEntry row, ClientFinanceDate value) {
		// row.setEntryDate(value.getDate());
		// }
		// };
		// this.addColumn(dateColumn);

		final AccountDropDownTable accountDropDownTable = new AccountDropDownTable(
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				}, null);

		this.addColumn(new ComboColumn<ClientTransactionItem, IAccounterCore>() {

			@Override
			protected IAccounterCore getValue(ClientTransactionItem obj) {
				return Accounter.getCompany().getAccount(obj.getAccount());
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					IAccounterCore newValue) {
				row.setAccount(newValue.getID());
			}

			@SuppressWarnings({ "unchecked" })
			@Override
			public AbstractDropDownTable getDisplayTable(
					ClientTransactionItem row) {
				return accountDropDownTable;
			}

			@Override
			protected String getColumnName() {
				return Global.get().Account();
			}

			@Override
			public int getWidth() {
				return 180;
			}
		});

		TextEditColumn<ClientTransactionItem> memoColumn = new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				return row.getDescription();
			}

			@Override
			public int getWidth() {
				return -1;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().memo();
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {
				row.setDescription(value);
			}
		};
		this.addColumn(memoColumn);

		AmountColumn<ClientTransactionItem> debitColumn = new AmountColumn<ClientTransactionItem>(
				currencyProvider) {

			@Override
			protected double getAmount(ClientTransactionItem row) {
				if (DecimalUtil.isGreaterThan(row.getLineTotal(), 0)) {
					return row.getLineTotal();
				}
				return 0;
			}

			@Override
			protected void setAmount(ClientTransactionItem row, double value) {
				row.setLineTotal(value);
				refreshTotals();
				getTable().update(row);
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().debit();
			}

			@Override
			public int getWidth() {
				return 110;
			}
		};
		this.addColumn(debitColumn);

		AmountColumn<ClientTransactionItem> creditColumn = new AmountColumn<ClientTransactionItem>(
				currencyProvider) {

			@Override
			protected double getAmount(ClientTransactionItem row) {
				if (DecimalUtil.isLessThan(row.getLineTotal(), 0)) {
					return -1 * row.getLineTotal();
				}
				return 0;
			}

			@Override
			protected void setAmount(ClientTransactionItem row, double value) {
				row.setLineTotal(-1 * value);
				refreshTotals();
				getTable().update(row);
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().credit();
			}

			@Override
			public int getWidth() {
				return 110;
			}
		};
		this.addColumn(creditColumn);

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();

		if (this.getAllRows().size() == 0) {
			result.addError(
					this,
					Accounter.messages().thereisNoRecordsTosave(
							Accounter.constants().journalEntry()));
		}
		// Validates account name
		List<ClientTransactionItem> entrylist = this.getAllRows();
		for (ClientTransactionItem entry : entrylist) {
			long accountId = entry.getAccount();
			for (ClientTransactionItem entry2 : entrylist) {
				long accountId2 = entry2.getAccount();
				if (!entry.equals(entry2) && accountId == accountId2) {
					result.addError(this, Accounter.constants()
							.shouldntSelectSameAccountInMultipleEntries());
				}
			}
		}
		for (ClientTransactionItem entry : entrylist) {
			if (entry.getAccount() == 0) {
				result.addError(this,
						Accounter.messages()
								.pleaseEnter(Global.get().account()));
			}
		}
		return result;
	}

	public boolean isValidTotal() {
		if (getTotalCredittotal() == getTotalDebittotal())
			return true;
		else {
			return false;
		}
	}

	public double getTotalCredittotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
	}

	public double getTotalDebittotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	/**
	 * This method is used to refresh the total with existing added entries.
	 */
	public void refreshTotals() {
		creditTotal = 0;
		debitTotal = 0;
		for (ClientTransactionItem rec : getAllRows()) {
			if (DecimalUtil.isGreaterThan(rec.getLineTotal(), 0)) {
				debitTotal += rec.getLineTotal();
			} else {
				creditTotal += (-1 * rec.getLineTotal());
			}
		}
		updateNonEditableItems();
	}

	public abstract void updateNonEditableItems();

	@Override
	public void setAllRows(List<ClientTransactionItem> rows) {
		for (ClientTransactionItem item : rows) {
			item.setID(0);
			item.taxRateCalculationEntriesList.clear();
		}
		super.setAllRows(rows);
		refreshTotals();
	}

	@Override
	public void delete(ClientTransactionItem row) {
		super.delete(row);
		refreshTotals();
	}

}
