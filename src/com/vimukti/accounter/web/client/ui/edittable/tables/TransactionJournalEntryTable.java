package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AccountDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.CustomerDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.DateColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TaxCodeTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.VendorDropDownTable;

public abstract class TransactionJournalEntryTable extends
		EditTable<ClientEntry> {

	/*
	 * holds all records debit n credit totals
	 */
	private double creditTotal;
	private double debitTotal;

	public TransactionJournalEntryTable() {
		initColumns();

	}

	private void initColumns() {
		TextEditColumn<ClientEntry> voucherNumber = new TextEditColumn<ClientEntry>() {

			@Override
			protected String getValue(ClientEntry row) {
				return row.getVoucherNumber();
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
				return Accounter.constants().voucherNo();
			}

			@Override
			protected void setValue(ClientEntry row, String value) {
				row.setVoucherNumber(value);
			}
		};
		this.addColumn(voucherNumber);

		DateColumn<ClientEntry> dateColumn = new DateColumn<ClientEntry>() {

			@Override
			protected ClientFinanceDate getValue(ClientEntry row) {
				return new ClientFinanceDate(row.getEntryDate());
			}

			@Override
			public int getWidth() {
				return 132;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().date();
			}

			@Override
			protected void setValue(ClientEntry row, ClientFinanceDate value) {
				row.setEntryDate(value.getDate());
			}
		};
		this.addColumn(dateColumn);

		final AccountDropDownTable accountDropDownTable = new AccountDropDownTable(
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				});

		final CustomerDropDownTable customerDropDownTable = new CustomerDropDownTable(
				new ListFilter<ClientCustomer>() {

					@Override
					public boolean filter(ClientCustomer e) {
						return true;
					}
				});

		final VendorDropDownTable vendorDropDownTable = new VendorDropDownTable(
				new ListFilter<ClientVendor>() {

					@Override
					public boolean filter(ClientVendor e) {
						return true;
					}
				});

		final TaxCodeTable taxCodeTable = new TaxCodeTable();

		this.addColumn(new ComboColumn<ClientEntry, IAccounterCore>() {

			@Override
			protected IAccounterCore getValue(ClientEntry obj) {
				switch (obj.getType()) {
				case ClientEntry.TYPE_FINANCIAL_ACCOUNT:
					return Accounter.getCompany().getAccount(obj.getAccount());
				case ClientEntry.TYPE_VENDOR:
					return Accounter.getCompany().getVendor(obj.getVendor());
				case ClientEntry.TYPE_CUSTOMER:
					return Accounter.getCompany()
							.getCustomer(obj.getCustomer());
				case ClientEntry.TYPE_VAT:
					return Accounter.getCompany().getTAXCode(obj.getTaxCode());
				}
				return null;
			}

			@Override
			protected void setValue(ClientEntry row, IAccounterCore newValue) {
				switch (row.getType()) {
				case ClientEntry.TYPE_FINANCIAL_ACCOUNT:
					row.setAccount(newValue.getID());
					break;
				case ClientEntry.TYPE_VENDOR:
					row.setVendor(newValue.getID());
					break;
				case ClientEntry.TYPE_CUSTOMER:
					row.setCustomer(newValue.getID());
					break;
				case ClientEntry.TYPE_VAT:
					row.setTaxCode(newValue.getID());
					break;
				}
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public AbstractDropDownTable getDisplayTable(ClientEntry row) {
				switch (row.getType()) {
				case ClientEntry.TYPE_FINANCIAL_ACCOUNT:
					return accountDropDownTable;
				case ClientEntry.TYPE_VENDOR:
					return vendorDropDownTable;
				case ClientEntry.TYPE_CUSTOMER:
					return customerDropDownTable;
				case ClientEntry.TYPE_VAT:
					return taxCodeTable;
				}
				return null;
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

		TextEditColumn<ClientEntry> memoColumn = new TextEditColumn<ClientEntry>() {

			@Override
			protected String getValue(ClientEntry row) {
				return row.getMemo();
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
			protected void setValue(ClientEntry row, String value) {
				row.setMemo(value);
			}
		};
		this.addColumn(memoColumn);

		AmountColumn<ClientEntry> debitColumn = new AmountColumn<ClientEntry>() {

			@Override
			protected double getAmount(ClientEntry row) {
				return row.getDebit();
			}

			@Override
			protected void setAmount(ClientEntry row, double value) {
				row.setDebit(value);
				row.setCredit(0);
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

		AmountColumn<ClientEntry> creditColumn = new AmountColumn<ClientEntry>() {

			@Override
			protected double getAmount(ClientEntry row) {
				return row.getCredit();
			}

			@Override
			protected void setAmount(ClientEntry row, double value) {
				row.setCredit(value);
				row.setDebit(0);
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

		this.addColumn(new DeleteColumn<ClientEntry>());
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();

		// Validates account name
		List<ClientEntry> entrylist = this.getAllRows();
		for (ClientEntry entry : entrylist) {
			if ((entry.getType() == ClientEntry.TYPE_FINANCIAL_ACCOUNT && entry
					.getAccount() == 0)
					|| (entry.getType() == ClientEntry.TYPE_VENDOR && entry
							.getVendor() == 0)
					|| (entry.getType() == ClientEntry.TYPE_CUSTOMER && entry
							.getCustomer() == 0)
					|| entry.getType() == ClientEntry.TYPE_VAT
					&& entry.getTaxCode() == 0) {
				result.addError(
						this,
						Accounter.messages().pleaseEnter(
								getTypeAsString(entry, entry.getType())));
			}
		}
		return result;
	}

	private String getTypeAsString(ClientEntry entry, int type) {
		switch (type) {
		case ClientEntry.TYPE_FINANCIAL_ACCOUNT:
			return Global.get().account();
		case ClientEntry.TYPE_CUSTOMER:
			return Global.get().customer();
		case ClientEntry.TYPE_VENDOR:
			return Global.get().vendor();
			// case ClientEntry.TYPE_VAT:
			// return "VAT";
		}
		return null;
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
		for (ClientEntry rec : getAllRows()) {
			creditTotal += rec.getCredit();
			debitTotal += rec.getDebit();
		}
		updateNonEditableItems();
	}

	public abstract void updateNonEditableItems();

	@Override
	public void setAllRows(List<ClientEntry> rows) {
		super.setAllRows(rows);
		refreshTotals();
	}
}
